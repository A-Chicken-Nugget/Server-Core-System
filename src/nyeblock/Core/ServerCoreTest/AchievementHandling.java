package nyeblock.Core.ServerCoreTest;

import java.util.ArrayList;

import nyeblock.Core.ServerCoreTest.Achievements.AchievementBase;
import nyeblock.Core.ServerCoreTest.Achievements.Play10Games;
import nyeblock.Core.ServerCoreTest.Achievements.Win10Games;

@SuppressWarnings("serial")
public class AchievementHandling {
	private ArrayList<AchievementBase> achievements;
	
	public AchievementHandling() {
		achievements = new ArrayList<AchievementBase>() {{
			add(new Play10Games());
			add(new Win10Games());
		}};
	}
	
	public ArrayList<AchievementBase> getAchievements() {
		return achievements;
	}
}
