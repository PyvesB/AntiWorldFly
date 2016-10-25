package com.hm.antiworldfly.listener;

import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.antiworldfly.particle.PacketSender;

/**
 * Class to disable flying if a player manages to toggle flying.
 * 
 * @author Pyves
 */
public class AntiWorldFlyToggleFly implements Listener {

	private AntiWorldFly plugin;

	public AntiWorldFlyToggleFly(AntiWorldFly awf) {

		this.plugin = awf;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerCommandPreprocessEvent(PlayerToggleFlightEvent event) {

		if (plugin.isDisabled() || event.getPlayer().hasPermission("antiworldfly.fly"))
			return;

		if (!this.plugin.isAntiFlyCreative() && event.getPlayer().getGameMode() == GameMode.CREATIVE
				|| event.getPlayer().getGameMode() == GameMode.SPECTATOR)
			return;

		Player player = event.getPlayer();

		for (String world : plugin.getAntiFlyWorlds()) {

			if (event.getPlayer().getWorld().getName().equalsIgnoreCase(world)) {
				// Disable flying.
				player.setAllowFlight(false);
				player.getPlayer().setFlying(false);
				event.setCancelled(true);

				if (plugin.isChatMessage())
					player.sendMessage(plugin.getChatHeader() + plugin.getPluginLang().getString("fly-disabled-chat",
							"Flying is disabled in this world."));

				if (plugin.isTitleMessage()) {
					try {
						PacketSender.sendTitlePacket(player, "{\"text\":\""
								+ plugin.getPluginLang().getString("fly-disabled-title", "&9AntiWorldFly") + "\"}",
								"{\"text\":\"" + plugin.getPluginLang().getString("fly-disabled-subtitle",
										"Flying is disabled in this world.") + "\"}");
					} catch (Exception e) {
						plugin.getLogger().log(Level.SEVERE, "Errors while trying to display flying disabled title: ",
								e);
					}
				}
				break;
			}
		}

	}

}
