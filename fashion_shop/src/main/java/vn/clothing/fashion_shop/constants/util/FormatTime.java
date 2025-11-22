package vn.clothing.fashion_shop.constants.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class FormatTime {
    public static String formatDateTime(Instant instant) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return fmt.format(instant);
    }
    public static String formatDate(Instant instant) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            .withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        return fmt.format(instant);
    }
}
