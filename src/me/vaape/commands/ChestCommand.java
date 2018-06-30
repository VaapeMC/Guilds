package me.vaape.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class ChestCommand implements Listener{
	
	Guilds plugin;
	
	public ChestCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("unchecked")
	public void executeChestCommand (Player player) {
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			if (GuildManager.getLevel(tagLower) > 4) {
				if (!GuildManager.getRole(UUID).equals("recruit")) {
					List<String> viewers = plugin.getConfig().getStringList("guilds." + tagLower + ".players viewing chest");
					if (plugin.getConfig().getStringList("guilds." + tagLower + ".players viewing chest").isEmpty()) {
						Inventory guildChest = Bukkit.createInventory(null, 54, ChatColor.BLUE + "" + ChatColor.BOLD + tag);
						if (plugin.getConfig().get("guilds." + tagLower + ".chest") != null) {
							plugin.reloadConfig();
							
							List<ItemStack> contentsList = (List<ItemStack>) plugin.getConfig().getList("guilds." + tagLower + ".chest");
							ItemStack[] contents = new ItemStack[] {};
							contents = contentsList.toArray(contents);
							guildChest.setContents(contents);
							player.openInventory(guildChest);
						}
						else {
							player.openInventory(guildChest);
						}
					}
					else {
						Player viewer = Bukkit.getPlayer(java.util.UUID.fromString(viewers.get(0)));
						Inventory openChest = viewer.getOpenInventory().getTopInventory();
						player.openInventory(openChest);
					}
					viewers.add(player.getUniqueId().toString());
					plugin.getConfig().set("guilds." + tagLower + ".players viewing chest", viewers);
					plugin.saveConfig();
				}
				else {
					player.sendMessage(ChatColor.RED + "Only members and above can access the guild chest.");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "Your guild must be level 5 to access its guild chest.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}
	
	@EventHandler
	public void onInvClose (InventoryCloseEvent event) {
		if (event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			String tag = GuildManager.getPlayerGuildTag(player.getUniqueId().toString());
			if (tag != null) {
				String tagLower = tag.toLowerCase();
				List<String> viewers = plugin.getConfig().getStringList("guilds." + tagLower + ".players viewing chest");
				if (viewers.contains(player.getUniqueId().toString())) {
					viewers.remove(player.getUniqueId().toString());
					plugin.getConfig().set("guilds." + tagLower + ".players viewing chest", viewers);
					if (viewers.size() == 0) {
						Inventory gChest = event.getInventory();
						ItemStack[] contents = gChest.getContents();
						plugin.getConfig().set("guilds." + tagLower + ".chest", contents);
					}
					plugin.saveConfig();
				}
			}
		}
	}
}
