package nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements;

import net.md_5.bungee.api.ChatColor;
import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class LevelRequirement extends RequirementBase {
	private int level;
	private Realm realm;
	
	public LevelRequirement(int level, Realm realm) {
		super(ChatColor.YELLOW + realm.toString() + " Level: " + ChatColor.GREEN + level, "You do not meet the level requirement!");
		
		this.level = level;
		this.realm = realm;
	}
	
	public boolean meetsRequirement(PlayerData playerData) { 
		boolean doesMeet = false;
		
		if (level >= playerData.getLevel(realm)) {
			doesMeet = true;
		}
		return doesMeet;
	}
}
