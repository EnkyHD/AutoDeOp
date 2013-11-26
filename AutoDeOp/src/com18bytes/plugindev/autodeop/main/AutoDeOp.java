package com18bytes.plugindev.autodeop.main;

/**
 * @author EnkyHD
 * @version 0.9
 * @email combinecraftowner@gmail.com
 * @donate Via Paypal: combinecraftowner@gmail.com
 *
 */


import com18bytes.plugindev.autodeop.listeners.CommandExecute;
import com18bytes.plugindev.autodeop.listeners.OPBlock;
import com18bytes.plugindev.autodeop.listeners.PlayerListener;
import com18bytes.plugindev.autodeop.utils.Metrics;
import com18bytes.plugindev.autodeop.utils.Metrics.Graph;
import com18bytes.plugindev.autodeop.utils.Updater;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;


public class AutoDeOp extends JavaPlugin
{

	public Logger log;
    public File filePlayers;
    public FileConfiguration configDonators;
    public boolean updateAvailable = false;
    public String versionAvailable = "";
	
	public void onEnable()
	{
		this.log = this.getLogger();
		loadConfig();
		
		PluginDescriptionFile pluginFile = getDescription();
		
		log.info("[AutoDeOp] Version " + pluginFile.getVersion() + " enabled.");
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getCommand("autodeop").setExecutor(new CommandExecute(this));
		if (getConfig().getBoolean("Block OPing non-Allowed OPs") || getConfig().getBoolean("Block OPing in Console")){
			getServer().getPluginManager().registerEvents(new OPBlock(this), this);
		}
		if (getConfig().getBoolean("BackEnd.UpdateChecking", true)) {
            Updater updater = new Updater(this, 46947, this.getFile(), Updater.UpdateType.DEFAULT, true);
            if (updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE) {
                log.info("A new version is available: v." + updater.getLatestName());
                log.info("Get it from: " + updater.getLatestFileLink());
                updateAvailable = true;
                versionAvailable = updater.getLatestName();
            }
		}
		if (getConfig().getBoolean("BackEnd.Metrics", true)) {
			try {
		    	Metrics metrics = new Metrics(this);
                Graph protection = metrics.createGraph("Protection Count");
                protection.addPlotter(new Metrics.Plotter("Console") {
                    @Override
                    public int getValue() {
                        return getConfig().getInt("Stats.blockedInConsole", 0);
                    }
                });
                protection.addPlotter(new Metrics.Plotter("Command") {
                    @Override
                    public int getValue() {
                        return getConfig().getInt("Stats.blockedByCommand", 0);
                    }
                });
                protection.addPlotter(new Metrics.Plotter("Joining") {
                    @Override
                    public int getValue() {
                        return getConfig().getInt("Stats.playersDeopOnJoin", 0);
                    }
                });
		    	metrics.start();
			} catch (IOException e) {
				log.info("Failed to send stats to mcstats.org");
			}
		}
	}
	
	public void onDisable()
	{
		PluginDescriptionFile pluginFile = getDescription();
		log.info("[AutoDeOp] Version " + pluginFile.getVersion() + " disabled.");
	}
	
	public void loadConfig()
    {
        File file = new File(this.getDataFolder() + File.separator + "config.yml");
        
        
        if (!file.exists())
        {
            this.saveDefaultConfig();
        } else {
        	FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        	if (config.contains("Version Do Not Change") || config.getInt("BackEnd.VersionDONOTCHANGE", 1) == 1) {
        		file.delete();
        		File tempfile = new File(this.getDataFolder() + File.separator + "oldconfig.yml");
        		try {
					config.save(tempfile);
				} catch (IOException e) {
				}
        		updateConfig();
        	}
        }
	}
	
	private void updateConfig() {
		File tempfile = new File(this.getDataFolder() + File.separator + "oldconfig.yml");

		FileConfiguration oldC = YamlConfiguration.loadConfiguration(tempfile);
		this.saveDefaultConfig();
		this.getConfig().set("Players.AllowedOp", oldC.getStringList("Players.AllowedOp"));
		this.getConfig().set("Players.AllowedGamemode", oldC.getStringList("Players.AllowedGamemode"));
		this.getConfig().set("Block.OPingNonAllowedOPs", oldC.getBoolean("Block.OPingNonAllowedOPs"));
		this.getConfig().set("Block.OPInConsole", oldC.getBoolean("Block.OPInConsole"));
		this.saveConfig();
	}
}
