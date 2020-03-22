package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class SkyWars_Butcher extends KitBase {
	public SkyWars_Butcher() {
		super("Butcher", Material.STONE_AXE, new ArrayList<InventoryItem>() {{
			add(new InventoryItem(new ItemStack(Material.STONE_AXE),0,true));
			add(new InventoryItem(new ItemStack(Material.COOKED_BEEF,15),8,false));
			add(new InventoryItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE),ArmorType.CHEST_PLATE));
			add(new InventoryItem(new ItemStack(Material.CHAINMAIL_LEGGINGS),ArmorType.LEGGINGS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Stone Axe");
			add(ChatColor.YELLOW + "\u2022 x15 Cooked Beef");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Chainmail Chestplate");
			add(ChatColor.YELLOW + "\u2022 Chainmail Leggings");
		}});
	}
}
