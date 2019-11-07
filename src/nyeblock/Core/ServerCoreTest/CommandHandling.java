package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Commands.Ban;
import nyeblock.Core.ServerCoreTest.Commands.CommandBase;
import nyeblock.Core.ServerCoreTest.Commands.ForceStart;
import nyeblock.Core.ServerCoreTest.Commands.SetPermission;

@SuppressWarnings({"rawtypes","unchecked","serial"})
public class CommandHandling implements CommandExecutor, TabCompleter {
	private Main mainInstance;
	private HashMap<CommandBase,String> commands = new HashMap<>();
	
	public CommandHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
		
		//
		// COMMANDS
		//
		
		commands.put(new SetPermission(mainInstance).getInstance(),"setPermission");
		commands.put(new Ban(mainInstance).getInstance(),"ban");
		commands.put(new ForceStart(mainInstance).getInstance(),"force-start");
		
		//
		// END COMMANDS
		//
		
		for (Map.Entry<CommandBase,String> entry : commands.entrySet()) {
			String name = entry.getValue();
			
			mainInstance.getCommand(name).setExecutor((CommandExecutor)this);
			mainInstance.getCommand(name).setTabCompleter((TabCompleter)this);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (sender instanceof Player) {
			Player ply = (Player) sender;
			PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			
			for (Map.Entry<CommandBase,String> entry : commands.entrySet()) {
				if (label.equalsIgnoreCase(entry.getValue())) {
					entry.getKey().execute(ply,playerData,args);
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
		
		for (Map.Entry<CommandBase,String> entry : commands.entrySet()) {
			if (command.getLabel().equalsIgnoreCase(entry.getValue())) {
				autoCompletes = entry.getKey().autoCompletes(args);
			}
		}
		
		return autoCompletes;
	}
}
