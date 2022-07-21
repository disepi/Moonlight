package com.disepi.moonlight.anticheat.check.player.scaffold;

import cn.nukkit.Player;
import cn.nukkit.inventory.transaction.data.UseItemData;
import cn.nukkit.math.BlockFace;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.Util;

public class ScaffoldB extends Check {
    // Constructor
    public ScaffoldB() {
        super("ScaffoldB", "Checks for invalid placements", 8);
    }

    public void doFailCheck(Player p, PlayerData d, String type, float x, float y, float z, double dist, BlockFace face) {
        float currentVl = d.getViolationScale(this);
        if (currentVl >= 2.0f)
            fail(p, "type=" + type + ", x=" + x + ", y=" + y + ", z=" + z + ", distance=" + (float) dist + ", blockFace=" + face + ", vl=" + currentVl);
        violate(p, d, 1, true);
    }

    public void check(InventoryTransactionPacket e, PlayerData d, Player p) {
        if (e.transactionType != InventoryTransactionPacket.TYPE_USE_ITEM) return;
        this.reward(d, 0.1f);

        // Click position check
        UseItemData useItemData = (UseItemData) e.transactionData;
        if (useItemData.actionType == 0) { // Placing

            float x = useItemData.clickPos.x;
            float y = useItemData.clickPos.y;
            float z = useItemData.clickPos.z;
            double placeDistance = Util.distance(useItemData.blockPos.x, useItemData.blockPos.y, useItemData.blockPos.z, useItemData.playerPos.x, useItemData.playerPos.y, useItemData.playerPos.z);

            // Place distance check
            if (placeDistance > 7.0f) {
                doFailCheck(p, d, "PLACE_DISTANCE", 0, 0, 0, placeDistance, useItemData.face);
                punish(p, d);
            }

            boolean isExtendedPlace = (x < 0 || y < 0 || z < 0 || x > 1 || y > 1 || z > 1);

            // Never possible in vanilla clients
            if (x == 0.5f && y == 0.5f && z == 0.5f) {
                doFailCheck(p, d, "PLACE_ANGLE_INVALID_ORIGIN", x, y, z, placeDistance, useItemData.face);
                punish(p, d);
            } else if (x < -7 || y < -7 || z < -7 || x > 7 || y > 7 || z > 7) { // Check for validity of placement
                doFailCheck(p, d, "PLACE_ANGLE_INVALID_SCALE", x, y, z, placeDistance, useItemData.face);
                punish(p, d);
            }

            float leniency = 0.1f;
            if (!isExtendedPlace) {
                switch (useItemData.face) {
                    case NORTH:
                        if (!Util.isRoughlyEqual(z, 0, leniency))
                            doFailCheck(p, d, "PLACE_ANGLE_NORTH", x, y, z, placeDistance, useItemData.face);
                        break;
                    case EAST:
                        if (!Util.isRoughlyEqual(x, 1, leniency))
                            doFailCheck(p, d, "PLACE_ANGLE_EAST", x, y, z, placeDistance, useItemData.face);
                        break;
                    case SOUTH:
                        if (!Util.isRoughlyEqual(z, 1, leniency))
                            doFailCheck(p, d, "PLACE_ANGLE_SOUTH", x, y, z, placeDistance, useItemData.face);
                        break;
                    case WEST:
                        if (!Util.isRoughlyEqual(x, 0, leniency))
                            doFailCheck(p, d, "PLACE_ANGLE_WEST", x, y, z, placeDistance, useItemData.face);
                        break;
                }
            }
        }
    }

}