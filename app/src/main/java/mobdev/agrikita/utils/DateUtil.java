package mobdev.agrikita.utils;

import android.os.Build;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static String formatOrderDate(String isoDateString) {
        OffsetDateTime dateTime = OffsetDateTime.parse(isoDateString); // e.g. 2025-05-13T00:29:45+08:00
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a"); // e.g. May 13, 2025 at 12:29 AM
        return dateTime.format(formatter);
    }
}
