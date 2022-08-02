package me.imjaxs.elyscube.uniquegenerator.listeners;

import com.intellectualcrafters.plot.object.Plot;
import me.imjaxs.elyscube.uniquegenerator.UniqueGenerator;
import me.imjaxs.elyscube.uniquegenerator.objects.Generator;
import me.imjaxs.elyscube.uniquegenerator.objects.level.Level;
import me.imjaxs.elyscube.uniquegenerator.objects.template.Template;
import me.imjaxs.elyscube.uniquegenerator.tools.file.FileResource;
import me.imjaxs.elyscube.uniquegenerator.tools.message.ChatMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlockListener implements Listener {
    private final UniqueGenerator plugin = UniqueGenerator.getInstance();
    private final FileResource messages = UniqueGenerator.getInstance().getFileMessages();

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        ItemStack itemStack = event.getItemInHand();
        if (itemStack == null)
            return;

        if (!this.plugin.getGenerators().isGenerator(itemStack))
            return;
        Location location = event.getBlock().getLocation();

        Plot plot = this.plugin.getPlotAPI().getPlot(location);
        if (plot == null) {
            event.setCancelled(true);

            String message = this.messages.getFileConfiguration().getString("no-plot");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(player);
            return;
        }

        if (!plot.isOwner(player.getUniqueId())) {
            event.setCancelled(true);

            String message = this.messages.getFileConfiguration().getString("no-plot-owner");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(player);
            return;
        }

        List<Generator> playerGenerators = this.plugin.getGenerators().getGenerators(player.getUniqueId());
        if (playerGenerators.size() == 10) {
            event.setCancelled(true);

            String message = this.messages.getFileConfiguration().getString("max-placed");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(player);
            return;
        }

        Template template = this.plugin.getGenerators().getTemplate(itemStack);
        if (template == null)
            return;

        Level level = this.plugin.getGenerators().getGeneratorLevel(itemStack);
        if (level == null)
            return;

        Generator generator = new Generator(player.getUniqueId(), location, template, level, 10000.0);
        this.plugin.getGenerators().insertGenerator(generator);

        String message = this.messages.getFileConfiguration().getString("place-success");
        if (message != null && !message.isEmpty())
            new ChatMessage(message).send(player);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Location location = event.getBlock().getLocation();

        Plot plot = this.plugin.getPlotAPI().getPlot(location);
        if (plot == null)
            return;

        Generator generator = this.plugin.getGenerators().getGenerator(location);
        if (generator == null)
            return;

        event.setCancelled(true);
    }
}
