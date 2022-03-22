package be.evasion.maze;

public enum Direction {
	NORTH(-1, 0, "nord"), SOUTH(+1, 0, "sud"), EAST(0, +1, "est"), WEST(0, -1, "ouest");
	private final int row;
	private final int col;
	private final String name;
	private Direction opposite;
	static{
		NORTH.opposite=SOUTH;
		SOUTH.opposite=NORTH;
		EAST.opposite=WEST;
		WEST.opposite=EAST;
	}
	private Direction(int row, int col, String name) {
		this.row = row;
		this.col = col;
		this.name = name;
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
	public Direction getOpposite(){
		return opposite;
	}
	public String getName(){
		return name;
	};
};

