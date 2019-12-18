package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class ReturnToHub extends ItemBase {
	public ReturnToHub(Main mainInstance, Player player) {
		super(mainInstance,player,"return_to_hub");
	}
	
	//Give the player the item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.LEAD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Return to Hub" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("return_to_hub");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	public void use(ItemStack item) {
		mainInstance.getPlayerHandlingInstance().getPlayerData(player).getCurrentRealm().leave(player, true, Realm.HUB);
	}
}
