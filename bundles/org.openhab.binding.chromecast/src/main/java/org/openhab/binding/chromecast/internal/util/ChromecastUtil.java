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
package org.openhab.binding.chromecast.internal.util;

import java.util.Locale;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Binding utility methods.
 *
 * @author Ravi Nadahar - Initial contribution
 */
@NonNullByDefault
public class ChromecastUtil {

    /**
     * Not to be instantiated
     */
    private ChromecastUtil() {
    }

    /**
     * Returns the index of the last extension separator ({@code .}) that isn't followed by a path separator
     * of the specified path.
     *
     * @param path the {@link String} to examine.
     * @return The index or {@code -1} if none was found.
     */
    public static int getExtensionIndex(@Nullable String path) {
        if (path == null || path.length() < 2) {
            return -1;
        }
        char[] filePathArray = path.toCharArray();
        for (int i = filePathArray.length - 1; i >= 0; i--) {
            switch (filePathArray[i]) {
                case '.':
                    return i == filePathArray.length - 1 ? -1 : i;
                case '/':
                case '\\':
                    return -1;
            }
        }
        return -1;
    }

    /**
     * Returns the extension of the specified path or {@code null} if none is found. The extension is the
     * string that follows the last extension separator ({@code .}) in the string that isn't followed by a
     * path separator.
     *
     * @param path the {@link String} to extract the extension from.
     * @param lowerCase {@code true} to convert the extension to lower case.
     * @return The extension or {@code null}.
     */
    @Nullable
    public static String getExtension(@Nullable String path, boolean lowerCase) {
        if (path == null || path.isBlank()) {
            return null;
        }

        int point = getExtensionIndex(path);
        if (point == -1) {
            return null;
        }

        String extension = path.substring(point + 1).trim();
        return lowerCase ? extension.toLowerCase(Locale.ROOT) : extension;
    }

    /**
     * Returns the filename part of the specified path, with or without extension.
     *
     * @param path the path from which to extract the filename.
     * @param stripExtension {@code true} to strip the extension from returned filename.
     * @return The resulting filename, possibly an empty string.
     */
    public static String getFilename(@Nullable String path, boolean stripExtension) {
        if (path == null || path.isBlank()) {
            return "";
        }
        char[] pathArray = path.toCharArray();
        int idx = -1;
        for (int i = pathArray.length - 1; i >= 0; i--) {
            if (pathArray[i] == '/' || pathArray[i] == '\\') {
                idx = i;
                break;
            }
        }
        if (idx == path.length() - 1) {
            return "";
        }
        String filename = idx < 0 ? path : path.substring(idx + 1);
        if (!stripExtension) {
            return filename;
        }
        idx = getExtensionIndex(filename);
        return idx < 0 ? filename : filename.substring(0, idx);
    }

    /**
     * Tries to infer the MIME type of a resource based on file extension. Known file extensions are limited,
     * so this is in no way an extensive evaluation.
     *
     * @param path the resource path/URL to evaluate.
     * @return The inferred MIME type or {@code null} if none could be inferred.
     */
    @Nullable
    public static String inferMimeType(String path) {
        String extension = getExtension(path, true);
        if (extension == null) {
            return null;
        }

        switch (extension) {
            case "aac":
                return "audio/aac";
            case "apng":
                return "image/apng";
            case "avif":
                return "image/avif";
            case "avi":
                return "video/x-msvideo";
            case "bmp":
                return "image/bmp";
            case "flac":
            case "flc":
                return "audio/flac";
            case "f4v":
            case "flv":
                return "video/x-flv";
            case "gif":
                return "image/gif";
            case "ico":
                return "image/vnd.microsoft.icon";
            case "jpeg":
            case "jpg":
                return "image/jpeg";
            case "mid":
            case "midi":
                return "audio/midi";
            case "mka":
                return "audio/x-matroska";
            case "mkv":
                return "video/x-matroska";
            case "mp3":
                return "audio/mpeg";
            case "m4a":
                return "audio/mp4";
            case "mp4":
                return "video/mp4";
            case "mpg":
            case "mpeg":
                return "video/mpeg";
            case "oga":
            case "opus":
                return "audio/ogg";
            case "ogv":
            case "ogg":
                return "video/ogg";
            case "png":
                return "image/png";
            case "ppm":
                return "image/x-portable-pixmap";
            case "svg":
                return "image/svg+xml";
            case "tif":
            case "tiff":
                return "image/tiff";
            case "ts":
                return "video/mp2t";
            case "wav":
            case "wave":
                return "audio/wav";
            case "weba":
                return "audio/webm";
            case "webm":
                return "video/webm";
            case "webp":
                return "image/webp";
            case "3gp":
                return "video/3gpp";
            case "3g2":
                return "video/3gpp2";
            case "vm":
                return "video/x-ms-wmv";
        }
        return null;
    }
}
