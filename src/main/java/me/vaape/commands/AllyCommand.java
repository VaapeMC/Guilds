package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class AllyCommand {
	
	Guilds plugin;
	
	public AllyCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	public void executeAllyCommand (Player player, String[] args) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			String name = GuildManager.getGuildName(tagLower);
			if (GuildManager.isLeader(UUID) || GuildManager.isElder(UUID)) {
				if (args.length > 2) {
					String rTagLower = args[2].toLowerCase();
					String rTag = GuildManager.getGuildTag(rTagLower);
					String rName = GuildManager.getGuildName(rTagLower);
					List<String> allies = GuildManager.getAllies(tagLower);
					if (args[1].equalsIgnoreCase("add") | args[1].equalsIgnoreCase("invite") || args[1].equalsIgnoreCase("request") || args[1].equalsIgnoreCase("req")) {
						if (rTag != null) {
							int level = GuildManager.getLevel(tagLower);
							if (!plugin.getConfig().getStringList("guilds." + rTagLower + ".requests").contains(tag)) {
								if (!rTagLower.equals(tagLower)) {
									if (allies.size() == 0) {
										if (level > 1) {
											GuildManager.ally(tag, rTag);
											List<Player> onlinePlayers = new ArrayList<Player>();
											for (Player p : Bukkit.getServer().getOnlinePlayers()) {
												onlinePlayers.add(p);
											}
											for (Player guildPlayer: onlinePlayers) {
												if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
													if (GuildManager.getGuildPlayers(rTagLower).contains(guildPlayer.getUniqueId().toString())) {
														if (GuildManager.isLeader(guildPlayer.getUniqueId().toString()) || GuildManager.isElder(guildPlayer.getUniqueId().toString())) {
															guildPlayer.sendMessage(ChatColor.BLUE + "[" + rTag + "] " + ChatColor.LIGHT_PURPLE + GuildManager.getGuildName(tag)  + " have sent you an alliance request, type " + ChatColor.GREEN +"/g ally accept " + tag + ChatColor.LIGHT_PURPLE + " to accept.");
															guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
														}
													}
													else if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
														guildPlayer.sendMessage(ChatColor.BLUE + "[" + tag + "] " + ChatColor.LIGHT_PURPLE + player.getName() + " has sent an alliance request to " + rTag + ".");
													}
												}
											}
										}
										else {
											player.sendMessage(ChatColor.RED + "Your guild must be level 2 before adding allies.");
										}
									}
									if (allies.size() == 1) {
										if (level > 3) {
											GuildManager.ally(tag, rTag);
											List<Player> onlinePlayers = new ArrayList<Player>();
											for (Player p : Bukkit.getServer().getOnlinePlayers()) {
												onlinePlayers.add(p);
											}
											for (Player guildPlayer: onlinePlayers) {
												if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
													if (GuildManager.getGuildPlayers(rTagLower).contains(guildPlayer.getUniqueId().toString())) {
														if (GuildManager.isLeader(guildPlayer.getUniqueId().toString()) || GuildManager.isElder(guildPlayer.getUniqueId().toString())) {
															guildPlayer.sendMessage(ChatColor.BLUE + "[" + rTag + "] " + ChatColor.LIGHT_PURPLE + GuildManager.getGuildName(tag)  + " have sent you an alliance request, type " + ChatColor.LIGHT_PURPLE +"/g ally accept " + tag + ChatColor.BLUE + "to accept.");
															guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
														}
													}
													else if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
														guildPlayer.sendMessage(ChatColor.BLUE + "[" + tag + "] " + ChatColor.LIGHT_PURPLE + player.getName() + " has sent an alliance request to " + rTag + ".");
													}
												}
											}
										}
										else {
											player.sendMessage(ChatColor.RED + "Your guild must be level 4 before adding more allies.");
										}
									}
									if (allies.size() == 2) {
										player.sendMessage(ChatColor.RED + "Guilds can not have more than 2 allies.");
									}
								}
								else {
									player.sendMessage(ChatColor.RED + "You can't ally your own guild.");
								}
							}
							else {
								player.sendMessage(ChatColor.RED + rTag + " has already recieved an alliance request from your guild recently.");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + args[2] + " could not be found.");
						}
					}
					else if (args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("break")) {
						if (rTag != null) {
							if (plugin.getConfig().getStringList("guilds." + tagLower + ".allies").contains(rTag)) {
								GuildManager.allyRemove(tag, rTag);
								List<Player> onlinePlayers = new ArrayList<Player>();
								for (Player p : Bukkit.getServer().getOnlinePlayers()) {
									onlinePlayers.add(p);
								}
								for (Player guildPlayer: onlinePlayers) {
									if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
										if (GuildManager.getGuildPlayers(rTagLower).contains(guildPlayer.getUniqueId().toString()) || GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
											guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
										}
									}
								}
								Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.YELLOW + name + " broke alliance with " + rName + ".");
							}
							else {
								player.sendMessage(ChatColor.RED + rTag + " isn't an ally of " + tag + ".");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + args[2] + " could not be found.");
						}
					}
					else if (args[1].equalsIgnoreCase("accept")) {
						if (rTag != null) {
							int level = GuildManager.getLevel(tagLower);
							if (plugin.getConfig().getStringList("guilds." + tagLower + ".requests").contains(rTag)) {
								if (allies.size() == 0) {
									if (level > 1) {
										GuildManager.allyAccept(tag, rTag);
										List<Player> onlinePlayers = new ArrayList<Player>();
										for (Player p : Bukkit.getServer().getOnlinePlayers()) {
											onlinePlayers.add(p);
										}
										for (Player guildPlayer: onlinePlayers) {
											if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
												if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString()) || GuildManager.getGuildPlayers(rTagLower).contains(guildPlayer.getUniqueId().toString())) {
													guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
												}
											}
										}
										Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.LIGHT_PURPLE + name + " and " + rName + " are now allies.");
									}
									else {
										player.sendMessage(ChatColor.RED + "Your guild must be level 2 before adding allies.");
									}
								}
								if (allies.size() == 1) {
									if (level > 3) {
										GuildManager.allyAccept(tag, rTag);
										List<Player> onlinePlayers = new ArrayList<Player>();
										for (Player p : Bukkit.getServer().getOnlinePlayers()) {
											onlinePlayers.add(p);
										}
										for (Player guildPlayer: onlinePlayers) {
											if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
												if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString()) || GuildManager.getGuildPlayers(rTagLower).contains(guildPlayer.getUniqueId().toString())) {
													guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
												}
											}
										}
										Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.LIGHT_PURPLE + name + " and " + rName + " are now allies.");
									}
									else {
										player.sendMessage(ChatColor.RED + "Your guild must be level 4 before adding more allies.");
									}
								}
								if (allies.size() == 2) {
									player.sendMessage(ChatColor.RED + "Guilds can not have more than 2 allies.");
								}
							}
							else {
								player.sendMessage(ChatColor.RED + rTag + " has not sent you an alliance request.");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + args[2] + " could not be found.");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "Wrong usage, try /g ally add/remove/accept [guild tag]");
					}
				}
				else {
					player.sendMessage(ChatColor.RED + "Wrong usage, try /g ally add/remove/accept [guild tag]");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You must be a leader or an elder to ally other guilds.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
}
