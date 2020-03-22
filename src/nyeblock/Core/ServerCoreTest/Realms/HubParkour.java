package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.DatabaseHandling;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Items.HidePlayers;
import nyeblock.Core.ServerCoreTest.Items.ReturnToStart;
import nyeblock.Core.ServerCoreTest.Menus.ParkourMenu;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("serial")
public class HubParkour extends RealmBase {
	private Main mainInstance;
	private PlayerHandling playerHandling;
	private DatabaseHandling databaseInstance;
	private World world = Bukkit.getWorld("world");
	// Player data
	private HashMap<String,Integer> playerCheckPoints = new HashMap<>();
	private HashMap<String,Long> playerTimes = new HashMap<>();
	private HashMap<String,Long> playerBestCompetitiveTimes = new HashMap<>();
	private HashMap<String,Long> playerBestNormalTimes = new HashMap<>();
	private ArrayList<Player> validPlayers = new ArrayList<>();
	// Zone points
	private Vector parkourZonePoint1 = new Vector(-21, 130, 28);
	private Vector parkourZonePoint2 = new Vector(19, 112, 80);
	private Vector timeResetZonePoint1 = new Vector(-6, 113, 39);
	private Vector timeResetZonePoint2 = new Vector(-21, 118, 28);
	private Vector restartZonePoint1 = new Vector(-21, 113, 29);
	private Vector restartZonePoint2 = new Vector(19, 114.5, 80);
	private ArrayList<Location> checkPointLocations = new ArrayList<Location>() {{
		add(new Location(world, -7.5, 113.5, 37.5).getBlock().getLocation());
		add(new Location(world, 6.5, 121.5, 51.5).getBlock().getLocation());
		add(new Location(world, -3.5, 122.5, 62.5).getBlock().getLocation());
	}};
	private ArrayList<Float[]> checkPointDirections = new ArrayList<Float[]>() {{
		add(new Float[] {-90F,0F});
		add(new Float[] {70F,0F});
		add(new Float[] {-10F,0F});
	}};
	private Location finishPoint = new Location(world, -13.5, 124.5, 71.5).getBlock().getLocation();
	//Hologram
	private Hologram top5Text;
	private ArrayList<HashMap<String, String>> top5CompetitiveList;
	private ArrayList<HashMap<String, String>> top5NormalList;
	private boolean displayCompTop5 = false;

