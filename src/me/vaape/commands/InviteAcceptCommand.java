package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class InviteAcceptCommand {
		
	Guilds plugin;
	
	public InviteAcceptCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeInviteAcceptCommand (Player player, String[] args) {
		String UUID = player.getUniqueId().toString();
		if (args.length > 1) {
			String tagLower = args[1].toLowerCase();
			String tag = plugin.getConfig().getString("guilds." + tagLower + ".tag");
			String name = GuildManager.getGuildName(tagLower);
			int capacity = GuildManager.getLevel(tagLower) + 2;
			int totalPlayers = GuildManager.getTotalPlayers(tagLower);
			if (GuildManager.getPlayerGuildTag(UUID) == null) {
				if (plugin.getConfig().getStringList("guilds." + tagLower + ".invited").contains(UUID)) {
					if (totalPlayers < capacity) {
						final List<String> invited = Guilds.getInstance().getConfig().getStringList("guilds." + tagLower + ".invited");
						invited.remove(UUID);
						Guilds.getInstance().getConfig().set("guilds." + tagLower + ".invited", invited);
						final List<String> recruits = Guilds.getInstance().getConfig().getStringList("guilds." + tagLower + ".recruits");
						recruits.add(UUID);
						Guilds.getInstance().getConfig().set("guilds." + tagLower + ".recruits", recruits);
						Guilds.getInstance().saveConfig();
						Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.YELLOW + player.getName() + " has joined " + name + ".");
						List<Player> onlinePlayers = new ArrayList<Player>();
						for (Player p : Bukkit.getServer().getOnlinePlayers()) {
							onlinePlayers.add(p);
						}
						for (Player guildPlayer : onlinePlayers) {
							if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
								if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
									guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
								}
							}
						}
					}
					else {
						player.sendMessage(ChatColor.RED + tag + " is at maximum capacity.");
					}
				}
				else {
					if (tag != null) {
						player.sendMessage(ChatColor.RED + "You don't have an invitation to join " + tag + ".");
					}
					else {
						player.sendMessage(ChatColor.RED + "There isn't a clan called " + args[1] + ".");
					}
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You must leave your current guild first.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "Wrong usage, try /g accept [guild]");
		}
	}
}
