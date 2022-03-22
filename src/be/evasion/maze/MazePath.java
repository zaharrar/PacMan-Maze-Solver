package be.evasion.maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MazePath implements Iterable<Direction>, Cloneable {
	List<Direction> path;
	public MazePath(){
		path = new ArrayList<>();
	}
	public boolean add(Direction direction){
		return path.add(direction);
	}
	public int size(){
		return path.size(); 
	}
	public Direction get(int i){
		return path.get(i);
	}
	public void reverse(){
		Collections.reverse(path);
	}
	public void concatenate(MazePath path){
		for(Direction direction : path){
			add(direction);
		}
	}
	@Override
	public String toString(){
		return path.toString();
	}
	@Override
	public Iterator<Direction> iterator() {
		return path.iterator();
	}
	@Override
	public MazePath clone(){
		MazePath path = new MazePath();
		for(Direction direction : this){
			path.add(direction);
		}
		return path;
	}
	@Override
	public boolean equals(Object obj){
		if(obj.getClass() != this.getClass()){
			return false;
		}
		MazePath path = (MazePath)obj;
		if(path.size() != size()){
			return false;
		}
		for(int i=0; i<size();i++){
			if(path.get(i) != get(i)){
				return false;
			}
		}
		return true;
	}
	public static MazePath opposite(MazePath path){
		MazePath opposite = new MazePath();
		for(Direction direction : path){
			opposite.add(direction.getOpposite());
		}
		opposite.reverse();
		return opposite;
	}
}
