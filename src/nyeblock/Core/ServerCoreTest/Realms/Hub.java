package nyeblock.Core.ServerCoreTest.Realms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.TextAnimation;

@SuppressWarnings("serial")
public class Hub extends RealmBase {
	private PlayerHandling playerHandlingInstance;
	private World world = Bukkit.getWorld("world");
	private TextAnimation boardAnim = new TextAnimation("Hub board animation", new ArrayList<String>() {
		{
			add("§7NyeBlock");
			add("§bN§7yeBlock");
			add("§bNy§7eBlock");
			add("§bNye§7Block");
			add("§bNyeB§7lock");
			add("§bNyeBl§7ock");
			add("§bNyeBlo§7ck");
			add("§bNyeBloc§7k");
			add("§bNyeBlock");
			add("§7N§byeBlock");
			add("§7Ny§beBlock");
			add("§7Nye§bBlock");
			add("§7NyeB§block");
			add("§7NyeBl§bock");
			add("§7NyeBlo§bck");
			add("§7NyeBloc§bk");
		}
	}, 250);
	
	public Hub(Main mainInstance) {
		super(mainInstance);
		playerHandlingInstance = mainInstance.getPlayerHandlingInstance();
		realm = Realm.HUB;
		
		//Floating text
		Hologram spawnText = HologramsAPI.createHologram(mainInstance, new Location(Bukkit.getWorld("world"),-9.498,116,-7.467));
		spawnText.appendTextLine(ChatColor.YELLOW + "Welcome to " + ChatColor.BOLD + "NyeBlock");
		spawnText.appendTextLine(ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A");
		spawnText.appendTextLine(ChatColor.YELLOW + "Choose a game to play with the Game menu item!");
		spawnText.appendTextLine(ChatColor.YELLOW + "Don't feel like playing a game? Try out the parkour!");
		spawnText.appendItemLine(new ItemStack(Material.NETHER_STAR));
		
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("hub_functions", .5, 0, "mainFunctions", false, null, this);
		
		ArrayList<String> hints = new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Use the " + ChatColor.BOLD + "GAME MENU " + ChatColor.RESET.toString() + ChatColor.YELLOW + "item to browse the available game modes!");
			add(ChatColor.YELLOW + "Check out the parkour! Look for the signs showing where the parkour is.");
			add(ChatColor.YELLOW + "View the nyeblock website @ " + ChatColor.GREEN + "http://nyeblock.com/" + ChatColor.YELLOW + ".");
		}};
		
		//Hint messages timer
		mainInstance.getTimerInstance().createTimer2("hint_messages", 90, 0, new Runnable() {
			@Override
			public void run() {
				messageToAll(hints.get(new Random().nextInt(hints.size())));
			}
		});
	}
	
	/**
	* Get players
	*/
	public ArrayList<Player> getPlayers() {
		return players;
	}
	/**
	* Main functions ran for the hub
	*/
	public void mainFunctions() {
		int playersOnline = Bukkit.getOnlinePlayers().size();
		
		// Manage hub weather/time
		world.setTime(1000);
		if (world.hasStorm()) {
			world.setStorm(false);
		}
		for (Player ply : players) {
			PlayerData pd = playerHandlingInstance.getPlayerData(ply);
			HashMap<Integer, String> scores = new HashMap<>();
			
			//Scoreboard
			scores.put(5, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
			scores.put(4, ChatColor.RESET.toString() + ChatColor.RESET.toString());
			scores.put(3, ChatColor.YELLOW + "Players online: " + ChatColor.GREEN + playersOnline);
			scores.put(2, ChatColor.RESET.toString());
			scores.put(1, ChatColor.GREEN + "http://nyeblock.com/");
			pd.setScoreboardTitle(boardAnim.getMessage());
			pd.updateObjectiveScores(scores);
		}
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		
		pd.setItems();
		return Bukkit.getWorld("world").getSpawnLocation();
	}
	/**
	* When a player joins the hub
	*/
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		System.out.println("ran2");
		
		//Setup team
		pd.setScoreBoardTeams(null,Team.OptionStatus.NEVER);
		
		//Add players to proper scoreboard teams
		updateTeamsFromUserGroups();
		
		pd.setCurrentRealm(this);
	}
	/**
	* When a player leaves the hub
	*/
	public void playerLeave(Player ply, boolean showLeaveMessage, boolean moveToHub) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		
		//Clear scoreboard info
		pd.clearScoreboard();
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandlingInstance.getPlayerData(player);
			
			pd2.removePlayerFromTeam(pd.getUserGroup().toString(), ply);
		}
	}
}
