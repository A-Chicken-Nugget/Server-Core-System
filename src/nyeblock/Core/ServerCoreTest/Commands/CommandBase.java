
package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public abstract class CommandBase extends BukkitCommand {
	protected Main mainInstance;
	protected PlayerHandling playerHandling;
	private String command;
	private List<UserGroup> accessGroups;
	
	public CommandBase(Main mainInstance, String command, String desc, String usage, List<String> aliases, List<UserGroup> accessGroups) {
		super(command);
		
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
		this.command = command;
		this.accessGroups = accessGroups;
		description = desc;
		usageMessage = usage;
		setAliases(aliases);
		
		((CraftServer)mainInstance.getServer()).getCommandMap().register(getCommand(), this);
	}
	
	public void execute(Player ply, String[] args) {};
	public List<String> autoCompletes(Player ply, String[] args) { return null; };
	public boolean canExecute(UserGroup userGroup) {
		if (accessGroups != null) {			
			return accessGroups.contains(userGroup);
		} else {
			return true;
		}
	}
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		Player ply = (Player)sender;
		
		if (canExecute(playerHandling.getPlayerData(ply).getUserGroup())) {
			execute(ply,args);
		} else {
			ply.sendMessage(ChatColor.RED + "You do not have access to this command.");
		}
		return false;
	}
	@Override
	public List<String> tabComplete(CommandSender sender, String label, String[] args) {
		Player ply = (Player)sender;
		List<String> autoCompletes = new ArrayList<String>();
		
		if (canExecute(playerHandling.getPlayerData(ply).getUserGroup())) {
			autoCompletes = autoCompletes(ply,args);
		}
		return autoCompletes;
	}
	
	//
	// GETTERS
	//
	
	public String getCommand() {
		return command;
	}
}
