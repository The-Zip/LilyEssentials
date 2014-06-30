package com.bobacadodl.lilyessentials;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.MessageRequest;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.FutureResultListener;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetPlayersResult;
import lilypad.client.connect.api.result.impl.MessageResult;
import lilypad.client.connect.api.result.impl.RedirectResult;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.bobacadodl.lilyessentials.commands.*;

public class LilyEssentials extends JavaPlugin {
	
	public Logger log = Logger.getLogger("Minecraft"); 

	private Connect connect;

	private LilyEssentialsConfig config;
	private String server;
	private HashMap<String, String> lastMessaged = new HashMap<String, String>();
	private ArrayList<String> adminChat = new ArrayList<String>();
	private ServerSync serverSync;
    String buildNumber = " Build #41 ";
    public Object prefix;
    //If your going to commit, change the build number so we know what build the server administrators are using when getting errors.

	public void onEnable() {
		serverSync = new ServerSync(this);
		getServer().getPluginManager().registerEvents(serverSync, this);
		connect = getServer().getServicesManager().getRegistration(Connect.class).getProvider();
		connect.registerEvents(new MessageListener(this));
		connect.registerEvents(serverSync);
		server = connect.getSettings().getUsername();
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		config = new LilyEssentialsConfig(this);
		config.load();
        log.info("LilyEssentials v1.2" + buildNumber + "is now loading.");
        log.info("LilyEssentials has been enabled successfully!");

		getCommand("admin").setExecutor(new AdminchatCommand(this));
		getCommand("alert").setExecutor(new AlertCommand(this));
		getCommand("alertserver").setExecutor(new AlertServerCommand(this));
		getCommand("dispatch").setExecutor(new DispatchCommand(this));
		getCommand("dispatchserver").setExecutor(new DispatchServerCommand(this));
		getCommand("find").setExecutor(new FindCommand(this));
		getCommand("glist").setExecutor(new GlistCommand(this));
		getCommand("message").setExecutor(new MessageCommand(this));
		getCommand("reply").setExecutor(new ReplyCommand(this));
		getCommand("sendall").setExecutor(new SendAllCommand(this));
		getCommand("send").setExecutor(new SendCommand(this));
		getCommand("hide").setExecutor(new HideCommand(this));
		getCommand("ignore").setExecutor(new IgnoreCommand(this));
		getCommand("ignore").setExecutor(new UnignoreCommand(this));
	}

	public void onDisable() {
		config.save();
		log.info("LilyEssentials has been disabled and saved!");
	}

	public void redirectRequest(String server, final Player player) {
		try {
			// create connection
			// new RedirectRequest to transfer the player
			connect.request(new RedirectRequest(server, player.getName()))
			.registerListener(
					new FutureResultListener<RedirectResult>() {
						// listen for a successful transfer
						public void onResult(
								RedirectResult redirectResult) {
							if (redirectResult.getStatusCode() == StatusCode.SUCCESS) {
								return;
							}
							player.sendMessage(ChatColor.RED + "Connection Error!!");
						}
					});

		} catch (Exception exception) {
			player.sendMessage(ChatColor.RED + "Connection Error!!");
		}
	}

	public FutureResult<GetPlayersResult> playerRequest()
			throws RequestException {
		return connect.request(new GetPlayersRequest(true));
	}

	public void request(String channel, String message) {
		try {
			MessageRequest request = new MessageRequest(
					new ArrayList<String>(), channel, message); // servername,  channelname (short),  message
			connect.request(request);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	public boolean request(ArrayList<String> servers, String channel,
			String message) {
		MessageRequest request = null;
		try {
			request = new MessageRequest(servers, channel, message); // servername, channelname (short),  message
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			FutureResult<MessageResult> futureResult = connect.request(request);
			/*
			 * futureResult. futureResult.registerListener(new
			 * FutureResultListener<MessageResult>(){
			 * 
			 * @Override public void onResult(MessageResult messageResult) {
			 * return messageResult.getStatusCode()==StatusCode.SUCCESS; } });
			 */
			MessageResult messageResult = futureResult.await(100L);
			if (messageResult != null) {
				return messageResult.getStatusCode() == StatusCode.SUCCESS;
			} else {
				return false;
			}
		} catch (RequestException e) {
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String wordsToString(int start, String[] words) {
		StringBuilder builder = new StringBuilder();
		for(String word : words) {
			builder.append(word);
			builder.append(" ");
		}
		return builder.toString().trim();
	}

	public Connect getConnect() {
		return connect;
	}

	public LilyEssentialsConfig getCfg() {
		return config;
	}

	public String getUsername() {
		return server;
	}

	public HashMap<String, String> getLastMessaged() {
		return lastMessaged;
	}

	public ArrayList<String> getAdminChat() {
		return adminChat;
	}

	public ServerSync getServerSync() {
		return serverSync;
	}

}
