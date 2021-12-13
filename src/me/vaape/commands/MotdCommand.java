package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class MotdCommand implements Listener{
	
	Guilds plugin;
	
	public MotdCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeMotdCommand (Player player, String[] args) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			String name = GuildManager.getGuildName(tagLower);
			List<String> motd = plugin.getConfig().getStringList("guilds." + tagLower + ".motd");
			int level = GuildManager.getLevel(tagLower);
			if (args.length == 1) {
				if (motd.size() > 0) {
					player.sendMessage(ChatColor.BLUE + name + " MOTD:");
					for (String line : motd) {
						player.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "- " + line);
					}
				}
				else {
					player.sendMessage(ChatColor.AQUA + "Your guild's MOTD is blank.");
				}
			}
			else if (args.length > 1) {
				if (args[1].equalsIgnoreCase("set")) {
					if (GuildManager.isLeader(UUID) || GuildManager.isElder(UUID)) {
						if (args.length > 2) {
							if (StringUtils.isNumeric(args[2])) {
								int lineNumber = Integer.parseInt(args[2]);
								if (lineNumber > 0 && lineNumber <= level) {
									if (args.length > 3) {
										StringBuilder lineBuilder = new StringBuilder();
										for (int i = 3; i < args.length; i++) {
											lineBuilder.append(args[i] + " ");
										}
										String line = lineBuilder.toString();
										line = StringUtils.chop(line); //Remove extra space from the end
										if (line.length() <= 100) {
											if (motd.size() >= lineNumber) {
												motd.set((lineNumber - 1), line);
												plugin.getConfig().set("guilds." + tagLower + ".motd", motd);
												List<Player> onlinePlayers = new ArrayList<Player>();
												for (Player p : Bukkit.getServer().getOnlinePlayers()) {
													onlinePlayers.add(p);
												}
												for (Player guildPlayer: onlinePlayers) {
													if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
														if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
															guildPlayer.sendMessage(ChatColor.BLUE + "[" + tag + "] " + ChatColor.YELLOW + player.getName() + " has updated the guild MOTD.");
															guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
														}
													}
												}
												plugin.saveConfig();
											}
											else {
												player.sendMessage(ChatColor.RED + "Line " + lineNumber + " of your guild's MOTD is empty.");
											}
										}
										else {
											player.sendMessage(ChatColor.RED + "Line message can't be longer than 100 characters.");
										}
									}
									else {
										player.sendMessage(ChatColor.RED + "Wrong usage, try /g motd set [line number] [message]");
									}
								}
								else {
									player.sendMessage(ChatColor.RED + "Invalid line number, you may have 1 line per guild level.");
								}
							}
							else {
								player.sendMessage(ChatColor.RED + "Wrong usage, try /g motd set [line number] [message]");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + "Wrong usage, try /g motd set [line number] [message]");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "You must be a leader or an elder to edit the guild MOTD.");
					}
				}
				else if (args[1].equalsIgnoreCase("add")) {
					if (GuildManager.isLeader(UUID) || GuildManager.isElder(UUID)) {
						if (args.length > 2) {
							if (motd.size() < level) {
								StringBuilder lineBuilder = new StringBuilder();
								for (int i = 2; i < args.length; i++) {
									lineBuilder.append(args[i] + " ");
								}
								String line = lineBuilder.toString();
								line = StringUtils.chop(line); //Remove extra space from the end
								if (line.length() <= 100) {
									motd.add(line);
									plugin.getConfig().set("guilds." + tagLower + ".motd", motd);
									List<Player> onlinePlayers = new ArrayList<Player>();
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										onlinePlayers.add(p);
									}
									for (Player guildPlayer: onlinePlayers) {
										if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
											if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
												guildPlayer.sendMessage(ChatColor.BLUE + "[" + tag + "] " + ChatColor.YELLOW + player.getName() + " has updated the guild MOTD.");
												guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
											}
										}
									}
									plugin.saveConfig();
								}
								else {
									player.sendMessage(ChatColor.RED + "Line message can't be longer than 100 characters.");
								}
							}
							else {
								player.sendMessage(ChatColor.RED + "You can't add any more lines to your guild's MOTD, you may have 1 line per guild level.");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + "Wrong usage, try /g motd set [line number] [message]");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "You must be a leader or an elder to edit the guild MOTD.");
					}
				}
				else if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("remove")) {
					if (GuildManager.isLeader(UUID) || GuildManager.isElder(UUID)) {
						if (args.length > 2) {
							if (StringUtils.isNumeric(args[2])) {
								int lineNumber = Integer.parseInt(args[2]);
								if (motd.size() >= lineNumber) {
									motd.remove(lineNumber - 1);
									plugin.getConfig().set("guilds." + tagLower + ".motd", motd);
									List<Player> onlinePlayers = new ArrayList<Player>();
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										onlinePlayers.add(p);
									}
									for (Player guildPlayer: onlinePlayers) {
										if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
											if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
												guildPlayer.sendMessage(ChatColor.BLUE + "[" + tag + "] " + ChatColor.YELLOW + player.getName() + " has updated the guild MOTD.");
												guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
											}
										}
									}
									plugin.saveConfig();
								}
								else {
									player.sendMessage(ChatColor.RED + "Line " + lineNumber + " of your guild's MOTD is empty.");
								}
							}
							else {
								player.sendMessage("Wrong usage, try /g motd remove [line number]");
							}
							
						}
						else {
							player.sendMessage(ChatColor.RED + "Wrong usage, try /g motd remove [line number]");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "You must be a leader or an elder to edit the guild MOTD.");
					}
				}
				else if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("wipe")) {
					if (GuildManager.isLeader(UUID) || GuildManager.isElder(UUID)) {
						motd.clear();
						plugin.getConfig().set("guilds." + tagLower + ".motd", motd);
						List<Player> onlinePlayers = new ArrayList<Player>();
						for (Player p : Bukkit.getServer().getOnlinePlayers()) {
							onlinePlayers.add(p);
						}
						for (Player guildPlayer : onlinePlayers) {
							if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
								if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
									guildPlayer.sendMessage(ChatColor.BLUE + "[" + tag + "] " + ChatColor.YELLOW + player.getName() + " has cleared the guild MOTD.");
									guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
								}
							}
						}
						plugin.saveConfig();
					}
					else {
						player.sendMessage(ChatColor.RED + "You must be a leader or an elder to edit the guild MOTD.");
					}
				}
				else {
					player.sendMessage(ChatColor.RED + "Wrong usage, try /g motd add/remove/set/clear");
				}
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
	
	@EventHandler
	public void onJoin (PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			String name = GuildManager.getGuildName(tagLower);
			List<String> motd = plugin.getConfig().getStringList("guilds." + tagLower + ".motd");
			if (motd.size() > 0) {
				player.sendMessage(ChatColor.BLUE + name + " MOTD:");
				for (String line : motd) {
					player.sendMessage(ChatColor.AQUA + "" + ChatColor.ITALIC + "- " + line);
				}
			}
		}
	}
}