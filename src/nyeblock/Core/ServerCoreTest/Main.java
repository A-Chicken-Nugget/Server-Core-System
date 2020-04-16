package nyeblock.Core.ServerCoreTest;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.WorldEdit;

import de.xxschrandxx.awm.api.config.WorldData;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldedit.LocalConfiguration;

import nyeblock.Core.ServerCoreTest.Misc.CustomNPCManager;
import nyeblock.Core.ServerCoreTest.Misc.Enums.LogType;
import nyeblock.Core.ServerCoreTest.Misc.VoidWorldGenerator;
import nyeblock.Core.ServerCoreTest.Misc.WorldManager;
import nyeblock.Core.ServerCoreTest.Realms.Hub;
import nyeblock.Core.ServerCoreTest.Realms.HubParkour;
import nyeblock.Core.ServerCoreTest.Realms.KitPvPLobby;
import nyeblock.Core.ServerCoreTest.Realms.PvPLobby;
import nyeblock.Core.ServerCoreTest.Realms.SkyWarsLobby;
import nyeblock.Core.ServerCoreTest.Realms.StepSpleefLobby;

public class Main extends JavaPlugin {
	//Instances
	private PlayerHandling playerHandling;
	private CommandHandling commandHandling;
	private RealmHandling realmHandling;
	private DatabaseHandling databaseHandling;
	private TimerHandling timerHandling;
	private KitHandling kitHandling;
	private AchievementHandling achievementHandling;
	private Hub hub;
	private HubParkour hubParkour;
	private CoreProtectAPI coreProtectAPI;
	private CustomNPCManager customNPCManager;
	private ProtocolManager protocolManager;
	//Lobby Instances
	private KitPvPLobby kitPvPLobby;
	private SkyWarsLobby skyWarsLobby;
	private StepSpleefLobby stepSpleefLobby;
	private PvPLobby pvPLobby;
	
	//Logs
	private boolean logPlayTime = false;
	
	//When this plugin is enabled
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		commandHandling = new CommandHandling(this);
		playerHandling = new PlayerHandling(this);
		commandHandling.setCommands();
		realmHandling = new RealmHandling(this);
		timerHandling = new TimerHandling();
		kitHandling = new KitHandling(this);
		achievementHandling = new AchievementHandling();
		CoreProtect coreProtect = (CoreProtect) getServer().getPluginManager().getPlugin("CoreProtect");
		coreProtectAPI = coreProtect.getAPI();
		customNPCManager = new CustomNPCManager(this);
		
		//Delete left over game worlds
		for(World world : Bukkit.getWorlds()) {
			String name = world.getName();
			
			if (name.contains("gameWorld")) {
				Bukkit.getServer().unloadWorld(name,false);
				WorldManager.deleteWorld(new File("./worlds/" + name));
				new File("./plugins/Async-WorldManager/worldconfigs/" + name + ".yml").delete();
			}
		}
		
		//Set spawn point for hub world
		Bukkit.getWorld("world").setSpawnLocation(new Location(Bukkit.getWorld("world"),-9.548, 113, -11.497));
		Bukkit.getWorld("world").loadChunk(-10, 113);
		
		//Handle config file
		File configFile = new File(this.getDataFolder(), "config.yml");
		FileConfiguration config;
		if (configFile.exists()) {
			config = this.getConfig();
		} else {
			config = this.getConfig();
			config.addDefault("mysql.host","host");
			config.addDefault("mysql.database","database");
			config.addDefault("mysql.port",3306);
			config.addDefault("mysql.username", "user");
			config.addDefault("mysql.password", "password");
			config.options().copyDefaults(true);
			this.saveConfig();
		}
		databaseHandling = new DatabaseHandling(this,config.getString("mysql.host"),config.getString("mysql.database"),config.getInt("mysql.port"),config.getString("mysql.username"),config.getString("mysql.password"));
		
		//Disable compass teleporting for ops
		LocalConfiguration worldEditConfig = WorldEdit.getInstance().getConfiguration();
		worldEditConfig.navigationWandMaxDistance = -1;
		
		//Initialize hub and hub parkour
		hub = new Hub(this);
		hubParkour = new HubParkour(this);
		
		//Set classes with event handlers
		getServer().getPluginManager().registerEvents(playerHandling, this);
		
		//
		// CREATE/LOAD WORLDS
		//
		Main mainInstance = this;
		
