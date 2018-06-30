package me.vaape.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class CreateCommand {

	Guilds plugin;
	
	public CreateCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	//TODO check if tag/name is taken
	
	public void executeCreateCommand(Player player, String[] args) { ///clan create tag name
		String UUID = player.getUniqueId().toString();
		if (GuildManager.getPlayerGuildTag(UUID) == null) {
			if (!(args.length < 3)) {
				String tag = args[1];
				String tagLower = tag.toLowerCase();
				ItemStack hand = player.getInventory().getItemInMainHand();
				StringBuilder nameBuilder = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					nameBuilder.append(args[i] + " ");
				}
				String name = nameBuilder.toString();
				name = StringUtils.chop(name); //Remove extra space from the end
				if (GuildManager.getGuildTag(tagLower) == null) {
					if (!GuildManager.getNameList().contains(name.toLowerCase())) {
						if (tag.length() < 6) {
							Pattern pattern = Pattern.compile("[a-zA-Z0-9 ]*");
							Matcher tagMatcher = pattern.matcher(tag);
							if (tagMatcher.matches()) {
								if (name.length() < 21) {
									Matcher nameMatcher = pattern.matcher(name);
									if (nameMatcher.matches()) {
										if (hand != null && hand.getType() != Material.AIR) {
											if (hand.getType() == Material.DRAGON_EGG) {
												hand.setAmount(hand.getAmount() - 1);
												player.getInventory().setItemInMainHand(hand);
												GuildManager.createClan(UUID, tag, name);
												Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.GREEN + player.getName() + " has created the guild " + name + ".");
												player.playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
											}
											else {
												player.sendMessage(ChatColor.RED + "You need a dragon egg to create a guild.");
											}
										}
										else {
											player.sendMessage(ChatColor.RED + "You need a dragon egg to create a guild.");
										}
									}
									else {
										player.sendMessage(ChatColor.RED + "Guilds name can not contain special charcters.");
									}
								}
								else {
									player.sendMessage(ChatColor.RED + "Guild name can not be longer than 20 characters.");
								}
							}
							else {
								player.sendMessage(ChatColor.RED + "Guild tag can not contain special charcters.");
							}
						}
						else {
							player.sendMessage(ChatColor.RED + "Guild tag can not be longer than 5 characters.");
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "The guild name " + name + " is taken.");
					}
				}
				else {
					player.sendMessage(ChatColor.RED + "The guild tag " + GuildManager.getGuildTag(tagLower) + " is taken.");
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "Wrong usage, try /g create [guild tag] [guild name]");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are already in a guild.");
		}
	}
}
