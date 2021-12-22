package me.vaape.guilds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.vaape.commands.ChatCommand;
import me.vaape.events.Fishing;
import me.vaape.events.GuildWars;
import net.md_5.bungee.api.ChatColor;

public class GuildManager {
	
	private static FileConfiguration config = Guilds.getInstance().getConfig();
	
	public static String getPlayerGuildTag(String UUID){
		ArrayList<String> guildTags = new ArrayList<String>();
		for (String tag : config.getConfigurationSection("guilds").getKeys(false)) {
			guildTags.add(tag);
		}
		for (String key : guildTags) {
			if (UUID.equals(config.getString("guilds." + key + ".leader"))) {
				return config.getString("guilds." + key + ".tag");
			}
			for (String elderUUID : config.getStringList("guilds." + key + ".elders")) {
				if (elderUUID.equals(UUID)) {
					return config.getString("guilds." + key + ".tag");
				}
			}
			for (String memberUUID : config.getStringList("guilds." + key + ".members")) {
				if (memberUUID.equals(UUID)) {
					return config.getString("guilds." + key + ".tag");
				}
			}
			for (String recruitUUID : config.getStringList("guilds." + key + ".recruits")) {
				if (recruitUUID.equals(UUID)) {
					return config.getString("guilds." + key + ".tag");
				}
			}
		}
		return null;
	}
	
	public static ArrayList<String> getNameList(){
		ArrayList<String> guildTags = new ArrayList<String>();
		for (String tag : config.getConfigurationSection("guilds").getKeys(false)) {
			guildTags.add(tag);
		}
		ArrayList<String> guildNames = new ArrayList<String>();
		for (String tag : guildTags) {
			guildNames.add(config.getString("guilds." + tag + ".name").toLowerCase());
		}
		return guildNames;
	}
	
	public static String getGuildName (String tag) {
		String tagLower = tag.toLowerCase();
		if (config.getString("guilds." + tagLower + ".name") != null) {
			return config.getString("guilds." + tagLower + ".name");
		}
		return null;
	}
	
	public static String getGuildTag (String tag) {
		if (config.getString("guilds." + tag + ".tag") != null) {
			return config.getString("guilds." + tag + ".tag");
		}
		return null;
	}
	
	public static void createClan(String UUID, String tag, String name) {
		String tagLower = tag.toLowerCase();
		String[] emptyList = {};
		config.set("guilds." + tagLower, tag);
		config.set("guilds." + tagLower + ".tag", tag);
		config.set("guilds." + tagLower + ".name", name);
		config.set("guilds." + tagLower + ".level", 1);
		config.set("guilds." + tagLower + ".motd", Arrays.asList(emptyList));
		config.set("guilds." + tagLower + ".allies", Arrays.asList(emptyList));
		config.createSection("guilds." + tagLower + ".home");
		config.set("guilds." + tagLower + ".home.world", null);
		config.set("guilds." + tagLower + ".home.x", null);
		config.set("guilds." + tagLower + ".home.y", null);
		config.set("guilds." + tagLower + ".home.z", null);
		config.set("guilds." + tagLower + ".home.yaw", null);
		config.set("guilds." + tagLower + ".home.pitch", null);
		config.set("guilds." + tagLower + ".leader", UUID);
		config.set("guilds." + tagLower + ".elders", Arrays.asList(emptyList));
		config.set("guilds." + tagLower + ".members", Arrays.asList(emptyList));
		config.set("guilds." + tagLower + ".recruits", Arrays.asList(emptyList));
		config.set("guilds." + tagLower + ".invited", Arrays.asList(emptyList));
		config.set("guilds." + tagLower + ".requests", Arrays.asList(emptyList));
		config.set("guilds." + tagLower + ".players viewing chest", Arrays.asList(emptyList));
		Guilds.instance.saveConfig();
	}
	
