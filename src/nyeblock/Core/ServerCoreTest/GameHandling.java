package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.api.MVWorldManager;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.KitPvP;
import nyeblock.Core.ServerCoreTest.Realms.PvP;
import nyeblock.Core.ServerCoreTest.Realms.SkyWars;
import nyeblock.Core.ServerCoreTest.Realms.StepSpleef;
import nyeblock.Core.ServerCoreTest.Misc.XY;

@SuppressWarnings("deprecation")
public class GameHandling {
	private Main mainInstance;
	private GameBase[][] games = new GameBase[200][200];
	
	/**
    * Find a game in the games list
    * @param realm - Realm of the game to find
    * @return game that has available player slots and is of the provided realm
    */
	public GameBase findGame(Realm realm) {
		GameBase gameToJoin = null;
		
		for (int x = 0; x < 200; x++) {
			for (int y = 0; y < 200; y++) {
				GameBase currentGame = games[x][y];
				
				if (currentGame != null && currentGame.getRealm().equals(realm)) {
					if (gameToJoin != null) {		
						if (currentGame.getPlayerCount() < currentGame.getMaxPlayers() 
								&& gameToJoin.getPlayerCount() < currentGame.getPlayerCount()) {			
							gameToJoin = currentGame;
						}
					} else {						
						gameToJoin = currentGame;
					}
				}
			}
		}
		return gameToJoin;
	}
	/**
    * Find the game that the provided player is in
    * @param ply - The player to search for
    * @return the game the player is in
    */
	public GameBase findPlayerGame(Player ply) {
		GameBase game = null;
		
		for (int x = 0; x < 200; x++) {
			for (int y = 0; y < 200; y++) {
				GameBase currentGame = games[x][y];
				
				if (currentGame != null) {
					if (currentGame.isInServer(ply)) {
						game = currentGame;
					}
				}
			}
		}
		return game;
	}
	/**
    * Find an available slot and add the provided game to that slot
    * @param game - The game to add
    * @return the position the game was added to
    */
	public XY addGameToList(GameBase game) {
		XY pos = null;
		
		for (int x = 0; x < 200; x++) {
			if (pos == null) {				
				for (int y = 0; y < 200; y++) {
					if (pos == null) {						
						if (games[x][y] == null) {
							games[x][y] = game;
							pos = new XY(x,y);
						}
					} else {
						break;
					}
				}
			} else {
				break;
			}
		}
		return pos;
	}
	/**
    * Remove game from the games list
    * @param gamePos - The position of the game in the games list
    */
	public void removeGameFromList(XY gamePos) {
		games[gamePos.x][gamePos.y] = null;
	}
	/**
    * Get the number of games active for the given realm
    * @param gamePos - The position of the game in the games list
    */
	public int getGamesCount(Realm realm) {
		int count = 0;
		
		for (int x = 0; x < 200; x++) {		
			for (int y = 0; y < 200; y++) {	
				GameBase game = games[x][y];
								
				if (game != null && game.getRealm() == realm) {
					count++;
				}
			}
		}
		return count;
	}
	
