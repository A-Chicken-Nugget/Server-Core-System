package nyeblock.Core.ServerCoreTest.Achievements;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.PointsReward;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.RewardBase;

@SuppressWarnings("serial")
public class Play25Games extends AchievementBase {
	public Play25Games() {
		super("Play 25 Games", Material.BOOK, new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Play in 25 games in any realm.");
		}}, new ArrayList<RewardBase>() {{
			add(new PointsReward(450));
		}}, "play_25_games");
	}
	
	public boolean meetsRequirements(PlayerData pd) {
		int total = 0;
		
		for (Map.Entry<String,Integer> entry : pd.getTotalGamesPlayed().entrySet()) {
			total += entry.getValue();
		}
		if (total >= 25) {
			return true;
		} else {
			return false;
		}
	}
}
