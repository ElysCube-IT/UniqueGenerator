package me.imjaxs.elyscube.uniquegenerator.tools.message;

import me.imjaxs.elyscube.uniquegenerator.tools.Useful;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ChatMessage {
    private String message;
    private List<String> messages;

    public ChatMessage(String message) {
        this.message = message;
        this.messages = new ArrayList<>();
    }

    public ChatMessage(List<String> messages) {
        this.message = "";
        this.messages = messages;
    }

    public ChatMessage replace(String key, String value) {
        if (!this.message.isEmpty())
            this.message = Useful.replace(this.message, key, value);

        if (!this.messages.isEmpty())
            this.messages = Useful.replace(this.messages, key, value);
        return this;
    }

    public void send(CommandSender sender) {
        if (!this.message.isEmpty())
            sender.sendMessage(Useful.colorize(this.message));

        if (!this.messages.isEmpty())
            this.messages.forEach(message -> sender.sendMessage(Useful.colorize(message)));
    }
}
