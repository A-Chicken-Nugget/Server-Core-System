package nyeblock.Core.ServerCoreTest.Games;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.util.Vector;

import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class GameMapInfo {
	@SuppressWarnings("serial")
	//Get vectors for specific maps used in games
	public ArrayList<HashMap<String,Vector>> getMapInfo(Realm realm, String name) {
		ArrayList<HashMap<String,Vector>> mapInfo = null;
		
		if (realm == Realm.KITPVP) {
			//kitPvP_first
			if (name.equalsIgnoreCase("first")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(0,63,-6));
						put("spawn_2", new Vector(0,63,6));
						put("spawn_3", new Vector(5,63,0));
						put("spawn_4", new Vector(-5,63,0));
					}});
					//Grace area
					add(new HashMap<String,Vector>() {{
						put("graceBound1", new Vector(11,78,11));
					}});
					add(new HashMap<String,Vector>() {{
						put("graceBound2", new Vector(-11,60,-11));
					}});
				}};
			//kitPvP_grassland
			} else if (name.equalsIgnoreCase("grassland")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(-27,59,-38));
						put("spawn_2", new Vector(-31,59,-34));
						put("spawn_3", new Vector(-35,59,-38));
						put("spawn_4", new Vector(-31,59,-42));
					}});
					//Grace area
					add(new HashMap<String,Vector>() {{
						put("graceBound1", new Vector(-19,54,-26));
					}});
					add(new HashMap<String,Vector>() {{
						put("graceBound2", new Vector(-39,70,-46));
					}});
				}};
			}
		} else if (realm == Realm.STEPSPLEEF) {
			//stepSpleef_first
			if (name.equalsIgnoreCase("first")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(2,60,9));
						put("spawn_2", new Vector(-6,60,9));
						put("spawn_3", new Vector(-20,60,21));
						put("spawn_4", new Vector(-18,60,34));
						put("spawn_5", new Vector(-5,60,51));
						put("spawn_6", new Vector(0,60,50));
						put("spawn_7", new Vector(20,60,27));
						put("spawn_8", new Vector(19,60,23));
					}});
				}};
			} 
		} else if (realm == Realm.SKYWARS) {
			//stepSpleef_first
			if (name.equalsIgnoreCase("first")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(3.522,75,-2.43));
						put("spawn_2", new Vector(45.456,75,6.565));
						put("spawn_3", new Vector(54.464,74,48.502));
						put("spawn_4", new Vector(45.452,75,90.433));
						put("spawn_5", new Vector(3.434,75,100.480));
						put("spawn_6", new Vector(-38.528,75,90.442));
						put("spawn_7", new Vector(-47.506,75,48.541));
						put("spawn_8", new Vector(-38.550,75,6.414));
					}});
				}};
			}
		}
		return mapInfo;
	}
}
