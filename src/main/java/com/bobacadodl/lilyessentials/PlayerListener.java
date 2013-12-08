package com.bobacadodl.lilyessentials;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	private LilyEssentials plugin;

	public PlayerListener(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		if (plugin.getAdminChat().contains(player.getName())) {
			if (e.getMessage().contains("|")) {
				player.sendMessage(ChatColor.DARK_RED + "Error!");
				player.sendMessage(ChatColor.RED
						+ "Your message cannot contain: '|'");
				return;
			}
			plugin.request("lilyessentials.admin",
					player.getName() + "\0" + e.getMessage());
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (plugin.getLastMessaged().containsKey(player.getName())) {
			plugin.getLastMessaged().remove(player.getName());
		}
	}

}
