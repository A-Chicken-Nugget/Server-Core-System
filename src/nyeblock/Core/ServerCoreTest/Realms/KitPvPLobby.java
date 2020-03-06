package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

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
import nyeblock.Core.ServerCoreTest.Items.QueueGame;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Menus.KitPvPShop;
import nyeblock.Core.ServerCoreTest.Menus.SkyWarsShop;
import nyeblock.Core.ServerCoreTest.Misc.CustomNPC;
import nyeblock.Core.ServerCoreTest.Misc.CustomNPCManager;
import nyeblock.Core.ServerCoreTest.Misc.LevelXPBar;
import nyeblock.Core.ServerCoreTest.Misc.TextAnimation;
import nyeblock.Core.ServerCoreTest.Misc.Enums.CustomNPCType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class KitPvPLobby extends RealmBase {
	private PlayerHandling playerHandlingInstance;
	private CustomNPCManager customNPCManagerInstance;
	private World world = Bukkit.getWorld("KitPvPLobby");
	private HashMap<UUID,Hologram> playerHolograms = new HashMap<>();
	private TextAnimation boardAnimation;
	private CustomNPC topPlayerNPC;
	private CustomNPC joinGameNPC;
	private CustomNPC placeholderNPC;

	public KitPvPLobby(Main mainInstance) {
		super(mainInstance,Realm.KITPVP_LOBBY);
		playerHandlingInstance = mainInstance.getPlayerHandlingInstance();
		customNPCManagerInstance = mainInstance.getCustomNPCManagerInstance();
		boardAnimation = new TextAnimation(mainInstance,new ArrayList<String>() {{
			add("§e§lKit PvP");
			add("§6§lK§e§lit PvP");
			add("§e§lK§6§li§e§lt PvP");
			add("§e§lKi§6§lt §e§lPvP");
			add("§e§lKit §6§lP§e§lvP");
			add("§e§lKit P§6§lv§e§lP");
			add("§e§lKit Pv§6§lP");
			add("§e§lKit PvP");
			add("§6§lKit PvP");
			add("§e§lKit PvP");
			add("§6§lKit PvP");
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
					scores.put(7, ChatColor.YELLOW + "KitPvP Level");
					scores.put(6, ChatColor.GREEN.toString() + pd.getLevel(Realm.SKYWARS));
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
		mainInstance.getTimerInstance().createMethodTimer("kitPvPLobby_functions", .5, 0, "mainFunctions", false, null, this);
		
		//Create holograms above NPC's
		Hologram joinGameText = HologramsAPI.createHologram(mainInstance, new Location(world,13.5,166,14.5));
		joinGameText.appendTextLine(ChatColor.YELLOW + "Click to join a " + ChatColor.BOLD + "Kit PvP" + ChatColor.RESET + ChatColor.YELLOW + " game");
		joinGameText.appendTextLine(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.fromDBName(realm.getDBName().split("_")[0])) + ChatColor.YELLOW + " games active");
		joinGameText.appendItemLine(new ItemStack(Material.NETHER_STAR));
		
		//Update the active games
		mainInstance.getTimerInstance().createRunnableTimer("kitPvPLobby_gamesActiveUpdate", 5, 0, new Runnable() {
			@Override
			public void run() {
				joinGameText.clearLines();
				joinGameText.appendTextLine(ChatColor.YELLOW + "Click to join a " + ChatColor.BOLD + "Kit PvP" + ChatColor.RESET + ChatColor.YELLOW + " game");
				joinGameText.appendTextLine(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.fromDBName(realm.getDBName().split("_")[0])) + ChatColor.YELLOW + " games active");
				joinGameText.appendItemLine(new ItemStack(Material.NETHER_STAR));
			}
		});
		
		//Create NPC's
//		topPlayerNPC = customNPCManagerInstance.spawnNPC(null, "HonestMage60658", CustomNPCType.NORMAL, new Location(world,-81.5,91,492.5,154,0));
		joinGameNPC = customNPCManagerInstance.spawnNPC(null, null, CustomNPCType.JOIN_REALM, new Location(world,13.5,163,14.5,180,0));
		joinGameNPC.setRealm(Realm.KITPVP);
//		placeholderNPC = customNPCManagerInstance.spawnNPC(null, "JoueurDuGrenier", CustomNPCType.NORMAL, new Location(world,-85.5,91,471.5,0,0));
		
		ArrayList<String> hints = new ArrayList<String>() {{
			add(ChatColor.YELLOW + "This is a hint message");
		}};
		
		//Hint messages timer
		mainInstance.getTimerInstance().createRunnableTimer("kitPvPLobby_hintMessages", 90, 0, new Runnable() {
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
		
		//Kit PvP Shop
		KitPvPShop shop = new KitPvPShop(mainInstance,player);
		ItemStack s = shop.give();
		player.getInventory().setItem(2, s);
		
		//Queue item
		QueueGame queueGame = new QueueGame(mainInstance,player);
		ItemStack qg = queueGame.give();
		player.getInventory().setItem(4, qg);
		player.getInventory().setHeldItemSlot(4);
		
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
		
		return new Location(world,0.5,162,.5,180,0);
	}
	/**
	* When a player joins the hub
	*/
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		Hologram hologram = HologramsAPI.createHologram(mainInstance,new Location(world,16.5,164,8.5));
		int level = pd.getLevel(Realm.KITPVP);
		
		//Hologram content
		hologram.appendTextLine(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Your Kit PvP Level Info");
		hologram.appendTextLine(null);
		hologram.appendTextLine(ChatColor.YELLOW + "Level: " + ChatColor.GREEN + level);
		hologram.appendTextLine(ChatColor.YELLOW.toString() + level + " " + LevelXPBar.getBarText(50, pd.getXPFromLevel(level), pd.getXPFromLevel((level+1))) + " " + ChatColor.YELLOW + (level+1));
		hologram.appendTextLine(ChatColor.GREEN.toString() + (pd.getXPFromLevel((level+1))-pd.getXp(Realm.KITPVP)) + ChatColor.YELLOW + " XP till next level");
		hologram.appendItemLine(new ItemStack(Material.EMERALD));
		
		//Set it visible to only the player
		VisibilityManager visiblityManager = hologram.getVisibilityManager();
		visiblityManager.setVisibleByDefault(false);
		visiblityManager.showTo(ply);
		
		playerHolograms.put(ply.getUniqueId(),hologram);
		
		ply.teleport(new Location(world,0.5,162,.5,180,0));
		
		//Create the level npc for the player
		CustomNPC levelNpc = customNPCManagerInstance.spawnNPC(null, ply.getName(), CustomNPCType.NORMAL, new Location(world,16.5,160,8.5,124,0));
		pd.setCustomDataKey("level_npc", String.valueOf(levelNpc.getId()));
		
		levelNpc.showFor(ply);
//		topPlayerNPC.showFor(ply);
		joinGameNPC.showFor(ply);
//		placeholderNPC.showFor(ply);
		
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
		playerHolograms.get(ply.getUniqueId()).delete();
		playerHolograms.remove(ply.getUniqueId());
		
//		topPlayerNPC.removeFor(ply);
		joinGameNPC.removeFor(ply);
//		placeholderNPC.removeFor(ply);
		customNPCManagerInstance.deleteNPC(Integer.valueOf(pd.getCustomDataKey("level_npc")));
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandlingInstance.getPlayerData(player);
			
			pd2.removePlayerFromTeam(pd.getUserGroup().toString(), ply);
		}
	}
}