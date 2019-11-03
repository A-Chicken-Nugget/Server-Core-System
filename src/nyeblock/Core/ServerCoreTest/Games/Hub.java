package nyeblock.Core.ServerCoreTest.Games;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;
import nyeblock.Core.ServerCoreTest.Misc.TextAnimation;

@SuppressWarnings({"deprecation","serial"})
public class Hub {
	private PlayerHandling playerHandlingInstance;
	private World world = Bukkit.getWorld("world");
	private ArrayList<Player> players = new ArrayList<Player>();
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
		playerHandlingInstance = mainInstance.getPlayerHandlingInstance();
		
		//Floating text
		Hologram spawnText = HologramsAPI.createHologram(mainInstance, new Location(Bukkit.getWorld("world"),-9.498,116,-7.467));
		spawnText.appendTextLine(ChatColor.YELLOW + "Welcome to " + ChatColor.BOLD + "NyeBlock");
		spawnText.appendTextLine(ChatColor.YELLOW + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		spawnText.appendTextLine(ChatColor.YELLOW + "Choose a game to play with the Game menu item!");
		spawnText.appendTextLine(ChatColor.YELLOW + "Don't feel like playing a game? Try out the parkour!");
		spawnText.appendItemLine(new ItemStack(Material.NETHER_STAR));
		
		//Main functions timer
		mainInstance.getTimerInstance().createTimer("hub_functions", .5, 0, "mainFunctions", false, null, this);
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
	* When a player joins the hub
	*/
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		
		//Add player
		players.add(ply);
		
		//Setup team
		pd.setScoreBoardTeams(new String[] {"default"});
		
		//Add player to proper team
		if (pd.getUserGroup() == UserGroup.ADMIN) {
			pd.addPlayerToTeam("admin", ply);
		} else if (pd.getUserGroup() == UserGroup.MODERATOR) {
			pd.addPlayerToTeam("moderator", ply);
		} else {
			pd.addPlayerToTeam("default", ply);
		}
		
		//Add players to teams
		for (Player player : players) {
			PlayerData pd2 = playerHandlingInstance.getPlayerData(player);
			
			if (player != ply) {
				//Update joining player team
				if (pd2.getUserGroup() == UserGroup.ADMIN) {
					pd.addPlayerToTeam("admin", player);
				} else if (pd2.getUserGroup() == UserGroup.MODERATOR) {
					pd.addPlayerToTeam("moderator", player);
				} else {					
					pd.addPlayerToTeam("default", player);
				}
				
				//Update current players teams
				if (pd.getUserGroup() == UserGroup.ADMIN) {
					pd2.addPlayerToTeam("admin", ply);
				} else if (pd.getUserGroup() == UserGroup.MODERATOR) {
					pd2.addPlayerToTeam("moderator", ply);
				} else {
					pd2.addPlayerToTeam("default", ply);
				}
			}
		}
	}
	/**
	* When a player leaves the hub
	*/
	public void playerLeave(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		
		//Clear scoreboard info
		pd.clearScoreboard();
		
		//Remove player
		players.remove(ply);
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandlingInstance.getPlayerData(player);
			
			pd2.removePlayerFromTeam("default", ply);
		}
	}
}
