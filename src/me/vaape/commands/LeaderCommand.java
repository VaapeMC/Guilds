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

public class LeaderCommand {

Guilds plugin;
	
	public LeaderCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public void executeLeaderCommand (Player player, String[] args) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			String name = GuildManager.getGuildName(tagLower);
			if (GuildManager.isLeader(UUID)) {
				if (args.length > 1) {
					OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[1]);
					if (reciever.hasPlayedBefore() || reciever.isOnline()) {
						String rUUID = reciever.getUniqueId().toString();
						if (GuildManager.getPlayerGuildTag(rUUID) == tag) {
							if (rUUID != UUID) {
								if (GuildManager.isOfficer(rUUID)) {
									GuildManager.promote(rUUID, tag);
									Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.YELLOW + reciever.getName() + " is the new leader of " + name + ".");
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
									player.sendMessage(ChatColor.RED + "You can only pass ownership of the guild to officers.");
								}
							}
							else {
								player.sendMessage(ChatColor.RED + "You are already the leader of your guild.");
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
					player.sendMessage(ChatColor.RED + "Wrong usage, try /g leader [player]");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "Only leaders can pass ownership of the guild.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
}
