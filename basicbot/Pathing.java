package bc19;
import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.System;

public class Pathing{
	//returns distance in r^2 terms
	public static int distance(int x1, int y1, int x2, int y2){
		return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
	}
	//given coordinates and a r^2 range, return a map of accessible locations
	public static LinkedList<Integer[]> findRange(MyRobot r, int x, int y, int range, boolean[][] blockers, int[][] dirMap){
		int steps = (int)Math.floor(Math.sqrt(range));
		return createPerm(r,x,y,steps,blockers, dirMap);	
	}

	//Returns a list of coordinates that are a certain number steps away from a starting coordinate
	public static LinkedList<Integer[]> createPerm(MyRobot r, int startX, int startY, int steps, boolean[][] blockers, int [][] dirMap){
		LinkedList<Integer[]> results = new LinkedList<Integer[]>();
		boolean[] yMin = new boolean[steps*2+1];
		boolean[] yMax = new boolean[steps*2+1];
		ArrayList<Integer[]> prevCoor = new ArrayList<Integer[]>();
		for(int i = 0; i < steps*2+1; i++)
			prevCoor.add(null);
		for(int y = 0 - steps; y <= steps; y++){
			int offset = (y < 0)?y+steps:steps-y;
			boolean minX = false;
			boolean maxX = false;
			for(int x = 0 - offset; x <= offset; x++) {
				int xCor = x + startX;
				int yCor = y + startY;
				if(checkBounds(r,xCor,yCor,blockers) && dirMap [yCor][xCor] == 9999){

					Integer[] coor = new Integer[3];
					coor[0] = yCor;
					coor[1] = xCor;
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

	//check to see if a tile is in map bounds or if it is not blocked
	public static boolean checkBounds(MyRobot r, int xCor, int yCor, boolean[][] blockers) {
		return (xCor >= 0 && xCor < r.map[0].length && yCor >= 0 && yCor < r.map.length && 
			r.map[yCor][xCor] && !blockers[yCor][xCor]);
	}

	//returns a distance map based on a map of end locations and a range^2
	public static int[][] rangeBFS(MyRobot r, boolean[][] endLocs, int range, boolean[][] blockers){
		long startTime = System.currentTimeMillis();

		LinkedList<Integer[]> nodes = new LinkedList<Integer[]>();
		int yBound = r.map.length;
		int xBound = r.map[0].length;

		int[][] dirMap = new int[yBound][xBound];

		for(int i = 0; i < dirMap.length; i++)
			for(int j = 0; j < dirMap[i].length; j++)
				dirMap[i][j] = 9999;

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
					if(coor[2]==1 && coorY > 0 && coorY < r.map.length - 1)
						nodes.add(coor);
					dirMap[coorY][coorX] = step + 1;
				}
			}

		}
		long diff = System.currentTimeMillis() - startTime;
		r.log("BFS Time: " + diff + " ms");
		printMap(dirMap,r);
		return dirMap;
	}

	//Checks the 8 adjacent locations given a distance map and coordinates.
	//Returns an integer array representing an adjacent direction
	public static Integer[] checkAdj(MyRobot r, int x, int y, int[][] map, boolean[][] blockers){
		int min = 999;
		Integer[] move = new Integer[2];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0){
					continue;
				}
				int xCor = x+j;
				int yCor = y+i;
				if (checkBounds(r,xCor,yCor,blockers)) {
					if (map[yCor][xCor] < min){
						min = map[yCor][xCor];
						move[1] = j;
						move[0] = i;
					}
				}
			}
		}
		return move;
	}

	//check for a robot at robot r's x,y -- returns true if there is a robot
	public static boolean checkForRobot(MyRobot r, int x, int y) {
		Robot[] visible = r.getVisibleRobots();
		for (Robot other : visible) {
			if (other.x == r.me.x+x && other.y == r.me.y+y) {
				return true;
			}
		}
		return false;
	}

	//find the coordinates of the map tile with the smallest number of steps to destination
	public static Integer[] getNextMove(MyRobot r,int startX, int startY, int steps, int[][] map, boolean[][] blockers) {
		int min = map[startY][startX];
		Integer[] move = new Integer[2];
		move[0] = 0;
		move[1] = 0;
		if (map[startY][startX] == 1) {
			return move;
		}
		for(int y = 0 - steps; y <= steps; y++){
			int offset = (y < 0)?y+steps:steps-y;
			for(int x = 0 - offset; x <= offset; x++) {
				int xCor = x + startX;
				int yCor = y + startY;
				if (checkBounds(r,xCor,yCor,blockers)) {
					if (map[yCor][xCor] <= min){
						min = map[yCor][xCor];
						move[1] = x;
						move[0] = y;
					}
				}
			}
		}
		return move;
	}

	/*Prints a map of integers*/
	public static void printMap(int[][] map, MyRobot r){
		for(int y = 0; y < map.length; y++){
			String line = "";
			for(int x = 0; x < map[y].length; x++){
				if(map[y][x] == 9999)
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

}