package me.imjaxs.elyscube.uniquegenerator.tools.builder;

import de.tr7zw.nbtapi.NBTItem;
import me.imjaxs.elyscube.uniquegenerator.tools.Useful;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemBuilder {
    private ItemStack itemStack;
    private ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder(Material material, short data) {
        this(new ItemStack(material, 1, data));
    }

    public ItemBuilder(Material material) {
        this(material, (short) 0);
    }

    public ItemBuilder setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setData(short data) {
        this.itemStack.setDurability(data);
        return this;
    }

    public String getName() {
        return this.itemMeta.getDisplayName();
    }

    public ItemBuilder setName(String name) {
        this.itemMeta.setDisplayName(Useful.colorize(name));
        return this;
    }

    public List<String> getLore() {
        return this.itemMeta.getLore();
    }

    public ItemBuilder setLore(List<String> lore) {
        this.itemMeta.setLore(Useful.colorize(lore));
        return this;
    }

    public String getNBTString(String key) {
        NBTItem nbtItem = new NBTItem(this.itemStack);
        return nbtItem.getString(key);
    }

    public ItemBuilder setNBTString(String key, String value) {
        NBTItem nbtItem = new NBTItem(this.itemStack);
        nbtItem.setString(key, value);

        this.itemStack = nbtItem.getItem();
        this.itemMeta = this.itemStack.getItemMeta();
        return this;
    }

    public int getNBTInteger(String key) {
        NBTItem nbtItem = new NBTItem(this.itemStack);
        return nbtItem.getInteger(key);
    }

    public ItemBuilder setNBTInteger(String key, int value) {
        NBTItem nbtItem = new NBTItem(this.itemStack);
        nbtItem.setInteger(key, value);

        this.itemStack = nbtItem.getItem();
        this.itemMeta = this.itemStack.getItemMeta();
        return this;
    }

    public boolean getNBTBoolean(String key) {
        NBTItem nbtItem = new NBTItem(this.itemStack);
        return nbtItem.getBoolean(key);
    }

    public ItemBuilder setNBTBoolean(String key, boolean value) {
        NBTItem nbtItem = new NBTItem(this.itemStack);
        nbtItem.setBoolean(key, value);

        this.itemStack = nbtItem.getItem();
        this.itemMeta = this.itemStack.getItemMeta();
        return this;
    }

    public ItemBuilder build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this;
    }

    public ItemStack toItemStack() {
        return this.itemStack;
    }
}
