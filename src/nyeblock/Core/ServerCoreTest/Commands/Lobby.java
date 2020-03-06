package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;

public class Lobby extends CommandBase {
	private PlayerHandling playerHandling;
	
	public Lobby(Main mainInstance) {
		super(mainInstance,
			"lobby",
			"If in a game, returns you to its lobby",
			"/lobby",
			new ArrayList<String>(),
			null
		);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		PlayerData playerData = playerHandling.getPlayerData(ply);
		RealmBase realm = playerData.getCurrentRealm();
		
		if (realm != null) {
			if (realm instanceof GameBase) {
				realm.leave(ply, playerData.getHiddenStatus() ? false : true, ((GameBase)realm).getLobbyRealm());
			} else {				
				ply.sendMessage(ChatColor.YELLOW + "You are not in a game!");
			}
		}
	}
}
