/*
 * Copyright (c) 2010-2025 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.upnpcontrol.internal.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.jupnp.model.gena.GENASubscription;
import org.jupnp.model.meta.RemoteDevice;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.state.StateVariableValue;
import org.jupnp.model.types.InvalidValueException;
import org.jupnp.model.types.UnsignedIntegerFourBytes;
import org.jupnp.support.lastchange.Event;
import org.jupnp.support.lastchange.EventedValue;
import org.jupnp.support.lastchange.InstanceID;
import org.jupnp.support.lastchange.LastChangeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: (Nad) Header + JavaDocs
@NonNullByDefault
public abstract class AbstractLastChangeSubscriptionCallback extends AbstractSubscriptionCallback {

    public final Logger logger = LoggerFactory.getLogger(AbstractLastChangeSubscriptionCallback.class);

    public AbstractLastChangeSubscriptionCallback(RemoteService service) {
        super(service);
    }

    protected abstract LastChangeParser createLastChangeParser();

    @Override
    protected void handleEvent(@NonNullByDefault({}) GENASubscription<RemoteService> subscription) {
        boolean trace = logger.isTraceEnabled();
        RemoteService service = subscription.getService();
        RemoteDevice device = service == null ? null : service.getDevice();

        Map<String, StateVariableValue<RemoteService>> map;
        StateVariableValue<RemoteService> lastChangeContent = (map = subscription.getCurrentValues()) == null ? null
            : map.get("LastChange");
        if (lastChangeContent == null) {
            logger.debug(
                "Received supposed 'LastChange' event from {} subscription{} without a 'LastChange' entry",
                service == null ? "unknown" : service.getServiceId().getId(),
                device == null ? "" : " with " + device.getDetails().getFriendlyName());
            return;
        }
        Event event = null;
        try {
            String lastChangeString = lastChangeContent.toString();
            if (!lastChangeString.isBlank()) {
                event = createLastChangeParser().parse(lastChangeString);
            }
        } catch (Exception e) {
            logger.warn(
                "Error parsing LastChange event from {} subscription{}: {}",
                service == null ? "unknown" : service.getServiceId().getId(),
                device == null ? "" : " with " + device.getDetails().getFriendlyName(),
                e.getMessage());
            logger.trace("", e);
            return;
        }

        Map<UnsignedIntegerFourBytes, List<EventedValue<?>>> updates = new HashMap<>();
        if (event != null) {
            List<EventedValue<?>> values;
            for (InstanceID instanceId : event.getInstanceIDs()) {
                values = instanceId.getValues();
                if (!values.isEmpty()) {
                    updates.put(instanceId.getId(), values);
                }
                if (trace) {
                    logger.trace(
                        "Processing 'LastChange' event for instanceID {} of {}{}:",
                        instanceId.getId(),
                        service == null ? "unknown" : service.getServiceId().getId(),
                        device == null ? "" : " from " + device.getDetails().getFriendlyName());
                    String valueString;
                    for (EventedValue<?> value : values) {
                        try {
                            valueString = value.toString();
                        } catch (InvalidValueException e) {
                            valueString = e.getMessage();
                        }
                        logger.trace("  EventedValue: {}={}", value.getName(), valueString);
                    }
                }
            }
        }
        if (!updates.isEmpty()) {
            onUpdate(subscription, updates);
        }
    }

    /**
     * Implement this method to handle updates received via this subscription.
     * Logging has already been taken care of, so only implement the actual
     * action required.
     *
     * @param subscription the {@link GENASubscription} that has a received an
     *            update.
     * @param updates a {@link Map} of instance ID's mapped to a {@link List} of
     *            updated values. The {@link List} can be considered immutable
     *            and can be shared without synchronization.
     */
    protected abstract void onUpdate(
        GENASubscription<?> subscription,
        Map<UnsignedIntegerFourBytes, List<EventedValue<?>>> updates);
}
