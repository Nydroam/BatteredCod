package bc19;
import java.util.LinkedList;
import java.lang.System;

public class Pathing{

	//given coordinates and a r^2 range, return a map of accessible locations
	public static LinkedList<Integer[]> findRange(MyRobot r, int x, int y, int range, boolean[][] blockers){
		int steps = (int)Math.floor(Math.sqrt(range));
		return createPerm(r,x,y,steps,blockers);	
	}

	//Returns a list of coordinates that are a certain number steps away from a starting coordinate
	public static LinkedList<Integer[]> createPerm(MyRobot r, int startX, int startY, int steps, boolean[][] blockers){
		LinkedList<Integer[]> results = new LinkedList<Integer[]>();
		for(int y = 0 - steps; y <= steps; y++){
			int offset = (y < 0)?y+steps:steps-y;
			for(int x = 0 - offset; x <= offset; x++) {
				int xCor = x + startX;
				int yCor = y + startY;

				if(checkBounds(r,xCor,yCor,blockers)){
					Integer[] coor = new Integer[2];
					coor[0] = yCor;
					coor[1] = xCor;
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

			LinkedList<Integer[]> nextList = findRange(r,currX,currY,range,blockers);
			int step = dirMap[currY][currX];
			while(!nextList.isEmpty()){
				Integer[] coor = nextList.poll();
				int coorY = coor[0];
				int coorX = coor[1];
				
				if(dirMap[coorY][coorX] > step + 1){
					nodes.add(coor);
					dirMap[coorY][coorX] = step + 1;
				}
			}

		}
		long diff = System.currentTimeMillis() - startTime;
		r.log("BFS Time: " + diff + " ms");
		return dirMap;
	}

	//Checks the 8 adjacent locations given a distance map and coordinates.
	//Returns an integer array representing an adjacent direction
	public static Integer[] checkAdj(MyRobot r, int x, int y, int[][] map){
		int min = 999;
		Integer[] move = new Integer[2];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (i == 0 && j == 0){
					continue;
				}
				int xCor = x+j;
				int yCor = y+i;
				if (xCor >= 0 && xCor < r.map[0].length && yCor >= 0 && yCor < r.map.length && r.map[yCor][xCor]) {
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