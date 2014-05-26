package com.bobacadodl.lilyessentials.commands;

import com.bobacadodl.lilyessentials.LilyEssentials;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class GlistCommand implements CommandExecutor {

    private LilyEssentials plugin;

    public GlistCommand(LilyEssentials plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("lilyessentials.general.glist")) {
            return false;
        }

        int onlinePlayers = plugin.getServerSync().getAllPlayers().size();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getCfg().format_glist_title.replace("{total}",
                        Integer.toString(onlinePlayers))
        ));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                plugin.getCfg().format_glist_title2.replace("{total}",
                        Integer.toString(onlinePlayers))
        ));

        for (String server : plugin.getServerSync().getServers()) {

            List<String> players = plugin.getServerSync().getPlayersOnServer(server);
            String online = Integer.toString(players.size());

            String msg = ChatColor.translateAlternateColorCodes('&',
                    plugin.getCfg().format_glist_line.replace("{server}", server)
                            .replace("{online}", online)
            );
            sender.sendMessage(msg);
        }

        return true;

    }
}
