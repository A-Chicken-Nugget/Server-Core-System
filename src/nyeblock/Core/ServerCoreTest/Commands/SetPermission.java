package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;

@SuppressWarnings({"rawtypes","unchecked"})
public class SetPermission extends CommandBase {
	private Main mainInstance;
	
	public SetPermission(Main mainInstance) {
		super(mainInstance);
		this.mainInstance = mainInstance;
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 3) {
			Player player = Bukkit.getPlayer(args[0]);
			
			if (player != null) {
				mainInstance.getPlayerHandlingInstance().getPlayerData(player).setPermission("nyeblock." + args[1],Boolean.parseBoolean(args[2]));
			} else {
				ply.sendMessage(ChatColor.RED + "Please enter a valid player!");
			}
		} else {
			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
		}
	}
	public List<String> autoCompletes(String[] args) {
		List<String> autoCompletes = new ArrayList();
		
		if (args.length == 1) {
			for (Player ply : Bukkit.getOnlinePlayers()) {
				if (ply.getName().contains(args[0])) {						
					autoCompletes.add(ply.getName());
				}
			}
		} else if (args.length == 2) {
			for (String permission : Arrays.asList("canBreakBlocks","canUseInventory","canPlaceBlocks","canBeDamaged","canDropItems")) {
				if (permission.contains(args[1])) {
					autoCompletes.add(permission);
				}
			}
		} else if (args.length == 3) {
			for (String permission : Arrays.asList("true","false")) {
				if (permission.contains(args[2])) {
					autoCompletes.add(permission);
				}
			}
		}
		
		return autoCompletes;
	}
}