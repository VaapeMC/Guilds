package me.vaape.commands;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand {

	Guilds plugin;

	public ReloadCommand(Guilds plugin) {
		this.plugin = plugin;
	}//done
	
	public void executeReloadCommand (Player player) {
		if (!player.hasPermission("guilds.reload")) {
			player.sendMessage(ChatColor.RED + "You do not have permission to do that.");
			return;
		}
		GuildManager.reloadGuildConfig();
		player.sendMessage(ChatColor.GREEN + "Guilds config reloaded.");
	}

}
