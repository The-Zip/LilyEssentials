package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class DispatchCommand implements CommandExecutor {

	private LilyEssentials plugin;

	public DispatchCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("lilyessentials.admin.dispatch")) {
			return false;
		}

		// Invalid
		if (args.length <= 0) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/dispatch [command]");
			return true;
		}

		String command = plugin.wordsToString(0, args);
		command.replaceFirst("/", "");
		plugin.request("lilyessentials.dispatch", command);

		return true;
	}
	
}
