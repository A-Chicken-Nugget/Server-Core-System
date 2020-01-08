package nyeblock.Core.ServerCoreTest.Maps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

@SuppressWarnings("serial")
public class SkyWars_Savanna extends MapBase {
	public SkyWars_Savanna(GameBase game) {
		super("Savanna",null,"./plugins/ServerCoreTest/maps/4_savanna.bd");
		World world = Bukkit.getWorld(game.getWorldName());
		
		setPoints(new ArrayList<MapPoint>() {{
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,75.5,80,23.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,37.5,81,13.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,3.5,81,27.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-15.5,81,70.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,3.5,80,113.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,41.5,81,123.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,75.5,81,109.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,94.5,81,66.5),-1));
			
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,81.5,54,23.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,87.5,62,23.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,86.5,65,29.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,58.5,68,20.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,37.5,55,7.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,37.5,63,1.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,43.5,66,2.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,21.5,67,22.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-2.5,55,27.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-8.5,63,27.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-7.5,66,21.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-1.5,68,49.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-21.5,55,70.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-27.5,63,70.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-26.5,66,64.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-2.5,68,85.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-2.5,54,113.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-8.5,62,113.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-7.5,65,107.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,20.5,68,115.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,41.5,55,129.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,41.5,63,135.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,35.5,66,134.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,57.5,68,117.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,81.5,55,109.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,87.5,63,109.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,86.5,66,115.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,86.5,68,86.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,100.5,55,66.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,106.5,63,66.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,105.5,66,72.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,86.5,69,49.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,63.5,61,43.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,39.5,61,34.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,15.5,61,43.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,5.5,61,68.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,15.5,61,93.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,39.5,61,102.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,63.5,61,93.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,73.5,61,68.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,52.5,63,70.5),-1).setChestValue(ChestValue.LEGENDARY));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,32.5,62,72.5),-1).setChestValue(ChestValue.LEGENDARY));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,37.5,56,71.5),-1).setChestValue(ChestValue.LEGENDARY));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,47.65,65,60.5),-1).setChestValue(ChestValue.LEGENDARY));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,38.5,65,61.5),-1).setChestValue(ChestValue.LEGENDARY));
		}});
	}
}
