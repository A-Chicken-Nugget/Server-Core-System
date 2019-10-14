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

@SuppressWarnings("deprecation")
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
	
//	private ArrayList<Entity> test = new ArrayList<>();
	
	public StepSpleef(Main mainInstance, String worldName, int duration, int maxPlayers) {
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.worldName = worldName;
		realm = Realm.STEPSPLEEF;
		this.duration = duration;
		this.maxPlayers = maxPlayers;
		
		//Scoreboard stuff
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = board.registerNewObjective("scoreboard", "");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "NYEBLOCK (ALPHA)");
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("score_" + worldName, .5, 0, "setScoreboard", this, null);
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("main_" + worldName, 1, 0, "mainFunctions", this, null);
		//Block deletion timer
		mainInstance.getTimerInstance().createTimer("blocks_" + worldName, .1, 0, "manageBlocks", this, null);
		//Snow ball timer
		mainInstance.getTimerInstance().createTimer("snowballs_" + worldName, 1, 0, "giveSnowballs", this, null);
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
//				BoundingBox boundingBox = ply.getBoundingBox();
//				
//				Location test = new Location(Bukkit.getWorld(worldName),boundingBox.getMinX(),boundingBox.getMinY(),boundingBox.getMinZ()).subtract(0, 1, 0);
//				Location test2 = new Location(Bukkit.getWorld(worldName),boundingBox.getMaxX(),boundingBox.getMinY(),boundingBox.getMaxZ()).subtract(0, 1, 0);
//				
//				if (test.getBlock().getType() != Material.AIR) {
//					blocksToDelete.put(test.getBlock(), (System.currentTimeMillis() / 1000L));
//				}
//				if (test2.getBlock().getType() != Material.AIR) {
//					blocksToDelete.put(test2.getBlock(), (System.currentTimeMillis() / 1000L));
//				}
				
//				test.get(0).teleport(new Location(Bukkit.getWorld(worldName),boundingBox.getMinX(),boundingBox.getMinY(),boundingBox.getMinZ()));
//				test.get(1).teleport(new Location(Bukkit.getWorld(worldName),boundingBox.getMaxX(),boundingBox.getMinY(),boundingBox.getMaxZ()));
				
//				for (Block block : Miscellaneous.getBlocksBelow(ply)) {
//					blocksToDelete.put(block, (System.currentTimeMillis() / 1000L));
//				}
				
				Location loc = ply.getLocation().subtract(0,.5,0);
				Vector locs[] = {loc.toVector().add(new Vector(1, 0, 0)),loc.toVector().subtract(new Vector(1, 0, 0)),loc.toVector().add(new Vector(0, 0, 1)),loc.toVector().subtract(new Vector(0, 0, 1))};
				Location closestLoc = null;
				
				for (int i = 0; i < 4; i++) {
					Location lc = locs[i].toLocation(Bukkit.getWorld(worldName));
//					test.get(i).teleport(lc);
					
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
				
				blocksToDelete.put(loc.toVector(), (System.currentTimeMillis() / 1000L));
				if (closestLoc != null) {		
					blocksToDelete.put(closestLoc.toVector(), (System.currentTimeMillis() / 1000L));
				}
			}
			//Delete blocks that are over .5 seconds old
			Iterator<Map.Entry<Vector, Long>> itr = blocksToDelete.entrySet().iterator();
			while(itr.hasNext())
			{
				Map.Entry<Vector, Long> entry = itr.next();
				if ((System.currentTimeMillis() / 1000L) - entry.getValue() >= 1) {
					Vector vec = entry.getKey();
					Block block = Bukkit.getWorld(worldName).getBlockAt(new Location(Bukkit.getWorld(worldName),vec.getX(),vec.getY(),vec.getZ()));
//					Block block = entry.getKey();
					
					if (block.getType() != Material.AIR) {
						block.setType(Material.AIR);
					}
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
				if (players.size() > 1) {
					if (readyCount == 0) {
						messageToAll(ChatColor.YELLOW + "The game will begin shortly!");
						soundToAll(Sound.BLOCK_NOTE_BLOCK_PLING,1);
					} else {
						if (readyCount >= 4) {
							active = true;
							countdownStart = System.currentTimeMillis() / 1000L;
							
							for(Player ply : players) {
								Vector spawn = getRandomSpawnPoint();
								ply.setVelocity(new Vector(0,0,0));
								ply.teleport(new Location(Bukkit.getWorld(ply.getWorld().getName()),spawn.getX(),spawn.getY(),spawn.getZ()));
							}
							mainInstance.getTimerInstance().createTimer("countdown_" + worldName, 1, 7, "countDown", this, null);
							mainInstance.getTimerInstance().deleteTimer("snowballs_" + worldName);
							clearSnowballs();
						}
					}
					readyCount++;
				} else {
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
					mainInstance.getTimerInstance().createTimer("kick_" + worldName, 8, 1, "kickEveryone", this, null);
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
			
			if (!ply.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "STEP SPLEEF")) {						
				pd.setObjectiveName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "STEP SPLEEF");
			}
			
			scores.put(pos++, ChatColor.GREEN + "http://nyeblock.com/");
			scores.put(pos++, ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Players Left: " + ChatColor.GREEN + playersInGame.size());
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Miscellaneous.formatSeconds(timeLeft)) : Miscellaneous.formatSeconds(duration)));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
			
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
				mainInstance.getTimerInstance().createTimer("kick_" + worldName, 8, 1, "kickEveryone", this, null);
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
		
		//Set players scoreboard
		playerHandling.getPlayerData(ply).setScoreboard(board,objective);
		
		//Add player to players array
		players.add(ply);
		
		//Teleport player to random spawn
		Vector randSpawn = getRandomSpawnPoint();
		ply.teleport(new Location(Bukkit.getWorld(worldName),randSpawn.getX(),randSpawn.getY(),randSpawn.getZ()));
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to Step Spleef",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map);
		
//		test.add(Bukkit.getWorld(worldName).spawnEntity(Bukkit.getWorld(worldName).getSpawnLocation(), EntityType.CHICKEN));
//		test.add(Bukkit.getWorld(worldName).spawnEntity(Bukkit.getWorld(worldName).getSpawnLocation(), EntityType.CHICKEN));
//		test.add(Bukkit.getWorld(worldName).spawnEntity(Bukkit.getWorld(worldName).getSpawnLocation(), EntityType.CHICKEN));
//		test.add(Bukkit.getWorld(worldName).spawnEntity(Bukkit.getWorld(worldName).getSpawnLocation(), EntityType.CHICKEN));
		
//		for (Entity ent : test) {
//			ent.setInvulnerable(true);
//		}
	}
	/**
    * Handle when a player leaves the game
    */
	@SuppressWarnings("serial")
	public void playerLeave(Player ply, boolean showLeaveMessage, boolean moveToHub) {
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
