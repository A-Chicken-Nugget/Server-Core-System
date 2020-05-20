package nyeblock.Core.ServerCoreTest.Realms;

import java.util.ArrayList;
import java.util.Random;


import nyeblock.Core.ServerCoreTest.Maps.KitPvP_Dinosaur;
import nyeblock.Core.ServerCoreTest.Maps.KitPvP_Grassland;
import nyeblock.Core.ServerCoreTest.Maps.MapBase;
import nyeblock.Core.ServerCoreTest.Maps.PvP_Nether;
import nyeblock.Core.ServerCoreTest.Maps.SkyWars_Savanna;
import nyeblock.Core.ServerCoreTest.Maps.SkyWars_Woodland;
import nyeblock.Core.ServerCoreTest.Maps.StepSpleef_Concrete;
import nyeblock.Core.ServerCoreTest.Maps.StickDuel_Grassland;
import nyeblock.Core.ServerCoreTest.Misc.Enums.Realm;

public class GameMapInfo {
	public static MapBase getRandomMap(GameBase game) {
		MapBase map = null;
		ArrayList<MapBase> mapPool = new ArrayList<>();
		Realm realm = game.getRealm();
		
		if (realm == Realm.KITPVP) {
			mapPool.add(new KitPvP_Grassland(game));
			mapPool.add(new KitPvP_Dinosaur(game));
		} else if (realm == Realm.STEPSPLEEF) {
			mapPool.add(new StepSpleef_Concrete(game));
		} else if (realm == Realm.SKYWARS) {
			mapPool.add(new SkyWars_Savanna(game));
			mapPool.add(new SkyWars_Woodland(game));
		} else if (realm == Realm.PVP_DUELS_FISTS || realm == Realm.PVP_2V2_FISTS) {
			mapPool.add(new PvP_Nether(game));
		} else if (realm == Realm.STICK_DUEL) {
			mapPool.add(new StickDuel_Grassland(game));
		}
		map = mapPool.get(new Random().nextInt(mapPool.size()));
		
		return map;
	}
}
