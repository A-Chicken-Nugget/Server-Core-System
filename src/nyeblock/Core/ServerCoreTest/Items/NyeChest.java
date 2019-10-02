package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.Block;
import org.bukkit.block.data.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class NyeChest {
	private ChestValue Value;
	private Vector Loc;
	private ArrayList<ItemStack> Items;
	
	public Block NyeChest(ChestValue value, Vector loc, ArrayList<ItemStack> items) {
		this.Value = value;
		this.Loc = loc;
		this.Items = items;
		
		return createChest();
	}
	
	public Block createChest() {
		Location 
	}
	
	//Getters
	public Vector getLocation() {
		return Loc;
	}
	
	public ChestValue getValue() {
		return Value;
	}
	
	public ArrayList<ItemStack> getItems() {
		return Items;
	}
}