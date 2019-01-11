package bc19;
public class MyRobot extends BCAbstractRobot {
	Bot bot = null;
    public Action turn() {

    	if(bot == null){
    		log("new robot made");
	        if(me.unit == SPECS.CASTLE)
	        	bot = new CastleBot(this);
	        if(me.unit == SPECS.CHURCH)
	        	bot = new ChurchBot(this);
	        if(me.unit == SPECS.PILGRIM)
	        	bot = new PilgrimBot(this);
	        if(me.unit == SPECS.CRUSADER)
	        	bot = new CrusaderBot(this);
	        if(me.unit == SPECS.PROPHET)
	        	bot = new ProphetBot(this);
	        if(me.unit == SPECS.PREACHER)
	        	bot = new PreacherBot(this);
    	}else{
    		bot.update(this);
    	}
        return bot.act();
    }
}