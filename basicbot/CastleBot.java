package bc19;
public class CastleBot extends Bot{
	int testData = 0;
	public CastleBot(MyRobot r){
		super(r);
	}
	public Action act(){
		if(testData == 0){
			testData = 1;
			r.log("SET ONCE");
		}
		return null;
	}

}