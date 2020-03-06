package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class Hide extends CommandBase {
	public Hide(Main mainInstance) {
		super(mainInstance,
			"hide",
			"Hide/show yourself to other players",
			"/hide",
			new ArrayList<String>(),
			Arrays.asList(UserGroup.ADMIN)
		);
	}
	
	public void execute(Player ply, String[] args) {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(ply);
		
		if (pd.getHiddenStatus()) {
			ply.sendMessage(ChatColor.YELLOW + "You are now visible!");
			pd.setHidden(false);
			ply.setCollidable(false);
		} else {
			ply.sendMessage(ChatColor.YELLOW + "You are now hidden!");
			pd.setHidden(true);
			ply.setCollidable(true);
		}
		pd.getCurrentRealm().updateHiddenShownPlayers();
	}
}
