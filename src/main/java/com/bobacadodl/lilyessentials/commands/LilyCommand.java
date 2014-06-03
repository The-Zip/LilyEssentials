package com.bobacadodl.lilyessentials.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Created at 22:59 on 02/06/2014.
 * Part of the "Lily Essentials" project.
 *
 * @author Connor Harries
 */
public abstract class LilyCommand implements CommandExecutor {

    protected String command = "";
    protected String permission = "";

    public LilyCommand(String name, String permission) {
        this.command = name;
        this.permission = permission;
    }

    public String getCommand() {
        return this.command;
    }

    public String getPermission() {
        return this.permission;
    }

    @Override
    public abstract boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings);
}
