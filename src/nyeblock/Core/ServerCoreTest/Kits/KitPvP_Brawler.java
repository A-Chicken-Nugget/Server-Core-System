package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class KitPvP_Brawler extends KitBase {
	public KitPvP_Brawler() {
		super("Brawler", Material.IRON_AXE, new ArrayList<InventoryItem>() {{
			ItemStack axe = new ItemStack(Material.IRON_AXE);
			axe.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			
			add(new InventoryItem(axe,0,true));
			add(new InventoryItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,5),1,false));
			add(new InventoryItem(helmet,ArmorType.HELMET));
			add(new InventoryItem(chest,ArmorType.CHEST_PLATE));
			add(new InventoryItem(leggings,ArmorType.LEGGINGS));
			add(new InventoryItem(boots,ArmorType.BOOTS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Iron Axe (Sharpness 2)");
			add(ChatColor.YELLOW + "\u2022 x5 Enchanted Golden Apple");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Leather Helmet (Protection 2)");
			add(ChatColor.YELLOW + "\u2022 Leather Chestplate (Protection 2)");
			add(ChatColor.YELLOW + "\u2022 Leather Leggings (Protection 2)");
			add(ChatColor.YELLOW + "\u2022 Leather Boots (Protection 2)");
		}});
	}
}
