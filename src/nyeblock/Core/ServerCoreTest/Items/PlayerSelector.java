package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;

public class PlayerSelector extends ItemBase {
	private PlayerHandling playerHandling;
	
	public PlayerSelector(Main mainInstance, Player player) {
		super(mainInstance,player,"player_selector");
		
		playerHandling = mainInstance.getPlayerHandlingInstance();
	}
	
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: " + ChatColor.BOLD + "N/A"
				+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("player_selector");
		item.setItemMeta(itemMeta);
		
		PlayerData playerData = playerHandling.getPlayerData(player);
		playerData.setCustomDataKey("player_selector_index","-1");
		
		return item;
	}
	public void use(ItemStack item) {
		PlayerData playerData = playerHandling.getPlayerData(player);
		int currentIndex = Integer.parseInt(playerData.getCustomDataKey("player_selector_index") != null ? playerData.getCustomDataKey("player_selector_index") : "-1");
		RealmBase game = playerData.getCurrentRealm();
		ArrayList<Player> playersInGame = game.getPlayersInRealm();
		ItemMeta itemMeta = item.getItemMeta();
		boolean foundPlayer = false;
		
		for (Player ply : playersInGame) {
			if (playersInGame.size() > currentIndex + 1) {
				Player playerToSpec = playersInGame.get(currentIndex + 1);
				
				if (playerToSpec != player && !playerHandling.getPlayerData(playerToSpec).getSpectatingStatus()) {	
					foundPlayer = true;
					player.teleport(playerToSpec);
					itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
							+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
							+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
					playerData.setCustomDataKey("player_selector_index",
							String.valueOf(currentIndex + 1));
				}
			} else {
				Player playerToSpec = playersInGame.get(0);
				
				if (playerToSpec != player && !playerHandling.getPlayerData(playerToSpec).getSpectatingStatus()) {
					foundPlayer = true;
					player.teleport(playerToSpec);
					itemMeta.setDisplayName(ChatColor.YELLOW + "Spectating: "
							+ ChatColor.GREEN.toString() + ChatColor.BOLD + playerToSpec.getName()
							+ ChatColor.RESET.toString() + ChatColor.GREEN + " (RIGHT-CLICK)");
					playerData.setCustomDataKey("player_selector_index", "0");
				}
			}
		}
		if (!foundPlayer) {
			itemMeta.setDisplayName(ChatColor.YELLOW + "No players to spectate.");
		}
		item.setItemMeta(itemMeta);
	}
}
