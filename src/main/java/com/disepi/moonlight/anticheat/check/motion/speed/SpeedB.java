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

    public void doFailCheck(PlayerData d, Player p, float value) {
        lagback(p, d);
        violate(p, d, value, true); // Violate
    }

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        reward(d, 0.05f); // Violation reward

        if (e.y < -100) return;

        if (d.onGround && d.onGroundTicks > 10) // If we are on the ground and speed is too high
        {
            if (d.currentSpeed > (d.collidedHorizontallyTicks > 0 ? 0.5 : 0.2815f)) {
                fail(p, "speed=" + d.currentSpeed + ", onGroundTicks=" + d.onGroundTicks + ", type=GROUNDED, vl=" + (int) getViolationScale(d)); // Failed check
                doFailCheck(d, p, 0.5f);
            }
        } else { // We are not on the ground
            double minSpeed = MotionUtils.getMinimumSpeedValue() + 0.01f;
            double expected = (MotionUtils.getExpectedSpeedValue(d.offGroundTicks) * 1.01) * (1 + ((d.speedMultiplier - 1) * 0.5f)); // Get expected value, multiply it by a tiny amount otherwise it can false flag in some cases
            if (d.currentSpeed > expected) // If expected speed is lower than the speed we received
            {
                fail(p, "expected=" + expected + ", received=" + d.currentSpeed + ", offGroundTicks=" + d.offGroundTicks + ", type=AIR, vl=" + (int) getViolationScale(d)); // Failed check
                doFailCheck(d, p, 1.25f);
            } else if (d.currentSpeed > minSpeed && d.lastSpeed > minSpeed) {
                if (d.currentSpeed == d.lastSpeed) {
                    fail(p, "received=" + d.currentSpeed + ", offGroundTicks=" + d.offGroundTicks + ", type=AIR_FRICTION_INVALID, vl=" + (int) getViolationScale(d)); // Failed check
                    doFailCheck(d, p, 1.25f);
                } else if (d.currentSpeed > d.lastSpeed && d.offGroundTicks != 0) {
                    fail(p, "received=" + d.currentSpeed + ", last=" + d.lastSpeed + ", offGroundTicks=" + d.offGroundTicks + ", type=AIR_FRICTION_INVALID_TYPE2, vl=" + (int) getViolationScale(d)); // Failed check
                    doFailCheck(d, p, 1.25f);
                }
            }
        }
    }

}
