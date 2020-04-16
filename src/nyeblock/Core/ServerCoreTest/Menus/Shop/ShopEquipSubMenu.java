package nyeblock.Core.ServerCoreTest.Menus.Shop;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;

import nyeblock.Core.ServerCoreTest.Menus.MenuBase;
import nyeblock.Core.ServerCoreTest.Menus.MenuOption;
import nyeblock.Core.ServerCoreTest.Menus.SubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuOption;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuOptionBase;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuTypeOption;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Options.ShopMenuTypeOptionItem;
import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;

public class ShopEquipSubMenu extends SubMenu {
	private boolean canEquipItems;
	private int maxItemsEquiped;
	
	public ShopEquipSubMenu(String title, int size, boolean canEquipItems, int maxItemsEquiped, MenuBase parent) {
		super(title,size,parent);
		
		this.canEquipItems = canEquipItems;
		this.maxItemsEquiped = maxItemsEquiped;
	}
	
	public void createShopOption(int position, Material material, String itemName, ArrayList<String> desc, HashMap<ClickType,Runnable> clickActions, ArrayList<RequirementBase> requirements, String uniqueId, int cost, boolean multiPurchasable) {
		if (canEquipItems && multiPurchasable) {
			System.out.println("[Core] Unable to add item " + uniqueId + " to " + title + ". The sub menu does not allow multipurchasable items!");
		} else {
			ShopMenuOption option = new ShopMenuOption(uniqueId,position,cost,requirements,this,multiPurchasable);
			option.setItemInfo(material, itemName, desc);
			option.setClickActions(clickActions);
			options.add(option);
			
			for (MenuOption optionn : options) {
				if (optionn instanceof ShopMenuOptionBase) {					
					((ShopMenuOptionBase)optionn).updateItemInfo();
				}
			}
		}
	}
	public void createShopTypeOption(int position, Material material, String itemName, ArrayList<String> desc, ArrayList<ShopMenuTypeOptionItem> optionItems, HashMap<ClickType,Runnable> clickActions, String displayText, String uniqueId, boolean multiPurchasable) {
		if (canEquipItems && multiPurchasable) {
			System.out.println("[Core] Unable to add item " + uniqueId + " to " + title + ". The sub menu does not allow multipurchasable items!");
		} else {			
			ShopMenuTypeOption option = new ShopMenuTypeOption(uniqueId,position,displayText,optionItems,this,multiPurchasable);
			option.setItemInfo(material, itemName, desc);
			option.setClickActions(clickActions);
			options.add(option);
			
			for (MenuOption optionn : options) {	
				if (optionn instanceof ShopMenuOptionBase) {					
					((ShopMenuOptionBase)optionn).updateItemInfo();
				}
			}
		}
	}
	public void useItem(String uniqueId) {
		for (MenuOption option : options) {
			if (option instanceof ShopMenuOptionBase) {				
				if (((ShopMenuOptionBase)option).getUniqueId().equalsIgnoreCase(uniqueId)) {
					((ShopMenuOptionBase)option).use();
				}
			}
		}
	}
	public boolean canEquipItem() {
		return !(getEquippedItems().size() >= maxItemsEquiped);
	}
	
	//
	// GETTERS
	//
	
	public String getTitle() {
		return title;
	}
	public boolean getCanEquipItems() {
		return canEquipItems;
	}
	public int getMaxEquippedItems() {
		return maxItemsEquiped;
	}
	public ArrayList<MenuOption> getOptions() {
		return options;
	}
	public ArrayList<ShopMenuOptionBase> getEquippedItems() {
		ArrayList<ShopMenuOptionBase> equippedItems = new ArrayList<>();
		
		for (MenuOption option : options) {
			if (option instanceof ShopMenuOptionBase) {	
				if (((ShopMenuOptionBase)option).isEquipped()) {
					equippedItems.add(((ShopMenuOptionBase)option));
				}
			}
		}
		return equippedItems;
	}
}
