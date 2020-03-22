package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Items.Fireball;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class SkyWars_Fireballer extends KitBase {
	public SkyWars_Fireballer() {
		super("Fireballer", Material.FIRE_CHARGE, 550, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(3,Realm.SKYWARS));
		}}, new ArrayList<InventoryItem>() {{
			Fireball fireball = new Fireball(4);
			
			add(new InventoryItem(new ItemStack(Material.WOODEN_SWORD),0,true));
			add(new InventoryItem(fireball.give(),1,false,fireball));
			add(new InventoryItem(new ItemStack(Material.LEATHER_HELMET),ArmorType.HELMET));
			add(new InventoryItem(new ItemStack(Material.LEATHER_CHESTPLATE),ArmorType.CHEST_PLATE));
			add(new InventoryItem(new ItemStack(Material.LEATHER_LEGGINGS),ArmorType.LEGGINGS));
			add(new InventoryItem(new ItemStack(Material.LEATHER_BOOTS),ArmorType.BOOTS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Wooden Sword");
			add(ChatColor.YELLOW + "\u2022 x4 Fireballs");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Leather Helmet");
			add(ChatColor.YELLOW + "\u2022 Leather Chestplate");
			add(ChatColor.YELLOW + "\u2022 Leather Leggings");
			add(ChatColor.YELLOW + "\u2022 Leather Boots");
		}}, "skyWars_kit_fireballer");
	}
}