	public static void leaveGuild(String UUID) {
		if (GuildWars.holders.contains(UUID)) {
			GuildWars.holders.remove(UUID);
		}
		if (ChatCommand.allyChat.contains(UUID)) {
			ChatCommand.allyChat.remove(UUID);
		}
		else if (ChatCommand.guildChat.contains(UUID)) {
			ChatCommand.guildChat.remove(UUID);
		}
		String tag = getPlayerGuildTag(UUID);
		String tagLower = tag.toLowerCase();
		String role = getRole(UUID);
		if (role != "leader") {
			config.getList("guilds." + tagLower + "." + role + "s").remove(UUID);
			Guilds.instance.saveConfig();
		}
	}
	
	public static void kick(String UUID, String tag) {
		if (GuildWars.holders.contains(UUID)) {
			GuildWars.holders.remove(UUID);
		}
		String role = GuildManager.getRole(UUID);
		config.getList("guilds." + tag + "." + role + "s").remove(UUID);
		Guilds.instance.saveConfig();
	}
	
	public static void levelUp(String tag) {
		String tagLower = tag.toLowerCase();
		config.set("guilds." + tagLower + ".level", config.getInt("guilds." + tagLower + ".level") + 1);
		Guilds.getInstance().saveConfig();
	}
	
	public static void delete(String tag) {
		String tagLower = tag.toLowerCase();
		config.set("guilds." + tagLower, null);
		Guilds.getInstance().saveConfig();
	}
	
	public static String getRole(String UUID) {
		if (getPlayerGuildTag(UUID) != null) {
			if (isLeader(UUID)) {
				return "leader";
			}
			if (isElder(UUID)) {
				return "elder";
			}
			if (isMember(UUID)) {
				return "member";
			}
			if (isRecruit(UUID)) {
				return "recruit";
			}
		}
		return null;
	}
	
	public static void sendGuildMessage (Player player, String tag, String message) {
		String tagLower = tag.toLowerCase();
		List<String> players = getGuildPlayers(tagLower);
		for (String playerUUID : players) {
			if (Bukkit.getServer().getPlayer(UUID.fromString(playerUUID)) != null) {
				Player guildPlayer = Bukkit.getServer().getPlayer(UUID.fromString(playerUUID));
				if (Fishing.isWinner(player)) {
					String symbol = "\u2742";
					guildPlayer.sendMessage(ChatColor.GREEN + "[" + tag + "] " + ChatColor.YELLOW + symbol + " " + ChatColor.RESET + player.getDisplayName() + ChatColor.GRAY + " " + message);
					Bukkit.getLogger().info(ChatColor.GREEN + "[" + tag + "] " + ChatColor.YELLOW + symbol + " " + ChatColor.RESET + player.getDisplayName() + ChatColor.GRAY + " " + message);
				}
				else {
					guildPlayer.sendMessage(ChatColor.GREEN + "[" + tag + "] " + ChatColor.RESET + player.getDisplayName() + ChatColor.GRAY + " " + message);
					Bukkit.getLogger().info(ChatColor.GREEN + "[" + tag + "] " + ChatColor.YELLOW + " " + ChatColor.RESET + player.getDisplayName() + ChatColor.GRAY + " " + message);
				}
			}
		}
	}
	
	public static void sendAllyMessage (Player player, String tag, String message) {
		String tagLower = tag.toLowerCase();
		List<String> players = getAllyPlayers(tagLower);
		for (String playerUUID : players) {
			if (Bukkit.getServer().getPlayer(UUID.fromString(playerUUID)) != null) {
				Player guildPlayer = Bukkit.getServer().getPlayer(UUID.fromString(playerUUID));
				if (Fishing.isWinner(player)) {
					String symbol = "\u2742";
					guildPlayer.sendMessage(ChatColor.LIGHT_PURPLE + "[" + tag + "] " + ChatColor.YELLOW + symbol + " " + ChatColor.RESET + player.getDisplayName() + ChatColor.GRAY + " " + message);
					Bukkit.getLogger().info(ChatColor.LIGHT_PURPLE + "[" + tag + "] " + ChatColor.YELLOW + symbol + " " + ChatColor.RESET + player.getDisplayName() + ChatColor.GRAY + " " + message);
				}
				else {
					guildPlayer.sendMessage(ChatColor.LIGHT_PURPLE + "[" + tag + "] " + ChatColor.RESET + player.getDisplayName() + ChatColor.GRAY + " " + message);
					Bukkit.getLogger().info(ChatColor.LIGHT_PURPLE + "[" + tag + "] " + ChatColor.YELLOW + " " + ChatColor.RESET + player.getDisplayName() + ChatColor.GRAY + " " + message);
				}
			}
		}
	}
	
