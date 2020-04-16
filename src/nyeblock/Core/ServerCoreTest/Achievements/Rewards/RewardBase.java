package nyeblock.Core.ServerCoreTest.Achievements.Rewards;

import nyeblock.Core.ServerCoreTest.PlayerData;

public abstract class RewardBase {
	private String displayText;
	
	public RewardBase(String displayText) {
		this.displayText = displayText;
	}
	
	public void giveReward(PlayerData pd) {}
	
	public String getDisplayText() {
		return displayText;
	}
}
