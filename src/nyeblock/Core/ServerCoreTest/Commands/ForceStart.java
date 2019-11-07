package nyeblock.Core.ServerCoreTest.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.GameHandling;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Games.GameBase;

public class ForceStart extends CommandBase {
	GameHandling gameHandling;
	PlayerHandling playerHandling;
	
	public ForceStart(Main mainInstance) {
		super(mainInstance);
		gameHandling = mainInstance.getGameInstance();
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, PlayerData pd, String[] args) {
		GameBase game = gameHandling.getPlayerGame(ply, playerHandling.getPlayerData(ply).getRealm());
		
		if (game != null) {	
			game.forceStart();
		} else {
			ply.sendMessage(ChatColor.YELLOW + "You are not in a game!");
		}
	}
}
