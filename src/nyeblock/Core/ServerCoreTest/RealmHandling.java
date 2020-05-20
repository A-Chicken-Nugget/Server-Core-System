package nyeblock.Core.ServerCoreTest;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Party;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.KitPvP;
import nyeblock.Core.ServerCoreTest.Realms.PvP;
import nyeblock.Core.ServerCoreTest.Realms.SkyWars;
import nyeblock.Core.ServerCoreTest.Realms.StepSpleef;
import nyeblock.Core.ServerCoreTest.Realms.StickDuel;

public class RealmHandling {
	private Main mainInstance;
	private GameBase[] games = new GameBase[9];
	
	public RealmHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	public GameBase[] getGames() {
		return games;
	}
	public void joinGame(Player ply, GameBase game) {
		mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,game}, this);
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
    * @param realm - The realm to search for
    */
	public GameBase createGame(Realm realm, int id, String worldName) {
		GameBase game = null;
		
		if (realm == Realm.KITPVP) {
			game = new KitPvP(mainInstance,id,worldName);
		} else if (realm == Realm.PVP_2V2_FISTS || realm == Realm.PVP_2V2_WEPSARMOR || realm == Realm.PVP_DEULS_WEPSARMOR || realm == Realm.PVP_DUELS_FISTS) {
			game = new PvP(mainInstance,id,worldName,realm);
		} else if (realm == Realm.SKYWARS) {
			game = new SkyWars(mainInstance,id,worldName);
		} else if (realm == Realm.STEPSPLEEF) {
			game = new StepSpleef(mainInstance,id,worldName);
		} else if (realm == Realm.STICK_DUEL) {
			game = new StickDuel(mainInstance,id,worldName);
		}
		return game;
	}
	/**
    * Join a realm game lobby
    * @param ply - Player joining the lobby
    * @param realm - Realm lobby to join
    */
	public void joinLobby(Player ply, Realm realm) {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		pd.getCurrentRealm().leave(ply, false, null);
		
		if (realm == Realm.SKYWARS_LOBBY) {
			mainInstance.getSkyWarsLobby().join(ply, false);
		} else if (realm == Realm.STEPSPLEEF_LOBBY) {
			mainInstance.getStepSpleefLobby().join(ply, false);
		} else if (realm == Realm.KITPVP_LOBBY) {
			mainInstance.getKitPvPLobby().join(ply, false);
		} else if (realm == Realm.PVP_LOBBY) {
			mainInstance.getPvPLobby().join(ply, false);
		} else if (realm == Realm.STICK_DUEL_LOBBY) {
			mainInstance.getStickDuelLobby().join(ply, false);
		}
	}
	/**
    * Join a realm game
    * @param ply - Player joining the game
    * @param destinationRealm - Realm to join
    */
	public void joinRealm(Player ply, Realm destinationRealm) {
		PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
		PlayerData pd = ph.getPlayerData(ply);
		boolean canQueue = true;
		
		if (!pd.isQueuingGame()) {
			if (pd.getParty() != null && destinationRealm != Realm.HUB) {
				Party party = pd.getParty();
				
				if (party.getCreator().equals(ply)) {
					party.messageToAll(ChatColor.GREEN + party.getCreator().getName() + ChatColor.YELLOW + " has queued for a " + destinationRealm.toString() + " game!", false);					
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Unable to queue for game. You are in a party and not the host.");
					canQueue = false;
				}
			}
			
			if (canQueue) {
				if (destinationRealm == Realm.HUB) {
					ply.teleport(new Location(Bukkit.getWorld("world"),-9.548, 113, -11.497));
					
					mainInstance.getHubInstance().join(ply, false);
				} else if (destinationRealm == Realm.PARKOUR) {
					mainInstance.getHubParkourInstance().join(ply, false);
				} else {
					pd.setQueuingStatus(true);
					GameBase gameToJoin = findGame(destinationRealm);
					
					//If no games are found, create one
					if (gameToJoin == null) {
						int id = getAvailablePosition();
						
						if (pd.getLogSearch()) {
							System.out.println("[SEARCH DEBUG] Creating new " + destinationRealm.toString() + " game");
						}
						if (id != -1) {
							gameToJoin = createGame(destinationRealm,id,"gameWorld_" + (id+1));
							addGameToList(gameToJoin);
							
							ply.sendMessage(ChatColor.YELLOW + "No " + destinationRealm.toString() + " worlds found! Creating a new one for you...");
							mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 1, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);						
						} else {
							ply.sendMessage(ChatColor.YELLOW + "No game worlds available to create your game. Please try again later.");
						}
					} else {
						if (pd.getLogSearch()) {
							System.out.println("[SEARCH DEBUG] Joining " + destinationRealm.toString() + " game");
						}
						ply.sendMessage(ChatColor.YELLOW + "Found a game. Joining...");
						mainInstance.getTimerInstance().createMethodTimer("worldWait_" + ply.getName(), 3, 0, "checkWorld", false, new Object[] {ply,gameToJoin}, this);
					}
				}
			}
		} else {
			ply.sendMessage(ChatColor.YELLOW + "Unable to queue for a game.");
		}
	} 
	public void checkWorld(Player ply, GameBase game) {
		PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		
		if (playerData.getLogSearch()) {
			System.out.println("[SEARCH DEBUG] Check world method ran");
		}
		if (game.getSchematicStatus()) {			
			mainInstance.getTimerInstance().deleteTimer("worldWait_" + ply.getName());
			
			if (playerData.getLogSearch()) {
				System.out.println("[SEARCH DEBUG] Schematic status active");
			}
			if (game.getPlayerCount() < game.getMaxPlayers() && game.getJoinStatus()) {
				if (playerData.getLogSearch()) {
					try {
						playerData.getCurrentRealm().leave(ply, true, null);
						
						//Join game
						game.join(ply,true);
						
						playerData.setQueuingStatus(false);
						
						if (playerData.getLogSearch()) {
							System.out.println("[SEARCH DEBUG] Ran join code");
						}
					} catch (Exception ex) {
						System.out.println("[SEARCH DEBUG] Join exception: " + ex.getMessage());
						ex.printStackTrace();
					}
				} else {					
					playerData.getCurrentRealm().leave(ply, true, null);
					
					if (playerData.getParty() != null) {
						Party party = playerData.getParty();
					
						if (party.getCreator().equals(ply)) {
							party.membersJoin(game);
						}
					}
					
					//Join game
					game.join(ply,true);
					
					playerData.setQueuingStatus(false);
				}
			} else {
				if (playerData.getLogSearch()) {
					System.out.println("[SEARCH DEBUG] Unable to join game");
				}
				playerData.setQueuingStatus(false);
				ply.sendMessage(ChatColor.YELLOW + "Unable to join game. Please try again.");
			}
		} else {
			if (playerData.getLogSearch()) {
				System.out.println("[SEARCH DEBUG] Schematic status not active");
			}
		}
	}
}
