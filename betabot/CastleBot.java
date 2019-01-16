package bc19;
import java.util.LinkedList;
import java.util.HashMap;
public class CastleBot extends Bot{
	
	//Resource locations split into unassigned and assigned
	LinkedList<Resource> karbList;
	LinkedList<Resource> aKarbList;
	LinkedList<Resource> fuelList;
	LinkedList<Resource> aFuelList;

	public CastleBot(MyRobot r){
		super(r);
	}
	public Action act(){
		Robot [] visible = r.getVisibleRobots();
		r.log("Turn: " + me.turn);
		if(me.turn == 1){//TURN 1---------------------------------------------------------------------------------------

			//Initialization

			symmetry = Logistics.symmetry(r.map,r);
			enemyCastles = Logistics.findOpposite(r,me.x,me.y,symmetry);
			myCastles = new LinkedList<Integer[]>();
			Integer[] myCor = new Integer[3];
			myCor[0] = me.y;
			myCor[1] = me.x;
			myCastles.add(myCor);


			//DETERMINING NUMBER OF CASTLES ==========================
			//loop through visible robots and check their castletalk to determine which castle this one is
			boolean firstCastle = true;
			int count = 1;
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
					coor[1] = other.castle_talk % 100 - 1;
					coor[2] = other.id;
					myCastles.add(coor);
				}
				if(!r.isVisible(other))
					count++;
			}
			if(firstCastle){
				numCastles = count;
			}

			//broadcast enemy castle x coordinate
			if(numCastles == 2)
				r.castleTalk(me.x + 1);
			if(numCastles == 3)
				r.castleTalk(me.x + 101);

			//RESOURCE ALLOCATION ===========================================//
			Task t = new Task();
			boolean[][] b = new boolean[r.map.length][r.map[0].length];
			boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];
			endLocs[me.y][me.x] = true;
			Pathing.rangeBFS(r,endLocs,4,b,t);





		}
		else if( me.turn <= 3){ //TURN 2-3 =================================================================================================
			for(Robot other : visible){
				if(other.castle_talk != 0 && other.id != r.id){
					Integer c = other.castle_talk % 100 - 1;
					boolean found = false;
					for(Integer[] l : myCastles){
						if(l.length == 3 && l[2].equals(other.id)){
							
							found = true;
							l[0] = c;
							break;
						}
					}
					if(!found){
						Integer[] coor = new Integer[3];
						coor[0] = -1;
						coor[1] = other.castle_talk % 100 - 1;
						coor[2] = other.id;
						myCastles.add(coor);
					}
				}
			}
			//broadcast MY castle y coordinate
			if(me.turn == 2)
				r.castleTalk(me.y + 1);
		}

		if(!fullyInit){
			fullyInit = fullyInitialize();
		}

		if(fullyInit){//FULLY INITIALIZED, START DOING STUFF ====================================================================================
			for(Integer[] c : myCastles){
				r.log("Castle At: " + c[1] + ", " + c[0]);
			}
		}

		return null;
	}
	
	public boolean fullyInitialize(){

		for(Integer[] c: myCastles){
			if(c[0] == -1 || c[1] == -1)
				return false;
		}
		if(myCastles.size()<numCastles)
			return false;
		return true;
	}
}