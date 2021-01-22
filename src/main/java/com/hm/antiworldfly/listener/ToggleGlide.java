package com.hm.antiworldfly.listener;

import com.hm.antiworldfly.AntiWorldFly;
import com.hm.antiworldfly.AntiWorldFlyRunnable;
import com.hm.mcshared.particle.FancyMessageSender;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;

import java.util.logging.Level;

/**
 * Class to block the player from using the Elytra in blocked worlds
 *
 * @author Sidpatchy
 */
public class ToggleGlide implements Listener {

    private final AntiWorldFly plugin;

    public ToggleGlide(AntiWorldFly awf) {
        this.plugin = awf;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityToggleGlideEvent(EntityToggleGlideEvent event) {

        Entity entity = event.getEntity();
        Player player = (Player) entity;

        if (plugin.isDisabled() || ! plugin.isElytraDisabled() ||
                (entity.hasPermission("antiworldfly.elytra." + entity.getWorld().getName())) ||
                (! (plugin.isAntiFlyCreative()) && player.getGameMode() == GameMode.CREATIVE)) {
            return;
        }

        for (String world : plugin.getAntiFlyWorlds()) {
            if (entity.getWorld().getName().equalsIgnoreCase(world)) {

                // Disable elytra
                event.setCancelled(true);

                if (plugin.isTitleMessage()) {
                    try {FancyMessageSender.sendTitle(player,
                            plugin.getPluginLang().getString("fly-disabled-title", "&9AntiWorldFly"),
                            plugin.getPluginLang().getString("fly-disabled-subtitle", "Flying is disabled in this world."));
                    } catch (Exception e) {
                        plugin.getLogger().log(Level.SEVERE, "Errors while trying to display flying disabled title: ",
                                e);
                    }
                }
                return;
            }
        }
    }
}
