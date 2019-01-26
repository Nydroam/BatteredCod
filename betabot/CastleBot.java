package bc19;
import java.util.LinkedList;
import java.util.HashMap;
public class CastleBot extends Bot{
	
	//Resource locations split into unassigned and assigned
	LinkedList<Resource> karbList;
	LinkedList<Resource> aKarbList;
	LinkedList<Resource> fuelList;
	LinkedList<Resource> aFuelList;
	LinkedList<Integer[]> lattice;
	int[][] resMap;
	Resource allocate;
	Integer[] allocLat;
	
	
	int signalCount;
	public CastleBot(MyRobot r){
		super(r);
	}
	public Action act(){

		Robot [] visible = r.getVisibleRobots();
		r.log("Turn: " + me.turn);
		/*if(!aFuelList.equals(null) && me.id==2533)
			for(Resource res: aFuelList)
				r.log("Fuel X: " + res.x + "Y: " + res.y + " ID: " + res.worker);
		if(!aKarbList.equals(null)&& me.id == 2533)
			for(Resource res: aKarbList)
				r.log("Karb X: " + res.x + "Y: " + res.y + " ID: " + res.worker);*/
		if(signalCount == 0)
			signalCount = -1;
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

			
			//r.log("Symmetry: " + symmetry);

			Task t = new Task();
			boolean[][] b = new boolean[r.map.length][r.map[0].length];
			boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];
			
			endLocs[me.y][me.x] = true;

			resMap = Pathing.rangeBFS(r,endLocs,4,b,t);
			
			karbList = t.karbList;
			fuelList = t.fuelList;
			aKarbList = new LinkedList<Resource>();
			aFuelList = new LinkedList<Resource>();
			lattice = t.lattice;

