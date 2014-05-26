package com.bobacadodl.lilyessentials;

public class LilyEssentialsConfig extends Config {
	
	public LilyEssentialsConfig(LilyEssentials plugin) {
		this.setFile(plugin);
	}

	public String format_alert = "&3[&bAlert&3] &6{message}";
	public String format_adminchaton = "&aEnabled Admin Chat!";
	public String format_adminchatoff = "&aDisabled Admin Chat!";
	public String format_admin = "&a({server}) &4[&c{player}&4]&c >> {message}";
	public String format_glist_title = "&3&lServers";
	public String format_glist_title2 = "&5&oTotal Online: &e&o{total}";
	public String format_glist_line = "    &6{server}: &a({online})";
	public String format_msg_send = "&a({server}) &7[me -> {player}] &f{message}";
	public String format_msg_from = "&a({server}) &7[{player} -> me] &f{message}";
    public String format_socialspy = "&a[Spy]"
}
