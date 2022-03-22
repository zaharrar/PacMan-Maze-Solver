package be.evasion.gui;

import be.evasion.maze.Direction;
import be.evasion.maze.Maze;
import be.evasion.maze.MazePath;
import be.evasion.util.MazeParser;

public class MazeEvasionGUI implements Runnable{
	private static boolean running = false;
	private static  Maze maze;
	private MazeFrame mazeFrame;
	private Thread gameThread;
	
	public MazeEvasionGUI(Maze maze){
		MazeEvasionGUI.maze = maze;
		this.mazeFrame = new MazeFrame();
	}
	public synchronized void start(){
		if(running)return;
		running = true;
		gameThread = new Thread(this);
		gameThread.start();
	}
	@Override
	public void run() {
		mazeFrame.startLoading();
		MazePath shortestPath = maze.getShortestPath();
		mazeFrame.stopLoading();
		if (!maze.isSolved()){
			mazeFrame.pathNotFound();
			return;
		}
		mazeFrame.pathFound();
		for(Direction direction : shortestPath){
			maze.movePakkuman(direction);
			if(maze.itemOn(maze.getPakkumanLocation())){
				maze.getItemOn(maze.getPakkumanLocation()).setDestroy(true);
			}
			mazeFrame.movePakkuman(direction, maze.getPakkumanLocation());
		}
	}
	public static Maze getMaze(){
		return maze;
	}
	public static void main(String[] args) {
		String filename = args[0];
		MazeParser parser = new MazeParser(filename);
		Maze maze = parser.getMaze();
		if(maze != null){
			MazeEvasionGUI game = new MazeEvasionGUI(maze);
			game.start();
		}else{
			System.out.println("The parser didn't success to parse the maze.");
		}
		
	}
}
