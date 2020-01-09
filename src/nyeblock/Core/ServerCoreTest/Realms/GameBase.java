package nyeblock.Core.ServerCoreTest.Realms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.boydti.fawe.object.clipboard.DiskOptimizedClipboard;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;

import net.coreprotect.CoreProtectAPI;
import net.coreprotect.CoreProtectAPI.ParseResult;
import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.SchematicHandling;
import nyeblock.Core.ServerCoreTest.Maps.MapBase;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

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
	//Player data
	protected ArrayList<HashMap<Location,Player>> teamsSetup = new ArrayList<>();
	protected HashMap<Player,HashMap<String,Integer>> playerXP = new HashMap<>();
	protected ArrayList<String> totalUsers = new ArrayList<>();
	//Game points
	protected ArrayList<Location> spawns = new ArrayList<>();
	//Etc
	protected int duration;
	protected long startTime;
	protected boolean active = false;
	protected int emptyCount = 0;
	protected boolean canUsersJoin = true;
	protected boolean forceStart = false;
	protected int playTimeCount = 0;
	protected boolean isSchematicSet = false;
	protected boolean endStarted = false;
	
	public GameBase(Main mainInstance, String worldName) {
		super(mainInstance);
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		
		//Create timer to check when the game world is created
		mainInstance.getTimerInstance().createMethodTimer("worldCheck_" + worldName, 1, 0, "checkWorld", true, null, this);
		
		//Create timer to delete world when empty
		mainInstance.getTimerInstance().createRunnableTimer("deleteCheck_" + worldName, 1, 0, new Runnable() {
			@Override
			public void run() {
				if (players.size() > 0) {        			
					if (emptyCount != 0) {
						emptyCount = 0;
					}
				} else {
					if (isSchematicSet) {				
						emptyCount++;
						
						if (emptyCount >= 10) {
							delete();
						}
					}
				}
			}
		});
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
		
		mainInstance.getGameInstance().removeGameFromList(id);
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
		String xp = "";
		int totalXP = 0;
		for (Map.Entry<String,Integer> entry2 : playerXP.get(ply).entrySet()) {
			if (!xp.equals("")) {
				xp += "\n" + entry2.getKey() + ": " + entry2.getValue();
				totalXP += entry2.getValue();
			} else {
				xp = entry2.getKey() + ": " + entry2.getValue();
				totalXP += entry2.getValue();
			}
		}
		
		ply.sendMessage(
			ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\n" +
			ChatColor.BOLD + "GAME SUMMARY" +
			ChatColor.RESET + ChatColor.YELLOW + "\n \n" +
			(totalXP == 0 ? "No XP received" : xp) + "\n \n" +
			"Total XP Received: " + totalXP + "\n" +
			ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A"
		);
		if (saveXP) {
			if (realm == Realm.PVP) {
				PvP game = (PvP)this;
				
				playerHandling.getPlayerData(ply).giveXP(game.getPvPMode(), game.getPvPType(), totalXP);
			} else {				
				playerHandling.getPlayerData(ply).giveXP(realm, totalXP);
			}
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
		playerXP.put(ply, new HashMap<String,Integer>());
		
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
		playerXP.remove(ply);
		
		playerLeave(ply);
		
		if (showLeaveMessage) {			
			messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " left");
		}
	}
	public Location playerRespawn(Player ply) { return null; }
	/**
    * Get the close status of the game
    */
	public boolean isGameClosed() {
		return endStarted;
	}
	
	//
	// GETTERS
	//
	
	public int getId() {
		return id;
	}
	public boolean getSchematicStatus() {
		return isSchematicSet;
	}
	public boolean getActiveStatus() {
		return active;
	}
	public void giveXP(Player ply, String type, int amount) {
		HashMap<String,Integer> xpStats = playerXP.get(ply);
		
		if (xpStats.get(type) != null) {
			xpStats.put(type,xpStats.get(type)+amount);
		} else {
			xpStats.put(type,amount);
		}
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
