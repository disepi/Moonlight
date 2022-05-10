package com.disepi.moonlight.anticheat.check.motion.fly;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MovePlayerPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;

public class FlyA extends Check {
    // Constructor
    public FlyA() {
        super("Fly", "Invalid vertical movement", 8);
    }

    // This is a simple value check that has 2 detections.
    // Check failure type "FLY" occurs when the player's expected downward velocity was bigger than the player's actual downward velocity.
    // Check failure type "GLIDE" occurs when the player's downward velocity is too small to occur in vanilla gameplay.

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        if (d.startFallPos == null || d.onGround || d.offGroundTicks < 10 || d.gravityLenientTicks > 0 || e.y < 0 || d.lerpTicks > 0)
            return; // Do not check if the player has not started falling.

        double gravity = d.startFallPos.y - e.y; // Player has started falling, get actual fall distance
        double predictedGravity = MotionUtils.getExpectedFallValue(d.offGroundTicks - 7); // Get predicted fall distance by the server

        if (predictedGravity - 0.0005 > gravity) // Check if expected velocity was higher than what we received from the client
        {
            this.fail(p, "gravity=" + gravity + ", predictedGravity=" + predictedGravity + ", offset=" + (predictedGravity - gravity) + ", type=fly"); // We have failed the check
            this.lagback(p, d);
        }

        if (d.offGroundTicks > 10 && gravity < 0.75) // Check if received velocity is impossible in vanilla gameplay
        {
            this.fail(p, "gravity=" + gravity + ", predictedGravity=" + predictedGravity + ", offset=" + (predictedGravity - gravity) + ", type=glide"); // We have failed the check
            this.lagback(p, d);
        }
    }

}
