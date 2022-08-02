package me.imjaxs.elyscube.uniquegenerator.objects;

import lombok.Getter;
import me.imjaxs.elyscube.uniquegenerator.UniqueGenerator;
import me.imjaxs.elyscube.uniquegenerator.enums.ValueType;
import me.imjaxs.elyscube.uniquegenerator.objects.level.Level;
import me.imjaxs.elyscube.uniquegenerator.objects.template.Template;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory.SimpleInventory;
import me.imjaxs.elyscube.uniquegenerator.tools.builder.inventory.button.SimpleButton;
import me.imjaxs.elyscube.uniquegenerator.tools.file.FileResource;
import me.imjaxs.elyscube.uniquegenerator.tools.message.ChatMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
import java.util.UUID;

public class Generator {
    private final UniqueGenerator plugin = UniqueGenerator.getInstance();
    private final FileResource messages = UniqueGenerator.getInstance().getFileMessages();

    @Getter private final UUID generatorUniqueID;
    @Getter private final UUID uniqueID;
    @Getter private final Location location;
    @Getter private final Template template;
    @Getter private Level level;
    @Getter private double storage;
    @Getter private long lastDepositTime;

    public Generator(UUID uniqueID, Location location, Template template, Level level, double storage) {
        this.generatorUniqueID = UUID.randomUUID();
        this.uniqueID = uniqueID;
        this.location = location;
        this.template = template;
        this.level = level;
        this.storage = storage;

        this.lastDepositTime = 0;
    }

    public void deposit() {
        this.storage += this.template.getAmount();
        this.lastDepositTime = System.currentTimeMillis();
    }

    public void withdraw(Player player) {
        if (this.template.getValue() == ValueType.MONEY)
            this.plugin.getVaultAPI().depositPlayer(player, this.storage);

        if (this.template.getValue() == ValueType.TOKEN)
            this.plugin.getTokenAPI().addTokens(player, this.storage);
        this.storage = 0.0;
    }

    public Inventory getInventory() {
        SimpleInventory inventory = new SimpleInventory(3, "» Generator")
                .addButton(
                        SimpleButton.Builder.create()
                                .of(Material.STAINED_GLASS_PANE, (short) 15)
                                .setFill(true)
                                .build().toSimpleButton()
                )
                .addButton(
                        SimpleButton.Builder.create()
                                .of(this.plugin.getHeadAPI().getItemHead("53144"))
                                .setName("&b&l» UPGRADE GENERATORE ")
                                .setLore(
                                        Arrays.asList(
                                                "&fCliccami per aumentare il livello del generatore.     ",
                                                "&fAl momento è di livello &b" + this.level.getInteger() + "&f.",
                                                "",
                                                this.template.getLevel(this.level.getInteger() + 1) == null ? "&cIl generatore ha raggiunto il livello massimo.     " : "&fIl prezzo dell'upgrade è di &b" + this.template.getLevel(this.level.getInteger() + 1).getPrice() + "$&f.    "
                                        )
                                )
                                .setSlot(10)
                                .setEvent(event -> {
                                    Player player = event.getPlayer();

                                    Level upgradeLevel = this.template.getLevel(this.level.getInteger() + 1);
                                    if (upgradeLevel == null)
                                        return;

                                    if (this.plugin.getVaultAPI().getBalance(player) < upgradeLevel.getPrice()) {
                                        String message = this.messages.getFileConfiguration().getString("no-money");
                                        if (message != null && !message.isEmpty())
                                            new ChatMessage(message).send(player);
                                        return;
                                    }
                                    this.plugin.getVaultAPI().withdrawPlayer(player, upgradeLevel.getPrice());
                                    this.level = upgradeLevel;

                                    String message = this.messages.getFileConfiguration().getString("upgrade-success");
                                    if (message != null && !message.isEmpty())
                                        new ChatMessage(message)
                                                .replace("%generator_level%", String.valueOf(this.level.getInteger())).send(player);
                                    player.openInventory(this.getInventory());
                                })
                                .build().toSimpleButton()
                ).addButton(
                        SimpleButton.Builder.create()
                                .of(this.plugin.getHeadAPI().getItemHead("30724"))
                                .setName("&b&l» RITIRA RISORSE ")
                                .setLore(
                                        Arrays.asList(
                                                "&fCliccami per ritirare le risorse prodotte dal generatore.     ",
                                                "&fAl momento ci sono &b" + this.storage + (this.template.getValue() == ValueType.MONEY ? "$&f." : " &ftoken."),
                                                ""
                                        )
                                )
                                .setSlot(13)
                                .setEvent(event -> {
                                    Player player = event.getPlayer();

                                    if (this.storage == 0.0)
                                        return;

                                    String message = this.messages.getFileConfiguration().getString("withdraw-success");
                                    if (message != null && !message.isEmpty())
                                        new ChatMessage(message)
                                                .replace("%generator_storage%", this.template.getValue() == ValueType.MONEY ? String.valueOf(this.storage) : String.valueOf(Integer.valueOf((int) this.storage)))
                                                .replace("%generator_value%", this.template.getValue() == ValueType.MONEY ? "$" : " &ftoken")
                                                .send(player);
                                    this.withdraw(player);

                                    player.openInventory(this.getInventory());
                                })
                                .build().toSimpleButton()
                ).addButton(
                        SimpleButton.Builder.create()
                                .of(this.plugin.getHeadAPI().getItemHead("51776"))
                                .setName("&b&l» RIMUOVI GENERATORE ")
                                .setLore(
                                        Arrays.asList(
                                                "&fCliccami per rimuovere il generatore.",
                                                "&fRicorda di avere un slot libero altrimenti verrà droppato.    ",
                                                ""
                                        )
                                )
                                .setSlot(16)
                                .setEvent(event -> {
                                    Player player = event.getPlayer();
                                    player.closeInventory();

                                    this.plugin.getGenerators().removeGenerator(this);
                                    this.location.getBlock().setType(Material.AIR);

                                    if (player.getInventory().firstEmpty() == -1) {
                                        player.getWorld().dropItem(player.getLocation(), template.createItem(level));
                                    } else {
                                        player.getInventory().addItem(this.template.createItem(this.level));
                                    }

                                    String message = this.messages.getFileConfiguration().getString("break-success");
                                    if (message != null && !message.isEmpty())
                                        new ChatMessage(message).send(player);
                                })
                                .build().toSimpleButton()
                );
        return inventory.getInventory();
    }
}
