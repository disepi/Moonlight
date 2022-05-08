package com.disepi.moonlight.events;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInvalidMoveEvent;
import com.disepi.moonlight.anticheat.Moonlight;

public class onPlayerMovedWrongly implements Listener {
    @EventHandler
    public void onPlayerMovedWrongly(PlayerInvalidMoveEvent event) {
        // Nukkit's default invalid movement check has many flaws, such as not accounting for speed potions,
        // sprint jumping under blocks and other instances where your speed peaks. It is better to disable
        // it completely and let Moonlight's checks handle the movement
        if (Moonlight.cancelNukkitInvalidMove)
            event.setCancelled(true);
    }
}
