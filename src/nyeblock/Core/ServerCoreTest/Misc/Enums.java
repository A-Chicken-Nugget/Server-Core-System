package nyeblock.Core.ServerCoreTest.Misc;

import java.util.Arrays;

import org.bukkit.ChatColor;

public class Enums {
	//Realm enums
	public enum Realm {
		HUB(1,"hub","Hub"),
		KITPVP(2,"kitpvp","Kit Pvp"),
		STEPSPLEEF(3,"stepspleef","Step Spleef"),
		SKYWARS(4,"skywars","Sky Wars"),
		PVP(5,"pvp","PvP"),
		PARKOUR(6,"parkour","Parkour");

		private int value; 
		private String dbname;
		private String name;

		private Realm(int value, String dbname, String name) {
			this.value = value;
			this.dbname = dbname;
			this.name = name;
		}

		public int getValue() {
			return value;
		}
		public String getDBName() {
			return dbname;
		}
		public String toString() {
			return name;
		}
	} 
	//User group enums
	public enum UserGroup {
		USER(1,"User","[" + ChatColor.WHITE + "User" + ChatColor.RESET + "]",ChatColor.WHITE),
		ADMIN(2,"Admin","[" + ChatColor.DARK_RED + "Admin" + ChatColor.RESET + "]",ChatColor.DARK_RED),
		MODERATOR(3,"Moderator","[" + ChatColor.GRAY + "Mod" + ChatColor.RESET + "]",ChatColor.GRAY),
		TESTER(4,"Tester","[" + ChatColor.YELLOW + "Tester" + ChatColor.RESET + "]",ChatColor.YELLOW),
		VIP(5,"VIP","[" + ChatColor.GREEN + "VIP" + ChatColor.RESET + "]",ChatColor.GREEN),
		VIP_MODERATOR(6,"VIP Moderator","[" + ChatColor.GRAY + "VIP Mod" + ChatColor.RESET + "]",ChatColor.GRAY),
		VIP_TESTER(7,"VIP Tester","[" + ChatColor.YELLOW + "VIP Tester" + ChatColor.RESET + "]",ChatColor.YELLOW);

		private int value;  
		private String name;
		private String tag;
		private ChatColor color;

		private UserGroup(int value, String name, String tag, ChatColor color) {
			this.value = value;
			this.name = name;
			this.tag = tag;
			this.color = color;
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
		public ChatColor getColor() {
			return color;
		}
	}
	//PvP realm mode
	public enum PvPMode {
		DUELS(1,"duels","Duels"),
		TWOVTWO(2,"2v2","2v2");

		private int value; 
		private String dbname;
		private String name;

		private PvPMode(int value, String dbname, String name) {
			this.value = value;
			this.dbname = dbname;
			this.name = name;
		}

		public static PvPMode fromInt(int pMode) {
			PvPMode pvPType = null;
			
			for (PvPMode mode : PvPMode.values()) {
				if (mode.getValue() == pMode) {
					pvPType = mode;
				}
			}
			return pvPType;
		}
		public int getValue() {
			return value;
		}
		public String getDBName() {
			return dbname;
		}
		public String toString() {
			return name;
		}
	} 
	//PvP realm enums
	public enum PvPType {
		FIST(1,"fists","Fist"),
		WEPSARMOR(2,"wepsarmor","Weapons/Armor");

		private int value;  
		private String dbname;
		private String name;

		private PvPType(int value, String dbname, String name) {
			this.value = value;
			this.dbname = dbname;
			this.name = name;
		}

		public static PvPType fromInt(int pMode) {
			PvPType pvPMode = null;
			
			for (PvPType mode : PvPType.values()) {
				if (mode.getValue() == pMode) {
					pvPMode = mode;
				}
			}
			return pvPMode;
		}
		public int getValue() {
			return value;
		}
		public String getDBName() {
			return dbname;
		}
		public String toString() {
			return name;
		}
	} 
	//Chest Value Enums
	public enum ChestValue {
		COMMON(1, "Common", true, 5, 8),
		MEDIUM(2, "Medium", true, 4, 6),
		HIGH(3, "High", false, 3, 4),
		LEGENDARY(4, "Legendary", false, 3, 4);
		
		private int value;
		private String name;
		private boolean includeDefault;
		private int min;
		private int max;
		
		private ChestValue(int value, String name, boolean includeDefault, int min, int max) {
			this.value = value;
			this.name = name;
			this.includeDefault = includeDefault;
			this.min = min;
			this.max = max;
		}
		
		public int getValue() {
			return value;
		}
		public String toString() {
			return name;
		}
		public boolean getShouldIncludeDefault() {
			return includeDefault;
		}
		public int getMin() {
			return min;
		}
		public int getMax() {
			return max;
		}
	}
	
}
