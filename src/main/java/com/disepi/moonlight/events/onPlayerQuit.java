package com.disepi.moonlight.events;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerQuitEvent;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class onPlayerQuit implements Listener {
    // Called upon player quit. It cleans up after the player has disconnected.
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        PlayerData data = Moonlight.getData(event.getPlayer());
        if(data.fake != null) {
            data.fake.despawnFromAll();
            data.fake = null;
        }
        Moonlight.removeData(event.getPlayer());
    }
}
