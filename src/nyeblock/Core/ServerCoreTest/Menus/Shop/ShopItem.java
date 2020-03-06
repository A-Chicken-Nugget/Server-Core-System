package nyeblock.Core.ServerCoreTest.Menus.Shop;

public class ShopItem {
	private String uniqueId;
	private int quantity;
	private boolean isEquipped;
	private String menuName;
	
	public ShopItem(String uniqueId, int quantity, boolean isEquipped, String menuName) {
		this.uniqueId = uniqueId;
		this.quantity = quantity;
		this.isEquipped = isEquipped;
		this.menuName = menuName;
	}
	
	public boolean updateQuantity(boolean add) {
		boolean shouldRemove = false;
		
		if (add) {
			quantity += 1;
		} else {
			quantity -= 1;
			
			if (quantity <= 0) {
				shouldRemove = true;
			}
		}
		return shouldRemove;
	}
	
	//
	// GETTERS
	//
	
	public String getUniqueId() {
		return uniqueId;
	}
	public int getQuantity() {
		return quantity;
	}
	public boolean isEquipped() {
		return isEquipped;
	}
	public String getMenuName() {
		return menuName;
	}
	
	//
	// SETTERS
	//
	
	public void setEquipped(boolean isEquiped) {
		this.isEquipped = isEquiped;
	}
}
