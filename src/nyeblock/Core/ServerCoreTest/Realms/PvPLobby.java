package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Team;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Menus.PvPShop;
import nyeblock.Core.ServerCoreTest.Misc.Enums.CustomNPCType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.LevelXPBar;
import nyeblock.Core.ServerCoreTest.Misc.CustomNPC;
import nyeblock.Core.ServerCoreTest.Misc.CustomNPCManager;
import nyeblock.Core.ServerCoreTest.Misc.TextAnimation;

@SuppressWarnings("serial")
public class PvPLobby extends RealmBase {
	private PlayerHandling playerHandlingInstance;
	private CustomNPCManager customNPCManagerInstance;
	private World world = Bukkit.getWorld("PvPLobby");
	private Realm levelRealm = Realm.PVP_DUELS_FISTS;
	private int levelInterval = 0;
	private HashMap<Player,Hologram> playerLevelHolograms = new HashMap<>();
	private TextAnimation boardAnimation;
	private CustomNPC fistsDuelsNPC;
	private CustomNPC fists2v2NPC;
	
	public PvPLobby(Main mainInstance) {
		super(mainInstance,Realm.PVP_LOBBY);
		playerHandlingInstance = mainInstance.getPlayerHandlingInstance();
		customNPCManagerInstance = mainInstance.getCustomNPCManagerInstance();
		boardAnimation = new TextAnimation(mainInstance,new ArrayList<String>() {{
			add("§e§lPvP");
			add("§6§lP§e§lvP");
			add("§e§lP§6§lv§e§lP");
			add("§e§lPv§6§lP");
			add("§e§lPvP");
			add("§6§lPvP");
			add("§e§lPvP");
			add("§6§lPvP");
		}},.3);
		
		//Scoreboard
		scoreboard = new Runnable() {
			@Override
			public void run() {
				for (Player ply : players) {
					PlayerData pd = playerHandlingInstance.getPlayerData(ply);
					HashMap<Integer, String> scores = new HashMap<>();
					
					//Scoreboard
					scores.put(9, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					scores.put(8, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(7, ChatColor.YELLOW + levelRealm.toString() + " Level");
					scores.put(6, ChatColor.GREEN.toString() + pd.getLevel(levelRealm));
					scores.put(5, ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(4, ChatColor.YELLOW + "Points");
					scores.put(3, ChatColor.GREEN.toString() + (pd.getPoints() == -1 ? "Loading..." : pd.getPoints()));
					scores.put(2, ChatColor.RESET.toString());
					scores.put(1, ChatColor.GREEN + "http://nyeblock.com/");
					pd.setScoreboardTitle(boardAnimation.getMessage());
					pd.updateObjectiveScores(scores);
				}
			}
		};
		
		//Main functions timer
		mainInstance.getTimerInstance().createMethodTimer("pvplobby_functions", .5, 0, "mainFunctions", false, null, this);
		
		//Duels fists NPC
		fistsDuelsNPC = customNPCManagerInstance.spawnNPC(null, null, CustomNPCType.JOIN_REALM, new Location(world,-91.5,91,470.5,-24,0));
		fistsDuelsNPC.setRealm(Realm.PVP_DUELS_FISTS);
		Hologram fistsDuelsText = HologramsAPI.createHologram(mainInstance, new Location(world,-91.5,94,470.5));
		fistsDuelsText.appendTextLine(ChatColor.YELLOW + "Click to join a " + ChatColor.BOLD + "Duels \u00BB Fists" + ChatColor.RESET + ChatColor.YELLOW + " game");
		fistsDuelsText.appendTextLine(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.PVP_DUELS_FISTS) + ChatColor.YELLOW + " games active");
		fistsDuelsText.appendItemLine(new ItemStack(Material.NETHER_STAR));
		mainInstance.getTimerInstance().createRunnableTimer("pvplobby_duelsfistsTextUpdate", 5, 0, new Runnable() {
			@Override
			public void run() {
				fistsDuelsText.clearLines();
				fistsDuelsText.appendTextLine(ChatColor.YELLOW + "Click to join a " + ChatColor.BOLD + "Duels \u00BB Fists" + ChatColor.RESET + ChatColor.YELLOW + " game");
				fistsDuelsText.appendTextLine(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.PVP_DUELS_FISTS) + ChatColor.YELLOW + " games active");
				fistsDuelsText.appendItemLine(new ItemStack(Material.NETHER_STAR));
			}
		});
		
		//2v2 fists NPC
		fists2v2NPC = customNPCManagerInstance.spawnNPC(null, null, CustomNPCType.JOIN_REALM, new Location(world,-85.5,91,471.5,4,0));
		fists2v2NPC.setRealm(Realm.PVP_2V2_FISTS);
		Hologram fists2v2Text = HologramsAPI.createHologram(mainInstance, new Location(world,-85.5,94,471.5));
		fists2v2Text.appendTextLine(ChatColor.YELLOW + "Click to join a " + ChatColor.BOLD + "2v2 \u00BB Fists" + ChatColor.RESET + ChatColor.YELLOW + " game");
		fists2v2Text.appendTextLine(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.PVP_2V2_FISTS) + ChatColor.YELLOW + " games active");
		fists2v2Text.appendItemLine(new ItemStack(Material.NETHER_STAR));
		mainInstance.getTimerInstance().createRunnableTimer("pvplobby_2v2fistsTextUpdate", 5, 0, new Runnable() {
			@Override
			public void run() {
				fists2v2Text.clearLines();
				fists2v2Text.appendTextLine(ChatColor.YELLOW + "Click to join a " + ChatColor.BOLD + "2v2 \u00BB Fists" + ChatColor.RESET + ChatColor.YELLOW + " game");
				fists2v2Text.appendTextLine(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.PVP_2V2_FISTS) + ChatColor.YELLOW + " games active");
				fists2v2Text.appendItemLine(new ItemStack(Material.NETHER_STAR));
			}
		});
		
