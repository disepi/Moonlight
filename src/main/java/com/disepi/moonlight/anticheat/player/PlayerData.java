package com.disepi.moonlight.anticheat.player;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import com.disepi.moonlight.utils.FakePlayer;
import com.disepi.moonlight.utils.PlayerUtils;

public class PlayerData {

    // Holds information about the players movement, statistics etc.
    public FakePlayer fake;
    public double lastX, lastY, lastZ, predictedFallAmount, currentSpeed, lastSpeed, balance = 0;
    public int onGroundTicks, offGroundTicks, fallingTicks = 0;
    public boolean onGround = true;
    public Vector3 startFallPos = null;
    public long lastTime = 0;

    // Constructor
    public PlayerData(Player player) {
        this.lastX = player.x;
        this.lastY = player.y;
        this.lastZ = player.z;
    }

    // Removes the instance of the fake player from the world and the class instance
    public void destructFakePlayer()
    {
        this.fake.despawnFromAll();
        this.fake = null;
    }
}
