package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.RealmHandling;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class GameMenu extends MenuBase {
	public GameMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"game_menu");
	}
	
	public void setContents() {
		RealmHandling realmHandling = mainInstance.getRealmHandlingInstance();
		SubMenu subMenu;
		
		//
		// Game menu
		//
		subMenu = new SubMenu("Game Menu",36,this);
		
		//KitPvp
		subMenu.createOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Kit PvP", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + realmHandling.getGamesCount(Realm.KITPVP) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight other players and");
			add(ChatColor.YELLOW + "try to kill as many as possible.");
			add(ChatColor.YELLOW + "At the end the player with the");
			add(ChatColor.YELLOW + "highest kills wins. Select up to 4 kits.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to join the Kit PvP lobby");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						player.closeInventory();
						realmHandling.joinLobby(player, Realm.KITPVP_LOBBY);
					}
				});
		}});
		
		//Sky wars
		subMenu.createOption(13, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars Lobby", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + realmHandling.getGamesCount(Realm.SKYWARS) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Each player starts with their");
			add(ChatColor.YELLOW + "own island. You must craft and");
			add(ChatColor.YELLOW + "build your way to the center of");
			add(ChatColor.YELLOW + "the map for better loot. The");
			add(ChatColor.YELLOW + "last player left wins!");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to join the Sky Wars lobby");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {       
		            	player.closeInventory();
		            	realmHandling.joinLobby(player, Realm.SKYWARS_LOBBY);
		            }
				});
		}});
		
		//Step Spleef
		subMenu.createOption(15, Material.IRON_BOOTS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef Lobby", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + realmHandling.getGamesCount(Realm.STEPSPLEEF) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Dodge and weave to survive");
			add(ChatColor.YELLOW + "when the blocks you've walked");
			add(ChatColor.YELLOW + "on get deleted shortly after.");
			add(ChatColor.YELLOW + "The last player left wins!");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to join the Step Spleef lobby");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {       
		            	player.closeInventory();
		            	realmHandling.joinLobby(player, Realm.STEPSPLEEF_LOBBY);
		            }
				});
		}});
		
		//PvP
		subMenu.createOption(21, Material.FISHING_ROD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + (realmHandling.getGamesCount(Realm.PVP_2V2_FISTS)+realmHandling.getGamesCount(Realm.PVP_DUELS_FISTS)) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight against other players");
			add(ChatColor.YELLOW + "with the items the gamemode you");
			add(ChatColor.YELLOW + "choose provides.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to join the PvP lobby");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						realmHandling.joinLobby(player, Realm.PVP_LOBBY);
		            }
				});
		}});
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "Game Menu" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("game_menu");
		item.setItemMeta(shopMeta);
		
		return item;
	}
//	public void onOpen() {
//		if (!isOpened) {			
//			mainInstance.getTimerInstance().createTimer2("invRefresh_" + player.getUniqueId(), 10, 0, new Runnable() {
//				@Override
//				public void run() {
//					setContents(true);
//				}
//			});
//			isOpened = true;
//		}
//	}
//	public void onDelete() {
//		mainInstance.getTimerInstance().deleteTimer("invRefresh_" + player.getUniqueId());
//	}
	//Use the item
	public void use(ItemStack item) {				
		if (mainInstance.getPlayerHandlingInstance().getPlayerData(player).getLoadedDBInfoStatus()) {			
			open();
		}
	}
}
