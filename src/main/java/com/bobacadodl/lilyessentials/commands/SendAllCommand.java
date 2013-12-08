package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class SendAllCommand implements CommandExecutor {
	
	private LilyEssentials plugin;

	public SendAllCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("lilyessentials.admin.sendall")) {
			return false;
		}

		// Invalid
		if (args.length != 1) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/sendall [server]");
			return true;
		}

		String server = args[0];
		plugin.request("lilyessentials.sendall", server);

		return true;
	}

}
