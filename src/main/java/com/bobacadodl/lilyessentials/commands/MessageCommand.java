package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class MessageCommand implements CommandExecutor 
{

	private LilyEssentials plugin;

	public MessageCommand(LilyEssentials plugin) 
	{
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{

		if (!sender.hasPermission("lilyessentials.general.message"))
			return false;

		// Invalid
		if (args.length <= 1) 
		{
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/msg [player] [message]");
			return true;
		}

		String target = args[0];

		if (plugin.getServerSync().lookupPlayer(target) == null) 
		{
			sender.sendMessage(ChatColor.DARK_RED + "This player is not online. Did you spell it right?");
			return true;
		}
		String message = "";

        for(int i = 1; i < args.length; i++)
        	message += (i == args.length - 1) ? args[i] : args[i] + " ";

		plugin.request("lilyessentials.message", target + "\0"
				+ sender.getName() + "\0" + message + "\0"
				+ plugin.getUsername());

		plugin.request("lilyessentials.spy", target +
				"\0" + sender.getName() + "\0" + message);

		return true;
	}
}
