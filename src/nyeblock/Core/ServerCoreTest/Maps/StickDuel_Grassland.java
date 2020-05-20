package nyeblock.Core.ServerCoreTest.Maps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

@SuppressWarnings("serial")
public class StickDuel_Grassland extends MapBase {
	public StickDuel_Grassland(GameBase game) {
		super("Grassland",null,"./plugins/ServerCoreTest/maps/stickDuel_grassland.bd");
		World world = Bukkit.getWorld(game.getWorldName());
		
		setPoints(new ArrayList<MapPoint>() {{
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-32.5,35,1.5,-90,0),1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,1.5,35,1.5,90,0),2));
			add(new MapPoint(MapPointType.BED_SPAWN,new Location(world,-37.5,35,1.5,-90,0),1));
			add(new MapPoint(MapPointType.BED_SPAWN,new Location(world,6.5,35,1.5,90,0),2));
		}});
	}
}
