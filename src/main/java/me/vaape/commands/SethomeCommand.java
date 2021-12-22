package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class SethomeCommand {
	
Guilds plugin;
	
	public SethomeCommand(Guilds plugin) { 
		this.plugin = plugin;
	}//done
	
	public void executeSethomeCommand (Player player) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			if (GuildManager.getLevel(tag) > 2) {
				if (GuildManager.getRole(UUID).equals("leader") || GuildManager.getRole(UUID).equals("elder")) {
					Location location = player.getLocation();
					String tagLower = tag.toLowerCase();
					GuildManager.setHome(location, tagLower);
					List<Player> onlinePlayers = new ArrayList<Player>();
					for (Player p : Bukkit.getServer().getOnlinePlayers()) {
						onlinePlayers.add(p);
					}
					for (Player guildPlayer : onlinePlayers) {
						if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
							if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
								guildPlayer.sendMessage(ChatColor.BLUE + "[" + tag + "] " + ChatColor.YELLOW + player.getName() + " has set a guild home location.");
								guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
							}
						}
					}
				}
				else {
					player.sendMessage(ChatColor.BLUE + "You must be a leader or an elder to set the guild's home.");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You guild must be level 3 to set a guild home.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
}
