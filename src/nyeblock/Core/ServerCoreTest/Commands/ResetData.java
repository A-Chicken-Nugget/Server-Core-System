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
import nyeblock.Core.ServerCoreTest.Misc.Enums.DBDataType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class ResetData extends CommandBase {
	private PlayerHandling playerHandling;
	
	public ResetData(Main mainInstance) {
		super(mainInstance,
			"resetData",
			"Reset the data of the specified player",
			"/resetData",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 2) {
			Player player = Bukkit.getPlayerExact(args[0]);
			
			if (player != null) {
				PlayerData pd = playerHandling.getPlayerData(player);
				
				if (args[1].equalsIgnoreCase("queuing_status")) {
					pd.setQueuingStatus(false);
					player.sendMessage(ChatColor.YELLOW + "Your queuing status has been reset.");
				} else if (args[1].equalsIgnoreCase("force_leave")) {
					pd.getCurrentRealm().leave(player,true,Realm.HUB);
				} else if (args[1].equalsIgnoreCase("kick_reset")) {
					player.kickPlayer(ChatColor.YELLOW + "Data reset. Join back.");
					pd.saveData(DBDataType.ALL);
					playerHandling.removePlayerData(player);
				} else if (args[1].equalsIgnoreCase("log_queuing")) {
					if (!pd.getLogSearch()) {						
						pd.setLogSearch(true);
						ply.sendMessage(ChatColor.YELLOW + "Enabled search log for " + player.getName());
					} else {
						pd.setLogSearch(false);
						ply.sendMessage(ChatColor.YELLOW + "Disabled search log for " + player.getName());
					}
				}
			}
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
		} else if (args.length == 2) {
			for (String permission : Arrays.asList("queuing_status","force_leave","kick_reset","log_queuing")) {
				if (permission.toLowerCase().contains(args[1].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		}
		return autoCompletes;
	}
}
		
