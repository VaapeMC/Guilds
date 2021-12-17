package me.vaape.commands;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class ChestCommand implements Listener{
	
	Guilds plugin;
	
	private HashMap<UUID, String> playersViewingChest = new HashMap<UUID, String>();
	
	public ChestCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeChestCommand (Player player, String[] args) {
		
		if (args.length > 1) {
			if (!player.hasPermission("guilds.admin")) {
				player.sendMessage(ChatColor.RED + "You do not have permission to see other Guilds' chests.");
				return;
			}
			
			if (GuildManager.getGuildTag(args[1].toLowerCase()) == null) { //If it is not a guild
				
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "getGuildTag = null");
				
				OfflinePlayer reciever = Bukkit.getOfflinePlayer(args[1]);
				if (reciever.hasPlayedBefore() || reciever.isOnline()) { //If it is a player
					
					Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "args[1] is player");
					
					String rUUID = reciever.getUniqueId().toString();
					String rTag = GuildManager.getPlayerGuildTag(rUUID);
					
					Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "rTag = " + rTag);
					
					String rTagLower = rTag.toLowerCase();
					
					if (rTag != null) {
						
						Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "rTag != null");
						
						List<String> viewers = plugin.getConfig().getStringList("guilds." + rTagLower + ".players viewing chest");
						if (plugin.getConfig().getStringList("guilds." + rTagLower + ".players viewing chest").isEmpty()) {
							
							Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "players viewing chest is empty");

							Inventory guildChest = Bukkit.createInventory(null, 54, ChatColor.BLUE + "" + ChatColor.BOLD + rTag);
							if (plugin.getConfig().get("guilds." + rTagLower + ".chest") != null) {
								//plugin.reloadConfig();
								
								Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "chest != null, opening g chest contents");
								
								Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "guilds."+rTagLower+".chest = " + plugin.getConfig().get("guilds." + rTagLower + ".chest").toString());

								List<ItemStack> contentsList = (List<ItemStack>) plugin.getConfig().getItemStack("guilds." + rTagLower + ".chest");
								ItemStack[] contents = new ItemStack[] {};
								contents = contentsList.toArray(contents);
								guildChest.setContents(contents);
								player.openInventory(guildChest);
							}
							else {
								
								Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "g chest == null, opening new container");

								player.openInventory(guildChest);
							}
						}
						else {
							
							Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "players viewing chest not empty, opening already open chest");
							
							Player viewer = Bukkit.getPlayer(java.util.UUID.fromString(viewers.get(0)));
							Inventory openChest = viewer.getOpenInventory().getTopInventory();
							player.openInventory(openChest);
						}
						viewers.add(player.getUniqueId().toString());
						plugin.getConfig().set("guilds." + rTagLower + ".players viewing chest", viewers);
						plugin.saveConfig();
					}
					else {
						player.sendMessage(ChatColor.RED + reciever.getName() + " is not in a guild.");
					}
				}
				else {
					player.sendMessage(ChatColor.RED + args[1] + " isn't registered as a player or guild.");
				}
			}
			else { //If Guild exists
				
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "Guild exists");

				
				String tag = GuildManager.getGuildTag(args[1].toLowerCase());
				String tagLower = tag.toLowerCase();
				
				List<String> viewers = plugin.getConfig().getStringList("guilds." + tagLower + ".players viewing chest");
				if (plugin.getConfig().getStringList("guilds." + tagLower + ".players viewing chest").isEmpty()) {
					
					Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "players viewing chest was empty");

					Inventory guildChest = Bukkit.createInventory(null, 54, ChatColor.BLUE + "" + ChatColor.BOLD + tag);
					if (plugin.getConfig().get("guilds." + tagLower + ".chest") != null) {
						
						Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "chest != null, adding g chest contents");

						//plugin.reloadConfig();
						
						Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "guilds."+tagLower+".chest = " + plugin.getConfig().get("guilds." + tagLower + ".chest").toString());
						
						List<ItemStack> contentsList = (List<ItemStack>) plugin.getConfig().getItemStack("guilds." + tagLower + ".chest");
						ItemStack[] contents = new ItemStack[] {};
						contents = contentsList.toArray(contents);
						guildChest.setContents(contents);
						player.openInventory(guildChest);
					}
					else {
						Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "chest == null, making new chest");

						player.openInventory(guildChest);
					}
				}
				else {
					
					Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "players viewing chest not empty, opening already open g chest");

					Player viewer = Bukkit.getPlayer(java.util.UUID.fromString(viewers.get(0)));
					Inventory openChest = viewer.getOpenInventory().getTopInventory();
					player.openInventory(openChest);
				}
				viewers.add(player.getUniqueId().toString());
				plugin.getConfig().set("guilds." + tagLower + ".players viewing chest", viewers);
				plugin.saveConfig();
			}
		}
		
		//Looking at their own g chest
		else {
			String UUID = player.getUniqueId().toString();
			if (GuildManager.getPlayerGuildTag(UUID) != null) {
				
				Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "getPlayerGuildTag != null");
				
				String tag = GuildManager.getPlayerGuildTag(UUID);
				String tagLower = tag.toLowerCase();
				if (GuildManager.getLevel(tagLower) > 4) {
					if (!GuildManager.getRole(UUID).equals("recruit")) {
						List<String> viewers = plugin.getConfig().getStringList("guilds." + tagLower + ".players viewing chest");
						if (plugin.getConfig().getStringList("guilds." + tagLower + ".players viewing chest").isEmpty()) {
							
							Bukkit.getServer().broadcastMessage(ChatColor.YELLOW + "players viewing chest is empty");
							
							Inventory guildChest = Bukkit.createInventory(null, 54, ChatColor.BLUE + "" + ChatColor.BOLD + tag);
							if (plugin.getConfig().get("guilds." + tagLower + ".chest") != null) {
								//plugin.reloadConfig();
								
								Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "chest not null, opening chest");
								
								Bukkit.getServer().broadcastMessage(ChatColor.DARK_PURPLE + "guilds."+tagLower+".chest = " + plugin.getConfig().get("guilds." + tagLower + ".chest").toString());
								
								List<ItemStack> contentsList = (List<ItemStack>) plugin.getConfig().get("guilds." + tagLower + ".chest");
								ItemStack[] contents = new ItemStack[] {};
								contents = contentsList.toArray(contents);
								guildChest.setContents(contents);
								player.openInventory(guildChest);
							}
							else {
								
								Bukkit.getServer().broadcastMessage(ChatColor.GREEN + "guild chest = null : opening new guild chest");
								
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
	}
	
	@EventHandler
	public void onInvClose (InventoryCloseEvent event) {
		if (event.getPlayer() instanceof Player) {
			Player player = (Player) event.getPlayer();
			
			if (event.getInventory().getType() != InventoryType.CHEST) {
				return;
			}
			
			Inventory gChest = event.getInventory();
			String chestTag = event.getView().getTitle().substring(4);
			String tagLower = chestTag.toLowerCase();
			List<String> viewers = plugin.getConfig().getStringList("guilds." + tagLower + ".players viewing chest");
			
			if (viewers == null) {
				return;
			}
			
			if (viewers.contains(player.getUniqueId().toString())) {
				
				Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "viewers contains player");
				
				viewers.remove(player.getUniqueId().toString());
				plugin.getConfig().set("guilds." + tagLower + ".players viewing chest", viewers);
				if (viewers.size() == 0) {
					
					Bukkit.getServer().broadcastMessage(ChatColor.LIGHT_PURPLE + "viewers size was 0 (player was the last viewer)");

					ItemStack[] contents = gChest.getContents();
                    List<ItemStack> mappedContent = Arrays
                            .stream(contents)
                            .map(content -> Objects.requireNonNullElseGet(content, () -> new ItemStack(Material.AIR))).collect(Collectors.toList());
                    plugin.getConfig().set("guilds." + tagLower + ".chest", mappedContent);
				}
				plugin.saveConfig();
			}
		}
	}
}
