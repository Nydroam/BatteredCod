package bc19;
import java.util.LinkedList;
import java.util.HashMap;
public class CastleBot extends Bot{
	int testData = 0;
	boolean firstCastle = true;
	int numCastles;
	LinkedList<Integer[]> opposite;
	LinkedList<Integer> signalQueue;
	int symmetry;
	int currSignal;

	public CastleBot(MyRobot r){
		super(r);
	}
	public void addSignals(){
		for (Integer[] c : opposite){
			if((c.length == 3 && c[0] >= 0 && c[1] >= 0) || numCastles == 1){
				r.log("X: " + c[1]);
				r.log("Y: " + c[0]);
				Integer signal = 10000 * numCastles;
				signal += 100 * c[1];
				signal += c[0];
				signalQueue.add(signal);
			}
		}

		r.log("signalQueue size:"+signalQueue.size());
		r.log("opposite size:"+opposite.size());
	}
	public BuildAction spawnUnit(int unitId){
		//spawn a crusader at optimal location
		boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];
		for(Integer[] c : opposite){
			if(c[0] >= 0 && c[1] >= 0)
				endLocs[c[0]][c[1]] = true;
		}

		int range = (unitId == 3) ? 9 : 4;
		int[][] pathMap = Pathing.rangeBFS(r,endLocs,9,blockers);
		Integer[] move = Pathing.checkAdj(r, me.x, me.y, pathMap, blockers);

		Pathing.printMap(pathMap,r);
		r.log("building: " + move[1] + ", " + move[0]);
		addSignals();
		if(currSignal == 0 && signalQueue.size()>0){
			currSignal = signalQueue.poll();
			r.log("currSignal " + currSignal);
			r.signal(currSignal,2);
		}
		return r.buildUnit(unitId,move[1],move[0]);
	}
	public Action act(){
		currSignal = 0;
		Robot [] visible = r.getVisibleRobots();
		r.log("Turn: " + me.turn);
		if (me.turn == 1){
			//check symmetry
			symmetry = Logistics.symmetry(r.map,r);
			opposite = Logistics.findOpposite(r,me.x,me.y,symmetry);
			signalQueue = new LinkedList<Integer>();

			//loop through visible robots and check their castletalk to determine which castle this one is
			for(Robot other : visible){
				if(other.castle_talk != 0 && other.id != r.id){
					firstCastle = false;
					int c = (int)Math.floor(other.castle_talk/100);
					if(c == 0)
						numCastles = 2;
					else
						numCastles = 3;
					Integer[] coor = new Integer[3];
					coor[0] = -1;
					coor[1] = other.castle_talk % 100;
					coor[2] = other.id;
					opposite.add(coor);
				}
			}
			if(firstCastle){
				numCastles = visible.length;
			}

			//broadcast enemy castle x coordinate
			if(numCastles == 2)
				r.castleTalk(opposite.get(0)[1]);
			if(numCastles == 3)
				r.castleTalk(opposite.get(0)[1] + 100);
		}
		else if(me.turn <= 3){

			for(Robot other : visible){
				if(other.castle_talk != 0 && other.id != r.id){
					Integer c = other.castle_talk % 100;
					boolean found = false;
					for(Integer[] l : opposite){
						if(l.length == 3 && l[2].equals(other.id)){
							
							found = true;
							l[0] = c;
							break;
						}
					}
					if(!found){
						Integer[] coor = new Integer[3];
						coor[0] = -1;
						coor[1] = other.castle_talk % 100;
						coor[2] = other.id;
						opposite.add(coor);
					}
				}
			}
			//broadcast enemy castle y coordinate
			if(me.turn == 2)
				r.castleTalk(opposite.get(0)[0]);
			
			
		}

		if(signalQueue.size()>0){
			currSignal = signalQueue.poll();
			r.signal(currSignal,2);
		}
		if(r.karbonite >= 20)
			return spawnUnit(3);
		return null;
	}

}