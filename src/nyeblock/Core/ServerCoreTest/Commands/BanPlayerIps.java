package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.DatabaseHandling;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BanPlayerIps extends CommandBase {
//	private Main mainInstance;
//	private DatabaseHandling databaseHandling;
//	
	public BanPlayerIps(Main mainInstance) {
		super(mainInstance);
//		this.mainInstance = mainInstance;
//		databaseHandling = mainInstance.getDatabaseInstance();
	}
//	
//	public void execute(Player ply, String[] args) {
//		if (args.length >= 2) {
//			try {
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
//		            	databaseHandling.query("INSERT INTO ipBans (ip,length,added,reason) VALUES ('" + ip + "'," + length + "," + System.currentTimeMillis()/1000L + ",'" + reason + "')", 0, true);									
//		            }
//				});
//				ply.sendMessage(ChatColor.YELLOW.toString() + "Ip banned " + ip + " for " + length + " minutes!");
//				
//				for (Player player2 : Bukkit.getOnlinePlayers()) {
//					String playerIp = player2.getAddress().toString().split(":")[0].replace("/","");
//					
//					if (playerIp.equals(ip)) {
//						player.kickPlayer("You have been banned.\n\nLength: " + length + " minute(s)\n\nReason:" + reason);
//					}
//				}
//			} catch (Exception ex) {
//				ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
//			}
//		} else {
//			ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
//		}
//	}
//	public List<String> autoCompletes(String[] args) {
//		List<String> autoCompletes = new ArrayList();
//		
//		return autoCompletes;
//	}
}
