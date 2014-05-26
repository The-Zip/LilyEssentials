package com.bobacadodl.lilyessentials.commands;

import com.bobacadodl.lilyessentials.LilyEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created at 20:26 on 26/05/2014.
 * Part of LilyEssentials.
 *
 * @author Connor Harries
 */
public class LilySpy implements CommandExecutor {
    private LilyEssentials plugin;

    public LilySpy(LilyEssentials plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player))
            return true;
        if(command.getName().equalsIgnoreCase("lilyspy")) {
            final Player player = (Player) commandSender;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', (plugin.getServerSync().isPlayerSpying(player) ? "&cLilySpy disabled." : "&aLilySpy enabled.")));
            plugin.getServerSync().toggleSpying(player);
            return true;
        }
        return false;
    }
}
