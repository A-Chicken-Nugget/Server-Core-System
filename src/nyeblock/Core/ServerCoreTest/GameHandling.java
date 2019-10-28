package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Games.KitPvP;
import nyeblock.Core.ServerCoreTest.Games.SkyWars;
import nyeblock.Core.ServerCoreTest.Games.StepSpleef;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("deprecation")
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
				
				mainInstance.getHubInstance().playerJoin(ply);
				
				if (ply.getOpenInventory() != null) {					
					ply.closeInventory();
				}
			} else if (realm == Realm.KITPVP) {
				pd.setQueuingStatus(true);
				KitPvP gameToJoin = null;
				
				//Loop through active games to find one for the player
				for(KitPvP currentGame : kitPvpGames) {
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
					String worldName = "kitPvP_" + UUID.randomUUID();
					gameToJoin = new KitPvP(mainInstance,worldName,300,15); //900
					kitPvpGames.add(gameToJoin);
					
					//Create void world
					Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "mv create " + worldName + " normal -g VoidGenerator -t FLAT");
					
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
					gameToJoin = new StepSpleef(mainInstance,worldName,300,2,15);
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
