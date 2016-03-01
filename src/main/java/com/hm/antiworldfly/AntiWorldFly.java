package com.hm.antiworldfly;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.hm.antiworldfly.command.HelpCommand;
import com.hm.antiworldfly.command.InfoCommand;
import com.hm.antiworldfly.language.Lang;
import com.hm.antiworldfly.listener.AntiWorldFlyPlayerJoin;
import com.hm.antiworldfly.listener.AntiWorldFlyPreProcess;
import com.hm.antiworldfly.listener.AntiWorldFlyWorldJoin;
import com.hm.antiworldfly.metrics.MetricsLite;

/**
 * A plugin to disable flying and chosen commands when joining or playing in specific worlds.
 * 
 * AntiWorldFly is under GNU General Public License version 3.
 * 
 * Please visit the plugin's GitHub for more information :
 * https://github.com/PyvesB/AntiWorldFly
 * 
 * Official plugin's server: hellominecraft.fr
 * 
 * Bukkit project page: dev.bukkit.org/bukkit-plugins/anti-world-fly
 * Spigot project page: spigotmc.org/resources/anti-world-fly.5357
 * 
 * @since March 2015.
 * @version 2.0.2
 * @author DarkPyves
 */

public class AntiWorldFly extends JavaPlugin implements Listener {

	// Plugin options and various parameters.
	private List<String> antiFlyWorlds;
	private boolean disabled;
	private boolean chatMessage;
	private boolean antiFlyCreative;
	private String chatHeader;
	private boolean titleMessage;

	// Plugin listeners.
	private AntiWorldFlyPreProcess awfPreProcess;
	private AntiWorldFlyWorldJoin awfWorldJoin;
	private AntiWorldFlyPlayerJoin awfPlayerJoin;

	// Additional classes related to plugin commands.
	private HelpCommand helpCommand;
	private InfoCommand infoCommand;
	private List<String> otherBlockedCommands;

	/**
	 * Constructor.
	 */
	public AntiWorldFly() {

		disabled = false;
		antiFlyWorlds = new ArrayList<String>();
	}

	/**
	 * Called when server is launched or reloaded.
	 */
	@Override
	public void onEnable() {

		loadLang();
		this.saveDefaultConfig();

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
			this.getLogger().severe("Error while sending Metrics statistics.");
		}

		awfPreProcess = new AntiWorldFlyPreProcess(this);
		awfWorldJoin = new AntiWorldFlyWorldJoin(this);
		awfPlayerJoin = new AntiWorldFlyPlayerJoin(this);

		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(awfPreProcess, this);
		pm.registerEvents(awfWorldJoin, this);
		pm.registerEvents(awfPlayerJoin, this);

		extractParametersFromConfig();

		chatHeader = ChatColor.GRAY + "[" + ChatColor.BLUE + "\u06DE" + ChatColor.GRAY + "] " + ChatColor.WHITE;

		helpCommand = new HelpCommand(this);
		infoCommand = new InfoCommand(this);

