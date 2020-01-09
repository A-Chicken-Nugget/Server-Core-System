package nyeblock.Core.ServerCoreTest.Menus;

public class ShopItem {
	private String uniqueId;
	private int quantity;
	private boolean isEquiped;
	
	public ShopItem(String uniqueId, int quantity, boolean isEquiped) {
		this.uniqueId = uniqueId;
		this.quantity = quantity;
		this.isEquiped = isEquiped;
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
		return isEquiped;
	}
	
	//
	// SETTERS
	//
	
	public void setEquipped(boolean isEquiped) {
		this.isEquiped = isEquiped;
	}
}
