package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.vaape.events.Events;
import me.vaape.events.GuildWars;
import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class DisbandCommand {
	
	Guilds plugin;
	
	public DisbandCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeDisbandCommand (Player player) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tagLower = GuildManager.getPlayerGuildTag(UUID).toLowerCase();
			String name = GuildManager.getGuildName(tagLower);
			if (GuildManager.isLeader(UUID)) {
				if (GuildWars.gwRunning && Events.getInstance().getConfig().getString("defenders").toLowerCase().equals(tagLower)) {
					player.sendMessage(ChatColor.RED + "You can not disband a guild during Guild Wars if you are currently defending.");
					return;
				}
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
				GuildManager.delete(tagLower);
				Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.RED + name + " has been disbanded.");
			}
			else {
				player.sendMessage(ChatColor.RED + "Only leaders can disband their guild.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
}
