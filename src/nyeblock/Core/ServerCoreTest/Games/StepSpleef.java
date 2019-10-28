package nyeblock.Core.ServerCoreTest.Games;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Miscellaneous;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings({"deprecation","serial"})
public class StepSpleef extends GameBase {
	//Game info
	private int duration;
	private long startTime;
	private boolean active = false;
	private boolean gameBegun = false;
	//Player data
	private ArrayList<Player> playersInGame = new ArrayList<>();
	private ArrayList<Player> playersSpectating = new ArrayList<>();
	//Block history
	private HashMap<Vector,Long> blocksToDelete = new HashMap<>();
	//Etc
	private long countdownStart;
	private int readyCount = 0;
	private int messageCount = 0;
	private boolean endStarted = false;
	private long lastNumber = 0;
	
	public StepSpleef(Main mainInstance, String worldName, int duration, int minPlayers, int maxPlayers) {
		super(mainInstance,worldName);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.worldName = worldName;
		realm = Realm.STEPSPLEEF;
		this.duration = duration;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("score_" + worldName, .5, 0, "setScoreboard", false, null, this);
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
		//Block deletion timer
		mainInstance.getTimerInstance().createTimer("blocks_" + worldName, .1, 0, "manageBlocks", false, null, this);
		//Snow ball timer
		mainInstance.getTimerInstance().createTimer("snowballs_" + worldName, 1, 0, "giveSnowballs", false, null, this);
	}
	
