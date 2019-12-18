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
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings({"deprecation","serial"})
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
	
	//
	// CONSTRUCTOR
	//
	
	public SkyWars(Main mainInstance, String worldName, int duration, int minPlayers, int maxPlayers) {
		super(mainInstance,worldName);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.worldName = worldName;
		realm = Realm.SKYWARS;
		this.duration = duration;
		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("score_" + worldName, .5, 0, "setScoreboard", false, null, this);
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
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
			//Unhide all players who might be hidden for certain players
			for (Player player : tempPlayers) {
				if (!ply.canSee(player)) {					
					player.showPlayer(mainInstance,ply);
				}
			}
			
			leave(ply,false,Realm.HUB);
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
				setPlayerKit(ply,playerKits.get(ply.getName()));
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
    * Run main checks for the game
    */
	public void mainFunctions() {
		//Set compass targets
		for (Player ply : playersSpectating) {
			PlayerData pd = playerHandling.getPlayerData(ply);
			String key = pd.getCustomDataKey("player_selector_index");
			int currentIndex = Integer.parseInt((key == null ? "0" : key));
			
			if (playersInGame.size() > currentIndex) {
				ply.setCompassTarget(players.get(currentIndex).getLocation());
			} else {
				if (playersInGame.size() > 0) {
					ply.setCompassTarget(players.get(0).getLocation());
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
					} else {
						if (readyCount >= 5) {
							active = true;
							countdownStart = System.currentTimeMillis() / 1000L;
							
							mainInstance.getTimerInstance().createTimer("countdown_" + worldName, 1, 7, "countDown", false, null, this);
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
		
		//Check if player has won
		if (playersInGame.size() == 1 && active) {
			for (Player ply : playersInGame) {				
				if (!endStarted) {
					endStarted = true;
					canUsersJoin = false;
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
					messageToAll(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + ply.getName() + " has won!");
					soundToAll(Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1);
					giveXP(ply,"Placing #1",200);
					for (Player player : players) {
						//Print the players xp summary
						printSummary(player,true);
					}
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
			scores.put(pos++, ChatColor.YELLOW + "Kills: " + ChatColor.GREEN + playerKills.get(ply.getName()));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Players Left: " + ChatColor.GREEN + playersInGame.size());
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Time left: " + ChatColor.GREEN + (gameBegun ? (timeLeft <= 0 ? "0:00" : Toolkit.formatMMSS(timeLeft)) : Toolkit.formatMMSS(duration)));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
			pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "SKY WARS");
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
		//Check when the game has ended
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
	public ArrayList<Player> getPlayersInRealm() {
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
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		Location loc = null;
		
		pd.setItems();
		if (pd.getSpectatingStatus()) {
			loc = players.get(Integer.parseInt(pd.getCustomDataKey("player_selector_index"))).getLocation();
		} else {				
			loc = getRandomSpawnPoint();
		}
		return loc;
	}
	/**
    * Handle when a player died
    */
	public void playerDeath(Player killed,Player killer) {		
		boolean isSpectating = playersSpectating.contains(killed);
		
		//Add player to ghost mode
		if (gameBegun && !isSpectating) {	
			PlayerData pd = playerHandling.getPlayerData(killed);
			
			pd.setSpectatingStatus(true);
			killed.setAllowFlight(true);
			playersSpectating.add(killed);
			playersInGame.removeAll(new ArrayList<Player>() {{
				add(killed);
			}});
			
			killed.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You are now spectating. You are invisible and can fly around."));
			
			//Unhide spectators
			for (Player ply : playersSpectating) {
				killed.showPlayer(mainInstance,ply);
			}
			
			//Hide player from players in the active game
			for (Player ply : playersInGame) {
				ply.hidePlayer(mainInstance,killed);
			}
			
			//Set permissions
			pd.setPermission("nyeblock.canBreakBlocks", false);
			pd.setPermission("nyeblock.canUseInventory", false);
			pd.setPermission("nyeblock.canDamage", false);
			pd.setPermission("nyeblock.canBeDamaged", false);
			pd.setPermission("nyeblock.canDropItems", false);
			pd.setPermission("nyeblock.canLoseHunger", false);
		}
		
		if (killer != null) {
			for (int i = 0; i < 10; i++) {
				killer.playEffect(killed.getLocation(), Effect.SMOKE, 1);
			}
			killer.playSound(killer.getLocation(), Sound.ITEM_TRIDENT_HIT, 10, 1);
			
			playerKills.put(killer.getName(), playerKills.get(killer.getName()) + 1);
			killer.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You killed " + ChatColor.GREEN + killed.getName()));
			giveXP(killer,"Kills",10);
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
		
		//Add player to arrays
		playerKills.put(ply.getName(), 0);
		playerKits.put(ply.getName(),"default");
		
		//Find available spot for player
		boolean foundSpot = false;
		for (int i = 0; i < 8; i++) {
			if (!foundSpot && !playerSpots.containsKey(i)) {
				playerSpots.put(i,ply.getName());
				foundSpot = true;
				
				Location spawn = spawns.get(i);
				ply.teleport(spawn);				
			}
		}
		ply.sendTitle(ChatColor.YELLOW + "Welcome to Sky Wars",ChatColor.YELLOW + "Map: " + ChatColor.GREEN + map);
		
		for (Player player : players) {
			if (player.getHealth() == 20) {				
				player.setHealth(player.getHealth() - 0.0001);
			}
		}
	}
	/**
    * Handle when a player leaves the game
    */
	public void playerLeave(Player ply, boolean showLeaveMessage) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Remove player from the current game
		playersInGame.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from spectating list
		playersSpectating.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Clear scoreboard
		pd.clearScoreboard();
		
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
	}
}
