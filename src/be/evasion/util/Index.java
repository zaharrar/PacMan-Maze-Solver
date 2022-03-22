package be.evasion.util;

import be.evasion.maze.Direction;

public class Index{
	private int row;
	private int col;
	
	public Index(){
		this.row = 0;
		this.col = 0;
	}
	public Index(int first, int second) {
		this.row = first;
		this.col = second;
	}
	public Index(Index index){
		this.row = index.getRow();
		this.col = index.getCol();
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public void incRow(int inc){
		row+=inc;
	}
	public void incCol(int inc){
		col+=inc;
	}
	public void inc(Direction dir){
		row+=dir.getRow();
		col+=dir.getCol();
	}
	@Override
	public String toString(){
		return "("+row+", "+col+")";
	}
	public static Index parseIndex(String str){
		str = str.replaceAll(" ", "").replaceAll("[()]", "");
		String[] infos = str.split(",");
		return new Index(Integer.parseInt(infos[0]),Integer.parseInt(infos[1]));
		
	}
}
