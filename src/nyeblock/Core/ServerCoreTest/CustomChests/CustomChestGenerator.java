package nyeblock.Core.ServerCoreTest.CustomChests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.SkyWars;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

public class CustomChestGenerator {
	public static void setChests(HashMap<Vector,ChestValue> chests, Main mainInstance, GameBase game) {
		World world = Bukkit.getWorld(game.getWorldName());
		
		for(Map.Entry<Vector,ChestValue> entry : chests.entrySet()) {
			Location loc = entry.getKey().toLocation(world);
			
			//Spawn chest
			Block block = loc.getBlock();
			if (!block.getType().equals(Material.CHEST)) {
				block.setType(Material.CHEST);
			}
			
			//Put floating text above chest				
        	Hologram hologram = HologramsAPI.createHologram(mainInstance, loc.add(0, 1.5, 0));
        	hologram.appendTextLine(ChatColor.YELLOW + entry.getValue().toString() + " Tier");
        	if (game instanceof SkyWars) {
        		((SkyWars)game).addHologram(hologram);
        	}
            
			//Get chests inventory
			Chest chest = (Chest)block.getState();
			Inventory inv = chest.getBlockInventory();
			//Get random items to put in the chest based on chest value
			ArrayList<ItemStack> items = new ChestValueItems().getChestItems(entry.getValue());
			//Put items in random slots
			for (ItemStack item : items) {
				boolean itemSet = false;
				
				while (!itemSet) {
					int slot = Toolkit.GetRandomNumber(0,26);
					
					if (inv.getItem(slot) == null) {
						itemSet = true;
						inv.setItem(slot, item);
					}
				}
			}
		}
	}
}
