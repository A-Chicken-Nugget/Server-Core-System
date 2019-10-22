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
			//concrete
			if (name.equalsIgnoreCase("concrete")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(-10.481,74,37.447));
						put("spawn_2", new Vector(-16.472,74,43.521));
						put("spawn_3", new Vector(-22.511,74,37.532));
						put("spawn_4", new Vector(-16.504,74,31.515));
					}});
					//Grace area
					add(new HashMap<String,Vector>() {{
						put("graceBound1", new Vector(-3,87,23));
					}});
					add(new HashMap<String,Vector>() {{
						put("graceBound2", new Vector(-29,69,50));
					}});
				}};
			//grassland
			} else if (name.equalsIgnoreCase("grassland")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(-78.526,89,35.524));
						put("spawn_2", new Vector(-82,89,39.468));
						put("spawn_3", new Vector(-86.497,89,35.517));
						put("spawn_4", new Vector(-82.520,89,31.547));
					}});
					//Grace area
					add(new HashMap<String,Vector>() {{
						put("graceBound1", new Vector(-93,99,24));
					}});
					add(new HashMap<String,Vector>() {{
						put("graceBound2", new Vector(-71,84,44));
					}});
				}};
			//nether
			} else if (name.equalsIgnoreCase("nether")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(-12.503,86,20.478));
						put("spawn_2", new Vector(-16.478,86,24.449));
						put("spawn_3", new Vector(-20,86,20.508));
						put("spawn_4", new Vector(-16,86,16.491));
					}});
					//Grace area
					add(new HashMap<String,Vector>() {{
						put("graceBound1", new Vector(-7,82,11));
					}});
					add(new HashMap<String,Vector>() {{
						put("graceBound2", new Vector(-25,99,29));
					}});
				}};
			}
		} else if (realm == Realm.STEPSPLEEF) {
			//concrete
			if (name.equalsIgnoreCase("concrete")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(-46.436,65,-20.529));
						put("spawn_2", new Vector(-46.518,65,-26.581));
						put("spawn_3", new Vector(-58.528,65,-38.471));
						put("spawn_4", new Vector(-64.559,65,-38.560));
						put("spawn_5", new Vector(-76.530,65,-26.481));
						put("spawn_6", new Vector(-76.485,65,-20.451));
						put("spawn_7", new Vector(-64.480,65,-8.512));
						put("spawn_8", new Vector(-58.445,65,-8.523));
					}});
				}};
			} 
		} else if (realm == Realm.SKYWARS) {
			//first
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
			//nether
			} else if (name.equalsIgnoreCase("nether")) {
				mapInfo = new ArrayList<HashMap<String,Vector>>() {{
					//Points
					add(new HashMap<String,Vector>() {{
						put("spawn_1", new Vector(-150.482,76,29.523));
						put("spawn_2", new Vector(-135.472,77,-14.438));
						put("spawn_3", new Vector(-90.548,76,-29.470));
						put("spawn_4", new Vector(-45.536,76,-14.468));
						put("spawn_5", new Vector(-30.542,77,30.355));
						put("spawn_6", new Vector(-45.516,76,75.514));
						put("spawn_7", new Vector(-90.525,76,90.440));
						put("spawn_8", new Vector(-135.458,76,75.498));
					}});
				}};
			}
		}
		return mapInfo;
	}
}
