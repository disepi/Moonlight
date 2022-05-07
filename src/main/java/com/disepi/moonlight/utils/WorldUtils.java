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
        for (int i = 0; i < 2; i++) { // Towering up can cause issues so we check 1 more block down
            if (isConsideredSolid(level, pos.x, pos.y - value, pos.z) || isConsideredSolid(level, pos.x - 0.6, pos.y - value, pos.z - 0.6) || isConsideredSolid(level, pos.x + 0.6, pos.y - value, pos.z + 0.6) || isConsideredSolid(level, pos.x - 0.6, pos.y - value, pos.z + 0.6) || isConsideredSolid(level, pos.x + 0.6, pos.y - value, pos.z - 0.6))
                return true;
            value += 1.0;
        }
        return false;
    }

    public static boolean isConsideredOnGround(double x, double y, double z, Level level) {
        return isConsideredOnGround(new Vector3(x, y, z), level);
    }

}
