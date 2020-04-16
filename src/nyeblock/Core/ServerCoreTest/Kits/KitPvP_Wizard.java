package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Items.Fireball;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings({"serial","deprecation"})
public class KitPvP_Wizard extends KitBase {
	public KitPvP_Wizard() {
		super("Wizard", Material.POTION, new ArrayList<InventoryItem>() {{
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			Fireball fireball = new Fireball(5);
			Potion damage = new Potion(PotionType.INSTANT_DAMAGE, 1);
			damage.setSplash(true);
			Potion regen = new Potion(PotionType.REGEN, 1);
			regen.setSplash(true);
			ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
			helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
			chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
			boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			
			add(new InventoryItem(sword,0,true));
			add(new InventoryItem(fireball.give(),1,false,fireball));
			add(new InventoryItem(damage.toItemStack(2),2,false));
			add(new InventoryItem(regen.toItemStack(1),3,false));
			add(new InventoryItem(helmet,ArmorType.HELMET));
			add(new InventoryItem(chest,ArmorType.CHEST_PLATE));
			add(new InventoryItem(leggings,ArmorType.LEGGINGS));
			add(new InventoryItem(boots,ArmorType.BOOTS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Iron Sword");
			add(ChatColor.YELLOW + "\u2022 x5 Fireballs");
			add(ChatColor.YELLOW + "\u2022 x2 Splash Potions of Harm");
			add(ChatColor.YELLOW + "\u2022 x1 Splash Potion of Regeneration");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Leather Helmet (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Leather Chestplate (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Leather Leggings (Protection 1)");
			add(ChatColor.YELLOW + "\u2022 Leather Boots (Protection 1)");
		}});
	}
}
