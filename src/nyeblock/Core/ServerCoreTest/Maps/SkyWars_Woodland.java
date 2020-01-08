package nyeblock.Core.ServerCoreTest.Maps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

@SuppressWarnings("serial")
public class SkyWars_Woodland extends MapBase {
	public SkyWars_Woodland(GameBase game) {
		super("Woodland",null,"./plugins/ServerCoreTest/maps/4_woodland.bd");
		World world = Bukkit.getWorld(game.getWorldName());
		
		setPoints(new ArrayList<MapPoint>() {{
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,117.5,88,34.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,47.5,88,34.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-9.5,88,91.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-9.5,88,161.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,47.5,88,218.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,117,88,218.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,174.5,88,161.5),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,174.5,88,91.5),-1));	
			
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,130.5,70,27.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,104.5,70,27.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,117.5,60,23.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,60.5,70,27.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,34.5,70,27.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,47.5,60,23.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-16.5,70,78.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-16.5,70,104.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-20.5,60,91.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-16.5,70,148.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-16.5,70,174.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,-20.5,60,161.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,34.5,70,225.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,60.5,70,225.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,47.5,60,229.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,104.5,70,225.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,130.5,70,225.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,117.5,60,229.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,181.5,70,174.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,181.5,70,148.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,185.5,60,161.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,181.5,70,104.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,181.5,70,78.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,185.5,60,91.5),-1).setChestValue(ChestValue.COMMON));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,82.5,67,68.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,43.5,67,83.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,24.5,67,126.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,41.5,67,167.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,82.5,67,184.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,121.5,67,169.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,140.5,67,126.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,123.5,67,85.5),-1).setChestValue(ChestValue.MEDIUM));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,99.5,73,109.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,65.5,73,109.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,65.5,73,143.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,99.5,73,143.5),-1).setChestValue(ChestValue.HIGH));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,84.5,71,126.5),-1).setChestValue(ChestValue.LEGENDARY));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,82.5,71,124.5),-1).setChestValue(ChestValue.LEGENDARY));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,80.5,71,126.5),-1).setChestValue(ChestValue.LEGENDARY));
			add(new MapPoint(MapPointType.CHEST_SPAWN,new Location(world,82.5,71,128.5),-1).setChestValue(ChestValue.LEGENDARY));
		}});
	}
}
