package com.hm.antiworldfly;

import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.hm.mcshared.particle.PacketSender;

public class AntiWorldFlyRunnable implements Runnable {

	private AntiWorldFly plugin;

	public AntiWorldFlyRunnable(AntiWorldFly awf) {
		this.plugin = awf;
	}

	@Override
	public void run() {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			// Check if flying is allowed in the world
			for (String world : plugin.getAntiFlyWorlds()) {
				if (!player.getWorld().getName().equalsIgnoreCase(world)) return;
			}

			if (player.hasPermission("antiworldfly.fly") || player.hasPermission("essentials.fly")) return;
			if (!this.plugin.isAntiFlyCreative() && player.getGameMode() == GameMode.CREATIVE
					|| "SPECTATOR".equals(player.getGameMode().toString())) return;

			if (plugin.isChatMessage()
					&& (plugin.isNotifyNotFlying() || !plugin.isNotifyNotFlying() && player.isFlying())) {
				player.sendMessage(plugin.getChatHeader()
						+ plugin.getPluginLang().getString("fly-disabled-chat", "Flying is disabled in this world."));
			}

			if (plugin.isTitleMessage()
					&& (plugin.isNotifyNotFlying() || !plugin.isNotifyNotFlying() && player.isFlying())) {
				try {
					PacketSender.sendTitlePacket(player,
							"{\"text\":\"" + plugin.getPluginLang().getString("fly-disabled-title", "&9AntiWorldFly")
									+ "\"}",
							"{\"text\":\"" + plugin.getPluginLang().getString("fly-disabled-subtitle",
									"Flying is disabled in this world.") + "\"}");
				} catch (Exception e) {
					plugin.getLogger().log(Level.SEVERE, "Errors while trying to display flying disabled title: ", e);
				}
			}

			// Disable flying.
			player.setAllowFlight(false);
			player.setFlying(false);
		}
	}
}
