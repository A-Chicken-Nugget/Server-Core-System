package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
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
			if (!args[0].equalsIgnoreCase("<<_All_players_in_world_>>")) {
				Player player = Bukkit.getPlayerExact(args[0]);
				
				if (player != null) {
					Realm realm = Realm.fromDBName(args[1]);
					
					if (realm != null) {					
						playerHandling.getPlayerData(player).getCurrentRealm().leave(player, true, realm);
					} else {
						ply.sendMessage(ChatColor.YELLOW + "Please enter a valid realm!");
					}
				} else {
					ply.sendMessage(ChatColor.RED + "Please enter a valid player!");
				}
			} else {
				Realm realm = Realm.fromDBName(args[1]);
				
				if (realm != null) {
					for (Player player : ply.getWorld().getPlayers()) {
						playerHandling.getPlayerData(player).getCurrentRealm().leave(player, true, realm);
					}
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Please enter a valid realm!");
				}
			}
		} else {
			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
		}
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList<>();
//		PlayerData pd = playerHandling.getPlayerData(player);
		
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
			for (String permission : Realm.listRealms()) {
				if (permission.toLowerCase().contains(args[1].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		}
		
		return autoCompletes;
	}
}
