package nyeblock.Core.ServerCoreTest.Realms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.boydti.fawe.object.clipboard.DiskOptimizedClipboard;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;

import net.coreprotect.CoreProtectAPI;
import net.coreprotect.CoreProtectAPI.ParseResult;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.DataWatcher.Item;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Maps.MapBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopItem;
import nyeblock.Core.ServerCoreTest.Misc.DamagePlayer;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.SummaryStatType;
import nyeblock.Core.ServerCoreTest.Misc.PlayerSummary;
import nyeblock.Core.ServerCoreTest.Misc.SummaryStat;

@SuppressWarnings("serial")
public abstract class GameBase extends RealmBase {
	//Instances
	protected Main mainInstance;
	protected PlayerHandling playerHandling;
	//Game info
	protected int id;
	protected String worldName;
	protected MapBase map;
	protected boolean gameBegun = false;
	protected long created = System.currentTimeMillis();
	protected World world;
	protected Realm lobbyRealm;
	//Player data
	protected ArrayList<HashMap<Location,Player>> teamsSetup = new ArrayList<>();
	private HashMap<Player,PlayerSummary> playerSummary = new HashMap<>();
	private ArrayList<String> totalUsers = new ArrayList<>();
	private HashMap<Player,Integer> playerTimePlayed = new HashMap<>();
	//Game points
	protected ArrayList<Location> spawns = new ArrayList<>();
	//Etc
	protected boolean shouldRainbowTitleText = false;
	protected boolean setWorldTime = false;
	protected int duration;
	protected long startTime;
	protected boolean active = false;
	protected int emptyCount = 0;
	protected boolean canUsersJoin = true;
	protected boolean forceStart = false;
	protected int playTimeCount = 0;
	protected boolean isSchematicSet = false;
	protected boolean endStarted = false;
	private HashMap<String,Color> colorStrings = new HashMap<String,Color>() {{
		put("red",Color.RED);
		put("blue",Color.BLUE);
		put("green",Color.GREEN);
	}};
	
