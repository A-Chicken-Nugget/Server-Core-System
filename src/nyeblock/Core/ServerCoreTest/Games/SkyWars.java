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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Miscellaneous;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("deprecation")
public class SkyWars extends GameBase {
	//Game info
	private int duration;
	private long startTime;
	private boolean active = false;
	private boolean gameBegun = false;
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
	private boolean endStarted = false;
	private long lastNumber = 0;
	//Scoreboard
	private Objective healthTag;
	
	//
	// CONSTRUCTOR
	//
	
	public SkyWars(Main mainInstance, String worldName, int duration, int maxPlayers) {
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.worldName = worldName;
		realm = Realm.SKYWARS;
		this.duration = duration;
		this.maxPlayers = maxPlayers;
		
		//Scoreboard stuff
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		objective = board.registerNewObjective("scoreboard", "");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "NYEBLOCK (ALPHA)");
		
		//Healthtag stuff
		healthTag = board.registerNewObjective("healthtag", "health");
		healthTag.setDisplaySlot(DisplaySlot.BELOW_NAME);
		healthTag.setDisplayName(ChatColor.DARK_RED + "\u2764");
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("score_" + worldName, .5, 0, "setScoreboard", this, null);
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("main_" + worldName, 1, 0, "mainFunctions", this, null);
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
				
				//Remove glass underneath players
				ply.getLocation().subtract(0, 1, 0).getBlock().setType(Material.AIR);
				ply.getLocation().subtract(0, 2, 0).getBlock().setType(Material.AIR);
				
				//Set permissions
				PlayerData pd = playerHandling.getPlayerData(ply);
				pd.setPermission("nyeblock.canBreakBlocks", true);
				pd.setPermission("nyeblock.canUseInventory", true);
				pd.setPermission("nyeblock.canDropItems", true);
				pd.setPermission("nyeblock.canLoseHunger", true);
				
