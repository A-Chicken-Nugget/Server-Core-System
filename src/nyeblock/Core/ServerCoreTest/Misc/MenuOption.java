package nyeblock.Core.ServerCoreTest.Misc;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuOption {
	private String name;
	private int position;
	protected SubMenu subMenu;
	private ItemStack item;
	protected Runnable action;
	
	public MenuOption(int position,SubMenu subMenu) {
		this.position = position;
		this.subMenu = subMenu;
		name = UUID.randomUUID().toString();
	}
	
	public void runAction() {
		if (action != null) {		
			action.run();
		}
	}
	
	//
	// GETTERS
	//
	
	public String getName() {
		return name;
	}
	public int getPosition() {
		return position;
	}
	public ItemStack getItem() {
		return item;
	}
	
	//
	// SETTERS
	//
	
	public void setAction(Runnable action) {
		this.action = action;
	}
	public void setItem(Material material, String itemName, ArrayList<String> desc) {
		ItemStack item = new ItemStack(material);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(itemName);
		itemMeta.setLocalizedName(name);
		itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		if (desc != null) {			
			ArrayList<String> itemMetaLore = new ArrayList<String>();
			for (String descItem : desc) {			
				itemMetaLore.add(descItem);
			}
			itemMeta.setLore(itemMetaLore);
		}
		item.setItemMeta(itemMeta);
		this.item = item;
	}
}
