package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.KitHandling;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.CustomChests.CustomChestGenerator;
import nyeblock.Core.ServerCoreTest.Items.PlayerSelector;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Items.ReturnToLobby;
import nyeblock.Core.ServerCoreTest.Maps.MapPoint;
import nyeblock.Core.ServerCoreTest.Menus.KitSelectorMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.SummaryStatType;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings({"deprecation","serial"})
public class SkyWars extends GameBase {
	private KitHandling kitHandling;
	//Player data
	private HashMap<String,Integer> playerKills = new HashMap<>();
	private HashMap<String,String> playerKits = new HashMap<>();
	private HashMap<Integer,String> playerSpots = new HashMap<>();
	private ArrayList<Player> playersSpectating = new ArrayList<>();
	private ArrayList<Player> playersInGame = new ArrayList<>();
	//Etc
	private long countdownStart;
	private int readyCount = 0;
	private int messageCount = 0;
	private long lastNumber = 0;
	private boolean setWorldTime = true;
	protected ArrayList<Hologram> holograms = new ArrayList<>();
	protected HashMap<Vector,ChestValue> chests = new HashMap<>();
	
	//
	// CONSTRUCTOR
	//
	
	/**
    * Custom game constructor
    */
	public SkyWars(Main mainInstance, int id, String worldName, int duration, int minPlayers, int maxPlayers) {
		super(mainInstance,Realm.SKYWARS,worldName,Realm.SKYWARS_LOBBY);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		kitHandling = mainInstance.getKitHandlingInstance();
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
					scores.put(pos++, ChatColor.YELLOW + "Kills: " + ChatColor.GREEN + playerKills.get(ply.getName()));
					scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(pos++, ChatColor.YELLOW + "Players Left: " + ChatColor.GREEN + playersInGame.size());
					scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Toolkit.formatMMSS(timeLeft)) : Toolkit.formatMMSS(duration)));
					scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					if (shouldRainbowTitleText) {
						pd.setScoreboardTitle(chatColorList.get(new Random().nextInt(chatColorList.size())) + ChatColor.BOLD.toString() + "SKY WARS");				
					} else {				
						pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "SKY WARS");
					}
					
					pd.updateObjectiveScores(scores);
				}
			}
		};
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
				
				//Remove glass underneath players
				ply.getLocation().subtract(0, 1, 0).getBlock().setType(Material.AIR);
				ply.getLocation().subtract(0, 2, 0).getBlock().setType(Material.AIR);
				
				//Set permissions
				PlayerData pd = playerHandling.getPlayerData(ply);
				pd.setPermission("nyeblock.canPlaceBlocks", true);
				pd.setPermission("nyeblock.canBreakBlocks", true);
				pd.setPermission("nyeblock.canUseInventory", true);
				pd.setPermission("nyeblock.canDropItems", true);
				pd.setPermission("nyeblock.canLoseHunger", true);
				
				//Give kit
				ply.getInventory().clear();
				kitHandling.setKit(ply, realm, playerKits.get(ply.getName()));
				
				if (ply.getOpenInventory() != null) {
					ply.getOpenInventory().close();
				}
				
				ply.setGameMode(GameMode.SURVIVAL);
				
				pd.addGamePlayed(realm, false);
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
	* What needs to be ran when the game is created
	*/
	public void onCreate() {
		GameBase instance = this;
		
		//Set points
		for (MapPoint point : map.getPoints()) {
			if (point.getType() == MapPointType.PLAYER_SPAWN) {				
				spawns.add(point.getLocation());
			} else {
				chests.put(point.getLocation().toVector(), point.getChestValue());
			}
		}
		
		//Spawn/fill chests
		new BukkitRunnable() {
	        public void run() {
	        	CustomChestGenerator.setChests(chests,mainInstance,instance);
			}
	    }.runTask(mainInstance);
	    
	    //Main functions timer
	    mainInstance.getTimerInstance().createMethodTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
	}
	/**
	* What needs to be ran when the world is deleted
	*/
	public void onDelete() {
		//Remove chests/holograms
    	if (chests.size() > 0) {
    		for(Map.Entry<Vector,ChestValue> entry : chests.entrySet()) {
    			Block block = entry.getKey().toLocation(world).getBlock();
    			
    			if (block.getType() == Material.CHEST) {	        				
    				Chest chest = (Chest)block.getState();
    				chest.getInventory().clear();
    				block.setType(Material.AIR);
    			}
    		}
    	}
    	if (holograms.size() > 0) {
    		for (Hologram hologram : holograms) {
    			hologram.delete();
    		}
    	}
		
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
			if (!gameBegun) {
				for (Player ply : players) {
					ply.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "Selected kit: " + ChatColor.GREEN + playerKits.get(ply.getName())));
				}
			}
		}
		//Check if player has won
		if (playersInGame.size() == 1 && active) {
			for (Player ply : playersInGame) {				
				if (!endStarted) {
					endStarted = true;
					canUsersJoin = false;
					
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + ply.getName() + " has won!");
					soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
					addStat(ply,"Placing #1",200,SummaryStatType.XP);
					playWinAction(ply);
					playerHandling.getPlayerData(ply).addGamePlayed(realm, true);
					for (Player player : players) {
						//Print the players xp summary
						printSummary(player,true);
					}
					mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 8, 1, "kickEveryone", false, null, this);
				}
			}
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
		permissions.setPermission("nyeblock.shouldDropItemsOnDeath", true);
		permissions.setPermission("nyeblock.canDamage", true);
		permissions.setPermission("nyeblock.canBeDamaged", true);
		permissions.setPermission("nyeblock.canTakeFallDamage", true);
		permissions.setPermission("nyeblock.tempNoDamageOnFall", true);
		permissions.setPermission("nyeblock.canDropItems", false);
		permissions.setPermission("nyeblock.canLoseHunger", false);
		permissions.setPermission("nyeblock.canSwapItems", false);
		permissions.setPermission("nyeblock.canMove", true);
	}
	/**
    * Set the players items
    */
	public void setItems(Player player) {
		if (gameBegun) {						
			//Select player
			PlayerSelector selectPlayer = new PlayerSelector(mainInstance,player);
			player.getInventory().setItem(4, selectPlayer.give());
			
			//Return to hub
			ReturnToHub returnToHub = new ReturnToHub(mainInstance,player);
			player.getInventory().setItem(8, returnToHub.give());
		} else {						
			//Select kit
			KitSelectorMenu selectKit = new KitSelectorMenu(mainInstance,player);
			player.getInventory().setItem(4, selectKit.give());
			
			//Return to lobby
			ReturnToLobby returnToLobby = new ReturnToLobby(mainInstance,lobbyRealm,player);
			player.getInventory().setItem(8, returnToLobby.give());
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
	public void addHologram(Hologram hologram) {
		holograms.add(hologram);
	}
	/**
    * Get a specific players kit
    * @param player - the player to get the kit for.
    */
	public String getPlayerKit(Player ply) {
		return playerKits.get(ply.getName());
	}
	/**
    * Get players in the current game
    */
	public ArrayList<Player> getPlayersInRealm() {
		return playersInGame;
	}
	/**
    * Set a specific players kit
    * @param player - the player to set the kit for.
    */
	public void setPlayerKit(Player ply, String kit) {
		playerKits.put(ply.getName(), kit);
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		ply.getInventory().clear();
		PlayerData pd = playerHandling.getPlayerData(ply);
		Location loc = null;
		
		setItems(ply);
		
		
		//Add player to ghost mode
		if (gameBegun && !playersSpectating.contains(ply)) {
			ply.setGameMode(GameMode.ADVENTURE);
			ply.setCollidable(false);
			pd.setSpectatingStatus(true);
			ply.setAllowFlight(true);
			playersSpectating.add(ply);
			playersInGame.removeAll(new ArrayList<Player>() {{
				add(ply);
			}});
			
			ply.sendMessage(ChatColor.YELLOW + "You are now spectating. You are invisible and can fly around.");
			
			//Unhide spectators
			for (Player player : playersSpectating) {
				ply.showPlayer(mainInstance,player);
			}
			
			//Hide player from players in the active game
			for (Player player : playersInGame) {
				player.hidePlayer(mainInstance,ply);
			}
			
			//Set permissions
			pd.setPermission("nyeblock.canBreakBlocks", false);
			pd.setPermission("nyeblock.canUseInventory", false);
			pd.setPermission("nyeblock.canDamage", false);
			pd.setPermission("nyeblock.canBeDamaged", false);
			pd.setPermission("nyeblock.canDropItems", false);
			pd.setPermission("nyeblock.canLoseHunger", false);
		}
		
		//Determine spawn position
		if (pd.getSpectatingStatus()) {
			String index = pd.getCustomDataKey("player_selector_index");
			
			if (index != "-1") {
				loc = players.get(Integer.parseInt(index)).getLocation();
			} else {
				Location location = ply.getLocation();
				
				if (!Arrays.asList(Material.AIR,Material.VOID_AIR).contains(location.subtract(0, .5, 0).getBlock().getType())) {
					loc = location.subtract(0, .5, 0);
				} else {
					for (int i = 0; i < spawns.size(); i++) {
						if (playerSpots.get(i) != null && playerSpots.get(i).equalsIgnoreCase(ply.getName())) {
							loc = spawns.get(i);	
						}
					}
				}
			}
		} else {
			for (int i = 0; i < spawns.size(); i++) {
				if (playerSpots.get(i) != null &&playerSpots.get(i).equalsIgnoreCase(ply.getName())) {
					loc = spawns.get(i);				
				}
			}
		}
		return loc;
	}
	/**
    * Handle when a player died
    */
	public void playerDeath(Player killed,Player killer) {
		boolean isSpectating = playersSpectating.contains(killed);
		
		if (killer != null) {
			for (int i = 0; i < 10; i++) {
				killer.playEffect(killed.getLocation(), Effect.SMOKE, 1);
			}
			killer.playSound(killer.getLocation(), Sound.ITEM_TRIDENT_HIT, 10, 1);
			
			playerKills.put(killer.getName(), playerKills.get(killer.getName()) + 1);
			killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName()));
			addStat(killer,"Kills",10,SummaryStatType.XP);
			killer.setLevel(killer.getLevel() + 2);
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
		pd.setScoreBoardTeams(null,Team.OptionStatus.NEVER);
		pd.createHealthTags();
		
		//Add players to proper scoreboard teams
		updateTeamsFromUserGroups();
		
		//Add player to arrays
		playerKills.put(ply.getName(), 0);
		playerKits.put(ply.getName(),"Stone Mason");
		
		//Find available spot for player
		if (spawns.size() > 0) {
			for (int i = 0; i < spawns.size(); i++) {
				if (!playerSpots.containsKey(i)) {
					Location spawn = spawns.get(i);
					
					playerSpots.put(i,ply.getName());
					ply.teleport(spawn);	
					break;
				}
			}			
		} else {			
			ply.teleport(Bukkit.getWorld(worldName).getSpawnLocation());
		}
		ply.sendTitle(ChatColor.YELLOW + "Welcome to Sky Wars",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map.getName());
		
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
		//Remove player from the current game
		playersInGame.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from spectating list
		playersSpectating.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from the spots list
		Iterator<Map.Entry<Integer, String>> itr = playerSpots.entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry<Integer, String> entry = itr.next();
			
			if (entry.getValue().equalsIgnoreCase(ply.getName())) {
				itr.remove();
			}
		}
	}
}
