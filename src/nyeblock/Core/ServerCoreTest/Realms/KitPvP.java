package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserRealm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings("deprecation")
public class KitPvP extends GameBase {
	//Game info
	private int duration;
	private long startTime;
	//Player data
	private HashMap<String,Integer> playerKills = new HashMap<>();
	private HashMap<String,String> playerKits = new HashMap<>();
	private HashMap<String,Boolean> playerInGraceBounds = new HashMap<>();
	//Etc
	private boolean endStarted = false;
	private ArrayList<String> top5 = new ArrayList<>();
	private int playTimeCount = 0;
	
	//
	// CONSTRUCTOR
	//
	
	public KitPvP(Main mainInstance, String worldName, int duration, int maxPlayers) {
		super(mainInstance,worldName);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.worldName = worldName;
		realm = UserRealm.KITPVP;
		this.duration = duration;
		this.maxPlayers = maxPlayers;
		startTime = System.currentTimeMillis() / 1000L;
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("scoreboard_" + worldName, .5, 0, "setScoreboard", false, null, this);
		
		//Delete timer
		mainInstance.getTimerInstance().createTimer("delete_" + worldName, 1, 0, "checkForDeletion", false, null, this);
		
//		PacketPlayOutWorldBorder border = new PacketPlayOutWorldBorder(null, PacketPlayOutWorldBorder.EnumWorldBorderAction.INITIALIZE);
//        border.
//		border.setX(Bukkit.getWorld("SkyRealms").getSpawnLocation().getX());
//        border.setX(Bukkit.getWorld("SkyRealms").getSpawnLocation().getX());
//        border.setZ(Bukkit.getWorld("SkyRealms").getSpawnLocation().getZ());
//        border.setRadius(20 / 2);
//        border.setSpeed(1L);
//        border.setPortalBoundary(10);
//        border.setWarningTime(20);
//        border.setWarningBlocks(2);
	}
	
	//
	// CLASS METHODS
	//
	
	/**
    * Kick everyone in the game
    */
	public void kickEveryone() {
		mainInstance.getTimerInstance().deleteTimer(worldName + "_fireworks");
		ArrayList<Player> tempPlayers = new ArrayList<>(players);
		
		for (Player ply : tempPlayers) {
			playerLeave(ply,false,true);
		}
	}
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
				canUsersJoin = false;
				
