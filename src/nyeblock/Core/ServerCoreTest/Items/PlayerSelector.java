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

public class PlayerSelector {
	private Main mainInstance;
	private ArrayList<Player> playersInGame;
	private Player player;
	
	public PlayerSelector(Main mainInstance, Realm realm, Player player) {
		this.mainInstance = mainInstance;
		this.player = player;
		playersInGame = mainInstance.getPlayerHandlingInstance().getPlayerData(player).getCurrentRealm().getPlayersInRealm();
	}
	
	public ItemStack give() {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
		boolean foundPlayer = false;
		int currentIndex = 0;
		
		pd.setCustomDataKey("player_world", player.getWorld().getName());
		
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta itemMeta = item.getItemMeta();
		
		for (int i = 0; i < playersInGame.size(); i++) {
			currentIndex++;
			if (currentIndex >= playersInGame.size()) {
				currentIndex = 0;
			}
			
			Player curPlayer = playersInGame.get(currentIndex);
			
			if (!curPlayer.equals(player) && !mainInstance.getPlayerHandlingInstance().getPlayerData(playersInGame.get(currentIndex)).getSpectatingStatus()) {
				foundPlayer = true;
				
				player.teleport(curPlayer);
				itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
						+ ChatColor.GREEN.toString() + ChatColor.BOLD + curPlayer.getName()
						+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
				pd.setCustomDataKey("player_selector_index",
						String.valueOf(currentIndex));
				break;
			}
		}
		if (!foundPlayer) {
			itemMeta.setDisplayName(ChatColor.YELLOW + "No players to spectate.");
		}
		itemMeta.setLocalizedName("player_selector");
		item.setItemMeta(itemMeta);
		
		return item;
	}
}
