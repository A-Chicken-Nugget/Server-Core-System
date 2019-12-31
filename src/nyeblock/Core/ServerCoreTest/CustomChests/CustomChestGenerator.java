package nyeblock.Core.ServerCoreTest.CustomChests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

public class CustomChestGenerator {
	private Main mainInstance;
	
	public CustomChestGenerator(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	public void setChests(HashMap<Vector,ChestValue> chests, World world) {
		for(Map.Entry<Vector,ChestValue> entry : chests.entrySet()) {
			Vector vector = entry.getKey();
			Location loc = new Location(world,vector.getX(),vector.getY(),vector.getZ());
			
			//Spawn chest
			Block block = loc.getBlock();
			if (!block.getType().equals(Material.CHEST)) {
	            block.setType(Material.CHEST);
			}
			//Put floating text above chest
            HologramsAPI.createHologram(mainInstance, loc.add(0, 1.5, 0)).appendTextLine(ChatColor.YELLOW + entry.getValue().toString() + " Tier");
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
