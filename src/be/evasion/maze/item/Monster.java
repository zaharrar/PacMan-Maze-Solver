package be.evasion.maze.item;

public class Monster extends Item {
	private static int created;
	private boolean killed;
	static{
		created = 1;
	}
	public Monster(){
		super(created);
		created++;
		this.killed = false;
	}
	public Monster(String name){
		super(created, name);
		created++;
	}
	public void setKilled(boolean killed){
		this.killed = killed;
	}
	public boolean isKilled(){
		return killed;
	}
}
