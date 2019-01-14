package bc19;
import java.util.LinkedList;
public class CrusaderBot extends Bot{

	int numCastles;
	LinkedList<Integer[]> opposite;
	int myCastle = -1;
	int symmetry;
	public CrusaderBot(MyRobot r){
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

				r.log("signal:" + sig);
				numCastles = (int)Math.floor(sig/10000);
			}
		}
		if(myCastle != -1 && numCastles > 0){
			if(numCastles > opposite.size()){

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
			}
			if(numCastles == opposite.size()){
				for(Integer[] c : opposite){
					r.log("Enemy Coordinates: " + c[1] + ", " + c[0]);
				}
			}
		}
		return null;
	}

}