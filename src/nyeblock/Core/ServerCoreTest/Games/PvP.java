package nyeblock.Core.ServerCoreTest.Games;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class PvP extends GameBase {
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
	
	public PvP(Main mainInstance, String worldName, int duration, int maxPlayers) {
		super(mainInstance,worldName);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.worldName = worldName;
		realm = Realm.SKYWARS;
		this.duration = duration;
		this.maxPlayers = maxPlayers;
		
		//Scoreboard timer
		mainInstance.getTimerInstance().createTimer("score_" + worldName, .5, 0, "setScoreboard", false, null, this);
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("main_" + worldName, 1, 0, "mainFunctions", false, null, this);
	}
}
