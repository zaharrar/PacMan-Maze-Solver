package be.evasion.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import be.evasion.maze.Direction;
import be.evasion.maze.Maze;
import be.evasion.maze.MazePath;

public class MazeOutputProducer {
	public static final String OUTPUTDIR = "output/";
	public static final String INITIALFILENAME = "initial.txt";
	public static final String FINALFILENAME = "final.txt";

	private static final String CANDYITEM = "o";
	private static final String PAKKUMANITEM = "P";
	private static final String FOOTSTEPITEM = "#";
	private String[][] product;
	private Maze maze;
	private MazePath shortestPath;
	private int rows, cols;
	private int cptc, cptm;
	public MazeOutputProducer(Maze maze){
		this.maze = maze;
		this.shortestPath = maze.getShortestPath();
		this.rows = maze.getRows();
		this.cols = maze.getCols();
		this.cptc = 0;
		this.cptm = 0;
		this.product = new String[rows][cols];
	}
	public void produceAll(){
		produceOutput();
		try {
			produceInitialFile();
		} catch (IOException e) {
			System.out.println("***An error occured while creating the "+INITIALFILENAME+" file***");
			e.printStackTrace();
		}
		try {
			produceFinalFile();
		} catch (IOException e) {
			System.out.println("***An error occured while creating the "+FINALFILENAME+" file***");
			e.printStackTrace();
		}
	}
	private void produceOutput(){
		System.out.println("Le labyrinthe a une dimension "+rows+" fois "+cols);
		System.out.println("Il contient "+maze.getMonstersSize()+" monstres et "+maze.getCandiesSize()+" bonbons.");
		System.out.println("M. Pakkuman se trouve en position "+maze.getInitialPakkumanLocation());
		System.out.print("Les monstres se trouvent en position: ");
		for(Integer key : maze.monstersLocationAsList()){System.out.print(maze.parseLocation(key)+" ");};
		System.out.println();
		System.out.print("Les bonbons se trouvent en position: ");
		for(Integer key : maze.candiesLocationAsList()){System.out.print(maze.parseLocation(key)+" ");};
		System.out.println();
		System.out.println();
		if(maze.isSolved()){
			System.out.println("Déplacements de M. Pakkuman:");
			Index location = maze.getInitialPakkumanLocation();
			System.out.println("1. "+location+" Départ");
			int i = 0;
			while(i<shortestPath.size()-1){
				location.inc(shortestPath.get(i));
				System.out.print((i+1)+". "+location+" "+shortestPath.get(i).getName());
				if(maze.candyOn(location) && !maze.getCandyOn(location).isDestroy()){
					System.out.println(", bonbon récolté");
					cptc++;
					maze.getItemOn(location).setDestroy(true);
				}else if(maze.monsterOn(location) && !maze.getMonsterOn(location).isDestroy()){
					System.out.println(", bonbon donné au méchant monstre");
					maze.getItemOn(location).setDestroy(true);
					cptm++;
				}else{
					System.out.println();
				}
				i++;
			}
			location.inc(shortestPath.get(i));
			System.out.println((i+1)+". "+location+" Sortie !");
			System.out.println();
			System.out.println("Trouvé un plus court chemin de logueur "+shortestPath.size());
			System.out.println("M. Pakkuman a récolté "+cptc+" bonbons et rencontré "+cptm+" monstres.");
			
		}else{
			System.out.println("Il n’y a pas moyen de sortir vive du labyrinthe car");
			if(maze.isBlocked()){
				System.out.println("le monstre "+maze.getBlockingMonsterID()+" nous empêche de sortir.");
			}else{
				System.out.println("le labyrinthe est impossible à résoudre.");
			}
		}
			
	}
	private void produceInitialFile() throws IOException{
		cleanProduct();
		buildInitialProduct();
		BufferedWriter bw = newBufferedWriter(INITIALFILENAME);
		bw.write("Situation de départ:\n");
		bw.write(productToString());
		bw.close();
	}
	private void produceFinalFile() throws IOException{
		cleanProduct();
		buildInitialProduct();
		buildFootstep();
		BufferedWriter bw = newBufferedWriter(FINALFILENAME);
		bw.write("Situation final:\n");
		bw.write(productToString());
		if(maze.isSolved()){
			bw.write("\nTrouvé un plus court chemin de longueur "+shortestPath.size()+".\n");
		}else{
			bw.write("Il n'y a pas moyen de sortir.\n");
		}
		bw.write("M. Pakkuman a pris "+cptc+" bonbons!\n");
		bw.write("Déplacements de M. Pakkuman: ");
		if(shortestPath == null){
			bw.write(" aucun.");
		}else{
			Index location = maze.getInitialPakkumanLocation();
			bw.write(location+" ");
			int i = 0;
			for(Direction direction : shortestPath){
				location.inc(direction);
				bw.write(location+" ");
				i++;
				if(i==10){
					i=0;
					bw.write("\n");
				}
			}
		}
		bw.close();
	}
	private BufferedWriter newBufferedWriter(String filename) throws IOException{
		BufferedWriter bw = null;
		File file = new File(OUTPUTDIR+filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		return bw;
		
	}
	private String productToString(){
		String repr = "";
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				repr += "+";
				repr += maze.get(i, j).canGo(Direction.NORTH) ? "   " : "---";
			}
			repr += "+\n";
			for(int j=0;j<cols;j++){
				repr += maze.get(i, j).canGo(Direction.WEST) ? " " : "|";
				repr += " " + product[i][j] + " ";
			}
			repr += "|\n";
		}
		for(int j=0;j<cols-1;j++){
			repr += "+";
			repr += "---";
		}
		repr += "+   +\n";
		return repr;
	}
	private void cleanProduct(){
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				product[i][j] = " ";
			}			
		}
	}
	private void buildFootstep(){
		Index location = maze.getInitialPakkumanLocation();
		for(Direction direction : shortestPath){
			location.inc(direction);
			if(!maze.itemOn(location)){
				product[location.getRow()][location.getCol()] = FOOTSTEPITEM;
			}
		}
	}
	private void buildInitialProduct(){
		Index location;
		for(Integer key : maze.monstersLocationAsList()){
			location = maze.parseLocation(key);
			product[location.getRow()][location.getCol()] = maze.getMonsterOn(location).getID().toString();
		}
		for(Integer key : maze.candiesLocationAsList()){
			location = maze.parseLocation(key);
			product[location.getRow()][location.getCol()] = CANDYITEM;
		}
		product[maze.getInitialPakkumanLocation().getRow()][maze.getInitialPakkumanLocation().getCol()] = PAKKUMANITEM;
	}
}
