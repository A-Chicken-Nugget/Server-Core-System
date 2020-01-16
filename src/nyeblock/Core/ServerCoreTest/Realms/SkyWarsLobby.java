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
import nyeblock.Core.ServerCoreTest.Items.QueueGame;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Menus.HubMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.LevelXPBar;
import nyeblock.Core.ServerCoreTest.Misc.TextAnimation;

@SuppressWarnings("serial")
public class SkyWarsLobby extends RealmBase {
	private PlayerHandling playerHandlingInstance;
	private World world = Bukkit.getWorld("SkyWarsLobby");
	private TextAnimation boardAnim = new TextAnimation("scoreboardTitle_" + uuid, new ArrayList<String>() {
		{
			add("§eSky Wars");
			add("§6S§eky Wars");
			add("§eS§6k§ey Wars");
			add("§eSk§6y §eWars");
			add("§eSky §6W§ears");
			add("§eSky W§6a§ers");
			add("§eSky Wa§6r§es");
			add("§eSky War§6s");
			add("§eSky Wars");
			add("§6Sky Wars");
			add("§eSky Wars");
			add("§6Sky Wars");
		}
	}, null);
	
	public SkyWarsLobby(Main mainInstance) {
		super(mainInstance,Realm.SKYWARS_LOBBY);
		playerHandlingInstance = mainInstance.getPlayerHandlingInstance();
		
		//Scoreboard
		scoreboard = new Runnable() {
			@Override
			public void run() {
				for (Player ply : players) {
					PlayerData pd = playerHandlingInstance.getPlayerData(ply);
					HashMap<Integer, String> scores = new HashMap<>();
//					int level = pd.getLevel(Realm.SKYWARS);
					
					//Scoreboard
//					scores.put(8, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(5, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
					scores.put(4, ChatColor.RESET.toString() + ChatColor.RESET.toString());
					scores.put(3, ChatColor.YELLOW + "Level: " + ChatColor.GREEN + pd.getLevel(Realm.SKYWARS));
					scores.put(2, ChatColor.RESET.toString());
					scores.put(1, ChatColor.GREEN + "http://nyeblock.com/");
					pd.setScoreboardTitle(ChatColor.BOLD + boardAnim.getMessage().toString());
					pd.updateObjectiveScores(scores);
				}
			}
		};
		
		//Floating text
//		Hologram spawnText = HologramsAPI.createHologram(mainInstance, new Location(Bukkit.getWorld("world"),-9.498,116,-7.467));
//		spawnText.appendTextLine(ChatColor.YELLOW + "Welcome to " + ChatColor.BOLD + "NyeBlock");
//		spawnText.appendTextLine(ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A");
//		spawnText.appendTextLine(ChatColor.YELLOW + "Choose a game to play with the Game menu item!");
//		spawnText.appendTextLine(ChatColor.YELLOW + "Don't feel like playing a game? Try out the parkour!");
//		spawnText.appendItemLine(new ItemStack(Material.NETHER_STAR));
		
		//Main functions timer
		mainInstance.getTimerInstance().createMethodTimer("skyWarsLobby_functions", .5, 0, "mainFunctions", false, null, this);
		
		ArrayList<String> hints = new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Use the " + ChatColor.BOLD + "GAME MENU " + ChatColor.RESET.toString() + ChatColor.YELLOW + "item to browse the available game modes!");
			add(ChatColor.YELLOW + "Check out the parkour! Look for the signs showing where the parkour is.");
			add(ChatColor.YELLOW + "View the nyeblock website @ " + ChatColor.GREEN + "http://nyeblock.com/" + ChatColor.YELLOW + ".");
		}};
		
		//Hint messages timer
		mainInstance.getTimerInstance().createRunnableTimer("hint_messages", 90, 0, new Runnable() {
			@Override
			public void run() {
				messageToAll(hints.get(new Random().nextInt(hints.size())));
			}
		});
	}
	
	/**
	* Main functions ran for the hub
	*/
	public void mainFunctions() {
		// Manage hub weather/time
		world.setTime(1000);
		if (world.hasStorm()) {
			world.setStorm(false);
		}
	}
	/**
	* Sets the players items
	* @param ply - Player to give the items to
	*/
	public void setItems(Player player) {
		player.getInventory().clear();
		
		//Queue item
		QueueGame queueGame = new QueueGame(mainInstance,player);
		ItemStack qg = queueGame.give();
		player.getInventory().setItem(4, qg);
		player.getInventory().setHeldItemSlot(4);
		
		//Return to hub
		ReturnToHub returnToHub = new ReturnToHub(mainInstance,player);
		player.getInventory().setItem(8, returnToHub.give());
		
		//Sky Wars Shop
//		HubMenu hubMenu = new HubMenu(mainInstance,player);
//		ItemStack hm = hubMenu.give();
//		player.getInventory().setItem(4, hm);
//		player.getInventory().setHeldItemSlot(4);
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		setItems(ply);
		
		return new Location(world,-85.5,89,490.5);
	}
	/**
	* When a player joins the hub
	*/
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		
		ply.teleport(new Location(world,-85.5,89,490.5));
		
		setItems(ply);
		
		//Setup team
		pd.setScoreBoardTeams(null,Team.OptionStatus.NEVER);
		
		//Add players to proper scoreboard teams
		updateTeamsFromUserGroups();
	}
	/**
	* When a player leaves the hub
	*/
	public void playerLeave(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		
		//Remove players from teams
		for (Player player : players) {
			PlayerData pd2 = playerHandlingInstance.getPlayerData(player);
			
			pd2.removePlayerFromTeam(pd.getUserGroup().toString(), ply);
		}
	}
}
