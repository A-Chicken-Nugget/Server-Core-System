package nyeblock.Core.ServerCoreTest.Realms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.EditSession;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.SchematicHandling;
import nyeblock.Core.ServerCoreTest.CustomChests.CustomChestGenerator;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.XY;

public abstract class GameBase extends nyeblock.Core.ServerCoreTest.Realms.RealmBase {
	//Instances
	protected Main mainInstance;
	protected PlayerHandling playerHandling;
	//Game info
	protected String worldName;
	protected String map;
	protected boolean gameBegun = false;
	//Player data
	protected ArrayList<HashMap<Location,Player>> teamsSetup = new ArrayList<>();
	protected HashMap<Player,HashMap<String,Integer>> playerXP = new HashMap<>();
	//Game points
	protected ArrayList<Location> spawns = new ArrayList<>();
	protected Vector safeZonePoint1;
	protected Vector safeZonePoint2;
	//Etc
	protected boolean active = true;
	protected int emptyCount = 0;
	protected boolean canUsersJoin = false;
	protected boolean forceStart = false;
	protected int playTimeCount = 0;
	
	protected XY gamePos;
	protected boolean isSchemSet = false;
	protected long created = System.currentTimeMillis();
	EditSession editSession;
	
	public GameBase(Main mainInstance, String worldName) {
		super(mainInstance,true);
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		
		//Create timer to check when the game world is created
		mainInstance.getTimerInstance().createTimer("worldCheck_" + worldName, 1, 0, "checkWorld", true, null, this);
	}
	
	/**
    * Checks if the world has been generated. If so then it sets a schematic and allows entry to the game
    */
	public void checkWorld() {
		if (Bukkit.getWorld(worldName) != null && !canUsersJoin) {
			//Set map schematic
			SchematicHandling sh = new SchematicHandling();
			String schem = sh.setSchematic(mainInstance,this);
			//Set map name
			map = schem;
			
			//Get map points
			GameMapInfo gmi = new GameMapInfo();
			ArrayList<HashMap<String,Location>> points = gmi.getMapInfo(this);
			ArrayList<Location> spawnPoints = new ArrayList<Location>();
			Location safeZonePoint1 = null;
			Location safeZonePoint2 = null;
			
			for (int i = 0; i < maxPlayers; i++) {
				spawnPoints.add(i,null);
			}
			
			//Go through map points
			for(int i = 0; i < points.size(); i++) {
				HashMap<String,Location> point = points.get(i);
				
				for(Map.Entry<String, Location> entry : point.entrySet()) {
					if (entry.getKey().contains("spawn")) {
						String spawn[] = entry.getKey().split("_");
						
						spawnPoints.set(Integer.parseInt(spawn[1])-1,entry.getValue());
					} else if (entry.getKey().equalsIgnoreCase("graceBound1")) {
						safeZonePoint1 = entry.getValue();
					} else if (entry.getKey().equalsIgnoreCase("graceBound2")) {
						safeZonePoint2 = entry.getValue();
					}
				}
			}
			
			spawnPoints.removeAll(Collections.singleton(null));
			
			if (realm == Realm.KITPVP) {	
				//Set grace points
				this.safeZonePoint1 = safeZonePoint1.toVector();
				this.safeZonePoint2 = safeZonePoint2.toVector();
			}
			//Set spawn points
			spawns = spawnPoints;
			
			if (realm == Realm.SKYWARS) {				
				//Spawn/fill chests
				CustomChestGenerator ccg = new CustomChestGenerator(mainInstance);
				ccg.setChests(gmi.getChestInfo(this),Bukkit.getWorld(worldName));
			}
			
			canUsersJoin = true;
			mainInstance.getTimerInstance().deleteTimer("worldCheck_" + worldName);
		}
	}
	/**
	* Print players xp summary
	* @param ply - Players summary to print
	* @param boolean - Should the players xp be saved
	*/
	public void printSummary(Player ply, boolean saveXP) {
		String xp = "";
		int totalXP = 0;
		for (Map.Entry<String,Integer> entry2 : playerXP.get(ply).entrySet()) {
			if (!xp.equals("")) {
				xp += "\n" + entry2.getKey() + ": " + entry2.getValue();
				totalXP += entry2.getValue();
			} else {
				xp = entry2.getKey() + ": " + entry2.getValue();
				totalXP += entry2.getValue();
			}
		}
		
		ply.sendMessage(
			ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\n" +
			ChatColor.BOLD + "GAME SUMMARY" +
			ChatColor.RESET + ChatColor.YELLOW + "\n \n" +
			(totalXP == 0 ? "No XP received" : xp) + "\n \n" +
			"Total XP Received: " + totalXP + "\n" +
			ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A"
		);
		if (saveXP) {
			playerHandling.getPlayerData(ply).giveXP(realm, totalXP);
		}
	}
	/**
    * Forces the start of the game
    */
	public void forceStart() {
		forceStart = true;
	}
	/**
    * Get the status of the game
    * @return status of the game
    */
	public boolean isGameActive() {
		return gameBegun;
	}
	public String getPlayerKit(Player ply) { return null; };
	public void setPlayerKit(Player ply, String kit) {};
	public void playerDeath(Player killed,Player killer) {};
	public boolean isInGraceBounds(Player ply) { return false; };
	/**
    * Game player join method
    * @param ply - Player joining the game
    */
	public void gameJoin(Player ply) {
		//Setup player xp
		playerXP.put(ply, new HashMap<String,Integer>());
		
		playerJoin(ply);
		
		//Show player has joined	
		messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has joined the game!");
	}
	public Location playerRespawn(Player ply) { return null; }
	
