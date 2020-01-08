package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Realms.RealmBase;

public class HidePlayers extends ItemBase {
	public HidePlayers(Main mainInstance, Player player) {
		super(mainInstance,player,"hide_players");
	}
	
	public ItemStack give() {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
		if (pd.getCustomDataKey("hide_players") == null) {			
			pd.setCustomDataKey("hide_players", "false");
		}
		
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta itemMeta = item.getItemMeta();		
		itemMeta.setDisplayName(ChatColor.YELLOW + "Hide Players: " + (Boolean.parseBoolean(pd.getCustomDataKey("hide_players")) ? (ChatColor.GREEN + "Enabled") : (ChatColor.RED + "Disabled")));
		itemMeta.setLocalizedName("hide_players");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		PlayerData playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
		boolean currentStatus = Boolean.parseBoolean(playerData.getCustomDataKey("hide_players"));
		ItemMeta itemMeta = item.getItemMeta();

		if (currentStatus) {
			RealmBase game = playerData.getCurrentRealm();
			
			if (game != null) {							
				for (Player ply2 : game.getPlayersInRealm()) {
					if (!player.canSee(ply2)) {
						if (!playerData.getSpectatingStatus()) {									
							player.showPlayer(mainInstance,ply2);
						}
					}
				}
			} else {
				for (Player ply2 : Bukkit.getOnlinePlayers()) {
					if (!player.canSee(ply2)) {
						if (!playerData.getSpectatingStatus()) {									
							player.showPlayer(mainInstance,ply2);
						}
					}
				}
			}
			playerData.setCustomDataKey("hide_players", "false");
			itemMeta.setDisplayName(ChatColor.YELLOW + "Hide Players: " + ChatColor.RED + "Disabled");
		} else {
			RealmBase game = playerData.getCurrentRealm();
			
			if (game != null) {							
				for (Player ply2 : playerData.getCurrentRealm().getPlayersInRealm()) {
					if (player.canSee(ply2)) {								
						player.hidePlayer(mainInstance,ply2);
					}
				}
			} else {
				for (Player ply2 : Bukkit.getOnlinePlayers()) {
					if (player.canSee(ply2)) {								
						player.hidePlayer(mainInstance,ply2);
					}
				}
			}
			playerData.setCustomDataKey("hide_players", "true");
			itemMeta.setDisplayName(ChatColor.YELLOW + "Hide Players: " + ChatColor.GREEN + "Enabled");
		}
		item.setItemMeta(itemMeta);
	}
}
