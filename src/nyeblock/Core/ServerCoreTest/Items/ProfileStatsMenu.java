package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Interfaces.SubMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.LevelXPBar;

@SuppressWarnings({"deprecation","serial"})
public class ProfileStatsMenu extends MenuBase {
	public ProfileStatsMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"profile_stats_menu");
	}
	
	public void setContents() {
		PlayerHandling playerHandling = mainInstance.getPlayerHandlingInstance();
		PlayerData playerData = playerHandling.getPlayerData(player);
		SubMenu subMenu;		
		
		//
		// Profile/Stats menu
		//
		subMenu = new SubMenu("Profile/Stats Menu",27);
		
		//My Stats
		subMenu.addOption(12, Material.COMMAND_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "My Stats", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view your stats");
		}}, new Runnable() {
            @Override
            public void run() {
            	playerData.getMenu().openMenu("My Stats");
            }
		});
		
		//My Profile
		subMenu.addOption(14, Material.SPONGE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "My Profile", new ArrayList<String>() {{
//			add(ChatColor.GREEN + "\u279D \u279D Click to view your profile");
			add(ChatColor.RED + "\u2716 Currently not available \u2716");
		}}, new Runnable() {
            @Override
            public void run() {
//            	playerData.getMenu().openMenu("My Profile");
            }
		});
		
		super.addSubMenu(subMenu);
		//
		// My Stats menu
		//
		subMenu = new SubMenu("My Stats",36);
		
		//Kitpvp
		int kitpvpLevel = playerData.getLevel(Realm.KITPVP);
		subMenu.addOption(11, Material.IRON_AXE, ChatColor.YELLOW.toString() + ChatColor.BOLD + "KitPvp Stats", new ArrayList<String>() {{
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
		subMenu.addOption(13, Material.GRASS_BLOCK, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Sky Wars Stats", new ArrayList<String>() {{
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
		subMenu.addOption(15, Material.IRON_BOOTS, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Step Spleef Stats", new ArrayList<String>() {{
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
		subMenu.addOption(21, Material.FISHING_ROD, ChatColor.YELLOW.toString() + ChatColor.BOLD + "PvP Stats", new ArrayList<String>() {{
			add(ChatColor.GREEN + "\u279D \u279D Click to view your PvP stats");
		}}, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("PvP Stats");
			}
		});
		
		//Back
		subMenu.addOption(27, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("Profile/Stats Menu");
			}
		});
		
		super.addSubMenu(subMenu);
		//
		// PvP Stats
		//
		subMenu = new SubMenu("PvP Stats",27);
		
		//Duels >> Fists
		int duelsfistsLevel = playerData.getLevel(PvPMode.DUELS,PvPType.FIST);
		subMenu.addOption(12, Material.CHICKEN, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Duels \u00BB Fists Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Level Info");
			add(ChatColor.YELLOW + "Level " + ChatColor.GREEN + duelsfistsLevel);
			add(ChatColor.YELLOW.toString() + duelsfistsLevel + " " + LevelXPBar.getBarText(50, playerData.getXPFromLevel(duelsfistsLevel), playerData.getXPFromLevel((duelsfistsLevel+1))) + " " + ChatColor.YELLOW + (duelsfistsLevel+1));
			add(ChatColor.GREEN.toString() + (playerData.getXPFromLevel((duelsfistsLevel+1))-playerData.getXp(PvPMode.DUELS,PvPType.FIST)) + ChatColor.YELLOW + " XP till next level");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Game Info");
			add(ChatColor.YELLOW + "Total games played: " + ChatColor.GREEN + playerData.getTotalGamesPlayed(PvPMode.DUELS,PvPType.FIST));
			add(ChatColor.YELLOW + "Games won: " + ChatColor.GREEN + playerData.getTotalGamesWon(PvPMode.DUELS,PvPType.FIST));
		}}, null);
		
		//2v2 >> Fists
		int twovtwofistsLevel = playerData.getLevel(PvPMode.TWOVTWO,PvPType.FIST);
		subMenu.addOption(14, Material.COOKED_CHICKEN, ChatColor.YELLOW.toString() + ChatColor.BOLD + "2v2 \u00BB Fists Stats", new ArrayList<String>() {{
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Level Info");
			add(ChatColor.YELLOW + "Level " + ChatColor.GREEN + twovtwofistsLevel);
			add(ChatColor.YELLOW.toString() + twovtwofistsLevel + " " + LevelXPBar.getBarText(50, playerData.getXPFromLevel(twovtwofistsLevel), playerData.getXPFromLevel((twovtwofistsLevel+1))) + " " + ChatColor.YELLOW + (twovtwofistsLevel+1));
			add(ChatColor.GREEN.toString() + (playerData.getXPFromLevel((twovtwofistsLevel+1))-playerData.getXp(PvPMode.TWOVTWO,PvPType.FIST)) + ChatColor.YELLOW + " XP till next level");
			add(ChatColor.RESET.toString());
			add(ChatColor.YELLOW.toString() + ChatColor.ITALIC + "Game Info");
			add(ChatColor.YELLOW + "Total games played: " + ChatColor.GREEN + playerData.getTotalGamesPlayed(PvPMode.TWOVTWO,PvPType.FIST));
			add(ChatColor.YELLOW + "Games won: " + ChatColor.GREEN + playerData.getTotalGamesWon(PvPMode.TWOVTWO,PvPType.FIST));
		}}, null);
		
		//Back
		subMenu.addOption(18, Material.RED_WOOL, ChatColor.YELLOW.toString() + ChatColor.BOLD + "Go back", null, new Runnable() {
			@Override
			public void run() {
				playerHandling.getPlayerData(player).getMenu().openMenu("My Stats");
			}
		});
		
		super.addSubMenu(subMenu);
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta shopMeta = (SkullMeta)item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "My Profile/Stats" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("profile_stats_menu");
		shopMeta.setOwner(player.getName());
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		setContents();
		open();
	}
}