		//SkyWars Lobby
		WorldData skl = new WorldData();
		skl.setWorldName("SkyWarsLobby");
		skl.setEnviroment(Environment.NORMAL);
		skl.setWorldType(WorldType.FLAT);
		skl.setGenerator(new VoidWorldGenerator());
		skl.setKeepSpawnInMemory(true);
		skl.setAutoSave(false);
		de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(skl);
		timerHandling.createRunnableTimer("skyWarsLobby_worldWait", 1, 0, new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getWorld("SkyWarsLobby") != null) {
					timerHandling.deleteTimer("skyWarsLobby_worldWait");
					skyWarsLobby = new SkyWarsLobby(mainInstance);
				}
			}
		});
		
		//StepSpleef Lobby
		WorldData ssl = new WorldData();
		ssl.setWorldName("StepSpleefLobby");
		ssl.setEnviroment(Environment.NORMAL);
		ssl.setWorldType(WorldType.FLAT);
		ssl.setGenerator(new VoidWorldGenerator());
		ssl.setKeepSpawnInMemory(true);
		ssl.setAutoSave(false);
		de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(ssl);
		timerHandling.createRunnableTimer("stepSpleefLobby_worldWait", 1, 0, new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getWorld("StepSpleefLobby") != null) {
					timerHandling.deleteTimer("stepSpleefLobby_worldWait");
					stepSpleefLobby = new StepSpleefLobby(mainInstance);
				}
			}
		});
		
		//KitPvP Lobby
		WorldData kpl = new WorldData();
		kpl.setWorldName("KitPvPLobby");
		kpl.setEnviroment(Environment.NORMAL);
		kpl.setWorldType(WorldType.FLAT);
		kpl.setGenerator(new VoidWorldGenerator());
		kpl.setKeepSpawnInMemory(true);
		kpl.setAutoSave(false);
		de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(kpl);
		timerHandling.createRunnableTimer("kitPvPLobby_worldWait", 1, 0, new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getWorld("KitPvPLobby") != null) {
					timerHandling.deleteTimer("kitPvPLobby_worldWait");
					kitPvPLobby = new KitPvPLobby(mainInstance);
				}
			}
		});
		
		//PvP Lobby
		WorldData ppl = new WorldData();
		ppl.setWorldName("PvPLobby");
		ppl.setEnviroment(Environment.NORMAL);
		ppl.setWorldType(WorldType.FLAT);
		ppl.setGenerator(new VoidWorldGenerator());
		ppl.setKeepSpawnInMemory(true);
		ppl.setAutoSave(false);
		de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(ppl);
		timerHandling.createRunnableTimer("pvPLobby_worldWait", 1, 0, new Runnable() {
			@Override
			public void run() {
				if (Bukkit.getWorld("PvPLobby") != null) {
					timerHandling.deleteTimer("pvPLobby_worldWait");
					pvPLobby = new PvPLobby(mainInstance);
				}
			}
		});
		
		//Create/load game worlds
		for (int i = 0; i < 10; i++) {			
			WorldData wd = new WorldData();
			wd.setWorldName("gameWorld_" + (i+1));
			wd.setEnviroment(Environment.NORMAL);
			wd.setWorldType(WorldType.FLAT);
			wd.setGenerator(new VoidWorldGenerator());
			wd.setKeepSpawnInMemory(false);
			wd.setAutoSave(false);
			de.xxschrandxx.awm.api.worldcreation.fawe.faweworld(wd);
		}
	}
	//When this plugin is disabled
	public void onDisable() {
		//Delete created game worlds
		for(World world : Bukkit.getWorlds()) {
			String name = world.getName();
			
			if (name.contains("gameWorld")) {
				Bukkit.getServer().unloadWorld(name,false);
				WorldManager.deleteWorld(new File("./worlds/" + name));
				new File("./plugins/Async-WorldManager/worldconfigs/" + name + ".yml").delete();
			}
		}
		
		//Save every players play time and xp
		for (Player ply : Bukkit.getWorld("world").getPlayers()) {
			playerHandling.getPlayerData(ply).saveToDB();
		}
	}
	
	//Log a message to the server console
	//TODO Make this log to db as well
	public void logMessage(LogType type, String message) {
		System.out.println(type.getColor() + "[" + type.getTag() + "] " + message);
	}
	
	//
	// GETTERS
	//
	
	public RealmHandling getRealmHandlingInstance() {
		return realmHandling;
	}
	public PlayerHandling getPlayerHandlingInstance() {
		return playerHandling;
	}
	public CommandHandling getCommandHandling() {
		return commandHandling;
	}
	public DatabaseHandling getDatabaseInstance() {
		return databaseHandling;
	}
	public TimerHandling getTimerInstance() {
		return timerHandling;
	}
	public KitHandling getKitHandlingInstance() {
		return kitHandling;
	}
	public AchievementHandling getAchievementHandlingInstance() {
		return achievementHandling;
	}
	public Hub getHubInstance() {
		return hub;
	}
	public HubParkour getHubParkourInstance() {
		return hubParkour;
	}
	public CoreProtectAPI getCoreProtectAPI() {
		return coreProtectAPI;
	}
	public CustomNPCManager getCustomNPCManagerInstance() {
		return customNPCManager;
	}
	public ProtocolManager getProtocolManagerInstance() {
		return protocolManager;
	}
	public SkyWarsLobby getSkyWarsLobby() {
		return skyWarsLobby;
	}
	public StepSpleefLobby getStepSpleefLobby() {
		return stepSpleefLobby;
	}
	public KitPvPLobby getKitPvPLobby() {
		return kitPvPLobby;
	}
	public PvPLobby getPvPLobby() {
		return pvPLobby;
	}
	
	public void setLogPlayTime(boolean ye) {
		logPlayTime = ye;
	}
	public boolean getLogPlayTime() {
		return logPlayTime;
	}
}
