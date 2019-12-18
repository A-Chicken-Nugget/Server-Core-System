package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings({"deprecation","serial"})
public class StepSpleef extends GameBase {
	//Game info
	private int duration;
	private long startTime;
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
	private boolean endStarted = false;
	private long lastNumber = 0;
	private int playTimeCount = 0;
	
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
		mainInstance.getTimerInstance().deleteTimer(worldName + "_fireworks");
		ArrayList<Player> tempPlayers = new ArrayList<>(players);
		
		for (Player ply : tempPlayers) {			
			//Unhide all players who might be hidden for certain players
			for (Player player : tempPlayers) {
				if (!ply.canSee(player)) {					
					player.showPlayer(mainInstance,ply);
				}
			}
			
			leave(ply,false,nyeblock.Core.ServerCoreTest.Misc.Enums.Realm.HUB);
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
						Location loc = ply.getLocation().subtract(0,.1,0);
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
	/**
    * Run main checks for the game
    */
	public void mainFunctions() {
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
							}
							mainInstance.getTimerInstance().createTimer("countdown_" + worldName, 1, 7, "countDown", false, null, this);
							mainInstance.getTimerInstance().deleteTimer("snowballs_" + worldName);
							clearSnowballs();
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
				mainInstance.getTimerInstance().deleteTimer("blocks_" + worldName);
				
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
		
		//Check if player has won
		if (playersInGame.size() == 1 && !endStarted) {
			endStarted = true;
			canUsersJoin = false;
			Player ply = playersInGame.get(0);
			
			messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + ply.getName() + " has won!");
			soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
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
					
					Firework firework = ply.getWorld().spawn(ply.getLocation(), Firework.class);
					FireworkMeta fireworkMeta = firework.getFireworkMeta();
					fireworkMeta.addEffect(effect);
					fireworkMeta.setPower(2);
					firework.setFireworkMeta(fireworkMeta);
				}
			});
			giveXP(ply,"Winning",150);
			
			//Print players xp summary
			for (Player ply2 : players) {
				printSummary(ply2,true);
			}
			mainInstance.getTimerInstance().createTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
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
			scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Toolkit.formatMMSS(timeLeft)) : Toolkit.formatMMSS(duration)));
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
				soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
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
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		pd.setItems();
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
				
				pd.setSpectatingStatus(true);
				playersSpectating.add(killed);
				killed.setAllowFlight(true);
				
				killed.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You are now spectating. You are invisible and can fly around."));
				
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
		
		//Add player to proper team
		pd.addPlayerToTeam(pd.getUserGroup().toString(), ply);
		
		//Add players to teams
		for (Player player : players) {
			PlayerData pd2 = playerHandling.getPlayerData(player);
			
			if (player != ply) {
				//Update joining player team
				pd.addPlayerToTeam(pd2.getUserGroup().toString(), player);
				
				//Update current players teams
				pd2.addPlayerToTeam(pd.getUserGroup().toString(), ply);
			}
		}
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to Step Spleef",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map);
	}
	/**
    * Handle when a player leaves the game
    */
	public void playerLeave(Player ply, boolean showLeaveMessage) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Clear scoreboard
		pd.clearScoreboard();
		
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
	}
}
