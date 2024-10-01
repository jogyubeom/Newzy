package com.newzy.backend.global.util;

import java.security.SecureRandom;

public class RandomStringGenerator {

    // 사용할 문자 집합
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    // 랜덤 문자열 생성 메소드
    public static String generateRandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("String length must be greater than 0");
        }

        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            randomString.append(CHARACTERS.charAt(randomIndex));
        }

        return randomString.toString();
    }
}
