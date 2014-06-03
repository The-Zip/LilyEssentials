package com.bobacadodl.lilyessentials;

import com.google.common.collect.Lists;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;


public class ServerSync implements Runnable, Listener {
    private LilyEssentials plugin;
    private WeakHashMap<String, List<String>> serverPlayers = new WeakHashMap<String, List<String>>();
    private Map<String, Long> lastReply = new HashMap<String, Long>();

    private WeakHashMap<UUID, Boolean> isHidden = new WeakHashMap<UUID, Boolean>();
    private WeakHashMap<UUID, Boolean> isSpying = new WeakHashMap<UUID, Boolean>();

    public ServerSync(LilyEssentials plugin) {
        this(plugin, 100L);
    }

    public ServerSync(LilyEssentials plugin, long delay) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, delay);
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Iterator<Entry<String, Long>> replys = lastReply.entrySet().iterator();
        while (replys.hasNext()) {
            Entry<String, Long> reply = replys.next();
            if ((reply.getValue() + 10000) < System.currentTimeMillis()) {
                replys.remove();
                serverPlayers.remove(reply.getKey());
            }
        }
        StringBuilder builder = new StringBuilder();
        builder.append(plugin.getUsername());
        builder.append("\0");
        Iterator<Player> players = Arrays.asList(Bukkit.getOnlinePlayers()).iterator();
        while (players.hasNext()) {
            Player current = players.next();
            if (isPlayerHidden(current))
                continue;
            builder.append(current.getName());
            if (players.hasNext()) {
                builder.append("\0");
            }
        }
        plugin.request("lilyessentials.sync", builder.toString());
    }

    @EventListener
    public void onSync(MessageEvent event) {
        if (event.getChannel().equals("lilyessentials.sync")) {
            try {
                String[] split = event.getMessageAsString().split("\0");
                String server = split[0];
                List<String> players = Lists.newArrayList(split);
                players.remove(0);
                serverPlayers.put(server, players);
                lastReply.put(server, System.currentTimeMillis());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if(isHidden.containsKey(event.getPlayer().getUniqueId()))
            isHidden.remove(event.getPlayer().getUniqueId());
        if(isSpying.containsKey(event.getPlayer().getUniqueId()))
            isSpying.remove(event.getPlayer().getUniqueId());
    }

    public boolean setPlayerHidden(Player player, boolean state) {
        isSpying.put(player.getUniqueId(), state);
        return state;
    }

    public boolean setPlayerSpying(Player player, Boolean spying) {
        isSpying.put(player.getUniqueId(), spying);
        return spying;
    }

    public boolean isPlayerHidden(Player player) {
        return (isSpying.containsKey(player.getUniqueId()) ? isSpying.get(player.getUniqueId()) : false);
    }

    public boolean isPlayerSpying(Player player) {
        return (isSpying.containsKey(player.getUniqueId()) ? isSpying.get(player.getUniqueId()) : false);
    }

    public boolean toggleSpying(Player player) {
        return setPlayerSpying(player, !isPlayerSpying(player));
    }

    public boolean toggleHidden(Player player) {
        return setPlayerHidden(player, !isPlayerHidden(player));
    }

    public List<String> getPlayersOnServer(String server) {
        return serverPlayers.get(server);
    }

    public String lookupPlayer(String player) {
        for (Entry<String, List<String>> server : serverPlayers.entrySet()) {
            if (server.getValue().contains(player)) {
                return server.getKey();
            }
        }
        return null;
    }

    public List<String> getAllPlayers() {
        List<String> players = new ArrayList<String>();
        for (List<String> server : serverPlayers.values()) {
            players.addAll(server);
        }
        return players;
    }

    public Set<String> getServers() {
        return serverPlayers.keySet();
    }

}
