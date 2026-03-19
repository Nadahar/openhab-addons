/*
 * Copyright (c) 2010-2026 Contributors to the openHAB project
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
package org.openhab.binding.shelly.internal.config;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link ShellyThingConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Markus Michels - Initial contribution
 */
@NonNullByDefault
public class ShellyThingConfiguration {
    // All access must be guarded by "this"
    /** IP address of the device */
    private String deviceIp = "";

    // All access must be guarded by "this"
    /** IP address or MAC address for BLU devices */
    private String deviceAddress = "";

    // All access must be guarded by "this"
    /** userid for HTTP basic auth */
    private String userId = "";

    // All access must be guarded by "this"
    /** password for HTTP basic auth */
    private String password = "";

    // All access must be guarded by "this"
    /** schedule interval for the update job */
    private int updateInterval = 60;

    // All access must be guarded by "this"
    /** threshold for battery value */
    private int lowBattery = 15;

    // All access must be guarded by "this"
    /** {@code true}: turn on device if brightness > 0 is set */
    private boolean brightnessAutoOn = true;

    // All access must be guarded by "this"
    /** Roller position favorite when control channel receives ON, 0=none */
    private int favoriteUP = 0;

    // All access must be guarded by "this"
    /** Roller position favorite when control channel receives ON, 0=none */
    private int favoriteDOWN = 0;

    // All access must be guarded by "this"
    /** {@code true}: register for Relay btn_xxx events */
    private boolean eventsButton = false;

    // All access must be guarded by "this"
    /** {@code true}: register for device out_xxx events */
    private boolean eventsSwitch = true;

    // All access must be guarded by "this"
    /** {@code true}: register for short/long push events */
    private boolean eventsPush = true;

    // All access must be guarded by "this"
    /** {@code true}: register for short/long push events */
    private boolean eventsRoller = true;

    // All access must be guarded by "this"
    /** {@code true}: register for sensor events */
    private boolean eventsSensorReport = true;

    // All access must be guarded by "this"
    /** {@code true}: use CoIoT events (based on COAP) */
    private boolean eventsCoIoT = false;

    // All access must be guarded by "this"
    /** local IP addresses used to create callback URL */
    private String localIp = "";

    // All access must be guarded by "this"
    private String localPort = "8080";

    // All access must be guarded by "this"
    private String realm = "";

    // All access must be guarded by "this"
    private Boolean enableBluGateway = false;

    // All access must be guarded by "this"
    private Boolean enableRangeExtender = true;

    public synchronized String getDeviceIp() {
        return deviceIp;
    }

    public synchronized void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public synchronized String getDeviceAddress() {
        return deviceAddress;
    }

    public synchronized void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public synchronized String getUserId() {
        return userId;
    }

    public synchronized void setUserId(String userId) {
        this.userId = userId;
    }

    public synchronized String getPassword() {
        return password;
    }

    public synchronized void setPassword(String password) {
        this.password = password;
    }

    public synchronized int getUpdateInterval() {
        return updateInterval;
    }

    public synchronized void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public synchronized int getLowBattery() {
        return lowBattery;
    }

    public synchronized void setLowBattery(int lowBattery) {
        this.lowBattery = lowBattery;
    }

    public synchronized boolean isBrightnessAutoOn() {
        return brightnessAutoOn;
    }

    public synchronized void setBrightnessAutoOn(boolean brightnessAutoOn) {
        this.brightnessAutoOn = brightnessAutoOn;
    }

    public synchronized int getFavoriteUP() {
        return favoriteUP;
    }

    public synchronized void setFavoriteUP(int favoriteUP) {
        this.favoriteUP = favoriteUP;
    }

    public synchronized int getFavoriteDOWN() {
        return favoriteDOWN;
    }

    public synchronized void setFavoriteDOWN(int favoriteDOWN) {
        this.favoriteDOWN = favoriteDOWN;
    }

    public synchronized boolean isEventsButton() {
        return eventsButton;
    }

    public synchronized void setEventsButton(boolean eventsButton) {
        this.eventsButton = eventsButton;
    }

    public synchronized boolean isEventsSwitch() {
        return eventsSwitch;
    }

    public synchronized void setEventsSwitch(boolean eventsSwitch) {
        this.eventsSwitch = eventsSwitch;
    }

    public synchronized boolean isEventsPush() {
        return eventsPush;
    }

    public synchronized void setEventsPush(boolean eventsPush) {
        this.eventsPush = eventsPush;
    }

    public synchronized boolean isEventsRoller() {
        return eventsRoller;
    }

    public synchronized void setEventsRoller(boolean eventsRoller) {
        this.eventsRoller = eventsRoller;
    }

    public synchronized boolean isEventsSensorReport() {
        return eventsSensorReport;
    }

    public synchronized void setEventsSensorReport(boolean eventsSensorReport) {
        this.eventsSensorReport = eventsSensorReport;
    }

    public synchronized boolean isEventsCoIoT() {
        return eventsCoIoT;
    }

    public synchronized void setEventsCoIoT(boolean eventsCoIoT) {
        this.eventsCoIoT = eventsCoIoT;
    }

    public synchronized String getLocalIp() {
        return localIp;
    }

    public synchronized void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public synchronized String getLocalPort() {
        return localPort;
    }

    public synchronized void setLocalPort(String localPort) {
        this.localPort = localPort;
    }

    public synchronized String getRealm() {
        return realm;
    }

    public synchronized void setRealm(String realm) {
        this.realm = realm;
    }

    public synchronized Boolean getEnableBluGateway() {
        return enableBluGateway;
    }

    public synchronized void setEnableBluGateway(Boolean enableBluGateway) {
        this.enableBluGateway = enableBluGateway;
    }

    public synchronized Boolean getEnableRangeExtender() {
        return enableRangeExtender;
    }

    public synchronized void setEnableRangeExtender(Boolean enableRangeExtender) {
        this.enableRangeExtender = enableRangeExtender;
    }

    @Override
    public String toString() {
        return "Device address=" + deviceAddress + ", HTTP user/password=" + userId + "/"
                + (password.isEmpty() ? "<none>" : "***") + ", update interval=" + updateInterval + "\n"
                + "Events: Button: " + eventsButton + ", Switch (on/off): " + eventsSwitch + ", Push: " + eventsPush
                + ", Roller: " + eventsRoller + "Sensor: " + eventsSensorReport + ", CoIoT: " + eventsCoIoT + "\n"
                + "Blu Gateway=" + enableBluGateway + ", Range Extender: " + enableRangeExtender;
    }
}