	public HubParkour(Main mainInstance) {
		super(mainInstance,Realm.PARKOUR);
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		databaseInstance = mainInstance.getDatabaseInstance();
		
		//Floating text
		top5Text = HologramsAPI.createHologram(mainInstance, new Location(Bukkit.getWorld("world"),-9.5,116,42.5));
		top5Text.appendTextLine(ChatColor.YELLOW + "Top 5 Fastest " + ChatColor.BOLD + "COMPETITIVE " + ChatColor.RESET + ChatColor.YELLOW + "Parkour times");
		top5Text.appendTextLine(ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A");
		top5Text.appendTextLine(ChatColor.YELLOW + "Loading...");
		top5Text.appendItemLine(new ItemStack(Material.CLOCK));

		// Zones timer
		mainInstance.getTimerInstance().createMethodTimer("parkour_functions", .15, 0, "mainFunctions", false, null, this);
		
		//Timers used to update the top 5 text. One if is asynchrous query and one to update the actual text
		mainInstance.getTimerInstance().createRunnableTimer("parkour_getTop5", 60, 0, new Runnable() {
			@Override
			public void run() {
				Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
		            @Override
		            public void run() {       
		            	top5CompetitiveList = mainInstance.getDatabaseInstance().query("SELECT name,time FROM parkourTimes WHERE type = 0 ORDER BY time LIMIT 5", false);
		            	top5NormalList = mainInstance.getDatabaseInstance().query("SELECT name,time FROM parkourTimes WHERE type = 1 ORDER BY time LIMIT 5", false);
		            }
				});
			}
		});
		mainInstance.getTimerInstance().createRunnableTimer("parkour_textFlip", 30, 0, new Runnable() {
			@Override
			public void run() {				
				if (displayCompTop5) {
					displayCompTop5 = false;
					
					top5Text.clearLines();
					top5Text.appendTextLine(ChatColor.YELLOW + "Top 5 Fastest " + ChatColor.BOLD + "COMPETITIVE " + ChatColor.RESET + ChatColor.YELLOW + "Parkour times");
					top5Text.appendTextLine(ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A");
					
					if (top5CompetitiveList != null) {						
						if (top5CompetitiveList.size() > 0) {						
							for (int i = 0; i < top5CompetitiveList.size(); i++) {				
								HashMap<String, String> queryData = top5CompetitiveList.get(i);
								
								top5Text.appendTextLine(ChatColor.YELLOW.toString() + (i + 1) + ".) " + queryData.get("name") + " (" + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(Long.parseLong(queryData.get("time"))) + ChatColor.YELLOW + ")");
							}
						} else {
							top5Text.appendTextLine(ChatColor.YELLOW + "None");
						}
					} else {
						top5Text.appendTextLine(ChatColor.YELLOW + "Loading...");
					}
					top5Text.appendItemLine(new ItemStack(Material.CLOCK));
				} else {
					displayCompTop5 = true;
					
					top5Text.clearLines();
					top5Text.appendTextLine(ChatColor.YELLOW + "Top 5 Fastest " + ChatColor.BOLD + "NORMAL " + ChatColor.RESET + ChatColor.YELLOW + "Parkour times");
					top5Text.appendTextLine(ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A");
					
					if (top5NormalList != null) {		
						if (top5NormalList.size() > 0) {
							for (int i = 0; i < top5NormalList.size(); i++) {				
								HashMap<String, String> queryData = top5NormalList.get(i);
								
								top5Text.appendTextLine(ChatColor.YELLOW.toString() + (i + 1) + ".) " + queryData.get("name") + " (" + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(Long.parseLong(queryData.get("time"))) + ChatColor.YELLOW + ")");
							}
						} else {
							top5Text.appendTextLine(ChatColor.YELLOW + "None");
						}
					} else {
						top5Text.appendTextLine(ChatColor.YELLOW + "Loading...");
					}
					top5Text.appendItemLine(new ItemStack(Material.CLOCK));
				}
			}
		});

		// Scoreboard timer
		mainInstance.getTimerInstance().createMethodTimer("parkour_scoreboard", .5, 0, "setScoreboard", false, null, this);
	}

	/**
	 * Get the players in parkour
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	/**
	 * Sets the scoreboard
	 */
	public void setScoreboard() {
		for (Player ply : players) {
			int pos = 1;
			PlayerData pd = playerHandling.getPlayerData(ply);
			boolean parkourMode = Boolean.valueOf(pd.getCustomDataKey("parkour_mode"));
			HashMap<Integer, String> scores = new HashMap<Integer, String>();

			scores.put(pos++, ChatColor.GREEN + "http://nyeblock.com/");
			scores.put(pos++, ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Best time: " + ChatColor.GREEN 
					+ (parkourMode ? (playerBestCompetitiveTimes.get(ply.getName()) == 0L ? "00:00.000"
							: new SimpleDateFormat("mm:ss.SSS")
								.format(playerBestCompetitiveTimes.get(ply.getName())))
					: (playerBestNormalTimes.get(ply.getName()) == 0L ? "00:00.000"
							: new SimpleDateFormat("mm:ss.SSS")
								.format(playerBestNormalTimes.get(ply.getName())))));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++,
					ChatColor.YELLOW + "Time: " + ChatColor.GREEN
							+ (playerTimes.get(ply.getName()) == 0L ? "00:00.000"
									: new SimpleDateFormat("mm:ss.SSS")
											.format(System.currentTimeMillis() - playerTimes.get(ply.getName()))));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Mode: " + ChatColor.GREEN
					+ (parkourMode ? "Competitive" : "Normal"));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
			pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "PARKOUR");
			pd.updateObjectiveScores(scores);
		}
	}
	/**
	 * Main functions ran for parkour
	 */
	public void mainFunctions() {
		for (Player ply : world.getPlayers()) {
			Location loc = ply.getLocation();

			if (ply.getLocation() != null) {
				PlayerData pd = playerHandling.getPlayerData(ply);

				// Check if player is in the parkour zones
				if (Toolkit.playerInArea(loc.toVector(), parkourZonePoint1, parkourZonePoint2)) {
					if (!players.contains(ply) && !validPlayers.contains(ply)) {
						validPlayers.add(ply);
						
						//Leave the hub
						mainInstance.getHubInstance().leave(ply,false,Realm.PARKOUR);
					}
					if (Toolkit.playerInArea(loc.toVector(), timeResetZonePoint1, timeResetZonePoint2)) {
						if (playerTimes.get(ply.getName()) != 0L) {
							playerTimes.put(ply.getName(), 0L);
						}
					} else {
						if (playerTimes.get(ply.getName()) == 0L) {
							playerTimes.put(ply.getName(), System.currentTimeMillis());
						}
						if (Toolkit.playerInArea(loc.toVector(), restartZonePoint1, restartZonePoint2)) {
							if (playerCheckPoints.get(ply.getName()) != -1) {
								Location tempPos = checkPointLocations.get(playerCheckPoints.get(ply.getName()));
								Float[] tempDirection = checkPointDirections.get(playerCheckPoints.get(ply.getName()));
								ply.teleport(new Location(world,tempPos.getX()+.5,tempPos.getY()+1,tempPos.getZ()+.5,tempDirection[0],tempDirection[1]));
							} else {
								Location tempPos = checkPointLocations.get(0);
								Float[] tempDirection = checkPointDirections.get(0);
								ply.teleport(new Location(world,tempPos.getX()+.5,tempPos.getY()+1,tempPos.getZ()+.5,tempDirection[0],tempDirection[1]));
								ply.sendMessage(ChatColor.YELLOW + "Your time will start once you leave this block.");
								playerCheckPoints.put(ply.getName(), 0);
							}
						}
					}
					Location loc2 = loc.subtract(0, .5, 0).getBlock().getLocation();
					
					if (loc2.equals(checkPointLocations.get(0))) {
						if (playerCheckPoints.get(ply.getName()) != 0) {
							ply.sendMessage(ChatColor.YELLOW + "Your time will start once you leave this block.");
							playerCheckPoints.put(ply.getName(), 0);
						} else {
							if (playerTimes.get(ply.getName()) != 0L) {
								playerTimes.put(ply.getName(), 0L);
							}
						}
					} else if (loc2.equals(checkPointLocations.get(1))) {
						if (playerCheckPoints.get(ply.getName()) != 1 && !Boolean.valueOf(pd.getCustomDataKey("parkour_mode"))) {
							ply.sendMessage(ChatColor.YELLOW + "You reached check point #1");	
							playerCheckPoints.put(ply.getName(), 1);
							ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1f);
						}
					} else if (loc2.equals(checkPointLocations.get(2))) {
						if (playerCheckPoints.get(ply.getName()) != 2 && !Boolean.valueOf(pd.getCustomDataKey("parkour_mode"))) {
							ply.sendMessage(ChatColor.YELLOW + "You reached check point #2");	
							playerCheckPoints.put(ply.getName(), 2);
							ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1f);
						}
					} else if (loc2.equals(finishPoint)) {
						long time = System.currentTimeMillis() - playerTimes.get(ply.getName());
						
						ply.playSound(ply.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 10, 1f);
						Location tempPos = checkPointLocations.get(0);
						Float[] tempDirection = checkPointDirections.get(0);
						ply.teleport(new Location(world,tempPos.getX()+.5,tempPos.getY()+1,tempPos.getZ()+.5,tempDirection[0],tempDirection[1]));
						playerCheckPoints.put(ply.getName(), 0);
						
						if (Boolean.valueOf(pd.getCustomDataKey("parkour_mode"))) {
							 if (playerBestCompetitiveTimes.get(ply.getName()) != 0L && time < playerBestCompetitiveTimes.get(ply.getName())) {
								 ply.sendMessage(ChatColor.YELLOW 
										 + "You finished the Competitive parkour! You beat your previous time  " 
										 + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(playerBestCompetitiveTimes.get(ply.getName())) 
										 + ChatColor.YELLOW + " with " + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(time));
								 playerBestCompetitiveTimes.put(ply.getName(),time);
							 } else {
								 ply.sendMessage(ChatColor.YELLOW + "You finished the Competitive parkour! Your time was " + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(time));
								 playerBestCompetitiveTimes.put(ply.getName(),time);
							 }
							 ArrayList<HashMap<String, String>> query = databaseInstance.query("SELECT time FROM parkourTimes WHERE type = 0 AND uniqueId = '" + ply.getUniqueId() + "'", false);
							 
							 if (query.size() > 0) {
								 HashMap<String, String> queryData = query.get(0);
								 
								 if (time < Long.parseLong(queryData.get("time"))) {
									 databaseInstance.query("UPDATE parkourTimes SET time = " + time + " WHERE type = 0 AND uniqueId = '" + ply.getUniqueId() + "'", true);
								 }
							 } else {
								 databaseInstance.query("INSERT INTO parkourTimes (type,uniqueId,name,time) VALUES (0,'" + ply.getUniqueId() + "','" + ply.getName() + "'," + time + ")", true);
							 }
						 } else {
							 if (playerBestNormalTimes.get(ply.getName()) != 0L && time < playerBestNormalTimes.get(ply.getName())) {
								 ply.sendMessage(ChatColor.YELLOW 
										 + "You finished the Normal parkour! You beat your previous time  " 
										 + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(playerBestNormalTimes.get(ply.getName())) 
										 + ChatColor.YELLOW + " with " + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(time));
								 playerBestNormalTimes.put(ply.getName(),time);
							 } else {
								 ply.sendMessage(ChatColor.YELLOW + "You finished the Normal parkour! Your time was " + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(time));
								 playerBestNormalTimes.put(ply.getName(),time);
							 }
							 ArrayList<HashMap<String, String>> query = databaseInstance.query("SELECT time FROM parkourTimes WHERE type = 1 AND uniqueId = '" + ply.getUniqueId() + "'", false);
							 
							 if (query.size() > 0) {
								 HashMap<String, String> queryData = query.get(0);
								 
								 if (time < Long.parseLong(queryData.get("time"))) {
									 databaseInstance.query("UPDATE parkourTimes SET time = " + time + " WHERE type = 1 AND uniqueId = '" + ply.getUniqueId() + "'", true);
								 }
							 } else {
								 databaseInstance.query("INSERT INTO parkourTimes (type,uniqueId,name,time) VALUES (1,'" + ply.getUniqueId() + "','" + ply.getName() + "'," + time + ")", true);
							 }
						 }
					}
				} else {
					if (players.contains(ply)) {
						mainInstance.getHubParkourInstance().leave(ply, false, null);
					}
				}
			}
		}
	}
	public void setItems(Player player) {
		//Parkour menu
		ParkourMenu parkourMenu = new ParkourMenu(mainInstance,player);
		ItemStack pm = parkourMenu.give();
		player.getInventory().setItem(4, pm);
		
		//Hide players
		HidePlayers hidePlayers = new HidePlayers(mainInstance,player);
		ItemStack hp = hidePlayers.give();
		player.getInventory().setItem(6, hp);
		
		//Go to start
		ReturnToStart startItem = new ReturnToStart(mainInstance,player);
		ItemStack si = startItem.give();
		player.getInventory().setItem(2, si);
	}
	/**
    * When a player clicks the return to start item
    */
	public void goToStart(Player ply) {
		Location tempPos = checkPointLocations.get(0);
		Float[] tempDirection = checkPointDirections.get(0);
		ply.teleport(new Location(world,tempPos.getX()+.5,tempPos.getY()+1,tempPos.getZ()+.5,tempDirection[0],tempDirection[1]));
		playerCheckPoints.put(ply.getName(), 0);
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		setItems(ply);
		Location tempPos = checkPointLocations.get(0);
		Float[] tempDirection = checkPointDirections.get(0);
		playerCheckPoints.put(ply.getName(), 0);
		
		return new Location(world,tempPos.getX()+.5,tempPos.getY()+1,tempPos.getZ()+.5,tempDirection[0],tempDirection[1]);
	}
	/**
	* When a player joins the hub
	*/
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Setup player
		playerTimes.put(ply.getName(), 0L);
		playerCheckPoints.put(ply.getName(),-1);
		playerBestCompetitiveTimes.put(ply.getName(), 0L);
		playerBestNormalTimes.put(ply.getName(), 0L);
		pd.setCustomDataKey("parkour_mode", "false");
		
		//Setup team
		pd.setScoreBoardTeams(null,Team.OptionStatus.NEVER);
		
		//Add player to proper team
		pd.addPlayerToTeam(pd.getUserGroup().toString(), ply);
		
		pd.setCurrentRealm(this);
		
		setItems(ply);
		
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
		

		//Get their previous best times
		Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
            @Override
            public void run() {             	
            	ArrayList<HashMap<String, String>> query = databaseInstance.query("SELECT time FROM parkourTimes WHERE type = 0 AND uniqueId = '" + ply.getUniqueId() + "'", false);
            	if (query.size() > 0) {
            		HashMap<String, String> queryData = query.get(0);
            		
            		playerBestCompetitiveTimes.put(ply.getName(), Long.parseLong(queryData.get("time")));
            	}
            	query = databaseInstance.query("SELECT time FROM parkourTimes WHERE type = 1 AND uniqueId = '" + ply.getUniqueId() + "'", false);
            	if (query.size() > 0) {
            		HashMap<String, String> queryData = query.get(0);
            		
            		playerBestNormalTimes.put(ply.getName(), Long.parseLong(queryData.get("time")));
            	}
            }
		});
	}
	/**
    * Handle when a player leaves parkour
    */
	public void playerLeave(Player ply) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		playerCheckPoints.remove(ply.getName());
		playerTimes.remove(ply.getName());
		validPlayers.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
				
		if (!mainInstance.getTimerInstance().timerExists("leave_" + ply.getUniqueId())) {			
			mainInstance.getHubInstance().join(ply, false);
		}
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandling.getPlayerData(player);
			
			pd2.removePlayerFromTeam(pd.getUserGroup().toString(), ply);
		}
	}
}
