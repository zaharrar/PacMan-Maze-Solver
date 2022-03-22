package be.evasion.maze;

import java.util.PriorityQueue;

public class MazeHeap extends PriorityQueue<MazeBox>{
	private static final long serialVersionUID = 4975460771680333240L;
	
	public MazeBox removeMin(){
		return poll();
	}
	public void refresh(MazeBox mazebox){
		remove(mazebox);
		add(mazebox);
	}
}
