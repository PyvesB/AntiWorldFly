package com.hm.antiworldfly.worldguard;

import com.hm.antiworldfly.AntiWorldFly;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;

public class FlagRegistry {
    private final AntiWorldFly plugin;

    public FlagRegistry(AntiWorldFly plugin) {
        this.plugin = plugin;
    }

    public static StateFlag flightFlag;

    public void register(String flagName) {
        com.sk89q.worldguard.protection.flags.registry.FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            StateFlag flag = new StateFlag(flagName, true);
            registry.register(flag);
            flightFlag = flag;
            plugin.getLogger().info("Flag \"" + flagName + "\" successfully registered!");
        } catch (FlagConflictException e) {
            // Thrown if the name is already in use by another plugin.
            Flag<?> existing = registry.get(flagName);
            if (existing instanceof StateFlag) {
                flightFlag = (StateFlag) existing;
            } else {
                // Multiple plugins are registering a flag of the same name of different types.
                plugin.getLogger().warning("Multiple plugins are registering the flag \"" + flagName + "\"");
            }
        }
    }
}
