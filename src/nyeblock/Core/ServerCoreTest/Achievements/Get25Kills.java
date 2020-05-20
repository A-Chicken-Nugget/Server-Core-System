package nyeblock.Core.ServerCoreTest.Achievements;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.PointsReward;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.RewardBase;

@SuppressWarnings("serial")
public class Get25Kills extends AchievementBase {
	public Get25Kills() {
		super("Get 25 Kills", Material.BOOK, new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Get 25 kills in any realm.");
		}}, new ArrayList<RewardBase>() {{
			add(new PointsReward(450));
		}}, "get_25_kills");
	}
	
	public boolean meetsRequirements(PlayerData pd) {
		int total = 0;
		
		for (Map.Entry<String,Integer> entry : pd.getTotalGameKills().entrySet()) {
			total += entry.getValue();
		}
		if (total >= 25) {
			return true;
		} else {
			return false;
		}
	}
}
