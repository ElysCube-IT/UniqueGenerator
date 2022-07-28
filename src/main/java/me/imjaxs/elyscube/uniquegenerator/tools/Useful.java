package me.imjaxs.elyscube.uniquegenerator.tools;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Useful {
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> colorize(List<String> strings) {
        return strings.stream().map(Useful::colorize).collect(Collectors.toList());
    }
}
