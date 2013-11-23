package com18bytes.plugindev.autodeop.listeners;

import com18bytes.plugindev.autodeop.main.AutoDeOp;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;

import java.util.List;

public class OPBlock implements Listener {
	
	private AutoDeOp plugin;
	
	public OPBlock(AutoDeOp plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		if(plugin.getConfig().getBoolean("Block.OPingNonAllowedOPs")){
			
			List<String> tempList = plugin.getConfig().getStringList("Players.AllowedOp");
			
			// Split the command
			String[] args = event.getMessage().split(" ");
			// Get rid of the first /
			String cmd = args[0].replace("/", "");
		    if(cmd.equalsIgnoreCase("op")) {
		    	// Cancel it
		    	for (String s : tempList) {
		    		if (s.equalsIgnoreCase(args[1].toLowerCase())) {
		    			return;
		    		}
		    	}
		    	event.setCancelled(true);
                plugin.getConfig().set("Stats.blockedByCommand", plugin.getConfig().getInt("Stats.blockedByCommand", 0) + 1);
		    }
		}
	}
	
	@EventHandler
	public void consoleCMD(ServerCommandEvent event){
		if(plugin.getConfig().getBoolean("Block.OPInConsole")){
			// Get rid of the first /
			String cmd = event.getCommand().replace("/", "");
            if (cmd.length() < 2) {
                return;
            }
			if(cmd.toLowerCase().charAt(0) == 'o' && cmd.toLowerCase().charAt(1) == 'p') {
				event.setCommand("");
                plugin.getConfig().set("Stats.blockedInConsole", plugin.getConfig().getInt("Stats.blockedInConsole", 0) + 1);
			}
		}
	}
}
