package com.hm.antiworldfly.listener;

import org.bukkit.entity.Player;
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
public class PlayerJoin implements Listener {

	private final AntiWorldFly plugin;

	public PlayerJoin(AntiWorldFly awf) {
		this.plugin = awf;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		for (String world : plugin.getAntiFlyWorlds()) {
			if (player.getWorld().getName().equalsIgnoreCase(world)) {
				// Schedule runnable to disable flying.
				plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,
						new AntiWorldFlyRunnable(player, plugin), 20);
				return;
			}
		}

		if (plugin.isToggleFlyingInNonBlockedWorlds() && !player.getAllowFlight()
				&& (player.hasPermission("antiworldfly.fly." + player.getWorld().getName()) || player.hasPermission("essentials.fly"))) {
			// Enable flying.
			player.setAllowFlight(true);
			if (!player.isOnGround()) {
				player.setFlying(true);
			}
		}
	}

}
