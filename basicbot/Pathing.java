package bc19;
import java.util.LinkedList;
import java.lang.System;

public class Pathing{

	//BFS given end locations
	public static int[][] BFS(MyRobot r, boolean[][] endLocs, boolean[][] blockers){
		long startTime = System.currentTimeMillis();
		LinkedList<Integer[]> nodes = new LinkedList<Integer[]>();
		int yBound = r.map.length;
		int xBound = r.map[0].length;

		int[][] dirMap = new int[r.map.length][r.map[0].length];

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
			int y = curr[0];
			int x = curr[1];
			int newVal = dirMap[y][x] + 1;

			 
			if( x + 1 < xBound && r.map[y][x+1] && !blockers[y][x+1] && dirMap[y][x+1] > newVal){
				dirMap[y][x+1] = newVal;
				Integer[] next = new Integer[2];
				next[0] = y;
				next[1] = x+1;
				nodes.add(next);
			}
			if( x - 1 >= 0 && r.map[y][x-1] && !blockers[y][x-1] && dirMap[y][x-1] > newVal){
				dirMap[y][x-1] = newVal;
				Integer[] next = new Integer[2];
				next[0] = y;
				next[1] = x-1;
				nodes.add(next);
			}
			if( y + 1 < yBound && r.map[y+1][x] && !blockers[y+1][x] && dirMap[y+1][x] > newVal){
				dirMap[y+1][x] = newVal;
				Integer[] next = new Integer[2];
				next[0] = y+1;
				next[1] = x;
				nodes.add(next);
			}
			if( y - 1 >= 0 && r.map[y-1][x] && !blockers[y-1][x] && dirMap[y-1][x] > newVal){
				dirMap[y-1][x] = newVal;
				Integer[] next = new Integer[2];
				next[0] = y-1;
				next[1] = x;
				nodes.add(next);
			}
			if( x + 1 < xBound && y + 1 < yBound && r.map[y+1][x+1] && !blockers[y+1][x+1] && dirMap[y+1][x+1] > newVal){
				dirMap[y+1][x+1] = newVal;
				Integer[] next = new Integer[2];
				next[0] = y+1;
				next[1] = x+1;
				nodes.add(next);
			}
			if( x + 1 < xBound && y - 1 >= 0 && r.map[y-1][x+1] && !blockers[y-1][x+1] && dirMap[y-1][x+1] > newVal){
				dirMap[y-1][x+1] = newVal;
				Integer[] next = new Integer[2];
				next[0] = y-1;
				next[1] = x+1;
				nodes.add(next);
			}
			if( x - 1 >= 0 && y + 1 < yBound && r.map[y+1][x-1] && !blockers[y+1][x-1] && dirMap[y+1][x-1] > newVal){
				dirMap[y+1][x-1] = newVal;
				Integer[] next = new Integer[2];
				next[0] = y+1;
				next[1] = x-1;
				nodes.add(next);
			}
			if( x - 1 >= 0 && y - 1 >= 0 && r.map[y-1][x-1] && !blockers[y-1][x-1] && dirMap[y-1][x-1] > newVal){
				dirMap[y-1][x-1] = newVal;
				Integer[] next = new Integer[2];
				next[0] = y-1;
				next[1] = x-1;
				nodes.add(next);
			}
		}
		long diff = System.currentTimeMillis() - startTime;
		r.log("BFS Time: " + diff + " ms");
		return dirMap;
	}

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