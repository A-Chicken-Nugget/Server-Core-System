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
						put("spawn_1", new Location(world,-7.5,52,34.5,-90,0));
						put("spawn_2", new Location(world,-3.5,52,38.5,180,0));
						put("spawn_3", new Location(world,.5,52,34.5,90,0));
						put("spawn_4", new Location(world,-3.5,52,30.5,0,0));
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
						put("spawn_1", new Location(world,-7.5,52,34.5,-90,0));
						put("spawn_2", new Location(world,-3.5,52,38.5,180,0));
						put("spawn_3", new Location(world,.5,52,34.5,90,0));
						put("spawn_4", new Location(world,-3.5,52,30.5,0,0));
					}});
					//Grace area
					add(new HashMap<String,Location>() {{
						put("graceBound1", new Location(world,4.82,49.35,26.25));
					}});
					add(new HashMap<String,Location>() {{
						put("graceBound2", new Location(world,-12.18,60.56,42.51));
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
						put("spawn_1", new Location(world,-20.5,62,-2.5,0,0));
						put("spawn_2", new Location(world,-39.5,62,16.5,-90,0));
						put("spawn_3", new Location(world,-20.5,62,35.5,180,0));
						put("spawn_4", new Location(world,-1.5,62,16.5,90,0));
					}});
				}};
			} 
		} else if (realm == Realm.SKYWARS) {
			if (map.equalsIgnoreCase("savanna")) {
				mapInfo = new ArrayList<HashMap<String,Location>>() {{
					add(new HashMap<String,Location>() {{
						put("spawn_1", new Location(world,-92.5,58,109.5));
						put("spawn_2", new Location(world,-73.5,58,66.5));
						put("spawn_3", new Location(world,-92.5,57,23.5));
						put("spawn_4", new Location(world,-130.5,58,13.5));
						put("spawn_5", new Location(world,-164.5,58,27.5));
						put("spawn_6", new Location(world,-183.5,58,70.5));
						put("spawn_7", new Location(world,-164.5,57,113.5));
						put("spawn_8", new Location(world,-126.5,58,123.5));
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
							put("spawn_1", new Location(world,-83.5,81,-37.5,180,0));
							put("spawn_2", new Location(world,-83.5,81,-57.5,0,0));
						}});
					} else {
						//Points
						add(new HashMap<String,Location>() {{
							put("spawn_1", new Location(world,-86.5,81,-37.5,-180,0));
							put("spawn_2", new Location(world,-80.5,81,-37.5,180,0));
							put("spawn_3", new Location(world,-86.5,81,-57.5,0,0));
							put("spawn_4", new Location(world,-80.5,81,-57.5,0,0));
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
			if (map.equalsIgnoreCase("savanna")) {
				//Island chests
				chestInfo.put(new Vector(-86.5,32,109.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-80.5,40,109.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-81.5,43,115.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-81.5,45,86.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(-67.5,32,66.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-61.5,40,66.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-62.5,43,72.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-81.5,46,49.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(-86.5,31,23.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-80.5,39,23.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-81.5,42,29.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-109.5,45,20.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(-130.5,32,7.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-130.5,40,1.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-124.5,43,2.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-146.5,44,22.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(-170.5,32,27.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-176.5,40,27.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-175.5,43,21.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-169.5,45,49.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(-189.5,32,70.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-195.5,40,70.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-194.5,43,64.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-170.5,45,85.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(-170.5,31,113.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-176.5,39,113.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-175.5,42,107.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-147.5,45,115.5),ChestValue.MEDIUM);
				chestInfo.put(new Vector(-126.5,32,129.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-126.5,40,135.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-132.5,43,134.5),ChestValue.COMMON);
				chestInfo.put(new Vector(-110.5,45,117.5),ChestValue.MEDIUM);
				//Ring chests
				chestInfo.put(new Vector(-128.5,38,102.5),ChestValue.HIGH);
				chestInfo.put(new Vector(-104.5,38,93.5),ChestValue.HIGH);
				chestInfo.put(new Vector(-94.5,38,68.5),ChestValue.HIGH);
				chestInfo.put(new Vector(-104.5,38,43.5),ChestValue.HIGH);
				chestInfo.put(new Vector(-128.5,38,34.5),ChestValue.HIGH);
				chestInfo.put(new Vector(-152.5,38,43.5),ChestValue.HIGH);
				chestInfo.put(new Vector(-162.5,38,68.5),ChestValue.HIGH);
				chestInfo.put(new Vector(-152.5,38,93.5),ChestValue.HIGH);
				//Middle island chests
				chestInfo.put(new Vector(-135.5,39,72.5),ChestValue.LEGENDARY);
				chestInfo.put(new Vector(-129.5,42,61.5),ChestValue.LEGENDARY);
				chestInfo.put(new Vector(-120.5,42,60.5),ChestValue.LEGENDARY);
				chestInfo.put(new Vector(-115.5,40,70.5),ChestValue.LEGENDARY);
				chestInfo.put(new Vector(-130.5,33,71.5),ChestValue.LEGENDARY);
			}
		}
		return chestInfo;
	}
}
