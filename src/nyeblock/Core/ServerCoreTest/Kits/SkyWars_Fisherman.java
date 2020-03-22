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
public class SkyWars_Fisherman extends KitBase {
	public SkyWars_Fisherman() {
		super("Fisherman", Material.FISHING_ROD, 850, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(5,Realm.SKYWARS));
		}}, new ArrayList<InventoryItem>() {{
			add(new InventoryItem(new ItemStack(Material.FISHING_ROD),0,true));
			add(new InventoryItem(new ItemStack(Material.COOKED_SALMON,16),1,false));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Fishing Rod");
			add(ChatColor.YELLOW + "\u2022 x16 Cooked Slamon");
		}}, "skyWars_kit_fisherman");
	}
}