	/**
    * Checks/gives players snowballs
    */
	public void giveSnowballs() { 
		for (Player ply : players) {
			if (ply.getInventory().getItem(0) != null) {
				if (ply.getInventory().getItem(0).getAmount() < 16) {
					ply.getInventory().setItem(0, new ItemStack(Material.SNOWBALL,16));
				}				
			} else { 
				ply.getInventory().setItem(0, new ItemStack(Material.SNOWBALL,16));
			}
		}
	}
	/**
    * Remove snowballs from players inventories
    */
	public void clearSnowballs() {
		for (Player ply : players) {
			ply.getInventory().clear(0);
		}
	}
	/**
    * Kick everyone in the game
    */
	public void kickEveryone() {
		ArrayList<Player> tempPlayers = new ArrayList<>(players);
		
		for (Player ply : tempPlayers) {			
			//Unhide all players who might be hidden for certain players
			for (Player player : tempPlayers) {					
				player.showPlayer(ply);
			}
			
			playerLeave(ply,false,true);
		}
	}
	/**
    * Manages count down timer
    */
	public void countDown() {
		long timeLeft = (6-((System.currentTimeMillis() / 1000L)-countdownStart));
		
		if (timeLeft <= 0) {
			gameBegun = true;
			startTime = System.currentTimeMillis() / 1000L;
			for(Player ply : players) {
				playersInGame.add(ply);
			}
			titleToAll(ChatColor.YELLOW + "Go!"," ");
			soundToAll(Sound.BLOCK_NOTE_BLOCK_BELL,1f);
		} else {      
			if (timeLeft != lastNumber) {   
				lastNumber = timeLeft;
				titleToAll(ChatColor.YELLOW + "Game starts in...",ChatColor.GREEN.toString() + timeLeft);
				soundToAll(Sound.BLOCK_NOTE_BLOCK_PLING,1);
			}
		}
	}
	/**
    * Add blocks to be deleted that the player stepped on
    */
	public void manageBlocks() {
		if (gameBegun) {
			//Add blocks to be deleted
			for (Player ply : playersInGame) {
				for (double startSub = .10; startSub < 1.1; startSub += .1) {
					Location loc = ply.getLocation().subtract(0,.1,0);
					ArrayList<Block> locs = Miscellaneous.getBlocksBelowPlayer(ply);
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
						break;
					}
				}
			}
			//Delete blocks that are over .5 seconds old
			Iterator<Map.Entry<Vector, Long>> itr = blocksToDelete.entrySet().iterator();
			while(itr.hasNext())
			{
				Map.Entry<Vector, Long> entry = itr.next();
				if (System.currentTimeMillis() - entry.getValue() >= 500L) {
					Vector vec = entry.getKey();
					
					Bukkit.getWorld(worldName).getBlockAt(new Location(Bukkit.getWorld(worldName),vec.getX(),vec.getY(),vec.getZ())).setType(Material.AIR);
					itr.remove();
				}
			}
		}
	}
	/**
    * Run main checks for the game
    */
	public void mainFunctions() {
		//Set compass targets
		for (Player ply : playersSpectating) {
			PlayerData pd = playerHandling.getPlayerData(ply);
			String key = pd.getCustomDataKey("player_selector_index");
			int currentIndex = Integer.parseInt((key == null ? "0" : key));
			
			if (playersInGame.size() > currentIndex) {
				ply.setCompassTarget(playersInGame.get(currentIndex).getLocation());
			} else {
				if (playersInGame.size() > 0) {
					ply.setCompassTarget(playersInGame.get(0).getLocation());
				}
			}
		}
		//Set player gamemodes
		for(Player ply : players) {
			if (!active) {
				if (ply.getGameMode() != GameMode.SURVIVAL) {
					ply.setGameMode(GameMode.SURVIVAL);
				}
			} else {
				if (gameBegun && ply.getGameMode() == GameMode.SURVIVAL) {
					boolean isPlaying = false;
					boolean isFlying = true;
    				
    				for(Player player : playersInGame) {
    					if (ply.getName().equalsIgnoreCase(player.getName())) {
    						isPlaying = true;
    						if (!ply.isFlying()) {
    							isFlying = false;
    						}
    					}
    				}
    				if (!isPlaying) {
    					ply.setGameMode(GameMode.ADVENTURE);
    					if (!isFlying) {
    						ply.setFlying(true);
    					}
    				}
				}
			}
		}
		//Check if the server is empty
		if (players.size() > 0) {        			
			if (emptyCount != 0) {
				emptyCount = 0;
			}
			if (!active) {
				if (players.size() >= minPlayers) {
					if (readyCount == 0) {
						messageToAll(ChatColor.YELLOW + "The game will begin shortly!");
						soundToAll(Sound.BLOCK_NOTE_BLOCK_PLING,1);
						
						for (Player ply : players) {
							PlayerData pd = playerHandling.getPlayerData(ply);
							
							if (pd.getPermission("nyeblock.canDamage")) {
								pd.setPermission("nyeblock.canDamage", false);
							}
						}
					} else {
						if (readyCount >= 10) {
							active = true;
							countdownStart = System.currentTimeMillis() / 1000L;
							
							for(Player ply : players) {
								Vector spawn = getRandomSpawnPoint();
								ply.setVelocity(new Vector(0,0,0));
								ply.teleport(new Location(Bukkit.getWorld(ply.getWorld().getName()),spawn.getX(),spawn.getY(),spawn.getZ()));
							}
							mainInstance.getTimerInstance().createTimer("countdown_" + worldName, 1, 7, "countDown", false, null, this);
							mainInstance.getTimerInstance().deleteTimer("snowballs_" + worldName);
							clearSnowballs();
						}
					}
					readyCount++;
				} else {
					if (readyCount != 0) {
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
				mainInstance.getTimerInstance().deleteTimer("score_" + worldName);
				mainInstance.getTimerInstance().deleteTimer("main_" + worldName);
				mainInstance.getTimerInstance().deleteTimer("blocks_" + worldName);
				
				//Delete world from server
				mainInstance.getMultiverseInstance().deleteWorld(worldName);
				//Remove game from games array
				mainInstance.getGameInstance().removeGame(realm,worldName);
			}
		}
	}
	/**
    * Set the players scoreboard
    */
	public void setScoreboard() {
		//Check if player has won
		if (playersInGame.size() == 1) {
			for (Player ply : playersInGame) {				
				if (!endStarted) {
					endStarted = true;
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + ply.getName() + " has won!");
					mainInstance.getTimerInstance().createTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
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
			scores.put(pos++, ChatColor.YELLOW + "Players Left: " + ChatColor.GREEN + playersInGame.size());
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Miscellaneous.formatMMSS(timeLeft)) : Miscellaneous.formatMMSS(duration)));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
			pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "STEP SPLEEF");
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
				
				messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Nobody wins!");
				//Wait 8 seconds, then kick everyone
				mainInstance.getTimerInstance().createTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
			}
		}
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
    * Get the status of the game
    */
	public boolean isGameActive() {
		return gameBegun;
	}
	/**
    * Get players in the current game
    */
	public ArrayList<Player> getPlayersInGame() {
		return playersInGame;
	}
	/**
    * Handle when a player died
    */
	@SuppressWarnings("serial")
	public void playerDeath(Player killed) {
		if (gameBegun) {			
			boolean isSpectating = playersSpectating.contains(killed);
			
			//Remove player from the current game
			playersInGame.removeAll(new ArrayList<Player>() {{
				add(killed);
			}});
			
			//Setup player to spectate
			if (!isSpectating) {	
				playersSpectating.add(killed);
				killed.sendMessage(ChatColor.YELLOW + "You are now spectating. You are invisible and can fly around.");
				
				//Unhide spectators
				for (Player ply : playersSpectating) {
					killed.showPlayer(ply);
				}
				
				//Hide player from players in the active game
				for (Player ply : playersInGame) {
					ply.hidePlayer(killed);
				}
			}
			
			if (!isSpectating) {			
				messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " has fallen into the void!");
			}
		}
		
		//Find a random spawn
		Vector randSpawn = getRandomSpawnPoint();
		killed.teleport(new Location(Bukkit.getWorld(worldName),randSpawn.getX(),randSpawn.getY(),randSpawn.getZ()));
	}
	/**
    * Handle when a player joins the game
    */
	public void playerJoin(Player ply) {
		if (!active) {			
			messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has joined the game!");
		}
		
		//Add player to players array
		players.add(ply);
		
		//Teleport player to random spawn
		Vector randSpawn = getRandomSpawnPoint();
		ply.teleport(new Location(Bukkit.getWorld(worldName),randSpawn.getX(),randSpawn.getY(),randSpawn.getZ()));
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to Step Spleef",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map);
	}
	/**
    * Handle when a player leaves the game
    */
	public void playerLeave(Player ply, boolean showLeaveMessage, boolean moveToHub) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Clear scoreboard
		pd.clearScoreboard();
		
		//Remove player from players list
		players.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from the current game
		playersInGame.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from spectating list
		playersSpectating.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		if (showLeaveMessage) {
			if (!active) {				
				messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has left the game!");
			}
		}
		if (moveToHub) {
			PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			
			//Set player realms/items/permissions
			playerData.setRealm(Realm.HUB,true,true);
			//Move player to hub
			mainInstance.getGameInstance().joinGame(ply, Realm.HUB);
		}
		
		//Unhide player from all players in the game
		for (Player player : players) {
			player.showPlayer(ply);
		}
	}
}
