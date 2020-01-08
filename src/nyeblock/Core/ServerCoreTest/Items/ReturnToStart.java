package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Realms.HubParkour;

public class ReturnToStart extends ItemBase {
	public ReturnToStart(Main mainInstance, Player player) {
		super(mainInstance,player,"parkour_start");
	}
	
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.LEAD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW + "Return to Start" + ChatColor.GREEN + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("parkour_start");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public void use(ItemStack item) {
		((HubParkour)mainInstance.getPlayerHandlingInstance().getPlayerData(player).getCurrentRealm()).goToStart(player);
	}
}
