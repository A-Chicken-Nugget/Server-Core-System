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

public class Hub extends CommandBase {
	private PlayerHandling playerHandling;
	
	public Hub(Main mainInstance) {
		super(mainInstance,
			"hub",
			"Returns you to the server hub",
			"/hub",
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
				realm.leave(ply, playerData.getHiddenStatus() ? false : true, Realm.HUB);
			} else {
				if (realm.getRealm() == Realm.PARKOUR) {
					realm.leave(ply, false, null);
					mainInstance.getRealmHandlingInstance().joinRealm(ply, Realm.HUB);
				} else if (realm.getRealm() == Realm.HUB) {					
					ply.sendMessage(ChatColor.YELLOW + "You are in the hub!");
				} else {
					realm.leave(ply, false, Realm.HUB);
				}
			}
		}
	}
}
