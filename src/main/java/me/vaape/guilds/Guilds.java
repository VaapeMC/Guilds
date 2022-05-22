package me.vaape.guilds;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.vaape.commands.ChatCommand;
import me.vaape.commands.ChestCommand;
import me.vaape.commands.GuildCommand;
import me.vaape.commands.HomeCommand;
import me.vaape.commands.MotdCommand;

public class Guilds extends JavaPlugin {
	
	public static Guilds instance;
	public static FileConfiguration config;
	private static HomeCommand homeCommand;
	private static MotdCommand motdCommand;
	private static ChatCommand chatCommand;
	private static ChestCommand chestCommand;
	
	@Override
	public void onEnable() {
		loadConfiguration();
		getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Guilds has been enabled!");
		this.getCommand("guild").setExecutor(new GuildCommand(this));
		this.getCommand("g").setExecutor(new GuildCommand(this));
		this.getCommand("gc").setExecutor(new ChatCommand(this));
		this.getCommand("ac").setExecutor(new ChatCommand(this));
		
		instance = this;
		
		homeCommand = new HomeCommand(instance);
	    this.getServer().getPluginManager().registerEvents(homeCommand, instance);
	    motdCommand = new MotdCommand(instance);
	    this.getServer().getPluginManager().registerEvents(motdCommand, instance);
	    chatCommand = new ChatCommand(instance);
	    this.getServer().getPluginManager().registerEvents(chatCommand, instance);
	    chestCommand = new ChestCommand(instance);
	    this.getServer().getPluginManager().registerEvents(chestCommand, instance);

		GuildManager.resetPlayersViewingChests(); //There is a bug where it keeps players in there so it wont open
	}
	
	@Override
	public void onDisable() {
		//Save all g chest players
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.getOpenInventory() == null) return;
			if (player.getOpenInventory().getType() != InventoryType.CHEST) return;

			Inventory gChest = player.getOpenInventory().getTopInventory();
			String chestTag = player.getOpenInventory().getTitle().substring(4);
			String tagLower = chestTag.toLowerCase();
			List<String> viewers = getInstance().getConfig().getStringList("guilds." + tagLower + ".players viewing chest");

			if (viewers.contains(player.getUniqueId().toString())) {
				viewers.remove(player.getUniqueId().toString());
				getInstance().getConfig().set("guilds." + tagLower + ".players viewing chest", viewers);
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "removed player");
				if (viewers.size() == 0) {
					ItemStack[] contents = gChest.getContents();
					List<ItemStack> mappedContent = Arrays
							.stream(contents)
							.map(content -> Objects.requireNonNullElseGet(content, () -> new ItemStack(Material.AIR))).collect(Collectors.toList());
					getInstance().getConfig().set("guilds." + tagLower + ".chest", mappedContent);
					Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "saved chest");
				}
				getInstance().saveConfig();
			}
		}
		instance = null;
	}
	
	public static Guilds getInstance() {
		return instance;
	}
	
	public void loadConfiguration() {
		final FileConfiguration config = this.getConfig();
		String[] allyList = {"Gangs", "Myst"};
		String[] elderList = {"05036bbe-f396-11e7-8c3f-9a214cf093ae", "05036fd8-f396-11e7-8c3f-9a214cf093ae"};
		String[] memberList = {"050372b2-f396-11e7-8c3f-9a214cf093ae", "05037596-f396-11e7-8c3f-9a214cf093ae"};
		String[] recruitList = {"05037898-f396-11e7-8c3f-9a214cf093ae", "05037b9a-f396-11e7-8c3f-9a214cf093ae"};
		String[] inChestList = {"05037898-f396-11e7-8c3f-9a214cf093ae", "05037b9a-f396-11e7-8c3f-9a214cf093ae"};
		config.addDefault("guilds.guild.tag", "gtag");
		config.addDefault("guilds.guild.name", "Name");
		config.addDefault("guilds.guild.level", 5);
		config.addDefault("guilds.guild.allies", allyList);
		config.addDefault("guilds.guild.home.world", "world");
		config.addDefault("guilds.guild.home.x", 10);
		config.addDefault("guilds.guild.home.y", 70);
		config.addDefault("guilds.guild.home.z", 10);
		config.addDefault("guilds.guild.motd", "world");
		config.addDefault("guilds.guild.leader", "1cc8faa2-f396-11e7-8c3f-9a214cf093ae");
		config.addDefault("guilds.guild.elders", elderList);
		config.addDefault("guilds.guild.members", memberList);
		config.addDefault("guilds.guild.recruits", recruitList);
		config.addDefault("guilds.guild.invited", "1cc8faa2-f396-11e7-8c3f-9a214cf093ae");
		config.addDefault("guilds.guild.requests", "Myst");
		config.addDefault("guilds.guild.players viewing chest", inChestList);
		config.options().copyDefaults(true);
		
		for (String guildTag : config.getConfigurationSection("guilds").getKeys(false)) {
			config.set("guilds." + guildTag + ".invited", null);
			config.set("guilds." + guildTag + ".requests", null);
		}
		List<Player> onlinePlayers = new ArrayList<Player>();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			onlinePlayers.add(p);
		}
		for (Player p : onlinePlayers) {
			p.closeInventory();
		}
		saveConfig();
	}


}