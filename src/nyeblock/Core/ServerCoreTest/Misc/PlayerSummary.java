package nyeblock.Core.ServerCoreTest.Misc;

import java.util.HashMap;
import nyeblock.Core.ServerCoreTest.Misc.Enums.SummaryStatType;

public class PlayerSummary {
	private HashMap<String,SummaryStat> stats = new HashMap<>();
	
	public void addStat(String name, Integer value, SummaryStatType type) {
		if (stats.get(name) == null) {
			stats.put(name, new SummaryStat(value,type));
		} else {
			stats.get(name).addValue(value);
		}
	}
	
	public HashMap<String,SummaryStat> getStats() {
		return stats;
	}
}
