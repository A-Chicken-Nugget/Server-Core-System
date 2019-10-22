package nyeblock.Core.ServerCoreTest;

import java.io.File;

//import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

public class Main extends JavaPlugin {
	private PlayerHandling playerHandling;
	private GameHandling gameHandling;
	private MultiverseCore multiverse;
	private DatabaseHandling databaseHandling;
	private TimerHandling timerHandling;
	
	//When this plugin is enabled, initialize important classes
	public void onEnable() {
		playerHandling = new PlayerHandling(this);
		gameHandling = new GameHandling(this);
		multiverse = (MultiverseCore) getServer().getPluginManager().getPlugin("Multiverse-Core");
		timerHandling = new TimerHandling();
		//Set spawn point for hub world
		Bukkit.getWorld("world").setSpawnLocation(new Location(Bukkit.getWorld("world"),-9.510, 113, -11.445));
		Bukkit.getWorld("world").loadChunk(-10, 113);
		
		//Handle config file
		File configFile = new File(this.getDataFolder(), "config.yml");
		if (configFile.exists()) {
			FileConfiguration config = this.getConfig();
			databaseHandling = new DatabaseHandling(config.getString("mysql.host"),config.getString("mysql.database"),config.getInt("mysql.port"),config.getString("mysql.username"),config.getString("mysql.password"));
		} else {
			FileConfiguration config = this.getConfig();
			config.addDefault("mysql.host","host");
			config.addDefault("mysql.database","database");
			config.addDefault("mysql.port",3306);
			config.addDefault("mysql.username", "user");
			config.addDefault("mysql.password", "password");
			config.options().copyDefaults(true);
			this.saveConfig();
			
			databaseHandling = new DatabaseHandling(config.getString("mysql.host"),config.getString("mysql.database"),config.getInt("mysql.port"),config.getString("mysql.username"),config.getString("mysql.password"));
		}
		
		getServer().getPluginManager().registerEvents(playerHandling, this);
		getServer().getPluginManager().registerEvents(gameHandling, this);
	}
	public void onDisable() {
		MultiverseCore mv = multiverse;
		for(MultiverseWorld world : mv.getMVWorldManager().getMVWorlds()) {
			if (!world.getName().toString().matches("world|world_nether|world_the_end")) {
				MVWorldManager wm = mv.getMVWorldManager();
				wm.deleteWorld(world.getName());
			}
		}
	}
	
	public GameHandling getGameInstance() {
		return gameHandling;
	}
	public PlayerHandling getPlayerHandlingInstance() {
		return playerHandling;
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
}
