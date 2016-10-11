package com.hm.antiworldfly;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.MetricsLite;

import com.hm.antiworldfly.command.HelpCommand;
import com.hm.antiworldfly.command.InfoCommand;
import com.hm.antiworldfly.listener.AntiWorldFlyPlayerJoin;
import com.hm.antiworldfly.listener.AntiWorldFlyPreProcess;
import com.hm.antiworldfly.listener.AntiWorldFlyToggleFly;
import com.hm.antiworldfly.listener.AntiWorldFlyWorldJoin;
import com.hm.antiworldfly.utils.FileManager;
import com.hm.antiworldfly.utils.UpdateChecker;
import com.hm.antiworldfly.utils.YamlManager;

/**
 * A plugin to disable flying and chosen commands when joining or playing in specific worlds.
 * 
 * AntiWorldFly is under GNU General Public License version 3.
 * 
 * Please visit the plugin's GitHub for more information : https://github.com/PyvesB/AntiWorldFly
 * 
 * Official plugin's server: hellominecraft.fr
 * 
 * Bukkit project page: dev.bukkit.org/bukkit-plugins/anti-world-fly
 * 
 * Spigot project page: spigotmc.org/resources/anti-world-fly.5357
 * 
 * @since March 2015.
 * @version 2.1.1
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
	private boolean notifyNotFlying;
	private boolean successfulLoad;

	// Fields related to file handling.
	private YamlManager config;
	private YamlManager lang;
	private FileManager fileManager;

	// Plugin listeners.
	private AntiWorldFlyPreProcess awfPreProcess;
	private AntiWorldFlyWorldJoin awfWorldJoin;
	private AntiWorldFlyPlayerJoin awfPlayerJoin;
	private AntiWorldFlyToggleFly awfPlayerToggleFly;

	// Used to check for plugin updates.
	private UpdateChecker updateChecker;

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
		fileManager = new FileManager(this);
	}

	/**
	 * Called when server is launched or reloaded.
	 */
	@Override
	public void onEnable() {

		// Start enabling plugin.
		long startTime = System.currentTimeMillis();

		this.getLogger().info("Registering listeners...");

		awfPreProcess = new AntiWorldFlyPreProcess(this);
		awfWorldJoin = new AntiWorldFlyWorldJoin(this);
		awfPlayerJoin = new AntiWorldFlyPlayerJoin(this);
		awfPlayerToggleFly = new AntiWorldFlyToggleFly(this);

		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(awfPreProcess, this);
		pm.registerEvents(awfWorldJoin, this);
		pm.registerEvents(awfPlayerJoin, this);
		pm.registerEvents(awfPlayerToggleFly, this);

		extractParametersFromConfig(true);

		// Check for available plugin update.
		if (config.getBoolean("checkForUpdate", true))
			updateChecker = new UpdateChecker(this);

		try {
			MetricsLite metrics = new MetricsLite(this);
			metrics.start();
		} catch (IOException e) {
			this.getLogger().severe("Error while sending Metrics statistics.");
			successfulLoad = false;
		}

		chatHeader = ChatColor.GRAY + "[" + ChatColor.BLUE + "\u06DE" + ChatColor.GRAY + "] " + ChatColor.WHITE;

		helpCommand = new HelpCommand(this);
		infoCommand = new InfoCommand(this);

		if (successfulLoad)
			this.getLogger().info("Plugin successfully enabled and ready to run! Took "
					+ (System.currentTimeMillis() - startTime) + "ms.");
		else
			this.getLogger().severe("Error(s) while loading plugin. Please view previous logs for more information.");
	}

	/**
	 * Extract plugin parameters from the configuration file.
	 * 
	 * @param attemptUpdate
	 */
	private void extractParametersFromConfig(boolean attemptUpdate) {

		successfulLoad = true;
		Logger logger = this.getLogger();

		logger.info("Backing up and loading configuration files...");

		try {
			config = fileManager.getNewConfig("config.yml");
		} catch (IOException e) {
			logger.severe("Error while loading configuration file.");
			e.printStackTrace();
			successfulLoad = false;
		} catch (InvalidConfigurationException e) {
			logger.severe("Error while loading configuration file, disabling plugin.");
			logger.severe("Verify your syntax using the following logs and by visiting yaml-online-parser.appspot.com");
			e.printStackTrace();
			successfulLoad = false;
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		try {
			lang = fileManager.getNewConfig(config.getString("languageFileName", "lang.yml"));
		} catch (IOException e) {
			logger.severe("Error while loading language file.");
			e.printStackTrace();
			successfulLoad = false;
		} catch (InvalidConfigurationException e) {
			logger.severe("Error while loading language file, disabling plugin.");
			logger.severe("Verify your syntax using the following logs and by visiting yaml-online-parser.appspot.com");
			e.printStackTrace();
			successfulLoad = false;
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}

		try {
			fileManager.backupFile("config.yml");
		} catch (IOException e) {
			logger.severe("Error while backing up configuration file.");
			e.printStackTrace();
			successfulLoad = false;
		}

		try {
			fileManager.backupFile(config.getString("languageFileName", "lang.yml"));
		} catch (IOException e) {
			logger.severe("Error while backing up language file.");
			e.printStackTrace();
			successfulLoad = false;
		}

		// Update configurations from previous versions of the plugin if server reloads or restarts.
		if (attemptUpdate) {
			updateOldConfiguration();
			updateOldLanguage();
		}

		antiFlyWorlds = this.getConfig().getStringList("antiFlyWorlds");
		chatMessage = this.getConfig().getBoolean("chatMessage", true);
		titleMessage = this.getConfig().getBoolean("titleMessage", true);
		antiFlyCreative = this.getConfig().getBoolean("antiFlyCreative", true);
		notifyNotFlying = this.getConfig().getBoolean("notifyNotFlying", true);
		otherBlockedCommands = this.getConfig().getStringList("otherBlockedCommands");

		// Set to null in case user changed the option and did a /awf reload. Do not recheck for update on /awf
		// reload.
		if (!config.getBoolean("checkForUpdate", true)) {
			updateChecker = null;
		}
	}

	/**
	 * Update configuration file from older plugin versions by adding missing parameters. Upgrades from versions prior
	 * to 2.1 are not supported.
	 */
	private void updateOldConfiguration() {

		boolean updateDone = false;

		// Added in version 2.1:
		if (!config.getKeys(false).contains("languageFileName")) {
			config.set("languageFileName", "lang.yml", "Name of the language file.");
			updateDone = true;
		}

		if (!config.getKeys(false).contains("checkForUpdate")) {
			config.set("checkForUpdate", true,
					"Check for update on plugin launch and notify when an OP joins the game.");
			updateDone = true;
		}
		
		// Added in version 2.2:
		if (!config.getKeys(false).contains("notifyNotFlying")) {
			config.set("notifyNotFlying", true,
					"Notify player when entering a world in which flying is blocked even if he is not flying.");
			updateDone = true;
		}

		if (updateDone) {
			// Changes in the configuration: save and do a fresh load.
			try {
				config.saveConfig();
				config.reloadConfig();
			} catch (IOException e) {
				this.getLogger().severe("Error while saving changes to the configuration file.");
				e.printStackTrace();
				successfulLoad = false;
			}
		}
	}

	/**
	 * Update language file from older plugin versions by adding missing parameters. Upgrades from versions prior to 2.1
	 * are not supported.
	 */
	private void updateOldLanguage() {

		boolean updateDone = false;

		// Added in version 2.1:
		if (!lang.getKeys(false).contains("awf-command-add-hover")) {
			lang.set("awf-command-add-hover", "Make sure you specify the correct name!");
			updateDone = true;
		}

		if (!lang.getKeys(false).contains("awf-command-remove-hover")) {
			lang.set("awf-command-remove-hover", "World must be listed in /awf list.");
			updateDone = true;
		}

		if (!lang.getKeys(false).contains("awf-command-list-hover")) {
			lang.set("awf-command-list-hover", "Flying and some specific commands are disabled in these worlds.");
			updateDone = true;
		}

		if (!lang.getKeys(false).contains("awf-command-disable-hover")) {
			lang.set("awf-command-disable-hover", "The plugin will not work until next reload or /awf enable.");
			updateDone = true;
		}

		if (!lang.getKeys(false).contains("awf-command-enable-hover")) {
			lang.set("awf-command-enable-hover",
					"Plugin enabled by default. Use this if you entered /awf disable before!");
			updateDone = true;
		}

		if (!lang.getKeys(false).contains("awf-command-reload-hover")) {
			lang.set("awf-command-reload-hover", "Reload most settings in config.yml and lang.yml files.");
			updateDone = true;
		}

		if (!lang.getKeys(false).contains("awf-command-info-hover")) {
			lang.set("awf-command-info-hover", "Some extra info about the plugin and its awesome author!");
			updateDone = true;
		}

		if (!lang.getKeys(false).contains("awf-tip")) {
			lang.set("awf-tip", "&lHINT&r &7You can &f&n&ohover&r &7or &f&n&oclick&r &7on the commands!");
			updateDone = true;
		}

		if (!lang.getKeys(false).contains("command-error")) {
			lang.set("command-error", "An error occurred while executing the command.");
			updateDone = true;
		}

		if (updateDone) {
			// Changes in the language file: save and do a fresh load.
			try {
				lang.saveConfig();
				lang.reloadConfig();
			} catch (IOException e) {
				this.getLogger().severe("Error while saving changes to the language file.");
				e.printStackTrace();
				successfulLoad = false;
			}
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

			sender.sendMessage(chatHeader + lang.getString("words-blocked", "Worlds in which flying is blocked:"));
			for (String world : antiFlyWorlds)
				sender.sendMessage(" - " + world);

		} else if (args[0].equalsIgnoreCase("info")) {

			infoCommand.getInfo(sender);

		} else if (sender.hasPermission("antiworldfly.use")) {

			String action = args[0].toLowerCase();

			if (action.equals("reload")) {

				this.reloadConfig();
				extractParametersFromConfig(false);

				if (successfulLoad) {

					if (sender instanceof Player)
						sender.sendMessage(chatHeader + lang.getString("configuration-successfully-reloaded",
								"Configuration successfully reloaded."));
					this.getLogger().info("Configuration successfully reloaded.");
				}
			} else if (action.equals("disable")) {

				disabled = true;
				sender.sendMessage(chatHeader
						+ lang.getString("awf-disabled", "AntiWorldFly disabled till next reload or /awf enable."));

			} else if (action.equals("enable")) {

				disabled = false;
				sender.sendMessage(chatHeader + lang.getString("awf-enabled", "AntiWorldFly enabled."));

			} else if (action.equals("add") && args.length == 2) {

				antiFlyWorlds.add(args[1]);
				config.set("antiFlyWorlds", antiFlyWorlds);
				try {
					config.saveConfig();
					config.reloadConfig();
				} catch (IOException e) {
					this.getLogger().severe("Error while adding world.");
					sender.sendMessage(chatHeader
							+ lang.getString("command-error", "An error occurred while executing the command."));
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(
						chatHeader + lang.getString("world-added", "New world successfully added: ") + args[1]);

			} else if (action.equals("remove") && args.length == 2) {

				for (int i = 0; i < antiFlyWorlds.size(); i++) {
					if (antiFlyWorlds.get(i).equals(args[1]))
						antiFlyWorlds.remove(i);
				}
				config.set("antiFlyWorlds", antiFlyWorlds);
				try {
					config.saveConfig();
					config.reloadConfig();
				} catch (IOException e) {
					this.getLogger().severe("Error while removing world.");
					sender.sendMessage(chatHeader
							+ lang.getString("command-error", "An error occurred while executing the command."));
					e.printStackTrace();
					return true;
				}
				sender.sendMessage(
						chatHeader + lang.getString("world-removed", "World successfully removed: ") + args[1]);

			} else
				sender.sendMessage(
						chatHeader + lang.getString("misused-command", "Misused command. Please type /awf."));

		} else
			sender.sendMessage(
					chatHeader + lang.getString("no-permissions", "You do not have the permission to do this."));

		return true;
	}

	// Various getters and setters. Names are self-explanatory.

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
	
	public boolean isNotifyNotFlying() {

		return notifyNotFlying;
	}


	public String getChatHeader() {

		return chatHeader;
	}

	public YamlManager getPluginLang() {

		return lang;
	}

	public UpdateChecker getUpdateChecker() {

		return updateChecker;
	}

	public List<String> getOtherBlockedCommands() {

		return otherBlockedCommands;
	}

	public void setSuccessfulLoad(boolean successfulLoad) {

		this.successfulLoad = successfulLoad;
	}
}
