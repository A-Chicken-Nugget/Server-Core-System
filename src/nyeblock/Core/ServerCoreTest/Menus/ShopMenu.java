package nyeblock.Core.ServerCoreTest.Menus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopSubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuTypeOptionItem;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.LevelRequirement;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class ShopMenu extends ShopBase {
	private PlayerHandling playerHandling;
	
	public ShopMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"hub_shop");
	}
	
	public void setContents() {
		playerHandling = mainInstance.getPlayerHandlingInstance();
		SubMenu subMenu;
		ShopSubMenu shopSubMenu;
		
		//
		// Shop menu
		//
		subMenu = new SubMenu("Hub Shops",36,this);
		
		//Realm win actions
		subMenu.createOption(11, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Win Actions", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Win actions are played whenever");
			add(ChatColor.YELLOW + "you win in any game.");
			add(ChatColor.YELLOW + ChatColor.ITALIC.toString() + "(Can only equip one item per realm)");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the win actions");
		}}, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					playerHandling.getPlayerData(player).getMenu().openMenu("Win Actions");
				}
			});
		}});
		
		//
		// Win Actions
		//
		shopSubMenu = new ShopSubMenu("Win Actions",36,true,1,this);
		
		//Rainbow scoreboard title
		shopSubMenu.createTypeOption(11, Material.BLUE_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Rainbow Scoreboard Title", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The title of the scoreboard will");
			add(ChatColor.YELLOW + "change colors randomly.");
		}}, new ArrayList<ShopMenuTypeOptionItem>() {{
			add(new ShopMenuTypeOptionItem("Kit PvP","kitpvp",450,Arrays.asList(new LevelRequirement(3,Realm.KITPVP))));
			add(new ShopMenuTypeOptionItem("Step Spleef","stepspleef",450,Arrays.asList(new LevelRequirement(3,Realm.STEPSPLEEF))));
			add(new ShopMenuTypeOptionItem("Sky Wars","skywars",450,Arrays.asList(new LevelRequirement(3,Realm.SKYWARS))));
			add(new ShopMenuTypeOptionItem("PvP \u00BB Duels \u00BB Fists","pvp_duels_fists",450,Arrays.asList(new LevelRequirement(3,Realm.PVP_DUELS_FISTS))));
			add(new ShopMenuTypeOptionItem("PvP \u00BB 2v2 \u00BB Fists","pvp_2v2_fists",450,Arrays.asList(new LevelRequirement(3,Realm.PVP_2V2_FISTS))));
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.purchaseItem("rainbow_scoreboard_winAction");
					}
				});
		}}, "For Realm", "rainbow_scoreboard_winAction", false);
		
		//Fireworks
		shopSubMenu.createTypeOption(13, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fireworks", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Random fireworks will be shot");
			add(ChatColor.YELLOW + "into the sky.");
		}}, new ArrayList<ShopMenuTypeOptionItem>() {{
			add(new ShopMenuTypeOptionItem("Kit PvP","kitpvp",800,Arrays.asList(new LevelRequirement(5,Realm.KITPVP))));
			add(new ShopMenuTypeOptionItem("Step Spleef","stepspleef",800,Arrays.asList(new LevelRequirement(5,Realm.STEPSPLEEF))));
			add(new ShopMenuTypeOptionItem("Sky Wars","skywars",800,Arrays.asList(new LevelRequirement(5,Realm.SKYWARS))));
			add(new ShopMenuTypeOptionItem("PvP \u00BB Duels \u00BB Fists","pvp_duels_fists",450,Arrays.asList(new LevelRequirement(3,Realm.PVP_DUELS_FISTS))));
			add(new ShopMenuTypeOptionItem("PvP \u00BB 2v2 \u00BB Fists","pvp_2v2_fists",450,Arrays.asList(new LevelRequirement(3,Realm.PVP_2V2_FISTS))));
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.purchaseItem("fireworks_winAction");
					}
				});
		}}, "For Realm", "fireworks_winAction", false);
		
		//Time speed up
		shopSubMenu.createTypeOption(15, Material.CLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Time Speed Up", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The timescale will speed up and");
			add(ChatColor.YELLOW + "alternate through day/night quickly.");
		}}, new ArrayList<ShopMenuTypeOptionItem>() {{
			add(new ShopMenuTypeOptionItem("Kit PvP","kitpvp",1200,Arrays.asList(new LevelRequirement(6,Realm.KITPVP))));
			add(new ShopMenuTypeOptionItem("Step Spleef","stepspleef",1200,Arrays.asList(new LevelRequirement(6,Realm.STEPSPLEEF))));
			add(new ShopMenuTypeOptionItem("Sky Wars","skywars",1200,Arrays.asList(new LevelRequirement(6,Realm.SKYWARS))));
			add(new ShopMenuTypeOptionItem("PvP \u00BB Duels \u00BB Fists","pvp_duels_fists",450,Arrays.asList(new LevelRequirement(3,Realm.PVP_DUELS_FISTS))));
			add(new ShopMenuTypeOptionItem("PvP \u00BB 2v2 \u00BB Fists","pvp_2v2_fists",450,Arrays.asList(new LevelRequirement(3,Realm.PVP_2V2_FISTS))));
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						shopSubMenu.purchaseItem("time_speed_up_winAction");
					}
				});
		}}, "For Realm", "time_speed_up_winAction", false);
		
		//Back
		shopSubMenu.createOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
			put(ClickType.LEFT,new Runnable() {
				@Override
				public void run() {
					playerHandling.getPlayerData(player).getMenu().openMenu("Shop Menu");
				}
			});
		}});
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "Shop Menu" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("shop_menu");
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
