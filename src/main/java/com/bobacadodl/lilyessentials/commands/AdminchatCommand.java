package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class AdminchatCommand implements CommandExecutor {

	private LilyEssentials plugin;

	public AdminchatCommand(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(!sender.hasPermission("lilyessentials.admin.chat")) {
			return false;
		}

		//toggle admin chat
		if(args.length <= 0) {
			if(plugin.getAdminChat().contains(sender.getName())){
				plugin.getAdminChat().remove(sender.getName());
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getCfg().format_adminchatoff));
			} else {
				plugin.getAdminChat().add(sender.getName());
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getCfg().format_adminchaton));
			}
			return true;
		}

		String message = plugin.wordsToString(0,args);
		plugin.request("lilyessentials.admin", sender.getName() + "\0" + message);

		return true;
	}

}