				//Give kit
				ply.getInventory().clear();
				setPlayerKit(ply,playerKits.get(ply.getName()));
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
//				if (ply.getGameMode() != GameMode.SURVIVAL) {
//					ply.setGameMode(GameMode.SURVIVAL);
//				}
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
				if (players.size() >= 2) {					
					if (readyCount == 0) {
						messageToAll(ChatColor.YELLOW + "The game will begin shortly!");
						soundToAll(Sound.BLOCK_NOTE_BLOCK_PLING,1);
					} else {
						if (readyCount >= 5) {
							active = true;
							countdownStart = System.currentTimeMillis() / 1000L;
							
							mainInstance.getTimerInstance().createTimer("countdown_" + worldName, 1, 7, "countDown", this, null);
						}
					}
					readyCount++;
				} else {
					if (messageCount >= 20) {
						messageCount = 0;
						
//						messageToAll(ChatColor.YELLOW + "Waiting for more players...");
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
		if (playersInGame.size() == 1 && active) {
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
			
			if (!ply.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "SKY WARS")) {						
				pd.setObjectiveName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "SKY WARS");
			}
			
			scores.put(pos++, ChatColor.GREEN + "http://nyeblock.com/");
			scores.put(pos++, ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Kills: " + ChatColor.GREEN + playerKills.get(ply.getName()));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Players Left: " + ChatColor.GREEN + playersInGame.size());
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Miscellaneous.formatSeconds(timeLeft)) : Miscellaneous.formatSeconds(duration)));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
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
    * Get a specific players kit
    * @param player - the player to get the kit for.
    */
	public String getPlayerKit(Player ply) {
		return playerKits.get(ply.getName());
	}
	/**
    * Get players in the current game
    */
	public ArrayList<Player> getPlayersInGame() {
		return playersInGame;
	}
	/**
    * Set a specific players kit
    * @param player - the player to set the kit for.
    */
	public void setPlayerKit(Player ply, String kit) {
		ply.getInventory().clear();
		
		if (kit.equalsIgnoreCase("default")) {
			//Sword
			ItemStack sword = new ItemStack(Material.WOODEN_SWORD);
			ply.getInventory().setItem(0, sword);
			ply.getInventory().setHeldItemSlot(0);
			//Steak
			ItemStack steak = new ItemStack(Material.COOKED_BEEF,10);
			ply.getInventory().setItem(8, steak);
			//Concrete
			ItemStack concrete = new ItemStack(Material.GRAY_CONCRETE,15);
			ply.getInventory().setItem(7, concrete);
		} 
//		else if (kit.equalsIgnoreCase("brawler")) {
//			//Axe
//			ItemStack axe = new ItemStack(Material.IRON_AXE);
//			axe.addEnchantment(Enchantment.DAMAGE_ALL, 2);
//			ply.getInventory().setItem(0, axe);
//			ply.getInventory().setHeldItemSlot(0);
//			//Golden Apples
//			ItemStack goldenApples = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,5);
//			ply.getInventory().setItem(1, goldenApples);
//			//Armor
//			ItemStack[] armor = {
//				new ItemStack(Material.LEATHER_BOOTS),
//				new ItemStack(Material.LEATHER_LEGGINGS),
//				new ItemStack(Material.LEATHER_CHESTPLATE),
//				new ItemStack(Material.LEATHER_HELMET)
//			};
//			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
//			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
//			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
//			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
//			ply.getInventory().setArmorContents(armor);
//		} else if (kit.equalsIgnoreCase("archer")) {
//			//Sword
//			ItemStack sword = new ItemStack(Material.STONE_AXE);
//			sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
//			ply.getInventory().setItem(0, sword);
//			ply.getInventory().setHeldItemSlot(0);
//			//Armor
//			ItemStack[] armor = {
//				new ItemStack(Material.CHAINMAIL_BOOTS),
//				new ItemStack(Material.CHAINMAIL_LEGGINGS),
//				new ItemStack(Material.CHAINMAIL_CHESTPLATE),
//				new ItemStack(Material.CHAINMAIL_HELMET)
//			};
//			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			ply.getInventory().setArmorContents(armor);
//			//Bow
//			ItemStack bow = new ItemStack(Material.BOW);
//			bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
//			ply.getInventory().setItem(1, bow);
//			//Arrows
//			ItemStack arrows = new ItemStack(Material.ARROW,40);
//			ply.getInventory().setItem(2, arrows);
//		} else if (kit.equalsIgnoreCase("wizard")) {
//			//Sword
//			ItemStack sword = new ItemStack(Material.IRON_SWORD);
//			ply.getInventory().setItem(0, sword);
//			ply.getInventory().setHeldItemSlot(0);
//			//Armor
//			ItemStack[] armor = {
//				new ItemStack(Material.LEATHER_BOOTS),
//				new ItemStack(Material.LEATHER_LEGGINGS),
//				new ItemStack(Material.LEATHER_CHESTPLATE),
//				new ItemStack(Material.LEATHER_HELMET)
//			};
//			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			ply.getInventory().setArmorContents(armor);
//			//5 Fire ball
//			ItemStack fireBall = new ItemStack(Material.FIRE_CHARGE,5);
//			ItemMeta fireBallMeta = fireBall.getItemMeta();
//			fireBallMeta.setLocalizedName("kitpvp_wizard_fireball");
//			fireBallMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fire Ball");
//			fireBall.setItemMeta(fireBallMeta);
//			ply.getInventory().setItem(1,fireBall);
//			//2 Potion of harm
//			Potion damage = new Potion(PotionType.INSTANT_DAMAGE, 1);
//			damage.setSplash(true);
//			ply.getInventory().setItem(2, damage.toItemStack(2));
//			//1 Potion of regeneration
//			Potion regen = new Potion(PotionType.REGEN, 1);
//			regen.setSplash(true);
//			ply.getInventory().setItem(3, regen.toItemStack(1));
//		}
		playerKits.put(ply.getName(), kit);
	}
	/**
    * Handle when a player died
    */
	@SuppressWarnings("serial")
	public void playerDeath(Player killed,Player killer) {		
		boolean isSpectating = playersSpectating.contains(killed);
		
		//Add player to ghost mode
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
			
			//Set permissions
			PlayerData pd = playerHandling.getPlayerData(killed);
			pd.setPermission("nyeblock.canBreakBlocks", false);
			pd.setPermission("nyeblock.canUseInventory", false);
			pd.setPermission("nyeblock.canDamage", false);
			pd.setPermission("nyeblock.canBeDamaged", false);
			pd.setPermission("nyeblock.canDropItems", false);
			pd.setPermission("nyeblock.canLoseHunger", false);
		}
		
		if (killer != null) {
			//Update the killers kill count
			playerKills.put(killer.getName(), playerKills.get(killer.getName()) + 1);
			ActionBarAPI.sendActionBar(killer,ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName(), 40);
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
		
		//Set random spawn
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
		
		//Add player to arrays
		players.add(ply);
		playerKills.put(ply.getName(), 0);
		playerKits.put(ply.getName(),"default");
		
		//Find available spot for player
		boolean foundSpot = false;
		for (int i = 0; i < 8; i++) {
			if (!foundSpot && !playerSpots.containsKey(i)) {
				playerSpots.put(i,ply.getName());
				foundSpot = true;
				
				Vector spawn = spawns.get(i);
				ply.teleport(new Location(Bukkit.getWorld(worldName),spawn.getX(),spawn.getY(),spawn.getZ()));				
			}
		}
		ply.sendTitle(ChatColor.YELLOW + "Welcome to Sky Wars",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map);
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
		
		//Remove player from the spots list
		Iterator<Map.Entry<Integer, String>> itr = playerSpots.entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry<Integer, String> entry = itr.next();
			
			if (entry.getValue().equalsIgnoreCase(ply.getName())) {
				itr.remove();
			}
		}
		
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
