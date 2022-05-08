package com.disepi.moonlight.utils;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

public class WorldUtils {
    public static BlockAir airB = new BlockAir();

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

    // Checks surroundings to retrieve the nearest solid block
    public static Block getNearestSolidBlock(Vector3 pos, Level level, int radius) {
        double value = 1.62 + 0.01; // getEyeHeight();
        Block under = level.getBlock((int) pos.x, (int) (pos.y - value), (int) pos.z); // get block
        if (!(under instanceof BlockAir)) // check if it isn't air
            return under; // return under block

        // check in radius around player
        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                Block temp = level.getBlock((int) pos.x + x, (int) (pos.y - value), (int) pos.z + z); // get temp block
                if (!(temp instanceof BlockAir)) return temp; // if it isn't air, return it
            }
        }
        return airB; // no block found, return air
    }

    public static Block getNearestSolidBlock(double x, double y, double z, Level level, int radius) {
        return getNearestSolidBlock(new Vector3(x, y, z), level, radius);
    }

}
