package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class QueueGame extends ItemBase {
	private PlayerData playerData;
	
	public QueueGame(Main mainInstance, Player player) {
		super(mainInstance,player,"queue_game");
		
		playerData = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
	}
	
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.GHAST_TEAR);
		ItemMeta itemMeta = item.getItemMeta();		
		itemMeta.setDisplayName(ChatColor.YELLOW + "Queue for a " + Realm.fromDBName(playerData.getRealm().getDBName().split("_")[0]) + " game" + ChatColor.GREEN + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("queue_game");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public void use(ItemStack item) {
		mainInstance.getGameInstance().joinGame(player, Realm.fromDBName(playerData.getRealm().getDBName().split("_")[0]));
	}
}
