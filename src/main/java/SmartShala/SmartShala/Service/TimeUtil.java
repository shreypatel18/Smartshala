package SmartShala.SmartShala.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Instant convertToInstant(String dateTimeString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, FORMATTER);
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant(); // Convert to Instant using system time zone
    }

    // Add minutes to a given date-time string and return updated time as a string
    public static String addMinutesToDateTime(String dateTimeString, int minutesToAdd) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, FORMATTER);
        LocalDateTime newDateTime = dateTime.plusMinutes(minutesToAdd);
        return newDateTime.format(FORMATTER);
    }


    public static boolean isAfterCurrentTime(String dateTimeString) {
        LocalDateTime givenDateTime = LocalDateTime.parse(dateTimeString, FORMATTER);
        return givenDateTime.isAfter(LocalDateTime.now());
    }

}
