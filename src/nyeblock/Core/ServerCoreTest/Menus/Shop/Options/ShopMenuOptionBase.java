package nyeblock.Core.ServerCoreTest.Menus.Shop.Options;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Menus.MenuOption;
import nyeblock.Core.ServerCoreTest.Menus.Shop.ShopSubMenu;
import nyeblock.Core.ServerCoreTest.Menus.Shop.SubMenu;

public abstract class ShopMenuOptionBase extends MenuOption {
	protected Player player;
	protected PlayerData playerData;
	protected String uniqueId;
	protected String displayName;
	protected int cost;
	protected ShopSubMenu subMenu;
	protected boolean isEquipped;
	protected boolean multiPurchasable;
	protected boolean hasPurchased;
	protected int quantity;
	//Item info
	protected String itemName;
	protected Material material;
	protected ArrayList<String> desc;
	
	public ShopMenuOptionBase(String uniqueId, int position, SubMenu subMenu, boolean multiPurchasable) {
		super(position,subMenu);
		
		player = subMenu.getParent().getPlayer();
		playerData = subMenu.getParent().getMainInstance().getPlayerHandlingInstance().getPlayerData(player);
		this.uniqueId = uniqueId;
		this.subMenu = (ShopSubMenu)subMenu;
		this.multiPurchasable = multiPurchasable;
	}
	
	public boolean isEquipped() {
		return isEquipped;
	}
	public void purchase() {}
	public void runAction(ClickType clickType) {}
	
	//
	// GETTERS
	//
	
	public String getUniqueId() {
		return uniqueId;
	}
	public boolean getMultiPurchasable() {
		return multiPurchasable;
	}
	public String getDisplayName() {
		return displayName;
	}
	public int getCost() {
		return cost;
	}
	public int getQuantity() {
		return quantity;
	}
	
	//
	// SETTERS
	//
	
	public void updateItemInfo() {}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public void setItemInfo(Material material, String itemName, ArrayList<String> desc) {
		this.material = material;
		this.itemName = itemName;
		this.desc = desc;
	}
}
