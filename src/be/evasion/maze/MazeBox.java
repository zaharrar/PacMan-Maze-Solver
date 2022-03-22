package be.evasion.maze;

import java.util.HashMap;
import java.util.Map;

import be.evasion.util.Index;

public class MazeBox implements Comparable<MazeBox>{
	private Map<Direction, Boolean> wall;
	private Float distance; // Distance between this and the source
	private MazeBox previous; // Reference the previous MazeBox
	private Index location;
	
	public MazeBox(int row, int col){
		this.location = new Index(row, col);
		this.wall = new HashMap<>();
		for(Direction direction : Direction.values()){
			wall.put(direction, false);
		}
		this.distance = Float.POSITIVE_INFINITY;
		this.previous = null;
	}
	public void open(Direction direction){
		wall.put(direction, true);
	}
	public void close(Direction direction){
		wall.put(direction, false);
	}
	public boolean canGo(Direction direction){
		return wall.get(direction);
	}
	public Index getLocation() {
		return location;
	}
	public float getDistance() {
		return distance;
	}
	public MazeBox getPrevious() {
		return previous;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public void setPrevious(MazeBox previous) {
		this.previous = previous;
	}
	public boolean isAccessible(){
		return distance != Float.POSITIVE_INFINITY;
	}
	public Direction directionTo(MazeBox mazeBox){
		for(Direction direction : Direction.values()){
			if(location.getRow()+direction.getRow()==mazeBox.getLocation().getRow() && location.getCol()+direction.getCol()==mazeBox.getLocation().getCol()){
				if(canGo(direction)){
					return direction;
				}
			}
		}
		return null;
	}
	public String toString(){
		return location+String.format("(%s%s%s%s)",
			   canGo(Direction.NORTH) ? "N":"", canGo(Direction.SOUTH) ? "S":"", canGo(Direction.WEST) ? "W":"", canGo(Direction.EAST) ? "E":"");
	}
	@Override
	public int compareTo(MazeBox mazeBox){
		return distance.compareTo(mazeBox.getDistance());
	}
}
