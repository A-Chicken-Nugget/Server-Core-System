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
public class SkyWars_EnderPearler extends KitBase {
	public SkyWars_EnderPearler() {
		super("Ender Pearler", Material.ENDER_PEARL, 750, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(5,Realm.SKYWARS));
		}}, new ArrayList<InventoryItem>() {{
			add(new InventoryItem(new ItemStack(Material.WOODEN_SWORD),0,true));
			add(new InventoryItem(new ItemStack(Material.ENDER_PEARL,2),1,false));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Wooden Sword");
			add(ChatColor.YELLOW + "\u2022 x2 Ender Pearls");
		}}, "skyWars_kit_enderpearler");
	}
}
