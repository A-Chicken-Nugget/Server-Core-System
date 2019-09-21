package nyeblock.Core.ServerCoreTest.Games;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

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
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.GhostFactory;

public class StepSpleef {
	//Instances needed to run the game
	private Main mainInstance;
	private PlayerHandling playerHandling;
	//Game info
	private String type;
	private String worldName;
	private int duration;
	private long startTime;
	private String map;
	private int maxPlayers;
	private boolean active = false;
	private boolean gameBegun = false;
	//Player data
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<Player> playersInGame = new ArrayList<>();
	//Game points
	private ArrayList<Vector> spawns = new ArrayList<>();
	//Block history
	private HashMap<Vector,Long> blocksToDelete = new HashMap<>();
	//Etc
	private long countdownStart;
	private int emptyCount = 0;
	private int readyCount = 0;
	private boolean endStarted = false;
	private long lastNumber = 0;
	private GhostFactory ghostFactory;
	DecimalFormat df = new DecimalFormat("#.##");
	
//	private ArrayList<Entity> test = new ArrayList<>();
	
	public StepSpleef(Main mainInstance, String type, String worldName, int duration, int maxPlayers) {
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.type = type;
		this.worldName = worldName;
		this.duration = duration;
		this.maxPlayers = maxPlayers;
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("score_" + worldName, .5, 0, "setScoreboard", this, null);
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("main_" + worldName, 1, 0, "mainFunctions", this, null);
		//Block deletion timer
		mainInstance.getTimerInstance().createTimer("blocks_" + worldName, .1, 0, "manageBlocks", this, null);
		//Snow ball timer
		mainInstance.getTimerInstance().createTimer("snowballs_" + worldName, 1, 0, "giveSnowballs", this, null);
		
		ghostFactory = new GhostFactory(mainInstance);
	}
	
	//Check/give players snowballs
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
	//Delete snowballs
	public void clearSnowballs() {
		for (Player ply : players) {
			ply.getInventory().clear(0);
		}
	}
	//Kick everyone in the game
	public void kickEveryone() {
		ghostFactory.clearGhosts();
		ghostFactory.close();
		mainInstance.getTimerInstance().deleteTimer("blocks_" + worldName);
		
		ArrayList<Player> tempPlayers = new ArrayList<>(players);
		
		for (Player ply : tempPlayers) {			
			playerLeave(ply,false,true);
		}
	}
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
	//Main code
	public void mainFunctions() {
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
			if (players.size() > 0 && !active) {
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
				mainInstance.getGameInstance().removeGame(type,worldName);
			}
		}
	}
	//Scoreboard code
	public void setScoreboard() {
		//Check if player has won
		if (playersInGame.size() == 3) {
			for (Player ply : playersInGame) {				
				if (!endStarted) {
//					gameBegun = false;
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
	//Print a title to all players in the game
	@SuppressWarnings("deprecation")
	public void titleToAll(String top, String bottom) {
		for(Player ply : players) {
			ply.sendTitle(top, bottom);
		}
	}
	//Play sound to all players in the game
	public void soundToAll(Sound sound, float pitch) {
		for(Player ply : players) {
			ply.playSound(ply.getLocation(), sound, 10, pitch);
		}
	}
	//Print a message in chat to all players in the game
	public void messageToAll(String message) {
		for(Player ply : players) {
			ply.sendMessage(message);
		}
	}
	//Check if a player is in this game
	public boolean isInServer(Player ply) {
		boolean found = false;
		
		for(Player player : players) {
			if (ply.getName().equalsIgnoreCase(player.getName())) {
				found = true;
			}
		}
		return found;
	}
	//Check if a player is in this game
	public boolean isInRound(Player ply) {
		boolean found = false;
		
		for(Player player : playersInGame) {
			if (ply.getName().equalsIgnoreCase(player.getName())) {
				found = true;
			}
		}
		return found;
	}
	//Get the status of the game
	public boolean isGameActive() {
		return !endStarted;
	}
	//Get the current player count
	public boolean getRoundActive() {
		return gameBegun;
	}
	//Get the current player count
	public int getPlayerCount() {
		return players.size();
	}
	//Get the max amount of players
	public int getMaxPlayers() {
		return maxPlayers;
	}
	//Get the world name
	public String getWorldName() {
		return worldName;
	}
	//Get a random spawn point
	public Vector getRandomSpawnPoint() {
		Vector vector = Bukkit.getWorld(worldName).getSpawnLocation().getDirection();
		
		if (spawns.size() > 0) {
			Random r = new Random();
			vector = spawns.get(r.nextInt(spawns.size()));
		}
		return vector;
	}
	//Set the game map
	public void setMap(String map) {
		this.map = map;
	}
	//Set the game spawn points
	public void setSpawnPoints(ArrayList<Vector> spawns) {
		this.spawns = spawns;
	}
	//When a player has died
	public void playerDeath(Player killed) {
		playersInGame.removeAll(new ArrayList<Player>() {{
			add(killed);
		}});
		ghostFactory.addGhost(killed);
		Vector randSpawn = getRandomSpawnPoint();
		killed.teleport(new Location(Bukkit.getWorld(worldName),randSpawn.getX(),randSpawn.getY(),randSpawn.getZ()));
		messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " has fallen into the void!");
	}
	//When a player joins the game
	@SuppressWarnings("deprecation")
	public void playerJoin(Player ply) {
		if (!active) {			
			messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has joined the game!");
		}
		//Add player to players array
		players.add(ply);
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
	//When a player leaves the game
	public void playerLeave(Player ply, boolean showLeaveMessage, boolean moveToHub) {
		//Remove player from players list
		ArrayList<Player> playersToRemove = new ArrayList<Player>();
		for(Player player : players) {
			if (player.getName().equalsIgnoreCase(ply.getName())) {
				playersToRemove.add(ply);
			}
		}
		players.removeAll(playersToRemove);
		
		if (showLeaveMessage) {
			if (!active) {				
				messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has left the game!");
			}
		}
		if (moveToHub) {
			PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			
			//Set player realms/items/permissions
			playerData.setRealm("hub",true,true);
			//Move player to hub
			mainInstance.getGameInstance().joinGame(ply, "hub");
		}
	}
}
