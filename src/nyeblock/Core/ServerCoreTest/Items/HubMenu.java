package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.SubMenu;
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
		subMenu.addOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp (Beta)", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.KITPVP) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight other players and");
			add(ChatColor.YELLOW + "try to kill as many as possible.");
			add(ChatColor.YELLOW + "At the end the player with the");
			add(ChatColor.YELLOW + "highest kills wins. Select up to 4 kits.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				mainInstance.getGameInstance().joinGame(player, Realm.KITPVP);
			}
		});
		
		//Sky wars
		subMenu.addOption(13, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars (Beta)", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.SKYWARS) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Each player starts with their");
			add(ChatColor.YELLOW + "own island. You must craft and");
			add(ChatColor.YELLOW + "build your way to the center of");
			add(ChatColor.YELLOW + "the map for better loot. The");
			add(ChatColor.YELLOW + "last player left wins!");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new Runnable() {
            @Override
            public void run() {       
            	player.closeInventory();
            	mainInstance.getGameInstance().joinGame(player, Realm.SKYWARS);
            }
		});
		
		//Step Spleef
		subMenu.addOption(15, Material.IRON_BOOTS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef (Beta)", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.STEPSPLEEF) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Dodge and weave to survive");
			add(ChatColor.YELLOW + "when the blocks you've walked");
			add(ChatColor.YELLOW + "on get deleted shortly after.");
			add(ChatColor.YELLOW + "The last player left wins!");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new Runnable() {
            @Override
            public void run() {       
            	player.closeInventory();
            	mainInstance.getGameInstance().joinGame(player, Realm.STEPSPLEEF);
            }
		});
		
		//PvP
		subMenu.addOption(21, Material.FISHING_ROD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Fight against other players");
			add(ChatColor.YELLOW + "with the items the gamemode you");
			add(ChatColor.YELLOW + "choose provides.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the game modes");
		}}, new Runnable() {
            @Override
            public void run() {       
            	playerHandling.getPlayerData(player).getMenu().openMenu("PvP Games");
            }
		});
		
		//
		// PvP games
		//
		subMenu = new SubMenu("PvP Games",36,this);
		
		//Duels
		subMenu.addOption(12, Material.GOLDEN_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Duels", new ArrayList<String>() {{
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight one other player.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the modes");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).setCustomDataKey("pvp_mode", "1");
				playerHandling.getPlayerData(player).getMenu().openMenu("Duels Modes");
			}
		});
		
		//2v2
		subMenu.addOption(14, Material.IRON_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "2v2", new ArrayList<String>() {{
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Fight 2 other players with a teammate.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view the modes");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).setCustomDataKey("pvp_mode", "2");
				playerHandling.getPlayerData(player).getMenu().openMenu("2v2 Modes");
			}
		});
		
		//Back
		subMenu.addOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("Game Menu");
			}
		});
		
		//
		// Duels modes
		//
		subMenu = new SubMenu("Duels Modes",36,this);
		
		//Fist
		subMenu.addOption(12, Material.RABBIT, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fist Fighting", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(PvPMode.DUELS,PvPType.FIST) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Use your fists to fight the other player.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				playerHandling.getPlayerData(player).setCustomDataKey("pvp_type", "1");
				mainInstance.getGameInstance().joinGame(player, Realm.PVP);
			}
		});
		
		//Sword/Armor
		subMenu.addOption(14, Material.IRON_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Weapon/Armor Fighting", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(PvPMode.DUELS,PvPType.WEPSARMOR) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Use your weps to fight the other player.");
			add(ChatColor.RESET.toString());
			add(ChatColor.RED.toString() + ChatColor.BOLD + "\u2716 Game mode currently closed \u2716");
		}}, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				player.sendMessage(ChatColor.RED + "This mode is currently closed and cannot be played.");
			}
		});
		
		//Back
		subMenu.addOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("PvP Games");
			}
		});
		
		//
		// 2v2 modes
		//
		subMenu = new SubMenu("2v2 Modes",36,this);
		
		//Fist
		subMenu.addOption(12, Material.RABBIT, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fist Fighting", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(PvPMode.TWOVTWO,PvPType.FIST) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Use your fists to fight the other players.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		}}, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				playerHandling.getPlayerData(player).setCustomDataKey("pvp_type", "1");
				mainInstance.getGameInstance().joinGame(player, Realm.PVP);
			}
		});
		
		//Sword/Armor
		subMenu.addOption(14, Material.IRON_SWORD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Weapon/Armor Fighting", new ArrayList<String>() {{
			add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(PvPMode.TWOVTWO,PvPType.WEPSARMOR) + ChatColor.YELLOW + " games active");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW + "Use your weapons/armor to fight the other players.");
			add(ChatColor.RESET.toString());
			add(ChatColor.RED.toString() + ChatColor.BOLD + "\u2716 Game mode currently closed \u2716");
		}}, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				player.sendMessage(ChatColor.RED + "This mode is currently closed and cannot be played.");
			}
		});
		
		//Back
		subMenu.addOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("PvP Games");
			}
		});
		
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
		open();
	}
}
