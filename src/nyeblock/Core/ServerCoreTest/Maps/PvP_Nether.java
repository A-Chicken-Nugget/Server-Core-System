package nyeblock.Core.ServerCoreTest.Maps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;
import nyeblock.Core.ServerCoreTest.Misc.Enums.PvPMode;
import nyeblock.Core.ServerCoreTest.Realms.GameBase;
import nyeblock.Core.ServerCoreTest.Realms.PvP;

@SuppressWarnings("serial")
public class PvP_Nether extends MapBase {
	public PvP_Nether(GameBase game) {
		super("Nether",null,"./plugins/ServerCoreTest/maps/5_nether.bd");
		World world = Bukkit.getWorld(game.getWorldName());
		
		if (((PvP)game).getPvPMode() == PvPMode.DUELS) {
			setPoints(new ArrayList<MapPoint>() {{
				add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-11.5,47,32.5,-90,0),1));
				add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,8.5,47,32.5,90,0),2));
			}});
		} else {
			setPoints(new ArrayList<MapPoint>() {{
				add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-11.5,47,29.5,-90,0),1));
				add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,-11.5,47,35.5,-90,0),2));
				add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,8.5,47,35.5,90,0),3));
				add(new MapPoint(MapPointType.PLAYER_SPAWN,new Location(world,8.5,47,29.5,90,0),4));
			}});
		}
	}
}
