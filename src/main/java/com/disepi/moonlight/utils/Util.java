package com.disepi.moonlight.utils;

import cn.nukkit.utils.Logger;

import java.security.SecureRandom;

public class Util {
    static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static Logger log;
    public static SecureRandom rnd = new SecureRandom();

    // Logs messages to the console
    public static void log(String string) {
        log.info(string);
    }

    // Returns a random string with specified length
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
        return sb.toString();
    }

    // Checks if a value is similar to another
    public static boolean isRoughlyEqual(double x, double y, double leniency) {
        return Math.abs(x - y) < leniency;
    }

    public static boolean isRoughlyEqual(float x, float y, float leniency) {
        return Math.abs(x - y) < leniency;
    }

}
