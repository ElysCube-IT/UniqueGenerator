package me.imjaxs.elyscube.uniquegenerator.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.List;
import java.util.stream.Collectors;

public class Useful {
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> colorize(List<String> strings) {
        return strings.stream().map(Useful::colorize).collect(Collectors.toList());
    }

    public static String replace(String string, String key, String value) {
        return string.replace(key, value);
    }

    public static List<String> replace(List<String> strings, String key, String value) {
        return strings.stream().map(string -> replace(string, key, value)).collect(Collectors.toList());
    }

    public static String serializeLocation(Location location) {
        if (location == null) return null;
        return location.getWorld().getName() + ":" + location.getX() + ":" + location.getY() + ":" + location.getZ() + ":" + location.getYaw() + ":" + location.getPitch();
    }

    public static Location deserializeLocation(String string) {
        if (string == null) return null;
        String[] split = string.split(":");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
}
