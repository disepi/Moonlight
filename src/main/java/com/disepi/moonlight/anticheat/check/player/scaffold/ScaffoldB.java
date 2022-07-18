package com.disepi.moonlight.anticheat.check.player.scaffold;

import cn.nukkit.Player;
import cn.nukkit.inventory.transaction.data.UseItemData;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.Util;

public class ScaffoldB extends Check {
    // Constructor
    public ScaffoldB() {
        super("ScaffoldB", "Checks for invalid placements", 4);
    }



    public void check(InventoryTransactionPacket e, PlayerData d, Player p) {
        if (e.transactionType != InventoryTransactionPacket.TYPE_USE_ITEM) return;
        this.reward(d, 0.25f);

        // Click position check
        UseItemData useItemData = (UseItemData) e.transactionData;
        if (useItemData.actionType == 0) { // Placing
            if (useItemData.clickPos.x == 0.0f && useItemData.clickPos.y == 0.0f && useItemData.clickPos.z == 0.0f)
                violate(p, d, 1, true); // Empty click position - *sometimes* possible
            else if (useItemData.clickPos.x == 0.5f && useItemData.clickPos.y == 0.5f && useItemData.clickPos.z == 0.5f)
                punish(p, d); // Middle click position - NEVER possible
        }
    }

}