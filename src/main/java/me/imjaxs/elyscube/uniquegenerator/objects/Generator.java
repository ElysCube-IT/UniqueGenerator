package me.imjaxs.elyscube.uniquegenerator.objects;

import lombok.Getter;
import me.imjaxs.elyscube.uniquegenerator.UniqueGenerator;
import me.imjaxs.elyscube.uniquegenerator.enums.ValueType;
import me.imjaxs.elyscube.uniquegenerator.objects.level.Level;
import me.imjaxs.elyscube.uniquegenerator.objects.template.Template;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Generator {
    private final UniqueGenerator plugin = UniqueGenerator.getInstance();

    @Getter private final UUID uniqueID;
    @Getter private final Location location;
    @Getter private final Template template;
    @Getter private final Level level;
    @Getter private double storage;
    private BukkitTask bukkitTask;

    public Generator(UUID uniqueID, Location location, Template template, Level level, double storage) {
        this.uniqueID = uniqueID;
        this.location = location;
        this.template = template;
        this.level = level;
        this.storage = storage;
    }

    public void withdraw(Player player) {
        if (this.template.getValue() == ValueType.MONEY) {
            // TODO
            this.plugin.getVaultAPI().depositPlayer(player, this.storage);
        }

        if (this.template.getValue() == ValueType.TOKEN) {
            // TODO
            this.plugin.getTokenAPI().addTokens(player, this.storage);
        }

        this.storage = 0.0;
    }

    public boolean isActive() {
        return this.bukkitTask != null && !Bukkit.getScheduler().isCurrentlyRunning(this.bukkitTask.getTaskId());
    }

    public void start() {
        this.bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            this.storage += this.template.getAmount();
        }, this.level.getSpeed() * 20L, this.level.getSpeed() * 20L);
    }

    public void stop() {
        if (isActive())
            this.bukkitTask.cancel();
    }
}
