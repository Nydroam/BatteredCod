package bc19;
import java.util.LinkedList;
public class Bot{
	
	MyRobot r;
	Robot me;
	boolean[][] blockers;
	boolean[][] lblockers;
	//when the bot has all the information it needs to function
	boolean fullyInit;
	int numCastles;

	LinkedList<Integer[]> enemyCastles;
	LinkedList<Integer[]> myCastles;

	int symmetry;
	int currTarget;
	boolean even;

	public Bot(MyRobot r){
		currTarget = -1;
		update(r);
		//create checkerboard pattern of TF

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
		lblockers = new boolean[newr.map.length][newr.map[0].length];
		for(Robot other : r.getVisibleRobots()){
			if(r.isVisible(other) && other.id != me.id && other.team == me.team){
				lblockers[other.y][other.x] = true;
			}
		}
		
	}
	public Action act(){
		return null;
	}

	public AttackAction attack(){
		Robot target = null;
		if(currTarget == -1 || r.getRobot(currTarget).equals(null)){
		

		int min = 9999;
		for(Robot other : r.getVisibleRobots()){
			if(r.isVisible(other) && other.team != me.team){
				int dist = Pathing.distance(other.x,other.y,me.x,me.y);
				int range = 16;
				if(me.unit == 4 || me.unit == 0)
					range = 64;
				if(dist <= range){
					if(target == null || target.unit == 2 || other.unit != 2){
						if(dist < min && !(me.unit == 4 && dist < 16)){
							target = other;
							min = dist;
							currTarget = other.id;	
						}
					}


				}
			}

		}
	}
		else
			target = r.getRobot(currTarget);
		if(target.equals(null))
			return null;
		return r.attack(target.x-me.x,target.y-me.y);
		
	}
}