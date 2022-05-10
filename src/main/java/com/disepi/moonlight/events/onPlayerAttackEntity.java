package com.disepi.moonlight.events;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;

public class onPlayerAttackEntity implements Listener {
    // Called upon an entity attacking another entity
    @EventHandler
    public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return; // If the attacker is not a player (for example Zombie hitting another Zombie), we skip the check
        Player player = (Player) event.getDamager(); // Returns the player
        PlayerData data = Moonlight.getData(player); // Get the data
        if(data == null) return;

        for (Check check : Moonlight.checks) { // Loop through all of Moonlight's checks
            check.check(event, data, player); // Call the check function that wants an "EntityDamageByEntityEvent"
        }
    }
}
