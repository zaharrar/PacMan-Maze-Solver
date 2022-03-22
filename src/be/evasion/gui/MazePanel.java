package be.evasion.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import be.evasion.gui.sprite.SpriteManager;
import be.evasion.gui.util.GUIUtils;
import be.evasion.maze.Direction;

public class MazePanel extends JPanel{

	private static final long serialVersionUID = -1952953065473937241L;
	private static final Color WALLCOLOR =Color.BLACK;
	private final Color BGCOLOR = Color.LIGHT_GRAY;
	private SpriteManager spriteManager;

	MazePanel(SpriteManager spriteManager){
		this.spriteManager = spriteManager;
		setPreferredSize(GUIUtils.getPanelAdjustedSize());
		setBackground(BGCOLOR);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.setColor(WALLCOLOR);
		drawMaze(g); 
		spriteManager.drawAll(g);
	}
	public void drawMaze(Graphics g){
		int tmpI = 0;
		int tmpJ = 0;
		// NORTH EDGE
		int wallThickness = GUIUtils.getWallThickness();
		int squareCaseLength = GUIUtils.getSquareCaseLength();
		for(int j = 0; j < MazeEvasionGUI.getMaze().getCols(); j++){
			g.fillRect(wallThickness + tmpJ, 0, squareCaseLength, wallThickness);
			tmpJ += wallThickness + squareCaseLength;
		}
		for (int i = 0; i < MazeEvasionGUI.getMaze().getRows(); i++){
			tmpJ = 0;
			g.fillRect(0, wallThickness+tmpI, wallThickness, squareCaseLength); // EXTREME WEST WALL
			for (int j = 0; j < MazeEvasionGUI.getMaze().getCols(); j++){
				if(!MazeEvasionGUI.getMaze().get(i, j).canGo(Direction.SOUTH)){
					g.fillRect(wallThickness + tmpJ, squareCaseLength + wallThickness + tmpI, squareCaseLength, wallThickness); // SOUTH WALL
				}
				if(!MazeEvasionGUI.getMaze().get(i, j).canGo(Direction.EAST)){
					g.fillRect(squareCaseLength + wallThickness + tmpJ, wallThickness + tmpI, wallThickness, squareCaseLength); // EAST WALL
				}
				g.fillRect(tmpJ, tmpI, wallThickness, wallThickness); // LITTLE CUBE
				tmpJ += wallThickness + squareCaseLength;
			}
			g.fillRect(tmpJ, tmpI, wallThickness, wallThickness); // LITTLE CUBE
			tmpI += wallThickness + squareCaseLength;
		}
		tmpJ = 0;
		for(int j = 0; j < MazeEvasionGUI.getMaze().getCols(); j++){
			g.fillRect(tmpJ, tmpI, wallThickness, wallThickness); // LITTLE CUBE
			tmpJ += wallThickness + squareCaseLength;
		}
		g.fillRect(tmpJ, tmpI, wallThickness, wallThickness); // LITTLE CUBE
	}
}

