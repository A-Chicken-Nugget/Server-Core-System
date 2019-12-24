package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;

import nyeblock.Core.ServerCoreTest.Items.HidePlayers;
import nyeblock.Core.ServerCoreTest.Items.HubMenu;
import nyeblock.Core.ServerCoreTest.Items.ItemBase;
import nyeblock.Core.ServerCoreTest.Items.KitSelector;
import nyeblock.Core.ServerCoreTest.Items.MenuBase;
import nyeblock.Core.ServerCoreTest.Items.ParkourMenu;
import nyeblock.Core.ServerCoreTest.Items.PlayerSelector;
import nyeblock.Core.ServerCoreTest.Items.ProfileStatsMenu;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Items.ReturnToStart;
import nyeblock.Core.ServerCoreTest.Items.ShopMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;
import nyeblock.Core.ServerCoreTest.Realms.SkyWars;
import nyeblock.Core.ServerCoreTest.Realms.StepSpleef;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings({"deprecation","unused"})
public class PlayerData {
	//Instances
	private Main mainInstance;
	private DatabaseHandling databaseHandlingInstance;
	//Instance variables
	private Player player;
	private int id;
	private int points;
	private double timePlayed;
	private long timeJoined = System.currentTimeMillis() / 1000L;
	private String ip;
	private UserGroup userGroup = UserGroup.USER;
	private PermissionAttachment permissions;
	private Realm realm = Realm.HUB;
	private HashMap<String,String> customData = new HashMap<>();
	private HashMap<String,ItemBase> customItems = new HashMap<>();
	private MenuBase openedMenu;
	private RealmBase currentGame;
	private boolean isSpectating = false;
	private boolean queuingGame = false;
	//Stats
	private HashMap<String,Integer> realmXp = new HashMap<>();
	private HashMap<String,Integer> totalGamesPlayed = new HashMap<>();
	private HashMap<String,Integer> totalGamesWon = new HashMap<>();
	//Scoreboard
	private Scoreboard board;
	private Objective objective;
	
