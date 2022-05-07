package com.disepi.moonlight.events;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import com.disepi.moonlight.anticheat.Moonlight;

public class onPlayerJoin implements Listener {
    // Upon a player joining, it sets up the player data structure for the target.
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Moonlight.addData(event.getPlayer());
    }
}
