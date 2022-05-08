package com.disepi.moonlight.events;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.MovePlayerPacket;
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
        data.currentSpeed = Util.distance(x, 0, z, data.lastX, 0, data.lastZ); // Get the current horizontal distance from the last position
        data.speedMultiplier = MotionUtils.getSpeedMultiplier(player); // Get speed multiplier from speed potions
        if (!player.isSprinting()) data.speedMultiplier *= 0.75f; // Check if the player is actually sprinting

        // Check whether we are actually standing on a block
        data.onGround = WorldUtils.isConsideredOnGround(x, y, z, player.level); // Check if we around a block
        data.onGroundAlternate = WorldUtils.isConsideredSolid(player.level, x, y - 1.62, z); // Check if we are DIRECTLY under a block

        packet.onGround = data.onGroundAlternate; // Our information is more accurate - we do NOT trust the client with the onGround value located inside the packet.

        // Cycles through and runs Moonlight's checks.
        if (player.gamemode != 1 && player.isAlive()) { // TODO: Implement this better and create creative mode specific checks or adjust checks to fit creative mode's movements. Creative mode has movement mechanics such as flying which can false flag a lot of checks.
            for (Check check : Moonlight.checks) { // Loop through all checks in Moonlight's list
                check.check(packet, data, player); // Call the check function that wants a MovePlayerPacket
            }
        }

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

            float differenceValue = (data.lastY - y);
            if (data.startFallPos == null && (y < data.lastY || data.offGroundTicks >= 6 || differenceValue > 0.0 && differenceValue < 0.07839966)) // Check if the start fall position is already defined, if not, we then use the mentioned methods
                data.startFallPos = new Vector3(x, y, z); // Set the start fall position value
            else
                data.fallingTicks++; // We have already started falling - increment falling ticks.
        }

        // Set/get data for when the next packet is received.
        data.lastSpeed = data.currentSpeed;
        data.lastX = x;
        data.lastY = y;
        data.lastZ = z;
    }
}
