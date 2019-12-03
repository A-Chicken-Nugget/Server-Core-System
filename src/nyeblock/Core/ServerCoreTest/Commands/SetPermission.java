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
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserRealm;

@SuppressWarnings({"rawtypes","unchecked"})
public class SetPermission extends CommandBase {
	private Main mainInstance;
	
	public SetPermission(Main mainInstance) {
		super(mainInstance);
		this.mainInstance = mainInstance;
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 3) {
			if (!args[0].equalsIgnoreCase("AllPlayersInGame")) {
				Player player = Bukkit.getPlayer(args[0]);
				
				if (player != null) {					
					mainInstance.getPlayerHandlingInstance().getPlayerData(player).setPermission("nyeblock." + args[1],Boolean.parseBoolean(args[2]));
				} else {
					ply.sendMessage(ChatColor.RED + "Please enter a valid player!");
				}
			} else {
				PlayerHandling ph = mainInstance.getPlayerHandlingInstance();
				PlayerData pd = ph.getPlayerData(ply);
				
				
				for (Player ply2 : ph.getPlayerData(ply).getCurrentRealm().getPlayersInGame()) {
					ph.getPlayerData(ply2).setPermission("nyeblock." + args[1],Boolean.parseBoolean(args[2]));
				}
			}
		} else {
			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
		}
	}
	public List<String> autoCompletes(String[] args) {
		List<String> autoCompletes = new ArrayList();
		
		if (args.length == 1) {
			for (Player ply : Bukkit.getOnlinePlayers()) {
				if (ply.getName().toLowerCase().contains(args[0].toLowerCase())) {						
					autoCompletes.add(ply.getName());
				}
			}
			if ("allplayersingame".contains(args[0].toLowerCase())) {	
				autoCompletes.add("AllPlayersInGame");
			}
		} else if (args.length == 2) {
			for (String permission : Arrays.asList("canBreakBlocks","canUseInventory","canPlaceBlocks","canBeDamaged","canDropItems")) {
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