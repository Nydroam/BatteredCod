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

				
				if(dist <= range && checkBounds(r, x + startX, y + startY, blockers) ) {
					if(dirMap[y + startY][x + startX] == 99){
						Integer[] coor = new Integer[4];
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
					else{
						if(!minX){
							minX = true;
						}
						else if(maxX){
							//r.log("RESULTS " + results);
							results.get(results.size()-1)[2]=0;
							maxX = false;
						}
						if(!yMin[y+steps]){
							yMin[y+steps] = true;
						}
						else if(yMax[y+steps]){
							prevCoor.get(y+steps)[2] = 0;
							yMax[y+steps] = false;
						}
					}
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

		boolean[][] lattice = new boolean[r.map.length][r.map[0].length];
		lattice = new boolean[r.map.length][r.map[0].length];
		for(int y = 0; y < r.map.length; y++){
			for(int x = 0; x < r.map[0].length; x++){
				if(y % 2 == 0){
					if(x % 2 == 0)
						lattice[y][x] = true;
				}
				else{
					if(x % 2 == 1)
						lattice[y][x] = true;
				}
			}
		}
		int[][] dirMap = new int[yBound][xBound];

		for(int i = 0; i < dirMap.length; i++)
			for(int j = 0; j < dirMap[i].length; j++)
				dirMap[i][j] = 99;

		for( int y = 0; y < endLocs.length; y++ ){
			for( int x = 0; x < endLocs.length; x++ ){
				if( endLocs[y][x] ){
					Integer[] coor = new Integer[4];
					coor[0] = y;
					coor[1] = x;
					dirMap[y][x] = 0;
					nodes.add(coor);
					if(r.me.y==y && r.me.x==x )
						coor[3] = 1;
				}
			}
		}
		boolean[][] rMap = new boolean[r.map.length][r.map[0].length];
		
		for(int tempY = 0; tempY < rMap.length; tempY++){
			for(int tempX = 0; tempX < rMap[0].length; tempX++){
				if(r.getFuelMap()[tempY][tempX] || r.getKarboniteMap()[tempY][tempX])
					rMap[tempY][tempX] = true;
			}
		}

		boolean[][] karbMap = new boolean[r.map.length][r.map[0].length];
		boolean[][] fuelMap = new boolean[r.map.length][r.map[0].length];
		for(int tempY = 0; tempY < rMap.length; tempY++){
			for(int tempX = 0; tempX < rMap[0].length; tempX++){
				karbMap[tempY][tempX] = r.getKarboniteMap()[tempY][tempX];
				fuelMap[tempY][tempX] = r.getFuelMap()[tempY][tempX];
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
				if(curr[3]==1)
					coor[3]=1;
				if(dirMap[coorY][coorX] > step + 1){
					if(coor[2]==1)
						nodes.add(coor);
					dirMap[coorY][coorX] = step + 1;
					
					if(t.markKarb && coor[3] == 1){
						
						if(karbMap[coorY][coorX]){
							t.karbList.add(new Resource(coorX,coorY,step));
						}
					}
					if(t.markFuel && coor[3] == 1){
						if(fuelMap[coorY][coorX]){
							t.fuelList.add(new Resource(coorX,coorY,step + 1));
						}
					}
					int s = 6;
					if((fuelMap[coorY][coorX] || karbMap[coorY][coorX]) && coor[3] == 1 && distance(r.me.x,r.me.y,coorX,coorY) >= s*s && r.me.unit == 0){
						//r.log("works");
						
						for(int dy = -1*s; dy <= s; dy++){
							for(int dx = -1*s; dx <= s; dx++){
								int checkX = coorX + dx;
								int checkY = coorY + dy;
								if(distance(coorX,coorY,checkX,checkY) <= s*s && checkBounds(r,checkX,checkY,blockers)){
									//r.log("fm" + fuelMap);
									fuelMap[checkY][checkX] = false;
									//r.log("km" + fuelMap);
									karbMap[checkY][checkX] = false;
								}
							}
						}
						//r.log("works2");
					}
					if(lattice[coorY][coorX]&& coor[3] == 1 && !rMap[coorY][coorX] && step+1>1){
						Integer[] l = new Integer[4];
						l[0] = coorY;
						l[1] = coorX;
						l[2] = -1;
						l[3] = step + 1;
						t.lattice.add(l);
					}
				}
			}

		}
		long diff = System.currentTimeMillis() - startTime;
		r.log("BFS Time: " + diff + " ms");
		//printMap(dirMap,r);
		return dirMap;
	}

	public static LinkedList<Integer[]> rangeAST(MyRobot r, int startX, int startY, int endX, int endY, int range, boolean[][] blockers) {
		long startTime = System.currentTimeMillis();
		LinkedList<Integer[]> path;
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
		LinkedList<Integer[]> startList = findRange(r,startX,startY,range,blockers,dirMap);
		if (startList.size() == 0)
			return null;
		if(blockers[endY][endX])
			return null;
		while(!nodes.isEmpty()) {
			Integer[] curr = findNext(nodes, endX, endY, dirMap);
			nodes.remove(curr);
			int currY = curr[0];
			int currX = curr[1];

			LinkedList<Integer[]> nextList = findRange(r,currX,currY,range,blockers,dirMap);
			int step = dirMap[currY][currX];
			while(!nextList.isEmpty()) {
				Integer[] coor = nextList.poll();
				int coorY = coor[0];
				int coorX = coor[1];

				if (coorX == endX && coorY == endY) {
					dirMap[coorY][coorX] = 98;
					//printMap(dirMap,r);
					path = createPath(r,startX,startY,endX,endY,range,dirMap,blockers);
					long diff = System.currentTimeMillis() - startTime;
					//r.log("AST Time: " + diff + " ms");
					return path;
				}
				
				if(dirMap[coorY][coorX] > step + 1){
						if(coor[2]==1) {
							nodes.add(coor);
						}
						dirMap[coorY][coorX] = step + 1;
				}
			}
		}
		return null;
	}

	public static Integer[] findNext(LinkedList<Integer[]> nodes, int endX, int endY, int[][] dirMap) {
		Integer[] nextNode;
		int minStep = 9999;
		int minDist = 9999;
		for (Integer[] node: nodes) {
			int newDist = distance(node[1],node[0],endX,endY);
			if (newDist < minDist) {
				minStep = dirMap[node[0]][node[1]];
				minDist = newDist;
				nextNode = node;
			} else if (newDist == minDist) {
				int newStep = dirMap[node[0]][node[1]];
				if (newStep < minStep) {
					minStep = newStep;
					nextNode = node;
				}
			}
		}
		return nextNode;
	}

	public static LinkedList<Integer[]> createPath(MyRobot r, int startX, int startY, int endX, int endY, int range, int[][] dirMap, boolean[][] blockers) {
		LinkedList<Integer[]> path = new LinkedList<Integer[]>();
		LinkedList<Integer[]> nodes = new LinkedList<Integer[]>();

		Integer[] end = new Integer[2];
		end[1] = endX;
		end[0] = endY;
		nodes.add(end);

		while(!nodes.isEmpty()) {
			Integer[] node = nodes.poll();
			//r.log("X : " + node[1] + " Y: " + node[0]);
			Integer[] nextStep = checkAdj(r,node[1],node[0],startX,startY,range,dirMap,blockers);
			nextStep[0] = 0 - nextStep[0];
			nextStep[1] = 0 - nextStep[1];
			path.add(0,nextStep);

			//create the next node and assign it's coordinates
			Integer[] nextNode = new Integer[2];
			//r.log("Next X : " + nextStep[1] + " Y: " + nextStep[0]);
			nextNode[1] = node[1] - nextStep[1];
			nextNode[0] = node[0] - nextStep[0];

			if (nextNode[1] == startX && nextNode[0] == startY) {
				return path;
			}

			nodes.add(nextNode);
		}
		return path;
	}
	//something to note: because the path is generated backwards, every path is not distance efficient at the start

	public static Integer[] checkAdj(MyRobot r, int startX, int startY, int endX, int endY, int range, int[][] dirMap, boolean[][] blockers){
		int steps = (int)Math.floor(Math.sqrt(range));
		int min = 9999;
		int minDist = 9999;
		Integer[] move = new Integer[2];
		move[0] = 99;

		for(int y = 0 - steps; y <= steps; y++){
			for(int x = 0 - steps; x <= steps; x++){
				if (x == 0 && y == 0) {
					continue;
				}
				int dist = distance(0,0,x,y);
				int xCor = startX+x;
				int yCor = startY+y;
				if(dist <= range && checkBounds(r, xCor, yCor, blockers)){
					if (dirMap[yCor][xCor] < min){
						min = dirMap[yCor][xCor];
						minDist = distance(xCor,yCor,endX,endY);
						move[1] = x;
						move[0] = y;
					} else if (dirMap[yCor][xCor] == min) {
						int newDist = distance(xCor,yCor,endX,endY);
						if (newDist < minDist) {
							minDist = newDist;
							move[1] = x;
							move[0] = y;
						}
					}
				}
			}
		}
		if (move[0] == 99) {
			return null;
		}
		return move;
	}

	public static Integer[] retreatMove(MyRobot r, int myX, int myY, LinkedList<Integer[]> enemies, int range, boolean[][] blockers) {
		int steps = (int)Math.floor(Math.sqrt(range));
		Integer[] move = new Integer[2];
		move[0] = 99;
		int maxDist = 0;
		for(int y = 0 - steps; y <= steps; y++){
			for(int x = 0 - steps; x <= steps; x++){
				if (x == 0 && y == 0) {
					continue;
				}
				int dist = distance(0,0,x,y);
				int xCor = myX+x;
				int yCor = myY+y;
				if(dist <= range && checkBounds(r, xCor, yCor, blockers)){
					int newDist = 0;
					for(int i = 0; i < enemies.size(); i++){
						Integer[] enemyCoord = enemies.get(i);
						newDist+=distance(xCor,yCor,enemyCoord[1],enemyCoord[0]);
						//r.log("En X: " + (myX + x) + " Y: " + (myY + y));
					}
					//r.log("Dist: " + newDist);
					if (newDist > maxDist) {
						maxDist = newDist;
						move[1] = x;
						move[0] = y;
					}
				}
			}
		}
		if (move[0] == 99) {
			return null;
		}
		return move;
	}
}