package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class SkyWars_StoneMason extends KitBase {
	public SkyWars_StoneMason() {
		super("Stone Mason", Material.STONE_SWORD, new ArrayList<InventoryItem>() {{
			add(new InventoryItem(new ItemStack(Material.STONE_SWORD),0,true));
			add(new InventoryItem(new ItemStack(Material.COOKED_BEEF,5),8,false));
			add(new InventoryItem(new ItemStack(Material.LEATHER_CHESTPLATE),ArmorType.CHEST_PLATE));
			add(new InventoryItem(new ItemStack(Material.LEATHER_LEGGINGS),ArmorType.LEGGINGS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Stone Sword");
			add(ChatColor.YELLOW + "\u2022 x5 Cooked Beef");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Leather Chestplate");
			add(ChatColor.YELLOW + "\u2022 Leather Leggings");
		}});
	}
}
