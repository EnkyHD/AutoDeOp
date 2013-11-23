package com18bytes.plugindev.autodeop.listeners;


import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com18bytes.plugindev.autodeop.main.AutoDeOp;

public class CommandExecute implements CommandExecutor {
	private AutoDeOp plugin;

	public CommandExecute(AutoDeOp plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("autodeop") && args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")){
				if (sender.hasPermission("autodeop.reload") || sender.isOp()){
					plugin.reloadConfig();
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "AutoDeOp config reloaded."));
					return true;
				}
				else{ 
					return false;
				}
			}
			else{ 
				return false;
			}
		}
		else{
			return false;
		}
	}
}
