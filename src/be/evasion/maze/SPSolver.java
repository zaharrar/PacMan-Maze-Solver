package be.evasion.maze;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.evasion.util.Index;

public class SPSolver {
	
	private Maze maze;
	private MazePath shortestPath;
	private boolean run, solved;
	private int candyInPocket, blockingMonsterID;
	private Set<Integer> killedMonsters;
	private List<Integer> candiesLocationList, monstersLocationList; 
	private List<List<Integer>> candiesLocationArrangementList, monstersLocationCombinaisonList;
	private List<Integer> buildingList;
	private Map<Integer, Map<Integer, Boolean>> candiesAccessibility;
	private List<Integer> candiesDirectReachable;
	
	public SPSolver(Maze maze){
		
		this.maze = maze;
		this.candiesLocationList = maze.candiesLocationAsList();
		this.monstersLocationList = null;
		this.killedMonsters = new HashSet<>();
		this.candiesLocationArrangementList = new ArrayList<>();
		this.monstersLocationCombinaisonList = new ArrayList<>();
		this.buildingList = new ArrayList<>();
		this.candiesAccessibility = new HashMap<>();
		this.candiesDirectReachable = new ArrayList<>();
		this.shortestPath = null;
		this.run = false;
		this.solved = false;
		this.blockingMonsterID = -1;
		this.candyInPocket = 0;
	}
	
