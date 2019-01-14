package bc19;
import java.util.LinkedList;
import java.util.HashMap;
public class CastleBot extends Bot{
	int testData = 0;
	boolean firstCastle = true;
	int numCastles;
	LinkedList<Integer[]> opposite;
	int symmetry;
	public CastleBot(MyRobot r){
		super(r);
	}
	public Action act(){
		Robot [] visible = r.getVisibleRobots();
		if (me.turn == 1){
			//check symmetry
			symmetry = Logistics.symmetry(r.map,r);
			opposite = Logistics.findOpposite(r,me.x,me.y,symmetry);
			

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
			/*
			//spawn a crusader at optimal location
			boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];
			LinkedList<Integer[]> range = Pathing.findRange(r,me.x,me.y,3);
			endLocs[opposite.get(0)[0]][opposite.get(0)[1]] = true;
			int[][] pathMap = Pathing.rangeBFS(r,endLocs,9);
		
			Integer[] move = Pathing.checkAdj(r, me.x, me.y, pathMap);
			Pathing.printMap(pathMap,r);
			return r.buildUnit(3,move[1],move[0]);
			*/
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
			if(me.turn == 3){
				for(Integer[] c : opposite){
					r.log("Enemy Coordinates: " + c[1] + ", " + c[0]);
				}
			}
		}
		return null;
	}

}