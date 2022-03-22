package be.evasion.main;

import be.evasion.maze.Maze;
import be.evasion.util.MazeOutputProducer;
import be.evasion.util.MazeParser;

public class MazeEvasion{
	public static void main(String[] args){
		String filename = args[0];
		MazeParser parser = new MazeParser(filename);
		Maze maze = parser.getMaze();
		if(maze != null){
			MazeOutputProducer producer = new MazeOutputProducer(maze);
			producer.produceAll();
		}else{
			System.out.println("The parser didn't success to parse the maze.");
		}
	}
}
