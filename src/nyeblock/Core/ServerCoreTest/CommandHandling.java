package nyeblock.Core.ServerCoreTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Games.GameBase;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

@SuppressWarnings({"rawtypes","unchecked"})

public class CommandHandling implements CommandExecutor, TabCompleter {
	private Main mainInstance;
	private PlayerHandling playerHandlingInstance;
	private GameHandling gameHandlingInstance;
	
	public CommandHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
		playerHandlingInstance = mainInstance.getPlayerHandlingInstance();
		gameHandlingInstance = mainInstance.getGameInstance();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		if (sender instanceof Player) {
			Player ply = (Player) sender;
			PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
			
			if (playerData.getUserGroup() == UserGroup.ADMIN) {
				if (args.length > 0) {
					if (label.equalsIgnoreCase("setpermission")) {
						
					}
				} else {
					if (label.equalsIgnoreCase("force-start")) {
						GameBase game = gameHandlingInstance.getPlayerGame(ply, playerHandlingInstance.getPlayerData(ply).getRealm());
						
						if (game != null) {	
							game.forceStart();
						} else {
							ply.sendMessage(ChatColor.YELLOW + "You are not in a game!");
						}
					} else {						
						ply.sendMessage(ChatColor.RED + "Please enter arguements for this command!");
					}
				}
			} else {
				ply.sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is a mistake.");
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> autoCompletes = new ArrayList();
		
		if (command.getLabel().equalsIgnoreCase("setpermission")) {
			if (args.length == 1) {
				for (Player ply : Bukkit.getOnlinePlayers()) {
					autoCompletes.add(ply.getName());
				}
			} else if (args.length == 2) {
				for (String permission : Arrays.asList("canBreakBlocks","canUseInventory")) {
					if (permission.contains(args[1])) {
						autoCompletes.add(permission);
					}
				}
			} else if (args.length == 3) {
				for (String permission : Arrays.asList("true","false")) {
					if (permission.contains(args[2])) {
						autoCompletes.add(permission);
					}
				}
			}
		}
		
		return autoCompletes;
	}
}
