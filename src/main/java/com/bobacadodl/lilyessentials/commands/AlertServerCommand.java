package com.bobacadodl.lilyessentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class AlertServerCommand implements CommandExecutor {

	private LilyEssentials plugin;

	public AlertServerCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission("lilyessentials.admin.alertserver")) {
			return false;
		}

		// Invalid
		if (args.length <= 0) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/alert [servers] [message]");
			return true;
		}

		String serverString = args[0];
		ArrayList<String> servers = new ArrayList<String>();
		for (String server : serverString.split(",")) {
			servers.add(server);
		}

		String message = plugin.wordsToString(1, args);
		plugin.request(servers, "lilyessentials.alert", message);

		return true;
	}
}
