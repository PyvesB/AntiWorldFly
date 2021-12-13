package com.hm.antiworldfly.worldguard.flags;

import com.hm.antiworldfly.AntiWorldFly;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class RegisterAntiFlyFlag {

    private final AntiWorldFly plugin;

    public RegisterAntiFlyFlag(AntiWorldFly awf) {
        this.plugin = awf;
    }

    public static StateFlag antiFlyFlag;

    public void register() {
        if (plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
            try {
                // create a flag with the name "my-custom-flag", defaulting to true
                StateFlag flag = new StateFlag(plugin.getAntiFlyFlag(), true);
                registry.register(flag);
                antiFlyFlag = flag; // only set our field if there was no error
                plugin.getLogger().info("WorldGuard flag successfully registered!");
            } catch (
                    FlagConflictException e) {
                // some other plugin registered a flag by the same name already.
                // you can use the existing flag, but this may cause conflicts - be sure to check type
                Flag<?> existing = registry.get(plugin.getAntiFlyFlag());
                if (existing instanceof StateFlag) {
                    antiFlyFlag = (StateFlag) existing;
                } else {
                    // types don't match - this is bad news! some other plugin conflicts with you
                    // hopefully this never actually happens
                    plugin.getLogger().warning("Multiple plugins are registering the flag \"" + plugin.getAntiFlyFlag() + "\"");
                }
            }
        }
    }
}
