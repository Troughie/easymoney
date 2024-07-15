package com.example.MoneyLover.utils;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@Service
public class GenerateString {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateString(int length, typeGenerate type)
    {
        if(type==typeGenerate.string)
        {
        if (length <= 0) {
            throw new IllegalArgumentException("Token length must be greater than 0");
        }

        StringBuilder token = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            token.append(CHARACTERS.charAt(index));
        }
        return token.toString();

        }else{
            int min = (int) Math.pow(10, length - 1); // Ví dụ: nếu length = 4, min = 1000
            int max = (int) Math.pow(10, length) - 1; // Ví dụ: nếu length = 4, max = 9999

            Random rand = new Random();
            int lastInt =rand.nextInt(max - min + 1) + min;
            return String.valueOf(lastInt);
        }
    }
}
