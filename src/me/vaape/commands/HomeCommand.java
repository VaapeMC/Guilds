package me.vaape.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class HomeCommand implements Listener{
	
Guilds plugin;
public int counter = 0;
public int number;
public static int home;
public static Map<String, Integer> tasks = new HashMap<String, Integer>();
	
	public HomeCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeHomeCommand (Player player) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			if (GuildManager.getLevel(tag) > 2) {
				if (!GuildManager.getRole(UUID).equals("recruit")) {
					if (GuildManager.getHome(tag) != null) {
						Location guildHome = GuildManager.getHome(tag);
						player.sendMessage(ChatColor.BLUE + "Teleporting to guild home in 5 seconds, don't move...");
						counter = 0;
						cancelRunnable(player);
						tasks.remove(UUID);
						home = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								player.sendMessage(ChatColor.BLUE + "Teleporting to guild home...");
								tasks.remove(UUID);
								player.teleport(guildHome);
							}
						}, 5 * 20);
						tasks.put(UUID, home);
					}
					else {
						player.sendMessage(ChatColor.RED + "Your guild does not have a guild home set. Use /guild sethome to set a new one.");
					}
				}
				else {
					player.sendMessage(ChatColor.RED + "You must be a member or above to use /guild home.");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You guild must be level 3 to use /guild home.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
	
	public void cancelRunnable(Player player) {
		String UUID = player.getUniqueId().toString();
		if (tasks.containsKey(UUID)) {
			Bukkit.getScheduler().cancelTask(tasks.get(UUID));
			tasks.remove(UUID);
		}
	}
	
	@EventHandler
	public void onMove (PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location from = event.getFrom();
		Location to = event.getTo();
		if ((Math.round(from.getX() * 100) / 100) == (Math.round(to.getX() * 100) /100) && //they can move 0.001 blocks
			(Math.round(from.getY() * 100) / 100) == (Math.round(to.getY() * 100) / 100) && 
			(Math.round(from.getZ() * 100) / 100) == (Math.round(to.getZ() * 100) / 100)) {
			return;
		}
		else {
			String UUID = player.getUniqueId().toString();
			if (tasks.containsKey(UUID)) {
				cancelRunnable(player);
				tasks.remove(UUID);
				player.sendMessage(ChatColor.RED + "Teleport cancelled...");
			}
		}
	}
	
	@EventHandler
	public void onDamage (EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			String UUID = player.getUniqueId().toString();
			if (tasks.containsKey(UUID)) {
				cancelRunnable(player);
				tasks.remove(UUID);
				player.sendMessage(ChatColor.RED + "Teleport cancelled...");
			}
		}
	}
}
