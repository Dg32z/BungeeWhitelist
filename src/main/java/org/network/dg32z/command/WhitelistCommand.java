package org.network.dg32z.command;

import net.md_5.bungee.api.plugin.TabExecutor;
import org.network.dg32z.BungeeWhitelist;
import org.network.dg32z.util.VanillaWhitelist;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class WhitelistCommand extends Command implements TabExecutor {

    private final BungeeWhitelist plugin;

    public WhitelistCommand(final BungeeWhitelist plugin) {
        super("whitelist");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GRAY + "使用 " + ChatColor.WHITE + "/whitelist help" + ChatColor.GRAY + " 查看可用命令!");
            return;
        }

        final String cmd = args[0].toLowerCase();

        switch (cmd) {
            case "on":
            case "off":
                if (!sender.hasPermission("whitelist.control.startoroff")) {
                    sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限!");
                    return;
                }

                plugin.getConfig().set("enabled", cmd.equals("on"));
                plugin.saveConfig();

                sender.sendMessage(cmd.equals("on") ? ChatColor.GREEN + "白名单已开启!" : ChatColor.RED + "白名单已关闭!");
                break;

            case "add":
                if (!sender.hasPermission("whitelist.control.add")) {
                    sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限!");
                    return;
                }

                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "用法: /whitelist add <玩家名>");
                    return;
                }

                final List<String> whitelistAdd = plugin.getConfig().getStringList("whitelist");
                if (whitelistAdd.contains(args[1])) {
                    sender.sendMessage(ChatColor.RED + "玩家 " + args[1] + " 已在白名单内了!");
                    return;
                }

                whitelistAdd.add(args[1]);
                plugin.getConfig().set("whitelist", whitelistAdd);
                plugin.saveConfig();

                sender.sendMessage(ChatColor.GRAY + "已将 " + ChatColor.WHITE + args[1] + " " + ChatColor.GREEN + "加入" + ChatColor.GRAY + " 白名单");
                break;

            case "remove":
                if (!sender.hasPermission("whitelist.control.remove")) {
                    sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限!");
                    return;
                }

                if (args.length != 2) {
                    sender.sendMessage(ChatColor.RED + "用法: /whitelist remove <玩家名>");
                    return;
                }

                final List<String> whitelistRemove = plugin.getConfig().getStringList("whitelist");
                if (!whitelistRemove.contains(args[1])) {
                    sender.sendMessage(ChatColor.RED + "玩家 " + args[1] + " 不在白名单内!");
                    return;
                }

                whitelistRemove.remove(args[1]);
                plugin.getConfig().set("whitelist", whitelistRemove);
                plugin.saveConfig();

                sender.sendMessage(ChatColor.GRAY + "已将 " + ChatColor.WHITE + args[1] + " " + ChatColor.RED + "移出" + ChatColor.GRAY + " 白名单");
                break;

            case "list":
                if (!sender.hasPermission("whitelist.control.list")) {
                    sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限!");
                    return;
                }

                final List<String> whitelistList = plugin.getConfig().getStringList("whitelist");
                sender.sendMessage(ChatColor.WHITE + "白名单内的玩家: " + ChatColor.GRAY + whitelistList);
                break;

            case "addfromvanilla":
                if (!sender.hasPermission("whitelist.control.import")) {
                    sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限!");
                    return;
                }

                final List<VanillaWhitelist> vanillaWhitelists = plugin.getVanillaWhitelistFile().read();
                final List<String> whitelistImport = plugin.getConfig().getStringList("whitelist");

                for (final VanillaWhitelist each : vanillaWhitelists) {
                    final String name = each.getName();

                    if (whitelistImport.contains(name)) {
                        sender.sendMessage(ChatColor.RED + "玩家 " + name + " 已在白名单内了!");
                        continue;
                    }

                    whitelistImport.add(name);
                    sender.sendMessage(ChatColor.GREEN + "已将 " + name + " 添加进白名单!");
                }

                plugin.getConfig().set("whitelist", whitelistImport);
                plugin.saveConfig();
                break;

            case "reload":
                if (!sender.hasPermission("whitelist.reload")) {
                    sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限!");
                    return;
                }

                plugin.saveConfig();
                sender.sendMessage(ChatColor.GREEN + "配置文件已重新加载!");
                break;

            case "help":
                if (!sender.hasPermission("whitelist.control.help")) {
                    sender.sendMessage(ChatColor.RED + "你没有使用该命令的权限!");
                    return;
                }

                sender.sendMessage(ChatColor.GRAY + "可用命令: "
                        + ChatColor.WHITE + "\n /whitelist on/off: " + ChatColor.GRAY + "开/关白名单"
                        + ChatColor.WHITE + "\n /whitelist add/remove: " + ChatColor.GRAY + "添加/移出白名单内的玩家"
                        + ChatColor.WHITE + "\n /whitelist list: " + ChatColor.GRAY + "查看白名单内的所有玩家"
                        + ChatColor.WHITE + "\n /whitelist addfromvanilla: " + ChatColor.GRAY + "从原版白名单文件中导入至本插件(把文件放到本插件目录内)"
                        + ChatColor.WHITE + "\n /whitelist reload: " + ChatColor.GRAY + "重新加载配置文件"
                );
                break;

            default:
                sender.sendMessage(ChatColor.RED + "未知的指令! 使用 /whitelist help 查看帮助!");
                break;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("remove") && sender.hasPermission("whitelist.control.remove")) {
            final List<String> whitelist = plugin.getConfig().getStringList("whitelist");
            return whitelist.stream()
                    .filter(name -> name.startsWith(args[1]))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}