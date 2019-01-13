package bc19;
public class CastleBot extends Bot{
	int testData = 0;
	public CastleBot(MyRobot r){
		super(r);
	}
	public Action act(){
		if (me.turn == 1){
			boolean [][] endLocs = new boolean[r.map.length][r.map[0].length];
			boolean [][] blockers = new boolean[r.map.length][r.map[0].length];
			endLocs[me.y][me.x] = true;
			int[][] pathMap = Pathing.rangeBFS(r,endLocs,9);
			Pathing.printMap(pathMap,r);
			r.log("Symmetry: " + Logistics.symmetry(r.map,r));
		}
		return null;
	}

}