package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.DatabaseHandling;
import nyeblock.Core.ServerCoreTest.Main;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Ban extends CommandBase {
	private DatabaseHandling databaseHandling;
	
	public Ban(Main mainInstance) {
		super(mainInstance);
		databaseHandling = mainInstance.getDatabaseInstance();
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 3) {
			Player player = Bukkit.getPlayer(args[0]);
			
			if (player != null) {
				try {
					int length = Integer.parseInt(args[1]);
					String tempReason = "";
					
					for (int i = 2; i < args.length; i++) {
						tempReason += " " + args[i];
					}
					
					final String reason = tempReason;
					Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
			            @Override
			            public void run() {       			            	
			            	databaseHandling.query("INSERT INTO bans (uniqueId,length,added,reason) VALUES ('" + player.getUniqueId() + "'," + length + "," + System.currentTimeMillis()/1000L + ",'" + reason + "')", 0, true);									
			            }
					});
					ply.sendMessage(ChatColor.YELLOW.toString() + player.getName() + " banned for " + length + " minutes!");
					player.kickPlayer("You have been banned.\n\nLength: " + length + " minute(s)\n\nReason:" + reason);
				} catch (Exception ex) {
					ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
				}
			} else {
				ply.sendMessage(ChatColor.RED + "Please enter a valid player!");
			}
		} else {
			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
		}
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList();
		
		if (args.length == 1) {
			for (Player ply : Bukkit.getOnlinePlayers()) {
				if (ply.getName().contains(args[0])) {						
					autoCompletes.add(ply.getName());
				}
			}
		} else if (args.length == 2) {
			autoCompletes.add("1");
			autoCompletes.add("5");
			autoCompletes.add("10");
			autoCompletes.add("30");
			autoCompletes.add("60");
			autoCompletes.add("300");
			autoCompletes.add("1440");
		}
		
		return autoCompletes;
	}
}
