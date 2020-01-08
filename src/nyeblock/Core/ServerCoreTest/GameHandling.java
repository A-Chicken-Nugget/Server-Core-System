package nyeblock.Core.ServerCoreTest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.KitPvP;
import nyeblock.Core.ServerCoreTest.Realms.PvP;
import nyeblock.Core.ServerCoreTest.Realms.SkyWars;
import nyeblock.Core.ServerCoreTest.Realms.StepSpleef;

public class GameHandling {
	private Main mainInstance;
	private GameBase[] games = new GameBase[100];
	
	public GameHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	public GameBase[] getGames() {
		return games;
	}
	/**
    * Find a game in the games list
    * @param realm - Realm of the game to find
    * @return game that has available player slots and is of the provided realm
    */
	public GameBase findGame(Realm realm) {
		GameBase gameToJoin = null;
		
		for (GameBase currentGame : games) {
			if (currentGame != null && currentGame.getRealm().equals(realm)) {
				if (gameToJoin != null) {		
					if (currentGame.getPlayerCount() < currentGame.getMaxPlayers() 
							&& gameToJoin.getPlayerCount() < currentGame.getPlayerCount()
							&& gameToJoin.getJoinStatus()) {			
						gameToJoin = currentGame;
					}
				} else {						
					if (currentGame.getPlayerCount() < currentGame.getMaxPlayers() 
							&& currentGame.getJoinStatus()) {			
						gameToJoin = currentGame;
					}
				}
			}
		}
		return gameToJoin;
	}
	/**
    * Find a game in the games list
    * @param mode - Mode of the pvp
    * @param type - Type of the pvp
    * @return game that has available player slots and is of the provided realm
    */
	public GameBase findGame(PvPMode mode, PvPType type) {
		GameBase gameToJoin = null;
		
		for (GameBase currentGame : games) {
			if (currentGame instanceof PvP) {					
				PvP pvpGame = ((PvP)currentGame);
				
				if (currentGame != null && currentGame.getRealm().equals(Realm.PVP) 
						&& pvpGame.getPvPMode().equals(mode)
						&& pvpGame.getPvPType().equals(type)) {
					if (gameToJoin != null) {		
						if (currentGame.getPlayerCount() < currentGame.getMaxPlayers() 
								&& gameToJoin.getPlayerCount() < currentGame.getPlayerCount()
								&& gameToJoin.getJoinStatus()) {			
							gameToJoin = currentGame;
						}
					} else {						
						if (currentGame.getPlayerCount() < currentGame.getMaxPlayers() 
								&& currentGame.getJoinStatus()) {			
							gameToJoin = currentGame;
						}
					}
				}
			}
		}
		return gameToJoin;
	}
	/**
    * Gets an available spot in the games list
    * @return the index of the available spot
    */
	public int getAvailablePosition() {
		int spot = -1;
		
		for (int i = 0; i < games.length; i++) {
			GameBase currentGame = games[i];
			
			if (currentGame == null) {
				spot = i;
				break;
			}
		}
		return spot;
	}
	/**
    * Add game to the games list
    * @param game - the game to add to the list
    */
	public void addGameToList(GameBase game) {
		for (int i = 0; i < games.length; i++) {
			GameBase currentGame = games[i];
			
			if (currentGame == null) {	
				games[i] = game;
				break;
			}
		}
	}
	/**
    * Remove game from the games list
    * @param id - the id if the game to remove
    */
	public void removeGameFromList(int id) {
		games[id] = null;
	}
	/**
    * Get the number of games active for the given realm
    * @param realm - The realm to search for
    */
	public int getGamesCount(Realm realm) {
		int count = 0;
		
		for (GameBase currentGame : games) {
			if (currentGame != null && currentGame.getRealm().equals(realm)) {
				count++;
			}
		}
		return count;
	}
	/**
    * Get the number of games active for the given realm
    * @param mode - Mode of the pvp
    * @param type - Type of the pvp
    */
	public int getGamesCount(PvPMode mode, PvPType type) {
		int count = 0;
		
		for (GameBase currentGame : games) {
			if (currentGame != null && currentGame.getRealm().equals(Realm.PVP)) {
				PvP pvpGame = ((PvP)currentGame);
				
				if (pvpGame.getPvPMode() == mode && pvpGame.getPvPType() == type) {					
					count++;
				}
			}
		}
		return count;
	}
	
