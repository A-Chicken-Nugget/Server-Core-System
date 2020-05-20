package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class SkyWars_Farmer extends KitBase {
	public SkyWars_Farmer() {
		super("Farmer", Material.WHEAT, 1000, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(6,Realm.SKYWARS));
		}}, new ArrayList<InventoryItem>() {{
			ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
			leggings.addEnchantment(Enchantment.PROTECTION_PROJECTILE,3);
			
			add(new InventoryItem(new ItemStack(Material.EGG,64),0,true));
			add(new InventoryItem(new ItemStack(Material.COOKED_BEEF,5),1,false));
			add(new InventoryItem(new ItemStack(Material.GOLDEN_APPLE,1),2,false));
			add(new InventoryItem(leggings,ArmorType.LEGGINGS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 x64 Eggs");
			add(ChatColor.YELLOW + "\u2022 x5 Cooked Beef");
			add(ChatColor.YELLOW + "\u2022 x1 Golden Apple");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Iron Leggings (Projectile Protection 3)");
		}}, "skyWars_kit_farmer");
	}
}
