package nyeblock.Core.ServerCoreTest.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.Realm;

public class ForceStart extends CommandBase {
	PlayerHandling playerHandling;
	
	public ForceStart(Main mainInstance) {
		super(mainInstance);
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		Realm game = playerHandling.getPlayerData(ply).getCurrentRealm();
		
		if (game != null) {
			if (game.getPlayerCount() > 1) {				
				game.forceStart();
			} else {
				ply.sendMessage(ChatColor.YELLOW + "Cannot force start a game with only 1 player!");
			}
		} else {
			ply.sendMessage(ChatColor.YELLOW + "You are not in a game!");
		}
	}
}
