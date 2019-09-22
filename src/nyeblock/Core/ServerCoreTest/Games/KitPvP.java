package nyeblock.Core.ServerCoreTest.Games;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Miscellaneous;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class KitPvP {
	//Instances needed to run the game
	private Main mainInstance;
	private PlayerHandling playerHandling;
	//Game info
	private Realm realm = Realm.KITPVP;
	private String worldName;
	private int duration;
	private long startTime;
	private String map;
	private int maxPlayers;
	//Player data
	private ArrayList<Player> players = new ArrayList<>();
	private HashMap<String,Integer> playerKills = new HashMap<>();
	private HashMap<String,String> playerKits = new HashMap<>();
	private HashMap<String,Boolean> playerInGraceBounds = new HashMap<>();
	//Game points
	private Vector safeZonePoint1;
	private Vector safeZonePoint2;
	private ArrayList<Vector> spawns = new ArrayList<>();
	//Etc
	private int emptyCount = 0;
	private boolean endStarted = false;
	private ArrayList<String> top5 = new ArrayList<>();
	
	//
	// CONSTRUCTOR
	//
	
	public KitPvP(Main mainInstance, String worldName, int duration, int maxPlayers) {
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.worldName = worldName;
		this.duration = duration;
		this.maxPlayers = maxPlayers;
		startTime = System.currentTimeMillis() / 1000L;
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("scoreboard_" + worldName, .5, 0, "setScoreboard", this, null);
		
		//Delete timer
		mainInstance.getTimerInstance().createTimer("delete_" + worldName, 1, 0, "checkForDeletion", this, null);
	}
	
	//
	// CLASS METHODS
	//
	
	/**
    * Checks if the server is empty, if it is for 10 seconds then delete timers and world
    */
	public void checkForDeletion() {
		if (players.size() > 0) {        			
			if (emptyCount != 0) {
				emptyCount = 0;
			}
		} else {
			emptyCount++;
			
			if (emptyCount >= 10) {
				//Delete timers
				mainInstance.getTimerInstance().deleteTimer("scoreboard_" + worldName);
				mainInstance.getTimerInstance().deleteTimer("delete_" + worldName);
				//Delete world from server
				mainInstance.getMultiverseInstance().deleteWorld(worldName);
				//Remove game from games array
				mainInstance.getGameInstance().removeGame(realm,worldName);
			}
		}
	}
	/**
    * Sets the kitpvp scoreboard
    */
	public void setScoreboard() {
		//Get top 5 players with the most kills
		HashMap<String,Integer> tempPlayerKills = new HashMap<String,Integer>(playerKills);
		
		for (int i = 0; i < 5; i++) {
			if (tempPlayerKills.size() > 0) {					
				int top = Collections.max(tempPlayerKills.values());
				String removePlayer = null;
				
				for (Map.Entry<String,Integer> entry : tempPlayerKills.entrySet()) {
					if (entry.getValue() == top) {		
						if (top5.size() > i) {
							if (!top5.get(i).equalsIgnoreCase(entry.getKey())) {
								top5.set(i, entry.getKey());
							}
						} else {
							top5.add(i, entry.getKey());
						}
						removePlayer = entry.getKey();
					}
				}
				tempPlayerKills.remove(removePlayer);
			}
		}					
		//Update players scoreboard
		for(Player ply : players)
		{       				
			int pos = 1;
			int timeLeft = (int)(duration-((System.currentTimeMillis() / 1000L)-startTime));
			PlayerData pd = playerHandling.getPlayerData(ply);
			HashMap<Integer,String> scores = new HashMap<Integer,String>();
			
			if (!ply.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName().equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "KITPVP")) {						
				pd.setObjectiveName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "KITPVP");
			}
			
			scores.put(pos++, ChatColor.GREEN + "http://nyeblock.com/");
			scores.put(pos++, ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Kills: " + ChatColor.GREEN + playerKills.get(ply.getName()));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
			if (top5.size() >= 5) {	
				scores.put(pos++, ChatColor.YELLOW + "5th.) " + top5.get(4) + " (" + ChatColor.GREEN + playerKills.get(top5.get(4)) + ChatColor.YELLOW + ")");
			}
			if (top5.size() >= 4) {		
				scores.put(pos++, ChatColor.YELLOW + "4th.) " + top5.get(3) + " (" + ChatColor.GREEN + playerKills.get(top5.get(3)) + ChatColor.YELLOW + ")");
			}
			if (top5.size() >= 3) {		
				scores.put(pos++, ChatColor.YELLOW + "3rd.) " + top5.get(2) + " (" + ChatColor.GREEN + playerKills.get(top5.get(2)) + ChatColor.YELLOW + ")");
			}
			if (top5.size() >= 2) {			
				scores.put(pos++, ChatColor.YELLOW + "2nd.) " + top5.get(1) + " (" + ChatColor.GREEN + playerKills.get(top5.get(1)) + ChatColor.YELLOW + ")");
			}
			if (top5.size() >= 1) {		
				scores.put(pos++, ChatColor.YELLOW + "1st.) " + top5.get(0) + " (" + ChatColor.GREEN + playerKills.get(top5.get(0)) + ChatColor.YELLOW + ")");
			}
			if (top5.size() > 0) {	
				scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			}
			scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (timeLeft <= 0 ? "0:00" : Miscellaneous.formatSeconds(timeLeft)));
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
		//Manage invincible area
		if (players.size() > 0) {        	
			if (safeZonePoint1 != null && safeZonePoint2 != null) {
				for(Player ply : players) {
					Location loc = ply.getLocation();
					
					//Check if player is in the grace bounds
					if(ply.getLocation() != null) {
						if (loc.getBlockX() >= safeZonePoint1.getBlockX() 
								&& loc.getBlockX() <= safeZonePoint2.getBlockX()
								&& loc.getBlockY() >= safeZonePoint1.getBlockY() 
								&& loc.getBlockY() <= safeZonePoint2.getBlockY()
								&& loc.getBlockZ() >= safeZonePoint1.getBlockZ() 
								&& loc.getBlockZ() <= safeZonePoint2.getBlockZ()) {
							PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
							PlayerData pdata = ph.getPlayerData(ply);
							
							if (!playerInGraceBounds.get(ply.getName())) {        								
								playerInGraceBounds.put(ply.getName(), true);
							}
							if (pdata != null) {     
								if (!pdata.getPermission("nyeblock.tempNoDamageOnFall")) {
									pdata.setPermission("nyeblock.tempNoDamageOnFall", true);
								}
								if (pdata.getPermission("nyeblock.canBeDamaged")) {        									
									pdata.setPermission("nyeblock.canBeDamaged", false);
								}
								if (ply.getGameMode() != GameMode.ADVENTURE) {
									ply.setGameMode(GameMode.ADVENTURE);
								}
							}
						} else {
							PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
							PlayerData pdata = ph.getPlayerData(ply);
							
							if (playerInGraceBounds.get(ply.getName())) {        								
								playerInGraceBounds.put(ply.getName(), false);
							}
							if (pdata != null) {
								if (!pdata.getPermission("nyeblock.canBeDamaged")) {
									pdata.setPermission("nyeblock.canBeDamaged", true);
								}
								if (ply.getGameMode() != GameMode.SURVIVAL) {
									ply.setGameMode(GameMode.SURVIVAL);
								}
							}
						}
					}
				}
			}
		}
		//Check when the game has ended and determine winner
		if ((duration-((System.currentTimeMillis() / 1000L)-startTime)) < 0) {
			if (!endStarted) {
				endStarted = true;
				int top = Collections.max(playerKills.values());
				
				if (top > 0) {        					
					for (Map.Entry<String,Integer> entry : playerKills.entrySet()) {
						if (entry.getValue() == top) {
							messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + entry.getKey() + " has won!");
						}
					}
				} else {
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Nobody wins!");
				}
				//Wait 8 seconds, then kick everyone
				mainInstance.getTimerInstance().createTimer("kick_" + worldName, 8, 1, "kickEveryone", this, null);
			}
		}
	}
	/**
    * Kicks everyone from the game
    */
	public void kickEveryone() {
		ArrayList<Player> tempPlayers = new ArrayList<>(players);
		
		for (Player ply : tempPlayers) {			
			playerLeave(ply,false,true);
		}
	}
	/**
    * Sends a message in chat to all players in the game
    * @param message - the message to send.
    */
	public void messageToAll(String message) {
		for(Player ply : players) {
			ply.sendMessage(message);
		}
	}
	/**
    * Checks to see if the provided player is in the game
    * @param player - the player to check for.
    */
	public boolean isInServer(Player ply) {
		boolean found = false;
		
		for(Player player : players) {
			if (ply.getName().equalsIgnoreCase(player.getName())) {
				found = true;
			}
		}
		return found;
	}
	/**
    * Checks to see if the provided player is in the grace zone
    * @param player - the player to check for.
    */
	public boolean isInGraceBounds(Player ply) {
		return playerInGraceBounds.get(ply.getName());
	}
	/**
    * Get the status of the game
    */
	public boolean isGameOver() {
		return endStarted;
	}
	/**
    * Get a specific players kit
    * @param player - the player to get the kit for.
    */
	public String getPlayerKit(Player ply) {
		return playerKits.get(ply.getName());
	}
	/**
    * Get the current amount of players in the game
    */
	public int getPlayerCount() {
		return players.size();
	}
	/**
    * Get the max amount of players the game can have
    */
	public int getMaxPlayers() {
		return maxPlayers;
	}
	/**
    * Get the world name
    */
	public String getWorldName() {
		return worldName;
	}
	/**
    * Get a random spawn point for the game
    */
	public Vector getRandomSpawnPoint() {
		Vector vector = Bukkit.getWorld(worldName).getSpawnLocation().getDirection();
		
		if (spawns.size() > 0) {
			Random r = new Random();
			vector = spawns.get(r.nextInt(spawns.size()));
		}
		return vector;
	}
	/**
    * Set a specific players kit
    * @param player - the player to set the kit for.
    */
	@SuppressWarnings("deprecation")
	public void setPlayerKit(Player ply, String kit) {
		ply.getInventory().clear(0);
		ply.getInventory().clear(1);
		ply.getInventory().clear(2);
		ply.getInventory().clear(3);
		if (kit.equalsIgnoreCase("knight")) {
			//Sword
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			ply.getInventory().setItem(0, sword);
			ply.getInventory().setHeldItemSlot(0);
			//Golden Apples
			ItemStack goldenApples = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,1);
			ply.getInventory().setItem(1, goldenApples);
			//Armor
			ItemStack[] armor = {
				new ItemStack(Material.IRON_BOOTS),
				new ItemStack(Material.IRON_LEGGINGS),
				new ItemStack(Material.IRON_CHESTPLATE),
				new ItemStack(Material.IRON_HELMET)
			};
			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ply.getInventory().setArmorContents(armor);
		} else if (kit.equalsIgnoreCase("brawler")) {
			//Axe
			ItemStack axe = new ItemStack(Material.IRON_AXE);
			axe.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			ply.getInventory().setItem(0, axe);
			ply.getInventory().setHeldItemSlot(0);
			//Golden Apples
			ItemStack goldenApples = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,5);
			ply.getInventory().setItem(1, goldenApples);
			//Armor
			ItemStack[] armor = {
				new ItemStack(Material.LEATHER_BOOTS),
				new ItemStack(Material.LEATHER_LEGGINGS),
				new ItemStack(Material.LEATHER_CHESTPLATE),
				new ItemStack(Material.LEATHER_HELMET)
			};
			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			ply.getInventory().setArmorContents(armor);
		} else if (kit.equalsIgnoreCase("archer")) {
			//Sword
			ItemStack sword = new ItemStack(Material.STONE_AXE);
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			ply.getInventory().setItem(0, sword);
			ply.getInventory().setHeldItemSlot(0);
			//Armor
			ItemStack[] armor = {
				new ItemStack(Material.CHAINMAIL_BOOTS),
				new ItemStack(Material.CHAINMAIL_LEGGINGS),
				new ItemStack(Material.CHAINMAIL_CHESTPLATE),
				new ItemStack(Material.CHAINMAIL_HELMET)
			};
			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ply.getInventory().setArmorContents(armor);
			//Bow
			ItemStack bow = new ItemStack(Material.BOW);
			bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
			ply.getInventory().setItem(1, bow);
			//Arrows
			ItemStack arrows = new ItemStack(Material.ARROW,40);
			ply.getInventory().setItem(2, arrows);
		} else if (kit.equalsIgnoreCase("wizard")) {
			//Sword
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			ply.getInventory().setItem(0, sword);
			ply.getInventory().setHeldItemSlot(0);
			//Armor
			ItemStack[] armor = {
				new ItemStack(Material.LEATHER_BOOTS),
				new ItemStack(Material.LEATHER_LEGGINGS),
				new ItemStack(Material.LEATHER_CHESTPLATE),
				new ItemStack(Material.LEATHER_HELMET)
			};
			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			ply.getInventory().setArmorContents(armor);
			//5 Fire ball
			ItemStack fireBall = new ItemStack(Material.FIRE_CHARGE,5);
			ItemMeta fireBallMeta = fireBall.getItemMeta();
			fireBallMeta.setLocalizedName("kitpvp_wizard_fireball");
			fireBallMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Fire Ball");
			fireBall.setItemMeta(fireBallMeta);
			ply.getInventory().setItem(1,fireBall);
			//2 Potion of harm
			Potion damage = new Potion(PotionType.INSTANT_DAMAGE, 1);
			damage.setSplash(true);
			ply.getInventory().setItem(2, damage.toItemStack(2));
			//1 Potion of regeneration
			Potion regen = new Potion(PotionType.REGEN, 1);
			regen.setSplash(true);
			ply.getInventory().setItem(3, regen.toItemStack(1));
		}
		playerKits.put(ply.getName(), kit);
	}
	/**
    * Set the map for the game
    * @param map - the map to set.
    */
	public void setMap(String map) {
		this.map = map;
	}
	/**
    * Set the spawn points for the game
    * @param spawns - an array list of spawn vectors.
    */
	public void setSpawnPoints(ArrayList<Vector> spawns) {
		this.spawns = spawns;
	}
	/**
    * Set the grace zone bounds
    * @param bound 1 - a vector for bound 1.
    * @param bound 2 - a vector for bound 2.
    */
	public void setSafeZoneBounds(Vector p1, Vector p2) {
		int x1 = Math.min(p1.getBlockX(), p2.getBlockX());
		int y1 = Math.min(p1.getBlockY(), p2.getBlockY());
		int z1 = Math.min(p1.getBlockZ(), p2.getBlockZ());
		int x2 = Math.max(p1.getBlockX(), p2.getBlockX());
		int y2 = Math.max(p1.getBlockY(), p2.getBlockY());
		int z2 = Math.max(p1.getBlockZ(), p2.getBlockZ());
		this.safeZonePoint1 = new Vector( x1, y1, z1);
		this.safeZonePoint2 = new Vector( x2, y2, z2);
	}
	/**
    * Handles when a player dies in the game
    * @param killed - the player killed.
    * @param killer - the player who killed.
    */
	public void playerDeath(Player killed,Player killer) {
		if (killer != null) {
			//Update the killers kill count
			playerKills.put(killer.getName(), playerKills.get(killer.getName()) + 1);
			ActionBarAPI.sendActionBar(killer,ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName(), 40);
			messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.GREEN + killer.getName() + ChatColor.YELLOW + "!");
		} else {
			messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " has died!");
		}
	}
	/**
    * Handles when a player joins the game
    * @param player - the player who joined the game.
    */
	@SuppressWarnings("deprecation")
	public void playerJoin(Player ply) {
		messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has joined the game!");
		//Add player to players array
		players.add(ply);
		//Set the players kills to 0
		playerKills.put(ply.getName(), 0);
		playerKits.put(ply.getName(),"knight");
		playerInGraceBounds.put(ply.getName(), true);
		Vector randSpawn = getRandomSpawnPoint();
		ply.teleport(new Location(Bukkit.getWorld(worldName),randSpawn.getX(),randSpawn.getY(),randSpawn.getZ()));
		ply.sendTitle(ChatColor.YELLOW + "Welcome to KitPvP",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map);
	}
	/**
    * Handles when a player leaves the game
    * @param player - the player who left the game.
    * @param bool - should a leave message be shown?
    * @param bool - should the player be moved to the hub?
    */
	public void playerLeave(Player ply, boolean showLeaveMessage, boolean moveToHub) {
		//Remove player from players list
		ArrayList<Player> playersToRemove = new ArrayList<Player>() {{
			add(ply);
		}};
		players.removeAll(playersToRemove);
		
		//Remove player from hashmaps
		playerKills.remove(ply.getName());
		playerKits.remove(ply.getName());
		playerInGraceBounds.remove(ply.getName());
		
		//Remove player from top5 list
		ArrayList<String> plyToRemove = new ArrayList<String>();
		for(String player : top5) {
			if (player.equalsIgnoreCase(ply.getName())) {
				plyToRemove.add(player);
			}
		}
		top5.removeAll(plyToRemove);
		
		if (showLeaveMessage) {			
			messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has left the game!");
		}
		if (moveToHub) {
			PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			
			//Set player realms/items/permissions
			playerData.setRealm(Realm.HUB,true,true);
			//Move player to hub
			mainInstance.getGameInstance().joinGame(ply, Realm.HUB);
		}
	}
}