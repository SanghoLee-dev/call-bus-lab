package com.callbus.codingtest;

import com.callbus.codingtest.utils.StringSplitUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CodingTestB {

    public String compress(final String any) {
        final String[] stringArray = getStringSplitArrayAddEnd(any);

        if (Arrays.stream(stringArray).anyMatch(this::isNumber)) {
            throw new IllegalArgumentException("압축하기 잘못된 문자열 입니다.");
        }

        final AtomicReference<String> compressString = new AtomicReference<>("");
        final AtomicInteger index = new AtomicInteger(1);
        Arrays.stream(stringArray).reduce((s1, s2) -> {
            if (s1.equals(s2)) {
                index.getAndIncrement();
                return s2;
            }
            final String numberCompressString = index.get() + s1;
            compressString.set(compressString + numberCompressString);
            index.set(1);
            return s2;
        });
        return compressString.get();
    }

    private String[] getStringSplitArrayAddEnd(final String any) {
        final String[] stringArray = Arrays.copyOf(StringSplitUtils.splitStringByCharacter(any), any.length() + 1);
        stringArray[any.length()] = "END";
        return stringArray;
    }

    public String decompress(final String compressed) {
        final String[] compressedSplitStrings = getStringSplitArrayAddEnd(compressed);

        if (Arrays.stream(compressedSplitStrings).noneMatch(this::isNumber)) {
            throw new IllegalArgumentException("해제하기 잘못된 문자열 입니다.");
        }

        final AtomicReference<String> deCompressString = new AtomicReference<>("");

        Arrays.stream(compressedSplitStrings).reduce((s1, s2) -> {
            if (s1.equals("")) {
                return s2;
            }

            if (isNumber(s1) && isNumber(s2)) {
                return s1 + s2;
            }

            if (!isNumber(s2)) {
                final int i = Integer.parseInt(s1);
                IntStream.range(0, i).forEach(operand -> deCompressString.set(deCompressString + s2));
                return "";
            }

            throw new IllegalArgumentException("");
        });

        return deCompressString.get();
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
    @DisplayName("잘못된 문자열을 압축할때")
    void compressExceptionTest() {
        // given
        // when & then
        assertThatThrownBy(() -> compress("3Z10A2B2C1Q2A"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("압축하기")
        ;
    }

    @ParameterizedTest
    @MethodSource("successCompress")
    @DisplayName("문자열 압축 성공 테스트")
    void compressTest(final String any, final String compressString) {
        // given
        final String compress = compress(any);
        // when & then
        assertThat(compress).isEqualTo(compressString);
    }

    @Test
    @DisplayName("잘못된 문자열을 해제할때")
    void decompressExceptionTest() {
        // given
        // when & then
        assertThatThrownBy(() -> decompress("ZZZAAAAAAAAAABBCCQAA"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해제하기")
        ;
    }

    @ParameterizedTest
    @MethodSource("successDeCompress")
    @DisplayName("문자열 해제 성공 테스트")
    void decompressTest(final String compressed, final String deCompressString) {
        // given
        final String decompress = decompress(compressed);
        // when & then
        assertThat(decompress).isEqualTo(deCompressString);
    }

    static Stream<Arguments> successCompress() {
        return Stream.of(Arguments.of("ZZZAAAAAAAAAABBCCQAA", "3Z10A2B2C1Q2A"), Arguments.of("BBCAZATTSSSSSSSEWWWWEEEE", "2B1C1A1Z1A2T7S1E4W4E"));
    }

    static Stream<Arguments> successDeCompress() {
        return Stream.of(Arguments.of("3Z10A2B2C185A", "ZZZAAAAAAAAAABBCCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"),
                Arguments.of("3Z10A2B2C1Q2A", "ZZZAAAAAAAAAABBCCQAA"), Arguments.of("2B1C1A1Z1A2T7S1E4W4E", "BBCAZATTSSSSSSSEWWWWEEEE"));
    }
}
