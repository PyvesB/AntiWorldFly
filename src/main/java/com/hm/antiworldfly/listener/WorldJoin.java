package com.hm.antiworldfly.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
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
 * @maintainer Sidpatchy
 */
public class WorldJoin implements Listener {

	private final AntiWorldFly plugin;

	public WorldJoin(AntiWorldFly awf) {
		this.plugin = awf;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void worldJoin(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		if (plugin.isDisabled() || player.hasPermission("antiworldfly.fly." + player.getWorld().getName())) {
			return;
		}

		if (!this.plugin.isAntiFlyCreative() && player.getGameMode() == GameMode.CREATIVE
				|| "SPECTATOR".equals(player.getGameMode().toString())) {
			return;
		}

		for (String world : plugin.getAntiFlyWorlds()) {
			if (player.getWorld().getName().equalsIgnoreCase(world)) {
				// Schedule runnable to disable flying.
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(
						Bukkit.getPluginManager().getPlugin("AntiWorldFly"),
						new AntiWorldFlyRunnable(player, plugin), 20);
				return;
			}
		}

		if (plugin.isToggleFlyingInNonBlockedWorlds() && !player.getAllowFlight()
				&& (player.hasPermission("antiworldfly.fly") || player.hasPermission("essentials.fly"))) {
			// Enable flying.
			player.setAllowFlight(true);
			if (!player.isOnGround()) {
				player.setFlying(true);
			}
		}
	}
}
