package nyeblock.Core.ServerCoreTest.Maps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

@SuppressWarnings("serial")
public class StepSpleef_Concrete extends MapBase {
	public StepSpleef_Concrete(GameBase game) {
		super("Concrete",null,"./plugins/ServerCoreTest/maps/3_concrete.bd");
		World world = Bukkit.getWorld(game.getWorldName());
		
		setPoints(new ArrayList<MapPoint>() {{
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-20.5,62,-2.5,0,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-39.5,62,16.5,-90,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-20.5,62,35.5,180,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-1.5,62,16.5,90,0),-1));
		}});
	}
}
