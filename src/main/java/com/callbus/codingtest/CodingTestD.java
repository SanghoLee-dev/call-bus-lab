package com.callbus.codingtest;

import java.util.Arrays;

public class CodingTestD {

    boolean isStraightLine(double[][]... dots) {
        if (dots.length < 3) {
            throw new IllegalArgumentException("3개 이상의 좌표를 넣어주세요.");
        }


        return false;
    }

    boolean isStraightLine(Point... points) {
        if (points.length < 3) {
            throw new IllegalArgumentException("3개 이상의 좌표를 넣어주세요.");
        }

        return false;
    }

}
