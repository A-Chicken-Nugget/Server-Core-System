package nyeblock.Core.ServerCoreTest.Menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;

public class NyeBlockMenu extends MenuBase {
//private PlayerHandling playerHandling;
	
	public NyeBlockMenu(Main mainInstance, Player player) {
		super(mainInstance,player,"nyeblock_menu");
	}
	
	public void setContents() {
		
	}
	//Give the player this item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.BEACON);
		ItemMeta shopMeta = item.getItemMeta();
		shopMeta.setDisplayName(ChatColor.YELLOW + "NyeBlock Menu" + ChatColor.GREEN + " (RIGHT-CLICK)");
		shopMeta.setLocalizedName("nyeblock_menu");
		item.setItemMeta(shopMeta);
		
		return item;
	}
	//Use the item
	public void use(ItemStack item) {
		player.sendMessage(ChatColor.YELLOW + "This feature is currently disabled.");
//		if (mainInstance.getPlayerHandlingInstance().getPlayerData(player).getLoadedDBInfoStatus()) {			
//			open();
//		}
	}
}