		ArrayList<String> hints = new ArrayList<String>() {{
			add(ChatColor.YELLOW + "This is a hint message");
		}};
		
		//Hint messages timer
		mainInstance.getTimerInstance().createRunnableTimer("pvplobby_hintMessages", 90, 0, new Runnable() {
			@Override
			public void run() {
				messageToAll(hints.get(new Random().nextInt(hints.size())));
			}
		});
	}
	
	/**
	* Main functions ran for the hub
	*/
	public void mainFunctions() {
		if (levelInterval < 20) {
			levelInterval += 1;
		} else {
			levelInterval = 0;
			
			if (levelRealm == Realm.PVP_DUELS_FISTS) {
				levelRealm = Realm.PVP_2V2_FISTS;
			} else {
				levelRealm = Realm.PVP_DUELS_FISTS;
			}
			for (Map.Entry<Player,Hologram> entry : playerLevelHolograms.entrySet()) {
				PlayerData pd = playerHandling.getPlayerData(entry.getKey());
				int level = pd.getLevel(levelRealm);
				Hologram hologram = entry.getValue();
				
				hologram.clearLines();
				hologram.appendTextLine(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Your " + levelRealm.toString() + " Level Info");
				hologram.appendTextLine(null);
				hologram.appendTextLine(ChatColor.YELLOW + "Level: " + ChatColor.GREEN + level);
				hologram.appendTextLine(ChatColor.YELLOW.toString() + level + " " + LevelXPBar.getBarText(50, pd.getXPFromLevel(level), pd.getXPFromLevel((level+1))) + " " + ChatColor.YELLOW + (level+1));
				hologram.appendTextLine(ChatColor.GREEN.toString() + (pd.getXPFromLevel((level+1))-pd.getXp(levelRealm)) + ChatColor.YELLOW + " XP till next level");
				hologram.appendItemLine(new ItemStack(Material.EMERALD));
			}
		}
		
		// Manage hub weather/time
		world.setTime(1000);
		if (world.hasStorm()) {
			world.setStorm(false);
		}
	}
	/**
    * Set the players permissions
    */
	public void setDefaultPermissions(Player player) {
		PermissionAttachment permissions = mainInstance.getPlayerHandlingInstance().getPlayerData(player).getPermissionAttachment();
		
		permissions.setPermission("nyeblock.canPlaceBlocks", false);
		permissions.setPermission("nyeblock.canBreakBlocks", false);
		permissions.setPermission("nyeblock.canUseInventory", false);
		permissions.setPermission("nyeblock.shouldDropItemsOnDeath", false);
		permissions.setPermission("nyeblock.canDamage", false);
		permissions.setPermission("nyeblock.canBeDamaged", false);
		permissions.setPermission("nyeblock.canTakeFallDamage", false);
		permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
		permissions.setPermission("nyeblock.canDropItems", false);
		permissions.setPermission("nyeblock.canLoseHunger", false);
		permissions.setPermission("nyeblock.canSwapItems", false);
		permissions.setPermission("nyeblock.canMove", true);
	}
	/**
	* Sets the players items
	* @param ply - Player to give the items to
	*/
	public void setItems(Player player) {
		player.getInventory().clear();
		
		//PvP Shop
		PvPShop shop = new PvPShop(mainInstance,player);
		ItemStack s = shop.give();
		player.getInventory().setItem(2, s);
		
		//Return to hub
		ReturnToHub returnToHub = new ReturnToHub(mainInstance,player);
		player.getInventory().setItem(8, returnToHub.give());
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		setItems(ply);
		
		return new Location(world,-86.5,91,482.5,90,0);
	}
	/**
	* When a player joins the hub
	*/
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		Hologram hologram = HologramsAPI.createHologram(mainInstance,new Location(world,-87.5,95,494.5));
		int level = pd.getLevel(levelRealm);
		
		//Hologram content
		hologram.appendTextLine(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Your " + levelRealm.toString() + " Level Info");
		hologram.appendTextLine(null);
		hologram.appendTextLine(ChatColor.YELLOW + "Level: " + ChatColor.GREEN + level);
		hologram.appendTextLine(ChatColor.YELLOW.toString() + level + " " + LevelXPBar.getBarText(50, pd.getXPFromLevel(level), pd.getXPFromLevel((level+1))) + " " + ChatColor.YELLOW + (level+1));
		hologram.appendTextLine(ChatColor.GREEN.toString() + (pd.getXPFromLevel((level+1))-pd.getXp(levelRealm)) + ChatColor.YELLOW + " XP till next level");
		hologram.appendItemLine(new ItemStack(Material.EMERALD));
		
		//Set it visible to only the player
		VisibilityManager visiblityManager = hologram.getVisibilityManager();
		visiblityManager.setVisibleByDefault(false);
		visiblityManager.showTo(ply);
		
		playerLevelHolograms.put(ply,hologram);
		
		ply.teleport(new Location(world,-86.5,91,482.5,90,0));
		
		//Create the level npc for the player
		CustomNPC levelNpc = customNPCManagerInstance.spawnNPC(null, ply.getName(), CustomNPCType.NORMAL, new Location(world,-87.5,91,494.5,-174,0));
		pd.setCustomDataKey("level_npc", String.valueOf(levelNpc.getId()));
		
		levelNpc.showFor(ply);
		fistsDuelsNPC.showFor(ply);
		fists2v2NPC.showFor(ply);
		
		//Setup team
		pd.setScoreBoardTeams(null,Team.OptionStatus.NEVER);
		
		//Add players to proper scoreboard teams
		updateTeamsFromUserGroups();
	}
	/**
	* When a player leaves the hub
	*/
	public void playerLeave(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		
		//Remove level hologram
		playerLevelHolograms.get(ply).delete();
		playerLevelHolograms.remove(ply);
		
		fistsDuelsNPC.removeFor(ply);
		fists2v2NPC.removeFor(ply);
		customNPCManagerInstance.deleteNPC(Integer.valueOf(pd.getCustomDataKey("level_npc")));
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandlingInstance.getPlayerData(player);
			
			pd2.removePlayerFromTeam(pd.getUserGroup().toString(), ply);
		}
	}
}
