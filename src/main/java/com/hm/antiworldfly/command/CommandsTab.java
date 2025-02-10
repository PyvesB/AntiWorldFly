package com.hm.antiworldfly.command;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import com.hm.antiworldfly.AntiWorldFly;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class CommandsTab implements TabCompleter {
    private final AntiWorldFly plugin;

    public CommandsTab(AntiWorldFly plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> cmdlist = new ArrayList<String>();
        FileConfiguration config = plugin.getConfig();

        if (args.length == 1) {
            cmdlist.add("help");
            cmdlist.add("info");
            if (sender.hasPermission("antiworldfly.use")) {
                cmdlist.addAll(Arrays.asList("reload", "disable", "enable", "add", "remove", "world"));
            }
        } else if (args.length == 2 && sender.hasPermission("antiworldfly.use")) {
            String input = args[1].toLowerCase();

            if (args[0].equalsIgnoreCase("add")) {
                for (World world : Bukkit.getWorlds()) {
                    String worldName = world.getName();
                    if (worldName.toLowerCase().startsWith(input)) {
                        cmdlist.add(worldName);
                    }
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                for (String worldName : config.getStringList("antiFlyWorlds")) {
                    if (worldName.toLowerCase().startsWith(input)) {
                        cmdlist.add(worldName);
                    }
                }
            }
        }
        return cmdlist.isEmpty() ? null : cmdlist;
    }
}
