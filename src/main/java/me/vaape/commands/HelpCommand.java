package me.vaape.commands;

import org.bukkit.entity.Player;

import me.vaape.guilds.Guilds;
import net.md_5.bungee.api.ChatColor;

public class HelpCommand {

	Guilds plugin;
	
	public HelpCommand(Guilds plugin) {
		this.plugin = plugin;
	}
	
	public void executeHelpCommand (Player player, String[] args) {
		if (args.length == 1) {
			player.sendMessage(
					ChatColor.BLUE +  "" + ChatColor.BOLD + "Guild commands page 1 of 2:\n"
					+ ChatColor.BLUE + "/g info [guild tag/player] " + ChatColor.GRAY + ChatColor.ITALIC + "- Gives info on your guild\n"
					+ ChatColor.BLUE + "/g list [page] " + ChatColor.GRAY + ChatColor.ITALIC + "- Gives a list of registered guilds\n"
					+ ChatColor.BLUE + "/gc " + ChatColor.GRAY + ChatColor.ITALIC + "- Talk in guild chat\n"
					+ ChatColor.BLUE + "/ac " + ChatColor.GRAY + ChatColor.ITALIC + "- Talk in alliance chat\n"
					+ ChatColor.BLUE + "/g create [guild tag] " + ChatColor.GRAY + ChatColor.ITALIC + "- Creates a new guild\n"
					+ ChatColor.BLUE + "/g leave " + ChatColor.GRAY + ChatColor.ITALIC + "- Leaves your current guild\n"
					+ ChatColor.BLUE + "/g accept [guild tag] " + ChatColor.GRAY + ChatColor.ITALIC + "- Accept a guild invitation\n"
					+ ChatColor.BLUE + "/g MOTD " + ChatColor.GRAY + ChatColor.ITALIC + "- Read guild message of the day\n"
					+ ChatColor.BLUE + "/g upgrade " + ChatColor.GRAY + ChatColor.ITALIC + "- Upgrades your guild to the next level\n"
					+ ChatColor.BLUE + "/g home " + ChatColor.GRAY + ChatColor.ITALIC + "- Teleports you to your guild home\n"
					+ ChatColor.BLUE + "/g chest " + ChatColor.GRAY + ChatColor.ITALIC + "- Opens guild's private chest\n"
					+ ChatColor.BLUE + ChatColor.BOLD + "/g help 2 for page 2..."
					);
		}
		else if (args[1].equals("1")) {
			player.sendMessage(
					ChatColor.BLUE +  "" + ChatColor.BOLD + "Guild commands page 1 of 2:\n"
					+ ChatColor.BLUE + "/g info [guild tag/player]" + ChatColor.GRAY + ChatColor.ITALIC + "- Gives info on your guild\n"
					+ ChatColor.BLUE + "/g list [page]" + ChatColor.GRAY + ChatColor.ITALIC + "- Gives a list of registered guilds\n"
					+ ChatColor.BLUE + "/gc " + ChatColor.GRAY + ChatColor.ITALIC + "- Talk in guild chat\n"
					+ ChatColor.BLUE + "/ac " + ChatColor.GRAY + ChatColor.ITALIC + "- Talk in alliance chat\n"
					+ ChatColor.BLUE + "/g create [guild tag] " + ChatColor.GRAY + ChatColor.ITALIC + "- Creates a new guild\n"
					+ ChatColor.BLUE + "/g leave " + ChatColor.GRAY + ChatColor.ITALIC + "- Leaves your current guild\n"
					+ ChatColor.BLUE + "/g accept [guild tag] " + ChatColor.GRAY + ChatColor.ITALIC + "- Accept a guild invitation\n"
					+ ChatColor.BLUE + "/g MOTD help" + ChatColor.GRAY + ChatColor.ITALIC + "- Read guild message of the day\n"
					+ ChatColor.BLUE + "/g upgrade " + ChatColor.GRAY + ChatColor.ITALIC + "- Upgrades your guild to the next level\n"
					+ ChatColor.BLUE + "/g home " + ChatColor.GRAY + ChatColor.ITALIC + "- Teleports you to your guild home\n"
					+ ChatColor.BLUE + "/g chest " + ChatColor.GRAY + ChatColor.ITALIC + "- Opens guild's private chest\n"
					+ ChatColor.BLUE + ChatColor.BOLD + "/g help 2 for page 2..."
					);
		}
		else if (args[1].equals("2")) {
			player.sendMessage(
					ChatColor.BLUE +  "" + ChatColor.BOLD + "Guild commands page 2 of 2:\n"
					+ ChatColor.BLUE + "/g invite [name] " + ChatColor.GRAY + ChatColor.ITALIC + "- Invites a new recruit to your guild\n"
					+ ChatColor.BLUE + "/g promote [member] " + ChatColor.GRAY + ChatColor.ITALIC + "- Promotes a guild member\n"
					+ ChatColor.BLUE + "/g demote [member] " + ChatColor.GRAY + ChatColor.ITALIC + "- Demotes a guild member\n"
					+ ChatColor.BLUE + "/g sethome " + ChatColor.GRAY + ChatColor.ITALIC + "- Sets a new guild home\n"
					+ ChatColor.BLUE + "/g kick [member] " + ChatColor.GRAY + ChatColor.ITALIC + "- Kicks a member from your guild\n"
					+ ChatColor.BLUE + "/g ally " + ChatColor.GRAY + ChatColor.ITALIC + "- Manage guild alliances\n"

					+ ChatColor.BLUE + "/g leader [member]" + ChatColor.GRAY + ChatColor.ITALIC + "- Passes guild leadership\n"
					+ ChatColor.BLUE + "/g disband " + ChatColor.GRAY + ChatColor.ITALIC + "- Disbands your current guild\n"
					);
		}
		else {
			player.sendMessage(ChatColor.RED + "Wrong usage. Try /g help [1 or 2].");
		}
	}
}