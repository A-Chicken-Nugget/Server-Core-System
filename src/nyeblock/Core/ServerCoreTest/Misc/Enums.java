package nyeblock.Core.ServerCoreTest.Misc;

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
	//Chest Value Enums
	public enum ChestValue {
		SHIT(1, "Shit"),
		BAD(2, "Bad"),
		AVERAGE(3, "Average"),
		GOOD(4, "Good"),
		DIAMOND(5, "Diamnond");
		
		private int value;
		private String name;
		
		private ChestValue(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int getValue() {
			return value;
		}
	//User group enums
	public enum UserGroup {
		USER(1,"User"),
		ADMIN(2,"Admin");

		private int value;  
		private String name;

		private UserGroup(int value, String name) {
			this.value = value;
			this.name = name;
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
		public int getValue() {
			return value;
		}
		public String toString() {
			return name;
		}
	}
}
