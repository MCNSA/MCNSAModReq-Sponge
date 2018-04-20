package uk.co.maboughey.moqreq.utils;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import uk.co.maboughey.moqreq.ModReq;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configuration {

    private final Path configDir;
    private final Path configFile;
    private final HoconConfigurationLoader configLoader;
    private CommentedConfigurationNode configNode;

    public static String DBHost;
    public static String DBName;
    public static String DBUser;
    public static String DBPassword;
    public static String ServerName;
    public static String DiscordModHook;
    public static String DiscordAdminHook;

    public Configuration(Path confDir) {
        this.configDir = confDir;
        this.configFile = Paths.get(configDir+"/config.conf");
        configLoader = HoconConfigurationLoader.builder().setPath(configFile).build();

        //First check if the config folder exists, create it if needed
        checkConfigDir();
        //Then check if the config file exists, creating a default one if needed
        checkConfFile();
    }

    private void checkConfigDir() {
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectories(configDir);
            }
            catch (IOException e) {
                //Disable plugin
                ModReq.isEnabled = false;
                ModReq.log.error("Error creating config directory: "+e.getMessage());
            }
        }
    }

    private void checkConfFile() {
        if (!Files.exists(configFile))
            try {
                //Log that we are loading default configuration and disable plugin
                ModReq.log.info("Loading default config. Plugin is disabled until you reload");
                ModReq.isEnabled = false;

                //Load default config
                Files.createFile(configFile);
                configNode = configLoader.load();
                configNode.getNode("DatabaseHost").setValue("localhost");
                configNode.getNode("DatabaseName").setValue("mcnsanotes");
                configNode.getNode("DatabaseUser").setValue("mcnsanotes");
                configNode.getNode("DatabasePassword").setValue("mcnsanotes");
                configNode.getNode("ServerName").setValue("Server");
                configNode.getNode("DiscordModHook").setValue("Empty");
                configNode.getNode("DiscordAdminHook").setValue("Empty");

                //Save default config
                configLoader.save(configNode);
            }
            catch (IOException e) {
                //Disable plugin
                ModReq.isEnabled = false;
                ModReq.log.error("Error creating config directory: "+e.getMessage());
            }
        else {
            //Enable the plugin
            ModReq.isEnabled = true;
            ModReq.log.warn("ModReq plugin is now enabled");
            load();
        }
    }

    public void load(){
        try{
            configNode = configLoader.load();
        }catch(IOException e){
            e.printStackTrace();
        }
        finally {
            DBHost = configNode.getNode("DatabaseHost").getString();
            DBName = configNode.getNode("DatabaseName").getString();
            DBUser = configNode.getNode("DatabaseUser").getString();
            DBPassword = configNode.getNode("DatabasePassword").getString();
            ServerName = configNode.getNode("ServerName").getString();
            DiscordModHook = "https://discordapp.com/api/webhooks/"+configNode.getNode("DiscordModHook").getString();
            DiscordAdminHook = "https://discordapp.com/api/webhooks/"+configNode.getNode("DiscordAdminHook").getString();
        }
    }

    public static String getDatabaseString() {
        return "jdbc:mysql://"+DBHost+"/"+DBName;
    }
}
