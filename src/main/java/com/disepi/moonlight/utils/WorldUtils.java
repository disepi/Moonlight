package com.disepi.moonlight.utils;

import cn.nukkit.block.BlockAir;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

public class WorldUtils {

    // Returns if the specified block is a valid block that the player can walk on
    public static boolean isConsideredSolid(Level level, int x, int y, int z) {
        return !(level.getBlock(x, y, z) instanceof BlockAir);
    }

    public static boolean isConsideredSolid(Level level, double x, double y, double z) {
        return isConsideredSolid(level, (int) x, (int) y, (int) z);
    }

    public static boolean isConsideredSolid(Level level, Vector3 pos) {
        return WorldUtils.isConsideredSolid(level, (int) pos.x, (int) pos.y, (int) pos.z);
    }

    // Checks surroundings to determine if the player is actually on ground
    public static boolean isConsideredOnGround(Vector3 pos, Level level) {
        double value = 1.62 + 0.01; // getEyeHeight();
        if (isConsideredSolid(level, pos.x, pos.y - value, pos.z)) // directly below player
            return true;

        // check in radius around player
        for (int x = -2; x < 2; x++) {
            for (int z = -2; z < 2; z++) {
                if (isConsideredSolid(level, pos.x + x, pos.y - value, pos.z + z)) return true;
            }
        }
        return false;
    }

    public static boolean isConsideredOnGround(double x, double y, double z, Level level) {
        return isConsideredOnGround(new Vector3(x, y, z), level);
    }

}
