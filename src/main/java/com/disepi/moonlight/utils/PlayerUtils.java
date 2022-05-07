package com.disepi.moonlight.utils;

import cn.nukkit.Player;
import cn.nukkit.level.Position;

import java.io.ByteArrayOutputStream;

public class PlayerUtils {
    // Stores the invisible skin
    public static byte[] skinArray = null;

    // Sets up the invisible skin
    public static void setupSkinStream() {
        if (skinArray != null) return;
        ByteArrayOutputStream str = new ByteArrayOutputStream();
        for (int i = 0; i < 16384; i++)
            str.write(0);
        skinArray = str.toByteArray();
    }

    // Returns an NPC bot.
    public static FakePlayer getFakePlayer(Player targetPlayer) {
        return new FakePlayer(new Position(targetPlayer.x, targetPlayer.y + 3.5, targetPlayer.z, targetPlayer.getLevel()), skinArray);
    }
}
