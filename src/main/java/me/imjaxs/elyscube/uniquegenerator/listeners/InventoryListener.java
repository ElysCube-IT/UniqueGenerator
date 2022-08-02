package me.imjaxs.elyscube.uniquegenerator.listeners;

import me.imjaxs.elyscube.uniquegenerator.events.SimpleClickEvent;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory.SimpleInventory;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory.button.SimpleButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (player.getOpenInventory().getTopInventory() == null)
            return;

        InventoryHolder openHolder = player.getOpenInventory().getTopInventory().getHolder();
        if (openHolder instanceof SimpleInventory && event.getClickedInventory().equals(player.getInventory())) {
            event.setCancelled(true);
            return;
        }

        InventoryHolder holder = event.getClickedInventory().getHolder();
        if (holder instanceof SimpleInventory) {
            event.setCancelled(true);

            SimpleInventory simpleInventory = (SimpleInventory) holder;
            SimpleButton simpleButton = simpleInventory.getButton(event.getSlot());

            if (simpleButton == null || simpleButton.isFill())
                return;

            SimpleClickEvent simpleClickEvent = new SimpleClickEvent(player, simpleInventory, simpleButton);
            Bukkit.getServer().getPluginManager().callEvent(simpleClickEvent);

            simpleButton.call(simpleClickEvent);
        }
    }
}
