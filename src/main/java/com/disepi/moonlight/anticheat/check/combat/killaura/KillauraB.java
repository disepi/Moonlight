package com.disepi.moonlight.anticheat.check.combat.killaura;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.MovePlayerPacket;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.FakePlayer;
import com.disepi.moonlight.utils.MotionUtils;
import com.disepi.moonlight.utils.PlayerUtils;
import com.disepi.moonlight.utils.Util;

public class KillauraB extends Check {
    // Constructor
    public KillauraB() {
        super("KillauraB", "Attacking an invalid entity", 8);
    }

    // This check summons an invisible entity behind the attacker.
    // The invisible entity will follow the players head to stay behind the attacker at all times to avoid possible false detections (example: legit player accidentally hitting entity)
    // Game modifications with the feature "killaura" will attack this invisible entity while attempting to attack other entities.
    // This attack will then cause the player to fail this check.

    // Our attack callback
    public void check(EntityDamageByEntityEvent e, PlayerData d, Player p) {
        // If the attacker hits a fake player, they fail the check
        if (e.getEntity() instanceof FakePlayer)
            this.fail(p, "attacking an invalid entity"); // Check has been failed by the user
        else // Instead, if we don't hit a fake player but a real player/mob/entity, do this instead:
        {
            if (d.fake == null) // If we do not currently have a fake player active, we make one
            {
                d.fake = PlayerUtils.getFakePlayer(p); // Assign the fake player to the player's data
                d.fake.spawn(p); // Spawn it for the user
            } else // If we already have a fake player active
                d.fake.ticks = 0; // Reset the timer so the fake player lasts longer
        }
    }

    // Our move callback
    public void check(MovePlayerPacket e, PlayerData d, Player p) {
        if (d.fake == null)
            return; // If the fake player does not exist for the player, we don't need to adjust it, so return.
        d.fake.ticks++; // Increment the amount of ticks the fake player has existed for

        if (d.fake.ticks > 100) // If we do not attack another entity for 100 ticks (5 seconds), we de-spawn the entity
        {
            d.fake.despawnFromAll(); // De-spawn the entity
            d.fake = null; // Remove the fake player instance in our player data
            return; // Return to avoid errors
        }

        // This changes the fake player name tag every second to avoid circumvention
        if (d.fake.ticks % 20 == 0) // The game runs at 20 ticks per second, so we wait 20 ticks for a whole second
            d.fake.setNameTag(Util.generateRandomString(6 + Util.rnd.nextInt(6))); // Set the name tag to random characters

        // Change yaw/pitch to make the fake bot appear legitimate
        d.fake.pitch = -90 + Util.rnd.nextInt(180); // Pitch
        d.fake.yaw = -180 + Util.rnd.nextInt(360); // Yaw

        // Calculate a position that is behind the player
        double distance = 3.1; // The distance away from our player
        d.fake.setPosition(new Vector3(p.x + -(d.viewVector.x * distance), p.y + -(d.viewVector.y * distance), p.z + -(d.viewVector.z * distance)));
    }

}
