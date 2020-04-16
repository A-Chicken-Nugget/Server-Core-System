package nyeblock.Core.ServerCoreTest.Menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("deprecation")
public class MenuOption {
	protected String name;
	protected int position;
	protected SubMenu subMenu;
	protected ItemStack item;
	protected HashMap<ClickType,Runnable> clickActions;
	
	public MenuOption(int position,SubMenu subMenu) {
		this.position = position;
		this.subMenu = subMenu;
		name = UUID.randomUUID().toString();
	}
	
	public void runAction(ClickType clickType) {
		if (clickActions != null) {			
			Runnable action = clickActions.get(clickType);

			if (action != null) {				
				action.run();
			}
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
	
	public void setClickActions(HashMap<ClickType,Runnable> clickActions) {
		this.clickActions = clickActions;
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
	public void setItem(String playerName, String itemName, ArrayList<String> desc) {
		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		
		SkullMeta itemMeta = (SkullMeta)item.getItemMeta();
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
		itemMeta.setOwner(playerName);
		item.setItemMeta(itemMeta);
		this.item = item;
	}
}
