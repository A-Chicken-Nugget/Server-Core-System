package nyeblock.Core.ServerCoreTest.Commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;

public class Hide extends CommandBase {
	public Hide(Main mainInstance) {
		super(mainInstance);
	}
	
	public void execute(Player ply, String[] args) {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		
		if (pd.getHiddenStatus()) {
			ply.sendMessage(ChatColor.YELLOW + "You are now visible!");
			pd.setHidden(false);
		} else {
			ply.sendMessage(ChatColor.YELLOW + "You are now hidden!");
			pd.setHidden(true);
		}
		pd.getCurrentRealm().updateHiddenShownPlayers();
	}
}
