package bc19;
import java.util.LinkedList;
import java.lang.System;

public class Logistics {

	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	public static final int DIAGONAL = 2;
	public static final int ALL = 3;
	public static MyRobot rob;

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
		r.log("BFS Time: " + diff + " ms");
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

	public static boolean checkSym(LinkedList<Boolean> hM) {
		while (hM.size() > 1) {
			if (!hM.poll().equals(hM.pop())) {
				//rob.log("" + hM.get(0) + "  " + hM.get(hM.size() - 1));
				return false;
			}
		}
		return true;
	}
}