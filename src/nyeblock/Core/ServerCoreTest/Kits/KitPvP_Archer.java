package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class KitPvP_Archer extends KitBase {
	public KitPvP_Archer() {
		super("Archer", Material.BOW, new ArrayList<InventoryItem>() {{
			ItemStack axe = new ItemStack(Material.STONE_AXE);
			axe.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			ItemStack bow = new ItemStack(Material.BOW);
			bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
			ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);
			helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack chest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
			chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
			leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);
			boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			
			add(new InventoryItem(axe,0,true));
			add(new InventoryItem(bow,1,false));
			add(new InventoryItem(new ItemStack(Material.ARROW,40),2,false));
			add(new InventoryItem(helmet,ArmorType.HELMET));
			add(new InventoryItem(chest,ArmorType.CHEST_PLATE));
			add(new InventoryItem(leggings,ArmorType.LEGGINGS));
			add(new InventoryItem(boots,ArmorType.BOOTS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Stone Axe (Sharpness 1)");
			add(ChatColor.YELLOW + "\u2022 Bow (Power 1)");
			add(ChatColor.YELLOW + "\u2022 x40 Arrows");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Chainmail Helmet (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Chainmail Chestplate (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Chainmail Leggings (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Chainmail Boots (Protection 1)");
		}});
	}
}
