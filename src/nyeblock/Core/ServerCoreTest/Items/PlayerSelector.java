package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

public class PlayerSelector {
	private Main mainInstance;
	private Player player;
	private ArrayList<Player> players = new ArrayList<>();
	
	public PlayerSelector(Main mainInstance, Realm realm, Player player) {
		this.mainInstance = mainInstance;
		this.player = player;
		
		GameBase game = mainInstance.getPlayerHandlingInstance().getPlayerData(player).getCurrentGame();
		
		if (game.isInServer(player)) {			
			players = game.getPlayersInGame();
		}
	}
	
	public ItemStack give() {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
		pd.setCustomDataKey("player_selector_index", "0");
		pd.setCustomDataKey("player_world", player.getWorld().getName());
		
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta itemMeta = item.getItemMeta();
		if (players.size() > 0) {			
			itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: " + ChatColor.GREEN.toString() + ChatColor.BOLD + players.get(0).getName() + ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
		} else {
			itemMeta.setDisplayName(ChatColor.YELLOW + "No players to spectate.");
		}
		itemMeta.setLocalizedName("player_selector");
		item.setItemMeta(itemMeta);
		
		return item;
	}
}
