package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.LevelXPBar;

@SuppressWarnings({"deprecation","serial"})
public class StatsMenu extends MenuBase {
	public StatsMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"stats_menu");
	}
	
	public void setContents() {
		PlayerHandling playerHandling = mainInstance.getPlayerHandlingInstance();
		PlayerData playerData = playerHandling.getPlayerData(player);
		SubMenu subMenu;		
		
		//
		// Profile/Stats menu
		//
		subMenu = new SubMenu("Stats Menu",27,this);
		
		//Realm Stats
		subMenu.createOption(12, Material.COMMAND_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Realm Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Your stats in each game realm on");
			add(ChatColor.YELLOW + "the server. This includes your level");
			add(ChatColor.YELLOW + "stats, games played and games won.");
			add(ChatColor.RESET.toString());
			add(ChatColor.GREEN + "\u279D \u279D Click to view your realm stats");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
		            	openMenu("Realm Stats",false);
		            }
				});
		}});
		
		//My Profile
//		subMenu.createOption(14, Material.SPONGE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Placeholder", new ArrayList<String>() {{
////			add(ChatColor.GREEN + "\u279D \u279D Click to view your profile");
//			add(ChatColor.RED + "\u2716 Currently not available \u2716");
//		}}, null);
		
		//
		// Realm Stats menu
		//
		subMenu = new SubMenu("Realm Stats",45,this);
		
		//Kitpvp
		int kitpvpLevel = playerData.getLevel(Realm.KITPVP);
		subMenu.createOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Level Info");
			add(ChatColor.YELLOW + "Level " + ChatColor.GREEN + kitpvpLevel);
			add(ChatColor.YELLOW.toString() + kitpvpLevel + " " + LevelXPBar.getBarText(50, playerData.getXPFromLevel(kitpvpLevel), playerData.getXPFromLevel((kitpvpLevel+1))) + " " + ChatColor.YELLOW + (kitpvpLevel+1));
			add(ChatColor.GREEN.toString() + (playerData.getXPFromLevel((kitpvpLevel+1))-playerData.getXp(Realm.KITPVP)) + ChatColor.YELLOW + " XP till next level");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Game Info");
			add(ChatColor.YELLOW + "Total games played: " + ChatColor.GREEN + playerData.getTotalGamesPlayed(Realm.KITPVP));
			add(ChatColor.YELLOW + "Games won: " + ChatColor.GREEN + playerData.getTotalGamesWon(Realm.KITPVP));
		}}, null);
		
		//Sky Wars
		int skywarsLevel = playerData.getLevel(Realm.SKYWARS);
		subMenu.createOption(13, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Level Info");
			add(ChatColor.YELLOW + "Level " + ChatColor.GREEN + playerData.getLevel(Realm.SKYWARS));
			add(ChatColor.YELLOW.toString() + skywarsLevel + " " + LevelXPBar.getBarText(50, playerData.getXPFromLevel(skywarsLevel), playerData.getXPFromLevel((skywarsLevel+1))) + " " + ChatColor.YELLOW + (skywarsLevel+1));
			add(ChatColor.GREEN.toString() + (playerData.getXPFromLevel((skywarsLevel+1))-playerData.getXp(Realm.SKYWARS)) + ChatColor.YELLOW + " XP till next level");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Game Info");
			add(ChatColor.YELLOW + "Total games played: " + ChatColor.GREEN + playerData.getTotalGamesPlayed(Realm.SKYWARS));
			add(ChatColor.YELLOW + "Games won: " + ChatColor.GREEN + playerData.getTotalGamesWon(Realm.SKYWARS));
		}}, null);
		
		//Step Spleef
		int stepspleefLevel = playerData.getLevel(Realm.STEPSPLEEF);
		subMenu.createOption(15, Material.IRON_BOOTS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Level Info");
			add(ChatColor.YELLOW + "Level " + ChatColor.GREEN + playerData.getLevel(Realm.STEPSPLEEF));
			add(ChatColor.YELLOW.toString() + stepspleefLevel + " " + LevelXPBar.getBarText(50, playerData.getXPFromLevel(skywarsLevel), playerData.getXPFromLevel((stepspleefLevel+1))) + " " + ChatColor.YELLOW + (stepspleefLevel+1));
			add(ChatColor.GREEN.toString() + (playerData.getXPFromLevel((stepspleefLevel+1))-playerData.getXp(Realm.STEPSPLEEF)) + ChatColor.YELLOW + " XP till next level");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Game Info");
			add(ChatColor.YELLOW + "Total games played: " + ChatColor.GREEN + playerData.getTotalGamesPlayed(Realm.STEPSPLEEF));
			add(ChatColor.YELLOW + "Games won: " + ChatColor.GREEN + playerData.getTotalGamesWon(Realm.STEPSPLEEF));
		}}, null);
		
		//PvP
		subMenu.createOption(21, Material.FISHING_ROD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP Stats", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view your PvP stats");
		}}, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
		            	openMenu("PvP Stats",false);
		            }
				});
		}});
		
		//Back
		subMenu.createOption(36, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						openMenu("Stats Menu",false);
					}
				});
		}});
		
		//
		// PvP Stats
		//
		subMenu = new SubMenu("PvP Stats",36,this);
		
		//Duels >> Fists
		int duelsfistsLevel = playerData.getLevel(Realm.PVP_DUELS_FISTS);
		subMenu.createOption(12, Material.CHICKEN, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Duels \u00BB Fists Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Level Info");
			add(ChatColor.YELLOW + "Level " + ChatColor.GREEN + duelsfistsLevel);
			add(ChatColor.YELLOW.toString() + duelsfistsLevel + " " + LevelXPBar.getBarText(50, playerData.getXPFromLevel(duelsfistsLevel), playerData.getXPFromLevel((duelsfistsLevel+1))) + " " + ChatColor.YELLOW + (duelsfistsLevel+1));
			add(ChatColor.GREEN.toString() + (playerData.getXPFromLevel((duelsfistsLevel+1))-playerData.getXp(Realm.PVP_DUELS_FISTS)) + ChatColor.YELLOW + " XP till next level");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Game Info");
			add(ChatColor.YELLOW + "Total games played: " + ChatColor.GREEN + playerData.getTotalGamesPlayed(Realm.PVP_DUELS_FISTS));
			add(ChatColor.YELLOW + "Games won: " + ChatColor.GREEN + playerData.getTotalGamesWon(Realm.PVP_DUELS_FISTS));
		}}, null);
		
		//2v2 >> Fists
		int twovtwofistsLevel = playerData.getLevel(Realm.PVP_2V2_FISTS);
		subMenu.createOption(14, Material.COOKED_CHICKEN, ChatColor.YELLOW.toString() + ChatColor.BOLD + "2v2 \u00BB Fists Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Level Info");
			add(ChatColor.YELLOW + "Level " + ChatColor.GREEN + twovtwofistsLevel);
			add(ChatColor.YELLOW.toString() + twovtwofistsLevel + " " + LevelXPBar.getBarText(50, playerData.getXPFromLevel(twovtwofistsLevel), playerData.getXPFromLevel((twovtwofistsLevel+1))) + " " + ChatColor.YELLOW + (twovtwofistsLevel+1));
			add(ChatColor.GREEN.toString() + (playerData.getXPFromLevel((twovtwofistsLevel+1))-playerData.getXp(Realm.PVP_2V2_FISTS)) + ChatColor.YELLOW + " XP till next level");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Game Info");
			add(ChatColor.YELLOW + "Total games played: " + ChatColor.GREEN + playerData.getTotalGamesPlayed(Realm.PVP_2V2_FISTS));
			add(ChatColor.YELLOW + "Games won: " + ChatColor.GREEN + playerData.getTotalGamesWon(Realm.PVP_2V2_FISTS));
		}}, null);
		
		//Back
		subMenu.createOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new HashMap<ClickType,Runnable>() {{
				put(ClickType.LEFT,new Runnable() {
					@Override
					public void run() {
						openMenu("Realm Stats",false);
					}
				});
		}});
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta shopMeta = (SkullMeta)item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "My Stats" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("stats_menu");
		shopMeta.setOwner(player.getName());
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
