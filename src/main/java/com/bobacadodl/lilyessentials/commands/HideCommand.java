package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class HideCommand implements CommandExecutor {

	private LilyEssentials plugin;

	public HideCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!sender.hasPermission("lilyessentials.admin.hide")) {
			return false;
		}
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player player = (Player) sender;

		if(plugin.getServerSync().toggleHidden(player)) {
			player.sendMessage(ChatColor.YELLOW + "You are now hidden.");
		} else {
			player.sendMessage(ChatColor.YELLOW + "You are no longer hidden.");
		}

		return true;
	}

}
