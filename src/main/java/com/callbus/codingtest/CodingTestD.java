package com.callbus.codingtest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CodingTestD {

    boolean isStraightLine(Point... points) {
        if (points.length < 3) {
            throw new IllegalArgumentException("3개 이상의 좌표를 넣어주세요.");
        }
        final AtomicBoolean returnBool = new AtomicBoolean(true);
        final AtomicReference<Double> doubleAtomicReference = new AtomicReference<>(0.0);
        Arrays.stream(points).reduce((point1, point2) -> {
            if (point1.equals(point2)) {
                throw new IllegalArgumentException("서로 다른 점을 넣어주세요.");
            }

            final double angle = (point1.getY() - point2.getY()) / (point1.getX() - point2.getX());
            Double prevAngle = doubleAtomicReference.get();

            if (prevAngle == 0.0) {
                doubleAtomicReference.set(angle);
                prevAngle = angle;
            }

            if (prevAngle != angle) {
                returnBool.set(false);
            }

            return point2;
        });

        return returnBool.get();
    }

    @Test
    @DisplayName("점이 3개 이하일때")
    void lessThanThreePointTest() {
        // when & then
        assertThatThrownBy(() -> isStraightLine(new Point(0, 0), new Point(1, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("3개")
        ;
    }

    @Test
    @DisplayName("같은 점이 있을때")
    void equalsPointTest() {
        // when & then
        assertThatThrownBy(() -> isStraightLine(new Point(0, 0), new Point(0, 0), new Point(1, 1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("서로 다른 점")
        ;
    }

    @Test
    @DisplayName("같은 선상 점 확인 성공 테스트")
    void isStraightLineTest_success() {
        // when & then
        assertThat(isStraightLine(new Point(0, 0), new Point(1, 1), new Point(2, 2))).isTrue();
    }

    @Test
    @DisplayName("같은 선상 점 확인 실패 테스트")
    void isStraightLineTest_fail() {
        // when & then
        assertThat(isStraightLine(new Point(0, 0), new Point(1, 2), new Point(2, 2))).isFalse();
    }
}

class Point {
    private final double x;
    private final double y;

    public Point(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
