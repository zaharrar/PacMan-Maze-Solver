package be.evasion.gui.sprite;

import java.awt.image.BufferedImage;

public class SpriteSheet {

	private static final int SPEED = 10;
	private BufferedImage sheet;
	private int width;
	private int height;
	private int maxSheet;
	public SpriteSheet(BufferedImage sheet, int width, int height, int maxSheet){
		this.width = width;
		this.height = height;
		this.sheet = sheet;
		this.maxSheet = maxSheet;
	}
	public BufferedImage crop(int row, int col){
		return sheet.getSubimage(col * width, row * height, width, height);
	}
	public int getMaxSheet(){
		return maxSheet;
	}
	public static int getSpeed(){
		return SPEED;
	}
	
}