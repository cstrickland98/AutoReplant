package net.ddns.gamersanonymous.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class CommandPluginToggle implements CommandExecutor
{
    FileConfiguration config;

    public CommandPluginToggle(FileConfiguration config)
    {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        if (commandSender.hasPermission("autoreplant.toggle"))
        {
            if (config.getBoolean("enable"))
            {
                config.set("enable", false);
                commandSender.sendMessage("[AutoReplant] Replanting disabled.");
                return true;
            } else
            {
                config.set("enable", true);
                commandSender.sendMessage("[AutoReplant] Replanting enabled.");
                return true;
            }
        }
        commandSender.sendMessage("You do not have the required permissions!");
        return false;
    }
}
