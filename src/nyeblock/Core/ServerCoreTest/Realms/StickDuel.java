package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Items.ReturnToLobby;
import nyeblock.Core.ServerCoreTest.Maps.MapPoint;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;
import nyeblock.Core.ServerCoreTest.Misc.Enums.GameStatusType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.SummaryStatType;

@SuppressWarnings({"deprecation","serial"})
public class StickDuel extends GameBase {
	//Game info
	private boolean active = false;
	private HashMap<Integer,Location> teamBeds = new HashMap<>();
	private HashMap<Integer,Integer> teamLifeCount = new HashMap<>(); 
	//Player data
	private ArrayList<Player> playersInGame = new ArrayList<>();
	//Etc
	private long countdownStart;
	private int readyCount = 0;
	private int messageCount = 0;
	private long lastNumber = 0;
	private int timeLeft = 0;
	
	//
	// CONSTRUCTOR
	//
	
	public StickDuel(Main mainInstance, int id, String worldName) {
		super(mainInstance,Realm.STICK_DUEL,worldName,Realm.STICK_DUEL_LOBBY);
		
		this.id = id;
		this.worldName = worldName;
		duration = 240;
		minPlayers = 2;
		maxPlayers = 2;
	}
	
	public StickDuel(Main mainInstance, int id, String worldName, int duration) {
		super(mainInstance,Realm.STICK_DUEL,worldName,Realm.STICK_DUEL_LOBBY);
		
		this.id = id;
		this.worldName = worldName;
		this.duration = duration;
		minPlayers = 2;
		maxPlayers = 2;
	}
	
	//
	// CLASS METHODS
	//
	
