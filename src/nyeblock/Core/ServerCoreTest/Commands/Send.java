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
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class Send extends CommandBase {
	private PlayerHandling playerHandling;
	
	public Send(Main mainInstance) {
		super(mainInstance);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 2) {			
			Player player = Bukkit.getPlayerExact(args[0]);
			
			if (player != null) {					
				playerHandling.getPlayerData(ply).getCurrentRealm().leave(ply, true, Realm.HUB);
			} else {
				ply.sendMessage(ChatColor.RED + "Please enter a valid player!");
			}
		} else {
			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
		}
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList<>();
		PlayerData pd = playerHandling.getPlayerData(player);
		
		if (args.length == 1) {
			for (Player ply : pd.getCurrentRealm().getPlayersInRealm()) {
				if (ply.getName().toLowerCase().contains(args[0].toLowerCase())) {						
					autoCompletes.add(ply.getName());
				}
			}
			if ("allplayersingame".contains(args[0].toLowerCase())) {	
				autoCompletes.add("AllPlayersInGame");
			}
		} else if (args.length == 2) {
			for (String permission : Arrays.asList("Hub")) {
				if (permission.toLowerCase().contains(args[1].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		}
		
		return autoCompletes;
	}
}
