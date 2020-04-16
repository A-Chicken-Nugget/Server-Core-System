package nyeblock.Core.ServerCoreTest.Misc;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.RealmHandling;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

public class Party {
	private Main mainInstance;
	private PlayerHandling playerHandling;
	private ArrayList<Player> members = new ArrayList<>();
	private Player creator;
	private int maxMembers;
	
	public Party(Main mainInstance ,Player ply, int maxMembers) {
		this.mainInstance = mainInstance;
		this.playerHandling = mainInstance.getPlayerHandlingInstance();
		this.maxMembers = maxMembers;
		members.add(ply);
		creator = ply;
	}
	
	public void messageToAll(String message, boolean showCreator) {
		for (Player ply : members) {
			if (showCreator) {				
				ply.sendMessage(message);
			} else {
				if (!ply.equals(creator)) {
					ply.sendMessage(message);
				}
			}
		}
	}
	public void playerJoin(Player ply) {
		if (members.size() >= maxMembers) {
			ply.sendMessage(ChatColor.YELLOW + "Unable to join party. The party is full.");
		} else {
			messageToAll(ChatColor.GREEN.toString() + ply.getName() + ChatColor.YELLOW + " has joined the party!",true);
			members.add(ply);
			playerHandling.getPlayerData(ply).setParty(this);
			ply.sendMessage(ChatColor.YELLOW + "You have joined " + creator.getName() + "'s party!");
		}
	}
	public void playerLeave(Player ply, boolean showLeaveMessage) {
		if (ply.equals(creator)) {
			members.remove(ply);
			playerHandling.getPlayerData(ply).setParty(null);
			
			for (Player player : members) {
				player.sendMessage(ChatColor.YELLOW + "The creator has closed the party!");
				playerLeave(player,false);
			}
		} else {
			members.remove(ply);
			playerHandling.getPlayerData(ply).setParty(null);
			
			if (showLeaveMessage) {
				messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has left the party!",true);
			}
		}
	}
	public void kickPlayer(Player ply) {
		playerLeave(ply,false);
		messageToAll(ChatColor.GREEN + ply.getName() + ChatColor.YELLOW + " has been kicked from the party!",true);
	}
	public void membersJoin(GameBase game) {
		messageToAll(ChatColor.YELLOW + "Joining the hosts game.",false);
		RealmHandling realmHandling = mainInstance.getRealmHandlingInstance();
		
		for (Player ply : members) {
			if (!ply.equals(creator)) {				
				realmHandling.joinGame(ply, game);
			}
		}
	}
	public void destroy() {
		for (Player player : members) {
			mainInstance.getPlayerHandlingInstance().getPlayerData(player).setParty(null);
			if (!player.equals(creator)) {				
				player.sendMessage(ChatColor.YELLOW + "The party you were in has been destroyed.");
			}
		}
	}
	
	public ArrayList<Player> getMembers() {
		return members;
	}
	public int getMemberCount() {
		return members.size();
	}
	public Player getCreator() {
		return creator;
	}
}
