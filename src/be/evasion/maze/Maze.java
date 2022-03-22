package be.evasion.maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.evasion.maze.item.Candy;
import be.evasion.maze.item.Item;
import be.evasion.maze.item.Monster;
import be.evasion.util.Index;

public class Maze {
	private int rows, cols;
	private MazeBox[][] maze;
	private HashMap<Integer, Monster> monsters;
	private HashMap<Integer, Candy> candies;
	private Index pakkumanLocation, initialPakkumanLocation;
	private SPSolver solver;
	
	public Maze(int  rows, int cols){
		this.rows = rows;
		this.cols = cols;
		this.monsters = new HashMap<>();
		this.candies = new HashMap<>();
		this.maze = new MazeBox[this.rows][this.cols];
		this.solver = null;
		buildMaze();
	}
	private void buildMaze(){
		for(int row = 0; row < rows; row++){
			for(int col = 0; col < cols; col++)
				maze[row][col] = new MazeBox(row, col);
		}
	}
	public void openFor(int row, int col, Direction direction){
		maze[row][col].open(direction);
	}
	public void putCandyFor(Index location){
		Candy candy = new Candy();
		candies.put(getIntKey(location), candy);
	}
	public void putMonsterFor(Index location){
		Monster monster = new Monster();
		monsters.put(getIntKey(location), monster);
	}
	public boolean monsterOn(Index location){
		return monsters.get(getIntKey(location)) != null;
	}
	public boolean monsterOn(int row, int col){
		return monsters.get(getIntKey(new Index(row, col))) != null;
	}
	public boolean monsterOn(Integer key){
		return monsters.get(key) != null;
	}
	public boolean candyOn(Index location){
		return candies.get(getIntKey(location)) != null;
	}
	public boolean candyOn(Integer key){
		return candies.get(key) != null;
	}
	public boolean candyOn(int row, int col){
		return candies.get(getIntKey(new Index(row, col))) != null;
	}
	public Monster getMonsterOn(Index location){
		return monsters.get(getIntKey(location));
	}
	public Monster getMonsterOn(Integer key){
		return monsters.get(key);
	}
	public Candy getCandyOn(Index location){
		return candies.get(getIntKey(location));
	}
	public Candy getCandyOn(Integer key){
		return candies.get(key);
	}
	public Monster getMonsterOn(int row, int col){
		return monsters.get(getIntKey(new Index(row, col)));
	}
	public Candy getCandyOn(int row, int col){
		return candies.get(getIntKey(new Index(row, col)));
	}

	public boolean itemOn(Index location){
		return monsterOn(location) || candyOn(location);
	}
	public boolean itemOn(int row, int col){
		return monsterOn(row, col) || candyOn(row, col);
	}
	public Item getItemOn(Index location){
		return monsterOn(location) ? getMonsterOn(location) : getCandyOn(location);
	}
	public Item getItemOn(Integer key){
		return monsterOn(key) ? getMonsterOn(key) : getCandyOn(key);
	}
	public Item getItemOn(int row, int col){
		return monsterOn(row, col) ? getMonsterOn(row, col) : getCandyOn(row, col);
	}
	public void movePakkuman(Direction direction){
		pakkumanLocation.incRow(direction.getRow());
		pakkumanLocation.incCol(direction.getCol());
	}
	public int getRows() {
		return rows;
	}
	public int getCols() {
		return cols;
	}
	public int getMonstersSize(){
		return monsters.size();
	}
	public int getCandiesSize(){
		return candies.size();
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public void setCols(int cols) {
		this.cols = cols;
	}
	public MazeBox get(int row, int col){
		return maze[row][col];
	}
	public MazeBox get(Index location){
		return get(location.getRow(), location.getCol());
	}
	public MazeBox get(int key){
		return get(parseLocation(key));
	}
	public Map<Integer, Monster> getMonsters(){
		return monsters;
	}
	public Map<Integer, Candy> getCandies(){
		return candies;
	}
	public Index getPakkumanLocation() {
		return pakkumanLocation;
	}
	public Index getInitialPakkumanLocation() {
		return new Index(initialPakkumanLocation);
	}
	public void setPakkumanLocation(Index pakkumanLocation) {
		this.pakkumanLocation = pakkumanLocation;
	}
	public void setInitialPakkumanLocation(Index initialPakkumanLocation) {
		this.initialPakkumanLocation = initialPakkumanLocation;
		if(pakkumanLocation == null){
			pakkumanLocation = new Index(initialPakkumanLocation);
		}
	}
	public Index getEnd(){
		return new Index(rows-1, cols-1);
	}
	public int getIntKey(int row, int col){
		return cols*row+col;
	}
	public int getIntKey(Index location){
		return cols*location.getRow()+location.getCol();
	}
	public Index parseLocation(int intKey){
		int row = (int)(intKey/cols), col = (int)(intKey%cols);
		return new Index(row, col);
	}
	public List<Integer> candiesLocationAsList() {
		return new ArrayList<Integer>(candies.keySet());
	}
	public List<Integer> monstersLocationAsList() {
		return new ArrayList<Integer>(monsters.keySet());
	}
	public MazePath getShortestPath(){
		return getShortestPath(false);
	}
	public MazePath getShortestPath(boolean recalculate){
		if(solver == null || recalculate){
			solver = new SPSolver(this);
		}
		return solver.getShortestPath();
	}
	public boolean isSolved(){
		if(solver != null){
			return solver.isSolved();
		}
		return false;
	}
	public boolean isBlocked(){
		if(solver != null){
			return solver.isBlocked();
		}
		return false;
	}
	public int getBlockingMonsterID(){
		if(solver != null){
			return solver.getBlockingMonsterID();
		}
		return -1;
	}
	@Override
	public String toString(){
		String str = "[\n";
		for(int row = 0; row < rows; row++){
			str+="[";
			for(int col = 0; col < cols; col++)
				str += maze[row][col].toString()+" ";
			str += "]\n";
		}
		str += "]";
		return str;
	}
}
