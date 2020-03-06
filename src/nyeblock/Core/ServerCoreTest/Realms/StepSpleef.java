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
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Items.PlayerSelector;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Items.ReturnToLobby;
import nyeblock.Core.ServerCoreTest.Maps.MapPoint;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.SummaryStatType;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings({"deprecation","serial"})
public class StepSpleef extends GameBase {
	//Game info
	private boolean active = false;
	//Player data
	private ArrayList<Player> playersInGame = new ArrayList<>();
	private ArrayList<Player> playersSpectating = new ArrayList<>();
	//Block history
	private HashMap<Vector,Long> blocksToDelete = new HashMap<>();
	//Etc
	private long countdownStart;
	private int readyCount = 0;
	private int messageCount = 0;
	private long lastNumber = 0;
	
	/**
    * Custom game constructor
    */
	public StepSpleef(Main mainInstance, int id, String worldName, int duration, int minPlayers, int maxPlayers) {
		super(mainInstance,Realm.STEPSPLEEF,worldName,Realm.STEPSPLEEF_LOBBY);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.id = id;
		this.worldName = worldName;
		this.duration = duration;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		
		scoreboard = new Runnable() {
			@Override
			public void run() {
				for(Player ply : players)
				{       				
					int pos = 1;
					int timeLeft = (int)(duration-((System.currentTimeMillis() / 1000L)-startTime));
					PlayerData pd = playerHandling.getPlayerData(ply);
					HashMap<Integer,String> scores = new HashMap<Integer,String>();
					
					scores.put(pos++, ChatColor.GREEN + "http://nyeblock.com/");
					scores.put(pos++, ChatColor.RESET.toString());
					scores.put(pos++, ChatColor.YELLOW + "Players Left: " + ChatColor.GREEN + playersInGame.size());
					scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Toolkit.formatMMSS(timeLeft)) : Toolkit.formatMMSS(duration)));
					scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					if (shouldRainbowTitleText) {
						pd.setScoreboardTitle(chatColorList.get(new Random().nextInt(chatColorList.size())) + ChatColor.BOLD.toString() + "STEP SPLEEF");				
					} else {				
						pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "STEP SPLEEF");
					}
					pd.updateObjectiveScores(scores);
				}
			}
		};
	}

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
			
			mainInstance.getRealmHandlingInstance().joinLobby(ply, lobbyRealm);
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
    * Add blocks to be deleted that the player stepped on
    */
	public void manageBlocks() {
		if (gameBegun && !endStarted) {
			for (Player ply : playersInGame) {
				Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
					@Override
					public void run() {
						Location loc = ply.getLocation().subtract(0,.5,0);
						ArrayList<Block> locs = Toolkit.getBlocksBelowPlayer(ply);
						Location closestLoc = null;
						
						for (int i = 0; i < locs.size(); i++) {
							Location lc = locs.get(i).getLocation();
							
							if (closestLoc != null) {
								if (loc.distance(closestLoc) > loc.distance(lc) && lc.getBlock().getType() != Material.AIR) {
									closestLoc = lc;
								}
							} else {
								if (lc.getBlock().getType() != Material.AIR) {							
									closestLoc = lc;
								}
							}
						}
						
						if (closestLoc != null) {		
							if (!blocksToDelete.containsKey(closestLoc.toVector())) {
								blocksToDelete.put(closestLoc.toVector(), (System.currentTimeMillis()));
							}
						}
					}
				});
			}
		}
		Iterator<Map.Entry<Vector, Long>> itr = blocksToDelete.entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry<Vector, Long> entry = itr.next();
			if (System.currentTimeMillis() - entry.getValue() >= 300L) {
				entry.getKey().toLocation(world).getBlock().setType(Material.AIR);
				
				itr.remove();
			}
		}
	}
	/**
	* What needs to be ran when the world is created
	*/
	public void onCreate() {
		//Set points
		for (MapPoint point : map.getPoints()) {
			spawns.add(point.getLocation());
		}
		
		//Main functions timer
		mainInstance.getTimerInstance().createMethodTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
		//Block deletion timer
		mainInstance.getTimerInstance().createMethodTimer("blocks_" + worldName, .1, 0, "manageBlocks", false, null, this);
		//Snow ball timer
		mainInstance.getTimerInstance().createRunnableTimer("snowballs_" + worldName, 1, 0, new Runnable() {
			@Override
			public void run() {
				for (Player ply : players) {
					PlayerInventory inv = ply.getInventory();
					
					if (inv.getItem(0) != null) {
						if (inv.getItem(0).getAmount() < 16) {
							inv.setItem(0, new ItemStack(Material.SNOWBALL,16));
						}				
					} else { 
						inv.setItem(0, new ItemStack(Material.SNOWBALL,16));
					}
				}
			}
		});
	}
	/**
	* What needs to be ran when the world is deleted
	*/
	public void onDelete() {
		mainInstance.getTimerInstance().deleteTimer("main_" + worldName);
		mainInstance.getTimerInstance().deleteTimer("blocks_" + worldName);
		mainInstance.getTimerInstance().deleteTimer("snowballs_" + worldName);
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
						
						for (Player ply : players) {
							PlayerData pd = playerHandling.getPlayerData(ply);
							
							if (ply.hasPermission("nyeblock.canDamage")) {
								pd.setPermission("nyeblock.canDamage", false);
							}
						}
					} else {
						if (readyCount >= 10) {
							active = true;
							countdownStart = System.currentTimeMillis() / 1000L;
							
							for(Player ply : players) {
								Location spawn = getRandomSpawnPoint();
								ply.setVelocity(new Vector(0,0,0));
								ply.setHealth(20);
								ply.teleport(spawn);
								playerHandling.getPlayerData(ply).addGamePlayed(realm, false);
							}
							mainInstance.getTimerInstance().createMethodTimer("countdown_" + worldName, 1, 7, "countDown", false, null, this);
							mainInstance.getTimerInstance().deleteTimer("snowballs_" + worldName);
							
							//Remove snowballs
							for (Player ply : players) {
								ply.getInventory().clear(0);
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
		//Check if player has won
		if (playersInGame.size() == 1 && !endStarted) {
			endStarted = true;
			canUsersJoin = false;
			Player ply = playersInGame.get(0);
			
			messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + ply.getName() + " has won!");
			soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
			playWinAction(ply);
			addStat(ply,"Winning",150,SummaryStatType.XP);
			playerHandling.getPlayerData(ply).addGamePlayed(realm, true);
			
			//Print players xp summary
			for (Player ply2 : players) {
				printSummary(ply2,true);
			}
			mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
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
		//Check when the game has ended and determine winner
		if (gameBegun && (duration-((System.currentTimeMillis() / 1000L)-startTime)) < 0) {
			if (!endStarted) {
				endStarted = true;
				gameBegun = false;
				
				messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Nobody wins!");
				soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
				//Wait 8 seconds, then kick everyone
				mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
			}
		}
	}
	/**
    * Set the players permissions
    */
	public void setDefaultPermissions(Player player) {
		PermissionAttachment permissions = mainInstance.getPlayerHandlingInstance().getPlayerData(player).getPermissionAttachment();
		
		permissions.setPermission("nyeblock.canBreakBlocks", false);
		permissions.setPermission("nyeblock.canBreakBlocks", false);
		permissions.setPermission("nyeblock.canUseInventory", false);
		permissions.setPermission("nyeblock.shouldDropItemsOnDeath", false);
		permissions.setPermission("nyeblock.canDamage", true);
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
    */
	public void setItems(Player player) {
		if (isGameActive()) {
			//Select player
			PlayerSelector selectPlayer = new PlayerSelector(mainInstance,player);
			player.getInventory().setItem(4, selectPlayer.give());
		}
		
		//Return to lobby
		ReturnToLobby returnToLobby = new ReturnToLobby(mainInstance,lobbyRealm,player);
		player.getInventory().setItem(8, returnToLobby.give());
	}
	/**
    * Check if a player is in the game
    */
	public boolean isInRound(Player ply) {
		boolean found = false;
		
		for(Player player : playersInGame) {
			if (ply.getName().equalsIgnoreCase(player.getName())) {
				found = true;
			}
		}
		return found;
	}
	/**
    * Get the end status of the game
    */
	public boolean isGameOver() {
		return endStarted;
	}
	/**
    * Get players in the current game
    */
	public ArrayList<Player> getPlayersInRealm() {
		return playersInGame;
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		ply.getInventory().clear();
		setItems(ply);
		
		return getRandomSpawnPoint();
	}
	/**
    * Handle when a player died
    */
	public void playerDeath(Player killed, Player killer) {
		if (gameBegun) {			
			boolean isSpectating = playersSpectating.contains(killed);
			
			//Remove player from the current game
			playersInGame.removeAll(new ArrayList<Player>() {{
				add(killed);
			}});
			
			//Setup player to spectate
			if (!isSpectating) {	
				PlayerData pd = playerHandling.getPlayerData(killed);
				
				killed.setGameMode(GameMode.ADVENTURE);
				pd.setSpectatingStatus(true);
				playersSpectating.add(killed);
				killed.setAllowFlight(true);
				
				killed.sendMessage(ChatColor.YELLOW + "You are now spectating. You are invisible and can fly around.");
				
				//Unhide spectators
				for (Player ply : playersSpectating) {
					killed.showPlayer(mainInstance,ply);
				}
				
				//Hide player from players in the active game
				for (Player ply : playersInGame) {
					ply.hidePlayer(mainInstance,killed);
				}
			}
			
			if (!isSpectating) {			
				messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " has fallen into the void!");
			}
		}
		
		//Find a random spawn
		Location randSpawn = getRandomSpawnPoint();
		killed.teleport(randSpawn);
	}
	/**
    * Handle when a player joins the game
    */
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Teleport player to random spawn
		Location randSpawn = getRandomSpawnPoint();
		ply.teleport(randSpawn);
		
		//Setup team
		pd.setScoreBoardTeams(null,Team.OptionStatus.NEVER);
		
		//Add players to proper scoreboard teams
		updateTeamsFromUserGroups();
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to Step Spleef",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map.getName());
	}
	/**
    * Handle when a player leaves the game
    */
	public void playerLeave(Player ply) {
		//Remove player from the current game
		playersInGame.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from spectating list
		playersSpectating.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
	}
}
