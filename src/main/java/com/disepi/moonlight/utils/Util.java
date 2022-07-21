package com.disepi.moonlight.utils;

import cn.nukkit.math.Vector2;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.NetworkStackLatencyPacket;
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

    // Returns the distance between two coordinates
    public static float distance(float x, float y, float z, float x2, float y2, float z2) {
        float dX = x - x2;
        float dY = y - y2;
        float dZ = z - z2;
        return (float) Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static double distance(double x, double y, double z, double x2, double y2, double z2) {
        double dX = x - x2;
        double dY = y - y2;
        double dZ = z - z2;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    public static NetworkStackLatencyPacket getStackLatency(long ms) {
        NetworkStackLatencyPacket stack = new NetworkStackLatencyPacket();
        stack.timestamp = ms;
        stack.unknownBool = true; // "needResponse" or "sendBack"
        return stack;
    }

    public static float floatMod(float x, float y) {
        return (x - (float) Math.floor(x / y) * y);
    }

    public static Vector2 getRotationsToPosition(Vector3 origin, Vector3 target) {
        double xDiff = target.x - origin.x;
        double yDiff = target.y - origin.y - 1.2;
        double zDiff = target.z - origin.z;

        float yaw = (float) (Math.atan2(zDiff, xDiff) * MotionUtils.DEG_RAD) - 90;
        return new Vector2((float) (yaw < -180 ? Math.abs(yaw + 180.0) : yaw), (float) -(Math.atan2(yDiff, Math.sqrt(xDiff * xDiff + zDiff * zDiff)) * MotionUtils.DEG_RAD));
    }

    // Checks if a value is similar to another
    public static boolean isRoughlyEqual(double x, double y, double leniency) {
        return Math.abs(x - y) < leniency;
    }

    public static boolean isRoughlyEqual(float x, float y, float leniency) {
        return Math.abs(x - y) < leniency;
    }

}
