package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class DemoteCommand {
	
Guilds plugin;
	
	public DemoteCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public void executeDemoteCommand (Player player, String[] args) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			if (args.length > 1) {
				OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[1]);
				if (reciever.hasPlayedBefore() || reciever.isOnline()) {
					String rUUID = reciever.getUniqueId().toString();
					if (GuildManager.getPlayerGuildTag(rUUID) == tag) {
						if (!rUUID.equals(UUID)) {
							String rRole = GuildManager.getRole(rUUID);
							String role = GuildManager.getRole(UUID);
							if (rRole.equals("leader")) {
								player.sendMessage(ChatColor.RED + "You can not demote a leader.");
							}
							else if (rRole.equals("elder")) {
								if (role.equals("leader")) {
									GuildManager.demote(rUUID, tagLower);
									String newRole = GuildManager.getRole(rUUID);
									String name = GuildManager.getGuildName(tag);
									Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.YELLOW + reciever.getName() + " has been demoted to " + newRole + " in " + name + ".");
									List<Player> onlinePlayers = new ArrayList<Player>();
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										onlinePlayers.add(p);
									}
									for (Player guildPlayer : onlinePlayers) {
										if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
											if (reciever.getName().equals(guildPlayer.getName())) {
												guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
											}
										}
									}
								}
								else {
									player.sendMessage(ChatColor.RED + "You must be a leader to demote elders.");
								}
							}
							else if (rRole.equals("member")) {
								if (role.equals("leader") || role.equals("elder")) {
									GuildManager.demote(rUUID, tagLower);
									String newRole = GuildManager.getRole(rUUID);
									String name = GuildManager.getGuildName(tag);
									Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.YELLOW + reciever.getName() + " has been demoted to " + newRole + " in " + name + ".");
									List<Player> onlinePlayers = new ArrayList<Player>();
									for (Player p : Bukkit.getServer().getOnlinePlayers()) {
										onlinePlayers.add(p);
									}
									for (Player guildPlayer : onlinePlayers) {
										if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
											if (reciever.getName().equals(guildPlayer.getName())) {
												guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
											}
										}
									}
								}
								else {
									player.sendMessage(ChatColor.RED + "You must be a leader to promote members.");
								}
							}
							else if (rRole.equals("recruit")) {
								player.sendMessage(ChatColor.RED + "Recruits can not be demoted. To kick a player use /g kick.");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + "You can not promote yourself.");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + reciever.getName() + " is not in your guild.");
					}
				}
				else {
					player.sendMessage(ChatColor.RED + args[1] + " can't be found.");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "Wrong usage, try /g promote [player]");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
}
