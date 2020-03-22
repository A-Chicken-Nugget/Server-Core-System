package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class SkyWars_Archer extends KitBase {
	public SkyWars_Archer() {
		super("Archer", Material.BOW, new ArrayList<InventoryItem>() {{
			add(new InventoryItem(new ItemStack(Material.WOODEN_SHOVEL),0,true));
			add(new InventoryItem(new ItemStack(Material.BOW),1,false));
			add(new InventoryItem(new ItemStack(Material.ARROW,20),8,false));
			add(new InventoryItem(new ItemStack(Material.LEATHER_HELMET),ArmorType.HELMET));
			add(new InventoryItem(new ItemStack(Material.LEATHER_CHESTPLATE),ArmorType.CHEST_PLATE));
			add(new InventoryItem(new ItemStack(Material.LEATHER_LEGGINGS),ArmorType.LEGGINGS));
			add(new InventoryItem(new ItemStack(Material.LEATHER_BOOTS),ArmorType.BOOTS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Wooden Shovel");
			add(ChatColor.YELLOW + "\u2022 Bow");
			add(ChatColor.YELLOW + "\u2022 x20 Arrows");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Leather Helmet");
			add(ChatColor.YELLOW + "\u2022 Leather Chestplate");
			add(ChatColor.YELLOW + "\u2022 Leather Leggings");
			add(ChatColor.YELLOW + "\u2022 Leather Boots");
		}});
	}
}
