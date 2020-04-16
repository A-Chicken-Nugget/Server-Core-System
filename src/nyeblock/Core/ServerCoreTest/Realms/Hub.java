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
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Team;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Items.HidePlayers;
import nyeblock.Core.ServerCoreTest.Menus.GameMenu;
import nyeblock.Core.ServerCoreTest.Menus.NyeBlockMenu;
import nyeblock.Core.ServerCoreTest.Menus.MyProfileMenu;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.TextAnimation;

@SuppressWarnings("serial")
public class Hub extends RealmBase {
	private PlayerHandling playerHandlingInstance;
	private World world = Bukkit.getWorld("world");
	private TextAnimation boardAnimation;
	
	public Hub(Main mainInstance) {
		super(mainInstance,Realm.HUB);
		playerHandlingInstance = mainInstance.getPlayerHandlingInstance();
		boardAnimation = new TextAnimation(mainInstance,new ArrayList<String>() {{
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
		}},.25);
		
		//Scoreboard
		scoreboard = new Runnable() {
			@Override
			public void run() {
				int playersOnline = Bukkit.getOnlinePlayers().size();
				
				// Manage hub weather/time
				world.setTime(1000);
				if (world.hasStorm()) {
					world.setStorm(false);
				}
				for (Player ply : players) {
					PlayerData pd = playerHandlingInstance.getPlayerData(ply);
					
					if (pd != null) {						
						HashMap<Integer, String> scores = new HashMap<>();
						
						//Scoreboard
						scores.put(7, ChatColor.GRAY + new SimpleDateFormat("MM/dd/yyyy").format(new Date()));
						scores.put(6, ChatColor.RESET.toString() + ChatColor.RESET.toString() + ChatColor.RESET.toString());
						scores.put(5, ChatColor.YELLOW + "Players online: " + ChatColor.GREEN + playersOnline);
						scores.put(4, ChatColor.RESET.toString() + ChatColor.RESET.toString());
						scores.put(3, ChatColor.YELLOW + "Points: " + ChatColor.GREEN + (pd.getPoints() == -1 ? "Loading..." : pd.getPoints()));
						scores.put(2, ChatColor.RESET.toString());
						scores.put(1, ChatColor.GREEN + "http://nyeblock.com/");
						pd.setScoreboardTitle(boardAnimation.getMessage());
						pd.updateObjectiveScores(scores);
					}
				}
			}
		};
		
		//Floating text
		Hologram spawnText = HologramsAPI.createHologram(mainInstance, new Location(Bukkit.getWorld("world"),-9.498,116,-7.467));
		spawnText.appendTextLine(ChatColor.YELLOW + "Welcome to " + ChatColor.BOLD + "NyeBlock");
		spawnText.appendTextLine(ChatColor.YELLOW + "\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A\u268A");
		spawnText.appendTextLine(ChatColor.YELLOW + "Choose a game to play with the Game menu item!");
		spawnText.appendTextLine(ChatColor.YELLOW + "Don't feel like playing a game? Try out the parkour!");
		spawnText.appendItemLine(new ItemStack(Material.NETHER_STAR));
		
		//Main functions timer
		mainInstance.getTimerInstance().createMethodTimer("hub_functions", .5, 0, "mainFunctions", false, null, this);
		
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
	* Get players
	*/
	public ArrayList<Player> getPlayers() {
		return players;
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
    * Set the players permissions
    */
	public void setDefaultPermissions(Player player) {
		PermissionAttachment permissions = mainInstance.getPlayerHandlingInstance().getPlayerData(player).getPermissionAttachment();
		
		permissions.setPermission("nyeblock.canPlaceBlocks", false);
		permissions.setPermission("nyeblock.canBreakBlocks", false);
		permissions.setPermission("nyeblock.canUseInventory", false);
		permissions.setPermission("nyeblock.shouldDropItemsOnDeath", false);
		permissions.setPermission("nyeblock.canDamage", false);
		permissions.setPermission("nyeblock.canBeDamaged", false);
		permissions.setPermission("nyeblock.canTakeFallDamage", false);
		permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
		permissions.setPermission("nyeblock.canDropItems", false);
		permissions.setPermission("nyeblock.canLoseHunger", false);
		permissions.setPermission("nyeblock.canSwapItems", false);
		permissions.setPermission("nyeblock.canMove", true);
	}
	/**
    * Set the players items
    */
	public void setItems(Player player) {
		//Profile/Stats menu
		MyProfileMenu profileStatsMenu = new MyProfileMenu(mainInstance,player);
		ItemStack ssm = profileStatsMenu.give();
		player.getInventory().setItem(2, ssm);
		
		//Game Menu
		GameMenu hubMenu = new GameMenu(mainInstance,player);
		ItemStack hm = hubMenu.give();
		player.getInventory().setItem(4, hm);
		player.getInventory().setHeldItemSlot(4);
		
		//Hide players
		HidePlayers hidePlayers = new HidePlayers(mainInstance,player);
		ItemStack hp = hidePlayers.give();
		player.getInventory().setItem(6, hp);
		
		//NyeBlock
		NyeBlockMenu nyeBlockMenu = new NyeBlockMenu(mainInstance,player);
		ItemStack nbm = nyeBlockMenu.give();
		player.getInventory().setItem(8, nbm);
	}
	/**
	* When a player respawns
	* @param ply - Player that is being respawned
	* @return location to respawn the player
	*/
	public Location playerRespawn(Player ply) {
		setItems(ply);
		
		return Bukkit.getWorld("world").getSpawnLocation();
	}
	/**
	* When a player joins the hub
	*/
	public void playerJoin(Player ply) {
		PlayerData pd = playerHandlingInstance.getPlayerData(ply);
		
		//Setup team
		pd.setScoreBoardTeams(null,Team.OptionStatus.NEVER);
		
		//Add players to proper scoreboard teams
		updateTeamsFromUserGroups();
		
		pd.setCurrentRealm(this);
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
