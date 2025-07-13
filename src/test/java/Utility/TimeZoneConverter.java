package Utility;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeZoneConverter {


    public static void main(String[] args) {
        LocalDateTime systemDateTime = LocalDateTime.now();

        ZoneId systemZone = ZoneId.systemDefault();
        ZonedDateTime systemZonedDateTime = systemDateTime.atZone(systemZone);

        // Define the Atlantic Standard Time zone (UTC-04:00)
        ZoneId atlanticTimeZone = ZoneId.of("America/Halifax"); // Use "America/Halifax" for AST

        ZonedDateTime atlanticZonedDateTime = systemZonedDateTime.withZoneSameInstant(atlanticTimeZone);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mma");
        String formattedAtlanticTime = atlanticZonedDateTime.format(formatter).toUpperCase();

        System.out.println("Atlantic Current Date and Time is :" + formattedAtlanticTime);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mma");
        String formattedAtlanticTime1 = atlanticZonedDateTime.format(formatter1).toUpperCase();

        System.out.println("Atlantic Current Date and Time is :" + formattedAtlanticTime1);

    }

}
    



