package bc19;
import java.util.LinkedList;
import java.util.HashMap;
public class CastleBot extends Bot{
	
	//Resource locations split into unassigned and assigned
	LinkedList<Resource> karbList;
	LinkedList<Resource> aKarbList;
	LinkedList<Resource> fuelList;
	LinkedList<Resource> aFuelList;
	int[][] resMap;
	Resource allocate;

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
			resMap = Pathing.rangeBFS(r,endLocs,4,b,t);

			karbList = t.karbList;
			fuelList = t.fuelList;
			r.log("karb list size: " + karbList.size());
			aKarbList = new LinkedList<Resource>();
			aFuelList = new LinkedList<Resource>();
			r.log("Symmetry: " + symmetry);
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

		if(!allocate.equals(null)){//match a workerid with a resource
			LinkedList<Resource> resList;
			if(allocate.isKarb)
				resList = aKarbList;
			else
				resList = aFuelList;
			for(Robot other:visible){
				if(other.unit == 2 && other.team == me.team){
					boolean dup = false;
					for(Resource res: resList)
						if(other.id == res.worker){
							dup = true;
						}
					if(!dup){
						allocate.worker = other.id;
						break;
					}
				}
			}
			allocate = null;
		}

		if(!fullyInit){
			fullyInit = fullyInitialize();
		}

		if(fullyInit){//FULLY INITIALIZED, START DOING STUFF ====================================================================================
			
		}

		if(r.karbonite >= 10 && aKarbList.size() <3){
			BuildAction a = spawnWorker(true);
			if(!a.equals(null)){
				r.log("SPAWNED");
				int s = 0;
				s += allocate.x*100;
				s += allocate.y;
				r.signal(s,2);
				return a;
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

	public BuildAction spawnWorker(boolean karb){
		LinkedList<Resource> resList;
		if(karb)
			resList = aKarbList;
		else
			resList = aFuelList;
		boolean found = false;
		for(Resource res: resList){
			if(res.worker == -1){
				allocate = res;
				allocate.isKarb = karb;
				found = true;
				break;
			}
		}
		if(!found){
			LinkedList<Resource> oldList;
			if(karb)
				oldList = karbList;
			else
				oldList = fuelList;
			if(oldList.size()>0){
				allocate = oldList.poll();
				allocate.isKarb = karb;
				resList.add(allocate);
			} else
				return null;
		}
		int[][] nullMap = new int[r.map.length][r.map[0].length];
		for(int y = 0; y < nullMap.length; y++){
			for(int x = 0; x < nullMap.length; x++){
				nullMap [y][x] = 99;
			}
		}
		LinkedList<Integer[]> coords = Pathing.findRange(r,me.x,me.y,2,blockers,nullMap);
		
		if(coords.size()>0){
			Integer[] coor = coords.poll();
			int min = resMap[coor[0]][coor[1]];
			for(Integer[] c : coords){
				if(resMap[c[0]][c[1]]<min){
					coor = c;
					min = resMap[c[0]][c[1]];
				}
			}
			return r.buildUnit(2,coor[1] - me.x, coor[0] - me.y);
		}
		return null;
	}
}