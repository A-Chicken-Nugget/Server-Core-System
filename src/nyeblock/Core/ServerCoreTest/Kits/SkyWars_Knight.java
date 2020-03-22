package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class SkyWars_Knight extends KitBase {
	public SkyWars_Knight() {
		super("Knight", Material.IRON_SWORD, new ArrayList<InventoryItem>() {{
			add(new InventoryItem(new ItemStack(Material.IRON_SWORD),0,true));
			add(new InventoryItem(new ItemStack(Material.CHAINMAIL_CHESTPLATE),ArmorType.CHEST_PLATE));
			add(new InventoryItem(new ItemStack(Material.CHAINMAIL_LEGGINGS),ArmorType.LEGGINGS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Iron Sword");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Chainmail Chestplate");
			add(ChatColor.YELLOW + "\u2022 Chainmail Leggings");
		}});
	}
}
