package com.disepi.moonlight.anticheat.check;

import cn.nukkit.Player;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.network.protocol.PlayerActionPacket;
import cn.nukkit.utils.TextFormat;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.Util;

public class Check {
    public String name, detection; // The basic information about the check
    public float maxViolationScale; // How many violations it will take for Moonlight to punish the player
    public int checkId; // Check identification number

    // Constructor
    public Check(String name, String detection, float maxViolationScale) {
        this.name = name;
        this.detection = detection;
        this.maxViolationScale = maxViolationScale;
        this.checkId = Moonlight.checkAmount++;
    }

    // Called upon a player failing the check.
    public void fail(Player p, String debug) {
        String message = Moonlight.stylizedChatString + p.getName() + TextFormat.GRAY + " failed " + TextFormat.WHITE + this.name + TextFormat.DARK_GRAY + " [" + debug + "]";
        Moonlight.sendMessageToModerators(p, message);
    }

    // Teleports the player to an appropriate location.
    public void lagback(Player p, PlayerData d, Vector3 pos) {
        d.resetMove = true;
        d.teleportPos = pos;
        d.isTeleporting = true;
        p.teleport(pos);
    }

    // Proxy function for above function
    public void lagback(Player p, PlayerData d) {
        this.lagback(p, d, d.lastGroundPos);
    }

    // Violation functions
    public void violate(Player player, PlayerData data, float amount, boolean punish)
    {
        data.violationMap[this.checkId] += amount;
        if(punish && getViolationScale(data) > this.maxViolationScale) punish(player,data); // We failed the check repeatedly, punish
    }
    public void reward(PlayerData data, float amount)
    {
        data.violationMap[this.checkId] -= amount;
        if(data.violationMap[this.checkId] < 0)
            data.violationMap[this.checkId] = 0;
    }
    public float getViolationScale(PlayerData data)
    {
        return data.violationMap[this.checkId];
    }

    // Punishes the player.
    public void punish(Player p, PlayerData d)
    {
        String message = Moonlight.stylizedChatString + p.getName() + TextFormat.GRAY + " was " + TextFormat.DARK_RED + "punished" + TextFormat.DARK_GRAY + ". [" + this.name + "]";
        Moonlight.sendMessageToModerators(p, message);
        p.kick();
    }

    // Below are override functions - they do nothing but they get overridden in standalone checks

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
    }

    public void check(EntityDamageByEntityEvent e, PlayerData d, Player p) {
    }

    public void check(PlayerActionPacket e, PlayerData d, Player p) {
    }

}
