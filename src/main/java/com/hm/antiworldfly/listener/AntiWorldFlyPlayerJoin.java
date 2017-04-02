package com.hm.antiworldfly.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.antiworldfly.AntiWorldFlyRunnable;

/**
 * Class to disable flying when a player joins the server.
 * 
 * @author Pyves
 */
public class AntiWorldFlyPlayerJoin implements Listener {

	private AntiWorldFly plugin;

	public AntiWorldFlyPlayerJoin(AntiWorldFly awf) {
		this.plugin = awf;
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
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
