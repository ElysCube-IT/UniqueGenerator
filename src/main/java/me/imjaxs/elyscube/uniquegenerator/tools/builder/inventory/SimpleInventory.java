package me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory;

import me.imjaxs.elyscube.uniquegenerator.tools.Useful;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory.button.SimpleButton;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;

public class SimpleInventory implements InventoryHolder {
    private final Inventory inventory;
    private final List<SimpleButton> buttons;

    public SimpleInventory(InventoryType type, String title) {
        this.inventory = Bukkit.createInventory(this, type, Useful.colorize(title));
        this.buttons = new ArrayList<>();
    }

    public SimpleInventory(int rows, String title) {
        this.inventory = Bukkit.createInventory(this, rows > 6 ? 6 * 9 : rows * 9, Useful.colorize(title));
        this.buttons = new ArrayList<>();
    }

    public SimpleInventory addButton(SimpleButton button) {
        this.buttons.add(button);
        return this;
    }

    public SimpleButton getButton(int slot) {
        for (SimpleButton button : this.buttons) {
            if (button.getSlot() != -1 && (button.getSlot() == slot || button.isFill()))
                return button;
        }
        return null;
    }

    public void update() {
        for (SimpleButton button : this.buttons) {
            if (button.isFill()) {
                for (int i = 0; i < this.inventory.getSize(); i++)
                    this.inventory.setItem(i, button.getItemStack());
            } else if (button.getSlot() != -1)
                this.inventory.setItem(button.getSlot(), button.getItemStack());
        }
    }

    @Override
    public Inventory getInventory() {
        this.update();
        return this.inventory;
    }
}
