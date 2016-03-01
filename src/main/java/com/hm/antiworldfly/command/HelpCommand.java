package com.hm.antiworldfly.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hm.antiworldfly.language.Lang;
import com.hm.antiworldfly.particle.PacketSender;
import com.hm.antiworldfly.AntiWorldFly;

public class HelpCommand {

	private AntiWorldFly plugin;

	public HelpCommand(AntiWorldFly plugin) {

		this.plugin = plugin;
	}

	public void getHelp(CommandSender sender) {

		sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE).append("-=-=-=-=-=-=-=-=-")
				.append(ChatColor.GRAY).append("[").append(ChatColor.BLUE).append("\u06DE").append("§lAntiWorldFly")
				.append(ChatColor.BLUE).append("\u06DE").append(ChatColor.GRAY).append("]").append(ChatColor.BLUE)
				.append("-=-=-=-=-=-=-=-=-").toString());

		sendJsonClickableMessage(sender,
				(new StringBuilder()).append(plugin.getChatHeader()).append(ChatColor.DARK_AQUA + "/awf disable")
						.append(ChatColor.WHITE).append(" - " + Lang.AWF_COMMAND_DISABLE).toString(), "/awf disable");

		sendJsonClickableMessage(sender,
				(new StringBuilder()).append(plugin.getChatHeader()).append(ChatColor.DARK_AQUA + "/awf enable")
						.append(ChatColor.WHITE).append(" - " + Lang.AWF_COMMAND_ENABLE).toString(), "/awf enable");

		sendJsonClickableMessage(
				sender,
				(new StringBuilder()).append(plugin.getChatHeader()).append(ChatColor.DARK_AQUA + "/awf list")
						.append(ChatColor.WHITE).append(" - " + Lang.AWF_COMMAND_LIST).toString(), "/awf list");

		sendJsonClickableMessage(
				sender,
				(new StringBuilder())
						.append(plugin.getChatHeader())
						.append(ChatColor.DARK_AQUA + "/awf add §oworld§r")
						.append(ChatColor.WHITE)
						.append(" - "
								+ ChatColor.translateAlternateColorCodes('&',
										Lang.AWF_COMMAND_ADD.toString().replace("WORLD", "§oworld§r"))).toString(),
				"/awf add world");

		sendJsonClickableMessage(
				sender,
				(new StringBuilder())
						.append(plugin.getChatHeader())
						.append(ChatColor.DARK_AQUA + "/awf remove §oworld§r")
						.append(ChatColor.WHITE)
						.append(" - "
								+ ChatColor.translateAlternateColorCodes('&', Lang.AWF_COMMAND_REMOVE.toString()
										.replace("WORLD", "§oworld§r"))).toString(), "/awf remove world");

		sendJsonClickableMessage(sender,
				(new StringBuilder()).append(plugin.getChatHeader()).append(ChatColor.DARK_AQUA + "/awf reload")
						.append(ChatColor.WHITE).append(" - " + Lang.AWF_COMMAND_RELOAD).toString(), "/awf reload");

		sendJsonClickableMessage(
				sender,
				(new StringBuilder()).append(plugin.getChatHeader()).append(ChatColor.DARK_AQUA + "/awf info")
						.append(ChatColor.WHITE).append(" - " + Lang.AWF_COMMAND_INFO).toString(), "/awf info");

		sender.sendMessage((new StringBuilder()).append(ChatColor.BLUE)
				.append("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-").toString());
	}

	/**
	 * Send a packet message to the server in order to display a clickable
	 * message. A suggestion command is then displayed in the chat. Parts of
	 * this method were extracted from ELCHILEN0's AutoMessage plugin, under MIT
	 * license (http://dev.bukkit.org/bukkit-plugins/automessage/). Thanks for
	 * his help on this matter.
	 */
	public void sendJsonClickableMessage(CommandSender sender, String message, String command) {

		// Build the json format string.
		String json = "{\"text\":\"" + message + "\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + command + "\"}}";
		if (sender instanceof Player)
			try {
				PacketSender.sendChatPacket((Player) sender, json);
			} catch (Exception ex) {

				plugin.getLogger()
						.severe("Errors while trying to display clickable in /awf help command. Displaying standard message instead.");
				sender.sendMessage(message);
			}
		else
			sender.sendMessage(message);
	}
}
