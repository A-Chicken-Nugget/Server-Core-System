package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings("serial")
public class SkyWars_Enchanter extends KitBase {
	public SkyWars_Enchanter() {
		super("Enchanter", Material.ENCHANTING_TABLE, 650, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(2,Realm.SKYWARS));
		}}, new ArrayList<InventoryItem>() {{
			add(new InventoryItem(new ItemStack(Material.ENCHANTING_TABLE),0,true));
			add(new InventoryItem(new ItemStack(Material.BOOKSHELF,16),1,false));
			add(new InventoryItem(new ItemStack(Material.EXPERIENCE_BOTTLE,45),2,false));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 x1 Enchanting Table");
			add(ChatColor.YELLOW + "\u2022 x16 Bookshelfs");
			add(ChatColor.YELLOW + "\u2022 x45 Experience Bottles");
		}}, "skyWars_kit_enchanter");
	}
}