			if(numCastles == 1){
				int[][] printLattice = new int[r.map.length][r.map[0].length];
					for(Integer[] l : lattice)
						printLattice[l[0]][l[1]] = l[3];
					Pathing.printMap(printLattice,r);
			}
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
			
		}
		if(!allocLat.equals(null)){//match a ranger with a destination
			for(Robot other:visible){
				if(other.unit > 2 && Pathing.distance(other.x,other.y,me.x,me.y) <=36 && other.team == me.team && other.turn == 1){
					allocLat[2] = other.id;
					break;
				}
			}
			allocLat = null;
		}
		if(!allocate.equals(null) && !aKarbList.equals(null)){//match a workerid with a resource
			LinkedList<Resource> resList;
			LinkedList<Resource> otherList;
			if(allocate.isKarb){
				resList = aKarbList;
				otherList = aFuelList;
			}
			else{
				resList = aFuelList;
				otherList = aKarbList;
			}

			for(Robot other:visible){
				if(other.unit == 2 && Pathing.distance(other.x,other.y,me.x,me.y) <=36 && other.team == me.team ){
					boolean dup = false;
					for(Resource res: resList)
						if(other.id == res.worker){
							dup = true;
						}
					for(Resource res: otherList)
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

		//broadcast MY castle y coordinate
			if(me.turn == 2){
				r.castleTalk(me.y + 1);
				
			}

		if(!fullyInit){
			fullyInit = fullyInitialize();
			if(fullyInit){//do stuff ONCE after fully initialized

				

				//ENEMY CASTLE INIT
				for(Integer[] c: myCastles){
					if(c[0]==me.y && c[1] == me.x)
						continue;
					enemyCastles.add(Logistics.findOpposite(r,c[1],c[0],symmetry).poll());
				}

				

				//RESOURCE ALLOCATION ===========================================//
				if(numCastles > 1){
				Task t = new Task();
				boolean[][] b = new boolean[r.map.length][r.map[0].length];
				boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];
				for(Integer[] c : myCastles)
					endLocs[c[0]][c[1]] = true;
				resMap = Pathing.rangeBFS(r,endLocs,4,b,t);

				karbList = t.karbList;
				karbList.poll();
				fuelList = t.fuelList;
				lattice = t.lattice;
					int[][] printLattice = new int[r.map.length][r.map[0].length];
					for(Integer[] l : lattice)
						printLattice[l[0]][l[1]] = l[3];
					Pathing.printMap(printLattice,r);
					/*
					if(r.karbonite >= 25 && r.fuel >= 50 ) {
				Action a = spawnSoldier(4);
				if(!a.equals(null)){
					return a;
				}
				}*/
				
			}
			}
		}
		if(fullyInit){//FULLY INITIALIZED, START DOING STUFF ====================================================================================
			
			//replacing dead workers
				for(Resource res : aKarbList){
					if(res.worker != -1 && !res.equals(allocate)){
						Robot other = r.getRobot(res.worker);
						if(other.equals(null))
							res.worker = -1;
					}
				}
				for(Resource res : aFuelList){
					if(res.worker != -1 && !res.equals(allocate)){
						Robot other = r.getRobot(res.worker);
						if(other.equals(null))
							res.worker = -1;
					}
				}
				for(Integer[] c : lattice){
					if(c[2] != -1){
						if(r.getRobot(c[2]).equals(null))
							c[2] = -1;
					}
				}
			/*if(me.turn%75 == 0 && me.turn > 0){
				if(numCastles == 1){
					Integer[] enemy = enemyCastles.get(0);
					r.signal(20000 + enemy[1] * 100 + enemy[0],100);
				}else
					signalCount = numCastles-1;
			}
			if(signalCount > 0){
				int s = 10000;
				if(signalCount == 1)
					s = 20000;
				Integer[] c = enemyCastles.get(signalCount);
				
				s += c[1] * 100 + c[0];
				//r.log("Sending Signal: " + s);
				r.signal(s,100);
				signalCount--;
			}*/
			boolean preacher = false;
			boolean enemySighted = false;
			int enemyCount = 0;
			int allyCount = 0;
			for(Robot other : visible){
				if(other.team != me.team){
					enemySighted = true;
					enemyCount++;
					if(other.unit == 5 && Pathing.distance(me.x,me.y,other.x,other.y) <= 16){
						preacher = true;
						break;
					}
					
				}else if(other.unit > 2)
					allyCount ++;
			}
			if(r.karbonite >= 25 && r.fuel >= 50 && !preacher) {
				Action a = null;
				if(enemySighted && allyCount < enemyCount){
					if(r.fuel > 200){
						a = spawnSoldier(4);
					}
				}
				else if(me.turn % 10 == 0 && me.turn >= 25)
					a = spawnSoldier(4);
				//else if(numCastles == 1 && me.turn > 1 && me.turn < 10)
					//a = spawnSoldier(4);
				if(!a.equals(null)){
					return a;
				}
			}

			if(enemySighted && r.fuel >= 10){
				Action a = attack();
				if(!a.equals(null))
					return a;
			}
			
		}
		if( r.karbonite >= 10 && r.fuel >= 50 && (me.turn == 1 || fullyInit)) {
		
			boolean deadFuel = false;
			for(Resource res: aFuelList){
				if(res.worker == -1)
					deadFuel = true;
			}
			if(me.turn > 20 && !fuelList.equals(null) && (fuelList.size() > 0 || deadFuel)) {
				//r.log("FUELED+============");
					BuildAction a = spawnWorker(false);
					if(!a.equals(null)){
						int s = 0;
						s += allocate.x*100;
						s += allocate.y;
						r.signal(s,2);
						return a;
					}
				
			}
			boolean deadKarb = false;
			for(Resource res: aKarbList){
				if(res.worker == -1)
					deadKarb = true;
			}
			if(!karbList.equals(null) && (karbList.size() > 0 || deadKarb)){
				//r.log("Karbonite worker");
				BuildAction a = spawnWorker(true);
				if(!a.equals(null)){
					int s = 0;
					s += allocate.x*100;
					s += allocate.y;
					r.signal(s,2);
					return a;
				}
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

	public BuildAction spawnSoldier(int unit){
		Integer[] target;
		for(Integer[] c : lattice){
			if(c[2] == -1){
				allocLat = c;
				target = c;
				break;
			}
		}
		if(target.equals(null))
			return null;

		LinkedList<Integer[]> path = Pathing.rangeAST(r,me.x,me.y,target[1],target[0],2,blockers);
		if (!path.equals(null) && path.size()>0) {
		Integer[] coord = path.poll();
			int s = 20000;
			s += target[1] * 100 + target[0];
			//r.log("Lattice (X, Y): " + target[1] + ", " + target[0]);
			r.signal(s,2);
			return r.buildUnit(unit,coord[1],coord[0]);
		}
		return null;
	}
	public BuildAction spawnWorker(boolean karb){
		LinkedList<Resource> resList;
		if(karb)
			resList = aKarbList;
		else
			resList = aFuelList;
		boolean found = false;
		for(int i = 0; i < resList.size(); i++){
			Resource res = resList.get(i);
			if(res.worker == -1){
				if(res.priority > (int)Math.floor(r.map.length/4)){
					resList.remove(i);
					i--;
				}else{
					allocate = res;
					allocate.isKarb = karb;
					found = true;
					break;
				}
			}
		}
		/*for(Resource res: resList){
			if(res.worker == -1){
				allocate = res;
				allocate.isKarb = karb;
				found = true;
				break;
			}
		}*/
		if(!found){
			LinkedList<Resource> oldList;
			if(karb)
				oldList = karbList;
			else
				oldList = fuelList;
			if(oldList.size()>0){
				allocate = oldList.poll();
				allocate.isKarb = karb;
				/*if(allocate.priority > 5){
					allocate = null;
					return null;
				}*/
				for(Integer[] c : enemyCastles){

					if(Pathing.distance(c[1],c[0],allocate.x,allocate.y)<=100 ){
						//oldList.add(allocate);
						allocate = null;
						return null;
					}
				}
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
		/*LinkedList<Integer[]> coords = Pathing.findRange(r,me.x,me.y,2,blockers,nullMap);
		
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
		return null;*/
		/*Integer[] coord = Pathing.checkAdj(r,me.x,me.y,allocate.x,allocate.y,2,nullMap,blockers);
		if (coord != null) {
			return r.buildUnit(2,coord[1], coord[0]);
		}
		return null;*/
		LinkedList<Integer[]> path = Pathing.rangeAST(r,me.x,me.y,allocate.x,allocate.y,2,blockers);
		if (!path.equals(null) && path.size()>0) {
		Integer[] coord = path.poll();
		
			return r.buildUnit(2,coord[1],coord[0]);
		}
		return null;
	}
}