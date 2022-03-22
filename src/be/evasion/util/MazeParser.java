package be.evasion.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import be.evasion.maze.Direction;
import be.evasion.maze.Maze;

public class MazeParser {
	
	private int rows, cols, nbMonsters, nbCandies;
	private BufferedReader br;
	private Maze maze;
	public MazeParser(String fileName){
		try {
			br = new BufferedReader(new FileReader(new File(fileName)));
			initRowsCols();
			maze = new Maze(rows, cols);
			initMaze();
			br.readLine(); // Elements du Labyrinthe: 
			nbMonsters = Integer.parseInt(br.readLine().split(":")[1].replaceAll(" ", ""));
			nbCandies = Integer.parseInt(br.readLine().split(":")[1].replaceAll(" ", ""));
			br.readLine(); // Emplacements:
			Index pakkumanLocation = Index.parseIndex(br.readLine().split(":")[1]);
			maze.setInitialPakkumanLocation(pakkumanLocation);
			putMonsters();
			putCandies();
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("The file "+fileName+" doesn't exist !");
		} catch (IOException e) {
			System.out.println("A problem occured while parsing the file "+fileName+" !");
		}
	}
	private void initRowsCols() throws IOException{
		String[] infos = br.readLine().replaceAll(" ", "").split(":")[1].split("fois");
		rows = Integer.parseInt(infos[0]);
		cols = Integer.parseInt(infos[1]);
	}
	private void putCandies() throws IOException{
		String[] locationArray = br.readLine().split(":")[1].replaceFirst(" ", "").split(" ");
		for(int i=0; i < nbCandies; i++){
			maze.putCandyFor(Index.parseIndex(locationArray[i]));
		}
	}
	private void putMonsters() throws IOException{
		String[] locationArray = br.readLine().split(":")[1].replaceFirst(" ", "").split(" ");
		for(int i=0; i < nbMonsters; i++){
			maze.putMonsterFor(Index.parseIndex(locationArray[i]));
		}
	}
	private void initMaze() throws IOException{
		br.readLine(); // North edge
		for(int row = 0; row < rows-1; row++){
			parseVerticalWalls(br.readLine(), row);
			parseHorizontalWalls(br.readLine(), row);
		}
		parseVerticalWalls(br.readLine(), rows-1);
		br.readLine(); // South edge
	}
	private void parseVerticalWalls(String line, int row){
		int linePos = 1; // Deny first extremity wall
		for (int col = 0; col < cols-1; col++){ // Wall on a westLine = 6 - 2 extremity
			linePos+=3; // Deny case
			if (!isEasthWall(line.charAt(linePos))){
				maze.openFor(row, col, Direction.EAST);
				maze.openFor(row, col+1, Direction.WEST);
			}
			linePos+=1; // EasthWall checked
		}
	}
	private void parseHorizontalWalls(String line, int row){
		int linePos = 0;
		for (int col = 0; col < cols; col++){ // cols walls on a north/south edge
			linePos+=1; // Deny the '+'
			if (!isSouthWall(line.charAt(linePos))){
				maze.openFor(row, col, Direction.SOUTH);
				maze.openFor(row+1, col, Direction.NORTH);
			}
			linePos+=3; // SouthWall checked
		}
	}
	private static boolean isEasthWall(char wall){
		return wall == '|';
	}
	private static boolean isSouthWall(char wall){
		return wall == '-';
	}
	public int getNbMonsters() {
		return nbMonsters;
	}
	public int getNbCandies() {
		return nbCandies;
	}
	public Maze getMaze(){
		return maze;
	}
}