		this.getLogger().info("AntiWorldFly v" + this.getDescription().getVersion() + " has been enabled.");

	}

	/**
	 * Extract plugin parameters from the configuration file.
	 */
	private void extractParametersFromConfig() {

		antiFlyWorlds = this.getConfig().getStringList("antiFlyWorlds");
		chatMessage = this.getConfig().getBoolean("chatMessage", true);
		titleMessage = this.getConfig().getBoolean("titleMessage", true);
		antiFlyCreative = this.getConfig().getBoolean("antiFlyCreative", true);
		otherBlockedCommands = this.getConfig().getStringList("otherBlockedCommands");
	}

	/**
	 * Load the lang.yml file.
	 */
	public void loadLang() {

		File lang = new File(getDataFolder(), "lang.yml");
		if (!lang.exists()) {
			try {
				getDataFolder().mkdir();
				lang.createNewFile();
				Reader defConfigStream = new InputStreamReader(this.getResource("lang.yml"), "UTF8");
				if (defConfigStream != null) {
					YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
					defConfig.save(lang);
					Lang.setFile(defConfig);
					return;
				}
			} catch (IOException e) {

				this.getLogger().severe("Error while creating language file.");
				e.printStackTrace();
			}
		}
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
		for (Lang item : Lang.values()) {
			if (conf.getString(item.getPath()) == null) {
				conf.set(item.getPath(), item.getDefault());
			}
		}
		Lang.setFile(conf);
		try {
			conf.save(lang);
		} catch (IOException e) {

			this.getLogger().severe("Error while saving language file.");
			e.printStackTrace();
		}
	}

	/**
	 * Called when server is stopped or reloaded.
	 */
	@Override
	public void onDisable() {

		this.getLogger().info("AntiWorldFly has been disabled.");
	}

	/**
	 * Called when a player or the console enters a command.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

		if (!cmd.getName().equalsIgnoreCase("awf"))
			return false;

		if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("help")) {

			helpCommand.getHelp(sender);

		} else if (args[0].equalsIgnoreCase("list")) {

			sender.sendMessage(chatHeader + Lang.WORLDS_BLOCKED);
			for (String world : antiFlyWorlds)
				sender.sendMessage(" - " + world);

		} else if (args[0].equalsIgnoreCase("info")) {

			infoCommand.getInfo(sender);

		} else if (args[0].equalsIgnoreCase("reload")) {
			if (sender.hasPermission("antiworldfly.use")) {
				try {

					loadLang();
					this.reloadConfig();
					extractParametersFromConfig();
					sender.sendMessage(chatHeader + Lang.CONFIGURATION_SUCCESSFULLY_RELOADED);
				} catch (Exception ex) {
					sender.sendMessage(chatHeader + Lang.CONFIGURATION_RELOAD_FAILED);
					ex.printStackTrace();
				}
			} else
				sender.sendMessage(chatHeader + Lang.NO_PERMS);
		}

		else if (sender.hasPermission("antiworldfly.use")) {

			String action = args[0].toLowerCase();

			if (action.equals("disable")) {

				disabled = true;
				sender.sendMessage(chatHeader + Lang.AWF_DISABLED);

			} else if (action.equals("enable")) {

				disabled = false;
				sender.sendMessage(chatHeader + Lang.AWF_ENABLED);

			} else if (action.equals("add") && args.length == 2) {

				antiFlyWorlds.add(args[1]);
				this.getConfig().set("antiFlyWorlds", antiFlyWorlds);
				this.saveConfig();
				sender.sendMessage(chatHeader + Lang.WORLD_ADDED + args[1]);

			} else if (action.equals("remove") && args.length == 2) {

				for (int i = 0; i < antiFlyWorlds.size(); i++) {
					if (antiFlyWorlds.get(i).equals(args[1]))
						antiFlyWorlds.remove(i);
				}
				this.getConfig().set("antiFlyWorlds", antiFlyWorlds);
				this.saveConfig();
				sender.sendMessage(chatHeader + Lang.WORLD_REMOVED + args[1]);

			} else
				sender.sendMessage(chatHeader + Lang.MISUSED_COMMAND);

		} else
			sender.sendMessage(chatHeader + Lang.NO_PERMS);

		return true;
	}

	/**
	 * Various getters and setters.
	 */

	public boolean isDisabled() {

		return disabled;
	}

	public List<String> getAntiFlyWorlds() {

		return antiFlyWorlds;
	}

	public boolean isChatMessage() {

		return chatMessage;
	}

	public boolean isTitleMessage() {

		return titleMessage;
	}

	public boolean isAntiFlyCreative() {

		return antiFlyCreative;
	}

	public String getChatHeader() {

		return chatHeader;
	}

	public List<String> getOtherBlockedCommands() {

		return otherBlockedCommands;
	}

}
