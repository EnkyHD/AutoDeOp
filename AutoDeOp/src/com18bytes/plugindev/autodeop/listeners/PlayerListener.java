package com18bytes.plugindev.autodeop.listeners;

/**
 * @author EnkyHD
 * @version 0.9
 * @email combinecraftowner@gmail.com
 * @donate Via Paypal: combinecraftowner@gmail.com
 * 
 *
 */

import com18bytes.plugindev.autodeop.main.AutoDeOp;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerListener implements Listener {
	
	private AutoDeOp plugin;
	
	public PlayerListener(AutoDeOp plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	private void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		String playerJoined = player.getName();
		String allowedOp = plugin.getConfig().getString("Players.AllowedOp");
		if (player.isOp())
		{
			if (!(allowedOp.toLowerCase().contains(playerJoined.toLowerCase())))
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "deop " + playerJoined);
                plugin.getConfig().set("Stats.playersDeopOnJoin", plugin.getConfig().getInt("Stats.playersDeopOnJoin", 0) + 1);
                plugin.saveConfig();
			}
			if (plugin.updateAvailable == true){
			    player.sendMessage(ChatColor.LIGHT_PURPLE + "[AutoDeOp] A new version is available: v." + plugin.versionAvailable + " http://dev.bukkit.org/server-mods/autodeop/");
                player.sendMessage(ChatColor.RED + "[AutoDeOp] Important to download! Fixes bugs and has new features!");
			}
		}
	
	}
	@EventHandler
	private void onGM(PlayerGameModeChangeEvent event)
	{
		Player playerGM = event.getPlayer();
		String playerModified = playerGM.getName();
		String allowedGameMode = plugin.getConfig().getString("Players.AllowedGamemode");
		if (plugin.getConfig().getBoolean("Block.PlayersFromChangingGameMode"))
		{
			if (!(allowedGameMode.toLowerCase().contains(playerModified.toLowerCase())))
            {
				if (playerGM.getGameMode() != plugin.getServer().getDefaultGameMode())
				{
                    event.setCancelled(true);
                    playerGM.setGameMode(plugin.getServer().getDefaultGameMode());
				}
			}
		}
	}
}
