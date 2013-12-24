package com.bobacadodl.lilyessentials.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.bobacadodl.lilyessentials.LilyEssentials;

public class UnignoreCommand implements CommandExecutor 
{

        private LilyEssentials plugin;

        public UnignoreCommand(LilyEssentials plugin) 
        {
                this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
        {

                if (!sender.hasPermission("lilyessentials.general.message")) 
                {
                        return false;
                }

                // Invalid
                if(args.length <= 0) {
                        sender.sendMessage(plugin.prefix + ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/ignore [person]");
                        return true;
                }

                String target = args[0];

                if (plugin.getServerSync().lookupPlayer(target) == null) 
                {
                        sender.sendMessage(plugin.prefix + ChatColor.DARK_RED + "That player isn't around. Did you mistype their name?");
                        return true;
                }

                // add ignore code here

                return true;
        }
}
