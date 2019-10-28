package nyeblock.Core.ServerCoreTest.Games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.SchematicHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public abstract class GameBase {
	//Instances
	protected Main mainInstance;
	protected PlayerHandling playerHandling;
	//Game info
	protected Realm realm;
	protected String worldName;
	protected String map;
	protected int minPlayers;
	protected int maxPlayers;
	//Player data
	protected ArrayList<Player> players = new ArrayList<>();
	//Game points
	protected ArrayList<Vector> spawns = new ArrayList<>();
	protected Vector safeZonePoint1;
	protected Vector safeZonePoint2;
	//Etc
	protected int emptyCount = 0;
	protected boolean canUsersJoin = false;
	
	public GameBase(Main mainInstance, String worldName) {
		mainInstance.getTimerInstance().createTimer("worldCheck_" + worldName, 1, 0, "checkWorld", true, null, this);
	}
	
	/**
    * Checks if the world has been generated. If so then it sets a schematic and allows entry to the game
    */
	public void checkWorld() {
		if (Bukkit.getWorld(worldName) != null && !canUsersJoin) {
			//Set map schematic
			SchematicHandling sh = new SchematicHandling();
			String schem = sh.setSchematic(realm, worldName);
			//Set map name
			map = schem;
			
			//Get map points
			GameMapInfo gmi = new GameMapInfo();
			ArrayList<HashMap<String,Vector>> points = gmi.getMapInfo(realm, schem);
			ArrayList<Vector> spawnPoints = new ArrayList<Vector>();
			Vector safeZonePoint1 = null;
			Vector safeZonePoint2 = null;
			
			//Go through map points           				
			for(int i = 0; i < points.size(); i++) {
				HashMap<String,Vector> point = points.get(i);
				
				for(Map.Entry<String, Vector> entry : point.entrySet()) {
					if (entry.getKey().contains("spawn")) {										
						spawnPoints.add(entry.getValue());
					} else if (entry.getKey().equalsIgnoreCase("graceBound1")) {
						safeZonePoint1 = entry.getValue();
					} else if (entry.getKey().equalsIgnoreCase("graceBound2")) {
						safeZonePoint2 = entry.getValue();
					}
				}
			}
			if (realm == Realm.KITPVP) {	
				//Set grace points
				this.safeZonePoint1 = safeZonePoint1;
				this.safeZonePoint2 = safeZonePoint2;
			}
			//Set spawn points
			spawns = spawnPoints;
			
			canUsersJoin = true;
			mainInstance.getTimerInstance().deleteTimer("worldCheck_" + worldName);
		}
	}
	/**
    * Sends a message in chat to all players in the game
    * @param message - the message to send.
    */
	public void messageToAll(String message) {
		for(Player ply : players) {
			ply.sendMessage(message);
		}
	}
	/**
    * Checks to see if the provided player is in the game
    * @param player - the player to check for.
    */
	public boolean isInServer(Player ply) {
		boolean found = false;
		
		for(Player player : players) {
			if (ply.getName().equalsIgnoreCase(player.getName())) {
				found = true;
			}
		}
		return found;
	}
	/**
    * Print a title to all players in the game
    */
	@SuppressWarnings("deprecation")
	public void titleToAll(String top, String bottom) {
		for(Player ply : players) {
			ply.sendTitle(top, bottom);
		}
	}
	/**
    * Play sound to all players in the game
    */
	public void soundToAll(Sound sound, float pitch) {
		for(Player ply : players) {
			ply.playSound(ply.getLocation(), sound, 10, pitch);
		}
	}
	
	//
	// GETTERS
	//
	
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
    * Get the current amount of players in the game
    */
	public int getPlayerCount() {
		return players.size();
	}
	/**
    * Get the max amount of players the game can have
    */
	public int getMaxPlayers() {
		return maxPlayers;
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
	public Vector getRandomSpawnPoint() {
		Vector vector = Bukkit.getWorld(worldName).getSpawnLocation().getDirection();
		
		if (spawns.size() > 0) {
			Random r = new Random();
			vector = spawns.get(r.nextInt(spawns.size()));
		}
		return vector;
	}
	
	//
	// SETTERS
	//
}
