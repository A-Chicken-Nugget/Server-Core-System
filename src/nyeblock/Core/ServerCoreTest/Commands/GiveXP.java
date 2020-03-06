package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class GiveXP extends CommandBase {
private PlayerHandling playerHandling;
	
	public GiveXP(Main mainInstance) {
		super(mainInstance,
			"giveXP",
			"Give XP to the specified player",
			"/giveXP <player> <realm> <amount>",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 3) {
			Player player = Bukkit.getPlayerExact(args[0]);
			
			if (player != null) {
				Realm realm = Realm.fromDBName(args[1]);
				
				if (realm != null) {					
					try {					
						Integer amount = Integer.parseInt(args[2]);
						playerHandling.getPlayerData(player).giveXP(realm, amount);
					} catch (Exception ex) {
						ply.sendMessage(ChatColor.RED + "Please enter a valid amount!");
					}
				} else {
					ply.sendMessage(ChatColor.YELLOW + "Please enter a valid realm!");
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
			for (String permission : Realm.listRealms(true)) {
				if (permission.toLowerCase().contains(args[1].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		}
		return autoCompletes;
	}
}
