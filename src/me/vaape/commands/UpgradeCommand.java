package me.vaape.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.vaape.guilds.GuildManager;
import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class UpgradeCommand {
	
	Guilds plugin;
	
	public UpgradeCommand(Guilds plugin) {
		this.plugin = plugin;
	}//done
	
	public void executeUpgradeCommand (Player player) {
		String UUID = player.getUniqueId().toString();
		ItemStack hand = player.getInventory().getItemInMainHand();
		if (GuildManager.getPlayerGuildTag(UUID) != null) {
			String tag = GuildManager.getPlayerGuildTag(UUID);
			String tagLower = tag.toLowerCase();
			String name = GuildManager.getGuildName(tagLower);
			int level = GuildManager.getLevel(tagLower);
			if (level < 5) {
				if (hand != null && hand.getType() != Material.AIR) {
					if (hand.getType() == Material.DRAGON_EGG && hand.getAmount() > level) {
						hand.setAmount(hand.getAmount() - (level + 1));
						player.getInventory().setItemInMainHand(hand);
						GuildManager.levelUp(tagLower);
						Bukkit.getServer().broadcastMessage(ChatColor.BLUE + "[Guilds] " + ChatColor.YELLOW + name + " has upgraded their guild to level "+ (level + 1) + ".");
						List<Player> onlinePlayers = new ArrayList<Player>();
						for (Player p : Bukkit.getServer().getOnlinePlayers()) {
							onlinePlayers.add(p);
						}
						for (Player guildPlayer : onlinePlayers) {
							if (Bukkit.getServer().getOnlinePlayers().contains(guildPlayer)) {
								if (GuildManager.getGuildPlayers(tagLower).contains(guildPlayer.getUniqueId().toString())) {
									guildPlayer.playSound(guildPlayer.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1f, 1f);
								}
							}
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "You need " + (level + 1) + " dragon eggs to upgrade your guild.");
					}
				}
			}
			else {
				player.sendMessage(ChatColor.RED + "You can not upgrade your guild past level 5.");
			}
		}
		else {
			player.sendMessage(ChatColor.RED + "You are not in a guild.");
		}
	}

}
