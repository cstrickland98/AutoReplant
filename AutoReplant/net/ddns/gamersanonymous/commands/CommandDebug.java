package net.ddns.gamersanonymous.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class CommandDebug implements CommandExecutor
{
    FileConfiguration config;

    public CommandDebug(FileConfiguration config)
    {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        if(commandSender.hasPermission("autoreplant.debug.toggle"))
        {
            if (config.getBoolean("debug"))
            {
                config.set("debug", false);
                commandSender.sendMessage("[AutoReplant] Debugging turned off.");
                return true;
            } else
            {
                config.set("debug", true);
                commandSender.sendMessage("[AutoReplant] Debugging turned on.");
                return true;
            }
        }
        commandSender.sendMessage("You do not have the required permissions!");
        return false;
    }
}
