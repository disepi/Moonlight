package com.disepi.moonlight.anticheat.check.player.scaffold;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MobEquipmentPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class ScaffoldA extends Check {
    // Constructor
    public ScaffoldA() {
        super("ScaffoldA", "Checks for invalid selections", 6);
    }

    public void check(MobEquipmentPacket e, PlayerData d, Player p) {
        if (e.windowId == 0 && (e.hotbarSlot > 8 || e.inventorySlot > 8))
            punish(p, d);

        long difference = System.currentTimeMillis() - d.lastSwitchTime;
        this.reward(d, 0.25f);
        if (difference < 3) {
            violate(p, d, 1, true);
            if (this.getViolationScale(d) > 3)
                fail(p, "difference=" + difference);
        }
    }

}