package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class AlertCommand implements CommandExecutor {

	private LilyEssentials plugin;

	public AlertCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!sender.hasPermission("lilyessentials.admin.alert")) {
			return false;
		}

		if(args.length <= 0){
			sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/alert [message]");
			return true;
		}

		String message = plugin.wordsToString(0,args);
		plugin.request("lilyessentials.alert", message);

		return true;
	}
}
