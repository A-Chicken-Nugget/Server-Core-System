package nyeblock.Core.ServerCoreTest;

import java.io.File;
import java.util.HashMap;

//import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.LocalConfiguration;

import nyeblock.Core.ServerCoreTest.Games.Hub;
import nyeblock.Core.ServerCoreTest.Games.HubParkour;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class Main extends JavaPlugin {
	private PlayerHandling playerHandling;
	private CommandHandling commandHandling;
	private GameHandling gameHandling;
	private MultiverseCore multiverse;
	private DatabaseHandling databaseHandling;
	private TimerHandling timerHandling;
	private Hub hub;
	private HubParkour hubParkour;
	
	//When this plugin is enabled, initialize important classes
	public void onEnable() {
		playerHandling = new PlayerHandling(this);
		gameHandling = new GameHandling(this);
		multiverse = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
		timerHandling = new TimerHandling();
		
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
		commandHandling = new CommandHandling(this);
		
		//Disable multiverse chat tags
		multiverse.getMVConfig().setPrefixChat(false);
		
		//Disable compass teleporting for ops
		LocalConfiguration worldEditConfig = WorldEdit.getInstance().getConfiguration();
		worldEditConfig.navigationWandMaxDistance = -1;
		
		//Initialize hub and hub parkour
		hub = new Hub(this);
		hubParkour = new HubParkour(this);
		
		//Set classes with event handlers
		getServer().getPluginManager().registerEvents(playerHandling, this);
	}
	public void onDisable() {
		//Delete created game worlds
		MultiverseCore mv = multiverse;
		for(MultiverseWorld world : mv.getMVWorldManager().getMVWorlds()) {
			if (!world.getName().toString().matches("world")) {
				MVWorldManager wm = mv.getMVWorldManager();
				wm.deleteWorld(world.getName());
			}
		}
		//Save every players play time and xp
		DatabaseHandling dh = this.getDatabaseInstance();
		for (Player ply : Bukkit.getWorld("world").getPlayers()) {
			HashMap<Realm,Integer> realmXp = playerHandling.getPlayerData(ply).getRealmXp();
			
			dh.query("UPDATE users SET timePlayed = (timePlayed + " + ((System.currentTimeMillis()/1000L)-playerHandling.getPlayerData(ply).getTimeJoined()) + ") WHERE name = '" + ply.getName() + "'", 0, true);
			dh.query("UPDATE userXP SET kitpvp = " + realmXp.get(Realm.KITPVP) + ", skywars = " + realmXp.get(Realm.SKYWARS) + ", stepspleef = " + realmXp.get(Realm.STEPSPLEEF) + " WHERE uniqueId = '" + ply.getUniqueId() + "'", 0, true);
		}
	}
	
	public GameHandling getGameInstance() {
		return gameHandling;
	}
	public PlayerHandling getPlayerHandlingInstance() {
		return playerHandling;
	}
	public CommandHandling getCommandHandling() {
		return commandHandling;
	}
	public MultiverseCore getMultiverseInstance() {
		return multiverse;
	}
	public DatabaseHandling getDatabaseInstance() {
		return databaseHandling;
	}
	public TimerHandling getTimerInstance() {
		return timerHandling;
	}
	public Hub getHubInstance() {
		return hub;
	}
	public HubParkour getHubParkourInstance() {
		return hubParkour;
	}
}
