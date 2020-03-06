package nyeblock.Core.ServerCoreTest.Menus.Shop.Requirements;

import org.bukkit.ChatColor;

import nyeblock.Core.ServerCoreTest.PlayerData;
import nyeblock.Core.ServerCoreTest.Misc.Enums.UserGroup;

public class UserGroupRequirement extends RequirementBase {
	UserGroup userGroup;
	
	public UserGroupRequirement(UserGroup userGroup) {
		super(ChatColor.YELLOW + userGroup.toString() + " rank", "You do not meet the rank requirement!");
		
		this.userGroup = userGroup;
	}
	
	public boolean meetsRequirement(PlayerData playerData) { 
		boolean doesMeet = false;
		
		if (playerData.getUserGroup().equals(UserGroup.ADMIN) || playerData.getUserGroup().equals(userGroup)) {
			doesMeet = true;
		}
		return doesMeet;
	}
}
