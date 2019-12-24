package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.DatabaseHandling;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BanIp extends CommandBase {
	private DatabaseHandling databaseHandling;
	
	public BanIp(Main mainInstance) {
		super(mainInstance);
		databaseHandling = mainInstance.getDatabaseInstance();
	}
	
	public void execute(Player ply, String[] args) {
		Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
            @Override
            public void run() {                   	
            	if (args.length >= 2) {
            		if (Toolkit.isIp(args[0])) {            			
            			try {
            				String ip = args[0];
            				int length = Integer.parseInt(args[1]);
            				String reason = "";
            				
            				for (int i = 2; i < args.length; i++) {
            					reason += " " + args[i];
            				}
            				
            				databaseHandling.query("INSERT INTO ipBans (ip,length,added,reason) VALUES ('" + ip + "'," + length + "," + System.currentTimeMillis()/1000L + ",'" + reason + "')", 0, true);									
            				ply.sendMessage(ChatColor.YELLOW.toString() + "Ip banned " + ip + " for " + length + " minutes!");
            				
            				for (Player player : Bukkit.getOnlinePlayers()) {
            					String playerIp = player.getAddress().toString().split(":")[0].replace("/","");
            					
            					if (playerIp.equals(ip)) {
            						player.kickPlayer("You have been banned.\n\nLength: " + length + " minute(s)\n\nReason:" + reason);
            					}
            				}
            			} catch (Exception ex) {
            				ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
            			}
            		} else {
            			ply.sendMessage(ChatColor.RED + "Please enter a valid ip!");
            		}
            	} else {
            		ply.sendMessage(ChatColor.RED + "Please enter the proper arguements for this command!");
            	}
            }
		});
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList();
		
		return autoCompletes;
	}
}
