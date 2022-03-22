package be.evasion.maze.item;

public class Candy extends Item {
	private static int created;

	static{
		created = 1;
	}
	public Candy(){
		super(created);
		created++;
	}
	public Candy(String name){
		super(created, name);
		created++;
	}
}
