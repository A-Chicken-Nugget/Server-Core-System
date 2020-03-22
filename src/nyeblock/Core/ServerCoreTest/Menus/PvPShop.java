package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopEquipSubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuTypeOptionItem;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class PvPShop extends ShopBase {
	public PvPShop(Main mainInstance, Player player) {
		super(mainInstance,player,"pvp_shop");
	}
	
	public void setContents() {
		SubMenu subMenu;
		ShopEquipSubMenu shopSubMenu;
		
		//
		// Shop menu
		//
		subMenu = new SubMenu("PvP Shop",36,this);
		
		//Realm win actions
		subMenu.createOption(11, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Win Actions", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Win actions are played whenever");
			add(ChatColor.YELLOW + "you win in a PvP game. These work");
			add(ChatColor.YELLOW + "on every PvP gamemode.");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "(Can only equip one item at a time)");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the win actions");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					openMenu("Win Actions",false);
				}
			});
		}});
		
		//
		// Win Actions
		//
		shopSubMenu = new ShopEquipSubMenu("Win Actions",36,true,1,this);
		
		//Rainbow scoreboard title
		shopSubMenu.createShopOption(11, Material.BLUE_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Rainbow Scoreboard Title", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The title of the scoreboard will");
			add(ChatColor.YELLOW + "change colors randomly.");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.useItem("skyWars_rainbow_scoreboard_winAction");
					}
				});
		}}, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(3,Realm.SKYWARS));
		}}, "pvp_rainbow_scoreboard_winAction", 350, false);
		
		//Fireworks
		shopSubMenu.createShopTypeOption(13, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fireworks", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Random fireworks will be shot");
			add(ChatColor.YELLOW + "into the sky.");
		}}, new ArrayList<ShopMenuTypeOptionItem>() {{
			add(new ShopMenuTypeOptionItem("Red","red",450,Arrays.asList(new LevelRequirement(3,Realm.SKYWARS))));
			add(new ShopMenuTypeOptionItem("Blue","blue",450,Arrays.asList(new LevelRequirement(4,Realm.SKYWARS))));
			add(new ShopMenuTypeOptionItem("Green","green",450,Arrays.asList(new LevelRequirement(5,Realm.SKYWARS))));
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.useItem("skyWars_fireworks_winAction");
					}
				});
		}}, "Color", "skyWars_fireworks_winAction", false);
		
		//Time speed up
		shopSubMenu.createShopOption(15, Material.CLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Time Speed Up", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The timescale will speed up and");
			add(ChatColor.YELLOW + "alternate through day/night quickly.");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.useItem("skyWars_time_speed_up_winAction");
					}
				});
		}}, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(5,Realm.SKYWARS));
		}}, "skyWars_time_speed_up_winAction", 550, false);
		
		//Back
		shopSubMenu.createOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					openMenu("PvP Shop",false);
				}
			});
		}});
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "Sky Wars Shop" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("pvp_shop");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		player.sendMessage(ChatColor.YELLOW + "This feature is currently disabled.");
//		if (mainInstance.getPlayerHandlingInstance().getPlayerData(player).getLoadedDBInfoStatus()) {			
//			open();
//		}
	}
}
