package org.network.dg32z.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.network.dg32z.BungeeWhitelist;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public final class VanillaWhitelistFile {

    private final BungeeWhitelist plugin;

    public VanillaWhitelistFile(final BungeeWhitelist plugin) {
        this.plugin = plugin;
    }

    public List<VanillaWhitelist> read() {
        final Gson gson = new Gson();

        try (final FileReader fileReader = new FileReader(plugin.getDataFolder() + "/whitelist.json")) {
            final Type listType = new TypeToken<List<VanillaWhitelist>>() {
            }.getType();

            return gson.fromJson(fileReader, listType);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException("没有找到目录下的 whitelist.json", e);
        } catch (final IOException e) {
            throw new RuntimeException("读取 whitelist.json 文件时出现错误", e);
        }
    }

}
