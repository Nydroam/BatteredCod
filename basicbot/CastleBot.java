package bc19;
public class CastleBot extends Bot{
	public CastleBot(MyRobot r){
		super(r);
	}
	public Action act(){
		r.log(""+me.karbonite);
		return null;
	}

}