package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class LeaveCommand {
	
	Guilds plugin;
	
	public LeaveCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeLeaveCommand (Player player) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			String name = GuildManager.getGuildName(tagLower);
			if (GuildManager.getRole(UUID) != "leader") {
				if (player.hasMetadata("royal")) {
					player.sendMessage(ChatColor.RED + "The king can not leave their guild.");
					return;
				}
				GuildManager.leaveGuild(UUID);
				Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.YELLOW + player.getName() + " has left " + name + ".");
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
				player.sendMessage(ChatColor.RED + "You can not leave a guild you lead, use /g leader to pass ownership of the guild or /g disband to disband the guild.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}

}
