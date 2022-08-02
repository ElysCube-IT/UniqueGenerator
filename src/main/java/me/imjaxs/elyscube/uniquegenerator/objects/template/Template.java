package me.imjaxs.elyscube.uniquegenerator.objects.template;

import lombok.Getter;
import me.imjaxs.elyscube.uniquegenerator.UniqueGenerator;
import me.imjaxs.elyscube.uniquegenerator.enums.ValueType;
import me.imjaxs.elyscube.uniquegenerator.objects.level.Level;
import me.imjaxs.elyscube.uniquegenerator.tools.Useful;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Template {
    @Getter private final String name;
    @Getter private final ValueType value;
    @Getter private final double amount;
    @Getter private final List<Level> levels;

    private final String itemHead;
    private final String itemName;
    private final List<String> itemLore;

    public Template(String name, ValueType value, double amount, List<Level> levels, String itemHead, String itemName, List<String> itemLore) {
        this.name = name;
        this.value = value;
        this.amount = amount;
        this.levels = levels;

        this.itemHead = itemHead;
        this.itemName = itemName;
        this.itemLore = itemLore;
    }

    public Level getLevel(int integer) {
        for (Level level : this.levels) {
            if (level.getInteger() == integer)
                return level;
        }
        return null;
    }

    public ItemStack createItem(Level level) {
        ItemBuilder itemBuilder = new ItemBuilder(UniqueGenerator.getInstance().getHeadAPI().getItemHead(this.itemHead))
                .setNBTBoolean("generator", true)
                .setNBTString("template", this.name)
                .setNBTInteger("level", level.getInteger())
                .setName(this.itemName)
                .setLore(Useful.replace(new ArrayList<>(this.itemLore), "%generator_level%", String.valueOf(level.getInteger())));
        return itemBuilder.build().toItemStack();
    }
}
