package be.evasion.maze.item;

public abstract class Item {
	private final int ID;
	private final String NAME;
	private boolean destroy;
	public Item(int id){
		this.ID = id;
		this.NAME = null;
		this.destroy = false;
	}
	public Item(int id, String name){
		this.ID = id;
		this.NAME = name;
		this.destroy = false;
	}
	public Integer getID(){
		return ID;
	}
	public String getName(){
		return NAME==null ? getGeneratedName() : NAME;
	}
	public boolean isDestroy() {
		return destroy;
	}
	public void setDestroy(boolean destroy) {
		this.destroy = destroy;
	}
	public String getGeneratedName(){
		return getClass().getSimpleName().toString()+" n°"+getID();
	}
	@Override
	public String toString(){
		return getName();
	}
}
