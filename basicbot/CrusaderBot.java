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
					int sig = other.signal;
					myCastle = other.id;
					numCastles = (int)Math.floor(sig/10000);
					symmetry = Logistics.symmetry(r.map,r);
					opposite = Logistics.findOpposite(r,other.x,other.y,symmetry);
				}
		}
		if(myCastle != -1){
			if(numCastles > opposite.size()){
				Robot castle = r.getRobot(myCastle);
				if(!castle.equals(null)&&r.isRadioing(castle)){
					int sig = castle.signal;
					Integer[] coor = new Integer[2];
					coor[0] = sig%100;
					coor[1] =(int)Math.floor(sig/100) % 100;
					opposite.add(coor);
				}
			}
			if(numCastles == opposite.size()){

			}
		}
		return null;
	}

}