package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Commands.Ban;
import nyeblock.Core.ServerCoreTest.Commands.BanIp;
import nyeblock.Core.ServerCoreTest.Commands.CommandBase;
import nyeblock.Core.ServerCoreTest.Commands.ForceStart;
import nyeblock.Core.ServerCoreTest.Commands.Leave;
import nyeblock.Core.ServerCoreTest.Commands.SetPermission;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

@SuppressWarnings({"rawtypes","unchecked"})
public class CommandHandling implements CommandExecutor, TabCompleter {
	private Main mainInstance;
	private HashMap<CommandBase,String> commands = new HashMap<>();
	private HashMap<String,List<UserGroup>> commandPermissions = new HashMap<>();
	
	public CommandHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
		
		//
		// COMMANDS
		//
		
		commands.put(new SetPermission(mainInstance).getInstance(),"setPermission");
		commandPermissions.put("setPermission", Arrays.asList(UserGroup.ADMIN));
		commands.put(new Ban(mainInstance).getInstance(),"ban");
		commandPermissions.put("ban", Arrays.asList(UserGroup.ADMIN));
		commands.put(new BanIp(mainInstance).getInstance(),"banIp");
		commandPermissions.put("banIp", Arrays.asList(UserGroup.ADMIN));
		commands.put(new ForceStart(mainInstance).getInstance(),"force-start");
		commandPermissions.put("force-start", Arrays.asList(UserGroup.ADMIN));
		commands.put(new Leave(mainInstance).getInstance(),"leave");
		commandPermissions.put("leave", null);
		
		//
		// END COMMANDS
		//
		
		for (Map.Entry<CommandBase,String> entry : commands.entrySet()) {
			String command = entry.getValue();
			
			mainInstance.getCommand(command).setExecutor((CommandExecutor)this);
			mainInstance.getCommand(command).setTabCompleter((TabCompleter)this);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (sender instanceof Player) {
			Player ply = (Player) sender;
			PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			
			for (Map.Entry<CommandBase,String> entry : commands.entrySet()) {
				if (label.equalsIgnoreCase(entry.getValue())) {
					if (commandPermissions.get(entry.getValue()) == null || commandPermissions.get(entry.getValue()).contains(playerData.getUserGroup())) {
						entry.getKey().execute(ply,args);
					} else {
						ply.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake.");
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> autoCompletes = new ArrayList();
		PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData((Player)sender);
		
		for (Map.Entry<CommandBase,String> entry : commands.entrySet()) {
			if (command.getLabel().equalsIgnoreCase(entry.getValue())) {
				if (commandPermissions.get(entry.getValue()).contains(playerData.getUserGroup())) {
					autoCompletes = entry.getKey().autoCompletes((Player)sender,args);
				}
			}
		}
		
		return autoCompletes;
	}
}
