package nyeblock.Core.ServerCoreTest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Games.GameBase;
import nyeblock.Core.ServerCoreTest.Games.KitPvP;
import nyeblock.Core.ServerCoreTest.Games.SkyWars;
import nyeblock.Core.ServerCoreTest.Games.StepSpleef;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.GameUtils;

@SuppressWarnings("deprecation")
public class GameHandling {
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
	//Get the game the player is in
	public GameBase getPlayerGame(Player ply, Realm realm) {
		GameBase gameClass = null;
		
		if (realm == Realm.KITPVP) {
			for (KitPvP game : kitPvpGames) {
				if (game.isInServer(ply)) {
					gameClass = game.getInstance();
				}
			}
		} else if (realm == Realm.STEPSPLEEF) {
			for (StepSpleef game : stepSpleefGames) {
				if (game.isInServer(ply)) {
					gameClass = game;
				}
			}
		} else if (realm == Realm.SKYWARS) {
			for (SkyWars game : skyWarsGames) {
				if (game.isInServer(ply)) {
					gameClass = game;
				}
			}
		}
		return gameClass;
	}
	//Handles the player joining games
	public void joinGame(Player ply, Realm realm) {
		PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
		PlayerData pd = ph.getPlayerData(ply);
		
		if (!pd.isQueuingGame()) {
			if (realm == Realm.HUB) {
				ply.teleport(new Location(Bukkit.getWorld("world"),-9.548, 113, -11.497));
				
				mainInstance.getHubInstance().playerJoin(ply);
				
				if (ply.getOpenInventory() != null) {					
					ply.closeInventory();
				}
			} else if (realm == Realm.KITPVP) {
				pd.setQueuingStatus(true);
				KitPvP gameToJoin = null;
				
				//Loop through active games to find one for the player
				for(KitPvP currentGame : kitPvpGames) {
					if (currentGame.getJoinStatus() && !currentGame.isGameOver()) {					
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
//					Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
//			            @Override
//			            public void run() {
//							GameUtils.copyWorld(new File("./game_worlds/kitpvp_nether").toPath(), new File("./" + worldName).toPath());
//							mainInstance.getMultiverseInstance().getMVWorldManager().world
//			            }
//					});
					MVWorldManager mvwm = mainInstance.getMultiverseInstance().getMVWorldManager();
					mvwm.addWorld(worldName, Environment.NORMAL, null, WorldType.FLAT, false, "VoidGenerator");
	            	
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,worldName,realm,true}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin.getWorldName(),realm,false}, this);
				}
			} else if (realm == Realm.STEPSPLEEF) {
				pd.setQueuingStatus(true);
				StepSpleef gameToJoin = null;
				
				//Loop through active games to find one for the player
				for(StepSpleef currentGame : stepSpleefGames) {
					if (currentGame.getJoinStatus() && !currentGame.isGameActive()) {	
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
					gameToJoin = new StepSpleef(mainInstance,worldName,300,6,15);
					stepSpleefGames.add(gameToJoin);
					
					//Create void world
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv create " + worldName + " normal -g VoidGenerator -t FLAT");
					
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,worldName,realm,true}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin.getWorldName(),realm,false}, this);
				}
			} else if (realm == Realm.SKYWARS) {
				pd.setQueuingStatus(true);
				SkyWars gameToJoin = null;
				
				//Loop through active games to find one for the player
				for(SkyWars currentGame : skyWarsGames) {
					if (currentGame.getJoinStatus()) {					
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
					
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,worldName,realm,true}, this);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
					mainInstance.getTimerInstance().createTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin.getWorldName(),realm,false}, this);
				}
			}
		} else {
			ply.sendMessage(ChatColor.YELLOW + "Unable to queue for a game.");
		}
	} 
	public void checkWorld(Player ply, String worldName, Realm realm, Boolean setData) {
		PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		
		if (realm == Realm.KITPVP) {
			for (KitPvP game : kitPvpGames) {
				if (game.getWorldName().equalsIgnoreCase(worldName) && game.getJoinStatus()) {
					mainInstance.getPlayerHandlingInstance().getPlayerData(ply).clearScoreboard();
					mainInstance.getHubInstance().playerLeave(ply);
					mainInstance.getTimerInstance().deleteTimer("worldWait_" + ply.getName());
					
					//Join game
					game.playerJoin(ply);
					//Set player data
					playerData.setRealm(realm,true,true);
					playerData.setQueuingStatus(false);
					//Clear hidden players
					for (Player player : Bukkit.getWorld("world").getPlayers()) {
						ply.showPlayer(player);
						
						if (!player.canSee(ply)) {
							player.showPlayer(ply);
						}
					}
				}
			}
		} else if (realm == Realm.STEPSPLEEF) {
			for (StepSpleef game : stepSpleefGames) {
				if (game.getWorldName().equalsIgnoreCase(worldName) && game.getJoinStatus()) {
					mainInstance.getPlayerHandlingInstance().getPlayerData(ply).clearScoreboard();
					mainInstance.getHubInstance().playerLeave(ply);
					mainInstance.getTimerInstance().deleteTimer("worldWait_" + ply.getName());
					
					//Join game
					game.playerJoin(ply);
					//Set player data
					playerData.setRealm(realm,true,true);
					playerData.setQueuingStatus(false);
					//Clear hidden players
					for (Player player : Bukkit.getWorld("world").getPlayers()) {
						ply.showPlayer(player);
					}
				}
			}
		} else if (realm == Realm.SKYWARS) {
			for (SkyWars game : skyWarsGames) {
				if (game.getWorldName().equalsIgnoreCase(worldName) && game.getJoinStatus()) {
					mainInstance.getPlayerHandlingInstance().getPlayerData(ply).clearScoreboard();
					mainInstance.getHubInstance().playerLeave(ply);
					mainInstance.getTimerInstance().deleteTimer("worldWait_" + ply.getName());
					
					//Join game
					game.playerJoin(ply);
					//Set player data
					playerData.setRealm(realm,true,true);
					playerData.setQueuingStatus(false);
					//Clear hidden players
					for (Player player : Bukkit.getWorld("world").getPlayers()) {
						ply.showPlayer(player);
					}
				}
			}
		}
	}
}
