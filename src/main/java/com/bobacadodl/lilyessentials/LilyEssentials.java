package com.bobacadodl.lilyessentials;

import com.bobacadodl.lilyessentials.commands.*;
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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class LilyEssentials extends JavaPlugin {

    public Logger log = Logger.getLogger("Minecraft");
    public Object prefix;
    String buildNumber = "version #40";
    private Connect connect;
    private LilyEssentialsConfig config;
    private String server;
    private HashMap<String, String> lastMessaged = new HashMap<String, String>();
    private ArrayList<String> adminChat = new ArrayList<String>();
    private ServerSync serverSync;

    public void onEnable() {
        PluginManager manager = getServer().getPluginManager();

        if (manager.getPlugin("LilyPad-Connect") == null) {
            log.severe("LilyPad-Connect was not found, disabling plugin.");
            manager.disablePlugin(this);
            return;
        }

        serverSync = new ServerSync(this);
        getServer().getPluginManager().registerEvents(serverSync, this);
        connect = getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        connect.registerEvents(new MessageListener(this));
        connect.registerEvents(serverSync);
        server = connect.getSettings().getUsername();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        config = new LilyEssentialsConfig(this);
        config.load();
        log.info("LilyEssentials " + buildNumber + " is now loading.");

        getCommand("dispatchserver").setExecutor(new DispatchServerCommand(this));
        getCommand("alertserver").setExecutor(new AlertServerCommand(this));
        getCommand("dispatch").setExecutor(new DispatchCommand(this));
        getCommand("admin").setExecutor(new AdminchatCommand(this));
        getCommand("message").setExecutor(new MessageCommand(this));
        getCommand("sendall").setExecutor(new SendAllCommand(this));
        getCommand("ignore").setExecutor(new IgnoreCommand(this));
        getCommand("alert").setExecutor(new AlertCommand(this));
        getCommand("glist").setExecutor(new GlistCommand(this));
        getCommand("reply").setExecutor(new ReplyCommand(this));
        getCommand("find").setExecutor(new FindCommand(this));
        getCommand("send").setExecutor(new SendCommand(this));
        getCommand("hide").setExecutor(new HideCommand(this));
        getCommand("lilyspy").setExecutor(new LilySpy(this));
    }

    public void onDisable() {
        config.save();
        log.info("LilyEssentials has been disabled and saved!");
    }

    public void redirectRequest(String server, final Player player) {
        try {
            connect.request(new RedirectRequest(server, player.getName()))
                    .registerListener(
                            new FutureResultListener<RedirectResult>() {
                                public void onResult(
                                        RedirectResult redirectResult) {
                                    if (redirectResult.getStatusCode() == StatusCode.SUCCESS) {
                                        return;
                                    }
                                    player.sendMessage(ChatColor.RED + "Connection Error!!");
                                }
                            }
                    );

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
            MessageRequest request = new MessageRequest(new ArrayList<String>(), channel, message);
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
            request = new MessageRequest(servers, channel, message);
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
        for (String word : words) {
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
