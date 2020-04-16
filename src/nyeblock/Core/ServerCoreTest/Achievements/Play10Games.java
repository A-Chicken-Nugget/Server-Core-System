package nyeblock.Core.ServerCoreTest.Achievements;

import java.util.ArrayList;
import java.util.Map;

import org.bukkit.Material;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.PointsReward;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.RewardBase;

@SuppressWarnings("serial")
public class Play10Games extends AchievementBase {
	public Play10Games() {
		super("Play 10 Games", Material.BOOK, new ArrayList<String>() {{
			add(ChatColor.YELLOW + "Play in 10 games in any realm.");
		}}, new ArrayList<RewardBase>() {{
			add(new PointsReward(250));
		}}, "play_10_games");
	}
	
	public boolean meetsRequirements(PlayerData pd) {
		int total = 0;
		
		for (Map.Entry<String,Integer> entry : pd.getTotalGamesPlayed().entrySet()) {
			total += entry.getValue();
		}
		if (total >= 10) {
			return true;
		} else {
			return false;
		}
	}
}
