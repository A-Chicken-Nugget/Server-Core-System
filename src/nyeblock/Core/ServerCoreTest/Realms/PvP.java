package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Maps.MapPoint;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings({"deprecation","serial"})
public class PvP extends GameBase {
	//Game info
	private boolean active = false;
	private boolean gameBegun = false;
	//Player data
	private ArrayList<Player> playersSpectating = new ArrayList<>();
	private ArrayList<Player> playersInGame = new ArrayList<>();
	//Etc
	private long countdownStart;
	private int readyCount = 0;
	private int messageCount = 0;
	private long lastNumber = 0;
	private PvPMode pvpMode;
	private PvPType pvpType;
	
	//
	// CONSTRUCTOR
	//
	
	public PvP(Main mainInstance, int id, String worldName, int duration, int minPlayers, int maxPlayers, Realm realm, PvPMode pvpMode, PvPType pvpType) {
		super(mainInstance,worldName);
		
		this.id = id;
		this.worldName = worldName;
		this.realm = realm;
		this.duration = duration;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.pvpMode = pvpMode;
		this.pvpType = pvpType;
	}
	
	//
	// CLASS METHODS
	//
	
	/**
    * Kick everyone in the game
    */
	public void kickEveryone() {
		ArrayList<Player> tempPlayers = new ArrayList<>(players);
		
		for (Player ply : tempPlayers) {			
			//Unhide all players who might be hidden for certain players
			for (Player player : tempPlayers) {
				if (!ply.canSee(player)) {					
					player.showPlayer(mainInstance,ply);
				}
			}
			
			leave(ply,false,Realm.HUB);
		}
	}
	/**
    * Manages count down timer
    */
	public void countDown() {
		long timeLeft = (6-((System.currentTimeMillis() / 1000L)-countdownStart));
		
		if (timeLeft <= 0) {
			gameBegun = true;
			canUsersJoin = false;
			startTime = System.currentTimeMillis() / 1000L;
			for(Player ply : players) {
				playersInGame.add(ply);
				
				//Set permissions
				PlayerData pd = playerHandling.getPlayerData(ply);
				pd.setPermission("nyeblock.canMove",true);
				pd.setPermission("nyeblock.canDamage", true);
				pd.setPermission("nyeblock.canBeDamaged", true);
				
				ply.setGameMode(GameMode.SURVIVAL);
			}
			titleToAll(ChatColor.YELLOW + "Go!"," ",0,10);
			soundToAll(Sound.BLOCK_NOTE_BLOCK_BELL,1f);
		} else {
			if (timeLeft != lastNumber) {   
				lastNumber = timeLeft;
				titleToAll(ChatColor.YELLOW + "Game starts in...",ChatColor.GREEN.toString() + timeLeft,(timeLeft == 5 ? 10 : 0),0);
				soundToAll(Sound.BLOCK_NOTE_BLOCK_PLING,1);
			}
		}
	}
	/**
	* What needs to be ran when the world is created
	*/
	public void onCreate() {
		//Set points
		for (int i = 0; i < map.getPoints().size(); i++) {
			spawns.add(i,null);
		}
		for (MapPoint point : map.getPoints()) {
			spawns.set(point.getPosition()-1,point.getLocation());
		}
		
		//Setup team spawns if they haven't been already
		int getCount = maxPlayers / 2;
		
		for (int team = 0; team < 2; team++) {				
			HashMap<Location,Player> teamSpawns = new HashMap<>();
			
			for (int spawn = 0; spawn < getCount; spawn++) {
				teamSpawns.put(spawns.get((team*getCount) + spawn), null);
			}
			teamsSetup.add(team,teamSpawns);
		}
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createMethodTimer("score_" + worldName, .5, 0, "setScoreboard", false, null, this);
		
		//Main functions timer
		mainInstance.getTimerInstance().createMethodTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
	}
	/**
	* What needs to be ran when the world is deleted
	*/
	public void onDelete() {
		mainInstance.getTimerInstance().deleteTimer("score_" + worldName);
		mainInstance.getTimerInstance().deleteTimer("main_" + worldName);
	}
	/**
    * Run main checks for the game
    */
	public void mainFunctions() {
		//Check if the server is empty
		if (players.size() > 0) {
			if (!active) {
				if (players.size() >= minPlayers || forceStart) {					
					if (readyCount == 0) {
						messageToAll(ChatColor.YELLOW + "The game will begin shortly!");
						soundToAll(Sound.BLOCK_NOTE_BLOCK_PLING,1);
					} else {
						if (readyCount >= 5) {
							active = true;
							countdownStart = System.currentTimeMillis() / 1000L;
							
							mainInstance.getTimerInstance().createMethodTimer("countdown_" + worldName, 1, 7, "countDown", false, null, this);
							
							for (Player ply : players) {
								PlayerData pd = playerHandling.getPlayerData(ply);
								
								ply.teleport(getPlayerSpawn(ply));
								
								pd.setPermission("nyeblock.canMove",false);								
								ply.getInventory().clear(8);
								
								if (ply.getFireTicks() > 0) {									
									mainInstance.getTimerInstance().createRunnableTimer("extinguish_" + ply.getName(), 1, 1, new Runnable() {
										@Override
										public void run() {
											ply.setFireTicks(0);
										}
									});
								}
								ply.setHealth(20);
								ply.setVelocity(new Vector(0,0,0));
								pd.addGamePlayed(realm, false);
							}
						}
					}
					readyCount++;
				} else {
					if (readyCount > 0) {
						readyCount = 0;
					}
					if (messageCount >= 20) {
						messageCount = 0;
						
						messageToAll(ChatColor.YELLOW + "Waiting for more players...");
					}
					messageCount++;
				}
			}
		}
	}
	/**
    * Set the players scoreboard
    */
	public void setScoreboard() {
		//Give players xp for play time
		if (gameBegun) {			
			playTimeCount++;
			if (playTimeCount >= 90 && !endStarted) {
				playTimeCount = 0;
				for (Player ply : players) {
					giveXP(ply,"Play time",5);
					ply.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You have received " + ChatColor.GREEN + "5xp" + ChatColor.YELLOW + " for playing."));
				}
			}
		}
		
		//Check if team has won
		if (gameBegun && !endStarted) {
			for (int team = 0; team < 2; team++) {
				int playersLeft = 0;
				
				for (Map.Entry<Location,Player> entry : teamsSetup.get(team).entrySet()) {
					if (entry.getValue() != null) {
						if (!playerHandling.getPlayerData(entry.getValue()).getSpectatingStatus()) {
							playersLeft++;
						}
					}
				}
				if (playersLeft == 0 && !endStarted) {
					endStarted = true;
					canUsersJoin = false;
					String namesString = "";
					ArrayList<Player> teamPlayers = new ArrayList<>();
					
					for (Map.Entry<Location,Player> entry : teamsSetup.get(team == 0 ? 1 : 0).entrySet()) {
						if (entry.getValue() != null) {
							if (namesString.equals("")) {
								namesString = entry.getValue().getName();
							} else {
								namesString += " and " + entry.getValue().getName();
							}
							teamPlayers.add(entry.getValue());
							playWinAction(entry.getValue());
							giveXP(entry.getValue(),"Winning",150);
							playerHandling.getPlayerData(entry.getValue()).addGamePlayed(realm, true);
						}
					}
					
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + namesString + " won!");
					soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
					for (Player ply : players) {
						//Print the players xp summary
						printSummary(ply,true);
					}
					mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
					break;
				}
			}
		}
		//Update players scoreboard
		for(Player ply : players)
		{       				
			int pos = 1;
			int timeLeft = (int)(duration-((System.currentTimeMillis() / 1000L)-startTime));
			PlayerData pd = playerHandling.getPlayerData(ply);
			HashMap<Integer,String> scores = new HashMap<Integer,String>();
			
			scores.put(pos++, ChatColor.GREEN + "http://nyeblock.com/");
			scores.put(pos++, ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Toolkit.formatMMSS(timeLeft)) : Toolkit.formatMMSS(duration)));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Mode: " + ChatColor.GREEN + pvpType.toString());
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
			if (shouldRainbowTitleText) {
				pd.setScoreboardTitle(chatColorList.get(new Random().nextInt(chatColorList.size())) + ChatColor.BOLD.toString() + pvpMode.toString());				
			} else {				
				pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + pvpMode.toString());
			}
			pd.updateObjectiveScores(scores);
		}
		//Manage weather/time
		if (world != null) {
			if (!setWorldTime) {				
				world.setTime(1000);
			}
			if (world.hasStorm()) {
				world.setStorm(false);
    		}
		}
		//Check when the game has ended
		if (gameBegun && (duration-((System.currentTimeMillis() / 1000L)-startTime)) < 0) {
			if (!endStarted) {
				endStarted = true;
				gameBegun = false;
				canUsersJoin = false;
				
				messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Nobody wins!");
				soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
				//Wait 8 seconds, then kick everyone
				mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
			}
		}
	}
	public PvPType getPvPType() {
		return pvpType;
	}
	public PvPMode getPvPMode() {
		return pvpMode;
	}
	/**
    * Get the close status of the game
    */
	public boolean isGameClosed() {
		return endStarted;
	}
	/**
    * Get the status of the game
    */
	public boolean isGameActive() {
		return gameBegun;
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		pd.setItems();
		ply.setFireTicks(0);
		return getPlayerSpawn(ply);
	}
	/**
    * Handle when a player died
    */
	public void playerDeath(Player killed,Player killer) {		
		boolean isSpectating = playersSpectating.contains(killed);
		killed.setFireTicks(0);
		
		//Handle spectating
		if (gameBegun && !isSpectating) {			
			PlayerData pd = playerHandling.getPlayerData(killed);
			isSpectating = true;
			
			killed.setGameMode(GameMode.ADVENTURE);
			pd.setSpectatingStatus(true);
			killed.setAllowFlight(true);
			playersSpectating.add(killed);
			playersInGame.remove(killed);
			
			killed.sendMessage(ChatColor.YELLOW + "You are now spectating. You are invisible and can fly around.");
			
			//Unhide spectators
			for (Player ply : playersSpectating) {
				killed.showPlayer(mainInstance,ply);
			}
			
			//Hide player from players in the active game
			for (Player ply : playersInGame) {
				ply.hidePlayer(mainInstance,killed);
			}
			
			//Set permissions
			pd.setPermission("nyeblock.canBreakBlocks", false);
			pd.setPermission("nyeblock.canUseInventory", false);
			pd.setPermission("nyeblock.canDamage", false);
			pd.setPermission("nyeblock.canBeDamaged", false);
			pd.setPermission("nyeblock.canDropItems", false);
			pd.setPermission("nyeblock.canLoseHunger", false);
		}
		
		if (killer != null) {
			for (int i = 0; i < 10; i++) {
				killer.playEffect(killed.getLocation(), Effect.SMOKE, 1);
			}
			killer.playSound(killer.getLocation(), Sound.ITEM_TRIDENT_HIT, 10, 1);
			
			killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName()));
			giveXP(killer,"Kills",10);
			messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.GREEN + killer.getName() + ChatColor.YELLOW + "!");
		} else {
			if (!isSpectating) {
				messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " has died!");
			}
		}		
	}
	/**
    * Handle when a player joins the game
    */
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Setup team
		pd.setScoreBoardTeams(new String[] {"team1","team2"},Team.OptionStatus.FOR_OWN_TEAM);
		pd.createHealthTags();
		
		//Find spawn for player
		teamloop:
		for (int team = 0; team < 2; team++) {
			HashMap<Location,Player> teamSpots = teamsSetup.get(team);
			
			for (Map.Entry<Location,Player> entry : teamSpots.entrySet()) {
				if (entry.getValue() == null) {
					ply.teleport(entry.getKey());
					teamSpots.put(entry.getKey(), ply);
					pd.addPlayerToTeam("team" + (team+1), ply);
					if (team == 0) {
						pd.setTeamColor("team1",ChatColor.GREEN);
						pd.setTeamColor("team2",ChatColor.RED);
					} else {
						pd.setTeamColor("team1",ChatColor.RED);
						pd.setTeamColor("team2",ChatColor.GREEN);
					}
					break teamloop;
				}
			}
		}
		int teamIndex = getPlayerTeam(ply);
		
		//Add players to teams
		for (Player player : players) {
			PlayerData pd2 = playerHandling.getPlayerData(player);
			
			if (player != ply) {
				//Update joining player team
				if (getPlayerTeam(player) == teamIndex) {
					pd.addPlayerToTeam("team" + (teamIndex+1), player);
				} else {
					if (teamIndex == 0) {
						pd.addPlayerToTeam("team2", player);
					} else {
						pd.addPlayerToTeam("team1", player);
					}
				}
				
				//Update current players teams
				pd2.addPlayerToTeam("team" + (teamIndex+1), ply);
			}
		}
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to PvP \u00BB " + pvpType.toString(),ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map.getName());
		
		for (Player player : players) {
			if (player.getHealth() == 20) {				
				player.setHealth(player.getHealth() - 0.0001);
			}
		}
	}
	/**
    * Handle when a player leaves the game
    */
	public void playerLeave(Player ply) {
		//Remove player from team
		for (int team = 0; team < 2; team++) {
			for (Map.Entry<Location,Player> entry : teamsSetup.get(team).entrySet()) {
				if (entry.getValue() != null) {					
					if (entry.getValue().getUniqueId().equals(ply.getUniqueId())) {
						teamsSetup.get(team).put(entry.getKey(), null);
					}
				}
			}
		}
		
		//Remove player from the current game
		playersInGame.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from spectating list
		playersSpectating.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from other players teams
		int teamIndex = getPlayerTeam(ply);
		for (Player player : players) {
			playerHandling.getPlayerData(player).removePlayerFromTeam("team" + (teamIndex+1), ply);
		}
	}
}
