package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class HubMenu extends MenuBase {
	public HubMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"hub_menu",36);
		
		mainInstance.getTimerInstance().createTimer2("invRefresh_" + player.getUniqueId(), 10, 0, new Runnable() {
			@Override
			public void run() {
				setContents(true);
			}
		});
		setContents(false);
	}
	
	public void setContents(boolean shouldRefresh) {
		//
		// Game menu
		//
		
		//Kitpvp
		ItemStack kitPvp = new ItemStack(Material.IRON_AXE);
		ItemMeta kitPvpMeta = kitPvp.getItemMeta();
		kitPvpMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp (Beta)");
		kitPvpMeta.setLocalizedName("kitPvP");
		kitPvpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> kitPvpMetaLore = new ArrayList<String>();
		kitPvpMetaLore.add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.KITPVP) + ChatColor.YELLOW + " games active");
		kitPvpMetaLore.add(ChatColor.RESET.toString());
		kitPvpMetaLore.add(ChatColor.YELLOW + "Fight other players and");
		kitPvpMetaLore.add(ChatColor.YELLOW + "try to kill as many as possible.");
		kitPvpMetaLore.add(ChatColor.YELLOW + "At the end the player with the");
		kitPvpMetaLore.add(ChatColor.YELLOW + "highest kills wins. Select up to 4 kits.");
		kitPvpMetaLore.add(ChatColor.RESET.toString());
		kitPvpMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		kitPvpMeta.setLore(kitPvpMetaLore);
		kitPvp.setItemMeta(kitPvpMeta);
		super.addOption("Game Menu", 11, kitPvp, new Runnable() {
            @Override
            public void run() {       
            	mainInstance.getGameInstance().joinGame(player, Realm.KITPVP);
            	player.closeInventory();
            }
		});
		//Sky wars
		ItemStack skyWars = new ItemStack(Material.GRASS_BLOCK);
		ItemMeta skyWarsMeta = skyWars.getItemMeta();
		skyWarsMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars (Beta)");
		skyWarsMeta.setLocalizedName("skyWars");
		skyWarsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> skyWarsMetaLore = new ArrayList<String>();
		skyWarsMetaLore.add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.SKYWARS) + ChatColor.YELLOW + " games active");
		skyWarsMetaLore.add(ChatColor.RESET.toString());
		skyWarsMetaLore.add(ChatColor.YELLOW + "Each player starts with their");
		skyWarsMetaLore.add(ChatColor.YELLOW + "own island. You must craft and");
		skyWarsMetaLore.add(ChatColor.YELLOW + "build your way to the center of");
		skyWarsMetaLore.add(ChatColor.YELLOW + "the map for better loot. The");
		skyWarsMetaLore.add(ChatColor.YELLOW + "last player left wins!");
		skyWarsMetaLore.add(ChatColor.RESET.toString());
		skyWarsMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		skyWarsMeta.setLore(skyWarsMetaLore);
		skyWars.setItemMeta(skyWarsMeta);
		super.addOption("Game Menu", 13, skyWars, new Runnable() {
            @Override
            public void run() {       
            	mainInstance.getGameInstance().joinGame(player, Realm.SKYWARS);
            	player.closeInventory();
            }
		});
		//Step Spleef
		ItemStack stepSpleef = new ItemStack(Material.IRON_BOOTS);
		ItemMeta stepSpleefMeta = stepSpleef.getItemMeta();
		stepSpleefMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef (Beta)");
		stepSpleefMeta.setLocalizedName("stepSpleef");
		stepSpleefMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> stepSpleefMetaLore = new ArrayList<String>();
		stepSpleefMetaLore.add(ChatColor.GREEN.toString() + mainInstance.getGameInstance().getGamesCount(Realm.STEPSPLEEF) + ChatColor.YELLOW + " games active");
		stepSpleefMetaLore.add(ChatColor.RESET.toString());
		stepSpleefMetaLore.add(ChatColor.YELLOW + "Dodge and weave to survive");
		stepSpleefMetaLore.add(ChatColor.YELLOW + "when the blocks you've walked");
		stepSpleefMetaLore.add(ChatColor.YELLOW + "on get deleted shortly after.");
		stepSpleefMetaLore.add(ChatColor.YELLOW + "The last player left wins!");
		stepSpleefMetaLore.add(ChatColor.RESET.toString());
		stepSpleefMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		stepSpleefMeta.setLore(stepSpleefMetaLore);
		stepSpleef.setItemMeta(stepSpleefMeta);
		super.addOption("Game Menu", 15, stepSpleef, new Runnable() {
            @Override
            public void run() {       
            	mainInstance.getGameInstance().joinGame(player, Realm.STEPSPLEEF);
            	player.closeInventory();
            }
		});
		//PvP
		ItemStack pvp = new ItemStack(Material.FISHING_ROD);
		ItemMeta pvpMeta = pvp.getItemMeta();
		pvpMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP");
		pvpMeta.setLocalizedName("pvp_games");
		pvpMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> pvpMetaLore = new ArrayList<String>();
		pvpMetaLore.add(ChatColor.YELLOW + "Fight against other players");
		pvpMetaLore.add(ChatColor.YELLOW + "with the items the gamemode you");
		pvpMetaLore.add(ChatColor.YELLOW + "choose provides.");
		pvpMetaLore.add(ChatColor.RESET.toString());
		pvpMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to view the game modes");
		pvpMeta.setLore(pvpMetaLore);
		pvp.setItemMeta(pvpMeta);
		super.addOption("Game Menu", 21, pvp, new Runnable() {
            @Override
            public void run() {       
            	mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("PvP Games");
            }
		});
		
		//
		// PvP games
		//
		
		//Duels
		ItemStack duels = new ItemStack(Material.GOLDEN_SWORD);
		ItemMeta duelsMeta = duels.getItemMeta();
		duelsMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Duels");
		duelsMeta.setLocalizedName("pvp_duels");
		duelsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> duelsMetaLore = new ArrayList<String>();
		duelsMetaLore.add(ChatColor.RESET.toString());
		duelsMetaLore.add(ChatColor.YELLOW + "Fight one other player.");
		duelsMetaLore.add(ChatColor.RESET.toString());
		duelsMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to view the modes");
		duelsMeta.setLore(duelsMetaLore);
		duels.setItemMeta(duelsMeta);
		super.addOption("PvP Games", 12, duels, new Runnable() {
			@Override
			public void run() {
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).setCustomDataKey("pvp_mode", "1");
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("Duels Modes");
			}
		});
		//2v2
		ItemStack dousvdous = new ItemStack(Material.IRON_SWORD);
		ItemMeta dousvdousMeta = dousvdous.getItemMeta();
		dousvdousMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "2v2");
		dousvdousMeta.setLocalizedName("pvp_2v2");
		dousvdousMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> dousvdousMetaLore = new ArrayList<String>();
		dousvdousMetaLore.add(ChatColor.RESET.toString());
		dousvdousMetaLore.add(ChatColor.YELLOW + "Fight 2 other players with a teammate.");
		dousvdousMetaLore.add(ChatColor.RESET.toString());
		dousvdousMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to view the modes");
		dousvdousMeta.setLore(dousvdousMetaLore);
		dousvdous.setItemMeta(dousvdousMeta);
		super.addOption("PvP Games", 14, dousvdous, new Runnable() {
			@Override
			public void run() {
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).setCustomDataKey("pvp_mode", "2");
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("2v2 Modes");
			}
		});
		//Back
		ItemStack goBack1 = new ItemStack(Material.RED_WOOL);
		ItemMeta goBack1Meta = goBack1.getItemMeta();
		goBack1Meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back");
		goBack1Meta.setLocalizedName("back_1");
		goBack1.setItemMeta(goBack1Meta);
		super.addOption("PvP Games", 27, goBack1, new Runnable() {
			@Override
			public void run() {
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("Game Menu");
			}
		});
		
		//
		// Duels modes
		//
		
		//Fist
		ItemStack fists = new ItemStack(Material.RABBIT);
		ItemMeta fistsMeta = fists.getItemMeta();
		fistsMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fist Fighting");
		fistsMeta.setLocalizedName("pvp_duels_fist");
		fistsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> fistsMetaLore = new ArrayList<String>();
		fistsMetaLore.add(ChatColor.RESET.toString());
		fistsMetaLore.add(ChatColor.YELLOW + "Use your fists to fight the other player.");
		fistsMetaLore.add(ChatColor.RESET.toString());
		fistsMetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		fistsMeta.setLore(fistsMetaLore);
		fists.setItemMeta(fistsMeta);
		super.addOption("Duels Modes", 12, fists, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).setCustomDataKey("pvp_type", "1");
				mainInstance.getGameInstance().joinGame(player, Realm.PVP);
			}
		});
		//Sword/Armor
		ItemStack weps = new ItemStack(Material.IRON_SWORD);
		ItemMeta wepsMeta = weps.getItemMeta();
		wepsMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Weapon/Armor Fighting");
		wepsMeta.setLocalizedName("pvp_duels_weps");
		wepsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> wepsMetaLore = new ArrayList<String>();
		wepsMetaLore.add(ChatColor.RESET.toString());
		wepsMetaLore.add(ChatColor.YELLOW + "Use your weps to fight the other player.");
		wepsMetaLore.add(ChatColor.RESET.toString());
		wepsMetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "\u2716 Game mode currently closed \u2716");
		wepsMeta.setLore(wepsMetaLore);
		weps.setItemMeta(wepsMeta);
		super.addOption("Duels Modes", 14, weps, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				player.sendMessage(ChatColor.RED + "This mode is currently closed and cannot be played.");
			}
		});
		//Back
		ItemStack goBack2 = new ItemStack(Material.RED_WOOL);
		ItemMeta goBack2Meta = goBack2.getItemMeta();
		goBack2Meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back");
		goBack2Meta.setLocalizedName("back_2");
		goBack2.setItemMeta(goBack2Meta);
		super.addOption("Duels Modes", 27, goBack2, new Runnable() {
			@Override
			public void run() {
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("PvP Games");
			}
		});
		
		//
		// 2v2 modes
		//
		
		//Fist
		ItemStack fists2v2 = new ItemStack(Material.RABBIT);
		ItemMeta fists2v2Meta = fists2v2.getItemMeta();
		fists2v2Meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fist Fighting");
		fists2v2Meta.setLocalizedName("pvp_2v2_fist");
		fists2v2Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> fists2v2MetaLore = new ArrayList<String>();
		fists2v2MetaLore.add(ChatColor.RESET.toString());
		fists2v2MetaLore.add(ChatColor.YELLOW + "Use your fists to fight the other players.");
		fists2v2MetaLore.add(ChatColor.RESET.toString());
		fists2v2MetaLore.add(ChatColor.GREEN + "\u279D \u279D Click to find a game");
		fists2v2Meta.setLore(fists2v2MetaLore);
		fists2v2.setItemMeta(fists2v2Meta);
		super.addOption("2v2 Modes", 12, fists2v2, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).setCustomDataKey("pvp_type", "1");
				mainInstance.getGameInstance().joinGame(player, Realm.PVP);
			}
		});
		//Sword/Armor
		ItemStack weps2v2 = new ItemStack(Material.IRON_SWORD);
		ItemMeta weps2v2Meta = weps2v2.getItemMeta();
		weps2v2Meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Weapon/Armor Fighting");
		weps2v2Meta.setLocalizedName("pvp_2v2_weps");
		weps2v2Meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		ArrayList<String> weps2v2MetaLore = new ArrayList<String>();
		weps2v2MetaLore.add(ChatColor.RESET.toString());
		weps2v2MetaLore.add(ChatColor.YELLOW + "Use your weapons/armor to fight the other players.");
		weps2v2MetaLore.add(ChatColor.RESET.toString());
		weps2v2MetaLore.add(ChatColor.RED.toString() + ChatColor.BOLD + "\u2716 Game mode currently closed \u2716");
		weps2v2Meta.setLore(weps2v2MetaLore);
		weps2v2.setItemMeta(weps2v2Meta);
		super.addOption("2v2 Modes", 14, weps2v2, new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
				player.sendMessage(ChatColor.RED + "This mode is currently closed and cannot be played.");
			}
		});
		//Back
		ItemStack goBack3 = new ItemStack(Material.RED_WOOL);
		ItemMeta goBack3Meta = goBack3.getItemMeta();
		goBack3Meta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back");
		goBack3Meta.setLocalizedName("back_2");
		goBack3.setItemMeta(goBack3Meta);
		super.addOption("2v2 Modes", 27, goBack3, new Runnable() {
			@Override
			public void run() {
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).getMenu().openMenu("PvP Games");
			}
		});
		if (shouldRefresh) {
			openMenu("Game Menu");
		}
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.NETHER_STAR);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Game Menu" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("hub_menu");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	public void onDelete() {
		mainInstance.getTimerInstance().deleteTimer("invRefresh_" + player.getUniqueId());
	}
	//Use the item
	public void use(ItemStack item) {
		setContents(true);
	}
}
