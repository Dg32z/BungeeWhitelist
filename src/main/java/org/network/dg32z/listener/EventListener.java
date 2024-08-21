package org.network.dg32z.listener;

import org.network.dg32z.BungeeWhitelist;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public final class EventListener implements Listener {

    private final BungeeWhitelist plugin;

    public EventListener(final BungeeWhitelist plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLogin(final LoginEvent event) {
        if (!plugin.getConfig().getBoolean("enabled")) return;

        final List<String> whitelist = plugin.getConfig().getStringList("whitelist");

        final String name = event.getConnection().getName();

        for (final String each : whitelist) if (each.equals(name)) return;

        event.setCancelled(true);
        event.setCancelReason(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("kick_message")));
    }

}
