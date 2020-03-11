package nyeblock.Core.ServerCoreTest.Misc;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryItem {
	private ItemStack item;
	private int slot;
	private boolean isArmor = false;
	private boolean shouldEquip;
	
	public InventoryItem(ItemStack item, int slot, boolean shouldEquip) {
		this.item = item;
		this.slot = slot;
		this.shouldEquip = shouldEquip;
	}
	public InventoryItem(ItemStack item, int slot, boolean isArmor, boolean shouldEquip) {
		this.item = item;
		this.slot = slot;
		this.isArmor = isArmor;
		this.shouldEquip = shouldEquip;
	}
	
	public void giveItem(PlayerInventory inventory) {
		if (isArmor) {
			Material
		} else {			
			inventory.setItem(slot, item);
			if (shouldEquip) {
				inventory.setHeldItemSlot(slot);
			}
		}
	}
}
