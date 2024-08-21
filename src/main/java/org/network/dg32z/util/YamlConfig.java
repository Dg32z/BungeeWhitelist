package org.network.dg32z.util;

import org.network.dg32z.BungeeWhitelist;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class YamlConfig {

    private final File configFile, folder;
    private final String fileName;
    private final BungeeWhitelist plugin;

    private static Configuration config;

    public YamlConfig(final String fileName, final BungeeWhitelist plugin) {
        this.plugin = plugin;
        this.fileName = fileName;
        folder = plugin.getDataFolder();
        configFile = new File(folder, fileName);
    }

    public void saveDefaultConfig() {
        if (!folder.exists()) folder.mkdirs();

        try {
            if (!configFile.exists()) Files.copy(plugin.getResourceAsStream(fileName), configFile.toPath());

            loadConfig();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig() throws IOException {
        config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
    }

    public void saveConfig() throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
    }

    public Configuration getConfig() {
        return config;
    }

}