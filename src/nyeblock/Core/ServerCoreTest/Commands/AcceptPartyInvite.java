package nyeblock.Core.ServerCoreTest.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;

public class AcceptPartyInvite extends CommandBase {
	private PlayerHandling playerHandling;
	
	public AcceptPartyInvite(Main mainInstance) {
		super(mainInstance,
			"acceptPartyInvite",
			"If invited to a party, this will accept that invitation.",
			"/acceptPartyInvite",
			new ArrayList<String>(),
			null
		);
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		PlayerData pd = playerHandling.getPlayerData(ply);
		
		if (pd.getPartyInvite() != null) {
			if (pd.getParty() != null) {
				pd.getParty().playerLeave(ply, true);
			}
			pd.getPartyInvite().playerJoin(ply);
		} else {
			ply.sendMessage(ChatColor.YELLOW + "You have not been invited to a party!");
		}
	}
	public List<String> autoCompletes(Player player, String[] args) {
		List<String> autoCompletes = new ArrayList<>();
		
		return autoCompletes;
	}
}
