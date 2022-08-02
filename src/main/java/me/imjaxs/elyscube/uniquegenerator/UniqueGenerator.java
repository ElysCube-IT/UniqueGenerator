package me.imjaxs.elyscube.uniquegenerator;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import lombok.Getter;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.imjaxs.elyscube.uniquegenerator.command.CommandManager;
import me.imjaxs.elyscube.uniquegenerator.handlers.GeneratorHandler;
import me.imjaxs.elyscube.uniquegenerator.listeners.BlockListener;
import me.imjaxs.elyscube.uniquegenerator.listeners.InventoryListener;
import me.imjaxs.elyscube.uniquegenerator.listeners.PlayerListener;
import me.imjaxs.elyscube.uniquegenerator.tools.file.FileResource;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class UniqueGenerator extends JavaPlugin implements Listener {
    @Getter private static UniqueGenerator instance;

    @Getter private HeadDatabaseAPI headAPI;
    @Getter private PlotAPI plotAPI;
    @Getter private Economy vaultAPI;
    @Getter private TokenEnchantAPI tokenAPI;

    @Getter private FileResource fileMessages;
    @Getter private FileResource fileGenerators;

    @Getter private GeneratorHandler generators;

    @Override
    public void onEnable() {
        instance = this;

        this.saveDefaultConfig();
        if (!setupNBT() || !this.setupHead() || !this.setupPlot() || !this.setupEconomy() || !this.setupToken() || !this.setupMessages() || !this.setupGenerators())
            this.getServer().getPluginManager().disablePlugin(this);
        this.getServer().getPluginManager().registerEvents(this, this);

        this.generators = new GeneratorHandler();
        this.generators.loadFromConfig();

        this.getCommand("generators").setExecutor(new CommandManager());

        this.getServer().getPluginManager().registerEvents(new BlockListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        this.generators.saveOnlineGenerators();
    }

    @EventHandler
    public void onDBLoad(DatabaseLoadEvent event) {
        this.headAPI = new HeadDatabaseAPI();
    }

    private boolean setupNBT() {
        return this.getServer().getPluginManager().isPluginEnabled("NBTAPI");
    }

    private boolean setupHead() {
        return this.getServer().getPluginManager().isPluginEnabled("HeadDatabase");
    }

    private boolean setupPlot() {
        if (!this.getServer().getPluginManager().isPluginEnabled("PlotSquared"))
            return false;

        this.plotAPI = new PlotAPI();
        return true;
    }

    private boolean setupEconomy() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Vault"))
            return false;

        RegisteredServiceProvider<Economy> registration = getServer().getServicesManager().getRegistration(Economy.class);
        if (registration == null)
            return false;

        this.vaultAPI = registration.getProvider();
        return true;
    }

    private boolean setupToken() {
        if (!this.getServer().getPluginManager().isPluginEnabled("TokenEnchant"))
            return false;

        this.tokenAPI = TokenEnchantAPI.getInstance();
        return this.tokenAPI != null;
    }

    public boolean setupMessages() {
        this.fileMessages = new FileResource(this.getDataFolder(), "messages.yml");
        if (!this.fileMessages.exists())
            this.fileMessages.create();

        this.fileMessages.load();
        return this.fileMessages.exists();
    }

    public boolean setupGenerators() {
        this.fileGenerators = new FileResource(this.getDataFolder(), "generators.yml");
        if (!this.fileGenerators.exists())
            this.fileGenerators.create();

        this.fileGenerators.load();
        return this.fileGenerators.exists();
    }
}
