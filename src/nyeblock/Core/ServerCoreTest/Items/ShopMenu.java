package nyeblock.Core.ServerCoreTest.Items;

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
import nyeblock.Core.ServerCoreTest.Misc.SubMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class ShopMenu extends MenuBase {
	private PlayerHandling playerHandling;
	
	public ShopMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"shop_menu"); //54
	}
	
	public void setContents() {
		playerHandling = mainInstance.getPlayerHandlingInstance();
		SubMenu subMenu;
		
		//
		// Shop menu
		//
		subMenu = new SubMenu("Shop Menu",36,this);
		
		//Kitpvp
		subMenu.addOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp Shop", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view the KitPvP Shop");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("KitPvp Shop");
			}
		});
		
		//Sky Wars
		subMenu.addOption(13, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars Shop", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view the Sky Wars Shop");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("Sky Wars Shop");
			}
		});
		
		//Step Spleef
		subMenu.addOption(15, Material.IRON_BOOTS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef Shop", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view the Step Spleef Shop");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("Step Spleef Shop");
			}
		});
		
		//PvP
		subMenu.addOption(21, Material.FISHING_ROD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP Shop", new ArrayList<String>() {{
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
		subMenu = new SubMenu("KitPvP Shop",36,this);
		
		//Win actions
		subMenu.addOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Win actions", new ArrayList<String>() {{
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
		subMenu = new SubMenu("KitPvp Shop \u00BB Win Actions",36,this);
		
		//Rainbow scoreboard title
		subMenu.addShopOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Rainbow Scoreboard Title", 500, new ArrayList<String>() {{
			add(ChatColor.YELLOW + "The title of the scoreboard will");
			add(ChatColor.YELLOW + "change colors randomly.");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Cost: " + ChatColor.GREEN + "500 points");
			add(ChatColor.YELLOW + "Level Requirement: " + ChatColor.GREEN + "Level 3 in KitPvP");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to purchase");
		}}, new Runnable() {
			@Override
			public void run() {
				PlayerData pd = playerHandling.getPlayerData(player);
				
				if (pd.getLevel(Realm.KITPVP) >= 3) {
					player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 1);
					player.sendMessage(ChatColor.YELLOW + "You have purchased " + ChatColor.GREEN + "Rainbow Scoreboard Title" + ChatColor.YELLOW + " for KitPvP!");
					
					pd.removePoints(500);
				} else {
					player.sendMessage(ChatColor.RED + "You do not meet the level requirement!");
				}
			}
		}, "rainbow_scoreboard_kitpvp");
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
		player.sendMessage(ChatColor.YELLOW + "This feature is currently disabled.");
//		setContents();
//		open();
	}
}
