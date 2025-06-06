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
package org.openhab.binding.fmiweather.internal;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.fmiweather.internal.client.Location;
import org.openhab.binding.fmiweather.internal.client.exception.FMIExceptionReportException;
import org.openhab.binding.fmiweather.internal.client.exception.FMIUnexpectedResponseException;
import org.xml.sax.SAXException;

/**
 * Test cases for {@link org.openhab.binding.fmiweather.internal.client.Client#parseStations}
 *
 * @author Sami Salonen - Initial contribution
 */
@NonNullByDefault
public class ParsingStationsTest extends AbstractFMIResponseParsingTest {

    private static final String STATIONS_XML = "stations.xml";

    @Test
    public void testParseStations() throws FMIExceptionReportException, FMIUnexpectedResponseException, SAXException,
            IOException, XPathExpressionException {
        Set<Location> stations = client.parseStations(readTestResourceUtf8(STATIONS_XML));
        assertNotNull(stations);
        assertThat(stations.size(), is(3));
        assertThat(stations,
                hasItems(
                        deeplyEqualTo(new Location("Porvoo Kilpilahti satama", "100683", new BigDecimal("60.303725"),
                                new BigDecimal("25.549164"))),
                        deeplyEqualTo(new Location("Parainen Utö", "100908", new BigDecimal("59.779094"),
                                new BigDecimal("21.374788"))),
                        deeplyEqualTo(new Location("Lemland Nyhamn", "100909", new BigDecimal("59.959194"),
                                new BigDecimal("19.953667")))));
    }
}