	//
	// GETTERS
	//
	
	public boolean getActiveStatus() {
		return active;
	}
	public void giveXP(Player ply, String type, int amount) {
		HashMap<String,Integer> xpStats = playerXP.get(ply);
		
		if (xpStats.get(type) != null) {
			xpStats.put(type,xpStats.get(type)+amount);
		} else {
			xpStats.put(type,amount);
		}
	}
	public EditSession getEditSession() {
		return editSession;
	}
	/**
    * Gets the given players spawn
    * @param ply - Player whose spawn you would like to get
    * @return location of spawn
    */
	public Location getPlayerSpawn(Player ply) {
		Location loc = null;
		
		teamloop:
		for (int team = 0; team < 2; team++) {
			HashMap<Location,Player> teamSpots = teamsSetup.get(team);
			
			for (Map.Entry<Location,Player> entry : teamSpots.entrySet()) {
				if (entry.getValue() == ply) {
					loc = entry.getKey();
					break teamloop;
				}
			}
		}
		return loc;
	}
	/**
    * Returns the index of the team the player is on
    * @param ply - Player whose team you would like to get
    * @return index of team
    */
	public int getPlayerTeam(Player ply) {
		int teamIndex = -1;
		
		teamloop:
		for (int team = 0; team < 2; team++) {
			HashMap<Location,Player> teamSpots = teamsSetup.get(team);
			
			for (Map.Entry<Location,Player> entry : teamSpots.entrySet()) {
				if (entry.getValue() == ply) {
					teamIndex = team;
					break teamloop;
				}
			}
		}
		return teamIndex;
	}
	/**
    * Get the time when this game was created
    * @return time when the game was created
    */
//	public ArrayList<Player> getPlayersInGame() {
//		return players;
//	}
	/**
    * Get the time when this game was created
    * @return time when the game was created
    */
	public long getCreated() {
		return created;
	}
	/**
    * Get the position of this game in the 2d games array
    * @return the x and y of the game
    */
	public XY getGamePos() {
		return gamePos;
	}
	/**
    * Get instance of this game
    */
	public GameBase getInstance() {
		return this;
	}
	/**
    * Get the join status of the game
    */
	public boolean getJoinStatus() {
		return canUsersJoin;
	}
	/**
    * Get the name of the map schematic
    */
	public String getMap() {
		return map;
	}
	/**
    * Get the world name
    */
	public String getWorldName() {
		return worldName;
	}
	/**
    * Get a random spawn point for the game
    */
	public Location getRandomSpawnPoint() {
		Location vector = Bukkit.getWorld(worldName).getSpawnLocation();
		
		if (spawns.size() > 0) {
			Random r = new Random();
			vector = spawns.get(r.nextInt(spawns.size()));
		}
		return vector;
	}
	
	//
	// SETTERS
	//
	public void setEditSession(EditSession editSession) {
		this.editSession = editSession;
	}
	/**
    * Set the position of this game in the games list
    */
	public void setGamePos(XY pos) {
		gamePos = pos;
	}
}
