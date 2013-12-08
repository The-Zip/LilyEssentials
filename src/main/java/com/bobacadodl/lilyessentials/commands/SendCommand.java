package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class SendCommand implements CommandExecutor {
	
	private LilyEssentials plugin;

	public SendCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("lilyessentials.admin.send")) {
			return false;
		}

		// Invalid
		if (args.length != 2) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/send [player] [server]");
			return true;
		}

		String player = args[0];
		String server = args[1];
		plugin.request("lilyessentials.send", player + "\0" + server);

		return true;
	}
}
