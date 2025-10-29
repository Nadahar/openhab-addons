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
package org.openhab.binding.chromecast.internal;

import org.digitalmediaserver.cast.event.CastEvent;
import org.digitalmediaserver.cast.event.CastEvent.CastEventListener;
import org.digitalmediaserver.cast.message.entity.Device;
import org.digitalmediaserver.cast.message.response.DeviceUpdatedResponse;
import org.digitalmediaserver.cast.message.response.MediaStatusResponse;
import org.digitalmediaserver.cast.message.response.ReceiverStatusResponse;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for listening to events from the Chromecast.
 *
 * @author Jason Holmes - Initial contribution
 */
@NonNullByDefault
public class ChromecastEventReceiver implements CastEventListener {
    private final Logger logger = LoggerFactory.getLogger(ChromecastEventReceiver.class);

    private final ChromecastScheduler scheduler;
    private final ChromecastStatusUpdater statusUpdater;

    public ChromecastEventReceiver(ChromecastScheduler scheduler, ChromecastStatusUpdater statusUpdater) {
        this.scheduler = scheduler;
        this.statusUpdater = statusUpdater;
    }

    @Override
    public void onEvent(@NonNullByDefault({}) CastEvent<?> event) {
        switch (event.getEventType()) {
            case CONNECTED:
                Boolean isConnected = (Boolean) event.getData();
                if (isConnected == null || !isConnected) {
                    // scheduler.cancelRefresh();
                    statusUpdater.updateStatus(ThingStatus.OFFLINE);
                    // We might have just had a connection problem, let's try to reconnect.
                    scheduler.scheduleConnect();
                } else {
                    statusUpdater.updateStatus(ThingStatus.ONLINE);
                    // scheduler.scheduleRefresh();
                }
            case CLOSE:
                statusUpdater.updateMediaStatus(null);
                break;
            case MEDIA_STATUS: //TODO: (Nad) Reset media on app change
                MediaStatusResponse mediaStatusResponse = event.getData(MediaStatusResponse.class);
                statusUpdater.updateMediaStatus(mediaStatusResponse == null ? null : mediaStatusResponse.getStatuses()); //TODO: (Nad) When does null actually happen?
                break;
            case RECEIVER_STATUS:
                ReceiverStatusResponse receiverStatusResponse = event.getData(ReceiverStatusResponse.class);
                statusUpdater.processStatusUpdate(receiverStatusResponse == null ? null : receiverStatusResponse.getStatus());
                break;
            case DEVICE_UPDATED:
                DeviceUpdatedResponse deviceUpdatedResponse = event.getData(DeviceUpdatedResponse.class);
                Device device;
                if (deviceUpdatedResponse != null && (device = deviceUpdatedResponse.getDevice()) != null) {
                    statusUpdater.processDeviceUpdate(device);
                }
                break;
            case UNKNOWN:
                logger.debug("Received an 'UNKNOWN' event (class={})", event.getEventType().getDataClass());
                break;
            case APPLICATION_AVAILABILITY:
            case CUSTOM_MESSAGE:
            case DEVICE_ADDED:
            case DEVICE_REMOVED:
            case ERROR_RESPONSE: //TODO: (Nad) This should probably be handled
            case LAUNCH_ERROR:  //TODO: (Nad) This should probably be handled
            case MULTIZONE_STATUS:
            default: // CustomMessageEvent [namespace: urn:x-cast:com.google.youtube.mdx, string payload: {"type":"mdxSessionStatus","data":{"screenId":"v9nc3i4luec213m8po6iabn8hm","deviceId":"20bf4ba4-9f17-4099-b9f3-852314e70471"}}]
                     // CustomMessageEvent [namespace: urn:x-cast:com.google.youtube.mdx, string payload: {"type":"mdxSessionStatus","data":{"screenId":"v9nc3i4luec213m8po6iabn8hm","deviceId":"20bf4ba4-9f17-4099-b9f3-852314e70471"}}]
                logger.debug("Unhandled event type: {} with data {}:", event.getEventType(), event.getData());
                break;
        }
    }
}