	public MazePath getShortestPath(){
		if(!run && shortestPath == null){
			shortestPath = process();
			run = true;
		}
		return shortestPath;
	}
	public boolean isSolved(){
		if(!run){
			return false;
		}
		return solved;
	}
	public boolean isBlocked(){
		return blockingMonsterID != -1;
	}
	public int getBlockingMonsterID(){
		return blockingMonsterID;
	}
	/**
	 * Find the shortest path of the maze.
	 * @return a MazePath containing the best shortest path
	 */
	private MazePath process(){
		MazePath bestCandyPath = null;
		initCandiesAccessibility();
		// Step 1 : firstPath contain the bestPath without killing monsters
		MazePath bestPath = buildShortestPath(maze.getPakkumanLocation(), maze.getEnd());
		// Step 2 : looking for the shortest by seeking k candies (thus kill k monsters on the way)
		int bestSize = Integer.MAX_VALUE;
		for(int k=1; k<candiesLocationList.size()+1;k++){
			// bestCandyPath is the shortest path to the end by seeking k candies on the way
			candiesArrangement(k);
			if(bestPath != null){
				bestSize = bestPath.size();
			}
			bestCandyPath = bestCandyPathFor(candiesLocationArrangementList, bestSize);
			bestPath = chooseBestPath(bestPath, bestCandyPath);
		}
		solved = bestPath != null;
		if(!solved){
			bestPath = getUnsolvedPath();
		}
		return bestPath;
	}
	private MazePath getUnsolvedPath(){
		// path while all monsters are killed
		MazePath unsolvedPath = buildShortestPath(maze.getPakkumanLocation(), maze.getEnd(), true);
		if(unsolvedPath != null){ // a monster blocked the exit
			boolean canGo = true;
			MazePath tmp = new MazePath();
			Index location = maze.getInitialPakkumanLocation();
			int i = 0;
			while(canGo){
				canGo = !maze.monsterOn(location);
				if(canGo){
					tmp.add(unsolvedPath.get(i));
					location.inc(unsolvedPath.get(i));
				}else{
					this.blockingMonsterID = maze.getMonsterOn(location).getID();
				}
				i++;
			}
			unsolvedPath = tmp;
		}
		return unsolvedPath;
	}
	private void initCandiesAccessibility(){
		buildPrevious(maze.getPakkumanLocation());
		for(Integer candyKey : maze.candiesLocationAsList()){
			if(maze.get(candyKey).isAccessible()){
				candiesDirectReachable.add(candyKey);
			}
		}
		MazePath path = null;
		List<Integer> monsterList = null;
		for(Integer mainKey : maze.candiesLocationAsList()){
			candiesAccessibility.put(mainKey, new HashMap<Integer, Boolean>());
			for(Integer secKey : maze.candiesLocationAsList()){
				path = buildShortestPath(maze.parseLocation(mainKey), maze.parseLocation(secKey), true);
				if (path != null){
					monsterList = getMonsterOnPath(maze.parseLocation(mainKey), path);
				}
				/* Si path != null && monstres entre les bonbons peuvent etre tué
				 * (par les bonbons directement accessible) => true
				 * sinon false
				*/
				candiesAccessibility.get(mainKey).put(secKey, path == null || monsterList.size() <= candiesDirectReachable.size());
			}
		}
	}
	private void candiesArrangement(int k){
		candiesLocationArrangementList.clear();
		buildingList.clear();
		for(Integer key : candiesDirectReachable){
			buildingList.add(key);
			Collections.swap(candiesLocationList, 0, candiesLocationList.indexOf(key));
			candiesArrangement0(k-1, 1);
			Collections.swap(candiesLocationList, 0, candiesLocationList.indexOf(key));
			buildingList.remove(key);

		}
	}
	/**
	 * Can be called only by candiesArrangement(k) !
	 */
	private void candiesArrangement0(int k, int begin){
		boolean correct = true;
		if(k==0){
			candiesLocationArrangementList.add(new ArrayList<Integer>(buildingList));
		}else{
			Integer key;
			for(int i=begin;i<candiesLocationList.size();i++){
				key = candiesLocationList.get(i);
				// Précondition : buildingList contains already the firstCandy to browse
				// correct = true if the candy on the key location 'key' is not blocked (with monsters or walls)
				correct = candiesAccessibility.get(buildingList.get(buildingList.size()-1)).get(key);
				buildingList.add(key);
				Collections.swap(candiesLocationList, begin, i);
				if(correct && candiesLocationList.size() - begin >= k){
					candiesArrangement0(k-1, begin+1);
				}
				Collections.swap(candiesLocationList, begin, i);
				buildingList.remove(key);
			}
		}
	}
	private void monstersCombinaison(int k){
		monstersLocationCombinaisonList.clear();
		buildingList.clear();
		monstersCombinaison(k, 0);
	}
	public void monstersCombinaison(int k, int begin){
		if(k==0){
			monstersLocationCombinaisonList.add(new ArrayList<Integer>(buildingList));
		}else{
			Integer elem;
			for(int i=begin;i<monstersLocationList.size();i++){
				elem = monstersLocationList.get(i);
				buildingList.add(elem);
				if(monstersLocationList.size() - begin >= k){
					monstersCombinaison(k-1, i+1);
				}
				buildingList.remove(elem);
			}
		}
	}
	private void setKilledMonstersOn(Collection<Integer> monsterLocationList, boolean killed){
		for(Integer intKey : monsterLocationList){
			maze.getMonsterOn(maze.parseLocation(intKey)).setKilled(killed);
		}
	}
	private MazePath bestCandyPathFor(List<List<Integer>> candiesLocationArrangementList, int bestSize){
		MazePath candyPath=null, bestCandyPath=null, monsterPath=null, bestMonsterPath=null;
		Index lastCandyLocation = null;
		for(List<Integer> candyLocationList :  candiesLocationArrangementList){
			candyPath = buildCandyPath(candyLocationList);
			if(candyPath != null && candyPath.size() >= bestSize){
				candyPath = null;
			}
			if(candyPath != null){
				bestMonsterPath = null;
				monstersLocationList = getAliveMonstersLocationList();
				/* While building candyPath, some monsters (which are in killedMonsters) could be killed.
				 * candyPocket contains the number of candies that pakkuman have in his pocket
				 * (so number of monsters that he can still kill)
				 */
				monstersCombinaison(candyInPocket);
				for(List<Integer> monsterLocationList : monstersLocationCombinaisonList){
					setKilledMonstersOn(monsterLocationList, true);
					lastCandyLocation = maze.parseLocation(candyLocationList.get(candyLocationList.size()-1));
					// monsterPath containing the path with the monsters on monsterLocationList are killed
					monsterPath = buildShortestPath(lastCandyLocation, maze.getEnd());
					bestMonsterPath = chooseBestPath(bestMonsterPath, monsterPath);
					setKilledMonstersOn(monsterLocationList, false);
				}
				if(candyPath != null && bestMonsterPath != null){
					candyPath.concatenate(bestMonsterPath);
				}else{
					candyPath = null;
				}
				bestCandyPath = chooseBestPath(bestCandyPath, candyPath);
			}
			// While building candyPath, some monsters (which are in killedMonsters) could be killed
			setKilledMonstersOn(killedMonsters, false);
		}
		return bestCandyPath;
	}
	private List<Integer> getAliveMonstersLocationList(){
		List<Integer> aliveMonsters= new ArrayList<>(maze.getMonsters().keySet());
		aliveMonsters.removeAll(killedMonsters);
		return aliveMonsters;
	}
	
