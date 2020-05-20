package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.KitHandling;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Items.ReturnToLobby;
import nyeblock.Core.ServerCoreTest.Maps.MapPoint;
import nyeblock.Core.ServerCoreTest.Menus.KitSelectorMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.SummaryStatType;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings("deprecation")
public class KitPvP extends GameBase {
	private KitHandling kitHandling = mainInstance.getKitHandlingInstance();
	//Player data
	private HashMap<String,Integer> playerKills = new HashMap<>();
	private HashMap<String,String> playerKits = new HashMap<>();
	private HashMap<String,Boolean> playerInGraceBounds = new HashMap<>();
	//Etc
	private Vector safeZonePoint1;
	private Vector safeZonePoint2;
	private ArrayList<String> top5 = new ArrayList<>();
	private boolean pointsSet = false;
	
	//
	// CONSTRUCTORS
	//
	
	public KitPvP(Main mainInstance, int id, String worldName) {
		super(mainInstance,Realm.KITPVP,worldName,Realm.KITPVP_LOBBY);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.id = id;
		this.worldName = worldName;
		duration = 120;
		maxPlayers = 20;
		gameBegun = true;
		startTime = System.currentTimeMillis() / 1000L;
	}
	
	public KitPvP(Main mainInstance, int id, String worldName, int duration, int maxPlayers) {
		super(mainInstance,Realm.KITPVP,worldName,Realm.KITPVP_LOBBY);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.id = id;
		this.worldName = worldName;
		this.duration = duration;
		this.maxPlayers = maxPlayers;
		gameBegun = true;
		startTime = System.currentTimeMillis() / 1000L;
	}
	
	//
	// CLASS METHODS
	//
	
//	/**
//  * Creates a new instance of this game
//  * @param mainInstance - instance of the main class
//  * @param realm - realm of the game
//  * @param id - id of the game
//  * @param worldName - name of the world the game is going to be on
//  * @return the new instance of the game
//  */
//	public static GameBase createNewInstance(Main mainInstance, Realm realm, int id, String worldName) {
//		return new KitPvP(mainInstance,id,worldName,120,20);
//	}
	
