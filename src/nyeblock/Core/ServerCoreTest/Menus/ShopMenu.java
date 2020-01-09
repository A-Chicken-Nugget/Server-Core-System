package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class ShopMenu extends ShopBase {
	private PlayerHandling playerHandling;
	
	public ShopMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"shop_menu");
	}
	
	public void setContents() {
		playerHandling = mainInstance.getPlayerHandlingInstance();
		SubMenu subMenu;
		ShopSubMenu shopSubMenu;
		
		//
		// Shop menu
		//
		subMenu = new SubMenu("Shop Menu",36,this);
		
		//Kitpvp
		subMenu.createOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp Shop", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view the KitPvP Shop");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("KitPvp Shop");
			}
		});
		
		//Sky Wars
		subMenu.createOption(13, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars Shop", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view the Sky Wars Shop");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("Sky Wars Shop");
			}
		});
		
		//Step Spleef
		subMenu.createOption(15, Material.IRON_BOOTS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef Shop", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view the Step Spleef Shop");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("Step Spleef Shop");
			}
		});
		
		//PvP
		subMenu.createOption(21, Material.FISHING_ROD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP Shop", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view the PvP Shop");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("PvP Shop");
			}
		});
		
		//
		// KitPvP Shop
		//
		subMenu = new SubMenu("KitPvP Shop",27,this);
		
		//Win actions
		subMenu.createOption(12, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Win actions", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Win actions are played whenever");
			add(ChatColor.YELLOW + "you win in any game.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the win actions");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("KitPvp Shop \u00BB Win Actions");
			}
		});
		
		//
		// KitPvP Shop >> Win Actions
		//
		shopSubMenu = new ShopSubMenu("KitPvp Shop \u00BB Win Actions",27,true,1,this);
		
		//Rainbow scoreboard title
		shopSubMenu.createOption(11, Material.BLUE_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Rainbow Scoreboard Title for KitPvP", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The title of the scoreboard will");
			add(ChatColor.YELLOW + "change colors randomly.");
		}}, new Runnable() {
			@Override
			public void run() {
				PlayerData pd = playerHandling.getPlayerData(player);
				
				if (pd.getLevel(Realm.KITPVP) >= 1) {
					shopSubMenu.purchaseItem("rainbow_scoreboard_kitpvp");
				} else {
					player.sendMessage(ChatColor.RED + "You do not meet the level requirement!");
				}
			}
		}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Cost: " + ChatColor.GREEN + "500 points");
			add(ChatColor.YELLOW + "Level Requirement: " + ChatColor.GREEN + "Level 1 in KitPvP");
		}}, "rainbow_scoreboard_kitpvp", 500, false);
		
		//Random Fireworks
		shopSubMenu.createOption(13, Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Random Fireworks for KitPvP", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Random fireworks will be shot");
			add(ChatColor.YELLOW + "into the sky.");
		}}, new Runnable() {
			@Override
			public void run() {
				PlayerData pd = playerHandling.getPlayerData(player);
				
				if (pd.getLevel(Realm.KITPVP) >= 1) {
					shopSubMenu.purchaseItem("random_fireworks_kitpvp");
				} else {
					player.sendMessage(ChatColor.RED + "You do not meet the level requirement!");
				}
			}
		}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Cost: " + ChatColor.GREEN + "500 points");
			add(ChatColor.YELLOW + "Level Requirement: " + ChatColor.GREEN + "Level 3 in KitPvP");
		}}, "random_fireworks_kitpvp", 500, false);
		
		//Time speed up
		shopSubMenu.createOption(15, Material.CLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Time Speed Up for KitPvP", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The timescale will speed up and");
			add(ChatColor.YELLOW + "alternate through day/night quickly.");
		}}, new Runnable() {
			@Override
			public void run() {
				PlayerData pd = playerHandling.getPlayerData(player);
				
				if (pd.getLevel(Realm.KITPVP) >= 1) {
					shopSubMenu.purchaseItem("time_speedup_kitpvp");
				} else {
					player.sendMessage(ChatColor.RED + "You do not meet the level requirement!");
				}
			}
		}, new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Cost: " + ChatColor.GREEN + "500 points");
			add(ChatColor.YELLOW + "Level Requirement: " + ChatColor.GREEN + "Level 3 in KitPvP");
		}}, "time_speedup_kitpvp", 500, false);
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
//		player.sendMessage(ChatColor.YELLOW + "This feature is currently disabled.");
		setContents();
		open();
	}
}
