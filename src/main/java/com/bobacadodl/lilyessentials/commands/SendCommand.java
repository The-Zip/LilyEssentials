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
		if (args.length <=  2) {
			sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
			sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/send [player] [server]");
			return true;
		}

		String player = args[0];
		String server = args[1];
		plugin.request("lilyessentials.send", player + "\0" + server);

		return true;
	}

    /* (This might be a cleaner, easier way to run this command)
    / Written by iiHeroo - Horrgs
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        //For commands, <> = required argument, [] = optional, ex for Banning:
        // /ban iiHeroo - It will use the default ban message if you don't put one.

        if(!(sender instanceof Player)) {
             sender.sendMessage(ChatColor.RED + "Only in-game player's can use that command!");
             //This will prevent the console from getting a stacktrace when running the command, you can allow the console to use commands using this tut
             // http://forums.bukkit.org/threads/tutorial-how-to-create-a-player-console-command-executor.198429/
             return true;
        }

        Player p = (Player) sender;

        if(p.hasPermission("lilyessentials.admin.send") {
           if(args.length != 2) {
              sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
              sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/send <player> <server>");
           }

           if(args.length == 2) {
              Player onlinetarget = Bukkit.getServer().getPlayer(args[0]);
              String server = args[1];

              if(onlinetarget != null) {
                 plugin.reguest("lilyessentials.send"), onlinetarget + "\0" + server);
              } else {
                 p.sendMessage(ChatColor.RED + "Player not found!");
              }
           }
        } else {
            p.sendMessage(ChatColor.RED + "No  permission");
            return false;
        }




        return true;
     */
}
