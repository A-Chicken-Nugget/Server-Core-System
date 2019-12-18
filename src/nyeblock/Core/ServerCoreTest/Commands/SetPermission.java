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

@SuppressWarnings({"rawtypes","unchecked"})
public class SetPermission extends CommandBase {
	private Main mainInstance;
	private PlayerHandling playerHandling;
	
	public SetPermission(Main mainInstance) {
		super(mainInstance);
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
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
				for (Player ply2 : playerHandling.getPlayerData(ply).getCurrentRealm().getPlayersInRealm()) {
					playerHandling.getPlayerData(ply2).setPermission("nyeblock." + args[1],Boolean.parseBoolean(args[2]));
				}
			}
		} else {
			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
		}
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList();
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