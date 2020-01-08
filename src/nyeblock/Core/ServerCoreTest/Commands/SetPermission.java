package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;

public class SetPermission extends CommandBase {
	private PlayerHandling playerHandling;
	
	public SetPermission(Main mainInstance) {
		super(mainInstance);
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 3) {
			if (!args[0].equalsIgnoreCase("<<_All_players_in_world_>>")) {
				Player player = Bukkit.getPlayerExact(args[0]);
				
				if (player != null) {					
					mainInstance.getPlayerHandlingInstance().getPlayerData(player).setPermission("nyeblock." + args[1],Boolean.parseBoolean(args[2]));
				} else {
					ply.sendMessage(ChatColor.RED + "Please enter a valid player!");
				}
			} else {
				for (Player player : ply.getWorld().getPlayers()) {
					playerHandling.getPlayerData(player).setPermission("nyeblock." + args[1],Boolean.parseBoolean(args[2]));
				}
			}
		} else {
			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
		}
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList<>();
		
		if (args.length == 1) {
			boolean foundPlayer = false;
			
			for (Player ply : player.getWorld().getPlayers()) {
				if (ply.getName().toLowerCase().contains(args[0].toLowerCase())) {	
					foundPlayer = true;
					autoCompletes.add(ply.getName());
				}
			}
			if (!foundPlayer) {
				for (Player ply : Bukkit.getOnlinePlayers()) {
					if (ply.getName().toLowerCase().contains(args[0].toLowerCase())) {	
						autoCompletes.add(ply.getName());
					}
				}
			}
			if ("<<_all_players_in_world_>>".contains(args[0].toLowerCase())) {	
				autoCompletes.add("<<_All_players_in_world_>>");
			}
		} else if (args.length == 2) {
			for (String permission : Arrays.asList("canBreakBlocks","canUseInventory","canPlaceBlocks","canBeDamaged","canDropItems","shouldDropItemsOnDeath","canLoseHunger","canMove")) {
				if (permission.toLowerCase().contains(args[1].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		} else if (args.length == 3) {
			for (String permission : Arrays.asList("true","false")) {
				if (permission.toLowerCase().contains(args[2].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		}
		
		return autoCompletes;
	}
}