	public static List<String> getGuildPlayers (String tag) {
		String tagLower = tag.toLowerCase();
		List<String> players = new ArrayList<String>();
		List<String> elders = config.getStringList("guilds." + tagLower + ".members");
		List<String> members = config.getStringList("guilds." + tagLower + ".elders");
		List<String> recruits = config.getStringList("guilds." + tagLower + ".recruits");
		players.add(getGuildLeader(tagLower));
		players.addAll(elders);
		players.addAll(members);
		players.addAll(recruits);
		return players;
	}
	
	public static List<String> getAllyPlayers (String tag) {
		String tagLower = tag.toLowerCase();
		List<String> players = new ArrayList<String>();
		List<String> allies = getAllies(tagLower);
		List<String> guildPlayers = getGuildPlayers(tagLower);
		players.addAll(guildPlayers);
		for (String ally : allies) {
			String allyTag = ally.toLowerCase();
			players.addAll(getGuildPlayers(allyTag));
		}
		return players;
	}
	
	public static void promote(String UUID, String tag) {
		String role = getRole(UUID);
		String tagLower = tag.toLowerCase();
		if (role.equals("elder")) {
			String leader = config.getString("guilds." + tagLower + ".leader");
			final List<String> elders = config.getStringList("guilds." + tagLower + ".elders");
			elders.add(leader);
			elders.remove(UUID);
			config.set("guilds." + tagLower + ".elders", elders);
			config.set("guilds." + tagLower + ".leader", UUID);
			Guilds.instance.saveConfig();
		}
		if (role.equals("member")) {
			final List<String> members = config.getStringList("guilds." + tagLower + ".members");
			members.remove(UUID);
			config.set("guilds." + tagLower + ".members", members);
			final List<String> elders = config.getStringList("guilds." + tagLower + ".elders");
			elders.add(UUID);
			config.set("guilds." + tagLower + ".elders", elders);
			Guilds.instance.saveConfig();
			
		}
		if (role.equals("recruit")) {
			final List<String> recruits = config.getStringList("guilds." + tagLower + ".recruits");
			recruits.remove(UUID);
			config.set("guilds." + tagLower + ".recruits", recruits);
			final List<String> members = config.getStringList("guilds." + tagLower + ".members");
			members.add(UUID);
			config.set("guilds." + tagLower + ".members", members);
			Guilds.instance.saveConfig();
		}
	}
	
	public static void demote(String UUID, String tag) {
		String role = getRole(UUID);
		String tagLower = tag.toLowerCase();
		if (role.equals("elder")) {
			final List<String> elders = config.getStringList("guilds." + tagLower + ".elders");
			elders.remove(UUID);
			config.set("guilds." + tagLower + ".elders", elders);
			final List<String> members = config.getStringList("guilds." + tagLower + ".members");
			members.add(UUID);
			config.set("guilds." + tagLower + ".members", members);
			Guilds.instance.saveConfig();
		}
		if (role.equals("member")) {
			final List<String> members = config.getStringList("guilds." + tagLower + ".members");
			members.remove(UUID);
			config.set("guilds." + tagLower + ".members", members);
			final List<String> recruits = config.getStringList("guilds." + tagLower + ".recruits");
			recruits.add(UUID);
			config.set("guilds." + tagLower + ".recruits", recruits);
			Guilds.instance.saveConfig();
			
		}
	}
	
