package nyeblock.Core.ServerCoreTest;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Items.HubMenu;
import nyeblock.Core.ServerCoreTest.Items.KitSelector;
import nyeblock.Core.ServerCoreTest.Items.ReturnToHub;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

@SuppressWarnings("unused")
public class PlayerData {
	private Main mainInstance;
	private Player player;
	private int points;
	private int xp;
	private double timePlayed;
	private String ip;
	private UserGroup userGroup;
	private PermissionAttachment permissions;
	private Realm realm = Realm.HUB;
	//Scoreboard
	private Scoreboard board;
	private Objective objective;
	private Team team;
	
	@SuppressWarnings("deprecation")
	public PlayerData(Main mainInstance, Player ply, int points, int xp, double timePlayed, String ip, UserGroup userGroup) {
		this.mainInstance = mainInstance;
		this.player = ply;
		permissions = new PermissionAttachment(mainInstance, ply);
		this.points = points;
		this.xp = xp;
		this.timePlayed = timePlayed;
		this.ip = ip;
		this.userGroup = userGroup;
		ScoreboardManager sbm = Bukkit.getScoreboardManager();
		board = sbm.getNewScoreboard();
		team = board.registerNewTeam("user");
		objective = board.registerNewObjective("scoreboard", "");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "NYEBLOCK (ALPHA)");
		ply.setScoreboard(board);
		setPermissions();
		setItems();
	}
	
	//Set player permissions depending on their realm
	public void setPermissions() {
		if (realm == Realm.HUB) {
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canUseInventory", false);
			permissions.setPermission("nyeblock.canDamage", false);
			permissions.setPermission("nyeblock.canBeDamaged", false);
			permissions.setPermission("nyeblock.canTakeFallDamage", false);
			permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
			permissions.setPermission("nyeblock.canDropItems", false);
			permissions.setPermission("nyeblock.canLoseHunger", false);
//			permissions.setPermission("nyeblock.showRunningParticles", true);
//			team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
		} else if (realm == Realm.KITPVP) {
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canUseInventory", false);
			permissions.setPermission("nyeblock.canDamage", true);
			permissions.setPermission("nyeblock.canBeDamaged", true);
			permissions.setPermission("nyeblock.canTakeFallDamage", true);
			permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
			permissions.setPermission("nyeblock.dropItems", false);
			permissions.setPermission("nyeblock.canLoseHunger", false);
		} else if (realm == Realm.STEPSPLEEF) {
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canUseInventory", false);
			permissions.setPermission("nyeblock.canDamage", false);
			permissions.setPermission("nyeblock.canBeDamaged", true);
			permissions.setPermission("nyeblock.canTakeFallDamage", false);
			permissions.setPermission("nyeblock.tempNoDamageOnFall", false);
			permissions.setPermission("nyeblock.canDropItems", false);
			permissions.setPermission("nyeblock.canLoseHunger", false);
		} else if (realm == Realm.SKYWARS) {
			permissions.setPermission("nyeblock.canBreakBlocks", false);
			permissions.setPermission("nyeblock.canUseInventory", false);
			permissions.setPermission("nyeblock.canDamage", true);
			permissions.setPermission("nyeblock.canBeDamaged", true);
			permissions.setPermission("nyeblock.canTakeFallDamage", true);
			permissions.setPermission("nyeblock.tempNoDamageOnFall", true);
			permissions.setPermission("nyeblock.canDropItems", false);
			permissions.setPermission("nyeblock.canLoseHunger", false);
		}
	}
	//Set a specific player permission
	public void setPermission(String permission, boolean value) {
		permissions.setPermission(permission, value);
	}
	//Give the player default items based on their realm
	public void setItems() {
		player.getInventory().clear();
		
		if (realm == Realm.HUB) {
			//Menu
			HubMenu hubMenu = new HubMenu();
			ItemStack hm = hubMenu.give();
			player.getInventory().setItem(4, hm);
			player.getInventory().setHeldItemSlot(4);
		} else if (realm == Realm.KITPVP) {
			//Return to hub
			ReturnToHub returnToHub = new ReturnToHub();
			player.getInventory().setItem(8, returnToHub.give());
			//Select kit
			KitSelector selectKit = new KitSelector(Realm.KITPVP);
			player.getInventory().setItem(7, selectKit.give());
			//Sword
			ItemStack sword = new ItemStack(Material.IRON_SWORD);
			sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			player.getInventory().setItem(0, sword);
			player.getInventory().setHeldItemSlot(0);
			//Golden Apples
			ItemStack goldenApples = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,1);
			player.getInventory().setItem(1, goldenApples);
			//Armor
			ItemStack[] armor = {
				new ItemStack(Material.IRON_BOOTS),
				new ItemStack(Material.IRON_LEGGINGS),
				new ItemStack(Material.IRON_CHESTPLATE),
				new ItemStack(Material.IRON_HELMET)
			};
			armor[0].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[1].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[2].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			armor[3].addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			player.getInventory().setArmorContents(armor);
		} else if (realm == Realm.STEPSPLEEF) {
			//Return to hub
			ReturnToHub returnToHub = new ReturnToHub();
			player.getInventory().setItem(8, returnToHub.give());
		} else if (realm == Realm.SKYWARS) {
			//Select kit
			KitSelector selectKit = new KitSelector(Realm.SKYWARS);
			player.getInventory().setItem(4, selectKit.give());
			//Return to hub
			ReturnToHub returnToHub = new ReturnToHub();
			player.getInventory().setItem(8, returnToHub.give());
		}
	}
//	//Set scoreboard team
//	public void setScoreboardTeam(String team) {
//		for
//	}
	//Set a players realm
	public void setRealm(Realm realm, boolean updatePermissions, boolean updateItems) {
		this.realm = realm;
		if (updatePermissions) {			
			setPermissions();
		}
		if (updateItems) {			
			setItems();
		}
		//Remove potion effects
		for(PotionEffect effect : player.getActivePotionEffects())
		{
		    player.removePotionEffect(effect.getType());
		}
	}
	//Get the players current realm
	public Realm getRealm() {
		return realm;
	}
	//Gets the players user group
	public UserGroup getUserGroup() {
		return userGroup;
	}
	//Get the value of a specific permission
	public boolean getPermission(String permission) {
		boolean value = false;
		
		for (Map.Entry<String, Boolean> entry : permissions.getPermissions().entrySet()) {
			if (permission.equalsIgnoreCase(entry.getKey())) {
				value = entry.getValue();
			}
		}
		return value;
	}
	//Get the title of the players scoreboard
	public String getObjectiveName() {
		return objective.getName();
	}
	//Get the players current scoreboard objective
	public Objective getObjective() {
		return objective;
	}
	//Set the title of the players scoreboard
	public void setObjectiveName(String name) {
		for (String s : board.getEntries()) {			
			board.resetScores(s);
		}
		objective.setDisplayName(name);
	}
	//Update the players scoreboard text
	public void updateObjectiveScores(HashMap<Integer,String> scores) {
		for (Map.Entry<Integer, String> entry : scores.entrySet()) {
			Miscellaneous.updateScore(objective, entry.getKey(), entry.getValue());
		}
	}
}
