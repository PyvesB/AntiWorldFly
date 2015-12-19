package com.hm.antiworldfly.language;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
 
/**
* An enum for requesting strings from the language file.
* @author gomeow
*/
public enum Lang {
    
    CONFIGURATION_SUCCESSFULLY_RELOADED("configuration-successfully-reloaded", "Configuration successfully reloaded."),
    CONFIGURATION_RELOAD_FAILED("configuration-reload-failed", "Errors while reloading configuration. Please view logs for more details."),
    NO_PERMS("no-permissions", "You do not have the permission to do this."),
    WORLDS_BLOCKED("words-blocked", "Worlds in which flying is blocked:"),
    AWF_DISABLED("awf-disabled", "AntiWorldFly disabled till next reload or /awf enable."),
    AWF_ENABLED("awf-enabled", "AntiWorldFly enabled."),
    WORLD_ADDED("world-added","New world successfully added: "),
    WORLD_REMOVED("world-removed","World successfully removed: "),
    MISUSED_COMMAND("misused-command","Misused command. Please type /awf."),
    FLY_DISABLED_CHAT("fly-disabled-chat", "Flying is disabled in this world."),
    COMMAND_DISABLED_CHAT("command-disabled-chat", "Command is disabled in this world."),
    FLY_DISABLED_TITLE("fly-disabled-title", "&9AntiWorldFly"),
    FLY_DISABLED_SUBTITLE("fly-disabled-subtitle", "Flying is disabled in this world."),
    AWF_COMMAND_DISABLE("awf-command-disable", "Disable plugin till next reload."),
    AWF_COMMAND_ENABLE("awf-command-enable", "Enable plugin (if previously disabled)."),
    AWF_COMMAND_LIST("awf-command-list", "List worlds in which flying is blocked."),
    AWF_COMMAND_ADD("awf-command-add", "Add WORLD to blocked worlds."),
    AWF_COMMAND_RELOAD("awf-command-reload", "Reload the plugin's configuration."),
    AWF_COMMAND_INFO("awf-command-info", "Display various information about the plugin."),
    AWF_COMMAND_REMOVE("awf-command-remove", "Remove WORLD from blocked worlds."),    
    VERSION_COMMAND_NAME("version-command-name", "Name:"),
    VERSION_COMMAND_VERSION("version-command-version", "Version:"),
    VERSION_COMMAND_WEBSITE("version-command-website", "Website:"),
    VERSION_COMMAND_AUTHOR("version-command-author", "Author:"),
    VERSION_COMMAND_DESCRIPTION("version-command-description", "Description:"),
    VERSION_COMMAND_DESCRIPTION_DETAILS("version-command-description-details", "A plugin to disable flying and chosen commands when joining or playing in specific worlds."),
    VERSION_COMMAND_ENABLED("version-command-enabled", "Plugin enabled:");
 
    private String path;
    private String def;
    private static YamlConfiguration LANG;
 
    /**
    * Lang enum constructor.
    * @param path The string path.
    * @param start The default string.
    */
    Lang(String path, String start) {
        this.path = path;
        this.def = start;
    }
 
    /**
    * Set the {@code YamlConfiguration} to use.
    * @param config The config to set.
    */
    public static void setFile(YamlConfiguration config) {
        LANG = config;
    }
 
    @Override
    public String toString() {        
        return ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def));
    }
 
    /**
    * Get the default value of the path.
    * @return The default value of the path.
    */
    public String getDefault() {
        return this.def;
    }
 
    /**
    * Get the path to the string.
    * @return The path to the string.
    */
    public String getPath() {
        return this.path;
    }
}