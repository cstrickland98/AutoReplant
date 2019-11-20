package net.ddns.gamersanonymous.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class CommandFullyGrown implements CommandExecutor
{
    FileConfiguration config;

    public CommandFullyGrown(FileConfiguration config)
    {
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings)
    {
        if(commandSender.hasPermission("autoreplant.fullygrown.toggle"))
        {
            if (config.getBoolean("requireFullyGrown"))
            {
                config.set("requireFullyGrown", false);
                commandSender.sendMessage("[AutoReplant] Fully grown requirements turned off.");
                return true;
            } else
            {
                config.set("requireFullyGrown", true);
                commandSender.sendMessage("[AutoReplant] Fully grown requirements turned on.");
                return true;
            }
        }
        commandSender.sendMessage("You do not have the required permissions!");
        return false;
    }
}
