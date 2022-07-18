package com.disepi.moonlight.anticheat.check.motion.speed;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MovePlayerPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class SpeedC extends Check {
    // Constructor
    public SpeedC() {
        super("SpeedC", "Invalid strafe movement", 24);
    }

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        reward(d, 0.1f); // Violate
        /*if (!d.onGround && d.offGroundTicks > 1 && d.currentSpeed > 0.275) {
            double expected = (MotionUtils.getExpectedSpeedValue(d.offGroundTicks) * d.speedMultiplier) * 1.75;

            double predictedVelX = d.viewVector.x * expected;
            double predictedVelZ = d.viewVector.z * expected;
            double velX = e.x - p.lastX;
            double velZ = e.z - p.lastZ;

            if (!(velZ <= 0.0f && velZ > predictedVelZ || velZ >= 0.0f && velZ < predictedVelZ || velX <= 0.0f && velX > predictedVelX || velX >= 0.0f && velX < predictedVelX)) {
                fail(p, "expectedX=" + predictedVelX + ", expectedZ=" + predictedVelZ + ", x=" + velX + ", z=" + velZ + ", vl=" + (int) getViolationScale(d));
                lagback(p, d);
                violate(p, d, 1, true); // Violate
            }
        }*/
    }

}
