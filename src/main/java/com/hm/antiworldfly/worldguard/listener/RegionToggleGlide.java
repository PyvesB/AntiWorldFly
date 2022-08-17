package com.hm.antiworldfly.worldguard.listener;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.mcshared.particle.FancyMessageSender;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import java.util.logging.Level;

/**
 * Class to block the player from using the Elytra in disabled regions.
 *
 * @author Sidpatchy
 */
public class RegionToggleGlide implements Listener {

    private final AntiWorldFly plugin;

    public RegionToggleGlide(AntiWorldFly awf) {
        this.plugin = awf;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {

        Entity entity = event.getEntity();
        if (!entity.getType().equals(EntityType.PLAYER)) { return; }
        Player player = (Player) entity;

        if (plugin.isDisabled() ||(entity.hasPermission("antiworldfly.elytra." + entity.getWorld().getName())) ||
                (! (plugin.isAntiFlyCreative()) && player.getGameMode() == GameMode.CREATIVE)) {
            return;
        }

        StateFlag flag = (StateFlag) WorldGuard.getInstance().getFlagRegistry().get(plugin.getAntiElytraFlag());

        RegionQuery query = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(BukkitAdapter.adapt(player.getLocation()));
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        if (!set.testState(localPlayer, flag)) {
            // Disable elytra
            event.setCancelled(true);

            if (plugin.isChatMessage()) {
                player.sendMessage(plugin.getChatHeader() + plugin.getPluginLang().getString("elytra-disabled-region",
                        "Elytras are disabled in this region."));
            }

            if (plugin.isTitleMessage()) {
                try {
                    FancyMessageSender.sendTitle(player,
                            plugin.getPluginLang().getString("fly-disabled-title", "&9AntiWorldFly"),
                            plugin.getPluginLang().getString("elytra-disabled-region", "Elytras are disabled in this region."));
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Errors while trying to display flying disabled title: ",
                            e);
                }
            }
        }
    }
}
