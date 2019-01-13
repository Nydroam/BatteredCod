package bc19;
import java.util.LinkedList;
public class CastleBot extends Bot{
	int testData = 0;
	boolean firstCastle;
	LinkedList<Integer[]> opposite;
	int symmetry;
	public CastleBot(MyRobot r){
		super(r);
	}
	public Action act(){
		Robot [] visible = r.getVisibleRobots();
		if (me.turn == 1){
			symmetry = Logistics.symmetry(r.map,r);
			opposite = Logistics.findOpposite(r,me.x,me.y,symmetry);
			r.castleTalk(opposite.get(0)[1]);
			boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];
			LinkedList<Integer[]> range = Pathing.findRange(r,me.x,me.y,3);
			endLocs[opposite.get(0)[0]][opposite.get(0)[1]] = true;
			int[][] pathMap = Pathing.rangeBFS(r,endLocs,9);
		
			Integer[] move = Pathing.checkAdj(r, me.x, me.y, pathMap);
			Pathing.printMap(pathMap,r);
			return r.buildUnit(3,move[1],move[0]);
		}
		return null;
	}

}