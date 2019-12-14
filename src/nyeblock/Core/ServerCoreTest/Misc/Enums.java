package nyeblock.Core.ServerCoreTest.Misc;

import java.util.Arrays;

import org.bukkit.ChatColor;

import nyeblock.Core.ServerCoreTest.Realms.Hub;
import nyeblock.Core.ServerCoreTest.Realms.KitPvP;
import nyeblock.Core.ServerCoreTest.Realms.StepSpleef;
import nyeblock.Core.ServerCoreTest.Realms.SkyWars;
import nyeblock.Core.ServerCoreTest.Realms.HubParkour;

public class Enums {
	//Realm enums
	public enum Realm {
		HUB(1,"Hub",Hub.class),
		KITPVP(2,"Kit Pvp",KitPvP.class),
		STEPSPLEEF(3,"Step Spleef",StepSpleef.class),
		SKYWARS(4,"Sky Wars",SkyWars.class),
		PVP(5,"PvP",nyeblock.Core.ServerCoreTest.Realms.PvP.class),
		PARKOUR(6,"Parkour",HubParkour.class);

		private int value;  
		private String name;
		public Class<?> cls;

		private Realm(int value, String name, Class<?> cls) {
			this.value = value;
			this.name = name;
			this.cls = cls;
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
		DUELS(1,"Duels"),
		TWOVTWO(2,"2v2");

		private int value;  
		private String name;

		private PvPMode(int value, String name) {
			this.value = value;
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
		public String toString() {
			return name;
		}
	} 
	//PvP realm enums
	public enum PvPType {
		FIST(1,"Fist"),
		WEPSARMOR(2,"Weapons/Armor");

		private int value;  
		private String name;

		private PvPType(int value, String name) {
			this.value = value;
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
		public String toString() {
			return name;
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
