package com.disepi.moonlight.anticheat.player;

import cn.nukkit.Player;
import cn.nukkit.math.Vector3;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.utils.FakePlayer;

public class PlayerData {

    // Holds information about the players movement, statistics etc.
    public float[] violationMap = new float[64]; // Violation map
    public FakePlayer fake; // The fake player entity used for some checks
    public float lastX, lastY, lastZ, lastPitch, lastYaw, predictedFallAmount, currentSpeed, lastSpeed, balance = 0; // Last player position info and other movement stuff
    public int onGroundTicks, offGroundTicks, fallingTicks = 0; // Ticks
    public boolean onGround = true; // onGround stores if the player is near ground, onGroundAlternate stores if the player is directly on ground
    public Vector3 startFallPos, lastGroundPos = null; // Position of when the player started falling
    public long lastTime = 0; // Last time when the player sent a move packet in milliseconds
    public long lastSwingTime, lastSwingTimeBefore = 0; // Last time when the player swung

    public int frictionLenientTicks = 0; // Ice blocks
    public int gravityLenientTicks = 0; // Ladders, lava, water, cobwebs, slimeblocks etc.
    public int blockAboveLenientTicks = 0; // Jumping below blocks
    public int staircaseLenientTicks = 0; // Jumping on staircases
    public int sprintingTicks = 0; // Will stay at 10 when player is sprinting and decrease over ticks if they are not
    public int jumpTicks = 0; // Increases to a fixed value when a player jumps and decreases after
    public int lerpTicks = 0; // Increases to a fixed value when a player's motion gets set by the server
    public int collidedHorizontallyTicks = 0; // Increases to a fixed value when a player collides horizontally
    public float speedMultiplier = 1; // Speed potions affect this

    public int speedPotionLenientTicks = 0;
    public int levitationPotionLenientTicks = 0;
    public int jumpPotionLenientTicks = 0;

    public int lastSpeedAmplifier = 0;
    public int lastLevitationAmplifier = 0;
    public int lastJumpAmplifier = 0;

    public int elytraWornLenience = 0;

    public float lastLerpStrength = 1;
    public boolean isTeleporting = false;
    public Vector3 teleportPos;
    public boolean isTouchscreen = false;
    public Vector3 viewVector;
    public boolean resetMove = false;

    public int moveTicks = 0;

    // Constructor
    public PlayerData(Player player) {
        this.lastX = (float) player.x;
        this.lastY = (float) player.y + 1.62f;
        this.lastZ = (float) player.z;
        this.teleportPos = new Vector3(player.x, player.y, player.z);
        this.lastGroundPos = this.teleportPos;
        this.isTeleporting = false;
        int deviceOSType = player.getLoginChainData().getDeviceOS();
        if (deviceOSType == 1 || deviceOSType == 2) isTouchscreen = true;
        long currentTime = System.currentTimeMillis();
        this.lastTime = currentTime;
        this.lastSwingTime = currentTime;
        this.lastSwingTimeBefore = currentTime;
        this.startFallPos = null;
    }

    // Removes the instance of the fake player from the world and the class instance
    public void destructFakePlayer() {
        this.fake.getLevel().removeEntity(this.fake);
        this.fake.despawnFromAll();
        this.fake = null;
    }

    public boolean isPlayerConsideredSprinting() {
        return this.sprintingTicks > 0;
    }

    public boolean isPlayerConsideredJumping() {
        return this.jumpTicks > 0;
    }

    public float getExtraJumpValue() { return (this.lastJumpAmplifier+1)/10.0f; }

    public boolean hasPlayerLoadedIn() { return this.moveTicks > 2;}

    public boolean isJumpBoostActive() { return this.jumpPotionLenientTicks > 0; }
    public boolean isLevitationActive() { return this.levitationPotionLenientTicks > 0; }
    public boolean isSpeedActive() { return this.speedPotionLenientTicks > 0; }

    public void violate(Check check, float amount) {
        this.violationMap[check.checkId] += amount;
    }

    public void reward(Check check, float amount) {
        this.violationMap[check.checkId] -= amount;
        if (this.violationMap[check.checkId] < 0)
            this.violationMap[check.checkId] = 0;
    }

    public float getViolationScale(Check check) {
        return this.violationMap[check.checkId];
    }

}
