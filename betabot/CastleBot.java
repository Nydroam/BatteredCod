package bc19;
import java.util.LinkedList;
import java.util.HashMap;
public class CastleBot extends Bot{
	

	public CastleBot(MyRobot r){
		super(r);
	}
	public Action act(){
		//r.log("Turn: " + me.turn);
		if(me.turn == 1){
			Task t = new Task();
			boolean[][] b = new boolean[r.map.length][r.map[0].length];
			boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];
			endLocs[me.y][me.x] = true;
			Pathing.rangeBFS(r,endLocs,4,b,t);
		}
		return null;
	}
}