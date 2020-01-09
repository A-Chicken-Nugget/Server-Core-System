package nyeblock.Core.ServerCoreTest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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

import com.sk89q.worldedit.LocalConfiguration;

import nyeblock.Core.ServerCoreTest.Misc.VoidWorldGenerator;
import nyeblock.Core.ServerCoreTest.Misc.WorldManager;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.Hub;
import nyeblock.Core.ServerCoreTest.Realms.HubParkour;

public class Main extends JavaPlugin {
	private PlayerHandling playerHandling;
	private CommandHandling commandHandling;
	private GameHandling gameHandling;
	private DatabaseHandling databaseHandling;
	private TimerHandling timerHandling;
	private Hub hub;
	private HubParkour hubParkour;
	private CoreProtectAPI coreProtectAPI;
	
	//When this plugin is enabled, initialize important classes
	public void onEnable() {
		playerHandling = new PlayerHandling(this);
		gameHandling = new GameHandling(this);
		timerHandling = new TimerHandling();
		CoreProtect coreProtect = (CoreProtect) getServer().getPluginManager().getPlugin("CoreProtect");
		coreProtectAPI = coreProtect.getAPI();
		
		//Delete left over game worlds
		for(World world : Bukkit.getWorlds()) {
			String name = world.getName();
			
			if (!name.toString().matches("world")) {
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
		commandHandling = new CommandHandling(this);
		
		//Disable compass teleporting for ops
		LocalConfiguration worldEditConfig = WorldEdit.getInstance().getConfiguration();
		worldEditConfig.navigationWandMaxDistance = -1;
		
		//Initialize hub and hub parkour
		hub = new Hub(this);
		hubParkour = new HubParkour(this);
		
		//Set classes with event handlers
		getServer().getPluginManager().registerEvents(playerHandling, this);
		
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
	public void onDisable() {
		//Force people to leave in active games
		for (GameBase game : getGameInstance().getGames()) {
			if (game != null && game.getPlayersInRealm().size() > 0) {
				for (Player ply : game.getPlayersInRealm()) {
					game.leave(ply, false, null);
				}
				game.delete();
			}
		}
		//Delete created game worlds
		for(World world : Bukkit.getWorlds()) {
			String name = world.getName();
			
			if (!name.toString().matches("world")) {
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
	
	public GameHandling getGameInstance() {
		return gameHandling;
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
	public Hub getHubInstance() {
		return hub;
	}
	public HubParkour getHubParkourInstance() {
		return hubParkour;
	}
	public CoreProtectAPI getCoreProtectAPI() {
		return coreProtectAPI;
	}
}
