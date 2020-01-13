package nyeblock.Core.ServerCoreTest.Menus.Shop.Options;

import java.util.ArrayList;
import java.util.List;

import nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements.RequirementBase;

public class ShopMenuTypeOptionItem {
	private String displayText;
	private String uniqueId;
	private int cost;
	private List<RequirementBase> requirements;
	
	public ShopMenuTypeOptionItem(String displayText, String uniqueId, int cost, List<RequirementBase> requirements) {
		this.displayText = displayText;
		this.uniqueId = uniqueId;
		this.cost = cost;
		this.requirements = requirements;
	}
	
	//
	// GETTERS
	//
	
	public String getDisplayText() {
		return displayText;
	}
	public String getUniqueId() {
		return uniqueId;
	}
	public int getCost() {
		return cost;
	}
	public List<RequirementBase> getRequirements() {
		return requirements;
	}
}
