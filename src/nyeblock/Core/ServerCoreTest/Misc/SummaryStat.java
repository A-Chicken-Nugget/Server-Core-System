package nyeblock.Core.ServerCoreTest.Misc;

import nyeblock.Core.ServerCoreTest.Misc.Enums.SummaryStatType;

public class SummaryStat {
	private int value;
	private SummaryStatType type;
	
	public SummaryStat(int value, SummaryStatType type) {
		this.value = value;
		this.type = type;
	}
	
	public void addValue(int value) {
		this.value += value;
	}
	public String toString() {
		if (type == SummaryStatType.INTEGER) {
			return String.valueOf(value);
		} else if (type == SummaryStatType.XP) {
			return String.valueOf(value) + "xp";
		}
		return "";
	}
	
	public SummaryStatType getType() {
		return type;
	}
	public int getValue() {
		return value;
	}
}
