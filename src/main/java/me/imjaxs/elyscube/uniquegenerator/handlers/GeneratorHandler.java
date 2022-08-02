package me.imjaxs.elyscube.uniquegenerator.handlers;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import me.imjaxs.elyscube.uniquegenerator.UniqueGenerator;
import me.imjaxs.elyscube.uniquegenerator.enums.ValueType;
import me.imjaxs.elyscube.uniquegenerator.objects.Generator;
import me.imjaxs.elyscube.uniquegenerator.objects.level.Level;
import me.imjaxs.elyscube.uniquegenerator.objects.template.Template;
import me.imjaxs.elyscube.uniquegenerator.tools.Useful;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.ItemBuilder;
import me.imjaxs.elyscube.uniquegenerator.tools.file.FileResource;
import me.imjaxs.elyscube.uniquegenerator.workload.GenCheckTask;
import me.imjaxs.elyscube.uniquegenerator.workload.abstraction.WorkloadThread;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class GeneratorHandler {
    private final UniqueGenerator plugin = UniqueGenerator.getInstance();
    private final FileResource generators = UniqueGenerator.getInstance().getFileGenerators();
    private final WorkloadThread workloadThread;
    private final GenCheckTask genCheckTask;
    private final Map<String, Template> loadedTemplates = Maps.newHashMap();
    private final Map<Location, Generator> onlineGenerators = Maps.newHashMap();

    public GeneratorHandler() {
        this.workloadThread = new WorkloadThread();
        Bukkit.getScheduler().runTaskTimer(this.plugin, this.workloadThread, 100L, 20L);

        this.genCheckTask = new GenCheckTask(this.workloadThread, this);
        this.genCheckTask.runTaskTimerAsynchronously(this.plugin, 100L, 20L);
    }

    public boolean isGenerator(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        return itemBuilder.getNBTBoolean("generator") && this.loadedTemplates.containsKey(itemBuilder.getNBTString("template"));
    }

    public Template getTemplate(String name) {
        return this.loadedTemplates.get(name.toLowerCase());
    }

    public Template getTemplate(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        return this.loadedTemplates.get(itemBuilder.getNBTString("template"));
    }

    public Generator getGenerator(Location location) {
        return this.onlineGenerators.get(location);
    }

    public Level getGeneratorLevel(ItemStack itemStack) {
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        return this.getTemplate(itemStack).getLevel(itemBuilder.getNBTInteger("level"));
    }

    public List<Generator> getGenerators(UUID uniqueID) {
        return this.onlineGenerators.values().stream().filter(generator -> generator.getUniqueID().equals(uniqueID)).collect(Collectors.toList());
    }

    public void insertGenerator(Generator generator) {
        this.onlineGenerators.put(generator.getLocation(), generator);
    }

    public void removeGenerator(Generator generator) {
        //
        this.onlineGenerators.remove(generator.getLocation());
    }

    public void loadFromConfig() {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            ConfigurationSection sections = this.plugin.getConfig().getConfigurationSection("generators");

            sections.getKeys(false).forEach(name -> {
                try {
                    ConfigurationSection section = sections.getConfigurationSection(name);

                    ValueType value = ValueType.valueOf(section.getString("info.value.type"));
                    double amount = section.getDouble("info.value.amount");

                    List<Level> levels = new ArrayList<>();
                    ConfigurationSection levelsSection = section.getConfigurationSection("info.levels");

                    levelsSection.getKeys(false).forEach(integer -> {
                        ConfigurationSection levelSection = levelsSection.getConfigurationSection(integer);
                        levels.add(new Level(Integer.parseInt(integer), levelSection.getDouble("money-price"), levelSection.getInt("speed")));
                    });

                    String itemHead = section.getString("item.headID");
                    String itemName = section.getString("item.name");
                    List<String> itemLore = section.getStringList("item.lore");

                    this.loadedTemplates.put(name.toLowerCase(), new Template(name.toLowerCase(), value, amount, levels, itemHead, itemName, itemLore));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
        Bukkit.getScheduler().runTaskLater(this.plugin, this::loadSavedGenerators, 20);
    }

    public void loadSavedGenerators() {
        Set<Generator> cache = Sets.newHashSet();

        FileConfiguration configuration = this.generators.getFileConfiguration();
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            ConfigurationSection sections = configuration.getConfigurationSection("generators");
            if (sections != null) {
                for (String generatorUUID : sections.getKeys(false)) {
                    ConfigurationSection section = sections.getConfigurationSection(generatorUUID);
                    UUID uniqueID = UUID.fromString(section.getString("owner"));

                    Template template = this.getTemplate(section.getString("template"));
                    if (template == null) continue;

                    Level level = template.getLevel(section.getInt("level"));
                    if (level == null) continue;

                    double storage = section.getDouble("storage");
                    Location location = Useful.deserializeLocation(section.getString("location"));

                    cache.add(new Generator(uniqueID, location, template, level, storage));
                }
            }
            Bukkit.getScheduler().runTask(this.plugin, () -> cache.forEach(this::insertGenerator));
        });
    }

    public void saveOnlineGenerators() {
        FileConfiguration configuration = this.generators.getFileConfiguration();

        configuration.set("generators", null);
        this.onlineGenerators.values().forEach(generator -> {
            configuration.set("generators." + generator.getGeneratorUniqueID() + ".owner", generator.getUniqueID().toString());
            configuration.set("generators." + generator.getGeneratorUniqueID() + ".template", generator.getTemplate().getName());
            configuration.set("generators." + generator.getGeneratorUniqueID() + ".level", generator.getLevel().getInteger());
            configuration.set("generators." + generator.getGeneratorUniqueID() + ".storage", generator.getStorage());
            configuration.set("generators." + generator.getGeneratorUniqueID() + ".location", Useful.serializeLocation(generator.getLocation()));
        });
        this.generators.save();
    }
}
