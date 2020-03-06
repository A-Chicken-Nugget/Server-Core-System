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

public class Leave extends CommandBase {
	private PlayerHandling playerHandling;
	
	public Leave(Main mainInstance) {
		super(mainInstance,
			"leave",
			"Leave the current realm you are in",
			"/leave",
			new ArrayList<String>(),
			null
		);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		PlayerData playerData = playerHandling.getPlayerData(ply);
		RealmBase game = playerData.getCurrentRealm();
		
		if (game != null) {
			if (game instanceof GameBase) {
				game.leave(ply, playerData.getHiddenStatus() ? false : true, ((GameBase)game).getLobbyRealm());
			} else {
				if (game.getRealm() == Realm.PARKOUR) {
					game.leave(ply, false, null);
					mainInstance.getRealmHandlingInstance().joinRealm(ply, Realm.HUB);
				} else if (game.getRealm() == Realm.HUB) {					
					ply.sendMessage(ChatColor.YELLOW + "Cannot leave your current realm!");
				} else {
					game.leave(ply, false, Realm.HUB);
				}
			}
		}
	}
}
