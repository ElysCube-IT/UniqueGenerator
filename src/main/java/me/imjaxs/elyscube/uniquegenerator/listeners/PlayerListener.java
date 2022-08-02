package me.imjaxs.elyscube.uniquegenerator.listeners;

import me.imjaxs.elyscube.uniquegenerator.UniqueGenerator;
import me.imjaxs.elyscube.uniquegenerator.objects.Generator;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {
    private final UniqueGenerator plugin = UniqueGenerator.getInstance();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK)
            return;

        Block block = event.getClickedBlock();
        if (block == null)
            return;

        Generator generator = this.plugin.getGenerators().getGenerator(block.getLocation());
        if (generator == null)
            return;
        event.setCancelled(true);

        if (!generator.getUniqueID().equals(player.getUniqueId()))
            return;

        player.openInventory(generator.getInventory());
    }
}
