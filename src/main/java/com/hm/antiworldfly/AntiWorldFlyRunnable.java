package com.hm.antiworldfly;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.hm.antiworldfly.language.Lang;
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
			player.sendMessage(plugin.getChatHeader() + Lang.FLY_DISABLED_CHAT);

		if (plugin.isTitleMessage()) {
			try {
				PacketSender.sendTitlePacket(player, "{\"text\":\"" + Lang.FLY_DISABLED_TITLE + "\"}", "{\"text\":\""
						+ Lang.FLY_DISABLED_SUBTITLE + "\"}");
			} catch (Exception ex) {

				plugin.getLogger().severe(
						"Errors while trying to display flying disabled title. Is your server up-to-date ?");
				ex.printStackTrace();
			}
		}

	}

}
