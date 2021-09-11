package com.votingsystem.springboot.util;

import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@UtilityClass
public class DateTimeUtil {
    public static final LocalTime BORDER_TIME_FOR_CANCEL_OF_VOTING = LocalTime.of(10, 59, 59);

    private static final ZoneId zoneId = ZoneId.systemDefault();

    private static Clock clock = Clock.systemDefaultZone();

    public static LocalTime now() {
        return LocalTime.now(getClock());
    }

    //https://stackoverflow.com/a/29360514
    public static void useFixedClockAt(LocalDateTime date) {
        clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
    }

    public static void useSystemDefaultZoneClock() {
        clock = Clock.systemDefaultZone();
    }

    private static Clock getClock() {
        return clock;
    }

    public static boolean isTimeToCancelVote() {
        return now().compareTo(BORDER_TIME_FOR_CANCEL_OF_VOTING) < 0;
    }
}