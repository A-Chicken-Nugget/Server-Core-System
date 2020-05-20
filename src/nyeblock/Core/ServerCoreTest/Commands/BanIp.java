package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Toolkit;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BanIp extends CommandBase {
	public BanIp(Main mainInstance) {
		super(mainInstance,
			"bannip",
			"Ban the specified ip for the specified amount of time",
			"/bannip <ip> <length> <reason>",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
	}
	
	public void execute(Player ply, String[] args) {                 	
    	if (args.length >= 2) {
    		if (Toolkit.isIp(args[0])) {            			
    			try {
    				String ip = args[0];
    				final int length = Integer.parseInt(args[1]);
    				String tempReason = "";
    				
    				for (int i = 2; i < args.length; i++) {
    					tempReason += " " + args[i];
    				}
    				final String reason = tempReason;
    				
    				Bukkit.getScheduler().runTaskAsynchronously(mainInstance, new Runnable() {
    		            @Override
    		            public void run() {  
    		            	mainInstance.getDatabaseInstance().query("INSERT INTO ip_bans (ip,length,created,reason) VALUES ('" + ip + "'," + length + "," + System.currentTimeMillis()/1000L + ",'" + reason + "')", true);					
    		            }
    				});
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
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList();
		
		if (args.length == 2) {
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
