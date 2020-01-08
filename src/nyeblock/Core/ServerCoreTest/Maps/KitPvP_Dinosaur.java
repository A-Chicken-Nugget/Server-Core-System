package nyeblock.Core.ServerCoreTest.Maps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;

@SuppressWarnings("serial")
public class KitPvP_Dinosaur extends MapBase {
	public KitPvP_Dinosaur(GameBase game) {
		super("Dinosaur Park",null,"./plugins/ServerCoreTest/maps/2_dino.bd");
		World world = Bukkit.getWorld(game.getWorldName());
		
		setPoints(new ArrayList<MapPoint>() {{
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,14.5,76,54.5,-90,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,18.5,76,58.5,180,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,22.5,76,54.5,90,0),-1));
			add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,18.5,76,50.5,0,0),-1));
			add(new MapPoint(MapPointType.GRACE_BOUND,new Location(world,31.2,66.2,41.2),-1));
			add(new MapPoint(MapPointType.GRACE_BOUND,new Location(world,6.6,87.3,66.3),-1));
		}});
	}
}
