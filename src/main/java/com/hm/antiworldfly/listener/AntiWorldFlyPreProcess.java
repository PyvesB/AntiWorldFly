package com.hm.antiworldfly.listener;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.antiworldfly.AntiWorldFlyRunnable;

/**
 * Class to block some commands specified by the user in the config.
 * 
 * @author Pyves
 */
public class AntiWorldFlyPreProcess implements Listener {

	private AntiWorldFly plugin;

	public AntiWorldFlyPreProcess(AntiWorldFly awf) {
		this.plugin = awf;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
		if (plugin.isDisabled() || event.getPlayer().hasPermission("antiworldfly.fly"))
			return;

		if (!this.plugin.isAntiFlyCreative() && event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR)
			return;

		String command = event.getMessage().toLowerCase();

		// Check for most common fly commands and aliases.
		if (command.startsWith("/fly") || command.startsWith("/essentials:fly") || command.startsWith("/efly")) {

			blockCommand(event);
		}
		// Check for creative mode commands.
		else if (command.startsWith("/gm 1") || command.startsWith("/gamemode c") || command.startsWith("/gm c")) {

			if (!this.plugin.isAntiFlyCreative())
				return;

			for (String world : plugin.getAntiFlyWorlds()) {
				if (event.getPlayer().getWorld().getName().equalsIgnoreCase(world)) {
					// Schedule runnable to disable flying.
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
							new AntiWorldFlyRunnable(event.getPlayer(), plugin), 20);

					break;

				}
			}
		} else {
			// Check if other commands were blocked by the user.
			boolean otherBlockedCommand = false;
			if (!plugin.getOtherBlockedCommands().isEmpty())
				for (String blockedCommand : plugin.getOtherBlockedCommands()) {
					if (blockedCommand.equalsIgnoreCase(command))
						otherBlockedCommand = true;
				}
			if (otherBlockedCommand)
				blockCommand(event);
		}

	}

	/**
	 * Block a command and cancel corresponding event.
	 * 
	 * @param event
	 */
	private void blockCommand(PlayerCommandPreprocessEvent event) {
		if (!this.plugin.isAntiFlyCreative() && event.getPlayer().getGameMode() == GameMode.CREATIVE)
			return;

		for (String world : plugin.getAntiFlyWorlds()) {
			if (event.getPlayer().getWorld().getName().equalsIgnoreCase(world)) {
				event.getPlayer().sendMessage(plugin.getChatHeader() + plugin.getPluginLang()
						.getString("command-disabled-chat", "Command is disabled in this world."));
				event.setCancelled(true);
				break;

			}
		}
	}
}
