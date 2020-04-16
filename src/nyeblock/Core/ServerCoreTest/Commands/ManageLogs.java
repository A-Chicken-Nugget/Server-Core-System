package nyeblock.Core.ServerCoreTest.Commands;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class ManageLogs extends CommandBase {
	public ManageLogs(Main mainInstance) {
		super(mainInstance,
			"manageLogs",
			"blah",
			"/manageLogs",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
	}
	
	public void execute(Player ply, String[] args) {
		if (args.length >= 2) {
			Boolean value = Boolean.parseBoolean(args[1]);
			
			if (value != null) {
				if (args[0].equalsIgnoreCase("play_time_log")) {
					mainInstance.setLogPlayTime(value);
				}
			}
		}
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList<>();
		
		if (args.length == 1) {
			for (String permission : Arrays.asList("play_time_log")) {
				if (permission.toLowerCase().contains(args[0].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		} else if (args.length == 2) {
			for (String permission : Arrays.asList("true","false")) {
				if (permission.toLowerCase().contains(args[1].toLowerCase())) {
					autoCompletes.add(permission);
				}
			}
		}
		return autoCompletes;
	}
}
		
