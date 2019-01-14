package bc19;
public class Bot{
	
	MyRobot r;
	Robot me;
	boolean[][] blockers;

	public Bot(MyRobot r){
		this.r = r;
		this.me = r.me;
	}
	public void update(MyRobot newr){
		blockers = new boolean[newr.map.length][newr.map[0].length];
		for(Robot other : r.getVisibleRobots()){
			if(r.isVisible(other)){
				blockers[other.y][other.x] = true;
			}
		}
		this.r = newr;
		this.me = r.me;
	}
	public Action act(){
		return null;
	}

}