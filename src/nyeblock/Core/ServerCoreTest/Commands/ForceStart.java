package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;

public class ForceStart extends CommandBase {
	PlayerHandling playerHandling;
	
	public ForceStart(Main mainInstance) {
		super(mainInstance,
			"force-start",
			"Force start the game you are currently in",
			"/force-start",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		RealmBase realm = playerHandling.getPlayerData(ply).getCurrentRealm();
		
		if (realm instanceof GameBase) {				
			((GameBase)realm).forceStart();
		} else {
			ply.sendMessage(ChatColor.YELLOW + "You are not in a game!");
		}
	}
}
