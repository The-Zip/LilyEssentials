package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class FindCommand implements CommandExecutor {

	private LilyEssentials plugin;

	public FindCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("lilyessentials.general.find"))
			return false;

		// Invalid
		if (args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/find [player]");
			return true;
		}

		String tofind = args[0];

		if (plugin.getServerSync().lookupPlayer(tofind) == null) {
			sender.sendMessage(ChatColor.DARK_RED + "Error!");
			sender.sendMessage(ChatColor.RED + "Player is offline");
			return true;
		}

		plugin.request("lilyessentials.find", sender.getName() + "\0" + tofind);
		return true;

	}

}
