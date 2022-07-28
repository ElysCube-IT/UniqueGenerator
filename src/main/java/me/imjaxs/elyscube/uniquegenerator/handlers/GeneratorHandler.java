package me.imjaxs.elyscube.uniquegenerator.handlers;

import com.google.common.collect.Maps;
import me.imjaxs.elyscube.uniquegenerator.objects.Generator;
import me.imjaxs.elyscube.uniquegenerator.objects.template.Template;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class GeneratorHandler {
    private final Map<String, Template> loadedTemplates = Maps.newHashMap();
    private final Map<Location, Generator> onlineGenerators = Maps.newHashMap();


    public boolean isGenerator(ItemStack item) {
        //
        return false;
    }

    public Template getTemplate(ItemStack item) {
        //
        return null;
    }

    public Generator getGenerator(Location location) {
        return this.onlineGenerators.get(location);
    }

    public void insertGenerator(Generator generator) {
        this.onlineGenerators.put(generator.getLocation(), generator);
    }

    public void removeGenerator(Generator generator) {
        //
        this.onlineGenerators.remove(generator.getLocation());
    }
}
