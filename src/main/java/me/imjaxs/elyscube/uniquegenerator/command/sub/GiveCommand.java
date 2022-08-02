package me.imjaxs.elyscube.uniquegenerator.command.sub;

import me.imjaxs.elyscube.uniquegenerator.UniqueGenerator;
import me.imjaxs.elyscube.uniquegenerator.command.SubCommand;
import me.imjaxs.elyscube.uniquegenerator.objects.level.Level;
import me.imjaxs.elyscube.uniquegenerator.objects.template.Template;
import me.imjaxs.elyscube.uniquegenerator.tools.file.FileResource;
import me.imjaxs.elyscube.uniquegenerator.tools.message.ChatMessage;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand extends SubCommand {
    private final UniqueGenerator plugin = UniqueGenerator.getInstance();
    private final FileResource messages = UniqueGenerator.getInstance().getFileMessages();

    @Override
    public String getPermission() {
        return "uniquegenerator.use.give";
    }

    @Override
    public String getUsage() {
        return "/generators give <player> <template> <level>";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length != 3)
            return false;

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            String message = this.messages.getFileConfiguration().getString("no-player");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        Template template = this.plugin.getGenerators().getTemplate(args[1].toLowerCase());
        if (template == null) {
            String message = this.messages.getFileConfiguration().getString("no-generator-exists");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        if (!NumberUtils.isNumber(args[2]))
            return false;
        int levelInteger = Integer.parseInt(args[2]);

        Level level = template.getLevel(levelInteger);
        if (level == null) {
            String message = this.messages.getFileConfiguration().getString("no-generator-level");
            if (message != null && !message.isEmpty())
                new ChatMessage(message).send(sender);
            return true;
        }

        if (target.getInventory().firstEmpty() == -1) {
            target.getWorld().dropItem(target.getLocation(), template.createItem(level));
        } else {
            target.getInventory().addItem(template.createItem(level));
        }

        String message = this.messages.getFileConfiguration().getString("give-generator");
        if (message != null && !message.isEmpty())
            new ChatMessage(message)
                    .replace("%generator_name%", template.getName())
                    .replace("%player_name%", target.getName())
                    .send(sender);
        return true;
    }
}
