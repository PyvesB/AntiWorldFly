package com.hm.antiworldfly.worldguard.listener;

import com.hm.antiworldfly.AntiWorldFly;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


/**
 * Gonna be completely honest, this is the best way I can think to do this. I was initially just gonna write
 * listeners that mirror the listeners for flight in worlds, but to check if a player is flying within a world
 * we're gonna have to listen to PlayerMoveEvent regardless so here we are. If we could guarantee a player wasn't
 * flying when entering the region, this would be a completely different story, but I'm fairly certain we can't
 * without this event.
 *
 * That being said, if anyone has a better way of doing this, I'm open to any and all improvements.
 */
public class PlayerMove implements Listener {

    private AntiWorldFly plugin;

    public PlayerMove(AntiWorldFly awf) {
        this.plugin = awf;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocessEvent(PlayerMoveEvent event) {

        Player player = event.getPlayer();

        if (plugin.isDisabled() || player.hasPermission("antiworldfly.fly." + player.getWorld().getName()) || !player.getPlayer().isFlying()) {
            return;
        }

        if (!this.plugin.isAntiFlyCreative() && event.getPlayer().getGameMode() == GameMode.CREATIVE
                || "SPECTATOR".equals(event.getPlayer().getGameMode().toString())) {
            return;
        }

        StateFlag flag = (StateFlag) WorldGuard.getInstance().getFlagRegistry().get(plugin.getAntiFlyFlag());

        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        if (set.testState(localPlayer, flag)) {
            // Disable flying.
            player.setAllowFlight(false);
            player.getPlayer().setFlying(false);
            event.setCancelled(true);
        }
    }
}