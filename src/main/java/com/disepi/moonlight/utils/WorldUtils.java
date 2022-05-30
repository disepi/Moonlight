package com.disepi.moonlight.utils;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockFence;
import cn.nukkit.block.BlockWall;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

public class WorldUtils {
    public static BlockAir airB = new BlockAir();

    public static Block getBlock(Level level, int x, int y, int z) {
        return level.getBlock(x < 0 ? x - 1 : x, y < 0 ? y - 1 : y, z < 0 ? z - 1 : z);
    }

    // Returns if the specified block is a valid block that the player can walk on
    public static boolean isConsideredSolid(Level level, int x, int y, int z) {
        return !(getBlock(level, x, y, z) instanceof BlockAir);
    }

    public static boolean isConsideredSolid(Level level, double x, double y, double z) {
        return isConsideredSolid(level, (int) x, (int) y, (int) z);
    }

    public static boolean isConsideredSolid(Level level, Vector3 pos) {
        return isConsideredSolid(level, (int) pos.x, (int) pos.y, (int) pos.z);
    }

    // Checks surroundings to retrieve the nearest solid block
    public static Block getNearestSolidBlock(Vector3 pos, Level level, float radius) {
        double value = 1.64; // getEyeHeight();
        Block under = getBlock(level, (int) pos.x, (int) (pos.y - value), (int) pos.z); // Get the block
        if (!(under instanceof BlockAir)) // Check if it isn't air
            return under; // Return the retrieved block

        // Check in radius around player
        for (float x = -radius; x < radius; x += 0.5f) {
            for (float z = -radius; z < radius; z += 0.5f) {
                Block temp = getBlock(level, (int) (pos.x + x), (int) (pos.y - value), (int) (pos.z + z)); // get temp block
                if (!(temp instanceof BlockAir)) return temp; // if it isn't air, return it
            }
        }

        // Fences/walls
        for (float x = -radius; x < radius; x += 0.5f) {
            for (float z = -radius; z < radius; z += 0.5f) {
                Block temp = getBlock(level, (int) (pos.x + x), (int) (pos.y - value - 0.5f), (int) (pos.z + z)); // get temp block
                if (temp instanceof BlockWall || temp instanceof BlockFence)
                    return temp; // check if it is a wall/fence block
            }
        }

        return airB; // no block found, return air
    }

    public static Block getNearestSolidBlock(double x, double y, double z, Level level, float radius) {
        return getNearestSolidBlock(new Vector3(x, y, z), level, radius);
    }

}
