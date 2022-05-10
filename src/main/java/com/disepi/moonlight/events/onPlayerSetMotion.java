package com.disepi.moonlight.events;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketSendEvent;
import cn.nukkit.network.protocol.SetEntityMotionPacket;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class onPlayerSetMotion implements Listener {

    // Listens for SetEntityMotionPacket. These are packets that get sent when you receive knock-back by mobs or external plugins.

    public static final double DEFAULT_MOTION_POWER = 1.0;

    @EventHandler
    public void onPlayerSetMotion(DataPacketSendEvent event) {
        if (!(event.getPacket() instanceof SetEntityMotionPacket)) // If the sent packet isn't SetEntityMotionPacket then we don't want it right now
            return;
        if (!(event.getPlayer() instanceof Player)) return; // Target must be a player
        SetEntityMotionPacket packet = (SetEntityMotionPacket) event.getPacket();
        PlayerData data = Moonlight.getData(event.getPlayer());
        if (data == null) return;
        float motionPower = Math.abs(packet.motionX) + Math.abs(packet.motionY) + Math.abs(packet.motionZ);
        data.lastLerpStrength = motionPower;
        data.lerpTicks = 30; // Set lerp ticks
    }
}
