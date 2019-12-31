package nyeblock.Core.ServerCoreTest;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import de.xxschrandxx.awm.api.config.WorldData;
import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Interfaces.XY;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.VoidWorldGenerator;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.KitPvP;
import nyeblock.Core.ServerCoreTest.Realms.PvP;
import nyeblock.Core.ServerCoreTest.Realms.SkyWars;
import nyeblock.Core.ServerCoreTest.Realms.StepSpleef;

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
    * Find a game in the games list
    * @param realm - Realm of the game to find
    * @param mode - Mode of the pvp
    * @param type - Type of the pvp
    * @return game that has available player slots and is of the provided realm
    */
	public GameBase findGame(Realm realm, PvPMode mode, PvPType type) {
		GameBase gameToJoin = null;
		
		for (int x = 0; x < 200; x++) {
			for (int y = 0; y < 200; y++) {
				GameBase currentGame = games[x][y];
				
				if (currentGame instanceof PvP) {					
					PvP pvpGame = ((PvP)currentGame);
					
					if (currentGame != null && currentGame.getRealm().equals(realm) 
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
		}
		return gameToJoin;
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
			} if (realm == Realm.KITPVP) {
				pd.setQueuingStatus(true);
				GameBase gameToJoin = findGame(realm);
				
				//If no games are found, create one
				if (gameToJoin == null) {
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					String worldName = "game_" + UUID.randomUUID();
					gameToJoin = new KitPvP(mainInstance,worldName,900,20);
					gameToJoin.setGamePos(addGameToList(gameToJoin));
					
					//Create void world
					Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
						@Override
						public void run() {
							WorldData wd = new WorldData();
							wd.setWorldName(worldName);
							wd.setEnviroment(Environment.NORMAL);
							wd.setWorldType(WorldType.FLAT);
							wd.setGenerator(new VoidWorldGenerator());
							de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(wd);
						}
					});
					
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
					Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
						@Override
						public void run() {
							WorldData wd = new WorldData();
							wd.setWorldName(worldName);
							wd.setEnviroment(Environment.NORMAL);
							wd.setWorldType(WorldType.FLAT);
							wd.setGenerator(new VoidWorldGenerator());
							de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(wd);
						}
					});
					
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
					String worldName = "game_" + UUID.randomUUID();
					gameToJoin = new SkyWars(mainInstance,worldName,900,4,8);
					gameToJoin.setGamePos(addGameToList(gameToJoin));
					
					//Create void world
					Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
						@Override
						public void run() {
							WorldData wd = new WorldData();
							wd.setWorldName(worldName);
							wd.setEnviroment(Environment.NORMAL);
							wd.setWorldType(WorldType.FLAT);
							wd.setGenerator(new VoidWorldGenerator());
							de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(wd);
						}
					});
					
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
						GameBase gameToJoin = findGame(realm,mode,type);
						
						//If no games are found, create one
						if (gameToJoin == null) {
							ply.sendMessage(ChatColor.YELLOW + "No " + mode.toString() + " \u00BB " + type.toString() + " worlds found! Creating a new one for you...");
							String worldName = "game_" + UUID.randomUUID();
							gameToJoin = new PvP(mainInstance,worldName,300,2,2,PvPMode.DUELS,PvPType.FIST);
							gameToJoin.setGamePos(addGameToList(gameToJoin));
							
							//Create void world
							Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
								@Override
								public void run() {
									WorldData wd = new WorldData();
									wd.setWorldName(worldName);
									wd.setEnviroment(Environment.NORMAL);
									wd.setWorldType(WorldType.FLAT);
									wd.setGenerator(new VoidWorldGenerator());
									de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(wd);
								}
							});
							
							mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						} else {
							ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
							mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
						}
					}
				} else if (mode == PvPMode.TWOVTWO) {
					if (type == PvPType.FIST) {
						pd.setQueuingStatus(true);
						GameBase gameToJoin = findGame(realm,mode,type);
						
						//If no games are found, create one
						if (gameToJoin == null) {
							ply.sendMessage(ChatColor.YELLOW + "No " + mode.toString() + " \u00BB " + type.toString() + " worlds found! Creating a new one for you...");
							String worldName = "game_" + UUID.randomUUID();
							gameToJoin = new PvP(mainInstance,worldName,300,4,4,PvPMode.TWOVTWO,PvPType.FIST);
							gameToJoin.setGamePos(addGameToList(gameToJoin));
							
							//Create void world
							Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
								@Override
								public void run() {
									WorldData wd = new WorldData();
									wd.setWorldName(worldName);
									wd.setEnviroment(Environment.NORMAL);
									wd.setWorldType(WorldType.FLAT);
									wd.setGenerator(new VoidWorldGenerator());
									de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(wd);
								}
							});
							
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
		PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		
		if (game.getJoinStatus()) {			
			mainInstance.getTimerInstance().deleteTimer("worldWait_" + ply.getName());
			
			if (game.getPlayerCount() < game.getMaxPlayers()) {						
				mainInstance.getPlayerHandlingInstance().getPlayerData(ply).clearScoreboard();
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
