package com.disepi.moonlight.events;

import cn.nukkit.Player;
import cn.nukkit.block.*;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemElytra;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.MovePlayerPacket;
import cn.nukkit.potion.Effect;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;
import com.disepi.moonlight.utils.Util;
import com.disepi.moonlight.utils.WorldUtils;

public class onPlayerMove implements Listener {

    // Called upon a player moves (when a MovePlayerPacket gets received).
    // I did not hook the PlayerMove event because it delayed movements to the next server tick;
    // For example: a player could possibly move twice in a single server tick and the
    // checks will false flag. This is not ideal and all information about the player's
    // movement should be accurate otherwise it can cause issues with the checks.

    @EventHandler
    public void onMove(DataPacketReceiveEvent event) {
        if (!(event.getPacket() instanceof MovePlayerPacket)) // If the received packet isn't MovePlayerPacket then we don't want it right now
            return;

        // Get and store packet info
        MovePlayerPacket packet = (MovePlayerPacket) event.getPacket();
        float x = packet.x;
        float y = packet.y;
        float z = packet.z;

        // Set/get data
        Player player = event.getPlayer(); // Get the player instance from the packet
        PlayerData data = Moonlight.getData(player); // Get the player data instance from Moonlight

        // Teleport/respawn check
        if (data.isTeleporting) {
            data.lastX = (float) data.teleportPos.x;
            data.lastY = (float) data.teleportPos.y;
            data.lastZ = (float) data.teleportPos.z;
            if (Util.distance(x, y, z, (float) data.teleportPos.x, (float) data.teleportPos.y, (float) data.teleportPos.z) > 1.7) {
                event.setCancelled(true);
                player.teleport(new Vector3(data.teleportPos.x, data.teleportPos.y, data.teleportPos.z));
                return;
            } else
                data.isTeleporting = false;
        }

        // Speed calculations
        data.currentSpeed = Util.distance(x, 0, z, data.lastX, 0, data.lastZ); // Get the current horizontal distance from the last position
        if (player.isSprinting()) data.sprintingTicks = 10;
        else data.sprintingTicks--; // Sprint tick stuff
        data.speedMultiplier = MotionUtils.getSpeedMultiplier(player); // Get speed multiplier from speed potions
        if (!data.isPlayerConsideredSprinting())
            data.speedMultiplier *= 0.75f; // Check if the player is actually sprinting
        if (player.isSneaking()) data.speedMultiplier *= 0.75f; // Check if the player is sneaking
        data.jumpTicks--; // Decrease jump ticks
        data.lerpTicks--; // Decrease lerp ticks

        // View vector calculation
        double cYaw = (packet.yaw + 90.0) * MotionUtils.DEG;
        double cPitch = packet.pitch * -MotionUtils.DEG;
        data.viewVector = new Vector3(Math.cos(cYaw), Math.sin(cPitch), Math.sin(cYaw));

        Item chestplateItem = player.getInventory().getArmorItem(1);
        boolean isWearingElytra = chestplateItem instanceof ItemElytra;

        // Check whether we are actually standing on a block
        Block block = WorldUtils.getNearestSolidBlock(x, y, z, player.level, 2); // Retrieve nearest solid block
        Block blockAboveNearestBlock = player.level.getBlock((int) block.x, (int) block.y + 1, (int) block.z);
        data.onGround = !(block instanceof BlockAir); // Set on ground if block is not air (solid)
        data.onGroundAlternate = !(player.level.getBlock((int) x, (int) (y - 1.62), (int) z) instanceof BlockAir); // Check if we are DIRECTLY under a block

        // Stair check - we also have to check for the above block because sometimes it
        if (block instanceof BlockStairs || blockAboveNearestBlock instanceof BlockStairs)
            data.staircaseLenientTicks = 20;
        else data.staircaseLenientTicks--;

        // Gravity changing blocks
        if (block instanceof BlockLadder || block instanceof BlockWater || block instanceof BlockWaterStill || block instanceof BlockLava || block instanceof BlockLavaStill || block instanceof BlockVine || block instanceof BlockCobweb || block instanceof BlockSlime || block instanceof BlockHayBale || block instanceof BlockBed)
            data.gravityLenientTicks = 20;
        else data.gravityLenientTicks--;

        // Friction changing blocks
        if (block instanceof BlockIce || block instanceof BlockIcePacked || block instanceof BlockWater || block instanceof BlockWaterStill || block instanceof BlockLava || block instanceof BlockLavaStill || block instanceof BlockSlime || block instanceof BlockHayBale || block instanceof BlockBed)
            data.frictionLenientTicks = 20;
        else data.frictionLenientTicks--;

        // Check for a block above us
        if (!(WorldUtils.getNearestSolidBlock(x, y + 2.53, z, player.level, 1) instanceof BlockAir))
            data.blockAboveLenientTicks = 20;
        else data.blockAboveLenientTicks--;


        // Adjust speed to environment
        if (data.lerpTicks > 0) // Damage ticks
        {
            data.currentSpeed /= 1.0 + (data.lastLerpStrength * 1.5f);
            data.startFallPos = new Vector3(x, y, z);
            data.fallingTicks = 0;
        }

        if (data.frictionLenientTicks > 0) data.currentSpeed /= 2.0;
        if (data.blockAboveLenientTicks > 0) data.currentSpeed /= 2.4;
        if (data.staircaseLenientTicks > 0) data.currentSpeed /= 2.4;
        if (isWearingElytra) data.currentSpeed /= 4.0;

        packet.onGround = data.onGroundAlternate; // Our information is more accurate - we do NOT trust the client with the onGround value located inside the packet.
        data.resetMove = false;

        // Cycles through and runs Moonlight's checks.
        if (player.gamemode != 1 && player.isAlive()) { // TODO: Implement this better and create creative mode specific checks or adjust checks to fit creative mode's movements. Creative mode has movement mechanics such as flying which can false flag a lot of checks.
            for (Check check : Moonlight.checks) { // Loop through all checks in Moonlight's list
                check.check(packet, data, player); // Call the check function that wants a MovePlayerPacket
            }
        }

        if(data.resetMove) // If we have to reset move
        {
            event.setCancelled(true);
            return;
        }

        if (data.onGroundAlternate) // set onground state
            data.lastGroundPos = new Vector3(x, y - (1.62 - 0.000001), z);

        // Calculate off/on ground ticks
        if (data.onGround) { // If we are onGround
            // Clear all info that is not relevant anymore
            data.offGroundTicks = 0;
            data.startFallPos = null;
            data.predictedFallAmount = 0;
            data.fallingTicks = 0;

            // Increment the onGround tick value.
            data.onGroundTicks++;
        } else {
            data.onGroundTicks = 0; // Clear onGround ticks because we are now off ground.
            data.offGroundTicks++; // Increment the offGround tick value.

            // Determine the position of when the player actually starts falling.
            // We have 3 methods of this:
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
            // 1.) Check if the current vertical position is less than the last movement's vertical position
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
            // 2.) Check if offGround ticks has reached 6 - this number comes from the amount of offGround
            // ticks it takes to start falling after a vanilla jump.
            // =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
            // 3.) Check if the fall distance from last vertical position is abnormal. This is more of a
            // check to detect fly cheats faster instead of falling

            Effect jumpBoostEffect = player.getEffect(8); // Get jump boost effect
            Effect levitationEffect = player.getEffect(24); // Get levitation effect
            if (levitationEffect != null || isWearingElytra) data.offGroundTicks = 0; // Fix levitation false flags

            float differenceValue = (data.lastY - y);
            if (data.startFallPos == null && (y < data.lastY || data.offGroundTicks >= (jumpBoostEffect != null ? 8 + jumpBoostEffect.getAmplifier() : 6) || differenceValue > 0.0 && differenceValue < 0.07839966)) // Check if the start fall position is already defined, if not, we then use the mentioned methods
            {
                data.startFallPos = new Vector3(x, y, z); // Set the start fall position value
                if (jumpBoostEffect != null) data.offGroundTicks = 7; // Jump boost fix
            } else
                data.fallingTicks++; // We have already started falling - increment falling ticks.
        }

        // Set/get data for when the next packet is received.
        data.lastSpeed = data.currentSpeed;
        data.lastX = x;
        data.lastY = y;
        data.lastZ = z;
        data.lastPitch = packet.pitch;
        data.lastYaw = packet.yaw;
    }
}
