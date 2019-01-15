package bc19;
public class Bot{
	
	MyRobot r;
	Robot me;
	boolean[][] blockers;

	public Bot(MyRobot r){
		update(r);
	}
	public void update(MyRobot newr){
		this.r = newr;
		this.me = r.me;
		blockers = new boolean[newr.map.length][newr.map[0].length];
		for(Robot other : r.getVisibleRobots()){
			if(r.isVisible(other)){
				blockers[other.y][other.x] = true;
			}
		}
		
	}
	public Action act(){
		return null;
	}
	
	public AttackAction attack(){
		Robot target = null;
		for(Robot other : r.getVisibleRobots()){
			if(r.isVisible(other) && other.team != me.team){
				int dist = Pathing.distance(other.x,other.y,me.x,me.y);
				int range = (me.id == 4) ? 64 : 16;
				if(dist <= range){
					target = other;
				}
			}
		}
		if(target.equals(null))
			return null;
		return r.attack(target.x-me.x,target.y-me.y);
	}

}