package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class SetUserGroup extends CommandBase {
	private PlayerHandling playerHandling;
	
	public SetUserGroup(Main mainInstance) {
		super(mainInstance);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 2) {			
			Player player = Bukkit.getPlayerExact(args[0]);
			
			if (player != null) {
				UserGroup group = UserGroup.fromName(args[1]);
				
				if (group != null) {
					playerHandling.getPlayerData(player).setUserGroup(group);
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Please enter a valid usergroup!");
				}
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
			for (Player ply : player.getWorld().getPlayers()) {
				if (ply.getName().toLowerCase().contains(args[0].toLowerCase())) {						
					autoCompletes.add(ply.getName());
				}
			}
		} else if (args.length == 2) {
			for (UserGroup group : UserGroup.values()) {
				if (group.toString().toLowerCase().contains(args[1].toLowerCase())) {
					autoCompletes.add(group.toString());
				}
			}
		}
		
		return autoCompletes;
	}
}
