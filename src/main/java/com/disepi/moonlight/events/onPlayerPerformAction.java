package com.disepi.moonlight.events;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.PlayerActionPacket;
import com.disepi.moonlight.anticheat.Moonlight;

public class onPlayerPerformAction implements Listener {

    // Listens for PlayerActionPackets. These are packets that get sent when you start sprinting, sneaking, etc.

    @EventHandler
    public void onPlayerPerformAction(DataPacketReceiveEvent event) {
        if (!(event.getPacket() instanceof PlayerActionPacket)) // If the received packet isn't PlayerActionPacket then we don't want it right now
            return;

        PlayerActionPacket packet = (PlayerActionPacket) event.getPacket();
        if (packet.action == PlayerActionPacket.ACTION_JUMP)
            Moonlight.getData(event.getPlayer()).jumpTicks = 20; // Set jump ticks
    }
}
