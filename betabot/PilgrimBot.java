package bc19;
import java.util.LinkedList;
public class PilgrimBot extends Bot{
	Integer[] toGo;
	boolean running;
	boolean[][] endLocs;
	Integer[] castle;
	int[][] Rmap;
	int[][] Cmap;
	public PilgrimBot(MyRobot r){
		super(r);
		running = false;
	}
	public void extractSignal(int signal){
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
			/*LinkedList<Integer[]> path = Pathing.rangeAST(r,me.x,me.y,toGo[1],toGo[0],4,blockers);
			for(Integer[] step: path) {
				r.log("X: " + step[1] + " Y " + step[0]);
			}*/
		}
		if (!running){
		for(Robot c: visible){
			if (c.team != me.team && c.unit != 2){
				running = true;
				break;
			}
		}
		if(me.fuel == 100 || me.karbonite == 20){
			running = true;
		}
		}
		if (Pathing.distance(me.x,me.y,castle[1],castle[0])<=2 && running){
			running = false;
			return r.give(castle[1]-me.x,castle[0]-me.y,me.karbonite,me.fuel);
			
		}
		else if (me.x == toGo[1] && me.y == toGo[0] && !running){
			return r.mine();
		}
		if(running){
			Integer[] move = nextMove(Cmap);
			return r.move(move[1] - me.x, move[0] -me.y);
		}
		else{
			Integer[] move = nextMove(Rmap);
			return r.move(move[1] - me.x, move[0] -me.y);
		}
	}

}