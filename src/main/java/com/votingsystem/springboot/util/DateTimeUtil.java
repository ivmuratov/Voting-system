package com.votingsystem.springboot.util;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class DateTimeUtil {
    public static final LocalTime BORDER_TIME_FOR_CANCEL_OF_VOTING = LocalTime.of(10, 59, 59);

    public static boolean isTimeToCancelVote() {
        return LocalTime.now().compareTo(BORDER_TIME_FOR_CANCEL_OF_VOTING) < 0;
    }
}