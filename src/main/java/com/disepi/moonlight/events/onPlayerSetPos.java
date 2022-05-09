package com.disepi.moonlight.events;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.Util;

public class onPlayerSetPos implements Listener {

    // Listens for teleports.

    public void doTeleport(Player p, Vector3 pos)
    {
        PlayerData data = Moonlight.getData(p);
        if(data == null) return;
        data.teleportPos = pos;
        data.isTeleporting = true;
    }

    @EventHandler
    public void onPlayerSetPos(PlayerTeleportEvent event) {
        doTeleport(event.getPlayer(), event.getTo());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        doTeleport(event.getPlayer(), event.getRespawnPosition());
    }
}
