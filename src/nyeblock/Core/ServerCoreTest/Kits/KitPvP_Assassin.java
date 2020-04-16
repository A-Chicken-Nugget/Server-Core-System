package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings({"serial","deprecation"})
public class KitPvP_Assassin extends KitBase {
	public KitPvP_Assassin() {
		super("Assassin", Material.STONE_SWORD, 750, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(4,Realm.KITPVP));
//			add(new UserGroupRequirement(UserGroup.VIP));
		}}, new ArrayList<InventoryItem>() {{
			Potion speed = new Potion(PotionType.SPEED, 2);
			
			add(new InventoryItem(new ItemStack(Material.STONE_SWORD),0,true));
			add(new InventoryItem(speed.toItemStack(1),1,false));
			add(new InventoryItem(new ItemStack(Material.LEATHER_HELMET),ArmorType.HELMET));
			add(new InventoryItem(new ItemStack(Material.LEATHER_CHESTPLATE),ArmorType.CHEST_PLATE));
			add(new InventoryItem(new ItemStack(Material.LEATHER_LEGGINGS),ArmorType.LEGGINGS));
			add(new InventoryItem(new ItemStack(Material.LEATHER_BOOTS),ArmorType.BOOTS));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 Stone Sword");
			add(ChatColor.YELLOW + "\u2022 x1 Speed Potion (Level 2)");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Leather Helmet");
			add(ChatColor.YELLOW + "\u2022 Leather Chestplate");
			add(ChatColor.YELLOW + "\u2022 Leather Leggings");
			add(ChatColor.YELLOW + "\u2022 Leather Boots");
		}}, "kitpvp_kit_assassin");
	}
}
