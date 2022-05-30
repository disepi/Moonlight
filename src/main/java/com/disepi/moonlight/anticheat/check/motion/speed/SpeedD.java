package com.disepi.moonlight.anticheat.check.motion.speed;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MovePlayerPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class SpeedD extends Check {
    float[] expected = {0.33319998f, 0.24810028f, 0.16479969f, 0.08310032f, 0.0029997826f};

    // Constructor
    public SpeedD() {
        super("SpeedD", "Invalid vertical jump movement", 8);
    }

    public void doFailCheck(Player p, PlayerData d, float value) {
        fail(p, "height=" + value + ", offGroundTicks=" + d.offGroundTicks);
        lagback(p, d);
        violate(p, d, 1, true);
    }

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        reward(d, 0.25f); // Violate

        // Catches teleports
        float value = e.y - d.lastY;
        if (value >= 1.0f) doFailCheck(p, d, value);

        if (d.gravityLenientTicks > 0)
            return;

        //if(!d.onGround)
        //    Util.log("test");

        if (!d.onGround && value > 0.0f && d.offGroundTicks != 0) {
            //  if(expected[d.offGroundTicks] != value)
            // doFailCheck(p,d,value);
        }
    }

}
