package nyeblock.Core.ServerCoreTest.Achievements.Rewards;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;

public class PointsReward extends RewardBase {
	private int amount;
	
	public PointsReward(int amount) {
		super(ChatColor.GREEN.toString() + amount + " " + ChatColor.YELLOW + "Points");
		this.amount = amount;
	}
	
	public void giveReward(PlayerData pd) {
		pd.addPoints(amount);
	}
}
