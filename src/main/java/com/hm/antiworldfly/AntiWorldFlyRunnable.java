package com.hm.antiworldfly;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.hm.antiworldfly.particle.PacketSender;

public class AntiWorldFlyRunnable implements Runnable {

	private Player player;
	private AntiWorldFly plugin;

	public AntiWorldFlyRunnable(Player player, AntiWorldFly awf) {

		this.player = player;
		this.plugin = awf;
	}

	@Override
	public void run() {

		if (plugin.isDisabled() || player.hasPermission("antiworldfly.fly"))
			return;

		if (!this.plugin.isAntiFlyCreative() && player.getGameMode() == GameMode.CREATIVE)
			return;

		// Disable flying.
		player.setAllowFlight(false);
		player.getPlayer().setFlying(false);

		if (plugin.isChatMessage())
			player.sendMessage(plugin.getChatHeader()
					+ plugin.getPluginLang().getString("fly-disabled-chat", "Flying is disabled in this world."));

		if (plugin.isTitleMessage()) {
			try {
				PacketSender.sendTitlePacket(player,
						"{\"text\":\"" + plugin.getPluginLang().getString("fly-disabled-title", "&9AntiWorldFly")
								+ "\"}",
						"{\"text\":\"" + plugin.getPluginLang().getString("fly-disabled-subtitle",
								"Flying is disabled in this world.") + "\"}");
			} catch (Exception ex) {

				plugin.getLogger()
						.severe("Errors while trying to display flying disabled title. Is your server up-to-date ?");
				ex.printStackTrace();
			}
		}

	}

}
