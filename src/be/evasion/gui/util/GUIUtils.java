package be.evasion.gui.util;

import java.awt.Dimension;

import be.evasion.gui.MazeEvasionGUI;
import be.evasion.gui.sprite.Sprite;
import be.evasion.util.Index;

public final class GUIUtils {
	private static final Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	private static final int SQUARECASEPROPORTION = 2; // Small proportion => big square
	private static final int WALLTHICKNESSPROPORTION = 6*SQUARECASEPROPORTION;
	private static int squareCaseLength;
	private static int wallThickness;
	private static Dimension panelAdjustedSize;
		
	static{
		squareCaseLength = getDefaultSquareCaseLength();
		wallThickness = getDefaultWallThickness();
		panelAdjustedSize = initAdjustedSize();
	}
	// TODO : Optimize this method
	private static Dimension initAdjustedSize(){
		int width = (squareCaseLength * MazeEvasionGUI.getMaze().getCols()) + (wallThickness *  (MazeEvasionGUI.getMaze().getCols()+1));
		int height = (squareCaseLength * MazeEvasionGUI.getMaze().getRows()) + (wallThickness *  (MazeEvasionGUI.getMaze().getRows()+1));
		wallThickness = getDefaultWallThickness();
		int randomProportion = 10;
		int randomFreeSpaceForBetterView = 170;
		int i = 0;
		while(width+randomFreeSpaceForBetterView >= screenSize.getWidth() || height+randomFreeSpaceForBetterView >= screenSize.getHeight()){
			squareCaseLength = (int)((screenSize.getHeight()-(i*randomProportion))/SQUARECASEPROPORTION);
			width = (squareCaseLength * MazeEvasionGUI.getMaze().getCols()) + (wallThickness *  (MazeEvasionGUI.getMaze().getCols()+1));
			height = (squareCaseLength * MazeEvasionGUI.getMaze().getRows()) + (wallThickness *  (MazeEvasionGUI.getMaze().getRows()+1));
			i++;
		}
		return new Dimension(width, height);
	}
	public static Dimension getScreenSize(){
		return screenSize;
	}
	public static Dimension getPanelAdjustedSize(){
		return panelAdjustedSize;
	}
	public static int getSquareCaseLength(){
		return squareCaseLength;
	}
	public static int getWallThickness(){
		return wallThickness;
	}
	public static int getDefaultSquareCaseLength(){
		return (int)(screenSize.getHeight()/SQUARECASEPROPORTION);
	}
	public static int getDefaultWallThickness(){
		return 1;
	}
	public static int getPixelShift(int index){
		return wallThickness + index*(wallThickness+squareCaseLength);
	}
	public static int getIndexFromPixelShift(int pixelShift){
		return (pixelShift - wallThickness)/(wallThickness+squareCaseLength);
	}
	public static boolean isAlign(Sprite pakkumanSprite, Index pakkumanLocation){
		return  pakkumanSprite.getY() == getPixelShift(pakkumanLocation.getRow()) &&
				pakkumanSprite.getX() == getPixelShift(pakkumanLocation.getCol());
	}
}