	public String getLifeDisplay(int lifes) {
		String display = "";
		
		if (lifes > 0) {
			for (int i = 0; i < lifes; i ++) {
				display += ChatColor.GREEN + "\u2B24 ";
			}
		} else {
			display = ChatColor.RED + "\u2716";
		}
		return display;
	}
	/**
    * Kick everyone in the game
    */
	public void kickEveryone() {
		ArrayList<Player> tempPlayers = getPlayers(true);
		
		for (Player ply : tempPlayers) {			
			//Unhide all players who might be hidden for certain players
			for (Player player : tempPlayers) {
				if (!ply.canSee(player)) {					
					player.showPlayer(mainInstance,ply);
				}
			}
			
			leave(ply,false,lobbyRealm);
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
			status = GameStatusType.ACTIVE;
			
			for(Player ply : players) {
				playersInGame.add(ply);
				
				//Set permissions
				PlayerData pd = playerHandling.getPlayerData(ply);
				pd.setPermission("nyeblock.canMove",true);
				pd.setPermission("nyeblock.canDamage", true);
				pd.setPermission("nyeblock.canBeDamaged", true);
				pd.setPermission("nyeblock.canPlaceBlocks", true);
				
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
		ArrayList<MapPoint> spawnPoints = map.getTypePoints(MapPointType.PLAYER_SPAWN);
		
		//Scoreboard
		scoreboard = new Runnable() {
			@Override
			public void run() {
				ArrayList<Player> allPlayers = new ArrayList<>(players);
				allPlayers.addAll(silentPlayers);
				
				for(Player ply : allPlayers)
				{       				
					int pos = 1;
					if (!endStarted) {	
						timeLeft = (int)(duration-((System.currentTimeMillis() / 1000L)-startTime));
					}
					PlayerData pd = playerHandling.getPlayerData(ply);
					HashMap<Integer,String> scores = new HashMap<Integer,String>();
					
					scores.put(pos++, ChatColor.GREEN + "http://nyeblock.com/");
					scores.put(pos++, ChatColor.RESET.toString());
					if (gameBegun) {
						scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Toolkit.formatMMSS(timeLeft)) : Toolkit.formatMMSS(duration)));
						scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
						scores.put(pos++, ChatColor.YELLOW + "Team 2: " + getLifeDisplay(teamLifeCount.get(1)) + (getPlayerTeam(ply) == 1 ? (ChatColor.GRAY + " (You)") : ""));
						scores.put(pos++, ChatColor.YELLOW + "Team 1: " + getLifeDisplay(teamLifeCount.get(0)) + (getPlayerTeam(ply) == 0 ? (ChatColor.GRAY + " (You)") : ""));
					} else {
						scores.put(pos++, ChatColor.YELLOW + "Status: " + ChatColor.GREEN + status.getText());
					}
					scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					if (shouldRainbowTitleText) {
						pd.setScoreboardTitle(chatColorList.get(new Random().nextInt(chatColorList.size())) + ChatColor.BOLD.toString() + realm.toString());				
					} else {				
						pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + realm.toString());
					}
					pd.updateObjectiveScores(scores);
				}
			}
		};
		
		//Set points
		for (int i = 0; i < spawnPoints.size(); i++) {
			spawns.add(i,spawnPoints.get(i).getLocation());
		}
		
		//Spawn beds
		for (MapPoint point : map.getTypePoints(MapPointType.BED_SPAWN)) {
			teamBeds.put(point.getPosition()-1,point.getLocation());
			teamLifeCount.put(point.getPosition()-1,2);
			Bukkit.getScheduler().runTask(mainInstance, new Runnable() {
			    @Override
			    public void run() {
			    	Location bedLoc = point.getLocation();
			    	
		    		Toolkit.setBed(bedLoc.getBlock(), Toolkit.getBlockFaceFromYaw(bedLoc.getYaw()), Material.RED_BED);
			    }
			});
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
		
		//Main functions timer
		mainInstance.getTimerInstance().createMethodTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
	}
	/**
	* What needs to be ran when the world is deleted
	*/
	public void onDelete() {
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
						status = GameStatusType.STARTING;
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
								setItems(ply);
								
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
					if (!status.equals(GameStatusType.WAITING_FOR_PLAYERS)) {
						status = GameStatusType.WAITING_FOR_PLAYERS;
					}
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
		//Check if a team is empty
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
							addStat(entry.getValue(),"Winning",150,SummaryStatType.XP);
							playerHandling.getPlayerData(entry.getValue()).addGamePlayed(realm, true);
						}
					}
					for (Player ply : playersInGame) {
						PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
						
						pd.setPermission("nyeblock.canDamage", false);
						pd.setPermission("nyeblock.canBeDamaged", false);
						printSummary(ply,true);
					}
					
					soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + namesString + " won!");
					mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 10, 1, "kickEveryone", false, null, this);
				}
			}
		}
		//Check when the game has ended
		if (gameBegun && (duration-((System.currentTimeMillis() / 1000L)-startTime)) < 0) {
			if (!endStarted) {
				endStarted = true;
				canUsersJoin = false;
				
				messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Nobody wins!");
				soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
				mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
			}
		}
		//Check if players are on island
		for (Player ply : players) {
			if (!Toolkit.playerInArea(ply.getLocation().toVector(),new Vector(-58,65,-23), new Vector(32,0,31))) {
				ply.teleport(getPlayerSpawn(ply));
			}
		}
	}
	public void bedBreak(int bedX, Player breaker) {
		if (gameBegun) {			
			for (Map.Entry<Integer,Location> entry : teamBeds.entrySet()) {
				if (Math.abs(Math.abs(entry.getValue().getX())-Math.abs(bedX)) < 2) {
					Integer lifeCount = teamLifeCount.get(entry.getKey());
					
					addStat(breaker,"Breaking bed",25,SummaryStatType.XP);
					if (lifeCount <= 1) {
						int winningTeam = entry.getKey() == 0 ? 1 : 0;
						String winString = "";
						endStarted = true;
						
						for (Map.Entry<Location,Player> team : teamsSetup.get(winningTeam).entrySet()) {
							if (entry.getValue() != null) {
								if (winString.equals("")) {
									winString = team.getValue().getName();
								} else {
									winString += " and " + team.getValue().getName();
								}
								playWinAction(team.getValue());
								addStat(team.getValue(),"Winning",150,SummaryStatType.XP);
								playerHandling.getPlayerData(team.getValue()).addGamePlayed(realm, true);
							}
						}
						for (Player ply : playersInGame) {
							PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
							
							pd.setPermission("nyeblock.canDamage", false);
							pd.setPermission("nyeblock.canBeDamaged", false);
							printSummary(ply,true);
						}
						
						soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
						messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + winString + " won!");
						mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 10, 1, "kickEveryone", false, null, this);
					} else {
						world.strikeLightning(entry.getValue());
						
						for (Player ply : players) {
							if (getPlayerTeam(ply) == entry.getKey()) {
								ply.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Your bed has been broken");
							} else {
								ply.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Team " + (entry.getKey()+1) + "'s bed has been broken");
							}
						}
						mainInstance.getTimerInstance().createRunnableTimer("bedSpawnWait_" + world.getName(), .5, 1, new Runnable() {
							@Override
							public void run() {							
								Toolkit.setBed(entry.getValue().getBlock(), Toolkit.getBlockFaceFromYaw(entry.getValue().getYaw()), Material.RED_BED);
							}
						});
						for (Player ply : players) {
							ply.teleport(getPlayerSpawn(ply));
						}
					}
					teamLifeCount.put(entry.getKey(),lifeCount-1);
				}
			}
		}
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		ply.getInventory().clear();
		setItems(ply);
		ply.setFireTicks(0);
		
		return getPlayerSpawn(ply);
	}
	/**
    * Handle when a player died
    */
	public void playerDeath(Player killed,Player killer) {		
		if (killer != null) {
			for (int i = 0; i < 10; i++) {
				killer.playEffect(killed.getLocation(), Effect.SMOKE, 1);
			}
			killer.playSound(killer.getLocation(), Sound.ITEM_TRIDENT_HIT, 10, 1);
			
			killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName()));
			addStat(killer,"Kills",10,SummaryStatType.XP);
			messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.GREEN + killer.getName() + ChatColor.YELLOW + "!");
		} else {
			messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " has died!");
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
					if (players.contains(ply)) {						
						teamSpots.put(entry.getKey(), ply);
						pd.addPlayerToTeam("team" + (team+1), ply);
						if (team == 0) {
							pd.setTeamColor("team1",ChatColor.GREEN);
							pd.setTeamColor("team2",ChatColor.RED);
						} else {
							pd.setTeamColor("team1",ChatColor.RED);
							pd.setTeamColor("team2",ChatColor.GREEN);
						}
					}
					break teamloop;
				}
			}
		}
		int teamIndex = (players.contains(ply) ? getPlayerTeam(ply) : 2);
		
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
					} else if (teamIndex == 1) {
						pd.addPlayerToTeam("team1", player);
					}
				}
				
				//Update current players teams
				pd2.addPlayerToTeam("team" + (teamIndex+1), ply);
			}
		}
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to " + realm.toString(),ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map.getName());
		
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
		
		//Remove player from other players teams
		int teamIndex = getPlayerTeam(ply);
		for (Player player : players) {
			playerHandling.getPlayerData(player).removePlayerFromTeam("team" + (teamIndex+1), ply);
		}
	}
	
	//
	// GETTERS
	//
	
	/**
	* Gets the specified players bed location
	* @param ply - Player to get bed for
	* @return location of the bed
	*/
	public Location getPlayerBed(Player ply) {
		return teamBeds.get(getPlayerTeam(ply));
	}
	
	//
	// SETTERS
	//
	
	/**
    * Set the players permissions
    * @param player - The player to set the permissions for
    */
	public void setDefaultPermissions(Player player) {
		PermissionAttachment permissions = mainInstance.getPlayerHandlingInstance().getPlayerData(player).getPermissionAttachment();
		
		permissions.setPermission("nyeblock.canBreakBlocks", false);
		permissions.setPermission("nyeblock.canPlaceBlocks", false);
		permissions.setPermission("nyeblock.canUseInventory", false);
		permissions.setPermission("nyeblock.shouldDropItemsOnDeath", false);
		permissions.setPermission("nyeblock.canDamage", false);
		permissions.setPermission("nyeblock.canBeDamaged", true);
		permissions.setPermission("nyeblock.canTakeFallDamage", false);
		permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
		permissions.setPermission("nyeblock.canDropItems", false);
		permissions.setPermission("nyeblock.canLoseHunger", false);
		permissions.setPermission("nyeblock.canSwapItems", false);
		permissions.setPermission("nyeblock.canMove", true);
	}
	/**
    * Set the players items
    * @param player - The player to set the items for
    */
	public void setItems(Player player) {
		if (!isGameActive()) {
			if (active) {				
				ItemStack stick = new ItemStack(Material.STICK);
				stick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
				
				player.getInventory().setItem(0,stick);
				player.getInventory().setItem(1,new ItemStack(Material.WHITE_WOOL,32));
				
				player.getInventory().setHeldItemSlot(0);			
			} else {				
				//Return to hub
				ReturnToLobby returnToLobby = new ReturnToLobby(mainInstance,lobbyRealm,player);
				player.getInventory().setItem(8, returnToLobby.give());
			}
		}
	}
}
