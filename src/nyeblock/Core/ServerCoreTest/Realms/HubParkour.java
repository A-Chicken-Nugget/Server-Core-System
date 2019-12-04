package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.DatabaseHandling;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Items.HidePlayers;
import nyeblock.Core.ServerCoreTest.Items.ParkourMenu;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

@SuppressWarnings({"deprecation","serial"})
public class HubParkour extends RealmBase {
	private Main mainInstance;
	private PlayerHandling playerHandling;
	private DatabaseHandling databaseInstance;
	private World world = Bukkit.getWorld("world");
	// Player data
	private ArrayList<Player> players = new ArrayList<>();
	private HashMap<String,Integer> playerCheckPoints = new HashMap<>();
	private HashMap<String,Long> playerTimes = new HashMap<>();
	private HashMap<String,Long> playerBestTimes = new HashMap<>();
	// Zone points
	private Vector parkourZonePoint1 = new Vector(31.412, 113, 8);
	private Vector parkourZonePoint2 = new Vector(78.510, 130, -20.700);
	private Vector timeResetZonePoint1 = new Vector(46.166, 111.5, -3.136);
	private Vector timeResetZonePoint2 = new Vector(29.541, 125, -20.541);
	private Vector restartZonePoint1 = new Vector(46.130, 113, 8);
	private Vector restartZonePoint2 = new Vector(78.865, 114, -20.700);
	private ArrayList<Location> checkPointLocations = new ArrayList<Location>() {{
		add(new Location(world, 43.473, 113.5, -11.459).getBlock().getLocation());
		add(new Location(world, 59.513, 116.5, -4.504).getBlock().getLocation());
		add(new Location(world, 57.436, 119.5, -15.534).getBlock().getLocation());
	}};
	private ArrayList<Float[]> checkPointDirections = new ArrayList<Float[]>() {{
		add(new Float[] {-89.4F,-2.3F});
		add(new Float[] {154.1F,2.4F});
		add(new Float[] {-38.8F,1.4F});
	}};
	private Location finishPoint = new Location(world, 68.505, 114.5, -8.509).getBlock().getLocation();
	//Hologram
	private Hologram top5Text;
	private ArrayList<HashMap<String, String>> top5ListText;

