package com.hm.antiworldfly.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.hm.antiworldfly.AntiWorldFly;

/**
 * Class in charge of displaying the plugin's extra information (/awf info).
 * 
 * @author Pyves
 */
public class InfoCommand {

	private AntiWorldFly plugin;

	public InfoCommand(AntiWorldFly plugin) {
		this.plugin = plugin;
	}

	/**
	 * Display information about the plugin.
	 * 
	 * @param sender
	 */
	public void getInfo(CommandSender sender) {
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE
				+ plugin.getPluginLang().getString("version-command-name", "Name:") + " " + ChatColor.WHITE
				+ plugin.getDescription().getName());
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE
				+ plugin.getPluginLang().getString("version-command-version", "Version:") + " " + ChatColor.WHITE
				+ plugin.getDescription().getVersion());
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE
				+ plugin.getPluginLang().getString("version-command-website", "Website:") + " " + ChatColor.WHITE
				+ plugin.getDescription().getWebsite());
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE
				+ plugin.getPluginLang().getString("version-command-author", "Author:") + " " + ChatColor.WHITE
				+ plugin.getDescription().getAuthors().get(0));
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE
				+ plugin.getPluginLang().getString("version-command-maintainer", "Maintainer:") + " " + ChatColor.WHITE
				+ plugin.getDescription().getAuthors().get(1));
		sender.sendMessage(plugin.getChatHeader() + ChatColor.BLUE
				+ plugin.getPluginLang().getString("version-command-description", "Description:") + " "
				+ ChatColor.WHITE + plugin.getPluginLang().getString("version-command-description-details",
						"A plugin to disable flying and chosen commands when joining or playing in specific worlds."));
	}
}
