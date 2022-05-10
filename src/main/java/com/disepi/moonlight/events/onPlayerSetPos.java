package com.disepi.moonlight.events;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.math.Vector3;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class onPlayerSetPos implements Listener {

    // Listens for teleports.

    public void doTeleport(Player p, Vector3 pos) {
        PlayerData data = Moonlight.getData(p);
        if (data == null) return;
        data.teleportPos = pos;
        data.isTeleporting = true;
        data.lastX = (float) data.teleportPos.x;
        data.lastY = (float) data.teleportPos.y;
        data.lastZ = (float) data.teleportPos.z;
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        doTeleport(event.getPlayer(), event.getTo());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        doTeleport(event.getPlayer(), event.getRespawnPosition());
    }
}
