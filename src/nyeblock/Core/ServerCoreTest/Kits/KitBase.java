package nyeblock.Core.ServerCoreTest.Kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;
import nyeblock.Core.ServerCoreTest.Misc.InventoryItem;

public abstract class KitBase {
	private String name;
	private Material material;
	private int cost;
	private ArrayList<RequirementBase> purchaseRequirements;
	private ArrayList<InventoryItem> items;
	private ArrayList<String> description;
	private String uniqueId;
	private boolean isShopKit;
	
	public KitBase(String name, Material material, ArrayList<InventoryItem> items, ArrayList<String> description) {
		this.name = name;
		this.material = material;
		this.items = items;
		this.description = description;
		isShopKit = false;
	}
	public KitBase(String name, Material material, int cost, ArrayList<RequirementBase> purchaseRequirements, ArrayList<InventoryItem> items, ArrayList<String> description, String uniqueId) {
		this.name = name;
		this.material = material;
		this.cost = cost;
		this.purchaseRequirements = purchaseRequirements;
		this.items = items;
		this.description = description;
		this.uniqueId = uniqueId;
		isShopKit = true;
	}
	
	public boolean isShopKit() {
		return isShopKit;
	}
	
	//
	// GETTERS
	//
	
	public String getName() {
		return name;
	}
	public Material getMaterial() {
		return material;
	}
	public int getCost() {
		return cost;
	}
	public ArrayList<RequirementBase> getPurchaseRequirements() {
		return purchaseRequirements;
	}
	public ArrayList<InventoryItem> getItems() {
		return items;
	}
	public ArrayList<String> getDescription() {
		return description;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	
	//
	// SETTERS
	//
	
	public void setItems(Main mainInstance, Player player) {
		PlayerInventory inventory = player.getInventory();
		
		for (InventoryItem item : items) {
			if (item.isCustom()) {
				item.getCustomItem().setData(mainInstance, player);
			}			
			item.giveItem(inventory);
		}
	}
}