	/**
	 * @return the path between all the candies (candy0 -> candy1 -> ... candyn)
	 */
	private MazePath buildCandyPath(List<Integer> locationList){
		killedMonsters.clear();
		MazePath candyPath = buildShortestPath(maze.getPakkumanLocation(),
											   maze.parseLocation(locationList.get(0)));
		Index source, destination;
		int candyInPocket = 1; // the first candy
		if(candyPath != null){
			MazePath tmpPath = null;
			for(int i=1; i < locationList.size();i++){
				source = maze.parseLocation(locationList.get(i-1));
				destination = maze.parseLocation(locationList.get(i));
				tmpPath = buildShortestPath(source, destination);
				if(tmpPath == null){ // this is maybe a monster who blocked the path
					tmpPath = buildShortestPath(source, destination, true); // path without monsters in it (see buildPrevious)
					if(tmpPath == null){ // no monsters blocked the path, so the path wanted does not exist
						return null;
					}
					List<Integer> monsterList = getMonsterOnPath(source, tmpPath);
					for(Integer key : monsterList){
						// killing all monsters on path
						if(!maze.getMonsterOn(maze.parseLocation(key)).isKilled()){
							maze.getMonsterOn(maze.parseLocation(key)).setKilled(true);
							candyInPocket--;
							killedMonsters.add(key);
						}
					}
					// if we do not have enough candies, break
					if(candyInPocket < 0){
						candyPath = null;
						break;
					}
				}
				candyPath.concatenate(tmpPath);
				candyInPocket++;
			}
		}
		this.candyInPocket = candyInPocket;
		return candyPath;
	}
	private List<Integer> getMonsterOnPath(Index source, MazePath path){
		Index location = new Index(source);
		List<Integer> monsterList = new ArrayList<>();
		if(maze.monsterOn(location)){
			monsterList.add(maze.getIntKey(location));
		}
		for(Direction dir : path){
			location.incRow(dir.getRow());
			location.incCol(dir.getCol());
			if(maze.monsterOn(location)){
				monsterList.add(maze.getIntKey(location));
			}
		}
		return monsterList;
	}
	
	private MazePath chooseBestPath(MazePath bestPath, MazePath path){
		if(bestPath == null){
			bestPath = path;
		}
		else if(path != null){
			if(path.size() < bestPath.size()){
				bestPath = path;
			}
		}
		return bestPath;

	}
	private MazePath buildShortestPath(Index source, Index destination){
		return buildShortestPath(source, destination, false);
	}
	private MazePath buildShortestPath(Index source, Index destination, boolean invisibleMonsters){
		buildPrevious(source, invisibleMonsters);
		MazePath path = new MazePath();
		Direction previousDirection;
		MazeBox previous = maze.get(destination);
		while(previous!=maze.get(source)){
			previousDirection = previous.directionTo(previous.getPrevious());
			if(previousDirection == null){
				// Si previousDirection == null, previous.getPrevious() n'est pas adjacent à previous (donc aucune arrete n'existe entre ces deux MazeBox)
				return null;
			}
			path.add(previousDirection.getOpposite());
			previous = previous.getPrevious();
		}
		path.reverse();
		return path;
	}
	private void buildPrevious(Index location){
		buildPrevious(location, false);
	}
	private void buildPrevious(Index location, boolean invisibleMonsters){
		buildPrevious(location.getRow(), location.getCol(), invisibleMonsters);
	}
	private void buildPrevious(int prow, int pcol, boolean invisibleMonsters){
		/* Algorithme de Dijkstra */
		MazeHeap heap = new MazeHeap();
		MazeBox m, y;
		float v = 0;
		initPrevious(heap, prow, pcol, invisibleMonsters);
		heap.remove(maze.get(prow, pcol));
		while(!heap.isEmpty()){
			m=heap.removeMin();
			if(m.getDistance() == Float.POSITIVE_INFINITY){
				heap.clear();
			}else{
				for(Direction direction : Direction.values()){
					if (m.canGo(direction)){
						y = maze.get(m.getLocation().getRow()+direction.getRow(), m.getLocation().getCol()+direction.getCol());
						if(invisibleMonsters ||
												(!maze.monsterOn(y.getLocation()) || 
												maze.getMonsterOn(y.getLocation()).isKilled())){
							if(heap.contains(y)){
								v = m.getDistance()+1;
								if(v<y.getDistance()){
									y.setDistance(v);
									y.setPrevious(m);
									heap.refresh(y);
								}
							}
						}
					}
				}
			}
			
		}
	}
	private void initPrevious(MazeHeap heap, int prow, int pcol, boolean invisibleMonsters){
		for(int i=0;i<maze.getRows();i++){
			for(int j=0;j<maze.getCols();j++){
				maze.get(i, j).setPrevious(maze.get(prow, pcol));
				maze.get(i, j).setDistance(Float.POSITIVE_INFINITY);
			}
		}
		maze.get(prow, pcol).setDistance(0);
		for(Direction direction : Direction.values()){
			if(maze.get(prow, pcol).canGo(direction)){
				Index adjLocation = new Index(prow+direction.getRow(), pcol+direction.getCol());
				if(invisibleMonsters || (!maze.monsterOn(adjLocation) || 
										  maze.getMonsterOn(adjLocation).isKilled())){
					maze.get(adjLocation).setDistance(1);
				}
			}
		}
		for(int i=0;i<maze.getRows();i++){
			for(int j=0;j<maze.getCols();j++){
				heap.add(maze.get(i, j));
			}
		}
	}

}
