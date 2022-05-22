package me.vaape.commands;

import java.util.ArrayList;

import me.vaape.events.Events;
import me.vaape.events.GuildWars;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.vaape.events.Fishing;
import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class ChatCommand implements CommandExecutor, Listener{
	
	Guilds plugin;
	
	public ChatCommand (Guilds passedPlugin) {
		this.plugin = passedPlugin;
	}
	
	public static ArrayList<String> guildChat = new ArrayList<>();
	public static ArrayList<String> allyChat = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			String UUID = player.getUniqueId().toString();
			if (GuildManager.getPlayerGuildTag(UUID) != null) {
				String tag = GuildManager.getPlayerGuildTag(UUID);
				if (label.equalsIgnoreCase("gc")) {
					if (args.length > 0) {
						StringBuilder messageBuilder = new StringBuilder();
						for (int i = 0; i < args.length; i++) {
							messageBuilder.append(args[i] + " ");
						}
						String message = messageBuilder.toString();
						message = StringUtils.chop(message); //Remove extra space from the end
						GuildManager.sendGuildMessage(player, tag, message);
					}
					else {
						if (guildChat.contains(UUID)) {
							guildChat.remove(UUID);
							player.sendMessage(ChatColor.BLUE + "Locked in " + ChatColor.BOLD + "public " + ChatColor.BLUE + "chat.");
						}
						else if (allyChat.contains(UUID)) {
							allyChat.remove(UUID);
							guildChat.add(UUID);
							player.sendMessage(ChatColor.BLUE + "Locked in " + ChatColor.GREEN + ChatColor.BOLD + "guild " + ChatColor.BLUE + "chat.");
						}
						else {
							guildChat.add(UUID);
							player.sendMessage(ChatColor.BLUE + "Locked in " + ChatColor.GREEN + ChatColor.BOLD + "guild " + ChatColor.BLUE + "chat.");
						}
					}
				}
				else if  (label.equalsIgnoreCase("ac")) {
					if (args.length > 0) {
						StringBuilder messageBuilder = new StringBuilder();
						for (int i = 0; i < args.length; i++) {
							messageBuilder.append(args[i] + " ");
						}
						String message = messageBuilder.toString();
						message = StringUtils.chop(message); //Remove extra space from the end
						GuildManager.sendAllyMessage(player, tag, message);
					}
					else {
						if (allyChat.contains(UUID)) {
							allyChat.remove(UUID);
							player.sendMessage(ChatColor.BLUE + "Locked in " + ChatColor.BOLD + "public " + ChatColor.BLUE + "chat.");
						}
						else if (guildChat.contains(UUID)) {
							guildChat.remove(UUID);
							allyChat.add(UUID);
							player.sendMessage(ChatColor.BLUE + "Locked in " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "ally " + ChatColor.BLUE + "chat.");
						}
						else {
							allyChat.add(UUID);
							player.sendMessage(ChatColor.BLUE + "Locked in " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "ally " + ChatColor.BLUE + "chat.");
						}
					}
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You are not in a guild.");
			}
		}
		return false;
	}
	
	@EventHandler
	public void onTalk (AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		String UUID = player.getUniqueId().toString();
		String message = event.getMessage();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			if (guildChat.contains(UUID)) {
				event.setCancelled(true);
				GuildManager.sendGuildMessage(player, tag, message);
			}
			else if (allyChat.contains(UUID)) {
				event.setCancelled(true);
				GuildManager.sendAllyMessage(player, tag, message);
			}
			else {
				String tagPrefix = "";
				if (Events.getInstance().getConfig().getString("defenders").equals(tag)) {
					tagPrefix = ChatColor.GOLD + "[" + tag + "] ";
				}
				else {
					tagPrefix = ChatColor.of("#9782b5") + "[" + tag + "] ";
				}
				if (Fishing.isWinner(player) || player.getName().equals("GrayFallacy")) {
					String symbol = "\u2742";
					event.setFormat(tagPrefix + ChatColor.YELLOW + symbol + " %s" + ChatColor.WHITE + " %s");
				}
				else {
					event.setFormat(tagPrefix + "%s" + ChatColor.WHITE + " %s");
				}
			}
		}
		else {
			if (guildChat.contains(UUID)) {
				guildChat.remove(UUID);
			}
			if (allyChat.contains(UUID)) {
				allyChat.remove(UUID);
			}
			if (Fishing.isWinner(player)) {
				String symbol = "\u2742";
				event.setFormat(ChatColor.YELLOW + symbol + " %s" + ChatColor.WHITE + " %s"); //TODO SYMBOL
			}
			else {
				event.setFormat("%s" + ChatColor.WHITE + " %s");
			}
		}
	}	
}
