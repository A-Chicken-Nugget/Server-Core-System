package nyeblock.Core.ServerCoreTest.Misc;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;

public class Enums {
	//Realm enums
	public enum Realm {
		HUB("hub","Hub",false),
		PARKOUR("parkour","Parkour",false),
		KITPVP("kitpvp","Kit PvP",true),
		KITPVP_LOBBY("kitpvp_lobby","Kit PvP Lobby",false),
		SKYWARS("skywars","Sky Wars",true),
		SKYWARS_LOBBY("skywars_lobby","Sky Wars Lobby",false),
		STEPSPLEEF("stepspleef","Step Spleef",true),
		STEPSPLEEF_LOBBY("stepspleef_lobby","Step Spleef Lobby",false),
		PVP_DUELS_FISTS("pvp_duels_fists","Duels \u00BB Fists",true),
		PVP_DEULS_WEPSARMOR("pvp_deuls_wepsarmor","Duels \u00BB Weapons/Armor",true),
		PVP_2V2_FISTS("pvp_2v2_fists","2v2 \u00BB Fists",true),
		PVP_2V2_WEPSARMOR("pvp_2v2_wepsarmor","2v2 \u00BB Weapons/Armor",true),
		PVP_LOBBY("pvp_lobby","PvP Lobby",false);

		private String dbname;
		private String name;
		private boolean isGame;

		private Realm(String dbname, String name, boolean isGame) {
			this.dbname = dbname;
			this.name = name;
			this.isGame = isGame;
		}

		public static ArrayList<String> listRealms(boolean listOnlyGames) {
			ArrayList<String> realms = new ArrayList<>();
			
			if (listOnlyGames) {
				for (Realm rel : Realm.values()) {
					if (rel.isGame()) {						
						realms.add(rel.getDBName());
					}
				}
			} else {
				for (Realm rel : Realm.values()) {			
					realms.add(rel.getDBName());
				}
			}
			return realms;
		}
		public static Realm fromName(String relm) {
			Realm rel = null;
			
			for (Realm group : Realm.values()) {
				if (group.toString().equalsIgnoreCase(relm)) {
					rel = group;
				}
			}
			return rel;
		}
		public static Realm fromDBName(String relm) {
			Realm rel = null;
			
			for (Realm group : Realm.values()) {
				if (group.getDBName().equalsIgnoreCase(relm)) {
					rel = group;
				}
			}
			return rel;
		}
		public String getDBName() {
			return dbname;
		}
		public String toString() {
			return name;
		}
		public boolean isGame() {
			return isGame;
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
		public static UserGroup fromName(String userGroup) {
			UserGroup userGrp = null;
			
			for (UserGroup group : UserGroup.values()) {
				if (group.toString().equalsIgnoreCase(userGroup)) {
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
		TWOVTWO(2,"2v2","2v2 PvP");

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
	//Map points enums
	public enum MapPointType {
		PLAYER_SPAWN(),
		GRACE_BOUND(),
		CHEST_SPAWN();
	}
	//Shop menu requirement type
	public enum RequirementType {
		LEVEL(),
		ACHIEVEMENT;
	}
	//Custom npc type
	public enum CustomNPCType {
		NORMAL(),
		JOIN_REALM(),
		TO_HUB();
	}
	//Summary Stat type
	public enum SummaryStatType {
		XP(),
		INTEGER(),
		LEVEL_BAR();
	}
	//Armor type
	public enum ArmorType {
		HELMET(3),
		CHEST_PLATE(2),
		LEGGINGS(1),
		BOOTS(0);
		
		private int armorSlot;
		
		private ArmorType(int armorSlot) {
			this.armorSlot = armorSlot;
		}
		
		public int getArmorSlot() {
			return armorSlot;
		}
	}
	//Achievement enums
//	public enum Achievement {
//		KILLS_15("kills_15", new Runnable() {
//			@Override
//			public void run() {
//				
//			}
//		});
//		
//		private String dbname;
//		private Runnable requirement;
//		
//		private Achievement(String dbname, Runnable requirement) {
//			this.dbname = dbname;
//			this.requirement = requirement;
//		}
//		
//		public String getDBName() {
//			return dbname;
//		}
////		public boolean tryRequirement(Main mainInstance, Player ply) {
//////			requirement.run(mainInstance,ply);
////		}
//	}
}
