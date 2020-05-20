package nyeblock.Core.ServerCoreTest.Achievements;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.PointsReward;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.RewardBase;

@SuppressWarnings("serial")
public class Get10Kills extends AchievementBase {
	public Get10Kills() {
		super("Get 10 Kills", Material.BOOK, new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Get 10 kills in any realm.");
		}}, new ArrayList<RewardBase>() {{
			add(new PointsReward(350));
		}}, "get_10_kills");
	}
	
	public boolean meetsRequirements(PlayerData pd) {
		int total = 0;
		
		for (Map.Entry<String,Integer> entry : pd.getTotalGameKills().entrySet()) {
			total += entry.getValue();
		}
		if (total >= 10) {
			return true;
		} else {
			return false;
		}
	}
}
