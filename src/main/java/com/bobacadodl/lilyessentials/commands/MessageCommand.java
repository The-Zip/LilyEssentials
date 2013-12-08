package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class MessageCommand implements CommandExecutor {

	private LilyEssentials plugin;

	public MessageCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("lilyessentials.general.message")) {
			return false;
		}

		// Invalid
		if (args.length <= 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/msg [player] [message]");
			return true;
		}

		String target = args[0];

		if (plugin.getServerSync().lookupPlayer(target) == null) {
			sender.sendMessage(ChatColor.DARK_RED + "Error!");
			sender.sendMessage(ChatColor.RED + "Player is offline");
			return true;
		}

		String message = plugin.wordsToString(1, args);
		plugin.request("lilyessentials.message", target + "\0"
				+ sender.getName() + "\0" + message + "\0"
				+ plugin.getUsername());

		return true;
	}
}
