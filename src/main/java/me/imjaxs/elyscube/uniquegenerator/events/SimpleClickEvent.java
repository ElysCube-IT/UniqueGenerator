package me.imjaxs.elyscube.uniquegenerator.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory.SimpleInventory;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory.button.SimpleButton;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@AllArgsConstructor
public class SimpleClickEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    @Getter private Player player;
    @Getter private SimpleInventory inventory;
    @Getter private SimpleButton button;

    public HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
