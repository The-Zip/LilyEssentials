package com.bobacadodl.lilyessentials.commands;

import com.bobacadodl.lilyessentials.LilyEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;

public class DispatchServerCommand implements CommandExecutor {
    LilyEssentials main;

    public DispatchServerCommand(LilyEssentials instance) {
        this.main = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {

        if (sender.hasPermission("lilyessentials.admin.dispatchserver")) {
            if (args.length >= 1) {
                String serverString = args[0];
                ArrayList<String> servers = new ArrayList<String>();
                Collections.addAll(servers, serverString.split(","));


                String command = main.wordsToString(1, args);
                command = command.replaceFirst("/", "");
                main.request(servers, "lilyessentials.dispatch", command);
            } else {
                //invalid
                sender.sendMessage(ChatColor.DARK_RED + "Invalid args!");
                sender.sendMessage(ChatColor.RED + "Proper Usage: " + ChatColor.YELLOW + "/dispatchserver [servers] [command]");
            }
            return true;
        }

        return false;
    }
}