	public static void setHome(Location location, String tag) {
		String tagLower = tag.toLowerCase();
		double yaw = (double) location.getYaw();
		double pitch = (double) location.getPitch();
		config.set("guilds." + tagLower + ".home.world", location.getWorld().getName());
		config.set("guilds." + tagLower + ".home.x", location.getX());
		config.set("guilds." + tagLower + ".home.y", location.getY());
		config.set("guilds." + tagLower + ".home.z", location.getZ());
		config.set("guilds." + tagLower + ".home.yaw", yaw);
		config.set("guilds." + tagLower + ".home.pitch", pitch);
		Guilds.instance.saveConfig();
	}
	
	public static void invite(String UUID, String tag) {
		String tagLower = tag.toLowerCase();
		if (!Guilds.instance.getConfig().getStringList("guilds." + tagLower + ".invited").contains(UUID)) {
			final List<String> invited = config.getStringList("guilds." + tagLower + ".invited");
			invited.add(UUID);
			Guilds.instance.getConfig().set("guilds." + tagLower + ".invited", invited);
			Guilds.instance.saveConfig();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Guilds.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					if (config.getStringList("guilds." + tag + ".invited").contains(UUID)) {
						invited.remove(UUID);
						config.set("guilds." + tagLower + ".invited", invited);
						Guilds.instance.saveConfig();
					}
				}
			}, 2 * 60 * 20);
		}
	}
	
	public static List<String> getAllies (String tag) {
		String tagLower = tag.toLowerCase();
		List<String> allies = config.getStringList("guilds." + tagLower + ".allies");
		return allies;
	}
	
	public static void ally (String tag, String rTag) {
		String tagLower = tag.toLowerCase();
		String rTagLower = rTag.toLowerCase();
		if (!config.getStringList("guilds." + rTagLower + ".requests").contains(tag)) {
			final List<String> requests = config.getStringList("guilds." + rTagLower + ".requests");
			requests.add(tag);
			config.set("guilds." + rTagLower + ".requests", requests);
			Guilds.instance.saveConfig();
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Guilds.getInstance(), new Runnable() {
				
				@Override
				public void run() {
					if (config.getStringList("guilds." + tag + ".requests").contains(tag)) {
						requests.remove(tag);
						config.set("guilds." + tagLower + ".requests", requests);
						Guilds.instance.saveConfig();
					}
				}
			}, 2 * 60 * 20);
		}
	}
	
	public static void getInfo(Player player, String tag) {
		String correctCaseTag = getGuildTag(tag.toLowerCase());
		String tagLower = tag.toLowerCase();
		String name = getGuildName(tagLower);
		int level = getLevel(tagLower);
		int playerNumber = getTotalPlayers(tagLower);
		//Leader
		String leaderUUID = getGuildLeader(tagLower);
		OfflinePlayer leader = Bukkit.getOfflinePlayer(UUID.fromString(leaderUUID));
		String leaderName = leader.getName();
		if (leader.isOnline()) {
			leaderName = (ChatColor.GREEN + "" + ChatColor.ITALIC + leaderName);
		}
		else {
			leaderName = (ChatColor.GRAY + "" + ChatColor.ITALIC + leaderName);
		}
		//Elders
		List<String> elderList = getElderList(tagLower);
		StringBuilder elders = new StringBuilder();
		for (String elderUUID : elderList) {
			OfflinePlayer offlineElder = Bukkit.getOfflinePlayer(UUID.fromString(elderUUID));
			String elderName = offlineElder.getName();
			if (offlineElder.isOnline()) {
				elderName = (ChatColor.GREEN + "" + ChatColor.ITALIC + elderName);
			}
			else {
				elderName = (ChatColor.GRAY + "" + ChatColor.ITALIC + elderName);
			}
			elders.append(elderName + ChatColor.GRAY + "" + ChatColor.ITALIC + ", ");
		}
		String eldersString = elders.toString();
		eldersString = StringUtils.chop(eldersString);
		eldersString = StringUtils.chop(eldersString);
		//Members
		List<String> memberList = getMemberList(tagLower);
		StringBuilder members = new StringBuilder();
		for (String memberUUID : memberList) {
			OfflinePlayer offlineMember = Bukkit.getOfflinePlayer(UUID.fromString(memberUUID));
			String memberName = offlineMember.getName();
			if (offlineMember.isOnline()) {
				memberName = (ChatColor.GREEN + "" + ChatColor.ITALIC + memberName);
			}
			else {
				memberName = (ChatColor.GRAY + "" + ChatColor.ITALIC + memberName);
			}
			members.append(memberName + ChatColor.GRAY + "" + ChatColor.ITALIC + ", ");
		}
		String membersString = members.toString();
		membersString = StringUtils.chop(membersString);
		membersString = StringUtils.chop(membersString);
		//Recruits
		List<String> recruitList = getRecruitList(tagLower);
		StringBuilder recruits = new StringBuilder();
		for (String recruitUUID : recruitList) {
			OfflinePlayer offlineRecruit = Bukkit.getOfflinePlayer(UUID.fromString(recruitUUID));
			String recruitName = offlineRecruit.getName();
			if (offlineRecruit.isOnline()) {
				recruitName = (ChatColor.GREEN + "" + ChatColor.ITALIC + recruitName);
			}
			else {
				recruitName = (ChatColor.GRAY + "" + ChatColor.ITALIC + recruitName);
			}
			recruits.append(recruitName + ChatColor.GRAY + "" + ChatColor.ITALIC + ", ");
		}
		String recruitsString = recruits.toString();
		recruitsString = StringUtils.chop(recruitsString);
		recruitsString = StringUtils.chop(recruitsString);
		//Allies
		List<String> allyList = getAllies(tagLower);
		StringBuilder allies = new StringBuilder();
		for (String ally : allyList) {
			allies.append(ally + ", ");
		}
		String alliesString = allies.toString();
		alliesString = StringUtils.chop(alliesString);
		alliesString = StringUtils.chop(alliesString);
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "---------------------");
		player.sendMessage(ChatColor.BLUE + name + ChatColor.AQUA + ChatColor.ITALIC + " [" + correctCaseTag + "]" + " (" + playerNumber + "/" + (level + 2) + ")");
		player.sendMessage(ChatColor.BLUE + "Level: " + ChatColor.GRAY + ChatColor.GRAY + ChatColor.ITALIC + level);
		player.sendMessage(ChatColor.BLUE + "Allies: " + ChatColor.GRAY + ChatColor.ITALIC + alliesString);
		player.sendMessage(ChatColor.BLUE + "Leader: " + leaderName);
		player.sendMessage(ChatColor.BLUE + "Elders: " + eldersString);
		player.sendMessage(ChatColor.BLUE + "Members: " + membersString);
		player.sendMessage(ChatColor.BLUE + "Recruits: " + recruitsString);
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "---------------------");
	}
	
	public static void allyRemove (String tag, String rTag) {
		String tagLower = tag.toLowerCase();
		String rTagLower = rTag.toLowerCase();
		final List<String> allies = config.getStringList("guilds." + tagLower + ".allies");
		allies.remove(rTag);
		config.set("guilds." + tagLower + ".allies", allies);
		final List<String> rAllies = config.getStringList("guilds." + rTagLower + ".allies");
		rAllies.remove(tag);
		config.set("guilds." + rTagLower + ".allies", rAllies);
		Guilds.getInstance().saveConfig();
	}
	
	public static void allyAccept (String tag, String rTag) {
		String tagLower = tag.toLowerCase();
		String rTagLower = rTag.toLowerCase();
		final List<String> requests = config.getStringList("guilds." + tagLower + ".requests");
		requests.remove(rTag);
		config.set("guilds." + tagLower + ".requests", requests);
		final List<String> rRequests = config.getStringList("guilds." + rTagLower + ".requests");
		rRequests.remove(tag);
		config.set("guilds." + rTagLower + ".requests", rRequests);
		
		final List<String> allies = config.getStringList("guilds." + tagLower + ".allies");
		allies.add(rTag);
		config.set("guilds." + tagLower + ".allies", allies);
		final List<String> rAllies = config.getStringList("guilds." + rTagLower + ".allies");
		rAllies.add(tag);
		config.set("guilds." + rTagLower + ".allies", rAllies);
		Guilds.getInstance().saveConfig();
	}
	
	public static int getLevel(String tag) {
		String tagLower = tag.toLowerCase();
		return config.getInt("guilds." + tagLower + ".level");
	}
	
	@SuppressWarnings("unused")
	public static int getTotalPlayers(String tag) {
		String tagLower = tag.toLowerCase();
		int players = 1;
		for (String player : config.getStringList("guilds." + tagLower + ".elders")) {
			players = players + 1;
		}
		for (String player : config.getStringList("guilds." + tagLower + ".members")) {
			players = players + 1;
		}
		for (String player : config.getStringList("guilds." + tagLower + ".recruits")) {
			players = players + 1;
		}
		return players;
	}
	
	public static Location getHome(String tag) {
		String tagLower = tag.toLowerCase();
		if (config.getString("guilds." + tagLower + ".home.x") != null) {
			String worldName = config.getString("guilds." + tagLower + ".home.world");
			World world = Bukkit.getServer().getWorld(worldName);
			int x = config.getInt("guilds." + tagLower + ".home.x");
			int y = config.getInt("guilds." + tagLower + ".home.y");
			int z = config.getInt("guilds." + tagLower + ".home.z");
			float yaw = (float) config.getInt("guilds." + tagLower + ".home.yaw");
			float pitch = (float) config.getInt("guilds." + tagLower + ".home.pitch");
			Location home = new Location(world, x, y, z, yaw, pitch);
			return home;
		}
		return null;
	}
	
	public static boolean isLeader(String UUID) {
		ArrayList<String> guildTags = new ArrayList<String>();
		for (String tag : config.getConfigurationSection("guilds").getKeys(false)) {
			guildTags.add(tag);
		}
		for (String key : guildTags) {
			if (UUID.equals(config.getString("guilds." + key + ".leader"))) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isMember(String UUID) {
		ArrayList<String> guildTags = new ArrayList<String>();
		for (String tag : config.getConfigurationSection("guilds").getKeys(false)) {
			guildTags.add(tag);
		}
		for (String key : guildTags) {
			for (Object memberUUID : config.getList("guilds." + key + ".members")) {
				if (memberUUID.equals(UUID)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isRecruit(String UUID) {
		ArrayList<String> guildTags = new ArrayList<String>();
		for (String tag : config.getConfigurationSection("guilds").getKeys(false)) {
			guildTags.add(tag);
		}
		for (String key : guildTags) {
			for (Object recruitUUID : config.getList("guilds." + key + ".recruits")) {
				if (recruitUUID.equals(UUID)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isElder(String UUID) {
		ArrayList<String> guildTags = new ArrayList<String>();
		for (String tag : config.getConfigurationSection("guilds").getKeys(false)) {
			guildTags.add(tag);
		}
		for (String key : guildTags) {
			for (Object elderUUID : config.getList("guilds." + key + ".elders")) {
				if (elderUUID.equals(UUID)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getAllyList(String tag) {
		List<String> allies = (List<String>) config.getList("guilds." + tag + ".allies");
		if (allies.isEmpty()) {
			return null;
		}
		else {
			return allies;
		}
	}
	
	public static String getGuildLeader(String tag) {
		String leader = config.getString("guilds." + tag + ".leader");
		return leader;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getElderList(String tag) {
		List<String> elders = (List<String>) config.getList("guilds." + tag + ".elders");
		return elders;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getMemberList(String tag) {
		List<String> members = (List<String>) config.getList("guilds." + tag + ".members");
		return members;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getRecruitList(String tag) {
		List<String> recruits = (List<String>) config.getList("guilds." + tag + ".recruits");
		return recruits;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getGuildAllies(String tag) {
		List<String> allies = (List<String>) config.getList("guilds." + tag + ".allies");
		return allies;
	}
	
	public static ArrayList<String> getGuildTagList() {
		ArrayList<String> guildTags = new ArrayList<String>();
		for (String clanTag : config.getConfigurationSection("guilds").getKeys(false)) {
			guildTags.add(clanTag);
		}
		return guildTags;
	}
}
