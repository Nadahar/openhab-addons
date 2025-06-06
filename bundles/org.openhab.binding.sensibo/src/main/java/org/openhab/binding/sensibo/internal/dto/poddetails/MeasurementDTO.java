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
package org.openhab.binding.sensibo.internal.dto.poddetails;

import java.time.ZonedDateTime;

import com.google.gson.annotations.SerializedName;

/**
 * All classes in the ..binding.sensibo.dto are data transfer classes used by the GSON mapper. This class reflects a
 * part of a request/response data structure.
 *
 * @author Arne Seime - Initial contribution.
 */
public class MeasurementDTO {
    public Double batteryVoltage;
    public Double temperature;
    public Double humidity;
    @SerializedName("rssi")
    public Integer wifiSignalStrength;
    @SerializedName("time")
    public TimeWrapperDTO measurementTimestamp;

    public ZonedDateTime getMeasurementTimestamp() {
        if (measurementTimestamp != null) {
            return measurementTimestamp.time;
        }
        return null;
    }
}
