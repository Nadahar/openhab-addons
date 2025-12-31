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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.jupnp.controlpoint.SubscriptionCallback;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.gena.GENASubscription;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.Device;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.meta.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: (Nad) Header + JavaDocs
@NonNullByDefault
public abstract class AbstractSubscriptionCallback extends SubscriptionCallback {

    public final Logger logger = LoggerFactory.getLogger(AbstractSubscriptionCallback.class);

    public AbstractSubscriptionCallback(RemoteService service) {
        super(service);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected void failed(
        @Nullable GENASubscription subscription,
        @Nullable UpnpResponse responseStatus,
        @Nullable Exception exception,
        @Nullable String defaultMsg) {
        if (subscription == null) {
            return;
        }
        if (logger.isDebugEnabled()) {
            Service service = subscription.getService();
            Device device = service == null ? null : service.getDevice();
            if (responseStatus != null) {
                logger.warn(
                    "{} subscription{} failed with HTTP response: {}",
                    service == null ? "unknown" : service.getServiceId().getId(),
                    device == null ? "" : " with " + device.getDetails().getFriendlyName(),
                    responseStatus.getResponseDetails());
            } else if (exception != null) {
                logger.warn(
                    "{} subscription{} failed with error: {}",
                    service == null ? "unknown" : service.getServiceId().getId(),
                    device == null ? "" : " with " + device.getDetails().getFriendlyName(),
                    exception.getMessage());
            } else {
                logger.debug(
                    "{} subscription{} failed with no response",
                    service == null ? "unknown" : service.getServiceId().getId(),
                    device == null ? "" : " with " + device.getDetails().getFriendlyName());
            }
            if (exception != null && logger.isTraceEnabled()) {
                logger.trace("", exception);
            }
        }
        onFailed(subscription);
    }

    /**
     * Override this method to react to a failure to establish a subscription.
     * Logging has already been taken care of, so only implement this method if
     * further action is required.
     *
     * @param subscription The {@link GENASubscription} that failed.
     */
    protected void onFailed(GENASubscription<?> subscription) {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void established(@Nullable GENASubscription subscription) {
        if (subscription == null) {
            return;
        }
        if (logger.isDebugEnabled()) {
            Service service = subscription.getService();
            Device device = service == null ? null : service.getDevice();
            logger.debug(
                "{} subscription established{}, listening for events",
                service == null ? "unknown" : service.getServiceId().getId(),
                device == null ? "" : " with " + device.getDetails().getFriendlyName());
        }
        onEstablished(subscription);
    }

    /**
     * Override this method to react to when a subscription has successfully
     * been established. Logging has already been taken care of, so only
     * implement this method if further action is required.
     *
     * @param subscription The {@link GENASubscription} that has been
     *            established.
     */
    protected void onEstablished(GENASubscription<?> subscription) {
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void ended(
        @Nullable GENASubscription subscription,
        @Nullable CancelReason reason,
        @Nullable UpnpResponse responseStatus) {
        if (subscription == null) {
            return;
        }
        if (logger.isDebugEnabled()) {
            Service service = subscription.getService();
            Device device = service == null ? null : service.getDevice();
            if (reason != null) {
                logger.debug(
                    "{} subscription{} ended because: {}",
                    service == null ? "unknown" : service.getServiceId().getId(),
                    device == null ? "" : " with " + device.getDetails().getFriendlyName(),
                    reason);
            } else {
                logger.debug(
                    "{} subscription{} ended gracefully",
                    service == null ? "unknown" : service.getServiceId().getId(),
                    device == null ? "" : " with " + device.getDetails().getFriendlyName());

            }
        }
        onTerminated(subscription, reason == null || reason == CancelReason.DEVICE_WAS_REMOVED);
    }

    /**
     * Override this method to react to when a subscription has been terminated,
     * either gracefully or because of a problem. Logging has already been taken
     * care of, so only implement this method if further action is required.
     *
     * @param subscription The {@link GENASubscription} that has been
     *            terminated.
     * @param graceful {@code true} if the subscription was terminated
     *            gracefully, {@code false} if it was terminated because of a
     *            problem.
     */
    protected void onTerminated(GENASubscription<?> subscription, boolean graceful) {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void eventReceived(@Nullable GENASubscription subscription) {
        if (subscription == null) {
            return;
        }
        if (logger.isTraceEnabled()) {
            Service service = subscription.getService();
            Device device = service == null ? null : service.getDevice();
            logger.trace(
                "Received {} subscription event with sequence number {}{}",
                service == null ? "unknown" : service.getServiceId().getId(),
                subscription.getCurrentSequence(),
                device == null ? "" : " from " + device.getDetails().getFriendlyName());
        }

        // Manual type check, since JUPnP's generics use has some "holes"
        if (subscription.getService() instanceof RemoteService) {
            handleEvent(subscription);
        } else {
            logger.warn("Received a GENA event of an unsupported type, ignoring event: {}", subscription);
        }
    }

    // Doc: Reception logged, handle..
    protected abstract void handleEvent(GENASubscription<RemoteService> subscription);

    @SuppressWarnings("rawtypes")
    @Override
    public void eventsMissed(
        @Nullable GENASubscription subscription,
        int numberOfMissedEvents) {
        if (subscription == null) {
            return;
        }
        if (logger.isDebugEnabled()) {
            Service service = subscription.getService();
            Device device = service == null ? null : service.getDevice();
            logger.debug(
                "{} {} subscription event{}{} missed",
                numberOfMissedEvents,
                service == null ? "unknown" : service.getServiceId().getId(),
                numberOfMissedEvents == 1 ? "" : "s",
                device == null ? "" : " from " + device.getDetails().getFriendlyName());
        }
    }
}
