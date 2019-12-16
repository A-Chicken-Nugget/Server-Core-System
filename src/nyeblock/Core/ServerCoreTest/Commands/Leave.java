package nyeblock.Core.ServerCoreTest.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;

public class Leave extends CommandBase {
	private Main mainInstance;
	private PlayerHandling playerHandling;
	
	public Leave(Main mainInstance) {
		super(mainInstance);
		this.mainInstance = mainInstance;
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		RealmBase game = playerHandling.getPlayerData(ply).getCurrentRealm();
		
		if (game != null) {
			if (game.isAGame()) {
				game.leave(ply, true, Realm.HUB);
			} else {
				if (game.getRealm() == Realm.PARKOUR) {
					game.leave(ply, true, null);
					mainInstance.getGameInstance().joinGame(ply, Realm.HUB);
				} else {					
					ply.sendMessage(ChatColor.YELLOW + "Cannot leave your current realm!");
				}
			}
		}
	}
}
