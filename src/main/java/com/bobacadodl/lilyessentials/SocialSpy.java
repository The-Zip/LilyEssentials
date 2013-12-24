package com.bobacadodl.lilyessentials;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class SocialSpy extends JavaPlugin implements Listener {

	private Connect connect;
	
	private Command cmd;

	public void sendinfo(String pname, String message) {
		try {
			MessageRequest request = new MessageRequest(Collections.<String> emptyList(),"messagesocialyspy", pname + "%,%" + message);
			connect.request(request); 
		} catch (UnsupportedEncodingException | RequestException e) {
			e.printStackTrace();
		} 
	}

	@EventListener
	public void onMessage(MessageEvent event) {
		String[] data = null;
		if(event.getChannel().equalsIgnoreCase("messagesocialyspy")) {
			try { 
				data = event.getMessageAsString().split("%,%"); 
			} 
			catch (UnsupportedEncodingException e) { e.printStackTrace();
			return; 
			}
			String pname = data[0];
			String message = data[1];
			for(Player p : getServer().getOnlinePlayers()) {
				if(p.hasPermission("lilyessentials.admin.socialspy") && (cmd.getName().equalsIgnoreCase("lilysocial"))) {
					p.sendMessage(ChatColor.RED + "[M]" + ChatColor.GOLD + " " + pname + ": " + message);
				} else {
					p.sendMessage(ChatColor.RED + "You don't have permissions!");
				}
			}  
		}
	} 
}
