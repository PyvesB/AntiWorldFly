package com.hm.antiworldfly.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.antiworldfly.AntiWorldFlyRunnable;

/**
 * Class to disable flying when a player changes worlds.
 * 
 * @author Pyves
 */
public class AntiWorldFlyWorldJoin implements Listener {

	private AntiWorldFly plugin;

	public AntiWorldFlyWorldJoin(AntiWorldFly awf) {
		this.plugin = awf;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void worldJoin(PlayerChangedWorldEvent event) {
		if (plugin.isDisabled() || event.getPlayer().hasPermission("antiworldfly.fly"))
			return;

		if (!this.plugin.isAntiFlyCreative() && event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| "SPECTATOR".equals(event.getPlayer().getGameMode().toString()))
			return;

		for (String world : plugin.getAntiFlyWorlds()) {

			if (event.getPlayer().getWorld().getName().equalsIgnoreCase(world)) {
				// Schedule runnable to disable flying.
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
						Bukkit.getPluginManager().getPlugin("AntiWorldFly"),
						new AntiWorldFlyRunnable(event.getPlayer(), plugin), 20);
				break;
			}
		}
	}
}
