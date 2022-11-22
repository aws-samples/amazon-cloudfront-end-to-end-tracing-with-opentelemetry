package com.aws.peach.application.support;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DtoUtil {
    public static String formatTimestamp(Instant timestamp) {
        return timestamp == null ? null : DateTimeFormatter.ISO_INSTANT.format(timestamp);
    }
}
