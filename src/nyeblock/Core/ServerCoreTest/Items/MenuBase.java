package nyeblock.Core.ServerCoreTest.Items;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import nyeblock.Core.ServerCoreTest.Main;
import nyeblock.Core.ServerCoreTest.PlayerData;

@SuppressWarnings("serial")
public abstract class MenuBase extends ItemBase {
	private Inventory inventory;
	private HashMap<String,HashMap<Integer,ItemStack>> menus = new HashMap<String,HashMap<Integer,ItemStack>>();
	private HashMap<String,Integer> menuSize = new HashMap<>();
	private HashMap<String,Runnable> optionActions = new HashMap<String,Runnable>();
	private int size;
	
	public void use(ItemStack item) {}
	/**
	 * Default constructor with parameters
	 * @param mainInstance - the plugin instance
	 * @param player - the player
	 */
	public MenuBase(Main mainInstance, Player player, String name, int size) {
		super(mainInstance,player,name);
		this.size = size;
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
	 * Clears all menu contents
	 */
	public void clear() {
		menus.clear();
		menuSize.clear();
		optionActions.clear();
	}
	/**
	 * TODO DO THIS SHIT BOI
	 * Set the size of the menu
	 * @param size - Number of slots in the menu
	 */
	public void setSize(String name, int size) {
		menuSize.put(name,size);
	}
	public void onDelete() {}
	/**
	 * Open a specific sub menu within the menu
	 * @param ply - the player
	 * @param name - name of the sub menu
	 */
	public void openMenu(String name) {
		HashMap<Integer,ItemStack> menu = menus.get(name);
		
		if (menu != null) {		
			inventory = Bukkit.createInventory(null, size, name);
			PlayerData pd = mainInstance.getPlayerHandlingInstance().getPlayerData(player);
			
			for(Map.Entry<Integer,ItemStack> option : menu.entrySet()) {
				inventory.setItem(option.getKey(), option.getValue());
			}
			pd.setMenu(this);
			player.openInventory(inventory);
			mainInstance.getTimerInstance().createTimer2("invCheck_" + player.getUniqueId(), .5, 0, new Runnable() {
				@Override
				public void run() {
					if (player.getOpenInventory().getTitle().equalsIgnoreCase("crafting")) {
						pd.setMenu(null);
						onDelete();
					}
				}
			});
		}
	}
	/**
	 * Run the clicked options action
	 * @param option - localized meta name of the item
	 * @return whether or not the option exists for the menu
	 */
	public boolean hasOption(String option) {
		return (optionActions.get(option) == null ? false : true);
	}
	/**
	 * Run the clicked options action
	 * @param option - localized meta name of the item
	 */
	public void optionClick(String option) {
		optionActions.get(option).run();
	}
}
