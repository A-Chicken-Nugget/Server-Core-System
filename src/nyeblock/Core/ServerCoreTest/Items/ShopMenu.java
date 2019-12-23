package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;

public class ShopMenu extends MenuBase {
	private PlayerHandling playerHandling;
	
	@SuppressWarnings("serial")
	public ShopMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"shop_menu"); //54
		playerHandling = mainInstance.getPlayerHandlingInstance();
		ItemStack item;
		
		//
		// Shop menu
		//
		
		//Kitpvp Shop
//		item = super.createItem(Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp Shop", new ArrayList<String>() {{
//			add(ChatColor.GREEN + "\u279D \u279D Click to view the KitPvp shop");
//		}});
//		super.addOption("Shop Menu", 11, item, new Runnable() {
//            @Override
//            public void run() {
//            	playerHandling.getPlayerData(player).getMenu().openMenu("KitPvp Shop");
//            }
//		});
//		//Sky Wars Shop
//		item = super.createItem(Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars Shop", new ArrayList<String>() {{
//			add(ChatColor.GREEN + "\u279D \u279D Click to view the Sky Wars shop");
//		}});
//		super.addOption("Shop Menu", 13, item, new Runnable() {
//            @Override
//            public void run() {       
//            	playerHandling.getPlayerData(player).getMenu().openMenu("Sky Wars Shop");
//            }
//		});
//		//Step Spleef Shop
//		item = super.createItem(Material.IRON_BOOTS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef Shop", new ArrayList<String>() {{
//			add(ChatColor.GREEN + "\u279D \u279D Click to view the Step Spleef shop");
//		}});
//		super.addOption("Shop Menu", 15, item, new Runnable() {
//            @Override
//            public void run() {       
//            	playerHandling.getPlayerData(player).getMenu().openMenu("Step Spleef Shop");
//            }
//		});
//		//PvP Shop
//		item = super.createItem(Material.FISHING_ROD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP Shop", new ArrayList<String>() {{
//			add(ChatColor.GREEN + "\u279D \u279D Click to view the PvP shop");
//		}});
//		super.addOption("Shop Menu", 21, item, new Runnable() {
//            @Override
//            public void run() {       
//            	mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("PvP Shop");
//            }
//		});
//		
//		//
//		// KitPvp Shop
//		//
//		
//		//Win actions
//		item = super.createItem(Material.FIREWORK_STAR, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Win Actions", new ArrayList<String>() {{
//			add(ChatColor.YELLOW + "These are actions played whenever");
//			add(ChatColor.YELLOW + "you win in any gamemode.");
//			add(ChatColor.RESET.toString());
//			add(ChatColor.GRAY + "Only one can be equipped at a time");
//			add(ChatColor.RESET.toString());
//			add(ChatColor.GREEN + "\u279D \u279D Click to view the win actions");
//		}});
//		super.addOption("KitPvp Shop", 22, item, new Runnable() {
//            @Override
//            public void run() {       
//            	mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("KitPvp Shop \u00BB Win Actions");
//            }
//		});
//		//Back
//		item = super.createItem(Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null);
//		super.addOption("KitPvp Shop", 45, item, new Runnable() {
//			@Override
//			public void run() {
//				mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("Shop Menu");
//			}
//		});
//		
//		//
//		// KitPvp Shop >> Win Actions
//		//
//		
//		//Win actions
//		item = super.createItem(Material.FIREWORK_ROCKET, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fireworks", new ArrayList<String>() {{
//			add(ChatColor.YELLOW + "Choose between the different types");
//			add(ChatColor.YELLOW + "of fireworks.");
//			add(ChatColor.RESET.toString());
//			add(ChatColor.GREEN + "\u279D \u279D Click to view the types of fireworks");
//		}});
//		super.addOption("KitPvp Shop \u00BB Win Actions", 22, item, new Runnable() {
//            @Override
//            public void run() {       
//            	mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("KitPvp Shop \u00BB Win Actions \u00BB Fireworks");
//            }
//		});
//		//Back
//		item = super.createItem(Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null);
//		super.addOption("KitPvp Shop \u00BB Win Actions", 45, item, new Runnable() {
//			@Override
//			public void run() {
//				mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("KitPvp Shop");
//			}
//		});
//		
//		//
//		// KitPvp Shop >> Win Actions >> Fireworks
//		//
//		
//		//Star
//		item = super.createItem(Material.NETHER_STAR, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Star", new ArrayList<String>() {{
//			add(ChatColor.YELLOW + "A type of firework that when");
//			add(ChatColor.YELLOW + "it explodes. It forms the shape");
//			add(ChatColor.YELLOW + "of a star.");
//			add(ChatColor.RESET.toString());
//			add(ChatColor.GREEN + "\u279D \u279D Click to purchase");
//		}});
//		super.addOption("KitPvp Shop \u00BB Win Actions \u00BB Fireworks", 21, item, new Runnable() {
//            @Override
//            public void run() {       
//            	
//            }
//		});
//		//Creeper
//		item = super.createItem(Material.NETHER_STAR, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Creeper", new ArrayList<String>() {{
//			add(ChatColor.YELLOW + "A type of firework that when");
//			add(ChatColor.YELLOW + "it explodes. It forms the shape");
//			add(ChatColor.YELLOW + "of a creeper.");
//			add(ChatColor.RESET.toString());
//			add(ChatColor.GREEN + "\u279D \u279D Click to purchase");
//		}});
//		super.addOption("KitPvp Shop \u00BB Win Actions \u00BB Fireworks", 23, item, new Runnable() {
//            @Override
//            public void run() {       
//            	
//            }
//		});
//		//Back
//		item = super.createItem(Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null);
//		super.addOption("KitPvp Shop \u00BB Win Actions \u00BB Fireworks", 45, item, new Runnable() {
//			@Override
//			public void run() {
//				mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("KitPvp Shop \u00BB Win Actions");
//			}
//		});
	}

	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.EMERALD);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Shop Menu" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("shop_menu");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		player.sendMessage(ChatColor.YELLOW + "This feature has been disabled since it is not finished.");
//		openMenu("Shop Menu");
	}
}
