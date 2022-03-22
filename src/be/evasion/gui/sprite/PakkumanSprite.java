package be.evasion.gui.sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import be.evasion.maze.Direction;

public class PakkumanSprite extends Sprite {
	private SpriteSheet spriteSheet;
	private int imgToDisplayX;
	private int imgToDisplayY;
	private Map<Direction, Integer> sheetDirection;
	public PakkumanSprite(BufferedImage image, int x, int y, int width, int height) {
		super(image, x, y, width, height);
		this.imgToDisplayX = 0;
		this.imgToDisplayY = 0;
		this.sheetDirection = new HashMap<>();	
		this.sheetDirection.put(Direction.SOUTH, 3);
		this.sheetDirection.put(Direction.NORTH, 2);
		this.sheetDirection.put(Direction.WEST, 1);
		this.sheetDirection.put(Direction.EAST,0);
		
	}
	public PakkumanSprite(SpriteSheet spriteSheet, int x, int y, int width, int height){
		super(null, x, y, width, height);
		this.spriteSheet = spriteSheet;
		this.imgToDisplayX = 0;
		this.imgToDisplayY = 0;
		this.sheetDirection = new HashMap<>();	
		this.sheetDirection.put(Direction.SOUTH, 0);
		this.sheetDirection.put(Direction.NORTH, 3);
		this.sheetDirection.put(Direction.WEST, 1);
		this.sheetDirection.put(Direction.EAST, 2);
	}
	public void draw(Graphics g){
		if(getDirection() == Direction.NORTH || getDirection() == Direction.SOUTH){
			g.drawImage(spriteSheet.crop(sheetDirection.get(getDirection()), imgToDisplayY), getX(), getY(), getWidth(), getHeight(), null);
		}
		if(getDirection() == Direction.EAST || getDirection() == Direction.WEST){
			g.drawImage(spriteSheet.crop(sheetDirection.get(getDirection()), imgToDisplayX), getX(), getY(), getWidth(), getHeight(), null);
		}
		if(getTraveledPixelX()%SpriteSheet.getSpeed() == 0){
			imgToDisplayX++;
		}
		if(getTraveledPixelY()%SpriteSheet.getSpeed() == 0){
			imgToDisplayY++;
		}
		if(imgToDisplayX==spriteSheet.getMaxSheet()){
			imgToDisplayX=0;
		}
		if(imgToDisplayY==spriteSheet.getMaxSheet()){
			imgToDisplayY=0;
		}
	}
}
