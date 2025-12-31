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

import java.util.Set;
import javax.xml.transform.Source;

import org.eclipse.jdt.annotation.NonNull;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.support.lastchange.EventedValue;
import org.jupnp.support.lastchange.LastChangeParser;
import org.jupnp.support.renderingcontrol.lastchange.RenderingControlVariable.Loudness;
import org.jupnp.support.renderingcontrol.lastchange.RenderingControlVariable.Mute;
import org.jupnp.support.renderingcontrol.lastchange.RenderingControlVariable.Volume;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: (Nad) Header + JavaDocs
public abstract class RenderingControlSubscriptionCallback extends AbstractLastChangeSubscriptionCallback {

    public final Logger logger = LoggerFactory.getLogger(RenderingControlSubscriptionCallback.class);

    public static final Set<Class<? extends EventedValue<?>>> HANDLED_PROPERTIES = Set.of(Volume.class, Mute.class, Loudness.class);

    public RenderingControlSubscriptionCallback(RemoteService service) {
        super(service);
    }

    @Override
    @NonNull
    protected LastChangeParser createLastChangeParser() {
        return new RenderingControlLastChangeParser();
    }

    public static class RenderingControlLastChangeParser extends LastChangeParser {

        public static final String NAMESPACE_URI = "urn:schemas-upnp-org:metadata-1-0/RCS/";

        @Override
        protected String getNamespace() {
            return NAMESPACE_URI;
        }

        @Override
        protected Source[] getSchemaSources() {
            return null;
        }

        @Override
        protected Set<Class<? extends EventedValue<?>>> getEventedVariables() {
            return HANDLED_PROPERTIES;
        }
    }
}
