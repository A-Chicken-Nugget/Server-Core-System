package nyeblock.Core.ServerCoreTest.Games;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public abstract class GameBase {
	//Instances
	protected Main mainInstance;
	protected PlayerHandling playerHandling;
	//Game info
	protected Realm realm;
	protected String worldName;
	protected String map;
	protected int maxPlayers;
	//Player data
	protected ArrayList<Player> players = new ArrayList<>();
	//Game points
	protected ArrayList<Vector> spawns = new ArrayList<>();
	//Etc
	protected int emptyCount = 0;
	//Scoreboard
	protected Scoreboard board;
	protected Objective objective;
	
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
	
	/**
    * Set the map for the game
    * @param map - the map to set.
    */
	public void setMap(String map) {
		this.map = map;
	}
	/**
    * Set the spawn points for the game
    * @param spawns - an array list of spawn vectors.
    */
	public void setSpawnPoints(ArrayList<Vector> spawns) {
		this.spawns = spawns;
	}
}
