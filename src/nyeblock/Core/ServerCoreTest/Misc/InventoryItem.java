package nyeblock.Core.ServerCoreTest.Misc;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import nyeblock.Core.ServerCoreTest.Items.ItemBase;
import nyeblock.Core.ServerCoreTest.Misc.Enums.ArmorType;

public class InventoryItem {
	private ItemStack item;
	private int slot;
	private boolean isArmor = false;
	private ArmorType armorType;
	private boolean shouldEquip;
	private ItemBase customItem;
	
	public InventoryItem(ItemStack item, int slot, boolean shouldEquip) {
		this.item = item;
		this.slot = slot;
		this.shouldEquip = shouldEquip;
	}
	public InventoryItem(ItemStack item, int slot, boolean shouldEquip, ItemBase customItem) {
		this.item = item;
		this.slot = slot;
		this.shouldEquip = shouldEquip;
		this.customItem = customItem;
	}
	public InventoryItem(ItemStack item, ArmorType armorType) {
		this.item = item;
		isArmor = true;
		this.armorType = armorType;
	}
	
	public boolean isArmor() {
		return isArmor;
	}
	public boolean isCustom() {
		boolean isCustom = false;
		
		if (customItem != null) {
			isCustom = true;
		}
		return isCustom;
	}
	
	public void giveItem(PlayerInventory inventory) {
		if (isArmor) {
			ItemStack[] armor = inventory.getArmorContents();
			
			armor[armorType.getArmorSlot()] = item;
			inventory.setArmorContents(armor);
		} else {			
			inventory.setItem(slot, item);
			if (shouldEquip) {
				inventory.setHeldItemSlot(slot);
			}
		}
	}
	
	public ItemStack getItem() {
		return item;
	}
	public ItemBase getCustomItem() {
		return customItem;
	}
}
