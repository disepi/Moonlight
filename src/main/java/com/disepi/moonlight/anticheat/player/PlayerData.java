package com.disepi.moonlight.anticheat.player;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import com.disepi.moonlight.utils.FakePlayer;

public class PlayerData {

    // Holds information about the players movement, statistics etc.
    public FakePlayer fake; // The fake player entity used for some checks
    public float lastX, lastY, lastZ, predictedFallAmount, currentSpeed, lastSpeed, balance = 0; // Last player position info and other movement stuff
    public int onGroundTicks, offGroundTicks, fallingTicks = 0; // Ticks
    public boolean onGround, onGroundAlternate = true; // onGround stores if the player is near ground, onGroundAlternate stores if the player is directly on ground
    public Vector3 startFallPos = null; // Position of when the player started falling
    public long lastTime = 0; // Last time when the player sent a move packet in milliseconds

    // Constructor
    public PlayerData(Player player) {
        this.lastX = (float) player.x;
        this.lastY = (float) player.y;
        this.lastZ = (float) player.z;
    }

    // Removes the instance of the fake player from the world and the class instance
    public void destructFakePlayer() {
        this.fake.despawnFromAll();
        this.fake = null;
    }
}
