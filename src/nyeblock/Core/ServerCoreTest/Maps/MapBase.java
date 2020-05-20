package nyeblock.Core.ServerCoreTest.Maps;

import java.io.File;
import java.util.ArrayList;

import nyeblock.Core.ServerCoreTest.Misc.Enums.MapPointType;

public abstract class MapBase {
	private String name;
	private int maxPlayers;
	private String creator;
	private File schematicFile;
	private File clearSchematicFile;
	private ArrayList<MapPoint> points;
	
	public MapBase(String name, String creator, String schematic) {
		this.name = name;
		this.creator = creator;
		schematicFile = new File(schematic);
		String[] split = schematic.split("/maps");
		clearSchematicFile = new File(split[0] + "/maps/clear" + split[1]);
	}
	
	//
	// GETTERS
	//
	
	public String getName() {
		return name;
	}
	public int getMaxPlayers() {
		return maxPlayers;
	}
	public String getCreator() {
		return creator;
	}
	public File getSchematicFile() {
		return schematicFile;
	}
	public File getClearSchematicFile() {
		return clearSchematicFile;
	}
	public ArrayList<MapPoint> getTypePoints(MapPointType type) {
		ArrayList<MapPoint> returnPoints = new ArrayList<>();
		
		for (MapPoint point : points) {
			if (point.getType().equals(type)) {
				returnPoints.add(point);
			}
		}
		return returnPoints;
	}
	public ArrayList<MapPoint> getPoints() {
		return points;
	}
	
	//
	// SETTERS
	//
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public void setPoints(ArrayList<MapPoint> points) {
		this.points = points;
	}
}
