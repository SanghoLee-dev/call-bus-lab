package com.callbus.codingtest;

public class CodingTestC {

    boolean isRightCarNumFormat(String carNum) {
        // 문자 2개
        carNum.substring(0,2);
        // 숫자 2개
        final String substring = carNum.substring(2, 4);
        // 문자 1개
        carNum.substring(4,5);
        // 숫자 4개
        carNum.substring(5,9);
        // 서울27가8421

        return true;
    }

}
