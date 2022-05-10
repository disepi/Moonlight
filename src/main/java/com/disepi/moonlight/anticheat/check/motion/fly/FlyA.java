package com.disepi.moonlight.anticheat.check.motion.fly;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MovePlayerPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;
import com.disepi.moonlight.utils.Util;

public class FlyA extends Check {
    // Constructor
    public FlyA() {
        super("FlyA", "Invalid vertical movement", 5);
    }

    // This is a simple value check that has 2 detections.
    // Check failure type "FLY" occurs when the player's expected downward velocity was bigger than the player's actual downward velocity.
    // Check failure type "GLIDE" occurs when the player's downward velocity is too small to occur in vanilla gameplay.

    // GLIDE type check helper function
    public void checkSmallMovement(Player p, PlayerData d, float difference, float value)
    {
        if(difference < value) // Check is difference is smaller than value
        {
            fail(p, "difference=" + difference + ", type=glide, vl=" + (int)getViolationScale(d)); // We have failed the check
            lagback(p, d); // Lagback the player
            violate(p,d,1.5f,true); // Violate check
        }
    }

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        reward(d, 0.025f); // Violation reward

        // GLIDE type check
        float diffVal = Math.abs(e.y - d.lastY);
        if(!d.onGround)
            checkSmallMovement(p,d,diffVal,0.0029997826f);
        else return;

        if (d.startFallPos == null || d.offGroundTicks < 10 || d.gravityLenientTicks > 0 || e.y < 0 || d.lerpTicks > 0)
            return; // Do not check if the player has not started falling.

        double gravity = d.startFallPos.y - e.y; // Player has started falling, get actual fall distance
        double predictedGravity = MotionUtils.getExpectedFallValue(d.offGroundTicks - 7); // Get predicted fall distance by the server

        // Gravity prediction
        if (predictedGravity - 0.0005 > gravity) // Check if expected velocity was higher than what we received from the client
        {
            fail(p, "gravity=" + gravity + ", predictedGravity=" + predictedGravity + ", offset=" + (predictedGravity - gravity) + ", type=fly, vl=" + (int)getViolationScale(d)); // We have failed the check
            lagback(p, d);
            violate(p,d,1,true);
        }

        // GLIDE type check
        if (d.offGroundTicks > 10 && gravity < 0.75) // Check if received velocity is impossible in vanilla gameplay
        {
            fail(p, "gravity=" + gravity + ", predictedGravity=" + predictedGravity + ", offset=" + (predictedGravity - gravity) + ", type=glide, vl=" + (int)getViolationScale(d)); // We have failed the check
            lagback(p, d);
            violate(p,d,1.5f,true);
        }

        // GLIDE type check
        checkSmallMovement(p,d,diffVal,0.44479942f);
    }

}
