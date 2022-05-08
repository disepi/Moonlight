package com.disepi.moonlight.anticheat;

import cn.nukkit.Player;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.check.combat.killaura.KillauraA;
import com.disepi.moonlight.anticheat.check.combat.killaura.KillauraB;
import com.disepi.moonlight.anticheat.check.exploit.badpackets.BadPackets;
import com.disepi.moonlight.anticheat.check.exploit.timer.Timer;
import com.disepi.moonlight.anticheat.check.motion.fly.FlyA;
import com.disepi.moonlight.anticheat.check.motion.speed.SpeedA;
import com.disepi.moonlight.anticheat.check.motion.speed.SpeedB;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.utils.FakePlayer;
import com.disepi.moonlight.utils.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Moonlight {
    public static Map<Player, PlayerData> players = new HashMap<Player, PlayerData>(); // Stores data of players in the world
    public static CopyOnWriteArrayList<Check> checks = new CopyOnWriteArrayList<Check>(); // Stores all of the Check instances
    public static CopyOnWriteArrayList<FakePlayer> fakePlayers = new CopyOnWriteArrayList<FakePlayer>(); // Stores all of the instances of fake players

    // Configuration values
    public static boolean cancelNukkitInvalidMove = true;

    // Sets up the list of checks
    public static void initializeChecks() {
        checks.clear();
        checks.add(new SpeedA());
        checks.add(new SpeedB());
        checks.add(new FlyA());
        checks.add(new Timer());
        checks.add(new KillauraA());
        checks.add(new KillauraB());
        checks.add(new BadPackets());
    }

    // Returns the data instance of a player
    public static PlayerData getData(Player player) {
        return players.get(player);
    }

    // Inserts a data instance for the target player
    public static void addData(Player player) {
        Moonlight.players.put(player, new PlayerData(player));
    }

    // Removes a data instance for the target player
    public static void removeData(Player player) {
        Moonlight.players.remove(player);
    }

    // Sends a message to all operators currently online - and the console
    public static void sendMessageToModerators(Player temp, String message) {
        // TODO: Add permissions and such for this, uses operator list for now
        for (String op : temp.getServer().getOps().getKeys()) // Get all operator names
            temp.getServer().getPlayer(op).sendMessage(message); // Send them the message
        Util.log(message); // Print to console too for easy debugging and access
    }

}
