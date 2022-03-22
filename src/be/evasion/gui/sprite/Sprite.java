package be.evasion.gui.sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import be.evasion.maze.Direction;
import be.evasion.maze.item.Item;

public class Sprite {
	private int width, height;
	private BufferedImage image;
	private int x, y;
	private int traveledPixelX;
	private int traveledPixelY;
	private Direction currentDirection;
	private Item associate;

	public Sprite(BufferedImage image, int x, int y, int width, int height){
		this(image, x, y, width, height, null);
	}
	public Sprite(BufferedImage image, int x, int y, int width, int height, Item associate){
		this.image = image;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.traveledPixelX = 0;
		this.traveledPixelY = 0;
		this.currentDirection = Direction.SOUTH;
		this.associate = associate;
	}
	public void draw(Graphics g){
		if(associate == null || !associate.isDestroy()){
			g.drawImage(image, x, y, width, height, null);
		}
	}
	public void move(Direction currentDirection){
		int xs = currentDirection.getCol();
		int ys = currentDirection.getRow();
		this.currentDirection = currentDirection;
		x += xs;
		y += ys;
		traveledPixelX+=Math.abs(xs);
		traveledPixelY+=Math.abs(ys);
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getTraveledPixelX() {
		return traveledPixelX;
	}
	public int getTraveledPixelY() {
		return traveledPixelY;
	}
	public Direction getDirection(){
		return currentDirection;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
}
