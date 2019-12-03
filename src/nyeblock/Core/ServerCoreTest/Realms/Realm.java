package nyeblock.Core.ServerCoreTest.Realms;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;

public abstract class Realm {
	private Main mainInstance;
	protected ArrayList<Player> players = new ArrayList<>();
	protected HashMap<Player,HashMap<String,Integer>> playerXP = new HashMap<>();
	
	public Realm(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	/**
    * Sends a message in chat to all players in the game
    * @param message - the message to send.
    */
	public void messageToAll(String message) {
		for(Player ply : players) {
			ply.sendMessage(ChatColor.BOLD + "§7Nye§bBlock §7\u00BB " + ChatColor.RESET + message);
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
    * @param top - top text
    * @param bottom - bottom text
    * @param fadeIn - fade in time (ms)
    * @param fadeOut - fade out time (ms)
    */
	public void titleToAll(String top, String bottom, int fadeIn, int fadeOut) {
		for(Player ply : players) {
			ply.sendTitle(top, bottom, fadeIn, 70, fadeOut);
		}
	}
	/**
    * Play sound to all players in the game
    * @param sound - sound that should be played
    * @param pitch - pitch that should be played
    */
	public void soundToAll(Sound sound, float pitch) {
		for(Player ply : players) {
			ply.playSound(ply.getLocation(), sound, 10, pitch);
		}
	}
	/**
    * Get the time when this game was created
    * @return time when the game was created
    */
	public ArrayList<Player> getPlayersInGame() {
		return players;
	}
	/**
    * Sub class player join method
    * @param ply - Player joining the game
    */
	public void playerJoin(Player ply) {};
	/**
    * Super class player join method
    * @param ply - Player joining the game
    */
	public void join(Player ply) {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		
		//Set the players current game
		pd.setCurrentGame(this);
		
		//Add to players list
		players.add(ply);
		
		//Setup player xp
		playerXP.put(ply, new HashMap<String,Integer>());
		
		//Show player has joined	
		messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has joined the game!");
		
		//Update joining players hidden/shown players
		for (Player ply2 : Bukkit.getOnlinePlayers()) {
			if (players.contains(ply2)) {
				if (!Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
					ply.showPlayer(mainInstance,ply2);
				}
			} else {								
				if (ply.canSee(ply2)) {
					ply.hidePlayer(mainInstance,ply2);
				}
			}
		}
		
		//Update current players hidden/shown players
		for (Player ply2 : players) {
			if (!ply2.canSee(ply)) {
				ply2.showPlayer(mainInstance,ply);
			}
		}
		
		playerJoin(ply);
	};

	public boolean isGameActive() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
    * Sub class player leave method
    * @param ply - Player joining the game
    * @param showLeaveMessage - Should a leave message be shown
    * @param moveToHub - Should the player be moved to the hub
    */
	public void playerLeave(Player ply, boolean showLeaveMessage, boolean moveToHub) {};
	public void leave(Player ply, boolean showLeaveMessage, boolean moveToHub) {
		playerLeave(ply,showLeaveMessage,moveToHub);
	}

	public Location getRandomSpawnPoint() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPlayerKit(Player ply) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPlayerKit(Player ply, Object playerKit) {
		// TODO Auto-generated method stub
		
	}

	public Location getPlayerSpawn(Player ply) {
		// TODO Auto-generated method stub
		return null;
	}

	public void playerDeath(Player killed, Player attacker) {
		// TODO Auto-generated method stub
		
	}

	public boolean isInGraceBounds(Player ply) {
		// TODO Auto-generated method stub
		return false;
	}

	public int getPlayerCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void forceStart() {
		// TODO Auto-generated method stub
		
	}
}
