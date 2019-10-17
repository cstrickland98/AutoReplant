/**
 * MIT License
 *
 * Copyright (c) 2019 Shatou (Hardcore_CPS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.ddns.gamersanonymous;

import net.ddns.gamersanonymous.commands.CommandDebug;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Author:     Shatou (Hardcore_CPS)
 * Date:       10/11/2019
 * MC Version: 1.14.4
 *
 * This plugin listens for a block break event, if it is a crop (excluding melons and pumpkins) it will automatically replant with
 * the appropriate materials after a short delay.
 */
public final class main extends JavaPlugin implements Listener, Cancellable
{

    private FileConfiguration config = this.getConfig();

    private final static int NUM_SEEDS_REMOVED = 1;
    private final static int DELAY = 20; //Delay to give the block time to break
    private boolean cancelled;
    private Material removeMat;

    /**
     * On plugin enable this will register the event listener to this plugin
     */
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this,this);

        config.addDefault("debug", false);
        config.options().copyDefaults(true);
        saveConfig();
        this.getCommand("ARDebugToggle").setExecutor(new CommandDebug(config));
    }

    /**
     * On plugin disable
     */
    @Override
    public void onDisable()
    {

    }

    /**
     * Our event listener which listens for a blockbreakevent. If the block is a crop on a farm then it will replant with
     * appropriate materials and take a seed from the player's inventory. If the player does not have a seed, then it will tell the
     * player not enough seeds.
     * @param blockBreakEvent The event we are listening for; e.g. the crop breaking
     */
    @EventHandler
    public void onPlantBreak(BlockBreakEvent blockBreakEvent)
    {
        if(blockBreakEvent.getPlayer().hasPermission("autoreplant.replant"))
        {
            //Get our block
            Block block = blockBreakEvent.getBlock();
            Material farm = blockBreakEvent.getBlock().getType();

            //If the block is a crop on a farm
            if (farm == Material.WHEAT || farm == Material.BEETROOTS || farm == Material.CARROTS || farm == Material.POTATOES)
            {

                Location farmPos = block.getLocation(); //The position of the block in the world
                String stringWorld = block.getWorld().getName(); //The name of the world
                Player player = blockBreakEvent.getPlayer(); //The player breaking the block

                //Switch to correctly set the materials we want to remove from a players inventory
                switch (farm)
                {
                    case WHEAT:
                        removeMat = Material.WHEAT_SEEDS; //The material to remove from the players inventory
                        break;

                    case BEETROOTS:
                        removeMat = Material.BEETROOT_SEEDS; //The material to remove from the players inventory
                        break;

                    case CARROTS:
                        removeMat = Material.CARROT; //The material to remove from the players inventory
                        break;

                    case POTATOES:
                        removeMat = Material.POTATO; //The material to remove from the players inventory
                        break;
                }

                //Schedules the seeds to be replanted the next tick
                BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
                scheduler.scheduleSyncDelayedTask(this, new Runnable()
                {

                    @Override
                    public void run()
                    {
                        //If the player has seeds and the blockbreakevent has not been cancelled
                        if (player.getInventory().contains(removeMat) && !blockBreakEvent.isCancelled())
                        {
                            //Gets the block location from the world from the server and sets it to the crop
                            //This just puts the crop down on a farm at state 0
                            try
                            {
                                Bukkit.getServer().getWorld(stringWorld).getBlockAt(farmPos).setType(farm);
                                //Remove an appropriate crop material from the player
                                removeItem(player, removeMat);
                            } catch (NullPointerException e)
                            {
                            }
                        }
                        //else if a player does not have any seeds in their inventory
                        else if (!(player.getInventory().contains(removeMat)))
                        {
                            player.sendMessage("Not enough seeds in inventory!");
                        }

                    }
                }, DELAY);
            }
        }
    }

    /**
     * Searches through the players inventory and removes what we specify.
     * @param player The player who broke the block
     * @param mat The material we want to remove
     */
    public void removeItem(Player player, Material mat)
    {
        for(int i = 0; i < player.getInventory().getContents().length; i++)
        {
            ItemStack item = player.getInventory().getItem(i);
            try
            {
                if (item.getType() != mat)
                {
                    continue;
                }
            }
            catch (NullPointerException e)
            {
                continue;
            }
            int itemAmount = item.getAmount();
            item.setAmount(itemAmount - NUM_SEEDS_REMOVED);
            if(config.getBoolean("debug"))
            {
                Bukkit.getServer().getConsoleSender().sendMessage("Removed " + NUM_SEEDS_REMOVED + " from slot " + i + " in " + player.getName() + "'s inventory.");
            }
            break; //Added break because otherwise it takes too many seeds
        }
    }

    @Override
    public boolean isCancelled()
    {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel)
    {
        cancelled = cancel;
    }
}
