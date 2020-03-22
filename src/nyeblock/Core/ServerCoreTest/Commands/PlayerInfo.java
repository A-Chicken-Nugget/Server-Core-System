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

public class PlayerInfo extends CommandBase {
	private PlayerHandling playerHandling;
	
	public PlayerInfo(Main mainInstance) {
		super(mainInstance,
			"playerInfo",
			"Get general info for the specified player",
			"/playerInfo <player>",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 1) {
			Player player = Bukkit.getPlayerExact(args[0]);
			
			if (player != null) {
				PlayerData pd = playerHandling.getPlayerData(player);
				
				ply.sendMessage(ChatColor.YELLOW + "Points: " + ChatColor.GREEN + pd.getPoints() + "\n"
					+ ChatColor.YELLOW + "Rank: " + ChatColor.GREEN + pd.getUserGroup().toString() + "\n"
					+ ChatColor.YELLOW + "Ip: " + ChatColor.GREEN + pd.getIp());
			} else {
				ply.sendMessage(ChatColor.RED + "Please enter a valid player!");
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
		}
		
		return autoCompletes;
	}
}
