package bc19;
import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.System;

public class Pathing{
	//returns distance in r^2 terms
	public static int distance(int x1, int y1, int x2, int y2){
		return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
	}

	/*Prints a map of integers*/
	public static void printMap(int[][] map, MyRobot r){
		for(int y = 0; y < map.length; y++){
			String line = "";
			for(int x = 0; x < map[y].length; x++){
				if(map[y][x] == 99)
					line += "  |";
				else{
					int i = map[y][x];
					int count = 0;
					while(i > 0){
						i = (int)Math.floor(i/10);
						count++;
					}
					if(map[y][x]!=0){
						for(int j = 0; j < 2-count; j++)
							line += "0";
						line += map[y][x];
					}else{
						line += "--";
					}
					line += "|";
				}
			}
			r.log(line);
		}

	}

	//Returns list of coordinates that are within range of given x and y
	//int array representing coordinates will have index 2 indicating whether or not it's an edge of the range
	public static LinkedList<Integer[]> findRange(MyRobot r, int startX, int startY, int range, boolean[][] blockers, int[][] dirMap){
		int steps = (int)Math.floor(Math.sqrt(range));
		LinkedList<Integer[]> results = new LinkedList<Integer[]>();

		//booleans to check y
		boolean[] yMin = new boolean[steps*2+1];
		boolean[] yMax = new boolean[steps*2+1];
		ArrayList<Integer[]> prevCoor = new ArrayList<Integer[]>();
		for(int i = 0; i < steps*2+1; i++)
			prevCoor.add(null);

		//check the square with side length steps for coordinates within the range
		for(int y = 0 - steps; y <= steps; y++){
			boolean minX = false;
			boolean maxX = false;
			for(int x = 0 - steps; x <= steps; x++){

				if (x == 0 && y == 0) {
					continue;
				}

				int dist = distance(0,0,x,y);

				//booleans to check whether not a min/max for this row has been found

				if(dist <= range && checkBounds(r, x + startX, y + startY, blockers) && dirMap[y + startY][x + startX] == 99){
					Integer[] coor = new Integer[3];
					coor[0] = y + startY;
					coor[1] = x + startX;
					//coor[2] = 1;
					if(!minX){
						minX = true;
						coor[2] = 1;
					}else if (!maxX){
						maxX = true;
						coor[2] = 1;
					}else{
						results.get(results.size()-1)[2]=0;
						coor[2] = 1;
					}
					if(!yMin[y+steps]){
						yMin[y+steps] = true;
						coor[2] = 1;
					}else if (!yMax[y+steps]){
						yMax[y+steps] = true;
						prevCoor.set(y+steps,coor);
						coor[2] = 1;
					}else{
						prevCoor.get(y+steps)[2] = 0;
						prevCoor.set(y+steps,coor);
						coor[2] = 1;
					}
					results.add(coor);
				}
			}
		}

		return results;
	}

	public static boolean checkBounds(MyRobot r, int x, int y, boolean[][] blockers){
		return (x >= 0 && x < r.map[0].length && y >= 0 && y < r.map.length && 
			r.map[y][x] && !blockers[y][x]);
	}

