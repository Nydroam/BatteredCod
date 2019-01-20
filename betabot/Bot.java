package bc19;
import java.util.LinkedList;
public class Bot{
	
	MyRobot r;
	Robot me;
	boolean[][] blockers;

	//when the bot has all the information it needs to function
	boolean fullyInit;
	int numCastles;

	LinkedList<Integer[]> enemyCastles;
	LinkedList<Integer[]> myCastles;

	int symmetry;

	public Bot(MyRobot r){
		update(r);
	}
	public void update(MyRobot newr){
		this.r = newr;
		this.me = r.me;
		blockers = new boolean[newr.map.length][newr.map[0].length];
		for(Robot other : r.getVisibleRobots()){
			if(r.isVisible(other) && other.id != me.id){
				blockers[other.y][other.x] = true;
			}
		}
		
	}
	public Action act(){
		return null;
	}
}