package com.bobacadodl.lilyessentials;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageListener {
	
	private LilyEssentials plugin;

	public MessageListener(LilyEssentials plugin) {
		this.plugin = plugin;
	}

	@EventListener
	public void onMessage(MessageEvent event) {
		if (event.getChannel().equalsIgnoreCase("lilyessentials.dispatch")) {
			try {
				String command = event.getMessageAsString();
				plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(),
						command);
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		} 
		if (event.getChannel().equalsIgnoreCase("lilyessentials.alert")) {
			try {
				String message = event.getMessageAsString();
				String msg = ChatColor.translateAlternateColorCodes('&',
						plugin.getCfg().format_alert.replace("{message}", message));
				plugin.getServer().broadcastMessage(msg);
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		} 
		if (event.getChannel().equalsIgnoreCase("lilyessentials.find")) {
			try {
				String code = event.getMessageAsString();
				String[] fromto = code.split("\0");
				String from = fromto[0];
				String tofind = fromto[1];
				Player p = plugin.getServer().getPlayer(tofind);
				if (p != null) {
					// return the player
					plugin.request("lilyessentials.blankmessage", from + "\0&6&n"
							+ p.getName() + ChatColor.YELLOW
							+ " is on server: &3" + plugin.getUsername());
				} else {

				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}

		if (event.getChannel().equalsIgnoreCase("lilyessentials.send")) {
			try {
				String code = event.getMessageAsString();
				String[] targetserver = code.split("\0");
				String target = targetserver[0];
				String server = targetserver[1];
				Player p = plugin.getServer().getPlayer(target);
				if (p != null) {
					// redirect request
					// p.sendMessage("You have been teleported to another server!");
					plugin.redirectRequest(server, p);
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}

		if (event.getChannel().equalsIgnoreCase("lilyessentials.sendall")) {
			try {
				String server = event.getMessageAsString();

				for (Player p : plugin.getServer().getOnlinePlayers()) {
					plugin.redirectRequest(server, p);
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}

		if (event.getChannel().equalsIgnoreCase("lilyessentials.blankmessage")) {
			try {
				String code = event.getMessageAsString();
				String[] tomessage = code.split("\0");
				String to = tomessage[0];
				String message = tomessage[1];
				Player p = plugin.getServer().getPlayer(to);
				if (p != null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							message));
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
		if (event.getChannel().equalsIgnoreCase("lilyessentials.messagesuccess")) {
			try {
				String code = event.getMessageAsString();
				String[] tofrommessage = code.split("\0");
				String to = tofrommessage[0];
				String from = tofrommessage[1];
				String message = tofrommessage[2];
				String fromServer = tofrommessage[3];
				Player p = plugin.getServer().getPlayer(from);
				if (p != null) {
					plugin.getLastMessaged().put(from, to);

					String chat = ChatColor.translateAlternateColorCodes(
							'&',
							plugin.getCfg().format_msg_send.replace("{player}", to)
							.replace("{message}", message)
							.replace("{server}", fromServer));

					p.sendMessage(chat);
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
		if (event.getChannel().equalsIgnoreCase("lilyessentials.message")) {
			try {
				String code = event.getMessageAsString();
				String[] tofrommessage = code.split("\0");
				String to = tofrommessage[0];
				String from = tofrommessage[1];
				String message = tofrommessage[2];
				String fromServer = tofrommessage[3];
				Player p = plugin.getServer().getPlayer(to);
				if (p != null) {
					String chat = ChatColor.translateAlternateColorCodes(
							'&',
							plugin.getCfg().format_msg_from.replace("{player}", to)
							.replace("{message}", message)
							.replace("{server}", fromServer));
					p.sendMessage(chat);
					plugin.getLastMessaged().put(p.getName(), from);
					plugin.request("lilyessentials.messagesuccess", p.getName()
							+ "\0" + from + "\0" + message + "\0" + plugin.getUsername());
					// main.request("blankmessage",
					// "from,&7[me -> "+to+"]"+ChatColor.WHITE+message);
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}

		}
		if (event.getChannel().equalsIgnoreCase("lilyessentials.spy")) {
			try {
				String code = event.getMessageAsString();
				String[] tofrommessage = code.split("\0");
				String to = tofrommessage[0];
				String from = tofrommessage[1];
				String message = tofrommessage[2];
				
				for ( Player player : Bukkit.getServer().getOnlinePlayers() ) {
					if(player.hasPermission("lilyessentials.socialspy")) {
						
						player.sendMessage(ChatColor.GREEN + "[Spy] " + 
								ChatColor.GRAY + from + " > " + to + ": " + message
						);
						
						//format
						// [Spy] user1 > user2: Hi.
					}
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
		if (event.getChannel().equalsIgnoreCase("lilyessentials.admin")) {
			try {
				String code = event.getMessageAsString();
				String[] usermessage = code.split("\0");
				String user = usermessage[0];
				String message = usermessage[1];
				String chat = ChatColor.translateAlternateColorCodes(
						'&',
						plugin.getCfg().format_admin.replace("{player}", user)
						.replace("{message}", message)
						.replace("{server}", plugin.getUsername()));
				for (Player p : plugin.getServer().getOnlinePlayers()) {
					if (p.hasPermission("lilyessentials.admin.chat")) {
						p.sendMessage(chat);
					}
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
		if (event.getChannel().equalsIgnoreCase("lilyessentials.glist")) {
			try {
				String toserver = event.getSender();
				String user = event.getMessageAsString();
				ArrayList<String> servers = new ArrayList<String>();
				servers.add(toserver);
				plugin.request(
						servers,
						"lilyessentials.glistreturn",
						user
						+ "\0"
						+ Integer.toString(plugin.getServer()
								.getOnlinePlayers().length) + "\0"
								+ plugin.getUsername());
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
		if (event.getChannel().equalsIgnoreCase("lilyessentials.glistreturn")) {
			try {
				String code = event.getMessageAsString();
				String[] useronlineserver = code.split("\0");
				String user = useronlineserver[0];
				String online = useronlineserver[1];
				String server = useronlineserver[2];
				Player p = Bukkit.getPlayer(user);
				if (p != null) {
					String msg = ChatColor.translateAlternateColorCodes('&',
							plugin.getCfg().format_glist_line.replace("{server}",
									server).replace("{online}", online));
					p.sendMessage(msg);
				}
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
		}
	}
}
