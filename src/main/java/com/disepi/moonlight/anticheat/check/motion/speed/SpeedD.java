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

    public void doFailCheck(Player p, PlayerData d, float value) {
        fail(p, "height=" + value);
        lagback(p, d);
        violate(p, d, 1, true);
    }

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        reward(d, 0.25f); // Violate

        // Catches teleports
        float value = e.y - d.lastY;
        if (value >= 1.0f) doFailCheck(p, d, value);

        if (d.onGroundAlternate || !d.onGroundAlternateLast || d.offGroundTicks != 0 || d.gravityLenientTicks > 0 || d.blockAboveLenientTicks > 0)
            return;

        // Jump height
        if (value > 0 && value < 0.33319998f && value != 0.24810028f) // TODO: remove fixed value, fixed value only occurs when jumping inside a hole
            doFailCheck(p, d, value);
    }

}
