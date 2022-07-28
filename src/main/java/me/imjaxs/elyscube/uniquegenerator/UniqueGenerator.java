package me.imjaxs.elyscube.uniquegenerator;

import com.intellectualcrafters.plot.api.PlotAPI;
import com.vk2gpz.tokenenchant.api.TokenEnchantAPI;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class UniqueGenerator extends JavaPlugin {
    @Getter private static UniqueGenerator instance;

    @Getter private PlotAPI plotAPI;
    @Getter private Economy vaultAPI;
    @Getter private TokenEnchantAPI tokenAPI;

    @Override
    public void onEnable() {
        instance = this;

        if (!this.setupPlot() || !this.setupEconomy() || !this.setupToken())
            this.getServer().getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
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
}
