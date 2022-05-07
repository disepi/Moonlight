package com.disepi.moonlight.anticheat.check.motion.speed;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.MovePlayerPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SpeedB extends Check {
    // Constructor
    public SpeedB() {
        super("SpeedB", "Off-ground movement did not follow friction rules", 8);
    }

    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        if(d.onGround) return;
        //double expected = MotionUtils.getExpectedSpeedValue(d.offGroundTicks);
       // if(d.currentSpeed < expected)
        //    fail(p, "expected=" + expected + ", received=" + d.currentSpeed);
    }

}
