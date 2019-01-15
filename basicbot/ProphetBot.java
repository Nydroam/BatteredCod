package bc19;
import java.util.LinkedList;
public class ProphetBot extends Bot{
	int numCastles;
	LinkedList<Integer[]> opposite;
	int myCastle = -1;
	int symmetry;
	boolean fullyInit;
	int[][] dirMap;
	public ProphetBot(MyRobot r){
		super(r);
	}
	public Action act(){
		
		Robot [] visible = r.getVisibleRobots();
		if(myCastle == -1){
			for(Robot other : visible)
				if(r.isVisible(other) && other.team == me.team && other.unit == 0){
					
					myCastle = other.id;
					symmetry = Logistics.symmetry(r.map,r);
					opposite = Logistics.findOpposite(r,other.x,other.y,symmetry);
				}
		}
		if(myCastle != -1 && numCastles == 0){
			Robot castle = r.getRobot(myCastle);
			if(!castle.equals(null) && r.isRadioing(castle)){
				int sig = castle.signal;

				numCastles = (int)Math.floor(sig/10000);
			}
		}
		if(myCastle != -1 && numCastles > 0){
			if(!fullyInit){

				Robot castle = r.getRobot(myCastle);
				if(!castle.equals(null)&&r.isRadioing(castle)){

					int sig = castle.signal;
					Integer[] coor = new Integer[2];
					coor[0] = sig%100;
					coor[1] =(int)Math.floor(sig/100) % 100;
					boolean duplicate = false;
					for (Integer[] c : opposite){
						if(c[0].equals(coor[0]) && c[1].equals(coor[1])){
							duplicate = true;
							break;
						}
					}
					if(!duplicate)
						opposite.add(coor);
				}
				fullyInit = fullyInit();
			}
			AttackAction attack = attack();
			if(!attack.equals(null))
				return attack;
			if(fullyInit){
				
				boolean[][] endLocs = new boolean[r.map.length][r.map[0].length];
				for(int i = 0; i < opposite.size(); i++){
					int[][] map = r.getVisibleRobotMap();
					Integer[] c = opposite.get(i);
					if(map[c[0]][c[1]]==0){
						//r.log("REMOVAL==================================== : " + opposite.size());
						opposite.remove(i);
						dirMap = null;
						i--;
					}else
						endLocs[c[0]][c[1]] = true;
				}
				if (dirMap == null) {
					dirMap = Pathing.rangeBFS(r,endLocs,4,blockers);
					Pathing.printMap(dirMap,r);
				}
				Integer[] move = Pathing.getNextMove(r,r.me.x,r.me.y,2,dirMap,blockers);
				//r.log("Move to: " + move);
				if(move[0] == 0 && move[1] == 0) {
					return null;
				}
				return r.move(move[1],move[0]);
			}
		}
		return null;
	}
	public boolean fullyInit(){
		for(Integer[] c: opposite){
			if(c[0] == -1 || c[1] == -1)
				return false;
		}
		if(opposite.size()<numCastles)
			return false;
		return true;
	}
}