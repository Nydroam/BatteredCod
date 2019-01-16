package bc19;
import java.util.LinkedList;
import java.lang.System;

public class Logistics {

	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	public static final int DIAGONAL = 2;
	public static final int ALL = 3;
	public static MyRobot rob;

	//takes in x and y of a castle and the symmetry (s) of a map to find an opposite castle
	public static LinkedList<Integer[]> findOpposite (MyRobot r, int x, int y, int s){
		int yMax = r.map.length - 1;
		int xMax = r.map[0].length - 1;
		LinkedList<Integer[]> coors = new LinkedList<Integer[]>();
		
		if( s == ALL || s == VERTICAL ){
			Integer[] coor = new Integer[2];
			coor[0] = y;
			coor[1] = xMax - x;
			coors.add(coor);
		}
		if( s == ALL || s == HORIZONTAL ){
			Integer[] coor = new Integer[2];
			coor[0] = yMax - y;
			coor[1] = x;
			coors.add(coor);
		}
		if( s == ALL || s == DIAGONAL ){
			Integer[] coor = new Integer[2];
			coor[0] = yMax - y;
			coor[1] = xMax - x;
			coors.add(coor);
		}
		
		return coors;

	}

	//takes a boolean map and determines what type of symmetry it is by comparing halves
	public static int symmetry (boolean[][] map, MyRobot r){
		long startTime = System.currentTimeMillis();
		rob = r;
		LinkedList<Boolean> halfMap = new LinkedList<Boolean>();
		boolean v = true;
		boolean h = true;
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				halfMap.add(map[y][x]);
			}
			if (!checkSym(halfMap)) {
				v = false;
				break;
			}
			halfMap = new LinkedList<Boolean>();	
		}
		halfMap = new LinkedList<Boolean>();
		for (int x = 0; x < map[0].length; x++) {
			for (int y = 0; y < map.length; y++) {
				halfMap.add(map[y][x]);
			}
			if (!checkSym(halfMap)) {
				h = false;
				break;
			}
			halfMap = new LinkedList<Boolean>();	
		}
		long diff = System.currentTimeMillis() - startTime;
		r.log("Symmetry Time: " + diff + " ms");
		if(v && h) {
			return ALL;
		} else if (v) {
			return VERTICAL;
		} else if (h) {
			return HORIZONTAL;
		} else {
			return DIAGONAL;
		}
	}

	//used in symmetry method to test a single column or row for symmetry
	public static boolean checkSym(LinkedList<Boolean> hM) {
		while (hM.size() > 1) {
			if (!hM.poll().equals(hM.pop())) {
				//rob.log("" + hM.get(0) + "  " + hM.get(hM.size() - 1));
				return false;
			}
		}
		return true;
	}

	//print a visual boolean map
	public static void printBoolMap(MyRobot r, boolean[][] map){
		for(int i = 0; i < map.length; i++){
			String line = "";
			for(int j = 0; j < map[0].length; j++){
				if(map[i][j])
					line += "T";
				else
					line += "-";
				line += " ";
			}
			r.log(line);
		}
		r.log("");
	}
}