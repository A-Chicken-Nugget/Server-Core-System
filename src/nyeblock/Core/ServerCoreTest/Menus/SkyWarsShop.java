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
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopSubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuTypeOptionItem;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class SkyWarsShop extends ShopBase {
	public SkyWarsShop(Main mainInstance, Player player) {
		super(mainInstance,player,"skywars_shop");
	}
	
	public void setContents() {
		SubMenu subMenu;
		
		//
		// Shop menu
		//
		subMenu = new SubMenu("Sky Wars Shop",36,this);
		
		//Realm win actions
		subMenu.createOption(11, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Win Actions", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Win actions are played whenever");
			add(ChatColor.YELLOW + "you win in a Sky Wars game.");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "(Can only equip 1 item at a time)");
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
		
		//Realm win actions
		subMenu.createOption(13, Material.IRON_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Special Kits", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Special kits that give you");
			add(ChatColor.YELLOW + "different fighting abilities");
			add(ChatColor.YELLOW + "from the default kits.");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "(Can only equip 1 item at a time)");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the special kits");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					openMenu("Special Kits",false);
				}
			});
		}});
		
		//
		// Win Actions
		//
		ShopSubMenu shopSubMenu = new ShopSubMenu("Win Actions",36,true,1,this);
		
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
		}}, "skyWars_rainbow_scoreboard_winAction", 350, false);
		
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
					openMenu("Sky Wars Shop",false);
				}
			});
		}});
		
		//
		// Special Kits
		//
		ShopSubMenu shopSubMenu2 = new ShopSubMenu("Special Kits",36,true,1,this);
		
		//Fireball kit
		shopSubMenu2.createShopOption(11, Material.FIRE_CHARGE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fireball", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "- Wood sword");
			add(ChatColor.YELLOW + "- x2 Fireball");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu2.useItem("skyWars_kit_fireball");
					}
				});
		}}, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(3,Realm.SKYWARS));
		}}, "skyWars_kit_fireball", 350, false);
		
		//Ender pearl kit
		shopSubMenu2.createShopOption(13, Material.ENDER_PEARL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Ender Pearl", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "- Wood sword");
			add(ChatColor.YELLOW + "- x2 Ender Pearl");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu2.useItem("skyWars_kit_enderPearl");
					}
				});
		}}, new ArrayList<RequirementBase>() {{
			add(new LevelRequirement(5,Realm.SKYWARS));
		}}, "skyWars_kit_enderPearl", 650, false);
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "Sky Wars Shop" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("skywars_shop");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		if (mainInstance.getPlayerHandlingInstance().getPlayerData(player).getLoadedDBInfoStatus()) {			
			open();
		}
	}
}
