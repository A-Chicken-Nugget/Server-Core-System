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
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Items.HidePlayers;
import nyeblock.Core.ServerCoreTest.Items.QueueGame;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Menus.GameMenu;
import nyeblock.Core.ServerCoreTest.Menus.SkyWarsShop;
import nyeblock.Core.ServerCoreTest.Misc.Enums.CustomNPCType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.LevelXPBar;
import nyeblock.Core.ServerCoreTest.Misc.CustomNPC;
import nyeblock.Core.ServerCoreTest.Misc.CustomNPCManager;
import nyeblock.Core.ServerCoreTest.Misc.TextAnimation;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings("serial")
public class SkyWarsLobby extends RealmBase {
	private PlayerHandling playerHandlingInstance;
	private CustomNPCManager customNPCManagerInstance;
	private World world = Bukkit.getWorld("SkyWarsLobby");
	private HashMap<UUID,Hologram> playerHolograms = new HashMap<>();
	private TextAnimation boardAnimation;
	private CustomNPC joinGameNPC;
	
	public SkyWarsLobby(Main mainInstance) {
		super(mainInstance,Realm.SKYWARS_LOBBY);
		playerHandlingInstance = mainInstance.getPlayerHandlingInstance();
		customNPCManagerInstance = mainInstance.getCustomNPCManagerInstance();
		boardAnimation = new TextAnimation(mainInstance,new ArrayList<String>() {{
			add("§e§lSky Wars");
			add("§6§lS§e§lky Wars");
			add("§e§lS§6§lk§e§ly Wars");
			add("§e§lSk§6§ly §e§lWars");
			add("§e§lSky §6§lW§e§lars");
			add("§e§lSky W§6§la§e§lrs");
			add("§e§lSky Wa§6§lr§e§ls");
			add("§e§lSky War§6§ls");
			add("§e§lSky Wars");
			add("§6§lSky Wars");
			add("§e§lSky Wars");
			add("§6§lSky Wars");
		}},.3);
		
		//Scoreboard
		scoreboard = new Runnable() {
			@Override
			public void run() {
				for (Player ply : getPlayers(true)) {
					PlayerData pd = playerHandlingInstance.getPlayerData(ply);
					HashMap<Integer, String> scores = new HashMap<>();
					
					//Scoreboard
					scores.put(9, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					scores.put(8, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(7, ChatColor.YELLOW + "SkyWars Level");
					scores.put(6, ChatColor.GREEN.toString() + pd.getLevel(Realm.SKYWARS));
					scores.put(5, ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(4, ChatColor.YELLOW + "Points");
					scores.put(3, ChatColor.GREEN.toString() + (pd.getPoints() == -1 ? "Loading..." : pd.getPoints()) + ChatColor.RESET.toString());
					scores.put(2, ChatColor.RESET.toString());
					scores.put(1, ChatColor.GREEN + "http://nyeblock.com/");
					pd.setScoreboardTitle(boardAnimation.getMessage());
					pd.updateObjectiveScores(scores);
				}
			}
		};
		
		//Main functions timer
		mainInstance.getTimerInstance().createMethodTimer("skyWarsLobby_functions", .5, 0, "mainFunctions", false, null, this);
		
		//Create holograms above NPC's
		Hologram joinGameText = HologramsAPI.createHologram(mainInstance, new Location(world,2.5,202,10.5));
		joinGameText.appendTextLine(ChatColor.YELLOW + "Click to join a " + ChatColor.BOLD + "Sky Wars" + ChatColor.RESET + ChatColor.YELLOW + " game");
		joinGameText.appendTextLine(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.fromDBName(realm.getDBName().split("_")[0])) + ChatColor.YELLOW + " games active");
		joinGameText.appendItemLine(new ItemStack(Material.NETHER_STAR));
		
		//Update the active games
		mainInstance.getTimerInstance().createRunnableTimer("skyWarLobby_gamesActiveUpdate", 5, 0, new Runnable() {
			@Override
			public void run() {
				joinGameText.clearLines();
				joinGameText.appendTextLine(ChatColor.YELLOW + "Click to join a " + ChatColor.BOLD + "Sky Wars" + ChatColor.RESET + ChatColor.YELLOW + " game");
				joinGameText.appendTextLine(ChatColor.GREEN.toString() + mainInstance.getRealmHandlingInstance().getGamesCount(Realm.fromDBName(realm.getDBName().split("_")[0])) + ChatColor.YELLOW + " games active");
				joinGameText.appendItemLine(new ItemStack(Material.NETHER_STAR));
			}
		});
		
		//Create NPC's
		joinGameNPC = customNPCManagerInstance.spawnNPC(null, null, CustomNPCType.JOIN_REALM, new Location(world,2.5,199,10.5,164,0));
		joinGameNPC.setRealm(Realm.SKYWARS);
		
		ArrayList<String> hints = new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Use /hub or /leave to return to the hub");
			add(ChatColor.YELLOW + "Use the " + ChatColor.BOLD + "SHOP MENU " + ChatColor.RESET.toString() + ChatColor.YELLOW + "to purchase items and custom stuff");
			add(ChatColor.YELLOW + "Use /level to view your " + realm.toString() + " level");
		}};
		
		//Hint messages timer
		mainInstance.getTimerInstance().createRunnableTimer("skyWarLobby_hintMessages", 90, 0, new Runnable() {
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
		//Check if players are on island
		for (Player ply : getPlayers(true)) {
			if (!Toolkit.playerInArea(ply.getLocation().toVector(),new Vector(127,239,-122), new Vector(-123,71,118))) {
				ply.teleport(new Location(world,.5,200,.5,0,0));
			}
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
		
		//Game Menu
		GameMenu hubMenu = new GameMenu(mainInstance,player);
		ItemStack hm = hubMenu.give();
		player.getInventory().setItem(0, hm);
		
		//Sky Wars Shop
		SkyWarsShop shop = new SkyWarsShop(mainInstance,player);
		ItemStack s = shop.give();
		player.getInventory().setItem(2, s);
		
		//Queue item
		QueueGame queueGame = new QueueGame(mainInstance,player);
		ItemStack qg = queueGame.give();
		player.getInventory().setItem(4, qg);
		player.getInventory().setHeldItemSlot(4);
		
		//Hide players
		HidePlayers hidePlayers = new HidePlayers(mainInstance,player);
		ItemStack hp = hidePlayers.give();
		player.getInventory().setItem(6, hp);
		
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
		
		return new Location(world,.5,200,.5,0,0);
	}
	/**
	* When a player joins the hub
	*/
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		Hologram hologram = HologramsAPI.createHologram(mainInstance,new Location(world,-1.5,203,10.5));
		int level = pd.getLevel(Realm.SKYWARS);
		
		//Hologram content
		hologram.appendTextLine(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Your Sky Wars Level Info");
		hologram.appendTextLine(null);
		hologram.appendTextLine(ChatColor.YELLOW + "Level: " + ChatColor.GREEN + level);
		hologram.appendTextLine(ChatColor.YELLOW.toString() + level + " " + LevelXPBar.getBarText(50, pd.getXPFromLevel(level), pd.getXPFromLevel((level+1))) + " " + ChatColor.YELLOW + (level+1));
		hologram.appendTextLine(ChatColor.GREEN.toString() + (pd.getXPFromLevel((level+1))-pd.getXp(Realm.SKYWARS)) + ChatColor.YELLOW + " XP till next level");
		hologram.appendItemLine(new ItemStack(Material.EMERALD));
		
		//Set it visible to only the player
		VisibilityManager visiblityManager = hologram.getVisibilityManager();
		visiblityManager.setVisibleByDefault(false);
		visiblityManager.showTo(ply);
		
		playerHolograms.put(ply.getUniqueId(),hologram);
		
		ply.teleport(new Location(world,.5,200,.5,0,0));
		
		//Create the level npc for the player
		CustomNPC levelNpc = customNPCManagerInstance.spawnNPC(null, ply.getName(), CustomNPCType.NORMAL, new Location(world,-1.5,199,10.5,-164,0));
		pd.setCustomDataKey("level_npc", String.valueOf(levelNpc.getId()));
		
		levelNpc.showFor(ply);
		joinGameNPC.showFor(ply);
		
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
		Hologram levelHologram = playerHolograms.get(ply.getUniqueId());
		if (levelHologram != null) {			
			levelHologram.delete();
			playerHolograms.remove(ply.getUniqueId());
		}
		
		joinGameNPC.removeFor(ply);
		customNPCManagerInstance.deleteNPC(Integer.valueOf(pd.getCustomDataKey("level_npc")));
		
		//Remove players from teams
		for (Player player : getPlayers(true)) {
			PlayerData pd2 = playerHandlingInstance.getPlayerData(player);
			
			pd2.removePlayerFromTeam(pd.getPrimaryUserGroup().toString(), ply);
		}
	}
}