	/**
    * Kick everyone in the game
    */
	public void kickEveryone() {
		ArrayList<Player> allPlayers = getPlayers(true);
		
		for (Player ply : allPlayers) {			
			//Unhide all players who might be hidden for certain players
			for (Player player : allPlayers) {
				if (!ply.canSee(player)) {					
					player.showPlayer(mainInstance,ply);
				}
			}
			
			leave(ply,false,lobbyRealm);
		}
	}
	/**
	* What needs to be ran when the world is created
	*/
	public void onCreate() {
		//Scoreboard
		scoreboard = new Runnable() {
			@Override
			public void run() {
				for(Player ply : getPlayers(true))
				{    
					int pos = 1;
					int timeLeft = (int)(duration-((System.currentTimeMillis() / 1000L)-startTime));
					PlayerData pd = playerHandling.getPlayerData(ply);
					HashMap<Integer,String> scores = new HashMap<Integer,String>();
					
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
					scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (timeLeft <= 0 ? "0:00" : Toolkit.formatMMSS(timeLeft)));
					scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));			
					if (shouldRainbowTitleText) {
						pd.setScoreboardTitle(chatColorList.get(new Random().nextInt(chatColorList.size())) + ChatColor.BOLD.toString() + "KITPVP");				
					} else {				
						pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "KITPVP");
					}
					pd.updateObjectiveScores(scores);
				}
			}
		};
		
		//Set points
		for (MapPoint point : map.getPoints()) {
			if (point.getType() == MapPointType.PLAYER_SPAWN) {
				spawns.add(point.getLocation());
			} else if (point.getType() == MapPointType.GRACE_BOUND) {
				if (safeZonePoint1 == null) {
					safeZonePoint1 = point.getLocation().toVector();
				} else {
					safeZonePoint2 = point.getLocation().toVector();
				}
			}
		}
		pointsSet = true;
		
		//Clear all entities
		for (Entity ent : world.getEntities()) ent.remove();
		
		//Main functions timer
	    mainInstance.getTimerInstance().createMethodTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
	}
	/**
    * What needs to be ran when the world is deleted
    */
	public void onDelete() {
		mainInstance.getTimerInstance().deleteTimer("delete_" + worldName);
		mainInstance.getTimerInstance().deleteTimer("main_" + worldName);
	}
	/**
	* Run main checks for the game
	*/
	public void mainFunctions() {
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
		//Manage invincible area
		if (pointsSet && players.size() > 0) {
			for(Player ply : players) {
				Location loc = ply.getLocation();
				
				if(ply.getLocation() != null) {
					PlayerData pdata = playerHandling.getPlayerData(ply);
					
					//Check if player is in the grace bounds
					if (Toolkit.playerInArea(loc.toVector(), safeZonePoint1, safeZonePoint2)) {
						if (playerInGraceBounds.get(ply.getName()) == null || !playerInGraceBounds.get(ply.getName())) {
							playerInGraceBounds.put(ply.getName(), true);
						}
						if (pdata != null) {     
							if (!ply.hasPermission("nyeblock.tempNoDamageOnFall")) {
								pdata.setPermission("nyeblock.tempNoDamageOnFall", true);
							}
							if (ply.hasPermission("nyeblock.canBeDamaged")) {        									
								pdata.setPermission("nyeblock.canBeDamaged", false);
							}
						}
						ply.setGameMode(GameMode.ADVENTURE);
					} else {
						if (playerInGraceBounds.get(ply.getName())) {     
							//Remove players to team
							for (Map.Entry<String,Boolean> entry : playerInGraceBounds.entrySet()) {
								Player player = Bukkit.getServer().getPlayer(entry.getKey());
								
								if (entry.getValue()) {
									PlayerData pd2 = playerHandling.getPlayerData(player);
									
									pdata.removePlayerFromTeam("default",player);
									pd2.removePlayerFromTeam("default", ply);
								}
							}
							ply.getInventory().clear(7);
							ply.getInventory().clear(8);
							playerInGraceBounds.put(ply.getName(), false);
							if (ply.getOpenInventory() != null) {
								ply.getOpenInventory().close();
							}
							ply.setGameMode(GameMode.SURVIVAL);
						}
						if (pdata != null) {
							if (!ply.hasPermission("nyeblock.canBeDamaged")) {
								pdata.setPermission("nyeblock.canBeDamaged", true);
							}
						}
					}
				}
			}
		}
		//Check when the game has ended and determine winner
		if ((duration-((System.currentTimeMillis() / 1000L)-startTime)) < 0) {
			if (!endStarted && players.size() > 0) {
				endStarted = true;
				canUsersJoin = false;
				int top = Collections.max(playerKills.values());
				
				//TODO REDO
				if (top > 0) {
					for (Map.Entry<String,Integer> entry : playerKills.entrySet()) {
						Player ply = Bukkit.getServer().getPlayer(entry.getKey());
						
						if (entry.getValue() == top) {
							playWinAction(ply);
							messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + entry.getKey() + " has won!");
							addStat(ply,"Placing #1",200,SummaryStatType.XP);
							playerHandling.getPlayerData(ply).addGamePlayed(realm, true);
						} else {						
							int place = 0;
							for (int i = 0; i < top5.size(); i++) {
								if (top5.get(i).equalsIgnoreCase(ply.getName())) {									
									place = i+1;
								}
							}
							
							if (place > 1) {
								addStat(ply,"Placing #" + place,200-(place*35),SummaryStatType.XP);
							}
						}
					}
					for (Player player : players) {
						//Print the players xp summary
						printSummary(player,false);
					}
				} else {
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Nobody wins!");
				}
				soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
				//Wait 10 seconds, then kick everyone
				mainInstance.getTimerInstance().createMethodTimer("kick_" + worldName, 10, 1, "kickEveryone", false, null, this);
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
		permissions.setPermission("nyeblock.canBeDamaged", false);
		permissions.setPermission("nyeblock.canTakeFallDamage", true);
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
		//Return to lobby
		ReturnToLobby returnToLobby = new ReturnToLobby(mainInstance,lobbyRealm,player);
		player.getInventory().setItem(8, returnToLobby.give());
		
		//Select kit
		KitSelectorMenu selectKit = new KitSelectorMenu(mainInstance,player);
		player.getInventory().setItem(7, selectKit.give());
	}
	/**
    * Checks to see if the provided player is in the grace zone
    * @param player - the player to check for.
    */
	public boolean isInGraceBounds(Player ply) {
		return playerInGraceBounds.get(ply.getName());
	}
	/**
    * Get a specific players kit
    * @param player - the player to get the kit for.
    */
	public String getPlayerKit(Player ply) {
		return playerKits.get(ply.getName());
	}
	/**
    * Set a specific players kit
    * @param player - the player to set the kit for.
    */
	public void setPlayerKit(Player ply, String kit) {
		//Reset Potion effects
		for(PotionEffect effect : ply.getActivePotionEffects()) {
			ply.removePotionEffect(effect.getType());
		}
		playerKits.put(ply.getName(), kit);
		kitHandling.setKit(ply, realm, kit);
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		ply.getInventory().clear();
		setItems(ply);
		kitHandling.setKit(ply, realm, playerKits.get(ply.getName()));
		
		return getRandomSpawnPoint();
	}
	/**
    * Handles when a player dies in the game
    * @param killed - the player killed.
    * @param killer - the player who killed.
    */
	public void playerDeath(Player killed,Player killer) {
		if (killer != null) {
			for (int i = 0; i < 10; i++) {
				killer.playEffect(killed.getLocation(), Effect.SMOKE, 1);
			}
			killer.playSound(killer.getLocation(), Sound.ITEM_TRIDENT_HIT, 10, 1);
			
			//Update the killers kill count
			if (playerKills.get(killer.getName()) != null) {				
				playerKills.put(killer.getName(), playerKills.get(killer.getName()) + 1);
			}
			
			killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " (" + ChatColor.GREEN + "+10 XP" + ChatColor.YELLOW + ")"));
			addStat(killer,"Kills",1,SummaryStatType.XP);
			
			messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " was killed by " + ChatColor.GREEN + killer.getName() + ChatColor.YELLOW + "!");
		} else {
			messageToAll(ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " has died!");
		}
	}
	/**
    * Handles when a player joins the game
    * @param player - the player who joined the game.
    */
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Setup team
		pd.setScoreBoardTeams(null,Team.OptionStatus.NEVER);
		pd.createHealthTags();
		
		//Add player to arrays
		playerKills.put(ply.getName(), 0);
		
		pd.addGamePlayed(realm, false);
		
		setPlayerKit(ply,"Knight");
		
		//Add players to proper scoreboard teams
		updateTeamsFromUserGroups();
		
		//Teleport to random spawn
		ply.teleport(getRandomSpawnPoint());
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to KitPvP",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map.getName());
		
		for (Player player : players) {
			if (player.getHealth() == 20) {				
				player.setHealth(player.getHealth() - 0.0001);
			}
		}
	}
	/**
    * Handles when a player leaves the game
    * @param player - the player who left the game.
    * @param bool - should a leave message be shown?
    * @param bool - should the player be moved to the hub?
    */
	public void playerLeave(Player ply) {
		PlayerData playerData = playerHandling.getPlayerData(ply);
		
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
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandling.getPlayerData(player);
			
			pd2.removePlayerFromTeam(playerData.getPrimaryUserGroup().toString(), ply);
		}
	}
}