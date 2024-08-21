package org.network.dg32z;

import org.network.dg32z.command.WhitelistCommand;
import org.network.dg32z.listener.EventListener;
import org.network.dg32z.util.VanillaWhitelistFile;
import org.network.dg32z.util.YamlConfig;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;

public final class BungeeWhitelist extends Plugin implements Listener {

    private YamlConfig yamlConfig;
    private VanillaWhitelistFile vanillaWhitelistFile;

    @Override
    public void onEnable() {
        yamlConfig = new YamlConfig("config.yml", this);
        yamlConfig.saveDefaultConfig();

        getProxy().getPluginManager().registerListener(this, new EventListener(this));
        getProxy().getPluginManager().registerCommand(this, new WhitelistCommand(this));
        vanillaWhitelistFile = new VanillaWhitelistFile(this);
    }

    @Override
    public void onDisable() {
        getProxy().getPluginManager().unregisterListeners(this);
        getProxy().getPluginManager().unregisterCommands(this);

        saveConfig();
    }

    public void saveConfig() {
        try {
            yamlConfig.saveConfig();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration getConfig() {
        return yamlConfig.getConfig();
    }

    public VanillaWhitelistFile getVanillaWhitelistFile() {
        return vanillaWhitelistFile;
    }

}
