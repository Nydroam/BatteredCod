package bc19;
import java.util.LinkedList;
public class ProphetBot extends Bot{
	Integer[] toGo;
	boolean running;
	boolean[][] endLocs;
	Integer[] castle;
	Robot castleBot;
	int[][] Rmap;
	int[][] Cmap;
	int strat;
	public ProphetBot(MyRobot r){
		super(r);
	}
	public void extractSignal(int signal){
		strat = (int)Math.floor(signal/10000);
		int sig = signal % 10000;
		int ycor = sig % 100;
		int xcor = (int)Math.floor((sig - ycor)/100);
		toGo[1] = xcor;
		toGo[0] = ycor;

	}
	public Integer[] nextMove(int[][] map){
		int min = 9999;
		Integer[] curr;
		int[][] nullMap = new int[r.map.length][r.map[0].length];
		for(int y = 0; y < nullMap.length; y++){
			for(int x = 0; x < nullMap.length; x++){
				nullMap [y][x] = 99;
			}
		}
		LinkedList<Integer[]> moves = Pathing.findRange(r,me.x,me.y,4,blockers,nullMap);
		for(Integer[] x: moves){
			if(map[x[0]][x[1]] < min){
				min = map[x[0]][x[1]];
				curr = x;
			}
		}
		return curr;
	}
	public Action act(){
		Robot [] visible = r.getVisibleRobots();
		if(me.turn == 1){
			toGo = new Integer[2];
			castle = new Integer[2];
	
			for(Robot c : visible){
				if (c.unit == 0 && r.isRadioing(c)){
					castleBot = c;
					int sig = c.signal;
					castle[1] = c.x;
					castle[0] = c.y;
					extractSignal(sig);
					break;
				}
			}
			endLocs = new boolean[r.map.length][r.map[0].length];
			endLocs[toGo[0]][toGo[1]] = true;
			Rmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
			endLocs = new boolean[r.map.length][r.map[0].length];
			endLocs[castle[0]][castle[1]] = true;
			Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
			
		}
		boolean enemySeen = false;
		boolean minRange = false;
		LinkedList<Integer[]> enemies = new LinkedList<Integer[]>();
		for(Robot other : visible){
			if(other.team != me.team){
				enemySeen = true;
				int dist = Pathing.distance(other.x,other.y,me.x,me.y);
				Integer[] enemyCoord = new Integer[2];
				enemyCoord[1] = other.x;
				enemyCoord[0] = other.y;
				enemies.add(enemyCoord);
				r.log("Enemy At: " + other.x + "," + other.y);
				if(dist <= 16) {
					minRange = true;
					r.log("Start Running");
				}
			}
		}
		if(minRange){ //retret in opposite direction of enemy units
			//Integer[] move = nextMove(Cmap);
			//Action a = r.move(move[1] - me.x, move[0] -me.y);
			Integer[] move = Pathing.retreatMove(r,me.x,me.y,enemies,4,blockers);
			Action a = r.move(move[1], move[0]);
			if(!a.equals(null))
				return a; 
		}
		if(enemySeen){
			Action atk = attack();
			if(!atk.equals(null))
				return atk;
		}

		if(me.turn <= 2 || strat == 1 || r.fuelMap[me.y][me.x] || r.karboniteMap[me.y][me.x]){//forward march
			Integer[] move = nextMove(Rmap);
			Action a = r.move(move[1] - me.x, move[0] -me.y);
			if(!a.equals(null))
				return a; 
		}

		return null;
	}
}