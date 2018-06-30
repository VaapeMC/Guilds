package me.vaape.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.vaape.guilds.Guilds;

public class GuildCommand implements CommandExecutor{

	Guilds plugin;
	CreateCommand createCommand;
	LeaveCommand leaveCommand;
	UpgradeCommand upgradeCommand;
	InviteCommand inviteCommand;
	InviteAcceptCommand inviteAcceptCommand;
	KickCommand kickCommand;
	PromoteCommand promoteCommand;
	DemoteCommand demoteCommand;
	SethomeCommand sethomeCommand;
	HomeCommand homeCommand;
	DisbandCommand disbandCommand;
	LeaderCommand leaderCommand;
	AllyCommand allyCommand;
	InfoCommand infoCommand;
	MotdCommand motdCommand;
	ChestCommand chestCommand;
	ListCommand listCommand;
	HelpCommand helpCommand;
	
	public GuildCommand (Guilds passedPlugin) {
		createCommand = new CreateCommand(passedPlugin);
		leaveCommand = new LeaveCommand(passedPlugin);
		upgradeCommand = new UpgradeCommand(passedPlugin);
		inviteCommand = new InviteCommand(passedPlugin);
		inviteAcceptCommand = new InviteAcceptCommand(passedPlugin);
		kickCommand = new KickCommand(passedPlugin);
		promoteCommand = new PromoteCommand(passedPlugin);
		demoteCommand = new DemoteCommand(passedPlugin);
		sethomeCommand = new SethomeCommand(passedPlugin);
		homeCommand = new HomeCommand(passedPlugin);
		disbandCommand = new DisbandCommand(passedPlugin);
		leaderCommand = new LeaderCommand(passedPlugin);
		allyCommand = new AllyCommand(passedPlugin);
		infoCommand = new InfoCommand(passedPlugin);
		motdCommand = new MotdCommand(passedPlugin);
		chestCommand = new ChestCommand(passedPlugin);
		listCommand = new ListCommand(passedPlugin);
		helpCommand = new HelpCommand(passedPlugin);
		this.plugin = passedPlugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;	
		
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("create")) {
				createCommand.executeCreateCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("leave")) {
				leaveCommand.executeLeaveCommand(player);
			}
			else if (args[0].equalsIgnoreCase("upgrade")) {
				upgradeCommand.executeUpgradeCommand(player);
			}
			else if (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("invite")) {
				inviteCommand.executeInviteCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("accept")) {
				inviteAcceptCommand.executeInviteAcceptCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("kick") || args[0].equalsIgnoreCase("remove")) {
				kickCommand.executeKickCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("promote")) {
				promoteCommand.executePromoteCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("demote")) {
				demoteCommand.executeDemoteCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("sethome") || args[0].equalsIgnoreCase("sh")) {
				sethomeCommand.executeSethomeCommand(player);
			}
			else if (args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("h")) {
				homeCommand.executeHomeCommand(player);
			}
			else if (args[0].equalsIgnoreCase("disband")) {
				disbandCommand.executeDisbandCommand(player);
			}
			else if (args[0].equalsIgnoreCase("leader") || args[0].equalsIgnoreCase("pass")) {
				leaderCommand.executeLeaderCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("ally") || args[0].equalsIgnoreCase("alliance")) {
				allyCommand.executeAllyCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("info")) {
				infoCommand.executeInfoCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("motd")) {
				motdCommand.executeMotdCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("chest")) {
				chestCommand.executeChestCommand(player);
			}
			else if (args[0].equalsIgnoreCase("list")) {
				listCommand.executeListCommand(player, args);
			}
			else if (args[0].equalsIgnoreCase("help")) {
				helpCommand.executeHelpCommand(player, args);
			}
			else {
				player.sendMessage(ChatColor.RED + "Unrecognised command, try /g help for a list of guild commands.");
			}
		}
		
		else {
			player.sendMessage(
					ChatColor.BLUE +  "" + ChatColor.BOLD + "Guild commands page 1 of 2:\n"
					+ ChatColor.BLUE + "/g info [guild tag/player]" + ChatColor.GRAY + ChatColor.ITALIC + "- Gives info on your guild\n"
					+ ChatColor.BLUE + "/g list [page]" + ChatColor.GRAY + ChatColor.ITALIC + "- Gives a list of registered guilds\n"
					+ ChatColor.BLUE + "/gc " + ChatColor.GRAY + ChatColor.ITALIC + "- Talk in guild chat\n"
					+ ChatColor.BLUE + "/ac " + ChatColor.GRAY + ChatColor.ITALIC + "- Talk in alliance chat\n"
					+ ChatColor.BLUE + "/g create [guild name] " + ChatColor.GRAY + ChatColor.ITALIC + "- Creates a new guild\n"
					+ ChatColor.BLUE + "/g leave " + ChatColor.GRAY + ChatColor.ITALIC + "- Leaves your current guild\n"
					+ ChatColor.BLUE + "/g accept [guild name] " + ChatColor.GRAY + ChatColor.ITALIC + "- Accept a guild invitation\n"
					+ ChatColor.BLUE + "/g MOTD " + ChatColor.GRAY + ChatColor.ITALIC + "- Read guild message of the day\n"
					+ ChatColor.BLUE + "/g upgrade " + ChatColor.GRAY + ChatColor.ITALIC + "- Upgrades your guild to the next level\n"
					+ ChatColor.BLUE + "/g home " + ChatColor.GRAY + ChatColor.ITALIC + "- Teleports you to your guild home\n"
					+ ChatColor.BLUE + "/g chest " + ChatColor.GRAY + ChatColor.ITALIC + "- Opens guild's private chest\n"
					+ ChatColor.BLUE + ChatColor.BOLD + "/g help 2 for page 2..."
					);
			return true;
	    }
		
		return false;
	}

}
