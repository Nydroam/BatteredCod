package bc19;
import java.util.LinkedList;
public class ChurchBot extends Bot{
	LinkedList<Integer[]> lattice;
	LinkedList<Resource> karbList;
	LinkedList<Resource> aKarbList;
	LinkedList<Resource> fuelList;
	LinkedList<Resource> aFuelList;
	int[][] resMap;
	Resource allocate;
	Integer[] allocLat;
	public ChurchBot(MyRobot r){
		super(r);
	}
	public Action act(){
		Robot [] visible = r.getVisibleRobots();
		if(me.turn == 1){

			boolean[][] b = new boolean[r.map.length][r.map[0].length];
			boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];

			endLocs[me.y][me.x] = true;
			Task t = new Task();
			resMap = Pathing.rangeBFS(r,endLocs,4,b,t);
			karbList = t.karbList;
			fuelList = t.fuelList;

			for(int i = 0; i < karbList.size(); i++){
				Resource res = karbList.get(i);
				if(Pathing.distance(res.x,res.y,me.x,me.y) >= 36){
					karbList.remove(i);
					i--;
				}else
					r.log("RESOURCE: " + res.x + ", " + res.y);
			}
			r.log("KARBLIST: " + karbList);
			for(int i = 0; i < fuelList.size(); i++){
				Resource res = fuelList.get(i);
				if(Pathing.distance(res.x,res.y,me.x,me.y) >= 36){
					fuelList.remove(i);
					i--;
				}
			}
			r.log("FUELLIST: " + fuelList);
			aKarbList = new LinkedList<Resource>();
			aFuelList = new LinkedList<Resource>();
			lattice = t.lattice;
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
		//checking dead workers/soldiers
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

			boolean preacher = false;
			boolean enemySighted = false;
			int enemyCount = 0;
			int allyCount = 0;
			LinkedList<Integer[]> enemies = new LinkedList<Integer[]>();
			for(Robot other : visible){
				if(other.team != me.team){
					enemySighted = true;
					enemyCount++;
					int enemyY = other.y;
					int enemyX = other.x;
					Integer[] enemyCor = new Integer[2];
					enemyCor[0] = enemyY;
					enemyCor[1] = enemyX;
					enemies.add(enemyCor);
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
						a = spawnSoldier(4,enemies);
					
					}
				}
				else if(me.turn % 5 == 0){
					a = spawnSoldier(4,enemies);
				}
				//else if(numCastles == 1 && me.turn > 1 && me.turn < 10)
					//a = spawnSoldier(4);
				if(!a.equals(null)){
					return a;
				}
			}

			if(r.karbonite >= 10 && r.fuel >= 50){
				

				boolean deadKarb = false;
				for(Resource res: aKarbList){
					if(res.worker == -1)
						deadKarb = true;
				}
				if(deadKarb || karbList.size() > 0){
					BuildAction a = spawnWorker(true);
					if(!a.equals(null)){
						int s = 0;
						s += allocate.x*100;
						s += allocate.y;
						r.signal(s,2);
						return a;
					}
				}
				boolean deadFuel = false;
				for(Resource res: aFuelList){
					if(res.worker == -1)
						deadFuel = true;
				}
				if(deadFuel || fuelList.size() > 0){
					BuildAction a = spawnWorker(false);
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

	public BuildAction spawnSoldier(int unit, LinkedList<Integer[]> enemies){
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

		if(enemies.size() ==0 ){
		blockers[target[0]][target[1]] = false;
		LinkedList<Integer[]> path = Pathing.rangeAST(r,me.x,me.y,target[1],target[0],2,blockers);
			if (!path.equals(null) && path.size()>0) {
			Integer[] coord = path.poll();
				int s = 20000;
				s += target[1] * 100 + target[0];
				//r.log("Lattice (X, Y): " + target[1] + ", " + target[0]);
				r.signal(s,2);
				return r.buildUnit(unit,coord[1],coord[0]);
			}
		}else{
			Integer[] go = Pathing.retreatMove(r,me.x,me.y,enemies,4,blockers);
			if(!go.equals(null)){
				int s = 20000;
				s += target[1] * 100 + target[0];
				//r.log("Lattice (X, Y): " + target[1] + ", " + target[0]);
				r.signal(s,2);
				return r.buildUnit(unit,go[1],go[0]);
			}
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

		LinkedList<Integer[]> path = Pathing.rangeAST(r,me.x,me.y,allocate.x,allocate.y,2,blockers);
		if (!path.equals(null) && path.size()>0) {
		Integer[] coord = path.poll();
		
			return r.buildUnit(2,coord[1],coord[0]);
		}
		return null;
	}

}