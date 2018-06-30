package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class ListCommand {
	
Guilds plugin;
	
	public ListCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeListCommand (Player player, String[] args) {
		List<String> tags = GuildManager.getGuildTagList();
		List<String> display = new ArrayList<>();
		for (String tag : tags) {
			String name = GuildManager.getGuildName(tag.toLowerCase());
			String guildTag = GuildManager.getGuildTag(tag.toLowerCase());
			String entry = ChatColor.GRAY + "[" + guildTag + "] " + name;
			List<Player> onlinePlayers = new ArrayList<Player>();
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				onlinePlayers.add(p);
			}
			for (Player onlinePlayer : onlinePlayers) {
				if (GuildManager.getGuildPlayers(tag.toLowerCase()).contains(onlinePlayer.getUniqueId().toString())) {
					entry = ChatColor.GREEN + "[" + guildTag + "] " + name;
				}
			}
			display.add(entry);
		}
		if (args.length > 1) {
			if (StringUtils.isNumeric(args[1])) {
				if (((Integer.parseInt(args[1]) - 1) * 19) >= display.size()) {
					player.sendMessage(ChatColor.BLUE + "List of guilds [" + args[1] + "] is empty.");
					return;
				}
				player.sendMessage(ChatColor.BLUE + "List of guilds [" + args[1] + "]:");
				if (display.size() < 19) {
					for (int i = 0 ; i < display.size() ; i++) {
						player.sendMessage(display.get(i));
					}
				}
				else {
					for (int i = ((Integer.parseInt(args[1]) - 1) * 19) ; i < (Integer.parseInt(args[1]) * 19) + 19 ; i++) {
						if (i < display.size()) {
							player.sendMessage(display.get(i));
						}
					}
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "Wrong usage, try /g list [page number]");
			}
		}
		else {
			player.sendMessage(ChatColor.BLUE + "List of guilds [1]:");
			if (display.size() < 10) {
				for (int i = 0 ; i < display.size() ; i++) {
					player.sendMessage(display.get(i));
				}
			}
			else {
				for (int i = 0 ; i < 19 ; i++) {
					player.sendMessage(display.get(i));
				}
			}
		}
	}
}
