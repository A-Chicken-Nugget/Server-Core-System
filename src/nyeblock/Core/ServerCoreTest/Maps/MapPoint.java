package nyeblock.Core.ServerCoreTest.Maps;

import org.bukkit.Location;

import nyeblock.Core.ServerCoreTest.Misc.Enums.ChestValue;
import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;

public class MapPoint {
	private MapPointType type;
	private ChestValue chestValue;
	private Location location;
	private int position;
	
	public MapPoint(MapPointType type, Location location) {
		this.type = type;
		this.location = location;
	}
	
	public MapPoint(MapPointType type, Location location, int position) {
		this.type = type;
		this.location = location;
		this.position = position;
	}
	
	//
	// GETTERS
	//
	
	public MapPointType getType() {
		return type;
	}
	public ChestValue getChestValue() {
		return chestValue;
	}
	public Location getLocation() {
		return location;
	}
	public int getPosition() {
		return position;
	}
	
	//
	// SETTERS
	//
	
	public MapPoint setChestValue(ChestValue chestValue) {
		this.chestValue = chestValue;
		
		return this;
	}
}
