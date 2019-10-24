package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;

public class HidePlayers {
	private Main mainInstance;
	private Player player;
	
	public HidePlayers(Main mainInstance, Player player) {
		this.mainInstance = mainInstance;
		this.player = player;
	}
	
	public ItemStack give() {
		PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
		if (pd.getCustomDataKey("hide_players") == null) {			
			pd.setCustomDataKey("hide_players", "false");
		}
		
		ItemStack item = new ItemStack(Material.COMPASS);
		ItemMeta itemMeta = item.getItemMeta();		
		itemMeta.setDisplayName(ChatColor.YELLOW + "Hide Players: " + (Boolean.parseBoolean(pd.getCustomDataKey("hide_players")) ? (ChatColor.GREEN.toString() + ChatColor.BOLD + "Enabled") : (ChatColor.RED.toString() + ChatColor.BOLD + "Disabled")));
		itemMeta.setLocalizedName("hide_players");
		item.setItemMeta(itemMeta);
		
		return item;
	}
}
