package com.disepi.moonlight.anticheat.check;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.utils.TextFormat;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class Check {
    public String name, detection; // The basic information about the check
    public int maxViolationScale; // How many violations it will take for Moonlight to punish the player

    // Constructor = simple
    public Check(String name, String detection, int maxViolationScale) {
        this.name = name;
        this.detection = detection;
        this.maxViolationScale = maxViolationScale;
    }

    // Called upon a player failing the check
    public void fail(Player p, String debug) {
        String message = TextFormat.DARK_GRAY + "[" + TextFormat.DARK_AQUA + "moonlight" + TextFormat.DARK_GRAY + "] " + TextFormat.WHITE + p.getName() + TextFormat.GRAY + " failed " + TextFormat.WHITE + this.name + TextFormat.DARK_GRAY + " [" + debug + "]";
        Moonlight.sendMessageToModerators(p, message);
    }

    // Below are override functions - they do nothing but they get overridden in standalone checks

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
    }

    public void check(EntityDamageByEntityEvent e, PlayerData d, Player p) {
    }

}
