package com.disepi.moonlight.events;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import com.disepi.moonlight.utils.FakePlayer;

public class onPlayerDamage implements Listener {
    // Called upon an entity taking damage
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof FakePlayer) event.setCancelled(); // Fake players are not meant to be damaged
    }
}
