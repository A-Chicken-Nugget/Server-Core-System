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
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class HubMenu extends MenuBase {
	public HubMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"hub_menu");
	}
	
	public void setContents() {
		PlayerHandling playerHandling = mainInstance.getPlayerHandlingInstance();
		SubMenu subMenu;
		
		//
		// Game menu
		//
		subMenu = new SubMenu("Game Menu",36,this);
		
		//KitPvp
		subMenu.createOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp (Beta)", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.KITPVP) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight other players and");
			add(ChatColor.YELLOW + "try to kill as many as possible.");
			add(ChatColor.YELLOW + "At the end the player with the");
			add(ChatColor.YELLOW + "highest kills wins. Select up to 4 kits.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						player.closeInventory();
						mainInstance.getGameInstance().joinGame(player, Realm.KITPVP);
					}
				});
		}});
		
		//Sky wars
		subMenu.createOption(13, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars (Beta)", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.SKYWARS) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Each player starts with their");
			add(ChatColor.YELLOW + "own island. You must craft and");
			add(ChatColor.YELLOW + "build your way to the center of");
			add(ChatColor.YELLOW + "the map for better loot. The");
			add(ChatColor.YELLOW + "last player left wins!");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {       
		            	player.closeInventory();
		            	mainInstance.getGameInstance().joinLobby(player, Realm.SKYWARS_LOBBY);
		            }
				});
		}});
		
		//Step Spleef
		subMenu.createOption(15, Material.IRON_BOOTS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef (Beta)", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.STEPSPLEEF) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Dodge and weave to survive");
			add(ChatColor.YELLOW + "when the blocks you've walked");
			add(ChatColor.YELLOW + "on get deleted shortly after.");
			add(ChatColor.YELLOW + "The last player left wins!");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {       
		            	player.closeInventory();
		            	mainInstance.getGameInstance().joinGame(player, Realm.STEPSPLEEF);
		            }
				});
		}});
		
		//PvP
		subMenu.createOption(21, Material.FISHING_ROD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Fight against other players");
			add(ChatColor.YELLOW + "with the items the gamemode you");
			add(ChatColor.YELLOW + "choose provides.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the game modes");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {       
		            	playerHandling.getPlayerData(player).getMenu().openMenu("PvP Games");
		            }
				});
		}});
		
		//
		// PvP games
		//
		subMenu = new SubMenu("PvP Games",36,this);
		
		//Duels
		subMenu.createOption(12, Material.GOLDEN_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Duels", new ArrayList<String>() {{
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight one other player.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the modes");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						playerHandling.getPlayerData(player).getMenu().openMenu("Duels Modes");
					}
				});
		}});
		
		//2v2
		subMenu.createOption(14, Material.IRON_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "2v2", new ArrayList<String>() {{
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight 2 other players with a teammate.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the modes");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						playerHandling.getPlayerData(player).getMenu().openMenu("2v2 Modes");
					}
				});
		}});
		
		//Back
		subMenu.createOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						playerHandling.getPlayerData(player).getMenu().openMenu("Game Menu");
					}
				});
		}});
		
		//
		// Duels modes
		//
		subMenu = new SubMenu("Duels Modes",36,this);
		
		//Fist
		subMenu.createOption(12, Material.RABBIT, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fist Fighting", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.PVP_DUELS_FISTS) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Use your fists to fight the other player.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						player.closeInventory();
						mainInstance.getGameInstance().joinGame(player, Realm.PVP_DUELS_FISTS);
					}
				});
		}});
		
		//Sword/Armor
		subMenu.createOption(14, Material.IRON_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Weapon/Armor Fighting", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.PVP_DEULS_WEPSARMOR) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Use your weps to fight the other player.");
			add(ChatColor.RESET.toString());
			add(ChatColor.RED.toString() + ChatColor.BOLD + "\u2716 Game mode currently closed \u2716");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						player.closeInventory();
						player.sendMessage(ChatColor.RED + "This mode is currently closed and cannot be played.");
					}
				});
		}});
		
		//Back
		subMenu.createOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						playerHandling.getPlayerData(player).getMenu().openMenu("PvP Games");
					}
				});
		}});
		
		//
		// 2v2 modes
		//
		subMenu = new SubMenu("2v2 Modes",36,this);
		
		//Fist
		subMenu.createOption(12, Material.RABBIT, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fist Fighting", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.PVP_2V2_FISTS) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Use your fists to fight the other players.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						player.closeInventory();
						mainInstance.getGameInstance().joinGame(player, Realm.PVP_2V2_FISTS);
					}
				});
		}});
		
		//Sword/Armor
		subMenu.createOption(14, Material.IRON_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Weapon/Armor Fighting", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.PVP_2V2_WEPSARMOR) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Use your weapons/armor to fight the other players.");
			add(ChatColor.RESET.toString());
			add(ChatColor.RED.toString() + ChatColor.BOLD + "\u2716 Game mode currently closed \u2716");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						player.closeInventory();
						player.sendMessage(ChatColor.RED + "This mode is currently closed and cannot be played.");
					}
				});
		}});
		
		//Back
		subMenu.createOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						playerHandling.getPlayerData(player).getMenu().openMenu("PvP Games");
					}
				});
		}});
		
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "Game Menu" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("hub_menu");
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