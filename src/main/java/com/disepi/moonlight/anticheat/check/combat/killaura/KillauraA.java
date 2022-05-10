package com.disepi.moonlight.anticheat.check.combat.killaura;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.math.Vector3;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class KillauraA extends Check {

    public KillauraA() {
        super("KillauraA", "Not aiming at target", 8);
    }


    public void check(EntityDamageByEntityEvent e, PlayerData d, Player p) {
        if (d.isTouchscreen) return;
        double distance = p.getPosition().distance(e.getEntity().getPosition());
        double expansion = 0.6;
        double verticalExpansion = 0.6;
        if (!e.getEntity().getBoundingBox().expand(expansion, verticalExpansion, expansion).isVectorInside(new Vector3(p.x + (d.viewVector.x * distance), p.y + 0.5 + (d.viewVector.y * distance), p.z + (d.viewVector.z * distance))))
            this.fail(p, "attacker was not aiming at target");
    }

}