	public HubParkour(Main mainInstance) {
		super(mainInstance);
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		databaseInstance = mainInstance.getDatabaseInstance();
		
		//Floating text
		top5Text = HologramsAPI.createHologram(mainInstance, new Location(Bukkit.getWorld("world"),40.015,116,-16.005));
		top5Text.appendTextLine(ChatColor.YELLOW + "Top 5 Fastest Parkour times");
		top5Text.appendTextLine(ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A");
		top5Text.appendTextLine(ChatColor.YELLOW + "Loading...");
		top5Text.appendItemLine(new ItemStack(Material.CLOCK));

		// Zones timer
		mainInstance.getTimerInstance().createTimer("parkour_functions", .15, 0, "mainFunctions", false, null, this);
		
		//Timers used to update the top 5 text. One if is asynchrous query and one to update the actual text
		mainInstance.getTimerInstance().createTimer("parkour_getTop5", 60, 0, "getTop5", false, null, this);
		mainInstance.getTimerInstance().createTimer("parkour_setTop5", 65, 0, "setTop5", false, null, this);

		// Scoreboard timer
		mainInstance.getTimerInstance().createTimer("parkour_scoreboard", .5, 0, "setScoreboard", false, null, this);
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
			HashMap<Integer, String> scores = new HashMap<Integer, String>();

			if (!ply.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName()
					.equalsIgnoreCase(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "PARKOUR")) {
				pd.setScoreboardTitle(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "PARKOUR");
			}

			scores.put(pos++, ChatColor.GREEN + "http://nyeblock.com/");
			scores.put(pos++, ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.YELLOW + "Best time: " + ChatColor.GREEN 
					+ (playerBestTimes.get(ply.getName()) == 0L ? "00:00.000"
									: new SimpleDateFormat("mm:ss.SSS")
											.format(playerBestTimes.get(ply.getName()))));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++,
					ChatColor.YELLOW + "Time: " + ChatColor.GREEN
							+ (playerTimes.get(ply.getName()) == 0L ? "00:00.000"
									: new SimpleDateFormat("mm:ss.SSS")
											.format(System.currentTimeMillis() - playerTimes.get(ply.getName()))));
			scores.put(pos++, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(pos++, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
			
			pd.updateObjectiveScores(scores);
			
			//Check hide players status
			if (Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
				for (Player ply2 : world.getPlayers()) {
					if (ply.canSee(ply2) && ply != ply2) {
						ply.hidePlayer(ply2);
					}
				}
			} else {
				for (Player ply2 : world.getPlayers()) {
					if (!ply.canSee(ply2) && ply != ply2) {
						ply.showPlayer(ply2);
					}
				}
			}
			
			//Set gamemode
			ply.setGameMode(GameMode.ADVENTURE);
		}
	}
	/**
	 * Query the top players in parkour
	 */
	public void getTop5() {
		Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
            @Override
            public void run() {       
            	top5ListText = mainInstance.getDatabaseInstance().query("SELECT name,time FROM parkourTimes ORDER BY time LIMIT 5", 2, false);
            }
		});
	}
	/**
	 * Set the top players in parkour
	 */
	public void setTop5() {
		if (top5ListText.size() > 0) {
			top5Text.clearLines();
			top5Text.appendTextLine(ChatColor.YELLOW + "Top 5 Fastest Parkour times");
			top5Text.appendTextLine(ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A");
			
			for (int i = 0; i < top5ListText.size(); i++) {				
				HashMap<String, String> queryData = top5ListText.get(i);
				
				top5Text.appendTextLine(ChatColor.YELLOW.toString() + (i + 1) + ".) " + queryData.get("name") + " (" + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(Long.parseLong(queryData.get("time"))) + ChatColor.YELLOW + ")");
			}
			top5Text.appendItemLine(new ItemStack(Material.CLOCK));
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
					if (!players.contains(ply)) {
						//Setup player
						players.add(ply);
						playerTimes.put(ply.getName(), 0L);
						playerCheckPoints.put(ply.getName(),-1);
						playerBestTimes.put(ply.getName(), 0L);
						
						//Leave the hub
						mainInstance.getHubInstance().playerLeave(ply,false,false);
						
						pd.setCurrentGame(this);
						
						//Show players in game
						for (Player ply2 : Bukkit.getOnlinePlayers()) {
							if (players.contains(ply2)) {
								if (!Boolean.parseBoolean(pd.getCustomDataKey("hide_players"))) {
									ply.showPlayer(mainInstance,ply2);
								}
							} else {								
								if (ply.canSee(ply2)) {								
									ply.hidePlayer(mainInstance,ply2);
								}
							}
						}
						
						//Clear scoreboard
						pd.clearScoreboard();
						
						//Setup team
						pd.setScoreBoardTeams(new String[] {"default"});
						
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
						
						//Get their previous best time
						ArrayList<HashMap<String, String>> query = databaseInstance.query("SELECT time FROM parkourTimes WHERE uniqueId = '" + ply.getUniqueId() + "'", 1, false);
						if (query.size() > 0) {
							HashMap<String, String> queryData = query.get(0);
							
							playerBestTimes.put(ply.getName(), Long.parseLong(queryData.get("time")));
						}

						// Give parkour items
						ply.getInventory().clear();
						ParkourMenu parkourMenu = new ParkourMenu();
						ItemStack pm = parkourMenu.give();
						ply.getInventory().setItem(4, pm);
						HidePlayers hidePlayers = new HidePlayers(mainInstance, ply);
						ItemStack hp = hidePlayers.give();
						ply.getInventory().setItem(6, hp);
						ItemStack startItem = new ItemStack(Material.LEAD);
						ItemMeta startItemMeta = startItem.getItemMeta();
						startItemMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Return to Start" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
						startItemMeta.setLocalizedName("parkour_start");
						startItem.setItemMeta(startItemMeta);
						ply.getInventory().setItem(2, startItem);
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
					} else {
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
								 if (playerBestTimes.get(ply.getName()) != 0L && time < playerBestTimes.get(ply.getName())) {
									 ply.sendMessage(ChatColor.YELLOW 
											 + "You finished the parkour! You beat your previous time  " 
											 + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(playerBestTimes.get(ply.getName())) 
											 + ChatColor.YELLOW + " with " + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(time));
									 playerBestTimes.put(ply.getName(),time);
								 } else {
									 ply.sendMessage(ChatColor.YELLOW + "You finished the parkour! Your time was " + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(time));
									 playerBestTimes.put(ply.getName(),time);
								 }
								 ArrayList<HashMap<String, String>> query = databaseInstance.query("SELECT time FROM parkourTimes WHERE uniqueId = '" + ply.getUniqueId() + "'", 1, false);
								 
								 if (query.size() > 0) {
									 HashMap<String, String> queryData = query.get(0);
									 
									 if (time < Long.parseLong(queryData.get("time"))) {
										 databaseInstance.query("UPDATE parkourTimes SET time = " + time + " WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);
									 }
								 } else {
									 databaseInstance.query("INSERT INTO parkourTimes (uniqueId,name,time) VALUES ('" + ply.getUniqueId() + "','" + ply.getName() + "'," + time + ")", 0, true);
								 }
							 } else {
								 ply.sendMessage(ChatColor.YELLOW + "You finished the parkour! Your time was " + ChatColor.GREEN + new SimpleDateFormat("mm:ss.SSS").format(time));
							 }
						} else {
							if (Toolkit.playerInArea(loc.toVector(), timeResetZonePoint1, timeResetZonePoint2)) {
								if (playerTimes.get(ply.getName()) != 0L) {
									playerTimes.put(ply.getName(), 0L);
								}
							} else {
								if (playerTimes.get(ply.getName()) == 0L) {
									playerTimes.put(ply.getName(), System.currentTimeMillis());
								}
							}
						}
					}
				} else {
					if (players.contains(ply)) {
						players.remove(ply);
						playerCheckPoints.remove(ply.getName());
						playerTimes.remove(ply.getName());

						// Give default hub items
						pd.setItems();
						
						//Clear scoreboard
						pd.clearScoreboard();
						
						mainInstance.getHubInstance().playerJoin(ply);
					}
				}
			}
		}
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
    * Handle when a player leaves parkour
    */
	public void playerLeave(Player ply) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		//Remove player from players list
		players.removeAll(new ArrayList<Player>() {{
			add(ply);
		}});
		
		//Remove player from the spots list
		playerCheckPoints.remove(ply.getName());
		
		//Remove player from the spots list
		playerTimes.remove(ply.getName());
		
		//Clear teams
		pd.clearScoreboard();
		
		//Remove player
		players.remove(ply);
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandling.getPlayerData(player);
			
			pd2.removePlayerFromTeam("default", ply);
		}
	}
}
