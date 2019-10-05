package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Games.GameMapInfo;
import nyeblock.Core.ServerCoreTest.Games.KitPvP;
import nyeblock.Core.ServerCoreTest.Games.SkyWars;
import nyeblock.Core.ServerCoreTest.Games.StepSpleef;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class GameHandling implements Listener {
	private Main mainInstance;
	private ArrayList<KitPvP> kitPvpGames = new ArrayList<>();
	private ArrayList<StepSpleef> stepSpleefGames = new ArrayList<>();
	private ArrayList<SkyWars> skyWarsGames = new ArrayList<>();
	
	public GameHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	//Return all of the active kitpvp games
	public ArrayList<KitPvP> getKitPvpGames() {
		return kitPvpGames;
	}
	//Return all of the active step spleef games
	public ArrayList<StepSpleef> getStepSpleefGames() {
		return stepSpleefGames;
	}
	//Return all of the active sky wars games
	public ArrayList<SkyWars> getSkyWarsGames() {
		return skyWarsGames;
	}
	
	//Remove game from list
	public void removeGame(Realm realm,String worldName) {
		if (realm == Realm.KITPVP) {			
			ArrayList<KitPvP> worldsToRemove = new ArrayList<>();
			
			for(KitPvP game : kitPvpGames) {
				if (game.getWorldName().equalsIgnoreCase(worldName)) {
					worldsToRemove.add(game);
				}
			}
			kitPvpGames.removeAll(worldsToRemove);
		} else if (realm == Realm.STEPSPLEEF) {
			ArrayList<StepSpleef> worldsToRemove = new ArrayList<>();
			
			for(StepSpleef game : stepSpleefGames) {
				if (game.getWorldName().equalsIgnoreCase(worldName)) {
					worldsToRemove.add(game);
				}
			}
			stepSpleefGames.removeAll(worldsToRemove);
		} else if (realm == Realm.SKYWARS) {
			ArrayList<SkyWars> worldsToRemove = new ArrayList<>();
			
			for(SkyWars game : skyWarsGames) {
				if (game.getWorldName().equalsIgnoreCase(worldName)) {
					worldsToRemove.add(game);
				}
			}
			skyWarsGames.removeAll(worldsToRemove);
		}
	}
	//Remove player from a game
	public void removePlayerFromGame(Player ply, Realm realm) {
		if (realm == Realm.KITPVP) {
			for (KitPvP game : kitPvpGames) {
				if (game.isInServer(ply)) {
					game.playerLeave(ply,true,false);
				}
			}
		} else if (realm == Realm.STEPSPLEEF) {
			for (StepSpleef game : stepSpleefGames) {
				if (game.isInServer(ply)) {
					game.playerLeave(ply,true,false);
				}
			}
		} else if (realm == Realm.SKYWARS) {
			for (SkyWars game : skyWarsGames) {
				if (game.isInServer(ply)) {
					game.playerLeave(ply,true,false);
				}
			}
		}
	}
	//Handles the player joining games
	public void joinGame(Player ply, Realm realm) {
		PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
		PlayerData pd = ph.getPlayerData(ply);
		
		if (!pd.isQueuingGame()) {
			if (realm == Realm.HUB) {
				ply.teleport(Bukkit.getWorld("world").getSpawnLocation());
			} else if (realm == Realm.KITPVP) {
				pd.setQueuingStatus(true);
				KitPvP gameToJoin = null;
				
				//Loop through active games to find one for the player
				for(KitPvP currentGame : kitPvpGames) {
					if (!currentGame.isGameOver()) {					
						if (gameToJoin != null) {
							if (gameToJoin.getPlayerCount() != gameToJoin.getMaxPlayers() && gameToJoin.getPlayerCount() < currentGame.getPlayerCount()) {
								gameToJoin = currentGame;
							}
						} else {
							gameToJoin = currentGame;
						}
					}
				}
				//If no games are found, create one
				if (gameToJoin == null) {
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					String worldName = "kitPvP_" + UUID.randomUUID();
					gameToJoin = new KitPvP(mainInstance,worldName,300,15); //900
					kitPvpGames.add(gameToJoin);
					
					//Create void world
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv create " + worldName + " normal -g VoidGenerator -t FLAT");
					
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", this, new Object[] {ply,worldName,realm,true});
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", this, new Object[] {ply,gameToJoin.getWorldName(),realm,false});
				}
			} else if (realm == Realm.STEPSPLEEF) {
				pd.setQueuingStatus(true);
				StepSpleef gameToJoin = null;
				
				//Loop through active games to find one for the player
				for(StepSpleef currentGame : stepSpleefGames) {
					if (currentGame.isGameActive()) {	
						if (gameToJoin != null) {
							if (gameToJoin.getPlayerCount() != gameToJoin.getMaxPlayers() && gameToJoin.getPlayerCount() < currentGame.getPlayerCount()) {
								gameToJoin = currentGame;
							}
						} else {
							gameToJoin = currentGame;
						}
					}
				}
				//If no games are found, create one
				if (gameToJoin == null) {
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					String worldName = "stepSpleef_" + UUID.randomUUID();
					gameToJoin = new StepSpleef(mainInstance,worldName,300,15);
					stepSpleefGames.add(gameToJoin);
					
					//Create void world
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv create " + worldName + " normal -g VoidGenerator -t FLAT");
					
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", this, new Object[] {ply,worldName,realm,true});
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", this, new Object[] {ply,gameToJoin.getWorldName(),realm,false});
				}
			} else if (realm == Realm.SKYWARS) {
				pd.setQueuingStatus(true);
				SkyWars gameToJoin = null;
				
				//Loop through active games to find one for the player
				for(SkyWars currentGame : skyWarsGames) {
					if (!currentGame.isGameActive()) {					
						if (gameToJoin != null) {
							if (gameToJoin.getPlayerCount() != gameToJoin.getMaxPlayers() && gameToJoin.getPlayerCount() < currentGame.getPlayerCount()) {
								gameToJoin = currentGame;
							}
						} else {
							gameToJoin = currentGame;
						}
					}
				}
				//If no games are found, create one
				if (gameToJoin == null) {
					ply.sendMessage(ChatColor.YELLOW + "No " + realm.toString() + " worlds found! Creating a new one for you...");
					String worldName = "skyWars_" + UUID.randomUUID();
					gameToJoin = new SkyWars(mainInstance,worldName,300,8); //900
					skyWarsGames.add(gameToJoin);
					
					//Create void world
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv create " + worldName + " normal -g VoidGenerator -t FLAT");
					
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", this, new Object[] {ply,worldName,realm,true});
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", this, new Object[] {ply,gameToJoin.getWorldName(),realm,false});
				}
			}
		} else {
			ply.sendMessage(ChatColor.YELLOW + "Unable to queue for a game.");
		}
	} 
	public void checkWorld(Player ply, String worldName, Realm realm, Boolean setData) {
		if (Bukkit.getWorld(worldName) != null) {
			if (realm == Realm.KITPVP) {
				for(KitPvP game : kitPvpGames) {
					if (game.getWorldName().equalsIgnoreCase(worldName)) {
						if (setData) {
							//Set map schematic
							SchematicHandling sh = new SchematicHandling();
							String schem = sh.setSchematic(realm, worldName);
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
							//Set grace points
							game.setSafeZoneBounds(safeZonePoint1, safeZonePoint2);
							//Set spawn points
							game.setSpawnPoints(spawnPoints);
							//Set map name
							game.setMap(schem);
						}
						//Join game
						game.playerJoin(ply);
						//Set player data
						PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
						PlayerData playerData = ph.getPlayerData(ply);
						playerData.setRealm(realm,true,true);
						playerData.setQueuingStatus(false);
					}
				}
			} else if (realm == Realm.STEPSPLEEF) {
				for(StepSpleef game : stepSpleefGames) {
					if (game.getWorldName().equalsIgnoreCase(worldName)) {
						if (setData) {
							//Set map schematic
							SchematicHandling sh = new SchematicHandling();
							String schem = sh.setSchematic(realm, worldName);
							//Get map points
							GameMapInfo gmi = new GameMapInfo();
							ArrayList<HashMap<String,Vector>> points = gmi.getMapInfo(realm, schem);
							ArrayList<Vector> spawnPoints = new ArrayList<Vector>();
							
							//Go through map points
							if (points != null) {	            				
								for(int i = 0; i < points.size(); i++) {
									HashMap<String,Vector> point = points.get(i);
									
									for(Map.Entry<String, Vector> entry : point.entrySet()) {
										spawnPoints.add(entry.getValue());
									}
								}
							}
							//Set spawn points
							game.setSpawnPoints(spawnPoints);
							//Set map name
							game.setMap(schem);
						}
						//Join game
						game.playerJoin(ply);
						//Set player data
						PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
						PlayerData playerData = ph.getPlayerData(ply);
						playerData.setRealm(realm,true,true);
						playerData.setQueuingStatus(false);
					}
				}
			} else if (realm == Realm.SKYWARS) {
				for(SkyWars game : skyWarsGames) {
					if (game.getWorldName().equalsIgnoreCase(worldName)) {
						if (setData) {
							//Set map schematic
							SchematicHandling sh = new SchematicHandling();
							String schem = sh.setSchematic(realm, worldName);
							//Get map points
							GameMapInfo gmi = new GameMapInfo();
							ArrayList<HashMap<String,Vector>> points = gmi.getMapInfo(realm, schem);
							ArrayList<Vector> spawnPoints = new ArrayList<Vector>();
							
							//Go through map points           				
							for(int i = 0; i < points.size(); i++) {
								HashMap<String,Vector> point = points.get(i);
								
								for(Map.Entry<String, Vector> entry : point.entrySet()) {							
									spawnPoints.add(entry.getValue());
								}
							}
							//Set spawn points
							game.setSpawnPoints(spawnPoints);
							//Set map name
							game.setMap(schem);
						}
						//Join game
						game.playerJoin(ply);
						//Set player data
						PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
						PlayerData playerData = ph.getPlayerData(ply);
						playerData.setRealm(realm,true,true);
						playerData.setQueuingStatus(false);
					}
				}
			}
			mainInstance.getTimerInstance().deleteTimer("worldWait_" + ply.getName());
		}
	}
}