	public GameBase(Main mainInstance, Realm realm, String worldName, Realm lobbyRealm) {
		super(mainInstance,realm);
		this.mainInstance = mainInstance;
		this.lobbyRealm = lobbyRealm;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		
		//Create timer to check when the game world is created
		mainInstance.getTimerInstance().createMethodTimer("worldCheck_" + worldName, 1, 0, "checkWorld", true, null, this);
		
		//Create timer to manage game functions
		mainInstance.getTimerInstance().createRunnableTimer("gameFunctions_" + worldName, 1, 0, new Runnable() {
			@Override
			public void run() {
				//Manage each players play time if the game has started
				if (gameBegun) {		
					for (Player player : players) {
						Integer timePlayed = playerTimePlayed.get(player);
						
						if (timePlayed == null) {
							playerTimePlayed.put(player,1);
						} else {
							if (timePlayed % 120 == 0 && !endStarted) {
								addStat(player,"Play time",5,SummaryStatType.XP);
								player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + "You have received " + ChatColor.GREEN + "5xp" + ChatColor.YELLOW + " for playing."));
							}
							playerTimePlayed.put(player,timePlayed + 1);
						}
					}
				}
			}
		});
		
		//Create timer to delete world if empty
		mainInstance.getTimerInstance().createRunnableTimer("deleteCheck_" + worldName, 1, 0, new Runnable() {
			@Override
			public void run() {
				//Check if the server is empty and if so delete
				if (players.size() > 0) {        			
					if (emptyCount != 0) {
						emptyCount = 0;
					}
				} else {
					if (isSchematicSet) {				
						emptyCount++;
						
						if (emptyCount >= 10) {
							mainInstance.getTimerInstance().deleteTimer("scoreboard_" + uuid);
							delete();
						}
					}
				}
			}
		});
	}
	
	public Color colorFromChatColor(String colorName) {
		Color color = Color.WHITE;
		
		if (colorStrings.get(colorName) != null) {
			color = colorStrings.get(colorName);
		}
		return color;
	}
	/**
    * Cleans up the world and deletes the game
    */
	public void playWinAction(Player player) {
		PlayerData playerData = playerHandling.getPlayerData(player);
		RealmBase game = playerData.getCurrentRealm();
		
		for (ShopItem item : playerData.getShopItems()) {
			if (item.isEquipped()) {
				if (item.getUniqueId().contains("rainbow_scoreboard_winAction")) {
					shouldRainbowTitleText = true;
				} else if (item.getUniqueId().contains("fireworks_winAction")) {
					Color color = colorFromChatColor(item.getUniqueId().split("::")[1]);
					
					mainInstance.getTimerInstance().createRunnableTimer(player.getUniqueId() + "_fireworks", .7, 0, new Runnable() {						
						public void run() {
							if (game.isInServer(player)) {
								FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(color).withFade(color).with(Type.STAR).trail(true).build();
								
								if (!playerData.getSpectatingStatus()) {
									Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
									FireworkMeta fireworkMeta = firework.getFireworkMeta();
									fireworkMeta.addEffect(effect);
									fireworkMeta.setPower(2);
									firework.setFireworkMeta(fireworkMeta);
								}
							} else {
								mainInstance.getTimerInstance().deleteTimer(player.getUniqueId() + "_fireworks");
							}
						}
					});
				} else if (item.getUniqueId().contains("time_speed_up_winAction")) {
					setWorldTime = true;
					mainInstance.getTimerInstance().createRunnableTimer(player.getUniqueId() + "_timeWarp", .1, 0, new Runnable() {
						@Override
						public void run() {
							if (game.isInServer(player)) {
								if (world.getTime() < 23000L) {
									world.setTime(world.getTime() + 1000L);
								} else {
									world.setTime(0);
								}
							} else {
								mainInstance.getTimerInstance().deleteTimer(player.getUniqueId() + "_timeWarp");
							}
						}
					});
				}
			}
		}
	}
	public void onCreate() {}
	public void onDelete() {}
	/**
    * Cleans up the world and deletes the game
    */
	public void delete() {
		mainInstance.getTimerInstance().deleteTimer("deleteCheck_" + worldName);
		canUsersJoin = false;
		
		System.out.println("[Core] Cleaning up " + worldName);
		
		onDelete();
		
		System.out.println("[" + worldName + "] Clearing entities.");
		
		//Clear all entities
		for (Entity ent : world.getEntities()) ent.remove();
				
		System.out.println("[" + worldName + "] Undoing player changes.");
		
		//Undo player changes
		CoreProtectAPI cp = mainInstance.getCoreProtectAPI();
		List<String[]> lookup = cp.performLookup((int)(System.currentTimeMillis()-created), totalUsers, null, null, null, null, 0, null);
		
		if (lookup != null) {
			for (String[] value : lookup) {
				ParseResult result = cp.parseResult(value);
				Block block = new Location(world,result.getX(),result.getY(),result.getZ()).getBlock();
				
				if (block.getType() != Material.AIR) {
					block.setType(Material.AIR);
				}
			}
		}
		
		System.out.println("[" + worldName + "] Removing schematic.");
		
		//Clear schematic
		Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
			@Override
			public void run() {
				DiskOptimizedClipboard clipboard = new DiskOptimizedClipboard(map.getClearSchematicFile());
				clipboard.paste(new BukkitWorld(world), BlockVector3.at(-42, 30, -6),false,true,null);
				clipboard.close();
			}
		});
		
		mainInstance.getRealmHandlingInstance().removeGameFromList(id);
		System.out.println("[" + worldName + "] Finished.");
	}
	/**
    * Checks if the world has been generated. If so then it sets a schematic and allows entry to the game
    */
	public void checkWorld() {
		if (Bukkit.getWorld(worldName) != null && !isSchematicSet) {
			GameBase instance = this;
			world = Bukkit.getWorld(worldName);
			
			System.out.println("[Core] Starting creation of a new " + instance.getRealm().toString() + " game. Using world " + worldName);
			
			Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
				@Override
				public void run() {	
					//Set map
					map = GameMapInfo.getRandomMap(instance);
					
					System.out.println("[" + worldName + "] Using map " + map.getName());
					
					System.out.println("[" + worldName + "] Setting schematic.");
					
					//Set schematic
					DiskOptimizedClipboard clipboard = new DiskOptimizedClipboard(map.getSchematicFile());
					clipboard.paste(new BukkitWorld(world), BlockVector3.at(-42, 30, -6),false,false,null);
					clipboard.close();
					
					//Run create method in sub class
				    onCreate();
				    
				    mainInstance.getTimerInstance().createRunnableTimer("schematicWait_" + worldName, 2, 1, new Runnable() {
				    	@Override
				    	public void run() {				
				    		setSchemStatus(true);
				    	}
				    });
				    System.out.println("[" + worldName + "] Finished.");
				}
			});
			mainInstance.getTimerInstance().deleteTimer("worldCheck_" + worldName);
		}
	}
	/**
	* Print players xp summary
	* @param ply - Players summary to print
	* @param boolean - Should the players xp be saved
	*/
	public void printSummary(Player ply, boolean saveXP) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		HashMap<String,SummaryStat> stats = new HashMap<>();
		int totalXP = 0;
		String statsString = "";
		
		if (playerSummary.get(ply) != null) {
			stats = playerSummary.get(ply).getStats();
		}
		
		if (stats.size() > 0) {			
			for (Map.Entry<String,SummaryStat> stat : stats.entrySet()) {
				SummaryStatType type = stat.getValue().getType();
				
				if (!statsString.equals("")) {
					if (type == SummaryStatType.XP) {
						statsString += "\n" + stat.getKey() + ": " + stat.getValue().getValue() + "xp";						
						totalXP += stat.getValue().getValue();
					} else if (type == SummaryStatType.INTEGER) {
						statsString += "\n" + stat.getKey() + ": " + stat.getValue().getValue();	
					}
				} else {
					if (type == SummaryStatType.XP) {
						statsString = stat.getKey() + ": " + stat.getValue().getValue() + "xp";
						totalXP += stat.getValue().getValue();
					} else if (type == SummaryStatType.INTEGER) {
						statsString = stat.getKey() + ": " + stat.getValue().getValue();
					}
				}
			}
		}
		
		ply.sendMessage(
			ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\n" +
			ChatColor.BOLD + "GAME SUMMARY" +
			ChatColor.RESET + ChatColor.YELLOW + "\n \n" +
			(stats.size() > 0 ? statsString : "No stats received") + "\n \n" +
			(totalXP == 0 ? "" : "Total XP Received: " + totalXP + "\n") +
			ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A"
		);
		if (saveXP) {			
			pd.giveXP(realm, totalXP);
		}
	}
	/**
    * Forces the start of the game
    */
	public void forceStart() {
		forceStart = true;
	}
	/**
    * Get the status of the game
    * @return status of the game
    */
	public boolean isGameActive() {
		return gameBegun;
	}
	public String getPlayerKit(Player ply) { return null; };
	public void setPlayerKit(Player ply, String kit) {};
	public void playerDeath(Player killed,Player killer) {};
	/**
    * Game player join method
    * @param ply - Player joining the game
    */
	public void gameJoin(Player ply) {
		if (!totalUsers.contains(ply.getName())) {
			totalUsers.add(ply.getName());
		}
		
		playerJoin(ply);
		
		if (!playerHandling.getPlayerData(ply).getHiddenStatus()) {
			messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " joined");
		}
	}
	/**
    * Game player leave method
    * @param ply - Player leaving the game
    * @param showLeaveMessage - should a leave message be printed
    */
	public void gameLeave(Player ply, boolean showLeaveMessage) {
		DamagePlayer lastDamage = mainInstance.getPlayerHandlingInstance().getLastPlayerDamage(ply);
		
		if (players.contains(ply)) {			
			playerLeave(ply);
			if (playerSummary.get(ply) != null) {			
				playerSummary.remove(ply);
			}
			if (lastDamage != null && ((System.currentTimeMillis()-lastDamage.getTime())/1000L) < 1) {
				playerDeath(ply,lastDamage.getPlayer());
			}
			if (showLeaveMessage) {			
				messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " left");
			}
		}
	}
	public Location playerRespawn(Player ply) { return null; }
	/**
    * Get the close status of the game
    */
	public boolean isGameClosed() {
		return endStarted;
	}
	public void addStat(Player ply, String name, int value, SummaryStatType type) {
		if (playerSummary.get(ply) == null) {
			playerSummary.put(ply, new PlayerSummary());
		}
		
		playerSummary.get(ply).addStat(name, value, type);
	}
	
	//
	// GETTERS
	//
	
	public Realm getLobbyRealm() {
		return lobbyRealm;
	}
	public int getId() {
		return id;
	}
	public boolean getSchematicStatus() {
		return isSchematicSet;
	}
	public boolean getActiveStatus() {
		return active;
	}
	/**
    * Gets the given players spawn
    * @param ply - Player whose spawn you would like to get
    * @return location of spawn
    */
	public Location getPlayerSpawn(Player ply) {
		Location loc = null;
		
		teamloop:
		for (int team = 0; team < 2; team++) {
			HashMap<Location,Player> teamSpots = teamsSetup.get(team);
			
			for (Map.Entry<Location,Player> entry : teamSpots.entrySet()) {
				if (entry.getValue() == ply) {
					loc = entry.getKey();
					break teamloop;
				}
			}
		}
		return loc;
	}
	/**
    * Returns the index of the team the player is on
    * @param ply - Player whose team you would like to get
    * @return index of team
    */
	public int getPlayerTeam(Player ply) {
		int teamIndex = -1;
		
		teamloop:
		for (int team = 0; team < 2; team++) {
			HashMap<Location,Player> teamSpots = teamsSetup.get(team);
			
			for (Map.Entry<Location,Player> entry : teamSpots.entrySet()) {
				if (entry.getValue() == ply) {
					teamIndex = team;
					break teamloop;
				}
			}
		}
		return teamIndex;
	}
	/**
    * Get the time when this game was created
    * @return time when the game was created
    */
	public long getCreated() {
		return created;
	}
	/**
    * Get instance of this game
    */
	public GameBase getInstance() {
		return this;
	}
	/**
    * Get the join status of the game
    */
	public boolean getJoinStatus() {
		return canUsersJoin;
	}
	/**
    * Get the name of the map schematic
    */
	public MapBase getMap() {
		return map;
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
	public Location getRandomSpawnPoint() {
		Location vector = Bukkit.getWorld(worldName).getSpawnLocation();
		
		if (spawns.size() > 0) {
			Random r = new Random();
			vector = spawns.get(r.nextInt(spawns.size()));
		}
		return vector;
	}
	
	//
	// SETTERS
	//
	
	public void setSchemStatus(boolean status) {
		isSchematicSet = status;
	}
	public void setJoinStatus(boolean status) {
		canUsersJoin = status;
	}
}