	public GameHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	//Handles the player joining games
	public void joinGame(Player ply, Realm realm) {
		PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
		PlayerData pd = ph.getPlayerData(ply);
		
		if (!pd.isQueuingGame()) {
			if (realm == Realm.HUB) {
				ply.teleport(new Location(Bukkit.getWorld("world"),-9.548, 113, -11.497));
				pd.setCurrentGame(null);
				
				mainInstance.getHubInstance().playerJoin(ply);
			} else if (realm == Realm.KITPVP) {
				pd.setQueuingStatus(true);
				GameBase gameToJoin = findGame(realm);
				
				//If no games are found, create one
				if (gameToJoin == null) {
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					String worldName = "game_" + UUID.randomUUID();
					gameToJoin = new KitPvP(mainInstance,worldName,900,20);
					gameToJoin.setGamePos(addGameToList(gameToJoin));
					
					//Create void world
					MVWorldManager mvwm = mainInstance.getMultiverseInstance().getMVWorldManager();
					mvwm.addWorld(worldName, Environment.NORMAL, null, WorldType.FLAT, false, "VoidGenerator");
					
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				}
			} else if (realm == Realm.STEPSPLEEF) {
				pd.setQueuingStatus(true);
				GameBase gameToJoin = findGame(realm);
				
				//If no games are found, create one
				if (gameToJoin == null) {
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					String worldName = "game_" + UUID.randomUUID();
					gameToJoin = new StepSpleef(mainInstance,worldName,600,5,20);
					gameToJoin.setGamePos(addGameToList(gameToJoin));
					
					//Create void world
					MVWorldManager mvwm = mainInstance.getMultiverseInstance().getMVWorldManager();
					mvwm.addWorld(worldName, Environment.NORMAL, null, WorldType.FLAT, false, "VoidGenerator");			
					
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				}
			} else if (realm == Realm.SKYWARS) {
				pd.setQueuingStatus(true);
				GameBase gameToJoin = findGame(realm);
				
				//If no games are found, create one
				if (gameToJoin == null) {
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					String worldName = "skyWars_" + UUID.randomUUID();
					gameToJoin = new SkyWars(mainInstance,worldName,900,4,8);
					gameToJoin.setGamePos(addGameToList(gameToJoin));
					
					//Create void world
					MVWorldManager mvwm = mainInstance.getMultiverseInstance().getMVWorldManager();
					mvwm.addWorld(worldName, Environment.NORMAL, null, WorldType.FLAT, false, "VoidGenerator");		
					
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				}
			} else if (realm == Realm.PVP) {
				PvPMode mode = PvPMode.fromInt(Integer.parseInt(pd.getCustomDataKey("pvp_mode")));
				PvPType type = PvPType.fromInt(Integer.parseInt(pd.getCustomDataKey("pvp_type")));
				
				if (mode == PvPMode.DUELS) {
					if (type == PvPType.FIST) {						
						pd.setQueuingStatus(true);
						GameBase gameToJoin = findGame(realm);
						
						//If no games are found, create one
						if (gameToJoin == null) {
							ply.sendMessage(ChatColor.YELLOW + "No " + mode.toString() + " \u00BB " + type.toString() + " worlds found! Creating a new one for you...");
							String worldName = "game_" + UUID.randomUUID();
							gameToJoin = new PvP(mainInstance,worldName,300,2,2,PvPMode.DUELS,PvPType.FIST);
							gameToJoin.setGamePos(addGameToList(gameToJoin));
							
							//Create void world
							MVWorldManager mvwm = mainInstance.getMultiverseInstance().getMVWorldManager();
							mvwm.addWorld(worldName, Environment.NORMAL, null, WorldType.FLAT, false, "VoidGenerator");		
							
							mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						} else {
							ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
							mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						}
					}
				} else if (mode == PvPMode.TWOVTWO) {
					if (type == PvPType.FIST) {
						pd.setQueuingStatus(true);
						GameBase gameToJoin = findGame(realm);
						
						//If no games are found, create one
						if (gameToJoin == null) {
							ply.sendMessage(ChatColor.YELLOW + "No " + mode.toString() + " \u00BB " + type.toString() + " worlds found! Creating a new one for you...");
							String worldName = "game_" + UUID.randomUUID();
							gameToJoin = new PvP(mainInstance,worldName,300,4,4,PvPMode.TWOVTWO,PvPType.FIST);
							gameToJoin.setGamePos(addGameToList(gameToJoin));
							
							//Create void world
							MVWorldManager mvwm = mainInstance.getMultiverseInstance().getMVWorldManager();
							mvwm.addWorld(worldName, Environment.NORMAL, null, WorldType.FLAT, false, "VoidGenerator");		
							
							mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						} else {
							ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
							mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						}
					}
				}
			}
		} else {
			ply.sendMessage(ChatColor.YELLOW + "Unable to queue for a game.");
		}
	} 
	public void checkWorld(Player ply, GameBase game) {
		Realm realm = game.getRealm();
		PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		
		if (game.getJoinStatus()) {			
			mainInstance.getTimerInstance().deleteTimer("worldWait_" + ply.getName());
			
			if (game.getJoinStatus() && game.getPlayerCount() < game.getMaxPlayers()) {						
				mainInstance.getPlayerHandlingInstance().getPlayerData(ply).clearScoreboard();
				mainInstance.getHubInstance().playerLeave(ply);
				
				//Set player data
				playerData.setRealm(realm,true,true);
				//Join game
				game.join(ply);
				
				playerData.setQueuingStatus(false);
			} else {
				playerData.setQueuingStatus(false);
				ply.sendMessage(ChatColor.YELLOW + "Unable to join game. Please try again.");
			}
		}
	}
}