				//Delete timers
				mainInstance.getTimerInstance().deleteTimer("scoreboard_" + worldName);
				mainInstance.getTimerInstance().deleteTimer("delete_" + worldName);
				//Delete world from server
				mainInstance.getMultiverseInstance().deleteWorld(worldName);
				//Remove game from games array
				mainInstance.getGameInstance().removeGameFromList(gamePos);
			}
		}
	}
	/**
    * Sets the kitpvp scoreboard and manages world/grace zone
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
			pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "KITPVP");
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
					
					if(ply.getLocation() != null) {
						PlayerData pdata = playerHandling.getPlayerData(ply);
						
						//Check if player is in the grace bounds
						if (Toolkit.playerInArea(loc.toVector(), safeZonePoint1, safeZonePoint2)) {
							if (playerInGraceBounds.get(ply.getName()) == null || !playerInGraceBounds.get(ply.getName())) {
								//Add players to team
								for (Map.Entry<String,Boolean> entry : playerInGraceBounds.entrySet()) {
									if (entry.getValue()) {
										Player player = Bukkit.getServer().getPlayer(entry.getKey());
										PlayerData pd2 = playerHandling.getPlayerData(player);
										
										pdata.addPlayerToTeam("default",player);
										pd2.addPlayerToTeam("default", ply);
									}
								}
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
								playerInGraceBounds.put(ply.getName(), false);
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
		}
		//Check when the game has ended and determine winner
		if ((duration-((System.currentTimeMillis() / 1000L)-startTime)) < 0) {
			if (!endStarted && players.size() > 0) {
				endStarted = true;
				int top = Collections.max(playerKills.values());
				
				//TODO REDO
				if (top > 0) {        					
					for (Map.Entry<String,Integer> entry : playerKills.entrySet()) {
						Player ply = Bukkit.getServer().getPlayer(entry.getKey());
						
						if (entry.getValue() == top) {
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
							messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + entry.getKey() + " has won!");
							giveXP(ply,"Placing #1",200);
						} else {						
							int place = 0;
							for (int i = 0; i < top5.size(); i++) {
								if (top5.get(i).equalsIgnoreCase(ply.getName())) {									
									place = 5-i;
								}
							}
							
							if (place <= 5) {
								giveXP(ply,"Placing #" + place,200-(place*35));
							}
						}

						//Print the players xp summary
						printSummary(ply,true);
					}
				} else {
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Nobody wins!");
				}
				soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
				//Wait 10 seconds, then kick everyone
				mainInstance.getTimerInstance().createTimer("kick_" + worldName, 10, 1, "kickEveryone", false, null, this);
			}
		}
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
    * Set a specific players kit
    * @param player - the player to set the kit for.
    */
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
    * Handles when a player dies in the game
    * @param killed - the player killed.
    * @param killer - the player who killed.
    */
	public void playerDeath(Player killed,Player killer) {
		if (killer != null) {
			//Update the killers kill count
			playerKills.put(killer.getName(), playerKills.get(killer.getName()) + 1);
			
			killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName() + ChatColor.YELLOW + " (" + ChatColor.GREEN + "+10 XP" + ChatColor.YELLOW + ")"));
			giveXP(killer,"Kills",10);
			
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
		pd.setScoreBoardTeams(new String[] {"default"});
		pd.createHealthTags();
		
		//Add player to arrays
		playerKills.put(ply.getName(), 0);
		playerKits.put(ply.getName(),"knight");
		
		//Add player to proper team
		if (pd.getUserGroup() == UserGroup.ADMIN) {
			pd.addPlayerToTeam("admin", ply);
		} else if (pd.getUserGroup() == UserGroup.MODERATOR) {
			pd.addPlayerToTeam("moderator", ply);
		} else if (pd.getUserGroup() == UserGroup.TESTER) {
			pd.addPlayerToTeam("tester", ply);
		} else {
			pd.addPlayerToTeam("default", ply);
		}
		
		//Add players to teams
		for (Player player : players) {
			PlayerData pd2 = playerHandling.getPlayerData(player);
			
			if (player != ply) {
				//Update joining player team
				if (pd2.getUserGroup() == UserGroup.ADMIN) {
					pd.addPlayerToTeam("admin", player);
				} else if (pd2.getUserGroup() == UserGroup.MODERATOR) {
					pd.addPlayerToTeam("moderator", player);
				} else if (pd2.getUserGroup() == UserGroup.TESTER) {
					pd.addPlayerToTeam("tester", ply);
				} else {					
					pd.addPlayerToTeam("default", player);
				}
				
				//Update current players teams
				if (pd.getUserGroup() == UserGroup.ADMIN) {
					pd2.addPlayerToTeam("admin", ply);
				} else if (pd.getUserGroup() == UserGroup.MODERATOR) {
					pd2.addPlayerToTeam("moderator", ply);
				} else if (pd.getUserGroup() == UserGroup.TESTER) {
					pd2.addPlayerToTeam("tester", ply);
				} else {
					pd2.addPlayerToTeam("default", ply);
				}
			}
		}
		
		//Teleport to random spawn
		Location randSpawn = getRandomSpawnPoint();
		ply.teleport(randSpawn);
//		ply.teleport(new Location(Bukkit.getWorld("games_world"),(gamePos.x*500)-(500/2),0,(gamePos.y*500)-(500/2)));
		
		ply.sendTitle(ChatColor.YELLOW + "Welcome to KitPvP",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map);
		
		for (Player player : players) {
			if (player.getHealth() == 20) {				
				player.setHealth(player.getHealth() - 0.0001);
			}
		}
		
		//Set gamemode
		//ply.setGameMode(GameMode.SURVIVAL);
	}
	/**
    * Handles when a player leaves the game
    * @param player - the player who left the game.
    * @param bool - should a leave message be shown?
    * @param bool - should the player be moved to the hub?
    */
	public void playerLeave(Player ply, boolean showLeaveMessage, boolean moveToHub) {
		PlayerData playerData = playerHandling.getPlayerData(ply);
		
		//Remove player from players list
		players.remove(ply);
		
		//Remove player from hashmaps
		playerKills.remove(ply.getName());
		playerKits.remove(ply.getName());
		playerInGraceBounds.remove(ply.getName());
		playerXP.remove(ply);
		
		//Remove player from top5 list
		ArrayList<String> plyToRemove = new ArrayList<String>();
		for(String player : top5) {
			if (player.equalsIgnoreCase(ply.getName())) {
				plyToRemove.add(player);
			}
		}
		top5.removeAll(plyToRemove);
		
		//Clear scoreboard
		playerData.clearScoreboard();
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandling.getPlayerData(player);
			
			pd2.removePlayerFromTeam("default", ply);
		}
		
		if (showLeaveMessage) {			
			messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has left the game!");
		}
		if (moveToHub) {
			//Set player realms/items/permissions
			playerData.setRealm(UserRealm.HUB,true,true);
			//Move player to hub
			mainInstance.getGameInstance().joinGame(ply, UserRealm.HUB);
		}
	}
}