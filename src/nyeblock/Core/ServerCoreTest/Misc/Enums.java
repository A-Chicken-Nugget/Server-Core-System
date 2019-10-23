package nyeblock.Core.ServerCoreTest.Misc;

import java.util.Arrays;

import org.bukkit.ChatColor;

public class Enums {
	//Realm enums
	public enum Realm {
		HUB(1,"Hub"),
		KITPVP(2,"Kit Pvp"),
		STEPSPLEEF(3,"Step Spleef"),
		SKYWARS(4,"Sky Wars");

		private int value;  
		private String name;

		private Realm(int value, String name) {
			this.value = value;
			this.name = name;
		}

		public int getValue() {
			return value;
		}
		public String toString() {
			return name;
		}
	} 
	//User group enums
	public enum UserGroup {
		USER(1,"User","[" + ChatColor.WHITE + "User" + ChatColor.RESET + "]"),
		ADMIN(2,"Admin","[" + ChatColor.DARK_RED + "Admin" + ChatColor.RESET + "]"),
		MODERATOR(3,"Moderator","[" + ChatColor.GRAY + "Mod" + ChatColor.RESET + "}");

		private int value;  
		private String name;
		private String tag;

		private UserGroup(int value, String name, String tag) {
			this.value = value;
			this.name = name;
			this.tag = tag;
		}

		public static UserGroup fromInt(int userGroup) {
			UserGroup userGrp = null;
			
			for (UserGroup group : UserGroup.values()) {
				if (group.getValue() == userGroup) {
					userGrp = group;
				}
			}
			return userGrp;
		}
		public static boolean isStaff(UserGroup userGroup) {
			boolean isStaff = false;
			
			if (Arrays.asList(UserGroup.ADMIN,UserGroup.MODERATOR).contains(userGroup)) {
				isStaff = true;
			}
			
			return isStaff;
		}
		public int getValue() {
			return value;
		}
		public String toString() {
			return name;
		}
		public String getTag() {
			return tag;
		}
	}
	
	//Chest Value Enums
	public enum ChestValue {
		SHIT(2, "Shit"),
		BAD(4, "Bad"),
		AVERAGE(6, "Average"),
		GOOD(8, "Good"),
		LEGENDARY(10, "Legendary");
		
		private int value;
		private String name;
		
		private ChestValue(int value, String name) 
		{
			this.value = value;
			this.name = name;
		}
		
		public int getValue() 
		{
			return value;
		}
		
		public String toString() 
		{
			return name;
		}
	}
	
}
