# AutoReplant
Minecraft 1.14.4 Spigot Plugin

Hey everyone! This is a plugin I decided to make because I couldn't find any up to date ones online.
This plugin automatically replants wheat, potatoes, carrots, and beetroots.


# Commands
/ARDebugToggle - Toggles debugging to the console. This was mostly useful for me, but it sends to the console:
  1. The number of items taken from the inventory
  2. The inventory location 
  3. The player they were taken from
  
Defaults to false in the config, but it was useful in testing to make sure the right amount of materials was being taken out of my inventory.

# Permissions
1. autoreplant.debug.toggle - Allows the permission holder to toggle console debugging. Defaults to op.
2. autoreplant.replant - Allows the permission holder to have their crops automatically replanted for them. Defaults to everyone.

# TODO
1. Add a check to see if the crop was fully grown before replanting. Currently the crops are replanted no matter what stage the crop is at.
2. Any requests.

Feel free to give me tips, advice, and criticism so I can become a better programmer!
