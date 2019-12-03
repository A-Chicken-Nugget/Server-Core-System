package nyeblock.Core.ServerCoreTest.Items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import nyeblock.Core.ServerCoreTest.Main;

@SuppressWarnings("serial")
public abstract class MenuBase {
	protected Main mainInstance;
	protected Player player;
	private HashMap<String,HashMap<Integer,ItemStack>> menus = new HashMap<String,HashMap<Integer,ItemStack>>();
	private HashMap<String,Runnable> optionActions = new HashMap<String,Runnable>();
	
	/**
	 * Default constructor without any parameters
	 */
	public MenuBase() {
	}
	/**
	 * Default constructor with parameters
	 * @param mainInstance - the plugin instance
	 * @param player - the player
	 */
	public MenuBase(Main mainInstance, Player player) {
		this.mainInstance = mainInstance;
		this.player = player;
	}
	
	/**
	 * Method defined in sub class that gives the player the menu item
	 */
	public ItemStack give() { return null; };
	/**
	 * Add an option to the menus list
	 * @param menuName - name of the sub menu the item is in
	 * @param pos - position of the item in the menu
	 * @param item - item that is displayed in the menu
	 * @param action - action ran when the option is clicked
	 */
	public void addOption(String menuName, int pos, ItemStack item, Runnable action) {
		if (menus.get(menuName) == null) {
			menus.put(menuName, new HashMap<Integer,ItemStack>() {{
				put(pos,item);
			}});
		} else {
			menus.get(menuName).put(pos, item);
		}
		optionActions.put(item.getItemMeta().getLocalizedName(), action);
	}
	/**
	 * Open a specific sub menu within the menu
	 * @param ply - the player
	 * @param name - name of the sub menu
	 */
	public void openMenu(Player ply, String name) {
		HashMap<Integer,ItemStack> menu = menus.get(name);
		
		if (menu != null) {		
			Inventory inv = Bukkit.createInventory(null, 36, name);
			
			for(Map.Entry<Integer,ItemStack> option : menu.entrySet()) {
				inv.setItem(option.getKey(), option.getValue());
			}
			ply.openInventory(inv);
		}
	}
	/**
	 * Run the clicked options action
	 * @param option - localized meta name of the item
	 */
	public void optionClick(String option) {
		optionActions.get(option).run();
	}
}
