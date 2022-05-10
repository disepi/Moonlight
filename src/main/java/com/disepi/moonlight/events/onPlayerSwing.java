package com.disepi.moonlight.events;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerAnimationEvent;
import cn.nukkit.network.protocol.AnimatePacket;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class onPlayerSwing implements Listener {

    // Listens for player swings.

    @EventHandler
    public void onPlayerSwing(PlayerAnimationEvent event) {
        if (event.getAnimationType() != AnimatePacket.Action.SWING_ARM) return; // We only want swings
        PlayerData data = Moonlight.getData(event.getPlayer());
        if (data == null) return;
        data.lastSwingTime = System.currentTimeMillis(); // Set swing time
    }
}
