package tool;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author xiezhidong
 * @date 2024/8/19 11:26
 */
public class DateUtils {

    public final static DateTimeFormatter COMMON_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public final static DateTimeFormatter COMMON_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public final static DateTimeFormatter COMMON_TIME = DateTimeFormatter.ofPattern("HH:mm:ss");


    public final static DateTimeFormatter HH_MM = DateTimeFormatter.ofPattern("HH:mm");




    public static String currentDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return localDateTimeFormat(localDateTime, COMMON_DATE_TIME);
    }


    public static String currentDate() {
        LocalDate localDate = LocalDate.now();
        return localDateFormat(localDate, COMMON_DATE);
    }

    public static String currentTime() {
        LocalTime localTime = LocalTime.now();
        return localTimeFormat(localTime, COMMON_TIME);
    }

    public static String currentHourMinute() {
        LocalTime localTime = LocalTime.now();
        return localTimeFormat(localTime, HH_MM);
    }



    public static String localDateTimeFormat(LocalDateTime localDateTime,DateTimeFormatter formatter) {
        if (localDateTime == null || formatter == null) {
            return null;
        }
        return localDateTime.format(formatter);
    }

    public static String localDateFormat(LocalDate localDate, DateTimeFormatter formatter) {
        if (localDate == null || formatter == null) {
            return null;
        }
        return localDate.format(formatter);
    }

    public static String localTimeFormat(LocalTime localTime, DateTimeFormatter formatter) {
        if (localTime == null || formatter == null) {
            return null;
        }
        return localTime.format(formatter);
    }
}
