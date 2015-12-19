package com.hm.antiworldfly.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hm.antiworldfly.language.Lang;
import com.hm.antiworldfly.AntiWorldFly;

public class InfoCommand {

	private AntiWorldFly plugin;

	public InfoCommand(AntiWorldFly plugin) {

		this.plugin = plugin;
	}

	/**
	 * Display information about the plugin.
	 */
	public void getInfo(CommandSender sender) {

		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE + Lang.VERSION_COMMAND_NAME + " " + ChatColor.WHITE
				+ plugin.getDescription().getName());
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE + Lang.VERSION_COMMAND_VERSION + " "
				+ ChatColor.WHITE + plugin.getDescription().getVersion());
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE + Lang.VERSION_COMMAND_WEBSITE + " "
				+ ChatColor.WHITE + plugin.getDescription().getWebsite());
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE + Lang.VERSION_COMMAND_AUTHOR + " " + ChatColor.WHITE
				+ plugin.getDescription().getAuthors().get(0));
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE + Lang.VERSION_COMMAND_DESCRIPTION + " "
				+ ChatColor.WHITE + Lang.VERSION_COMMAND_DESCRIPTION_DETAILS);
		if (plugin.isDisabled())
			sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE + Lang.VERSION_COMMAND_ENABLED + " "
					+ ChatColor.WHITE + "NO");
		else
			sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE + Lang.VERSION_COMMAND_ENABLED + " "
					+ ChatColor.WHITE + "YES");
	}
}
