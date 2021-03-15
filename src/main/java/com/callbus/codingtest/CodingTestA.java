package com.callbus.codingtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.IntStream;

public class CodingTestA {


    @Test
    @DisplayName("요일 입력값 유효성 체크 테스트")
    void isValidDayTest() {
        CallBusTimeChecker callBusTimeChecker = new CallBusTimeChecker();
//        callBusTimeChecker.isSunDayToMonday(day, hourOfDay);
    }

    void isValidHourOfDayTest() {

    }

    void isValidRunTimeTest() {

    }

    void isValidSunDayToMonday() {

    }

    void isValidWednesdayStartTime() {

    }
}

class CallBusTimeChecker {
    private static final int[] DAYS = {0, 1, 2, 3, 4, 5, 6};
    private static final IntStream HOUR_OF_DAYS = IntStream.range(0, 23);
    private static final LocalTime START_TIME = LocalTime.of(23, 0);
    private static final LocalTime END_TIME = LocalTime.of(3, 59);
    private static final LocalTime WEDNESDAY_START_TIME = LocalTime.of(22, 0);

    public boolean isServiceTime(int day, int hourOfDay) {
        isValidDay(day);
        isValidHourOfDay(hourOfDay);

        // 일 - 월 시간 체크
        if (isSunDayToMonday(day, hourOfDay)) {
            return false;
        }

        // 수 22시 부터 운행
        if (isWednesdayStartTime(day, hourOfDay)) {
            return true;
        }

        // 운행 시간인지 확인
        return isRunTime(hourOfDay);
    }

    private void isValidDay(final int day) {
        if (Arrays.stream(DAYS).anyMatch(value -> value == day)) {
            return;
        }

        throw new IllegalArgumentException("요일을 잘못입력하였습니다. 0 ~ 6");
    }

    private void isValidHourOfDay(final int hourOfDay) {
        if (HOUR_OF_DAYS.anyMatch(value -> value == hourOfDay)) {
            return;
        }

        throw new IllegalArgumentException("시간을 잘못입력하였습니다. 0 ~ 23");
    }

    public boolean isSunDayToMonday(final int day, final int hourOfDay) {
        final LocalTime inputTime = LocalTime.of(hourOfDay, 0);

        if (day == 6) {
            return !inputTime.isBefore(START_TIME);
        }

        if (day == 0) {
            return !inputTime.isAfter(END_TIME);
        }
        return false;
    }

    private boolean isWednesdayStartTime(final int day, final int hourOfDay) {
        if (day != 2) {
            return false;
        }

        final LocalTime nowLocalTime = LocalTime.of(hourOfDay, 0);
        return nowLocalTime.isBefore(WEDNESDAY_START_TIME) && nowLocalTime.isAfter(END_TIME);
    }

    public boolean isRunTime(final int hourOfDay) {
        final LocalTime nowLocalTime = LocalTime.of(hourOfDay, 0);
        return nowLocalTime.isBefore(START_TIME) && nowLocalTime.isAfter(END_TIME);
    }
}