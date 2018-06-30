package me.vaape.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class InfoCommand {
	
Guilds plugin;
	
	public InfoCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("deprecation")
	public void executeInfoCommand (Player player, String[] args) {
		String UUID = player.getUniqueId().toString();
		String tag = GuildManager.getPlayerGuildTag(UUID);
		if (args.length == 1) {
			if (tag != null) {
				GuildManager.getInfo(player, tag);
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not in a guild.");
			}
		}
		else if (args.length > 1) {
			if (GuildManager.getGuildTag(args[1]) == null) {
				OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[1]);
				if (reciever.hasPlayedBefore() || reciever.isOnline()) {
					String rUUID = reciever.getUniqueId().toString();
					String rTag = GuildManager.getPlayerGuildTag(rUUID);
					if (rTag != null) {
						GuildManager.getInfo(player, rTag);
					}
					else {
						player.sendMessage(ChatColor.RED + reciever.getName() + " is not in a guild.");
					}
				}
				else {
					player.sendMessage(ChatColor.RED + args[1] + " isn't registered as a player or guild.");
				}
			}
			else {
				GuildManager.getInfo(player, args[1].toLowerCase());
			}
		}
	}
}
