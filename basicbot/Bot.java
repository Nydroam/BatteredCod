package bc19;
public class Bot{
	
	MyRobot r;
	Robot me;
	public Bot(MyRobot r){
		this.r = r;
		this.me = r.me;
	}
	public void update(MyRobot newr){
		this.r = newr;
		this.me = r.me;
	}
	public Action act(){
		return null;
	}

}