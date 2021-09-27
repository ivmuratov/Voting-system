package com.votingsystem.springboot.util;

import lombok.experimental.UtilityClass;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@UtilityClass
public class DateTimeUtil {
    public static final LocalTime DEADLINE_FOR_VOTE_CHANGE = LocalTime.of(11, 0, 0);

    private static final ZoneId zoneId = ZoneId.systemDefault();

    private static Clock clock = Clock.systemDefaultZone();

    private static LocalTime now() {
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

    public static boolean isDeadlineForVoteChangeHasCome() {
        return now().compareTo(DEADLINE_FOR_VOTE_CHANGE) >= 0;
    }
}