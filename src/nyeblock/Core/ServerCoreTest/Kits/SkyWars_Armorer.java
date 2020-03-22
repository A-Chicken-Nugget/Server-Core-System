package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class SkyWars_Armorer extends KitBase {
	public SkyWars_Armorer() {
		super("Armorer", Material.GOLDEN_CHESTPLATE, 250, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(2,Realm.SKYWARS));
		}}, new ArrayList<InventoryItem>() {{
			add(new InventoryItem(new ItemStack(Material.GOLDEN_HELMET),ArmorType.HELMET));
			add(new InventoryItem(new ItemStack(Material.GOLDEN_CHESTPLATE),ArmorType.CHEST_PLATE));
			add(new InventoryItem(new ItemStack(Material.GOLDEN_LEGGINGS),ArmorType.LEGGINGS));
			add(new InventoryItem(new ItemStack(Material.GOLDEN_BOOTS),ArmorType.BOOTS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Golden Helmet");
			add(ChatColor.YELLOW + "\u2022 Golden Chestplate");
			add(ChatColor.YELLOW + "\u2022 Golden Leggings");
			add(ChatColor.YELLOW + "\u2022 Golden Boots");
		}}, "skyWars_kit_armorer");
	}
}
