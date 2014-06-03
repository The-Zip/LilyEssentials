package com.bobacadodl.lilyessentials;

import com.bobacadodl.lilyessentials.commands.*;
import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.*;
import lilypad.client.connect.api.result.*;
import lilypad.client.connect.api.result.impl.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class LilyEssentials extends JavaPlugin {

    public Logger log = Logger.getLogger("Minecraft");
    String buildNumber = "#40";
    private Connect connect;
    private LilyEssentialsConfig config;
    private String server;
    private HashMap<String, String> lastMessaged = new HashMap<String, String>();
    private ArrayList<String> adminChat = new ArrayList<String>();
    private ServerSync serverSync;

    public void onEnable() {
        PluginManager manager = getServer().getPluginManager();

        PluginDescriptionFile pluginDescriptionFile = this.getDescription();

        boolean foundDependencies = true;

        for(String p : pluginDescriptionFile.getDepend()) {
            if(manager.getPlugin(p) == null) {
                log.severe(String.format("Could not find \"%s\".", p));
                foundDependencies = false;
            }
        }

        if (foundDependencies) {
            log.severe("LilyEssentials has been disabled.");
            manager.disablePlugin(this);
            return;
        }

        serverSync = new ServerSync(this);
        getServer().getPluginManager().registerEvents(serverSync, this);
        try {
            connect = getServer().getServicesManager().getRegistration(Connect.class).getProvider();
            connect.registerEvents(new MessageListener(this));
            connect.registerEvents(serverSync);
            server = connect.getSettings().getUsername();
        } catch (Exception ex) {
            getLogger().info("Unable to reach Connect proxy.");
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        config = new LilyEssentialsConfig(this);
        config.load();
        log.info("Starting LilyEssentials build " + buildNumber + ".");

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
            MessageResult messageResult = futureResult.await(60L);
            return messageResult != null && messageResult.getStatusCode() == StatusCode.SUCCESS;
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
        for(int i = start; i < words.length; i++) {
            builder.append(words[i - 1]);
        }
        return builder.toString().trim();
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
