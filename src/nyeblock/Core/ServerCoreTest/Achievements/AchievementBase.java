package nyeblock.Core.ServerCoreTest.Achievements;

import java.util.ArrayList;

import org.bukkit.Material;

import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Achievements.Rewards.RewardBase;

public abstract class AchievementBase {
	private String name;
	private Material material;
	private ArrayList<String> description;
	private ArrayList<RewardBase> rewards;
	private String uniqueId;
	
	public AchievementBase(String name, Material material, ArrayList<String> description, ArrayList<RewardBase> rewards, String uniqueId) {
		this.name = name;
		this.material = material;
		this.description = description;
		this.rewards = rewards;
		this.uniqueId = uniqueId;
	}
	
	public boolean meetsRequirements(PlayerData pd) { return false; }
	public void giveRewards(PlayerData pd) {
		for (RewardBase reward : rewards) {
			reward.giveReward(pd);
		}
	}
	
	public String getName() {
		return name;
	}
	public Material getMaterial() {
		return material;
	}
	public ArrayList<String> getDescription() {
		return description;
	}
	public ArrayList<RewardBase> getRewards() {
		return rewards;
	}
	public String getUniqueId() {
		return uniqueId;
	}
}
