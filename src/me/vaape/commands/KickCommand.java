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

public class KickCommand {
	
	Guilds plugin;
	
	public KickCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	//TODO can't kick leaders/elders
	@SuppressWarnings("deprecation")
	public void executeKickCommand (Player player, String[] args) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			String name = GuildManager.getGuildName(tagLower);
			if (GuildManager.isLeader(UUID) || GuildManager.isElder(UUID)) {
				if (args.length > 1) {
					OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[1]);
					if (reciever.hasPlayedBefore() || reciever.isOnline()) {
						String rUUID = reciever.getUniqueId().toString();
						if (GuildManager.getPlayerGuildTag(rUUID) == tag) {
							if (GuildManager.isMember(rUUID) || GuildManager.isRecruit(rUUID)) {
								GuildManager.kick(rUUID, tagLower);
								Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.YELLOW + reciever.getName() + " has been kicked from " + name + ".");
								List<Player> onlinePlayers = new ArrayList<Player>();
								for (Player p : Bukkit.getServer().getOnlinePlayers()) {
									onlinePlayers.add(p);
								}
								for (Player guildPlayer : onlinePlayers) {
									if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
										if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString()) || reciever.getName().equals(guildPlayer.getName())) {
											guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
										}
									}
								}
							}
							else {
								player.sendMessage(ChatColor.RED + "Elders may only kick members and recruits.");
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
					player.sendMessage(ChatColor.RED + "Wrong usage, try /g kick [player]");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You must be a leader or an elder to kick players.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
}
