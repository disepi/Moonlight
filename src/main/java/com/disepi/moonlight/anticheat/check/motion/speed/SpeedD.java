package com.disepi.moonlight.anticheat.check.motion.speed;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MovePlayerPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;

public class SpeedD extends Check {
    // Constructor
    public SpeedD() {
        super("SpeedD", "Invalid vertical jump movement", 8);
    }

    public void doFailCheck(Player p, PlayerData d, float value, float expected) {
        fail(p, "height=" + value + ", expected=" + expected + ", offGroundTicks=" + d.offGroundTicks);
        lagback(p, d);
        violate(p, d, 1, true);
    }

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        reward(d, 0.25f); // Violate

        boolean hasJumpBoost = d.isJumpBoostActive();

        // Catches teleports
        float value = e.y - d.lastY;
        float expectedTeleportValue = hasJumpBoost ? 1.0f + d.getExtraJumpValue() : 1.0f;
        if (value >= expectedTeleportValue) doFailCheck(p, d, value, expectedTeleportValue);

        if (!d.onGround && value > 0.0f && d.offGroundTicks < 6) {
            for (float expected : MotionUtils.GRAVITY_JUMP_VALUES) {
                if (expected == value) return;
            }
            doFailCheck(p, d, value, 0);
        }

    }

}
