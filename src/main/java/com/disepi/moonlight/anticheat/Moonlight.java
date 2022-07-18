package com.disepi.moonlight.anticheat;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import com.disepi.moonlight.anticheat.check.Check;
import com.disepi.moonlight.anticheat.check.combat.killaura.KillauraA;
import com.disepi.moonlight.anticheat.check.combat.killaura.KillauraB;
import com.disepi.moonlight.anticheat.check.exploit.badpackets.BadPackets;
import com.disepi.moonlight.anticheat.check.exploit.noswing.NoSwing;
import com.disepi.moonlight.anticheat.check.exploit.timer.Timer;
import com.disepi.moonlight.anticheat.check.motion.speed.SpeedA;
import com.disepi.moonlight.anticheat.check.player.scaffold.ScaffoldA;
import com.disepi.moonlight.anticheat.check.player.scaffold.ScaffoldB;
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
    public static int checkAmount = 0; // Check amount
    public static String stylizedChatString = TextFormat.DARK_GRAY + "[" + TextFormat.DARK_AQUA + "moonlight" + TextFormat.DARK_GRAY + "] " + TextFormat.WHITE;
    public static String kickString = "Invalid byte buffer received from client";

    // Configuration values
    public static boolean cancelNukkitInvalidMove = true;

    // Sets up the list of checks
    public static void initializeChecks() {
        checks.clear();

        checks.add(new SpeedA());
        //checks.add(new SpeedB());
        //checks.add(new SpeedC());
        //checks.add(new SpeedD());
        //checks.add(new FlyA());
        checks.add(new KillauraA());
        checks.add(new KillauraB());
        checks.add(new ScaffoldA());
        checks.add(new ScaffoldB());
        checks.add(new BadPackets());
        checks.add(new NoSwing());
        checks.add(new Timer());
    }

    // Returns the data instance of a player
    public static PlayerData getData(Player player) {
        return players.get(player);
    }

    // Inserts a data instance for the target player
    public static void addData(Player player) {
        if (!player.isOp())
            Moonlight.players.put(player, new PlayerData(player));
    }

    // Removes a data instance for the target player
    public static void removeData(Player player) {
        Moonlight.players.remove(player);
    }

    // Sends a message to all operators currently online - and the console
    public static void sendMessageToModerators(Player temp, String message) {
        // TODO: Add permissions and such for this, uses operator list for now
        for (Player p : temp.getServer().getOnlinePlayers().values()) // Get all operator names
        {
            if (p.isOp())
                p.sendMessage(message);
        }
        Util.log(message); // Print to console too for easy debugging and access
    }

}
