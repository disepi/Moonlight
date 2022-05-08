package com.disepi.moonlight;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.TextFormat;
import com.disepi.moonlight.anticheat.Moonlight;
import com.disepi.moonlight.anticheat.player.PlayerData;
import com.disepi.moonlight.events.*;
import com.disepi.moonlight.utils.FakePlayer;
import com.disepi.moonlight.utils.PlayerUtils;
import com.disepi.moonlight.utils.Util;

public class Main extends PluginBase {
    @Override
    public void onEnable() {
        super.onEnable();

        // Configurations
        saveDefaultConfig();

        // Logger + show that we loaded in console
        Util.log = getLogger();
        Util.log(TextFormat.GREEN + "Loaded!");

        // Game event listeners
        PluginManager mgr = getServer().getPluginManager();
        mgr.registerEvents(new onPlayerMove(), this);
        mgr.registerEvents(new onPlayerJoin(), this);
        mgr.registerEvents(new onPlayerAttackEntity(), this);
        mgr.registerEvents(new onPlayerDamage(), this);
        mgr.registerEvents(new onPlayerMovedWrongly(), this);

        // Setup invisible skin for FakePlayer
        PlayerUtils.setupSkinStream();

        // Initialize and start up all checks
        Moonlight.initializeChecks();

        // Initialize players that were already in the server - maybe "/reload" was called
        for (Player plr : getServer().getOnlinePlayers().values())
            Moonlight.addData(plr);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        // Inform user
        Util.log(TextFormat.GREEN + "Unloading!");

        // Destroy all instances of fake players
        for (PlayerData plr : Moonlight.players.values())
            if (plr.fake != null)
                plr.fake.despawnFromAll();
        for (FakePlayer plr : Moonlight.fakePlayers) {
            if (plr != null)
                plr.despawnFromAll();
        }

        // Empty the player data list
        Moonlight.players.clear();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return super.onCommand(sender, command, label, args);
    }
}
