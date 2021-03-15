package com.callbus.codingtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CodingTestA {

    @Test
    @DisplayName("요일 입력값 유효성 체크 테스트 - 잘못된 요일값 입력")
    void isValidDayTest() {
        // given
        final CallBusTimeChecker callBusTimeChecker = new CallBusTimeChecker();
        // when & then
        final int wrong_day = 7;
        assertThatThrownBy(() -> callBusTimeChecker.isServiceTime(wrong_day, 23))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("요일")
        ;
    }

    @Test
    @DisplayName("요일 입력값 유효성 체크 테스트 - 잘못된 시간값 입력")
    void isValidHourOfDayTest() {
        // given
        final CallBusTimeChecker callBusTimeChecker = new CallBusTimeChecker();
        // when & then
        final int wrong_time = 25;
        assertThatThrownBy(() -> callBusTimeChecker.isServiceTime(0, wrong_time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시간")
        ;
    }

    @ParameterizedTest
    @MethodSource("notRunnableTime")
    @DisplayName("운행시간 확인 테스트 - 운행시간 외 운행 안함")
    void isValidRunTimeTest(final int day, final int hourOfDay) {
        // given
        final CallBusTimeChecker callBusTimeChecker = new CallBusTimeChecker();
        // when & then
        assertThat(callBusTimeChecker.isServiceTime(day, hourOfDay)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("sundayToMonday")
    @DisplayName("운행시간 확인 테스트 - 일 -> 월 운행시간 운행 안함")
    void isValidSunDayToMonday(final int day, final int hourOfDay) {
        // given
        final CallBusTimeChecker callBusTimeChecker = new CallBusTimeChecker();

        // when & then
        assertThat(callBusTimeChecker.isServiceTime(day, hourOfDay)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("runnableWednesdayTime")
    @DisplayName("운행시간 확인 테스트 - 수요일 22시 부터 운행")
    void isValidWednesdayStartTime(final int day, final int hourOfDay) {
        // given
        final CallBusTimeChecker callBusTimeChecker = new CallBusTimeChecker();

        // when & then
        assertThat(callBusTimeChecker.isServiceTime(day, hourOfDay)).isTrue();

    }

    static Stream<Arguments> notRunnableTime() {
        return Stream.of(Arguments.of(1, 20), Arguments.of(4, 4), Arguments.of(5, 10));
    }

    static Stream<Arguments> sundayToMonday() {
        return Stream.of(Arguments.of(6, 23), Arguments.of(0, 1), Arguments.of(0, 2));
    }

    static Stream<Arguments> runnableWednesdayTime() {
        return Stream.of(Arguments.of(2, 22), Arguments.of(2, 23), Arguments.of(3, 0));
    }
}

class CallBusTimeChecker {
    private static final int[] DAYS = {0, 1, 2, 3, 4, 5, 6};
    private static final int[] HOUR_OF_DAYS = IntStream.range(0, 24).toArray();
    private static final LocalTime START_TIME = LocalTime.of(23, 0);
    private static final LocalTime END_TIME = LocalTime.of(3, 59);
    private static final LocalTime WEDNESDAY_START_TIME = LocalTime.of(22, 0);

    public boolean isServiceTime(int day, int hourOfDay) {
        isValidDay(day);
        isValidHourOfDay(hourOfDay);

        if (isSundayToMonday(day, hourOfDay)) {
            return false;
        }

        if (isWednesdayStartTime(day, hourOfDay)) {
            return true;
        }

        return isRunTime(hourOfDay);
    }

    private void isValidDay(final int day) {
        if (Arrays.stream(DAYS).anyMatch(value -> value == day)) {
            return;
        }

        throw new IllegalArgumentException("요일을 잘못입력하였습니다. 0 ~ 6");
    }

    private void isValidHourOfDay(final int hourOfDay) {
        if (Arrays.stream(HOUR_OF_DAYS).anyMatch(value -> value == hourOfDay)) {
            return;
        }

        throw new IllegalArgumentException("시간을 잘못입력하였습니다. 0 ~ 23");
    }

    private boolean isSundayToMonday(final int day, final int hourOfDay) {
        final LocalTime inputTime = LocalTime.of(hourOfDay, 1);

        if (day == 6) {
            return inputTime.equals(START_TIME) || inputTime.isAfter(START_TIME);
        }

        if (day == 0) {
            return inputTime.isBefore(END_TIME);
        }
        return false;
    }

    private boolean isWednesdayStartTime(final int day, final int hourOfDay) {
        if (day != 2) {
            return false;
        }

        final LocalTime nowLocalTime = LocalTime.of(hourOfDay, 0);
        return nowLocalTime.equals(WEDNESDAY_START_TIME) || nowLocalTime.isAfter(WEDNESDAY_START_TIME) && nowLocalTime.isBefore(END_TIME);
    }

    private boolean isRunTime(final int hourOfDay) {
        final LocalTime nowLocalTime = LocalTime.of(hourOfDay, 1);
        return nowLocalTime.equals(START_TIME) || nowLocalTime.isAfter(START_TIME) || nowLocalTime.isBefore(END_TIME);
    }
}
