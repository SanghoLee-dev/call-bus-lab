package com.callbus.codingtest;

import com.callbus.codingtest.utils.StringSplitUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CodingTestC {

    public boolean isRightCarNumFormat(String carNum) {
        if (carNum.length() != 9) {
            throw new IllegalArgumentException("차량번호 길이가 다릅니다.");
        }

        // 문자 2개
        final boolean stringTwoCondition = Arrays.stream(StringSplitUtils.splitStringByCharacter(carNum.substring(0, 2)))
                .noneMatch(this::isNumber);
        // 숫자 2개
        final boolean numberTwoCondition = Arrays.stream(StringSplitUtils.splitStringByCharacter(carNum.substring(2, 4)))
                .allMatch(this::isNumber);
        // 문자 1개
        final boolean stringOneCondition = Arrays.stream(StringSplitUtils.splitStringByCharacter(carNum.substring(4, 5)))
                .noneMatch(this::isNumber);
        // 숫자 4개
        final boolean numberFourCondition = Arrays.stream(StringSplitUtils.splitStringByCharacter(carNum.substring(5, 9)))
                .allMatch(this::isNumber);

        return stringTwoCondition && numberTwoCondition && stringOneCondition && numberFourCondition;
    }

    private boolean isNumber(final String s) {
        boolean isNumberFormat = false;

        try {
            Integer.parseInt(s);
            isNumberFormat = true;
        } catch (NumberFormatException e) {
        }
        return isNumberFormat;
    }

    @Test
    @DisplayName("잘못된 길이의 차량번호")
    void invalidLengthTest() {
        // given
        final String carNum = "서울27가8421111";

        // when & then
        assertThatThrownBy(() -> isRightCarNumFormat(carNum))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("길이")
        ;
    }

    @ParameterizedTest
    @MethodSource("invalidFormatCarNumbers")
    @DisplayName("잘못된 형식 차량번호")
    void invalidFormTest(final String carNum) {
        // given

        // when & then
        assertThat(isRightCarNumFormat(carNum)).isFalse();
    }

    @ParameterizedTest
    @MethodSource("isRightCarNumFormatCarNumbers")
    @DisplayName("정상적인 차량 번호 - 성공 테스트")
    void isRightCarNumFormatTest(final String carNum) {
        // given

        // when & then
        assertThat(isRightCarNumFormat(carNum)).isTrue();
    }

    static Stream<Arguments> invalidFormatCarNumbers() {
        return Stream.of(Arguments.of("서127가8421"), Arguments.of("212나가8421"));
    }

    static Stream<Arguments> isRightCarNumFormatCarNumbers() {
        return Stream.of(Arguments.of("서울27가8421"), Arguments.of("경기07마8421"));
    }
}
