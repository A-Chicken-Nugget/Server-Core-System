package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class BanPlayerIps extends CommandBase {
	public BanPlayerIps(Main mainInstance) {
		super(mainInstance,
			"bannPlayerIps",
			"Ban all the specified player's ips",
			"/bannPlayerIps <player> <length> <reason>",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 3) {
			try {
//				Player player = Bukkit.getPlayer(args[0]);
//				int length = Integer.parseInt(args[1]);
//				String tempReason = "";
//				ArrayList<String> ips = new ArrayList<>();
//				
//				for (int i = 2; i < args.length; i++) {
//					tempReason += " " + args[i];
//				}
//				final String reason = tempReason;
//				
//				Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
//		            @Override
//		            public void run() {
//		            	mainInstance.getDatabaseInstance().query("INSERT INTO ip_bans (ip,length,created,reason) VALUES ('" + mainInstance.getPlayerHandlingInstance().getPlayerData(player).getIp() + "'," + length + "," + System.currentTimeMillis()/1000L + ",'" + reason + "')", true);
//		            	ArrayList<HashMap<String,String>> ipQuery = mainInstance.getDatabaseInstance().query("SELECT DISTINCT * FROM user_ips WHERE uniqueId = '" + player.getUniqueId() + "'", false);
//		            	ips.add(mainInstance.getPlayerHandlingInstance().getPlayerData(player).getIp());
//		            	
//		    			if (ipQuery.size() > 0) {
//		    				for (int i = 0; i < ipQuery.size(); i++) {
//		    					HashMap<String, String> ipQueryData = ipQuery.get(i);
//		    					
//		    					ips.add(ipQueryData.get("ip"));
//		    					mainInstance.getDatabaseInstance().query("INSERT INTO ip_bans (ip,length,created,reason) VALUES ('" + ipQueryData.get("ip") + "'," + length + "," + System.currentTimeMillis()/1000L + ",'" + reason + "')", true);
//		    				}
//		    			}
//		            }
//				});
//				ply.sendMessage(ChatColor.YELLOW.toString() + player.getName() + "'s ips banned for " + length + " minutes!");
//				
//				for (Player player2 : Bukkit.getOnlinePlayers()) {
//					String playerIp = player2.getAddress().toString().split(":")[0].replace("/","");
//					
//					if (ips.contains(playerIp)) {
//						player.kickPlayer("You have been banned.\n\nLength: " + length + " minute(s)\n\nReason:" + reason);
//					}
//				}
			} catch (Exception ex) {
				ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
			}
		} else {
			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
		}
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList<>();
		
		if (args.length == 1) {
			for (Player ply : player.getWorld().getPlayers()) {
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