	public PlayerData(Main mainInstance, Player ply) {
		this.mainInstance = mainInstance;
		currentGame = mainInstance.getHubInstance();
		databaseHandlingInstance = mainInstance.getDatabaseInstance();
		player = ply;
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainInstance, new Runnable() {
			@Override
			public void run() {				
				// Teleport to spawn
				player.teleport(new Location(Bukkit.getWorld("world"),-9.548, 113, -11.497));
				createScoreboard();
			}
		});
	}
	
	/**
    * Add game played
    * @param realm - realm to add to the games played
    * @param won - did the player win
    */
	public void addGamePlayed(Realm realm, boolean won) {
		totalGamesPlayed.put(realm.getDBName(), totalGamesPlayed.get(realm.getDBName())+1);
		if (won) {
			totalGamesWon.put(realm.getDBName(), totalGamesWon.get(realm.getDBName()));
		}
	}
	/**
    * Add game played
    * @param mode - The PvP mode
	* @param type - The PvP type
	* @param won - did the player win
    */
	public void addGamePlayed(PvPMode mode, PvPType type, boolean won) {
		totalGamesPlayed.put(mode.getDBName() + "_" + type.getDBName(), totalGamesPlayed.get(mode.getDBName() + "_" + type.getDBName())+1);
		if (won) {
			totalGamesWon.put(mode.getDBName() + "_" + type.getDBName(), totalGamesWon.get(mode.getDBName() + "_" + type.getDBName()));
		}
	}
	/**
    * Give the player xp
    * @param realm - realm to give the xp should be added to
    * @param amount - amount of xp added to the specified realm
    */
	public void giveXP(Realm realm, int amount) {
		int currentLevel = getLevel(realm);
		
		if (realmXp.get(realm.getDBName()) != null) {			
			realmXp.put(realm.getDBName(), realmXp.get(realm.getDBName()) + amount);
		} else {
			realmXp.put(realm.getDBName(), amount);
		}
		
		int newLevel = getLevel(realm);
		if (currentLevel < newLevel) {
			player.sendMessage(ChatColor.YELLOW + "You have leveled up! You are new level " + ChatColor.GREEN + newLevel + ChatColor.YELLOW + "!");
		}
	}
	/**
    * Give the player xp
    * @param realm - realm to give the xp should be added to
    * @param amount - amount of xp added to the specified realm
    */
	public void giveXP(PvPMode mode, PvPType type, int amount) {
		int currentLevel = getLevel(mode,type);
		
		if (realmXp.get(mode.getDBName() + "_" + type.getDBName()) != null) {			
			realmXp.put(mode.getDBName() + "_" + type.getDBName(), realmXp.get(mode.getDBName() + "_" + type.getDBName()) + amount);
		} else {
			realmXp.put(mode.getDBName() + "_" + type.getDBName(), amount);
		}
		
		int newLevel = getLevel(mode,type);
		if (currentLevel < newLevel) {
			player.sendMessage(ChatColor.YELLOW + "You have leveled up! Your " + realm.toString() + " level is now " + ChatColor.GREEN + newLevel + ChatColor.YELLOW + "!");
		}
	}
	/**
    * Creates a scoreboard for the player
    */
	public void createScoreboard() {
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = board.registerNewObjective("scoreboard", "");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		player.setScoreboard(board);
	}
	/**
    * Clear the players scoreboard teams, scores and health tags
    */
	public void clearScoreboard() {
		for (Team team : board.getTeams()) {
			team.unregister();
		}
		for (String score : board.getEntries()) {
			board.resetScores(score);
		}
		for (Objective objective : board.getObjectives()) {
			if (objective.getName().equals("healthtag")) {
				objective.unregister();
			}
		}
	}
	/**
    * Add the given player to a specific team within the players scoreboard
    * @param teamName - name of the team to add the player to.
    * @param ply - player to add to the specified team.
    */
	public void addPlayerToTeam(String teamName, Player ply) {
		Team team = board.getTeam(teamName);
		
		if (team != null) {
			team.addPlayer(ply);
		} else {
			mainInstance.getTimerInstance().createTimer2("teamRecheck_" + ply.getUniqueId(), .5, 2, new Runnable() {
				@Override
				public void run() {
					if (board.getTeam(teamName) != null) {						
						addPlayerToTeam(teamName,ply);
					}
				}
			});
		}
	}
	/**
    * Remove a player from a specific team within the players scoreboard
    * @param teamName - name of the team to remove the player from.
    * @param ply - player to remove from the specified team.
    */
	public void removePlayerFromTeam(String teamName, Player ply) {
		for (Team team : board.getTeams()) {
			if (team.getName().equals(teamName)) {
				if (team.hasPlayer(ply)) {					
					team.removePlayer(ply);
				}
			}
		}
	}
	/**
    * Create health tags to be displayed with the players scoreboard
    */
	public void createHealthTags() {
		if (board.getObjective("healthtag") == null) {			
			Objective healthTag = board.registerNewObjective("healthtag", "health");
			healthTag.setDisplaySlot(DisplaySlot.BELOW_NAME);
			healthTag.setDisplayName(ChatColor.DARK_RED + "\u2764");
			player.setHealth(player.getHealth() - 0.0001);
		}
	}
	
	//
	// GETTERS
	//
	
	/**
	* Get total games won
	* @return the players total amount of games won for each realm
	*/
	public HashMap<String,Integer> getTotalGamesWon() {
		return totalGamesWon;
	}
	/**
	* Get total games played
	* @return the players total amount of games played for each realm
	*/
	public HashMap<String,Integer> getTotalGamesPlayed() {
		return totalGamesPlayed;
	}
	/**
	* Get total games won
	* @param realm - the realm
	* @return the players total amount of games won for specified realm
	*/
	public int getTotalGamesWon(Realm realm) {
		return totalGamesWon.get(realm.getDBName());
	}
	/**
	* Get total games won
	* @param mode - pvp mode
	* @param type - pvp type
	* @return the players total amount of games won for specified pvp mode
	*/
	public int getTotalGamesWon(PvPMode mode, PvPType type) {
		return totalGamesWon.get(mode.getDBName() + "_" + type.getDBName());
	}
	/**
	* Get total games played
	* @param realm - the realm
	* @return the players total amount of games played for specified realm
	*/
	public int getTotalGamesPlayed(Realm realm) {
		return totalGamesPlayed.get(realm.getDBName());
	}
	/**
	* Get total games played
	* @param mode - pvp mode
	* @param type - pvp type
	* @return the players total amount of games played for specified pvp mode
	*/
	public int getTotalGamesPlayed(PvPMode mode, PvPType type) {
		return totalGamesPlayed.get(mode.getDBName() + "_" + type.getDBName());
	}
	/**
	* Get the player level
	* @param realm - The realm go get the players level from
	* @return the players level in the provided realm
	*/
	public int getLevel(Realm realm) {
		return (int)Math.floor(0.1*Math.sqrt(realmXp.get(realm.getDBName())));
	}
	/**
	* Get the player level
	* @param mode - The PvP mode
	* @param type - The PvP type
	* @return the players level in the specified pvp mode
	*/
	public int getLevel(PvPMode mode, PvPType type) {
		return (int)Math.floor(0.1*Math.sqrt(realmXp.get(mode.getDBName() + "_" + type.getDBName())));
	}
	/**
	* Get the player level
	* @param realm - The realm go get the players level from
	* @return the players level in the provided realm
	*/
	public int getXPFromLevel(int level) {
		return (int)Math.pow(10*level,2);
	}
	/**
	* Get the player xp
	* @param realm - The realm go get the players xp from
	* @return the players xp in the provided realm
	*/
	public int getXp(Realm realm) {
		return realmXp.get(realm.getDBName());
	}
	/**
	* Get the player xp
	* @param realm - The realm go get the players xp from
	* @return the players xp in the provided realm
	*/
	public int getXp(PvPMode mode, PvPType type) {
		return realmXp.get(mode.getDBName() + "_" + type.getDBName());
	}
	/**
	* Get a custom item by name
	* @param name - Name of the custom item
	* @return instance of the custom item
	*/
	public ItemBase getCustomItem(String name) {
		return customItems.get(name);
	}
	/**
    * Get if the player is spectating
    */
	public String getTeam() {
		return board.getPlayerTeam(player).getName();
	}
	/**
    * Get if the player is spectating
    */
	public boolean getSpectatingStatus() {
		return isSpectating;
	}
	/**
    * Get the players current game
    */
	public RealmBase getCurrentRealm() {
		return currentGame;
	}
	/**
	* Get the players open menu
	* @return instance of the open menu
	*/
	public MenuBase getMenu() {
		return openedMenu;
	}
	/**
	* Get the players realm xp hashmap
	* @return hashmap of realms and their associated xp 
	*/
	public HashMap<String,Integer> getRealmXp() {
		return realmXp;
	}
	/**
    * Get the value of a custom data key
    * @param name - name of the data you want to get.
    * @return value of the specified data key
    */
	public String getCustomDataKey(String name) {
		String value = null;
		
		for (Map.Entry<String, String> entry : customData.entrySet()) {
			if (entry.getKey().equalsIgnoreCase(name)) {
				value = entry.getValue();
			}
		}
		return value;
	}
	/**
    * Get the users database id
    * @return the players database id
    */
	public int getId() {
		return id;
	}
	/**
    * Get the unix timestamp when the player joined the server
    * @return the beginning of the players session on the server
    */
	public long getTimeJoined() {
		return timeJoined;
	}
	/**
    * Get the players current realm
    * @return the players realm
    */
	public Realm getRealm() {
		return realm;
	}
	/**
    * Get the players user group
    * @return the players user group
    */
	public UserGroup getUserGroup() {
		return userGroup;
	}
	/**
    * Get the players queuing status
    * @return the players queuing status
    */
	public boolean isQueuingGame() {
		return queuingGame;
	}
	
	//
	// SETTERS
	//
	
	/**
	* Associate a custom item with the player
	* @param name - Name of the custom item
	* @param item - Reference of the item
	*/
	public void addCustomItem(String name, ItemBase item) {
		customItems.put(name, item);
	}
	/**
	* Update the player and set the scoreboard to the new player
	*/
	public void setPlayer(Player ply) {
		player = ply;
		permissions = player.addAttachment(mainInstance);
		player.setScoreboard(board);
	}
	/**
    * Set if the player is spectating
    */
	public void setSpectatingStatus(boolean status) {
		isSpectating = status;
	}
	/**
    * Set the players current game
    */
	public void setCurrentRealm(RealmBase game) {
		currentGame = game;
	}
	/**
    * Set the players open menu
    */
	public void setMenu(MenuBase menu) {
		openedMenu = menu;
	}
	/**
    * Set the players data
    * @param mainInstance - plugin instance
    * @param ply - the player
    * @param id - database id
    * @param points - amount of points
    * @param xp - amount of xp
    * @param timePlayed - amount of time played on the server
    * @param ip - players ip
    * @param userGroup - players user group
    */
	public void setData(int id, int points, int xp, double timePlayed, String ip, UserGroup userGroup) {
		this.id = id;
		permissions = player.addAttachment(mainInstance);
		this.points = points;
		this.timePlayed = timePlayed;
		this.ip = ip;
		this.userGroup = userGroup;
		
		Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
            @Override
            public void run() {                   	
            	//Get the players realm xp
            	ArrayList<HashMap<String, String>> realmXPQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM userXP WHERE uniqueId = '" + player.getUniqueId() + "'", 7, false);
            	
            	//If the player exists in the userXP table
            	if (realmXPQuery.size() > 0) {
            		HashMap<String, String> realmXPQueryData = realmXPQuery.get(0);
            		
            		//Set the players realm xp
            		realmXp.put("kitpvp", Integer.parseInt(realmXPQueryData.get("kitpvp")));
            		realmXp.put("skywars", Integer.parseInt(realmXPQueryData.get("skywars")));
            		realmXp.put("stepspleef", Integer.parseInt(realmXPQueryData.get("stepspleef")));
            		realmXp.put("duels_fists", Integer.parseInt(realmXPQueryData.get("duels_fists")));
            		realmXp.put("2v2_fists", Integer.parseInt(realmXPQueryData.get("2v2_fists")));
            	} else {
            		//Insert the user in the userXP table
            		mainInstance.getDatabaseInstance().query("INSERT INTO userXP (uniqueId) VALUES ('" + player.getUniqueId() + "')", 0, true);
            		
            		realmXPQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM userXP WHERE uniqueId = '" + player.getUniqueId() + "'", 7, false);
            		HashMap<String, String> realmXPQueryData = realmXPQuery.get(0);
            		
            		//Set the players realm xp
            		realmXp.put("kitpvp", Integer.parseInt(realmXPQueryData.get("kitpvp")));
            		realmXp.put("skywars", Integer.parseInt(realmXPQueryData.get("skywars")));
            		realmXp.put("stepspleef", Integer.parseInt(realmXPQueryData.get("stepspleef")));
            		realmXp.put("duels_fists", Integer.parseInt(realmXPQueryData.get("duels_fists")));
            		realmXp.put("2v2_fists", Integer.parseInt(realmXPQueryData.get("2v2_fists")));
            	}
            	
            	//Get the players total games played
            	ArrayList<HashMap<String, String>> totalGamesQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM userTotalGames WHERE uniqueId = '" + player.getUniqueId() + "'", 7, false);
            	
            	//If the player exists in the table
            	if (totalGamesQuery.size() > 0) {
            		HashMap<String, String> totalGamesQueryData = totalGamesQuery.get(0);
            		
            		//Set the players total games played
            		totalGamesPlayed.put("kitpvp", Integer.parseInt(totalGamesQueryData.get("kitpvp")));
            		totalGamesPlayed.put("skywars", Integer.parseInt(totalGamesQueryData.get("skywars")));
            		totalGamesPlayed.put("stepspleef", Integer.parseInt(totalGamesQueryData.get("stepspleef")));
            		totalGamesPlayed.put("duels_fists", Integer.parseInt(totalGamesQueryData.get("duels_fists")));
            		totalGamesPlayed.put("2v2_fists", Integer.parseInt(totalGamesQueryData.get("2v2_fists")));
            	} else {
            		//Insert the user in the table
            		mainInstance.getDatabaseInstance().query("INSERT INTO userTotalGames (uniqueId) VALUES ('" + player.getUniqueId() + "')", 0, true);
            		
            		totalGamesQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM userTotalGames WHERE uniqueId = '" + player.getUniqueId() + "'", 7, false);
            		HashMap<String, String> totalGamesQueryData = totalGamesQuery.get(0);
            		
            		//Set the players total games played
            		totalGamesPlayed.put("kitpvp", Integer.parseInt(totalGamesQueryData.get("kitpvp")));
            		totalGamesPlayed.put("skywars", Integer.parseInt(totalGamesQueryData.get("skywars")));
            		totalGamesPlayed.put("stepspleef", Integer.parseInt(totalGamesQueryData.get("stepspleef")));
            		totalGamesPlayed.put("duels_fists", Integer.parseInt(totalGamesQueryData.get("duels_fists")));
            		totalGamesPlayed.put("2v2_fists", Integer.parseInt(totalGamesQueryData.get("2v2_fists")));
            	}
            	
            	//Get the players game wins
            	ArrayList<HashMap<String, String>> gamesWonQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM userGamesWon WHERE uniqueId = '" + player.getUniqueId() + "'", 7, false);
            	
            	//If the player exists in the table
            	if (gamesWonQuery.size() > 0) {
            		HashMap<String, String> gamesWonQueryData = gamesWonQuery.get(0);
            		
            		//Set the players games won
            		totalGamesWon.put("kitpvp", Integer.parseInt(gamesWonQueryData.get("kitpvp")));
            		totalGamesWon.put("skywars", Integer.parseInt(gamesWonQueryData.get("skywars")));
            		totalGamesWon.put("stepspleef", Integer.parseInt(gamesWonQueryData.get("stepspleef")));
            		totalGamesWon.put("duels_fists", Integer.parseInt(gamesWonQueryData.get("duels_fists")));
            		totalGamesWon.put("2v2_fists", Integer.parseInt(gamesWonQueryData.get("2v2_fists")));
            	} else {
            		//Insert the user in the table
            		mainInstance.getDatabaseInstance().query("INSERT INTO userGamesWon (uniqueId) VALUES ('" + player.getUniqueId() + "')", 0, true);
            		
            		gamesWonQuery = mainInstance.getDatabaseInstance().query("SELECT * FROM userGamesWon WHERE uniqueId = '" + player.getUniqueId() + "'", 7, false);
            		HashMap<String, String> gamesWonQueryData = gamesWonQuery.get(0);
            		
            		//Set the players games won
            		totalGamesWon.put("kitpvp", Integer.parseInt(gamesWonQueryData.get("kitpvp")));
            		totalGamesWon.put("skywars", Integer.parseInt(gamesWonQueryData.get("skywars")));
            		totalGamesWon.put("stepspleef", Integer.parseInt(gamesWonQueryData.get("stepspleef")));
            		totalGamesWon.put("duels_fists", Integer.parseInt(gamesWonQueryData.get("duels_fists")));
            		totalGamesWon.put("2v2_fists", Integer.parseInt(gamesWonQueryData.get("2v2_fists")));
            	}
            }
		});
		setPermissions();
	}
	/**
    * Set the players permissions based on their current realm
    */
	public void setPermissions() {
		if (realm == Realm.HUB) {
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
		} else if (realm == Realm.KITPVP) {
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canUseInventory", false);
			permissions.setPermission("nyeblock.shouldDropItemsOnDeath", false);
			permissions.setPermission("nyeblock.canDamage", true);
			permissions.setPermission("nyeblock.canBeDamaged", false);
			permissions.setPermission("nyeblock.canTakeFallDamage", true);
			permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
			permissions.setPermission("nyeblock.canDropItems", false);
			permissions.setPermission("nyeblock.canLoseHunger", false);
			permissions.setPermission("nyeblock.canSwapItems", false);
			permissions.setPermission("nyeblock.canMove", true);
		} else if (realm == Realm.STEPSPLEEF) {
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canUseInventory", false);
			permissions.setPermission("nyeblock.shouldDropItemsOnDeath", false);
			permissions.setPermission("nyeblock.canDamage", true);
			permissions.setPermission("nyeblock.canBeDamaged", true);
			permissions.setPermission("nyeblock.canTakeFallDamage", false);
			permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
			permissions.setPermission("nyeblock.canDropItems", false);
			permissions.setPermission("nyeblock.canLoseHunger", false);
			permissions.setPermission("nyeblock.canSwapItems", false);
			permissions.setPermission("nyeblock.canMove", true);
		} else if (realm == Realm.SKYWARS) {
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canUseInventory", false);
			permissions.setPermission("nyeblock.shouldDropItemsOnDeath", true);
			permissions.setPermission("nyeblock.canDamage", true);
			permissions.setPermission("nyeblock.canBeDamaged", true);
			permissions.setPermission("nyeblock.canTakeFallDamage", true);
			permissions.setPermission("nyeblock.tempNoDamageOnFall", true);
			permissions.setPermission("nyeblock.canDropItems", false);
			permissions.setPermission("nyeblock.canLoseHunger", false);
			permissions.setPermission("nyeblock.canSwapItems", false);
			permissions.setPermission("nyeblock.canMove", true);
		} else if (realm == Realm.PVP) {
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canUseInventory", false);
			permissions.setPermission("nyeblock.shouldDropItemsOnDeath", false);
			permissions.setPermission("nyeblock.canDamage", false);
			permissions.setPermission("nyeblock.canBeDamaged", true);
			permissions.setPermission("nyeblock.canTakeFallDamage", true);
			permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
			permissions.setPermission("nyeblock.canDropItems", false);
			permissions.setPermission("nyeblock.canLoseHunger", false);
			permissions.setPermission("nyeblock.canSwapItems", false);
			permissions.setPermission("nyeblock.canMove", true);
		}
	}
	/**
    * Set a specific permission for the player
    * @param permission - permission to set
    * @param value - value to set to given permission
    */
	public void setPermission(String permission, boolean value) {
		permissions.setPermission(permission, value);
	}
	/**
    * Give the player default items based on their realm
    */
	public void setItems() {
		player.getInventory().clear();
		customItems.clear();
		
		if (realm == Realm.HUB) {
			//Game Menu
			HubMenu hubMenu = new HubMenu(mainInstance,player);
			ItemStack hm = hubMenu.give();
			player.getInventory().setItem(4, hm);
			player.getInventory().setHeldItemSlot(4);
			
			//Hide players
			HidePlayers hidePlayers = new HidePlayers(mainInstance,player);
			ItemStack hp = hidePlayers.give();
			player.getInventory().setItem(6, hp);
			
			//Shop menu
			ShopMenu shopMenu = new ShopMenu(mainInstance,player);
			ItemStack sm = shopMenu.give();
			player.getInventory().setItem(2, sm);
			
			//Profile/Stats menu
			ProfileStatsMenu profileStatsMenu = new ProfileStatsMenu(mainInstance,player);
			ItemStack ssm = profileStatsMenu.give();
			player.getInventory().setItem(0, ssm);
			
		} else if (realm == Realm.PARKOUR) {
			//Parkour menu
			ParkourMenu parkourMenu = new ParkourMenu(mainInstance,player);
			ItemStack pm = parkourMenu.give();
			player.getInventory().setItem(4, pm);
			
			//Hide players
			HidePlayers hidePlayers = new HidePlayers(mainInstance,player);
			ItemStack hp = hidePlayers.give();
			player.getInventory().setItem(6, hp);
			
			//Go to start
			ReturnToStart startItem = new ReturnToStart(mainInstance,player);
			ItemStack si = startItem.give();
			player.getInventory().setItem(2, si);
		} if (realm == Realm.KITPVP) {
			//Return to hub
			ReturnToHub returnToHub = new ReturnToHub(mainInstance,player);
			player.getInventory().setItem(8, returnToHub.give());
			//Select kit
			KitSelector selectKit = new KitSelector(mainInstance,player);
			player.getInventory().setItem(7, selectKit.give());
			//Sword
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			player.getInventory().setItem(0, sword);
			player.getInventory().setHeldItemSlot(0);
			//Golden Apples
			ItemStack goldenApples = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,1);
			player.getInventory().setItem(1, goldenApples);
			//Armor
			ItemStack[] armor = {
				new ItemStack(Material.IRON_BOOTS),
				new ItemStack(Material.IRON_LEGGINGS),
				new ItemStack(Material.IRON_CHESTPLATE),
				new ItemStack(Material.IRON_HELMET)
			};
			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			player.getInventory().setArmorContents(armor);
		} else if (realm == Realm.STEPSPLEEF) {
			if (currentGame != null) {
				if (currentGame.isInServer(player) && ((GameBase)currentGame).isGameActive()) {
					//Select player
					PlayerSelector selectPlayer = new PlayerSelector(mainInstance,Realm.STEPSPLEEF,player);
					player.getInventory().setItem(4, selectPlayer.give());
				}
			}
			
			//Return to hub
			ReturnToHub returnToHub = new ReturnToHub(mainInstance,player);
			player.getInventory().setItem(8, returnToHub.give());
		} else if (realm == Realm.SKYWARS) {
			if (currentGame != null) {
				if (currentGame.isInServer(player)) {
					if (((GameBase)currentGame).isGameActive()) {						
						//Select player
						PlayerSelector selectPlayer = new PlayerSelector(mainInstance,Realm.SKYWARS,player);
						player.getInventory().setItem(4, selectPlayer.give());
					} else {						
						//Select kit
						KitSelector selectKit = new KitSelector(mainInstance,player);
						player.getInventory().setItem(4, selectKit.give());
					}
				}
			}
			//Return to hub
			ReturnToHub returnToHub = new ReturnToHub(mainInstance,player);
			player.getInventory().setItem(8, returnToHub.give());
		} else if (realm == Realm.PVP) {
			if (currentGame != null && ((GameBase)currentGame).isGameActive()) {						
				//Select player
				PlayerSelector selectPlayer = new PlayerSelector(mainInstance,Realm.PVP,player);
				player.getInventory().setItem(4, selectPlayer.give());
			}
			//Return to hub
			ReturnToHub returnToHub = new ReturnToHub(mainInstance,player);
			player.getInventory().setItem(8, returnToHub.give());
		}
	}
	/**
    * Set the players realm
    * @param realm - realm to set.
    * @param updatePermissions - should their permissions be updated?
    * @param updateItems - should their items be updated?
    */
	public void setRealm(Realm realm, boolean updatePermissions, boolean updateItems, boolean resetPlayer) {
		this.realm = realm;
		
		if (updatePermissions) {			
			setPermissions();
		}
		if (updateItems) {			
			setItems();
		}
		if (resetPlayer) {
			//Remove potion effects
			for(PotionEffect effect : player.getActivePotionEffects())
			{
				player.removePotionEffect(effect.getType());
			}
			//Reset flying
			player.setAllowFlight(false);
			//Reset health/food
			player.setHealth(20);
			player.setFoodLevel(20);
			//Remove fire
			player.setFireTicks(0);
			//Reset title
			player.sendTitle("", "", 0, 0, 0);
			//Reset spectating status
			setSpectatingStatus(false);
			player.setLevel(0);
			openedMenu = null;
		}
	}
	/**
    * Set the players queuing status. If true then the player can queue, if false then they cannot
    * @param status - status to set.
    */
	public void setQueuingStatus(boolean status) {
		queuingGame = status;
	}
	/**
    * Set a custom data set with the given value
    * @param key - name of the custom data.
    * @param value - value to set to the data.
    */
	public void setCustomDataKey(String key, String value) {
		customData.put(key, value);
	}
	/**
    * Set the title of the players scoreboard
    * @param title - title to set the scoreboards title to.
    */
	public void setScoreboardTitle(String title) {
		objective.setDisplayName(title);
	}
	/**
    * Update the players scoreboard text if any part of it has changed
    * @param scores - scores on the scoreboard to compare/set.
    */
	public void updateObjectiveScores(HashMap<Integer,String> scores) {
		for (Map.Entry<Integer, String> entry : scores.entrySet()) {
			Toolkit.updateScore(objective, entry.getKey(), entry.getValue());
		}
	}
	/**
    * Set the players scoreboard teams and adds the given teams
    * @param teams - list of teams to create.
    */
	public void setScoreBoardTeams(String[] teams,Team.OptionStatus status) {
		if (teams != null) {			
			for (int i = 0; i < teams.length; i++) {			
				Team team = board.registerNewTeam(teams[i]);
				team.setOption(Team.Option.COLLISION_RULE, status);
			}
		}
		for (UserGroup userGroup : UserGroup.values()) {
			if (board.getTeam(userGroup.toString()) == null) {
				Team team = board.registerNewTeam(userGroup.toString());
				team.setOption(Team.Option.COLLISION_RULE, status);
				team.setColor(userGroup.getColor());
			}
		}
	}
	/**
    * Set the teams color on the scoreboard
    * @param team - Name of the team to set
    * @param color - Color to set
    */
	public void setTeamColor(String name, ChatColor color) {
		for (Team team : board.getTeams()) {
			if (team.getName().equalsIgnoreCase(name)) {
				team.setColor(color);
			}
		}
	}
}
