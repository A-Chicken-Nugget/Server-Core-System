package nyeblock.Core.ServerCoreTest.Items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class ReturnToHub {
	//Give the player the item
	public ItemStack give() {
		ItemStack item = new ItemStack(Material.LEAD);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Return to Hub" + ChatColor.GREEN.toString() + ChatColor.BOLD + " (RIGHT-CLICK)");
		itemMeta.setLocalizedName("return_to_hub");
		item.setItemMeta(itemMeta);
		
		return item;
	}
}
