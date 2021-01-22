package com.hm.antiworldfly;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.hm.mcshared.particle.FancyMessageSender;

public class AntiWorldFlyRunnable implements Runnable {

	private final Player player;
	private final AntiWorldFly plugin;

	public AntiWorldFlyRunnable(Player player, AntiWorldFly awf) {
		this.player = player;
		this.plugin = awf;
	}

	@Override
	public void run() {
		if (plugin.isDisabled() || player.hasPermission("antiworldfly.fly") || player.hasPermission("antiworldfly.fly." + player.getWorld().getName())) {
			return;
		}

		if (!this.plugin.isAntiFlyCreative() && player.getGameMode() == GameMode.CREATIVE
				|| "SPECTATOR".equals(player.getGameMode().toString())) {
			return;
		}

		if (plugin.isChatMessage()
				&& (plugin.isNotifyNotFlying() || !plugin.isNotifyNotFlying() && player.isFlying())) {
			player.sendMessage(plugin.getChatHeader()
					+ plugin.getPluginLang().getString("fly-disabled-chat", "Flying is disabled in this world."));
		}

		if (plugin.isTitleMessage()
				&& (plugin.isNotifyNotFlying() || !plugin.isNotifyNotFlying() && player.isFlying())) {
			try {
				String title = ChatColor.translateAlternateColorCodes('&',
						plugin.getPluginLang().getString("fly-disabled-title", "&9AntiWorldFly"));
				String subtitle = ChatColor.translateAlternateColorCodes('&',
						plugin.getPluginLang().getString("fly-disabled-subtitle", "Flying is disabled in this world."));
				FancyMessageSender.sendTitle(player, title, subtitle);
			} catch (Exception e) {
				plugin.getLogger().log(Level.SEVERE, "Errors while trying to display flying disabled title: ", e);
			}
		}

		// Disable flying.
		player.setAllowFlight(false);
		player.setFlying(false);
	}
}
