package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

@SuppressWarnings({"serial","deprecation"})
public class SkyWars_Frog extends KitBase {
	public SkyWars_Frog() {
		super("Frog", Material.LILY_PAD, 850, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(3,Realm.SKYWARS));
		}}, new ArrayList<InventoryItem>() {{
			ItemStack helmet = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta itemMeta = (SkullMeta)helmet.getItemMeta();
			itemMeta.setOwner("frog");
			helmet.setItemMeta(itemMeta);
			
			Potion leaping = new Potion(PotionType.JUMP,1);
			Potion speed = new Potion(PotionType.SPEED, 2);
			
			add(new InventoryItem(leaping.toItemStack(1),0,true));
			add(new InventoryItem(speed.toItemStack(1),1,false));
			add(new InventoryItem(helmet,ArmorType.HELMET));
		}}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Items");
			add(ChatColor.YELLOW + "\u2022 x1 Potion of jumping");
			add(ChatColor.YELLOW + "\u2022 x1 Potion of speed");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "Armor");
			add(ChatColor.YELLOW + "\u2022 Phrog helmet");
		}}, "skyWars_kit_frog");
	}
}
