package nyeblock.Core.ServerCoreTest.Realms;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public abstract class RealmBase {
	private Main mainInstance;
	protected PlayerHandling playerHandling;
	protected ArrayList<Player> players = new ArrayList<>();
	protected Realm realm;
	protected int minPlayers = 0;
	protected int maxPlayers = 0;
	protected ArrayList<ChatColor> colorList = new ArrayList<ChatColor>() {{
		add(ChatColor.BLUE);
		add(ChatColor.GREEN);
		add(ChatColor.YELLOW);
		add(ChatColor.DARK_GREEN);
		add(ChatColor.DARK_BLUE);
		add(ChatColor.DARK_PURPLE);
		add(ChatColor.LIGHT_PURPLE);
		add(ChatColor.DARK_RED);
		add(ChatColor.RED);
		add(ChatColor.AQUA);
	}};
	
	public RealmBase(Main mainInstance) {
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	//
	// RANDOM METHODS
	//
	
	/**
	* Updates all the players hidden/shown players
	*/
	public void updateHiddenShownPlayers() {
		for (Player ply : players) {
			PlayerData pd = playerHandling.getPlayerData(ply);
			
			for (Player ply2 : players) {
				if (ply != ply2) {
					PlayerData pd2 = mainInstance.getPlayerHandlingInstance().getPlayerData(ply2);
					
					if (!ply.canSee(ply2)) {
						if (realm == Realm.HUB || realm == Realm.PARKOUR) {
							if (!Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
								if (!pd2.getHiddenStatus()) {
									ply.showPlayer(mainInstance,ply2);
								}
							} else {
								if (!pd2.getHiddenStatus()) {
									ply.showPlayer(mainInstance,ply2);
								}
							}
						} else {
							if (!pd2.getHiddenStatus()) {
								ply.showPlayer(mainInstance,ply2);						
							}
						}
					} else {
						if (realm == Realm.HUB || realm == Realm.PARKOUR) {
							if (Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
								if (pd2.getHiddenStatus()) {
									ply.hidePlayer(mainInstance,ply2);
								}
							} else {
								if (pd2.getHiddenStatus()) {
									ply.hidePlayer(mainInstance,ply2);
								}
							}
						} else {
							if (pd2.getHiddenStatus()) {
								ply.hidePlayer(mainInstance,ply2);
							}
						}
					}
					if (!ply2.canSee(ply)) {
						if (realm == Realm.HUB || realm == Realm.PARKOUR) {
							if (!Boolean.parseBoolean(pd2.getCustomDataKey("hide_players"))) {
								if (!pd.getHiddenStatus()) {
									ply2.showPlayer(mainInstance,ply);
								}
							} else {
								if (!pd.getHiddenStatus()) {
									ply2.showPlayer(mainInstance,ply);
								}
							}
						} else {
							if (!pd.getHiddenStatus()) {
								ply2.showPlayer(mainInstance,ply);						
							}
						}
					} else {
						if (realm == Realm.HUB || realm == Realm.PARKOUR) {
							if (Boolean.parseBoolean(pd2.getCustomDataKey("hide_players"))) {
								if (pd.getHiddenStatus()) {
									ply2.hidePlayer(mainInstance,ply);
								}
							} else {
								if (pd.getHiddenStatus()) {
									ply2.hidePlayer(mainInstance,ply);
								}
							}
						} else {
							if (pd.getHiddenStatus()) {
								ply2.hidePlayer(mainInstance,ply);
							}
						}
					}
				}
			}
		}
	}
	/**
	* Updates all the players teams according to the players usergroup
	*/
	public void updateTeamsFromUserGroups() {
		for (Player ply : players) {	
			PlayerData pd = playerHandling.getPlayerData(ply);
			
			for (Player player : players) {
				PlayerData pd2 = playerHandling.getPlayerData(player);
				
				//Update joining player team
				pd.addPlayerToTeam(pd2.getUserGroup().toString(), player);
				
				//Update current players teams
				pd2.addPlayerToTeam(pd.getUserGroup().toString(), ply);
			}
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
    * Sends a message in chat to all players in the game
    * @param message - the message to send.
    */
	public void messageToAll(String message) {
		for(Player ply : players) {
			ply.sendMessage(ChatColor.BOLD + "§7Nye§bBlock §7\u00BB " + ChatColor.RESET + message);
		}
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
    * Player join method
    * @param ply - Player joining
    */
	public void playerJoin(Player ply) {}
	/**
    * Game player join method
    * @param ply - Player joining the game
    */
	public void gameJoin(Player ply) {}
	/**
    * Super class player join method
    * @param ply - Player joining
    * @param isGame - Is the player joining a game
    */
	public void join(Player ply, boolean isGame) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Set the players current game
		if (pd.getCurrentRealm() != this) {
			pd.setCurrentRealm(this);
		}
		
		//Add to players list
		boolean found = false;
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getName().equalsIgnoreCase(ply.getName())) {
				if (players.get(i) != ply) {
					players.set(i, ply);
				}
				found = true;
			}
		}
		if (!found) {
			players.add(ply);
		}
		
		//Update playerdata
		pd.setRealm(realm,true,true,true);
		
		//Show/hide players accordingly
		for (Player ply2 : Bukkit.getOnlinePlayers()) {
			if (players.contains(ply2)) {
				PlayerData pd2 = playerHandling.getPlayerData(ply2);
				
				if (!ply.canSee(ply2)) {
					if (realm == Realm.HUB || realm == Realm.PARKOUR) {
						if (!Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
							if (!pd2.getHiddenStatus()) {
								ply.showPlayer(mainInstance,ply2);
							}
						} else {
							if (!pd2.getHiddenStatus()) {
								ply.showPlayer(mainInstance,ply2);
							}
						}
					} else {
						if (!pd2.getHiddenStatus()) {
							ply.showPlayer(mainInstance,ply2);						
						}
					}
				} else {
					if (realm == Realm.HUB || realm == Realm.PARKOUR) {
						if (Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
							if (pd2.getHiddenStatus()) {
								ply.hidePlayer(mainInstance,ply2);
							}
						} else {
							if (pd2.getHiddenStatus()) {
								ply.hidePlayer(mainInstance,ply2);
							}
						}
					}
				}
				if (!ply2.canSee(ply)) {
					if (realm == Realm.HUB || realm == Realm.PARKOUR) {
						if (!Boolean.parseBoolean(pd2.getCustomDataKey("hide_players"))) {
							if (!pd.getHiddenStatus()) {
								ply2.showPlayer(mainInstance,ply);
							}
						} else {
							if (!pd.getHiddenStatus()) {
								ply2.showPlayer(mainInstance,ply);
							}
						}
					} else {
						if (!pd.getHiddenStatus()) {
							ply2.showPlayer(mainInstance,ply);						
						}
					}
				} else {
					if (realm == Realm.HUB || realm == Realm.PARKOUR) {
						if (Boolean.parseBoolean(pd2.getCustomDataKey("hide_players"))) {
							if (pd.getHiddenStatus()) {
								ply2.hidePlayer(mainInstance,ply);
							}
						} else {
							if (pd.getHiddenStatus()) {
								ply2.hidePlayer(mainInstance,ply);
							}
						}
					}
				}
			} else {
				if (ply.canSee(ply2)) {					
					ply.hidePlayer(mainInstance,ply2);
				}
				if (ply2.canSee(ply)) {
					ply2.hidePlayer(mainInstance,ply);
				}
			}
		}
		
		//Run the proper join method depending on the type of realm
		if (isGame) {
			gameJoin(ply);
		} else {
			playerJoin(ply);
		}
		
		mainInstance.getTimerInstance().createTimer2("setGamemode_" + ply.getUniqueId(), 1, 0, new Runnable() {
			@Override
			public void run() {
				if (ply.getGameMode() != GameMode.ADVENTURE) {
					ply.setGameMode(GameMode.ADVENTURE);
				} else {
					mainInstance.getTimerInstance().deleteTimer("setGamemode_" + ply.getUniqueId());
				}
			}
		});
	}
	/**
    * Sub class player leave method
    * @param ply - Player joining the game
    */
	public void playerLeave(Player ply) {};
	/**
    * Player leave method
    * @param ply - Player joining the game
    * @param showLeaveMessage - Should a leave message be shown
    * @param destination - The realm to send the player
    */
	public void leave(Player ply, boolean showLeaveMessage, Realm destination) {
		players.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		if (this instanceof GameBase) {
			((GameBase)this).gameLeave(ply, showLeaveMessage);
		} else {			
			playerLeave(ply);
		}
		
		if (destination != null) {			
			mainInstance.getGameInstance().joinGame(ply, destination);
		}
	}
	public void playerDeath(Player killed, Player attacker) {}
	public Location playerRespawn(Player ply) { return null; }
	
	//
	// GETTERS
	//
	
	/**
    * Get the max amount of players the realm can have
    */
	public int getMaxPlayers() {
		return maxPlayers;
	}
	/**
    * Get the min amount of players the realm can have
    */
	public int getMinPlayers() {
		return minPlayers;
	}
	/**
    * Get all the players in the realm
    * @return list of players
    */
	public ArrayList<Player> getPlayersInRealm() {
		return players;
	}
	public Location getRandomSpawnPoint() { return null; }
	public Location getPlayerSpawn(Player ply) { return null; }
	/**
	 * Get the realm of this game
	 */
	public Realm getRealm() {
		return realm;
	}
	/**
    * Get the current amount of players in the game
    */
	public int getPlayerCount() {
		return players.size();
	}
}
