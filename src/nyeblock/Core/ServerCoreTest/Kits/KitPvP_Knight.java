package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class KitPvP_Knight extends KitBase {
	public KitPvP_Knight() {
		super("Knight", Material.IRON_CHESTPLATE, new ArrayList<InventoryItem>() {{
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			ItemStack helmet = new ItemStack(Material.IRON_HELMET);
			helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack chest = new ItemStack(Material.IRON_CHESTPLATE);
			chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
			leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack boots = new ItemStack(Material.IRON_BOOTS);
			boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			
			add(new InventoryItem(sword,0,true));
			add(new InventoryItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,1),1,false));
			add(new InventoryItem(helmet,ArmorType.HELMET));
			add(new InventoryItem(chest,ArmorType.CHEST_PLATE));
			add(new InventoryItem(leggings,ArmorType.LEGGINGS));
			add(new InventoryItem(boots,ArmorType.BOOTS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Iron Sword (Sharpness 1)");
			add(ChatColor.YELLOW + "\u2022 x1 Enchanted Golden Apple");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Iron Helmet (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Iron Chestplate (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Iron Leggings (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Iron Boots (Protection 1)");
		}});
	}
}
