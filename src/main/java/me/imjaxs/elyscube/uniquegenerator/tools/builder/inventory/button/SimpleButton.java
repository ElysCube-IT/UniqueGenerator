package me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory.button;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.imjaxs.elyscube.uniquegenerator.events.SimpleClickEvent;
import me.imjaxs.elyscube.uniquegenerator.tools.Useful;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
public class SimpleButton {
    @Getter private final ItemStack itemStack;
    @Getter private final int slot;
    @Getter private final boolean fill;
    private final Consumer<SimpleClickEvent> event;

    public void call(SimpleClickEvent event) {
        this.event.accept(event);
    }

    public static class Builder {
        private ItemStack itemStack;
        private ItemMeta itemMeta;
        private int slot = -1;
        private boolean fill = false;
        private Consumer<SimpleClickEvent> event = null;

        public static Builder create() {
            return new Builder();
        }

        public Builder of(SimpleButton button) {
            this.itemStack = button.getItemStack();
            this.itemMeta = this.itemStack.getItemMeta();

            return this;
        }

        public Builder of(ItemStack itemStack) {
            this.itemStack = itemStack;
            this.itemMeta = this.itemStack.getItemMeta();

            return this;
        }

        public Builder of(Material material, short data) {
            return this.of(new ItemStack(material, 1, data));
        }

        public Builder of(Material material) {
            return this.of(material, (short) 0);
        }

        public Builder setMaterial(Material material) {
            this.itemStack.setType(material);
            return this;
        }

        public Builder setAmount(int amount) {
            this.itemStack.setAmount(amount);
            return this;
        }

        public Builder setData(short data) {
            this.itemStack.setDurability(data);
            return this;
        }

        public Builder setName(String name) {
            this.itemMeta.setDisplayName(Useful.colorize(name));
            return this;
        }

        public Builder setLore(List<String> lore) {
            this.itemMeta.setLore(Useful.colorize(lore));
            return this;
        }

        public Builder setSlot(int slot) {
            this.slot = slot;
            return this;
        }

        public Builder setFill(boolean fill) {
            this.fill = fill;
            return this;
        }

        public Builder setEvent(Consumer<SimpleClickEvent> event) {
            this.event = event;
            return this;
        }

        public Builder build() {
            this.itemStack.setItemMeta(this.itemMeta);
            return this;
        }

        public SimpleButton toSimpleButton() {
            return new SimpleButton(this.itemStack, this.slot, this.fill, this.event);
        }
    }
}
