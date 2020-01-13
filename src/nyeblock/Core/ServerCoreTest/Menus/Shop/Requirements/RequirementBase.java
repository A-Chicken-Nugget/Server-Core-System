package nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements;

import nyeblock.Core.ServerCoreTest.PlayerData;

public abstract class RequirementBase {
	protected String displayText;
	protected String failMessage;
	
	public RequirementBase(String displayText, String failMessage) {
		this.displayText = displayText;
		this.failMessage = failMessage;
	}
	
	public boolean meetsRequirement(PlayerData playerData) { return false; }
	
	//
	// GETTERS
	//
	
	public String getDisplayText() {
		return displayText;
	}
	public String getFailMessage() {
		return failMessage;
	}
}
