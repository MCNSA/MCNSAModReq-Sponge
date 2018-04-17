package uk.co.maboughey.moqreq;


import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import uk.co.maboughey.moqreq.commands.CommandManager;
import uk.co.maboughey.moqreq.database.DatabaseManager;
import uk.co.maboughey.moqreq.utils.Configuration;
import uk.co.maboughey.moqreq.utils.Log;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.nio.file.Path;

@Plugin(id="mcnsamodreq", name="MCNSA ModReq", version="1.0-Sponge")
public class ModReq {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer plugin;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    public static Boolean isEnabled = true;
    public static Log log;

    public static Configuration config;
    public static DatabaseManager databaseManager;

    private CommandManager commandManager;


    @Listener
    public void preInit(GamePreInitializationEvent event) {
        //Setup logger
        log = new Log(logger);

        log.info("Loading Configuration");
        config = new Configuration(configDir);

        log.info("Loading Commands");
        commandManager = new CommandManager(plugin);

        log.info("Loading Database");
        databaseManager = new DatabaseManager();
    }

    @Listener
    public void onLogin(ClientConnectionEvent.Join event){
        Player player = event.getTargetEntity();
        //Notify those with permissions if there is any requests open
        if (player.hasPermission("modreq.notify.mod")) {
            //Has permission so lets tell them about mod messages
            Messaging.notifyModLogin(player);
        }
        else if (player.hasPermission("modreq.notify.player")) {
            //Notify player about their own
            Messaging.notifyPlayerLogin(player);
        }
    }
}