	//returns a distance map based on a map of end locations and a range^2
	public static int[][] rangeBFS(MyRobot r, boolean[][] endLocs, int range, boolean[][] blockers, Task t){
		long startTime = System.currentTimeMillis();

		LinkedList<Integer[]> nodes = new LinkedList<Integer[]>();
		int yBound = r.map.length;
		int xBound = r.map[0].length;

		int[][] dirMap = new int[yBound][xBound];

		for(int i = 0; i < dirMap.length; i++)
			for(int j = 0; j < dirMap[i].length; j++)
				dirMap[i][j] = 99;

		for( int y = 0; y < endLocs.length; y++ ){
			for( int x = 0; x < endLocs.length; x++ ){
				if( endLocs[y][x] ){
					Integer[] coor = new Integer[2];
					coor[0] = y;
					coor[1] = x;
					dirMap[y][x] = 0;
					nodes.add(coor);
				}
			}
		}
		while(!nodes.isEmpty()){
			Integer[] curr = nodes.poll();
			int currY = curr[0];
			int currX = curr[1];

			LinkedList<Integer[]> nextList = findRange(r,currX,currY,range,blockers,dirMap);
			int step = dirMap[currY][currX];
			while(!nextList.isEmpty()){
				Integer[] coor = nextList.poll();
				int coorY = coor[0];
				int coorX = coor[1];
				
				if(dirMap[coorY][coorX] > step + 1){
					if(coor[2]==1)
						nodes.add(coor);
					dirMap[coorY][coorX] = step + 1;
					if(t.markKarb){
						boolean[][] karbMap = r.getKarboniteMap();
						if(karbMap[coorY][coorX]){
							t.karbList.add(new Resource(coorX,coorY,step));
						}
					}
					if(t.markFuel){
						boolean[][] fuelMap = r.getFuelMap();
						if(fuelMap[coorY][coorX]){
							t.fuelList.add(new Resource(coorX,coorY,step));
						}
					}
				}
			}

		}
		long diff = System.currentTimeMillis() - startTime;
		r.log("BFS Time: " + diff + " ms");
		printMap(dirMap,r);
		return dirMap;
	}

	public static int[][] rangeAST(MyRobot r, int startX, int startY, int endX, int endY, int range, boolean[][] blockers) {
		long startTime = System.currentTimeMillis();
		int[][] dirMap = new int[r.map.length][r.map[0].length];
		for(int i = 0; i < dirMap.length; i++)
				for(int j = 0; j < dirMap[i].length; j++)
					dirMap[i][j] = 99;

		LinkedList<Integer[]> nodes = new LinkedList<Integer[]>();

		Integer[] start = new Integer[3];
		start[0] = startY;
		start[1] = startX;
		nodes.add(start);
		dirMap[startY][startX] = 0;

		while(!nodes.isEmpty()) {
			Integer[] curr = findNext(nodes, endX, endY, dirMap);
			nodes.remove(curr);
			int currY = curr[0];
			int currX = curr[1];
			r.log("X: " + currX + " Y: " + currY);
			LinkedList<Integer[]> nextList = findRange(r,currX,currY,range,blockers,dirMap);
			int step = dirMap[currY][currX];
			while(!nextList.isEmpty()) {
				Integer[] coor = nextList.poll();
				int coorY = coor[0];
				int coorX = coor[1];

				if (coorX == endX && coorY == endY) {
					dirMap[coorY][coorX] = 98;
					long diff = System.currentTimeMillis() - startTime;
					r.log("AST Time: " + diff + " ms");
					printMap(dirMap,r);
					return dirMap;
				}
				
				if(dirMap[coorY][coorX] > step + 1){
						if(coor[2]==1) {
							r.log("Adding-- X: " + coorX + " Y: " + coorY);
							nodes.add(coor);
						}
						dirMap[coorY][coorX] = step + 1;
				}
			}
		}

		return dirMap;
	}

	public static Integer[] findNext(LinkedList<Integer[]> nodes, int endX, int endY, int[][] dirMap) {
		Integer[] nextNode;
		int minStep = 9999;
		int minDist = 9999;
		for (Integer[] node: nodes) {
			int newDist = distance(node[1],node[0],endX,endY);
			if (newDist < minDist) {
				minStep = dirMap[node[1]][node[0]];
				minDist = newDist;
				nextNode = node;
			} else if (newDist == minDist) {
				int newStep = dirMap[node[1]][node[0]];
				if (newStep < minStep) {
					minStep = newStep;
					nextNode = node;
				}
			}
		}
		return nextNode;
	}
}

/*int currStep = dirMap[node[1]][node[0]];
			if (currStep > maxStep) {
				maxStep = currStep;
				minDist = distance(node[1],node[0],endX,endY);
				nextNode = node;
			} else if (currStep == maxStep) {
				int newDist = distance(node[1],node[0],endX,endY);
				if (newDist < minDist) {
					minDist = newDist;
					nextNode = node;
				}
			}*/