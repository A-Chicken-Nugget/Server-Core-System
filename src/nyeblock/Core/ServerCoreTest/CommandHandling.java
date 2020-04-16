package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;

import nyeblock.Core.ServerCoreTest.Commands.Ban;
import nyeblock.Core.ServerCoreTest.Commands.CommandBase;
import nyeblock.Core.ServerCoreTest.Commands.Find;
import nyeblock.Core.ServerCoreTest.Commands.ForceStart;
import nyeblock.Core.ServerCoreTest.Commands.GiveXP;
import nyeblock.Core.ServerCoreTest.Commands.Hide;
import nyeblock.Core.ServerCoreTest.Commands.Hub;
import nyeblock.Core.ServerCoreTest.Commands.Leave;
import nyeblock.Core.ServerCoreTest.Commands.Lobby;
import nyeblock.Core.ServerCoreTest.Commands.ManageLogs;
import nyeblock.Core.ServerCoreTest.Commands.ManagePoints;
import nyeblock.Core.ServerCoreTest.Commands.PlayerInfo;
import nyeblock.Core.ServerCoreTest.Commands.ResetData;
import nyeblock.Core.ServerCoreTest.Commands.Send;
import nyeblock.Core.ServerCoreTest.Commands.SetPermission;
import nyeblock.Core.ServerCoreTest.Commands.SetUserGroup;

@SuppressWarnings("serial")
public class CommandHandling {
	private Main mainInstance;
	private ArrayList<CommandBase> commands;
	
	public CommandHandling(Main mainInstance) {
		this.mainInstance = mainInstance;
	}
	
	public CommandBase getCommand(String commandName) {
		CommandBase cmd = null;
		
		for (CommandBase command : commands) {
			if (command.getName().equalsIgnoreCase(commandName)) {
				cmd = command;
			}
		}
		return cmd;
	}
	
	public void setCommands() {
		commands = new ArrayList<CommandBase>() {{
			add(new Ban(mainInstance));
//			add(new BanIp(mainInstance));
//			add(new BanPlayerIps(mainInstance));
			add(new ForceStart(mainInstance));
			add(new GiveXP(mainInstance));
			add(new Hide(mainInstance));
			add(new Leave(mainInstance));
			add(new ManagePoints(mainInstance));
			add(new ResetData(mainInstance));
			add(new Send(mainInstance));
			add(new SetPermission(mainInstance));
			add(new SetUserGroup(mainInstance));
			add(new Lobby(mainInstance));
			add(new Hub(mainInstance));
			add(new Find(mainInstance));
			add(new PlayerInfo(mainInstance));
			add(new ManageLogs(mainInstance));
		}};		
	}
}
