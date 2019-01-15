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
}