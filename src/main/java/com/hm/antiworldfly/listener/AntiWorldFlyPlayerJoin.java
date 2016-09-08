package com.hm.antiworldfly.listener;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.antiworldfly.AntiWorldFlyRunnable;
import com.hm.antiworldfly.utils.UpdateChecker;

public class AntiWorldFlyPlayerJoin implements Listener {

	private AntiWorldFly plugin;

	public AntiWorldFlyPlayerJoin(AntiWorldFly awf) {

		this.plugin = awf;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {

		// Check if OP to display new version message if needed.
		if (plugin.getUpdateChecker() != null && plugin.getUpdateChecker().isUpdateNeeded()
				&& event.getPlayer().hasPermission("antiworldfly.use")) {
			event.getPlayer().sendMessage(plugin.getChatHeader() + "Update available: v"
					+ plugin.getUpdateChecker().getVersion() + ". Download at one of the following:");
			event.getPlayer().sendMessage(ChatColor.GRAY + UpdateChecker.BUKKIT_DONWLOAD_URL);
			event.getPlayer().sendMessage(ChatColor.GRAY + UpdateChecker.SPIGOT_DONWLOAD_URL);
		}

		for (String world : plugin.getAntiFlyWorlds()) {

			if (event.getPlayer().getWorld().getName().equalsIgnoreCase(world)) {
				// Schedule runnable to disable flying.
				plugin.getServer()
						.getScheduler()
						.scheduleSyncDelayedTask(plugin, new AntiWorldFlyRunnable(event.getPlayer(), plugin), 20);

				break;
			}
		}
	}

}
