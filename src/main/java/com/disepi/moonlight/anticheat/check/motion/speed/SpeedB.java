package com.disepi.moonlight.anticheat.check.motion.speed;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MovePlayerPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;

public class SpeedB extends Check {
    // Constructor
    public SpeedB() {
        super("SpeedB", "Movement did not follow friction rules", 8);
    }

    // This check compares what the player's speed should be by using expected values.
    // If the player's speed exceeds the expected value, the player fails the check.
    // This doubles off as an ordinary speed check and a friction check.

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        if (d.onGroundAlternate && d.onGroundTicks > 15 && d.currentSpeed > MotionUtils.GROUND_SPEED_DEFAULT) // If we are on the ground and speed is too high
            fail(p, "speed=" + d.currentSpeed + ", onGroundTicks=" + d.onGroundTicks + ", type=GROUNDED"); // Failed check
        else { // We are not on the ground
            double expected = MotionUtils.getExpectedSpeedValue(d.offGroundTicks) * 1.01; // Get expected value, multiply it by a tiny amount otherwise it can false flag in some cases
            if (d.currentSpeed > expected) // If expected speed is lower than the speed we received
                fail(p, "expected=" + expected + ", received=" + d.currentSpeed + ", offGroundTicks=" + d.offGroundTicks + ", type=AIR"); // Failed check
        }
    }

}
