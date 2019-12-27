package nyeblock.Core.ServerCoreTest.Realms;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class GameMapInfo {
	@SuppressWarnings("serial")
	//Get vectors for specific maps used in games
	public ArrayList<HashMap<String,Location>> getMapInfo(GameBase game) {
		ArrayList<HashMap<String,Location>> mapInfo = null;
		Realm realm = game.getRealm();
		World world = Bukkit.getWorld(game.getWorldName());
		String map = game.getMap();
		
		if (realm == Realm.KITPVP) {
			//concrete
			if (map.equalsIgnoreCase("concrete")) {
				mapInfo = new ArrayList<HashMap<String,Location>>() {{
					//Points
					add(new HashMap<String,Location>() {{
						put("spawn_1", new Location(world,-10.481,74,37.447,90,1.5F));
						put("spawn_2", new Location(world,-16.472,74,43.521,-180,1.5F));
						put("spawn_3", new Location(world,-22.511,74,37.532,-90,1.5F));
						put("spawn_4", new Location(world,-16.504,74,31.515,.5F,1.5F));
					}});
					//Grace area
					add(new HashMap<String,Location>() {{
						put("graceBound1", new Location(world,-3,87,23));
					}});
					add(new HashMap<String,Location>() {{
						put("graceBound2", new Location(world,-29,69,50));
					}});
				}};
			//grassland
			} else if (map.equalsIgnoreCase("grassland")) {
				mapInfo = new ArrayList<HashMap<String,Location>>() {{
					//Points
					add(new HashMap<String,Location>() {{
						put("spawn_1", new Location(world,-78.526,89,35.524,90,1.5F));
						put("spawn_2", new Location(world,-82,89,39.468,-180,1.5F));
						put("spawn_3", new Location(world,-86.497,89,35.517,-90,1.5F));
						put("spawn_4", new Location(world,-82.520,89,31.547,0,1.5F));
					}});
					//Grace area
					add(new HashMap<String,Location>() {{
						put("graceBound1", new Location(world,-93,99,24));
					}});
					add(new HashMap<String,Location>() {{
						put("graceBound2", new Location(world,-71,84,44));
					}});
				}};
			//nether
			} else if (map.equalsIgnoreCase("nether")) {
				mapInfo = new ArrayList<HashMap<String,Location>>() {{
					//Points
					add(new HashMap<String,Location>() {{
						put("spawn_1", new Location(world,-12.503,86,20.478,90,1.5F));
						put("spawn_2", new Location(world,-16.478,86,24.449,-180,1.5F));
						put("spawn_3", new Location(world,-20,86,20.508,-90,1.5F));
						put("spawn_4", new Location(world,-16.577,86,16.481,1.2F,1.5F));
					}});
					//Grace area
					add(new HashMap<String,Location>() {{
						put("graceBound1", new Location(world,-7,82,11));
					}});
					add(new HashMap<String,Location>() {{
						put("graceBound2", new Location(world,-25,99,29));
					}});
				}};
			}
		} else if (realm == Realm.STEPSPLEEF) {
			//concrete
			if (map.equalsIgnoreCase("concrete")) {
				mapInfo = new ArrayList<HashMap<String,Location>>() {{
					//Points
					add(new HashMap<String,Location>() {{
						put("spawn_1", new Location(world,-46.436,65,-20.529));
						put("spawn_2", new Location(world,-46.518,65,-26.581));
						put("spawn_3", new Location(world,-58.528,65,-38.471));
						put("spawn_4", new Location(world,-64.559,65,-38.560));
						put("spawn_5", new Location(world,-76.530,65,-26.481));
						put("spawn_6", new Location(world,-76.485,65,-20.451));
						put("spawn_7", new Location(world,-64.480,65,-8.512));
						put("spawn_8", new Location(world,-58.445,65,-8.523));
					}});
				}};
			} 
		} else if (realm == Realm.SKYWARS) {
//			//first
//			if (map.equalsIgnoreCase("first")) {
//				mapInfo = new ArrayList<HashMap<String,Location>>() {{
//					//Points
//					add(new HashMap<String,Location>() {{
//						put("spawn_1", new Location(world,3.522,75,-2.43));
//						put("spawn_2", new Location(world,45.456,75,6.565));
//						put("spawn_3", new Location(world,54.464,74,48.502));
//						put("spawn_4", new Location(world,45.452,75,90.433));
//						put("spawn_5", new Location(world,3.434,75,100.480));
//						put("spawn_6", new Location(world,-38.528,75,90.442));
//						put("spawn_7", new Location(world,-47.506,75,48.541));
//						put("spawn_8", new Location(world,-38.550,75,6.414));
//					}});
//				}};
//			//nether
//			} else if (map.equalsIgnoreCase("nether")) {
//				mapInfo = new ArrayList<HashMap<String,Location>>() {{
//					//Points
//					add(new HashMap<String,Location>() {{
//						put("spawn_1", new Location(world,-150.482,76,29.523));
//						put("spawn_2", new Location(world,-135.472,77,-14.438));
//						put("spawn_3", new Location(world,-90.548,76,-29.470));
//						put("spawn_4", new Location(world,-45.536,76,-14.468));
//						put("spawn_5", new Location(world,-30.542,77,30.355));
//						put("spawn_6", new Location(world,-45.516,76,75.514));
//						put("spawn_7", new Location(world,-90.525,76,90.440));
//						put("spawn_8", new Location(world,-135.458,76,75.498));
//					}});
//				}};
//			}
			if (map.equalsIgnoreCase("savanna")) {
				mapInfo = new ArrayList<HashMap<String,Location>>() {{
					add(new HashMap<String,Location>() {{
						put("spawn_1", new Location(world,12.5,121,64.5));
						put("spawn_2", new Location(world,-6.5,121,107.5));
						put("spawn_3", new Location(world,12.5,120,150.5));
						put("spawn_4", new Location(world,50.5,121,160.5));
						put("spawn_5", new Location(world,84.5,121,146.5));
						put("spawn_6", new Location(world,103.5,121,103.5));
						put("spawn_7", new Location(world,84.5,120,60.5));
						put("spawn_8", new Location(world,46.5,121,50.5));
					}});
				}};
			}
		} else if (realm == Realm.PVP) {
			PvP pvpGame = ((PvP)game);
			
			//nether
			if (map.equalsIgnoreCase("nether")) {
				mapInfo = new ArrayList<HashMap<String,Location>>() {{
					if (pvpGame.getPvPMode() == PvPMode.DUELS) {
						//Points
						add(new HashMap<String,Location>() {{
							put("spawn_1", new Location(world,7.503,54,6.493,0,1.5F));
							put("spawn_2", new Location(world,7.503,54,26.541,-180,1.5F));
						}});
					} else {
						//Points
						add(new HashMap<String,Location>() {{
							put("spawn_1", new Location(world,10.502,54,26.512,-180,1.5F));
							put("spawn_2", new Location(world,4.68,54,26.465,-180,1.5F));
							put("spawn_3", new Location(world,4.68,54,6.486,0,1.5F));
							put("spawn_4", new Location(world,10.502,54,6.486,0,1.5F));
						}});
					}
				}};
			}
		}
		return mapInfo;
	}
	//
	public HashMap<Vector,ChestValue> getChestInfo(GameBase game) {
		HashMap<Vector,ChestValue> chestInfo = new HashMap<>();
		Realm realm = game.getRealm();
		String map = game.getMap();
		
		if (realm == Realm.SKYWARS) {
//			if (map.equalsIgnoreCase("nether")) {
//				chestInfo.put(new Vector(-150.5,61,32.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-152.5,58,30.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-133.5,61,77.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-137.5,58,76.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-89.5,61,92.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-92.5,58,91.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-43.5,61,77.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-47.5,58,76.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-28.5,62,32.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-32.5,59,31.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-44.5,61,-12.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-47.5,58,-13.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-89.5,61,-27.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-92.5,58,-28.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-134.5,62,-12.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-137.5,59,-13.5),ChestValue.COMMON);
//				chestInfo.put(new Vector(-115.5,61,11.5),ChestValue.MEDIUM);
//				chestInfo.put(new Vector(-68.5,61,8.5),ChestValue.MEDIUM);
//				chestInfo.put(new Vector(-68.5,61,53.5),ChestValue.MEDIUM);
//				chestInfo.put(new Vector(-113.5,61,51.5),ChestValue.MEDIUM);
//				chestInfo.put(new Vector(-90.5,47,17.5),ChestValue.HIGH);
//				chestInfo.put(new Vector(-77.5,47,30.5),ChestValue.HIGH);
//				chestInfo.put(new Vector(-90.5,47,43.5),ChestValue.HIGH);
//				chestInfo.put(new Vector(-103.5,47,30.5),ChestValue.HIGH);
//				chestInfo.put(new Vector(-90.5,43,30.5),ChestValue.LEGENDARY);
//			}
			if (map.equalsIgnoreCase("savanna")) {
				//Island chests
				chestInfo.put(new Vector(6.5,95,64.5),ChestValue.COMMON);
				chestInfo.put(new Vector(.5,103,64.5),ChestValue.COMMON);
				chestInfo.put(new Vector(1.5,106,58.5),ChestValue.COMMON);
				chestInfo.put(new Vector(7.5,108,86.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(-12.5,95,107.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-18.5,103,107.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-17.5,105,101.5),ChestValue.COMMON);
				chestInfo.put(new Vector(6.5,108,122.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(6.5,94,150.5),ChestValue.COMMON);
				chestInfo.put(new Vector(.5,102,150.5),ChestValue.COMMON);
				chestInfo.put(new Vector(1.5,105,144.5),ChestValue.COMMON);
				chestInfo.put(new Vector(29.5,108,152.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(50.5,95,166.5),ChestValue.COMMON);
				chestInfo.put(new Vector(50.5,103,172.5),ChestValue.COMMON);
				chestInfo.put(new Vector(44.5,106,171.5),ChestValue.COMMON);
				chestInfo.put(new Vector(66.5,108,154.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(90.5,95,146.5),ChestValue.COMMON);
				chestInfo.put(new Vector(96.5,103,146.5),ChestValue.COMMON);
				chestInfo.put(new Vector(95.5,106,152.5),ChestValue.COMMON);
				chestInfo.put(new Vector(95.5,108,123.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(109.5,95,103.5),ChestValue.COMMON);
				chestInfo.put(new Vector(115.5,103,103.5),ChestValue.COMMON);
				chestInfo.put(new Vector(114.5,105,109.5),ChestValue.COMMON);
				chestInfo.put(new Vector(95.5,109,86.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(90.5,94,60.5),ChestValue.COMMON);
				chestInfo.put(new Vector(96.5,102,60.5),ChestValue.COMMON);
				chestInfo.put(new Vector(95.5,105,66.5),ChestValue.COMMON);
				chestInfo.put(new Vector(67.5,108,57.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(46.5,95,44.5),ChestValue.COMMON);
				chestInfo.put(new Vector(46.5,103,38.5),ChestValue.COMMON);
				chestInfo.put(new Vector(52.5,106,39.5),ChestValue.COMMON);
				chestInfo.put(new Vector(30.5,107,59.5),ChestValue.MEDIUM);
				//Ring chests
				chestInfo.put(new Vector(24.5,101,80.5),ChestValue.HIGH);
				chestInfo.put(new Vector(14.5,101,105.5),ChestValue.HIGH);
				chestInfo.put(new Vector(24.5,101,130.5),ChestValue.HIGH);
				chestInfo.put(new Vector(48.5,101,139.5),ChestValue.HIGH);
				chestInfo.put(new Vector(72.5,101,130.5),ChestValue.HIGH);
				chestInfo.put(new Vector(82.5,101,105.5),ChestValue.HIGH);
				chestInfo.put(new Vector(72.5,101,80.5),ChestValue.HIGH);
				chestInfo.put(new Vector(48.5,101,71.5),ChestValue.HIGH);
				//Middle island chests
				chestInfo.put(new Vector(41.5,102,109.5),ChestValue.LEGENDARY);
				chestInfo.put(new Vector(47.5,105,98.5),ChestValue.LEGENDARY);
				chestInfo.put(new Vector(56.5,105,97.5),ChestValue.LEGENDARY);
				chestInfo.put(new Vector(61.5,103,107.5),ChestValue.LEGENDARY);
				chestInfo.put(new Vector(46.5,96,108.5),ChestValue.LEGENDARY);
			}
		}
		return chestInfo;
	}
}
