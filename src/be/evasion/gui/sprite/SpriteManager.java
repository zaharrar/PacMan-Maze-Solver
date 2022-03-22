package be.evasion.gui.sprite;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import be.evasion.gui.MazeEvasionGUI;
import be.evasion.gui.util.GUIUtils;
import be.evasion.maze.Direction;
import be.evasion.maze.item.Candy;
import be.evasion.maze.item.Monster;
import be.evasion.util.Index;

public class SpriteManager {
	private BufferedImage pakkumanImage, monsterImage, candyImage, endImage;
	private SpriteSheet pakkumanSpriteSheet;
	private PakkumanSprite pakkumanSprite;
	private List<Sprite> monsterSpriteList;
	private List<Sprite> candySpriteList;
	private Sprite endSprite;
	public SpriteManager(){
		try {
			//this.pakkumanImage = ImageIO.read(new File("img/pakkuman.png"));
			this.monsterImage = ImageIO.read(new File("img/monster.png"));
			this.candyImage = ImageIO.read(new File("img/candy3.png"));
			this.pakkumanImage = ImageIO.read(new File("img/pakkuman-sheet.png"));
			this.endImage = ImageIO.read(new File("img/freedom.png"));
			this.pakkumanSpriteSheet = new SpriteSheet(pakkumanImage, 62, 56, 4);
			//this.pakkumanImage = ImageIO.read(new File("img/hero.png"));
			//this.pakkumanSpriteSheet = new SpriteSheet(pakkumanImage, 128,128,4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.pakkumanSprite = new PakkumanSprite(pakkumanSpriteSheet, GUIUtils.getPixelShift(MazeEvasionGUI.getMaze().getPakkumanLocation().getCol()),
				GUIUtils.getPixelShift(MazeEvasionGUI.getMaze().getPakkumanLocation().getRow()),
				GUIUtils.getSquareCaseLength(), GUIUtils.getSquareCaseLength());
		this.endSprite = new Sprite(endImage, GUIUtils.getPixelShift(MazeEvasionGUI.getMaze().getEnd().getCol()),
				GUIUtils.getPixelShift(MazeEvasionGUI.getMaze().getEnd().getRow()),
				GUIUtils.getSquareCaseLength(), GUIUtils.getSquareCaseLength());
		this.monsterSpriteList = new ArrayList<Sprite>();
		for(Entry<Integer, Monster> entry : MazeEvasionGUI.getMaze().getMonsters().entrySet()) {
			Index location = MazeEvasionGUI.getMaze().parseLocation(entry.getKey());
			monsterSpriteList.add( new Sprite(monsterImage, GUIUtils.getPixelShift(location.getCol()), 
															GUIUtils.getPixelShift(location.getRow()), 
															GUIUtils.getSquareCaseLength(), GUIUtils.getSquareCaseLength(), entry.getValue()));
		}
		this.candySpriteList = new ArrayList<Sprite>();
		for(Entry<Integer, Candy> entry : MazeEvasionGUI.getMaze().getCandies().entrySet()) {
			Index location = MazeEvasionGUI.getMaze().parseLocation(entry.getKey());
			candySpriteList.add( new Sprite(candyImage, GUIUtils.getPixelShift(location.getCol()), 
														GUIUtils.getPixelShift(location.getRow()), 
														GUIUtils.getSquareCaseLength(), GUIUtils.getSquareCaseLength(), entry.getValue()));
		}

	}
	public void drawAll(Graphics g){
		endSprite.draw(g);
		for(Sprite sprite : candySpriteList) sprite.draw(g);
		pakkumanSprite.draw(g);
		for(Sprite sprite : monsterSpriteList) sprite.draw(g);
	}
	public void movePakkumanSprite(Direction direction){
		pakkumanSprite.move(direction);
	}
	public PakkumanSprite getPakkumanSprite(){
		return pakkumanSprite;
	}

}
