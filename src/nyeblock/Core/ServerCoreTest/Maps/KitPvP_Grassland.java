package nyeblock.Core.ServerCoreTest.Maps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

@SuppressWarnings("serial")
public class KitPvP_Grassland extends MapBase {
	public KitPvP_Grassland(GameBase game) {
		super("Grassland",null,"./plugins/ServerCoreTest/maps/2_grassland.bd");
		World world = Bukkit.getWorld(game.getWorldName());
		
		setPoints(new ArrayList<MapPoint>() {{
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,.5,64,34.5,90,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-3.5,64,30.5,0,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-7.5,64,34.5,-90,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-3.5,64,38.5,180,0),-1));
			add(new MapPoint(MapPointType.GRACE_BOUND,new Location(world,-12.7,61.4,44.1),-1));
			add(new MapPoint(MapPointType.GRACE_BOUND,new Location(world,4.9,70,25.4),-1));
		}});
	}
}
