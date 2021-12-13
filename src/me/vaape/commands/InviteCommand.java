package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class InviteCommand {

	Guilds plugin;
	
	public InviteCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeInviteCommand (Player sender, String[] args) {
		String UUID = sender.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			if (GuildManager.isLeader(UUID) || GuildManager.isElder(UUID)) {
				if (args.length > 1) {
					Player reciever = Bukkit.getServer().getPlayer(args[1]);
					String tag = GuildManager.getPlayerGuildTag(UUID);
					String tagLower = tag.toLowerCase();
					String name = GuildManager.getGuildName(tagLower);
					int capacity = GuildManager.getLevel(tagLower) + 2;
					int totalPlayers = GuildManager.getTotalPlayers(tagLower);
					if (reciever != null) {
						String rUUID = reciever.getUniqueId().toString();
						if (GuildManager.getPlayerGuildTag(rUUID) == null) {
							if (reciever.isOnline()) {
								if (totalPlayers < capacity) {
									if (!plugin.getConfig().getStringList("guilds." + tagLower + ".invited").contains(UUID)) {
										GuildManager.invite(rUUID, tag);
										reciever.sendMessage(ChatColor.BLUE + sender.getName() + " has invited you to join " + name + ", type" + ChatColor.GREEN + " /g accept " + tag + ChatColor.BLUE + " to join. This invitation will expire in 2 minutes.");
										reciever.playSound(reciever.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
										List<Player> onlinePlayers = new ArrayList<Player>();
										for (Player p : Bukkit.getServer().getOnlinePlayers()) {
											onlinePlayers.add(p);
										}
										for (Player guildPlayer : onlinePlayers) {
											if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
												if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
													guildPlayer.sendMessage(ChatColor.BLUE + "[" + tag + "] " + ChatColor.YELLOW + sender.getName() + " has invited " + reciever.getName() + " to join your guild.");
												}
											}
										}
									}
									else {
										sender.sendMessage(ChatColor.RED + reciever.getName() + " has already recieved an invitation from your clan recently.");
									}
								}
								else {
									sender.sendMessage(ChatColor.RED + "Your guild is at max capacity.");
								}
							}
							else {
								sender.sendMessage(ChatColor.RED + reciever.getName() + " can't be found.");
							}
						}
						else {
							sender.sendMessage(ChatColor.RED + reciever.getName() + " is already in a guild.");
						}
					}
					else {
						sender.sendMessage(ChatColor.RED + args[1] + " can't be found.");
					}
				}
				else {
					sender.sendMessage(ChatColor.RED + "Wrong usage, try /g invite [player]");
				}
			}
			else {
				sender.sendMessage(ChatColor.RED + "You must be a leader or an elder to invite new recruits.");
			}
		}
		else {
			sender.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
}
