package com.disepi.moonlight.anticheat.check.combat.killaura;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.math.Vector3;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.MotionUtils;

public class KillauraA extends Check {

    public KillauraA() {
        super("KillauraA", "Not aiming at target", 8);
    }

    public void check(EntityDamageByEntityEvent e, PlayerData d, Player p) {
        double cYaw = (p.yaw + 90.0) * MotionUtils.DEG;
        double cPitch = p.pitch * -MotionUtils.DEG;
        double distance = p.getPosition().distance(e.getEntity().getPosition());
        double expansion = 0.4;
        double verticalExpansion = 0.5;
        if (!e.getEntity().getBoundingBox().expand(expansion, verticalExpansion, expansion).isVectorInside(new Vector3(p.x + (Math.cos(cYaw) * Math.cos(cPitch) * distance), p.y + 0.5 + (Math.sin(cPitch) * distance), p.z + (Math.sin(cYaw) * Math.cos(cPitch) * distance))))
            this.fail(p, "attacker was not aiming at target");
    }

}
