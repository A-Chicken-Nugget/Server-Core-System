package nyeblock.Core.ServerCoreTest.Commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.boydti.fawe.object.clipboard.DiskOptimizedClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;

public class Leave extends CommandBase {
	private PlayerHandling playerHandling;
	
	public Leave(Main mainInstance) {
		super(mainInstance);
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public void execute(Player ply, String[] args) {
		PlayerData playerData = playerHandling.getPlayerData(ply);
		RealmBase game = playerData.getCurrentRealm();
		
		if (game != null) {
			if (game instanceof GameBase) {
				game.leave(ply, playerData.getHiddenStatus() ? false : true, Realm.HUB);
			} else {
				if (game.getRealm() == Realm.PARKOUR) {
					game.leave(ply, false, null);
					mainInstance.getGameInstance().joinGame(ply, Realm.HUB);
				} else {					
					ply.sendMessage(ChatColor.YELLOW + "Cannot leave your current realm!");
				}
			}
		}
	}
}
