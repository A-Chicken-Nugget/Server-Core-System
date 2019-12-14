package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings({"deprecation","serial"})
public class PvP extends GameBase {
	//Game info
	private int duration;
	private long startTime;
	private boolean active = false;
	private boolean gameBegun = false;
	//Player data
	private HashMap<String,Integer> playerKills = new HashMap<>();
	private ArrayList<Player> playersSpectating = new ArrayList<>();
	private ArrayList<Player> playersInGame = new ArrayList<>();
	//Etc
	private long countdownStart;
	private int readyCount = 0;
	private int messageCount = 0;
	private boolean endStarted = false;
	private long lastNumber = 0;
	private PvPMode pvpMode;
	private PvPType pvpType;
	
	//
	// CONSTRUCTOR
	//
	
	public PvP(Main mainInstance, String worldName, int duration, int minPlayers, int maxPlayers, PvPMode pvpMode, PvPType pvpType) {
		super(mainInstance,worldName);
		
		this.worldName = worldName;
		realm = Realm.PVP;
		this.duration = duration;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		this.pvpMode = pvpMode;
		this.pvpType = pvpType;
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("score_" + worldName, .5, 0, "setScoreboard", false, null, this);
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
	}
	
	//
	// CLASS METHODS
	//
	
	/**
    * Kick everyone in the game
    */
	public void kickEveryone() {
		mainInstance.getTimerInstance().deleteTimer(worldName + "_fireworks");
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
    * Run main checks for the game
    */
	public void mainFunctions() {
		//Setup team spawns if they haven't been already
		if (spawns.size() > 0 && teamsSetup.size() == 0) {
			int getCount = maxPlayers / 2;
			
			for (int team = 0; team < 2; team++) {				
				HashMap<Location,Player> teamSpawns = new HashMap<>();
				
				for (int spawn = 0; spawn < getCount; spawn++) {
					teamSpawns.put(spawns.get((team*getCount) + spawn), null);
				}
				teamsSetup.add(team,teamSpawns);
			}
		}
		
		//Check if the server is empty
		if (players.size() > 0) {        			
			if (emptyCount != 0) {
				emptyCount = 0;
			}
			if (!active) {
				if (players.size() >= minPlayers || forceStart) {					
					if (readyCount == 0) {
						messageToAll(ChatColor.YELLOW + "The game will begin shortly!");
						soundToAll(Sound.BLOCK_NOTE_BLOCK_PLING,1);
					} else {
						if (readyCount >= 5) {
							active = true;
							countdownStart = System.currentTimeMillis() / 1000L;
							
							mainInstance.getTimerInstance().createTimer("countdown_" + worldName, 1, 7, "countDown", false, null, this);
							
							for (Player ply : players) {
								PlayerData pd = playerHandling.getPlayerData(ply);
								
								ply.teleport(getPlayerSpawn(ply));
								
								pd.setPermission("nyeblock.canMove",false);								
								ply.getInventory().clear(8);
								
								if (ply.getFireTicks() > 0) {									
									mainInstance.getTimerInstance().createTimer2("extinguish_" + ply.getName(), 1, 1, new Runnable() {
										@Override
										public void run() {
											ply.setFireTicks(0);
										}
									});
								}
								ply.setHealth(20);
								ply.setVelocity(new Vector(0,0,0));
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
		} else {
			emptyCount++;
			
			//Check if the server has been empty for 6 seconds
			if (emptyCount >= 10) {
				canUsersJoin = false;
				
				mainInstance.getTimerInstance().deleteTimer("score_" + worldName);
				mainInstance.getTimerInstance().deleteTimer("main_" + worldName);
				
				//Delete world from server
				mainInstance.getMultiverseInstance().deleteWorld(worldName);
				//Remove game from games array
				mainInstance.getGameInstance().removeGameFromList(gamePos);
			}
		}
	}
	/**
    * Set the players scoreboard
    */
	public void setScoreboard() {
		//Give players xp for play time
		playTimeCount++;
		if (playTimeCount >= 180 && !endStarted) {
			playTimeCount = 0;
			for (Player ply : players) {
				giveXP(ply,"Play time",5);
				ply.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You have received " + ChatColor.GREEN + "5xp" + ChatColor.YELLOW + " for playing."));
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
				System.out.println((team+1) + " Left: " + playersLeft);
				if (playersLeft <= 0 && !endStarted) {
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
							giveXP(entry.getValue(),"Winning",150);
						}
					}
					
					mainInstance.getTimerInstance().createTimer2(worldName + "_fireworks", .7, 0, new Runnable() {
						@Override
						public void run() {
							List<Color> c = new ArrayList<Color>();
			                c.add(Color.GREEN);
			                c.add(Color.RED);
			                c.add(Color.BLUE);
			                c.add(Color.ORANGE);
			                c.add(Color.YELLOW);
			                FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(c).withFade(c).with(Type.STAR).trail(true).build();
							
			                for (Player ply : teamPlayers) {
			                	if (!playerHandling.getPlayerData(ply).getSpectatingStatus()) {			                		
			                		Firework firework = ply.getWorld().spawn(ply.getLocation(), Firework.class);
			                		FireworkMeta fireworkMeta = firework.getFireworkMeta();
			                		fireworkMeta.addEffect(effect);
			                		fireworkMeta.setPower(2);
			                		firework.setFireworkMeta(fireworkMeta);
			                	}
			                }
						}
					});
					
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + namesString + " won!");
					for (Player ply : players) {
						//Print the players xp summary
						printSummary(ply,true);
					}
					mainInstance.getTimerInstance().createTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
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
			if (!endStarted) {				
				pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + pvpMode.toString());
			} else {
				pd.setScoreboardTitle(colorList.get(new Random().nextInt(colorList.size())) + ChatColor.BOLD.toString() + pvpMode.toString());
			}
			pd.updateObjectiveScores(scores);
		}
		//Manage weather/time
		World world = Bukkit.getWorld(worldName);
		if (world != null) {        			
			world.setTime(1000);
			if (world.hasStorm()) {
				world.setStorm(false);
    		}
		}
		//Check when the game has ended and determine winner
		if (gameBegun && (duration-((System.currentTimeMillis() / 1000L)-startTime)) < 0) {
			if (!endStarted) {
				endStarted = true;
				gameBegun = false;
				canUsersJoin = false;
				
				messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Nobody wins!");
				//Wait 8 seconds, then kick everyone
				mainInstance.getTimerInstance().createTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
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
    * Handle when a player died
    */
	public void playerDeath(Player killed,Player killer) {		
		boolean isSpectating = playersSpectating.contains(killed);
		killed.setFireTicks(0);
		
		//Handle spectating
		if (gameBegun && !isSpectating) {			
			PlayerData pd = playerHandling.getPlayerData(killed);
			isSpectating = true;
			
			pd.setSpectatingStatus(true);
			killed.setAllowFlight(true);
			playersSpectating.add(killed);
			
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
			killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName()));
			giveXP(killer,"Kills",10);
			messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.GREEN + killer.getName() + ChatColor.YELLOW + "!");
		} else {
			if (!isSpectating) {
				messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " has died!");
			}
		}
		
		//Remove player from players array
		playersInGame.removeAll(new ArrayList<Player>() {{
			add(killed);
		}});			
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
					System.out.println("Adding " + ply.getName() + " to team " + (team+1));
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
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to PvP \u00BB " + pvpType.toString(),ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map);
		
		for (Player player : players) {
			if (player.getHealth() == 20) {				
				player.setHealth(player.getHealth() - 0.0001);
			}
		}
	}
	/**
    * Handle when a player leaves the game
    */
	public void playerLeave(Player ply, boolean showLeaveMessage) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
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
		
		//Clear scoreboard
		pd.clearScoreboard();
		
		//Remove player from other players teams
		int teamIndex = getPlayerTeam(ply);
		for (Player player : players) {
			playerHandling.getPlayerData(player).removePlayerFromTeam("team" + (teamIndex+1), ply);
		}
		
		if (showLeaveMessage) {
			if (!active) {				
				messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has left the game!");
			}
		}
	}
}
