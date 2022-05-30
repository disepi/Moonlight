package com.disepi.moonlight.utils;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.AddEntityPacket;

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

    public static AddEntityPacket getCustomAddEntityPacket(int type) {
        AddEntityPacket addEntityPkt = new AddEntityPacket();
        addEntityPkt.type = type;
        long entID = Entity.entityCount++;
        addEntityPkt.entityUniqueId = entID;
        addEntityPkt.entityRuntimeId = entID;
        addEntityPkt.speedX = 0.0F;
        addEntityPkt.speedY = 0.0F;
        addEntityPkt.speedZ = 0.0F;
        addEntityPkt.yaw = 0;
        addEntityPkt.pitch = 0;
        return addEntityPkt;
    }
}
