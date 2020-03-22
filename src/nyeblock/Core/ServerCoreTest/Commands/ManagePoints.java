package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class ManagePoints extends CommandBase {
	private PlayerHandling playerHandling;
	
	public ManagePoints(Main mainInstance) {
		super(mainInstance,
			"managePoints",
			"Give/take points from the specified player",
			"/managePoints",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 3) {
			if (!args[0].equalsIgnoreCase("<<_All_players_in_world_>>")) {
				Player player = Bukkit.getPlayerExact(args[0]);
				
				if (player != null) {
					try {					
						Integer amount = Integer.parseInt(args[2]);
						PlayerData pd = playerHandling.getPlayerData(player);	
						
						if (args[1].equalsIgnoreCase("give")) {
							pd.addPoints(amount);
						} else if (args[1].equalsIgnoreCase("take")) {
							pd.removePoints(amount);
						} else {
							ply.sendMessage(ChatColor.RED + "Please enter a valid action!");
						}
					} catch (Exception ex) {
						ply.sendMessage(ChatColor.RED + "Please enter a valid amount!");
					}
				} else {
					ply.sendMessage(ChatColor.RED + "Please enter a valid player!");
				}
			} else {
				try {					
					Integer amount = Integer.parseInt(args[2]);
					
					if (args[1].equalsIgnoreCase("give")) {
						for (Player player : ply.getWorld().getPlayers()) {							
							playerHandling.getPlayerData(player).addPoints(amount);
						}
					} else if (args[1].equalsIgnoreCase("take")) {
						for (Player player : ply.getWorld().getPlayers()) {							
							playerHandling.getPlayerData(player).removePoints(amount);
						}
					} else {
						ply.sendMessage(ChatColor.RED + "Please enter a valid action!");
					}
				} catch (Exception ex) {
					ply.sendMessage(ChatColor.RED + "Please enter a valid amount!");
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
			for (String permission : Arrays.asList("give","take")) {
				if (permission.toLowerCase().contains(args[1].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		}
		return autoCompletes;
	}
}
