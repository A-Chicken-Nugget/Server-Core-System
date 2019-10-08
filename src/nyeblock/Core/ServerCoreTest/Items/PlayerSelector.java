package nyeblock.Core.ServerCoreTest.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.GameHandling;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.PlayerHandling;
import nyeblock.Core.ServerCoreTest.Games.StepSpleef;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

@SuppressWarnings("deprecation")
public class PlayerSelector {
	private Main mainInstance;
	private Realm realm;
	private Player player;
	private ArrayList<Player> players = new ArrayList<>();
	
	public PlayerSelector(Main mainInstance, Realm realm, Player player) {
		this.mainInstance = mainInstance;
		this.realm = realm;
		this.player = player;
		
		GameHandling gh = mainInstance.getGameInstance();
		if (realm == Realm.STEPSPLEEF) {
			for (StepSpleef game : gh.getStepSpleefGames()) {
				if (game.isInServer(player)) {
					players = game.getPlayersInGame();
				}
			}
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
