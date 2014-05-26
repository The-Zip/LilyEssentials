package com.bobacadodl.lilyessentials.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class DispatchServerCommand implements CommandExecutor{
	LilyEssentials main;
	public DispatchServerCommand(LilyEssentials instance){this.main=instance;}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if(sender.hasPermission("lilyessentials.admin.dispatchserver")){
			if(args.length>=1){
				String serverString = args[0];
				ArrayList<String> servers = new ArrayList<String>();
				for(String server:serverString.split(",")){
					servers.add(server);
				}
				
				
				String command = main.wordsToString(1,args);

				command.replaceFirst("/", "");
				main.request(servers, "lilyessentials.dispatch", command);
			}
			else{
				//invalid
				sender.sendMessage(ChatColor.DARK_RED+"Invalid args!");
				sender.sendMessage(ChatColor.RED+"Proper Usage: "+ChatColor.YELLOW+"/dispatchserver [servers] [command]");
			}
			return true;
		}
		
		return false;
	}
}