	/**
    * Sub class player leave method
    * @param ply - Player joining the game
    * @param showLeaveMessage - Should a leave message be shown
    * @param runLeaveMethod - Should the leave method of the current realm be ran
    * @param moveToHub - Should the player be moved to the hub
    */
	//Handles the player joining games
	public void joinGame(Player ply, Realm realm) {
		PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
		PlayerData pd = ph.getPlayerData(ply);
		
		if (!pd.isQueuingGame()) {
			if (realm == Realm.HUB) {
				ply.teleport(new Location(Bukkit.getWorld("world"),-9.548, 113, -11.497));
				
				mainInstance.getHubInstance().join(ply, false);
			} else if (realm == Realm.PARKOUR) {
				mainInstance.getHubParkourInstance().join(ply, false);
			} else if (realm == Realm.PVP) {
				PvPMode mode = PvPMode.fromInt(Integer.parseInt(pd.getCustomDataKey("pvp_mode")));
				PvPType type = PvPType.fromInt(Integer.parseInt(pd.getCustomDataKey("pvp_type")));
				
				if (mode == PvPMode.DUELS) {
					if (type == PvPType.FIST) {						
						pd.setQueuingStatus(true);
						GameBase gameToJoin = findGame(mode,type);
						
						//If no games are found, create one
						if (gameToJoin == null) {
							int id = getAvailablePosition();
							gameToJoin = new PvP(mainInstance,id,"gameWorld_" + (id+1),300,2,2,PvPMode.DUELS,PvPType.FIST);
							addGameToList(gameToJoin);
							
							ply.sendMessage(ChatColor.YELLOW + "No " + mode.toString() + " \u00BB " + type.toString() + " worlds found! Creating a new one for you...");
							mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						} else {
							ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
							mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						}
					}
				} else if (mode == PvPMode.TWOVTWO) {
					if (type == PvPType.FIST) {
						pd.setQueuingStatus(true);
						GameBase gameToJoin = findGame(mode,type);
						
						//If no games are found, create one
						if (gameToJoin == null) {
							int id = getAvailablePosition();
							gameToJoin = new PvP(mainInstance,id,"gameWorld_" + (id+1),300,4,4,PvPMode.TWOVTWO,PvPType.FIST);
							addGameToList(gameToJoin);
							
							ply.sendMessage(ChatColor.YELLOW + "No " + mode.toString() + " \u00BB " + type.toString() + " worlds found! Creating a new one for you...");
							mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						} else {
							ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
							mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						}
					}
				}
			} else if (realm == Realm.KITPVP) {
				pd.setQueuingStatus(true);
				GameBase gameToJoin = findGame(realm);
				
				//If no games are found, create one
				if (gameToJoin == null) {
					int id = getAvailablePosition();
					gameToJoin = new KitPvP(mainInstance,id,"gameWorld_" + (id+1),120,20);
					addGameToList(gameToJoin);
					
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				}
			} else if (realm == Realm.STEPSPLEEF) {
				pd.setQueuingStatus(true);
				GameBase gameToJoin = findGame(realm);
				
				//If no games are found, create one
				if (gameToJoin == null) {
					int id = getAvailablePosition();
					gameToJoin = new StepSpleef(mainInstance,id,"gameWorld_" + (id+1),600,5,20);
					addGameToList(gameToJoin);
					
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				}
			} else if (realm == Realm.SKYWARS) {
				pd.setQueuingStatus(true);
				GameBase gameToJoin = findGame(realm);
				
				//If no games are found, create one
				if (gameToJoin == null) {
					int id = getAvailablePosition();
					gameToJoin = new SkyWars(mainInstance,id,"gameWorld_" + (id+1),900,4,8);
					addGameToList(gameToJoin);
					
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
				}
			}
		} else {
			ply.sendMessage(ChatColor.YELLOW + "Unable to queue for a game.");
		}
	} 
	public void checkWorld(Player ply, GameBase game) {
		if (game.getSchematicStatus()) {			
			PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			mainInstance.getTimerInstance().deleteTimer("worldWait_" + ply.getName());
			
			if (game.getPlayerCount() < game.getMaxPlayers()) {						
				playerData.clearScoreboard();
				mainInstance.getHubInstance().leave(ply, true, null);
				
				//Join game
				game.join(ply,true);
				
				playerData.setQueuingStatus(false);
			} else {
				playerData.setQueuingStatus(false);
				ply.sendMessage(ChatColor.YELLOW + "Unable to join game. Please try again.");
			}
		}
	}
}
