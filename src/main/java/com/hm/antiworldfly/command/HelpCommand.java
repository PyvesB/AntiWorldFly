package com.hm.antiworldfly.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.antiworldfly.particle.PacketSender;

/**
 * Class in charge of displaying the plugin's help (/awf help).
 * 
 * @author Pyves
 */
public class HelpCommand {

	private AntiWorldFly plugin;

	public HelpCommand(AntiWorldFly plugin) {

		this.plugin = plugin;
	}

	public void getHelp(CommandSender sender) {

		// Header.
		sender.sendMessage(ChatColor.BLUE + "------------------ " + ChatColor.WHITE + ChatColor.BLUE + plugin.getIcon()
				+ ChatColor.translateAlternateColorCodes('&', " &9AntiWorldFly ") + ChatColor.BLUE + plugin.getIcon()
				+ ChatColor.WHITE + ChatColor.BLUE + " ------------------");

		sendJsonClickableHoverableMessage(sender,
				plugin.getChatHeader() + ChatColor.BLUE + "/awf list" + ChatColor.WHITE + " > "
						+ plugin.getPluginLang().getString("awf-command-list", "List worlds in which AWF operates."),
				"/awf list", plugin.getPluginLang().getString("awf-command-list-hover",
						"Flying and some specific commands are disabled in these worlds."));

		sendJsonClickableHoverableMessage(sender,
				plugin.getChatHeader() + ChatColor.BLUE + "/awf info" + ChatColor.WHITE + " > "
						+ plugin.getPluginLang().getString("awf-command-info",
								"Display various information about the plugin."),
				"/awf info", plugin.getPluginLang().getString("awf-command-info-hover",
						"Some extra info about the plugin and its awesome author!"));

		if (sender.hasPermission("antiworldfly.use"))
			sendJsonClickableHoverableMessage(sender,
					plugin.getChatHeader() + ChatColor.BLUE + "/awf reload" + ChatColor.WHITE + " > "
							+ plugin.getPluginLang().getString("awf-command-reload",
									"Reload the plugin's configuration."),
					"/awf reload", plugin.getPluginLang().getString("awf-command-reload-hover",
							"Reload most settings in config.yml and lang.yml files."));

		if (sender.hasPermission("antiworldfly.use"))
			sendJsonClickableHoverableMessage(sender,
					plugin.getChatHeader() + ChatColor.BLUE + "/awf enable" + ChatColor.WHITE + " > "
							+ plugin.getPluginLang().getString("awf-command-enable", "Enable plugin."),
					"/awf enable", plugin.getPluginLang().getString("awf-command-enable-hover",
							"Plugin enabled by default. Use this if you entered /awf disable before!"));

		if (sender.hasPermission("antiworldfly.use"))
			sendJsonClickableHoverableMessage(sender,
					plugin.getChatHeader() + ChatColor.BLUE + "/awf disable" + ChatColor.WHITE + " > "
							+ plugin.getPluginLang().getString("awf-command-disable", "Disable plugin."),
					"/awf disable", plugin.getPluginLang().getString("awf-command-disable-hover",
							"The plugin will not work until next reload or /awf enable."));

		if (sender.hasPermission("antiworldfly.use"))
			sendJsonClickableHoverableMessage(sender,
					plugin.getChatHeader() + ChatColor.BLUE + "/awf add &oworld&r" + ChatColor.WHITE + " > "
							+ plugin.getPluginLang().getString("awf-command-add", "Add WORLD to blocked worlds.")
									.replace("WORLD", "&oworld&r"),
					"/awf add world", plugin.getPluginLang().getString("awf-command-add-hover",
							"Make sure you specify the correct name!"));

		if (sender.hasPermission("antiworldfly.use"))
			sendJsonClickableHoverableMessage(sender, plugin.getChatHeader() + ChatColor.BLUE + "/awf remove &oworld&r"
					+ ChatColor.WHITE + " > "
					+ plugin.getPluginLang().getString("awf-command-remove", "Remove WORLD from blocked worlds.")
							.replace("WORLD", "&oworld&r"),
					"/awf remove world",
					plugin.getPluginLang().getString("awf-command-remove-hover", "World must be listed in /awf list."));

		// Empty line.
		sender.sendMessage(ChatColor.BLUE + " ");

		// Tip message.
		sender.sendMessage(ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', plugin.getPluginLang()
				.getString("awf-tip", "&lHINT&r &8You can &7&n&ohover&r &8or &7&n&oclick&r &8on the commands!")));
	}

	/**
	 * Send a packet message to the server in order to display a clickable and hoverable message. A suggested command is
	 * displayed in the chat when clicked on, and an additional help message appears when a command is hovered.
	 * 
	 * @param sender
	 * @param message
	 * @param command
	 * @param hover
	 */
	public void sendJsonClickableHoverableMessage(CommandSender sender, String message, String command, String hover) {

		// Build the json format string.
		String json = "{\"text\":\"" + message + "\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\""
				+ command + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[{\"text\":\"" + hover
				+ "\",\"color\":\"blue\"}]}}";

		// Send clickable and hoverable message if sender is a player and if no exception is caught.
		if (sender instanceof Player) {
			try {
				PacketSender.sendChatPacket((Player) sender, json, PacketSender.CHAT_MESSAGE_BYTE);
			} catch (Exception ex) {
				plugin.getLogger().severe(
						"Errors while trying to display clickable and hoverable message in /awf help command. Displaying standard message instead.");
				sender.sendMessage(message);
			}
		} else {
			sender.sendMessage(message);
		}
	}
}
