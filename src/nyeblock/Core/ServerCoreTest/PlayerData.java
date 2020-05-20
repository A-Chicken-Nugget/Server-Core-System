package nyeblock.Core.ServerCoreTest;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.DataWatcherObject;
import net.minecraft.server.v1_15_R1.DataWatcherRegistry;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;

import nyeblock.Core.ServerCoreTest.Achievements.AchievementBase;
import nyeblock.Core.ServerCoreTest.Items.HidePlayers;
import nyeblock.Core.ServerCoreTest.Items.ItemBase;
import nyeblock.Core.ServerCoreTest.Items.PlayerSelector;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Items.ReturnToLobby;
import nyeblock.Core.ServerCoreTest.Items.ReturnToStart;
import nyeblock.Core.ServerCoreTest.Menus.GameMenu;
import nyeblock.Core.ServerCoreTest.Menus.KitSelectorMenu;
import nyeblock.Core.ServerCoreTest.Menus.MenuBase;
import nyeblock.Core.ServerCoreTest.Menus.NyeBlockMenu;
import nyeblock.Core.ServerCoreTest.Menus.ParkourMenu;
import nyeblock.Core.ServerCoreTest.Menus.MyProfileMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopItem;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;
import nyeblock.Core.ServerCoreTest.Misc.Party;
import nyeblock.Core.ServerCoreTest.Misc.Enums.DBDataType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.LogType;
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
	private int id = -1;
	private int points = -1;
	private double timePlayed;
	private long timeJoined = System.currentTimeMillis() / 1000L;
	private String ip;
	private ArrayList<UserGroup> userGroups = new ArrayList<>();
	private PermissionAttachment permissions;
	private Realm realm = Realm.HUB;
	private ChatColor chatTextColor = null;
	private ChatColor nameTextColor = null;
	private HashMap<String,String> customData = new HashMap<>();
	private HashMap<String,ItemBase> customItems = new HashMap<>();
	private ArrayList<ShopItem> shopItems = new ArrayList<>();
	private ArrayList<String> achievements = new ArrayList<>();
	private Party party;
	private Party partyInvite;
	private MenuBase openedMenu;
	private RealmBase currentRealm;
	private boolean isSpectating = false;
	private boolean isHidden = false;
	private boolean queuingGame = false;
	private boolean loadedDBInfo = false;
	private boolean logSearch = false;
	//Stats
	private HashMap<String,Integer> realmXp = new HashMap<>();
	private HashMap<String,Integer> totalGamesPlayed = new HashMap<>();
	private HashMap<String,Integer> totalGamesWon = new HashMap<>();
	private HashMap<String,Integer> totalGameKills = new HashMap<>();
	private HashMap<String,Integer> totalGameDeaths = new HashMap<>();
	//Scoreboard
	private Scoreboard board;
	private Objective objective;
	
	public PlayerData(Main mainInstance, Player ply) {
		this.mainInstance = mainInstance;
		currentRealm = mainInstance.getHubInstance();
		databaseHandlingInstance = mainInstance.getDatabaseInstance();
		player = ply;
		permissions = player.addAttachment(mainInstance);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(mainInstance, new Runnable() {
			@Override
			public void run() {				
				// Teleport to spawn
				player.teleport(new Location(Bukkit.getWorld("world"),-9.548, 113, -11.497));
				createScoreboard();
				mainInstance.getHubInstance().join(ply, false);
				
//				ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
//				BookMeta bookMeta = (BookMeta)book.getItemMeta();
//				bookMeta.setTitle("Welcome");
//				bookMeta.setAuthor("Nyeblock");
//				bookMeta.setPages(Arrays.asList("test1","test2"));
//				book.setItemMeta(bookMeta);
//				player.openBook(book);
			}
		});
		currentRealm.updateTeamsFromUserGroups();
	}
	
	//
	// CLASS METHODS
	//
	
	//Temp
	public void setLogSearch(boolean logSearch) {
		this.logSearch = logSearch;
	}
	public boolean getLogSearch() {
		return logSearch;
	}
	
	/**
	* Give the player a death in the specified realm
	* @param realm - realm to add the death to
	*/
	public void addDeath(Realm realm) {
		if (realm.isGame()) {
			Integer totalDeaths = totalGameDeaths.get(realm.getDBName());
			
			if (totalDeaths != null) {				
				totalGameDeaths.put(realm.getDBName(), totalDeaths + 1);
			}
		}
	}
	/**
	* Give the player a kill in the specified realm
	* @param realm - realm to add the kill to
	*/
	public void addKill(Realm realm) {
		if (realm.isGame()) {
			Integer totalKills = totalGameKills.get(realm.getDBName());
			
			if (totalKills != null) {				
				totalGameKills.put(realm.getDBName(), totalKills + 1);
			}
		}
	}
	/**
	* Give an achievement to the player
	* @param uniqueId - the unique id of the achievement
	*/
	public void addAchievement(String uniqueId) {
		if (!achievements.contains(uniqueId)) {			
			achievements.add(uniqueId);
		} else {
			mainInstance.logMessage(LogType.WARNING, "Attempted to give the player an achievement they already had!");
		}
	}
	/**
	* Give a shop item to the player. If it exists add to its quantity
	* @param uniqueId - the unique id of the shop item
	* @param menuName - the name of the menu the shop item is purchased in
	*/
	public void addShopItem(String uniqueId,String menuName) {
		boolean found = false;
		
		for (ShopItem item : shopItems) {
			if (item.getUniqueId().equalsIgnoreCase(uniqueId)) {
				item.updateQuantity(true);
				found = true;
			}
		}
		if (!found) {
			shopItems.add(new ShopItem(uniqueId,1,false,menuName));
		}
	}
	/**
	* Removed a shop item from the player
	* @param item - the shop item to remove
	*/
	public void removeShopItem(ShopItem item) {
		if (item.updateQuantity(false)) {
			shopItems.remove(item);
		}
	}
	/**
	* Give points to player
	* @param amount - the amount of points to give
	*/
	public void addPoints(int amount) {
		points += amount;
	}
	/**
	* Take points away
	* @param amount - the amount of points to take away
	*/
	public void removePoints(int amount) {
		points -= amount;
	}
	/**
	* Associate a custom item with the player
	* @param name - Name of the custom item
	* @param item - Reference of the item
	*/
	public void addCustomItem(String name, ItemBase item) {
		customItems.put(name, item);
	}
	/**
    * Add game played
    * @param realm - realm to add to the games played
    * @param won - did they win
    */
	public void addGamePlayed(Realm realm, boolean won) {
		if (!won) {
			Integer totalPlayed = totalGamesPlayed.get(realm.getDBName());
			
			if (totalPlayed != null) {				
				totalGamesPlayed.put(realm.getDBName(), totalPlayed + 1);
			}
		} else {
			Integer totalWon = totalGamesPlayed.get(realm.getDBName());
			
			if (totalWon != null) {				
				totalGamesWon.put(realm.getDBName(), totalWon + 1);			
			}
		}
	}
	/**
    * Give the player xp
    * @param realm - realm
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
			player.sendMessage(ChatColor.YELLOW + "You have leveled up! You are now level " + ChatColor.GREEN + newLevel + ChatColor.YELLOW + "!");
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
    * Deletes the players scoreboard
    */
	public void clearScoreboard() {
		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
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
			mainInstance.getTimerInstance().createRunnableTimer("teamRecheck_" + ply.getUniqueId(), .5, 2, new Runnable() {
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
    * Get the players queuing status
    * @return the players queuing status
    */
	public boolean isQueuingGame() {
		return queuingGame;
	}
	/**
    * Clear the players custom items
    */
	public void clearCustomItems() {
		customItems.clear();
	}
	/**
    * Save the players data to the DB
    */
	public void saveData(DBDataType type) {
		if (loadedDBInfo) {			
			Gson gson = new Gson();
			UUID uuid = player.getUniqueId();
			
			if (type.equals(DBDataType.ALL) || type.equals(DBDataType.ACHIEVEMENTS)) {
				if (achievements != null) {
					databaseHandlingInstance.query("UPDATE user_achievements SET achievements = '" + gson.toJson(achievements) + "' WHERE uniqueId = '" + uuid + "'", true);					
				}
			}
			if (type.equals(DBDataType.ALL) || type.equals(DBDataType.SHOP_ITEMS)) {
				if (shopItems != null) {					
					databaseHandlingInstance.query("UPDATE user_shop_items SET items = '" + gson.toJson(shopItems) + "' WHERE uniqueId = '" + uuid + "'", true);
				}
			}
			if (type.equals(DBDataType.ALL) || type.equals(DBDataType.STATS)) {
				if (realmXp != null && totalGamesPlayed != null && totalGamesWon != null && totalGameKills != null && totalGameDeaths != null) {					
					databaseHandlingInstance.query("UPDATE user_stats SET realm_xp = '" + gson.toJson(realmXp) + "',"
							+ "games_played = '" + gson.toJson(totalGamesPlayed) + "',"
							+ "games_won = '" + gson.toJson(totalGamesWon) + "',"
							+ "game_kills = '" + gson.toJson(totalGameKills) + "',"
							+ "game_deaths = '" + gson.toJson(totalGameDeaths) + "' WHERE uniqueId = '" + uuid + "'", true);
				}
			}
			if (type.equals(DBDataType.ALL) || type.equals(DBDataType.USER)) {
				databaseHandlingInstance.query("UPDATE users SET name = '" + StringEscapeUtils.escapeSql(player.getName()) + "',"
						+ "points = " + points + ","
						+ "time_played = time_played + " + ((System.currentTimeMillis()/1000L)-getTimeJoined())
						+ " WHERE uniqueId = '" + uuid + "'", true);
			}
		}
	}
	/**
    * Setup the players data. Will pull/create the players data in the DB
    */
	public void setupData() {
		try {
			UUID uuid = player.getUniqueId();
			ArrayList<HashMap<String,String>> userQuery = mainInstance.getDatabaseInstance().query(
					"SELECT u.*, a.achievements, g.groups, i.items, s.realm_xp, s.games_played, s.games_won, s.game_kills, s.game_deaths "
							+ "FROM users u, user_achievements a, user_groups g, user_shop_items i, user_stats s "
							+ "WHERE u.uniqueId = '" + uuid + "' and a.uniqueId = '" + uuid + "' and g.uniqueId = '" + uuid + "' and i.uniqueId = '" + uuid + "' and s.uniqueId = '" + uuid + "'"
							, false);
			ip = player.getAddress().toString().split(":")[0].replace("/","");
			
			if (userQuery.size() > 0) {
				HashMap<String, String> userQueryData = userQuery.get(0);
				ArrayList<UserGroup> groups = new ArrayList<>();
				Gson gson = new Gson();
				ArrayList<Integer> groupIds = gson.fromJson(userQueryData.get("groups"), new TypeToken<ArrayList<Integer>>(){}.getType());
				
				for (int groupId : groupIds) {
					groups.add(UserGroup.fromInt(groupId));
				}
				
				id = Integer.parseInt(userQueryData.get("id"));
				points = Integer.parseInt(userQueryData.get("points"));
				timePlayed = Integer.parseInt(userQueryData.get("time_played"));
				userGroups = groups;
				achievements = gson.fromJson(userQueryData.get("achievements"), new TypeToken<ArrayList<String>>(){}.getType());
				if (achievements == null) {
					achievements = new ArrayList<String>();
				}
				realmXp = gson.fromJson(userQueryData.get("realm_xp"), new TypeToken<HashMap<String,Integer>>(){}.getType());
				if (realmXp == null) {
					realmXp = new HashMap<String,Integer>();
				}
				totalGamesPlayed = gson.fromJson(userQueryData.get("games_played"), new TypeToken<HashMap<String,Integer>>(){}.getType());
				if (totalGamesPlayed == null) {
					totalGamesPlayed = new HashMap<String,Integer>();
				}
				totalGamesWon = gson.fromJson(userQueryData.get("games_won"), new TypeToken<HashMap<String,Integer>>(){}.getType());
				if (totalGamesWon == null) {
					totalGamesWon = new HashMap<String,Integer>();
				}
				totalGameKills = gson.fromJson(userQueryData.get("game_kills"), new TypeToken<HashMap<String,Integer>>(){}.getType());
				if (totalGameKills == null) {
					totalGameKills = new HashMap<String,Integer>();
				}
				totalGameDeaths = gson.fromJson(userQueryData.get("game_deaths"), new TypeToken<HashMap<String,Integer>>(){}.getType());
				if (totalGameDeaths == null) {
					totalGameDeaths = new HashMap<String,Integer>();
				}
				shopItems = gson.fromJson(userQueryData.get("items"), new TypeToken<ArrayList<ShopItem>>(){}.getType());
				if (shopItems == null) {
					shopItems = new ArrayList<ShopItem>();
				}
				
				//Set the players chat or name text colors
				for (ShopItem item : shopItems) {
					if (item.isEquipped()) {
						String uniqueId = item.getUniqueId();
						
						if (uniqueId.contains("text_color")) {
							setChatTextColor(Toolkit.getColorFromString(uniqueId.split("-")[0].toUpperCase()));
						} else if (uniqueId.contains("name_color")) {
							setNameTextColor(Toolkit.getColorFromString(uniqueId.split("-")[0].toUpperCase()));
						}
					}
				}
				//Log ip change
				if (!userQueryData.get("ip").equals(ip)) {
					mainInstance.getDatabaseInstance().query("UPDATE users SET ip = '" + ip + "' WHERE uniqueId = '" + player.getUniqueId() + "'", true);
					mainInstance.getDatabaseInstance().query("INSERT INTO user_ips (uniqueId,ip) VALUES ('" + uuid + "','" + ip + "')", true);
				}
			} else {
				databaseHandlingInstance.query("INSERT INTO users (uniqueId,name,ip) VALUES ('" + uuid + "','" + StringEscapeUtils.escapeSql(player.getName()) + "','" + ip + "');",true);
				databaseHandlingInstance.query("INSERT INTO user_achievements (uniqueId) VALUES ('" + uuid + "');",true);
				databaseHandlingInstance.query("INSERT INTO user_groups (uniqueId,groups) VALUES ('" + uuid + "','[1]');",true);
				databaseHandlingInstance.query("INSERT INTO user_ips (uniqueId,ip) VALUES ('" + uuid + "','" + ip + "');",true);
				databaseHandlingInstance.query("INSERT INTO user_shop_items (uniqueId) VALUES ('" + uuid + "');",true);
				databaseHandlingInstance.query("INSERT INTO user_stats (uniqueId) VALUES ('" + uuid + "');",true);
				
				points = 0;
				timePlayed = 0;
				userGroups.add(UserGroup.USER);
				for (Realm realm : Realm.values()) {
					//Xp
					realmXp.put(realm.getDBName(),0);
					
					if (realm.isGame()) {
						//Games won
						totalGamesWon.put(realm.getDBName(), 0);
						//Games played
						totalGamesPlayed.put(realm.getDBName(), 0);
						//Game kills
						totalGameKills.put(realm.getDBName(), 0);
						//Game deaths
						totalGameDeaths.put(realm.getDBName(), 0);
					}
				}
			}
			currentRealm.updateTeamsFromUserGroups();
			loadedDBInfo = true;
		} catch (Exception ex) {
			System.out.println("Test: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	//
	// GETTERS
	//
	
	public UserGroup getPrimaryUserGroup() {
		UserGroup returnGroup = null;
		
		for (UserGroup group : userGroups) {
			if (group.getWeight() == 1) {
				returnGroup = group;
			}
		}
		return returnGroup;
	}
	public Party getPartyInvite() {
		return partyInvite;
	}
	public Party getParty() {
		return party;
	}
	/**
    * Get the players achievements
    * @return list of achievement unique ids that the player has
    */
	public ArrayList<String> getAchievements() {
		return achievements;
	}
	/**
    * Get the players ip
    * @return the players ip
    */
	public String getIp() {
		return ip;
	}
	/**
    * Get the players name text color
    * @return the color of the players name text
    */
	public ChatColor getNameTextColor() {
		return nameTextColor;
	}
	/**
    * Get the players permission attachment
    * @return the players permission attachment
    */
	public PermissionAttachment getPermissionAttachment() {
		return permissions;
	}
	/**
    * Get the players instance
    * @return the players instance
    */
	public Player getPlayer() {
		return player;
	}
	/**
    * Get the players chat text color
    * @return the color of the players chat text
    */
	public ChatColor getChatTextColor() {
		return chatTextColor;
	}
	/**
    * Get the players loaded info from db status
    * @return whether or not the players data has loaded from the db
    */
 	public boolean getLoadedDBInfoStatus() {
		return loadedDBInfo;
	}
	/**
	* Get a specific shop item
	* @param uniqueId - uniqueId of the item
	* @return the requested item or null if not found
	*/
	public ShopItem getShopItem(String uniqueId) {
		ShopItem returnItem = null;
		
		for (ShopItem item : shopItems) {
			if (item.getUniqueId().equalsIgnoreCase(uniqueId)) {
				returnItem = item;
			}
		}
		return returnItem;
	}
	/**
	* Get the players shop items
	*/
	public ArrayList<ShopItem> getShopItems() {
		return shopItems;
	}
	/**
	* Get the players points
	*/
	public int getPoints() {
		return points;
	}
	/**
	* Get the players hidden status
	* @return if the player is hidden
	*/
	public boolean getHiddenStatus() {
		return isHidden;
	}
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
	* Get total game deaths
	* @return the players total amount of deaths for each game
	*/
	public HashMap<String,Integer> getTotalGameDeaths() {
		return totalGameDeaths;
	}
	/**
	* Get total game kills
	* @return the players total amount of kills for each game
	*/
	public HashMap<String,Integer> getTotalGameKills() {
		return totalGameKills;
	}
	/**
	* Get total games won
	* @param realm - the realm
	* @return the players total amount of games won for specified realm
	*/
	public Integer getTotalGamesWon(Realm realm) {
		return totalGamesWon.get(realm.getDBName());
	}
	/**
	* Get total games played
	* @param realm - the realm
	* @return the players total amount of games played for specified realm
	*/
	public Integer getTotalGamesPlayed(Realm realm) {
		return totalGamesPlayed.get(realm.getDBName());
	}
	/**
	* Get game kills
	* @param realm - the realm
	* @return the players kills in the specified realm
	*/
	public Integer getGameKills(Realm realm) {
		return totalGameKills.get(realm.getDBName());
	}
	/**
	* Get game deaths
	* @param realm - the realm
	* @return the players deaths in the specified realm
	*/
	public Integer getGameDeaths(Realm realm) {
		return totalGameDeaths.get(realm.getDBName());
	}
	/**
	* Get the player level
	* @param realm - The realm go get the players level from
	* @return the players level in the provided realm
	*/
	public int getLevel(Realm realm) {
		Integer currentXp = realmXp.get(realm.getDBName());
		
		if (currentXp != null) {			
			return (int)Math.floor(0.1*Math.sqrt(realmXp.get(realm.getDBName())));
		} else {
			return 0;
		}
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
		return currentRealm;
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
    * Get the players user groups
    * @return list of the players user groups
    */
	public ArrayList<UserGroup> getUserGroups() {
		return userGroups;
	}
	
	//
	// SETTERS
	//
	
	public void setPartyInvite(Party partyInvite) {
		this.partyInvite = partyInvite;
	}
	public void setParty(Party party) {
		this.party = party;
	}
	/**
    * Set the players name text color
    * @param color - the color to set
    */
	public void setNameTextColor(ChatColor color) {
		nameTextColor = color;
	}
	/**
    * Set the players chat text color
    * @param color - the color to set
    */
	public void setChatTextColor(ChatColor color) {
		chatTextColor = color;
	}
	/**
	* Set the players hidden status
	* @param shouldHide - should the player be hidden
	*/
	public void setHidden(boolean shouldHide) {
		isHidden = shouldHide;
	}
	/**
    * Set the players usergroup
    * @param group - group to change to
    */
	public void setUserGroup(UserGroup group) {
		boolean set = false;
		ArrayList<Integer> groupIds = new ArrayList<>();
		Iterator<UserGroup> groups = userGroups.iterator();
		int i = 0;
		
		while (groups.hasNext()) {
			UserGroup current = groups.next();
			
			if (current.getWeight() == group.getWeight()) {
				userGroups.set(i,group);
				set = true;
			}
			i++;
		}
		if (!set) {
			userGroups.add(group);
		}
		for (UserGroup grp : userGroups) {
			groupIds.add(grp.getValue());
		}
		currentRealm.updateTeamsFromUserGroups();
		mainInstance.getDatabaseInstance().query("UPDATE user_groups SET groups = '" + new Gson().toJson(groupIds) + "' WHERE uniqueId = '" + player.getUniqueId() + "'", true);
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
		
		if (isSpectating && !mainInstance.getTimerInstance().timerExists("compassTarget_" + player.getUniqueId())) {
			mainInstance.getTimerInstance().createRunnableTimer("compassTarget_" + player.getUniqueId(), .5, 0, new Runnable() {
				@Override
				public void run() {					
					String key = getCustomDataKey("player_selector_index");
					
					if (!key.equals("-1")) {
						ArrayList<Player> players = currentRealm.getPlayers(false);
						
						if (players.size() > 0) {
							ArrayList<Player> playersInRealm = currentRealm.getPlayers(false);
							Integer index = Integer.parseInt(key == null ? "0" : key);
			
							if (index < playersInRealm.size()) {
								player.setCompassTarget(playersInRealm.get(index).getLocation());
							} else {
								if (index != 0) {
									player.setCompassTarget(playersInRealm.get(index-1).getLocation());
								}
							}
						}
					}
				}
			});
		} else {
			if (mainInstance.getTimerInstance().timerExists("compassTarget_" + player.getUniqueId())) {
				mainInstance.getTimerInstance().deleteTimer("compassTarget_" + player.getUniqueId());
			}
		}
	}
	/**
    * Set the players current game
    */
	public void setCurrentRealm(RealmBase game) {
		currentRealm = game;
	}
	/**
    * Set the players open menu
    */
	public void setMenu(MenuBase menu) {
		openedMenu = menu;
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
    * Set the players realm
    * @param realm - realm to set.
    * @param updatePermissions - should their permissions be updated?
    * @param updateItems - should their items be updated?
    */
	public void setRealm(Realm realm, boolean resetPlayer) {
		this.realm = realm;
		
		//Reset player stuff
		if (resetPlayer) {
			//Potion effects
			for(PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}
			//Flying status
			player.setAllowFlight(false);
			//Health/food
			player.setHealth(20);
			player.setFoodLevel(20);
			//Fire ticks
			player.setFireTicks(0);
			//Title text
			player.sendTitle("", "", 0, 0, 0);
			//Action bar text
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(""));
			//Spectating status
			setSpectatingStatus(false);
			//Level
			player.setLevel(0);
			//Opened menu
			openedMenu = null;
			//Arrows in player
			((CraftPlayer)player).getHandle().getDataWatcher().set(new DataWatcherObject<>(11, DataWatcherRegistry.b),0);
			//Set collision status
			player.setCollidable(true);
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
    * Set the players scoreboard teams and adds the given teams
    * @param teams - list of teams to create.
    * @param collisionStatus - should the player collide with their teammates
    */
	public void setScoreBoardTeams(String[] teams, Team.OptionStatus collisionStatus) {
		if (teams != null) {			
			for (int i = 0; i < teams.length; i++) {			
				Team team = board.registerNewTeam(teams[i]);
				team.setOption(Team.Option.COLLISION_RULE, collisionStatus);
			}
		}
		for (UserGroup userGroup : UserGroup.values()) {
			if (board.getTeam(userGroup.toString()) == null) {
				Team team = board.registerNewTeam(userGroup.toString());
				team.setOption(Team.Option.COLLISION_RULE, collisionStatus);
				team.setColor(userGroup.getColor());
			}
		}
	}
	/**
    * Set the teams name tag display
    * @param team - Name of the team to set
    * @param status - should the tag be displayed
    */
	public void setTeamNametagDisplay(String name, Team.OptionStatus status) {
		for (Team team : board.getTeams()) {
			if (team.getName().equalsIgnoreCase(name)) {
				team.setOption(Team.Option.NAME_TAG_VISIBILITY, status);
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
