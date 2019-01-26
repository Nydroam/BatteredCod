package bc19;
import java.util.LinkedList;
public class PilgrimBot extends Bot{
	Integer[] toGo;
	boolean deposit;
	boolean[][] endLocs;
	Integer[] castle;
	int[][] Rmap;
	int[][] Cmap;
	public PilgrimBot(MyRobot r){
		super(r);
		deposit = false;
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
			//Pathing.printMap(Rmap,r);
			/*LinkedList<Integer[]> path = Pathing.rangeAST(r,me.x,me.y,toGo[1],toGo[0],4,blockers);
			for(Integer[] step: path) {
				r.log("X: " + step[1] + " Y " + step[0]);
			}*/
		}
		if(me.turn % 10 == 0){
			endLocs = new boolean[r.map.length][r.map[0].length];
			endLocs[toGo[0]][toGo[1]] = true;
			Rmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
			endLocs = new boolean[r.map.length][r.map[0].length];
			endLocs[castle[0]][castle[1]] = true;
			Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
			//Pathing.printMap(Rmap,r);
		}
		if (!deposit){
			LinkedList<Integer[]> enemies = new LinkedList<Integer[]>();
			for(Robot other: visible){
				if (other.team != me.team && other.unit != 2){
					if (me.karbonite > 0 || me.fuel > 0) {
						deposit = true;
						break;
					} else {
						Integer[] enemyCoord = new Integer[2];
						enemyCoord[1] = other.x;
						enemyCoord[0] = other.y;
						enemies.add(enemyCoord);
					}
				}
			}
			if(!enemies.isEmpty()) {
				Integer[] move = Pathing.retreatMove(r,me.x,me.y,enemies,4,blockers);
				Action a = r.move(move[1], move[0]);
				if(!a.equals(null))
				return a; 
			}
			if(me.fuel == 100 || me.karbonite == 20){
				deposit = true;
				boolean dRange = false;
				for(Robot other : visible){
					if(other.team == me.team && (other.unit == 0 || other.unit == 1) && Pathing.distance(other.x,other.y,me.x,me.y) <= 25){
						dRange = true;
						castle[0] = other.y;
						castle[1] = other.x;
						endLocs = new boolean[r.map.length][r.map[0].length];
						endLocs[castle[0]][castle[1]] = true;
						Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
					}
				}
				if(!dRange && r.karbonite >= 50 && r.fuel >=200){//make church if no church/castle in range
					for(int dy = -1; dy <= 1; dy++){
						for(int dx = -1; dx <= 1; dx++){
							int xadj = me.x + dx;
							int yadj = me.y + dy;
							if(!blockers[yadj][xadj] && !r.getFuelMap()[yadj][xadj] && !r.getKarboniteMap()[yadj][xadj] && r.map[yadj][xadj]){
								castle[0] = yadj;
								castle[1] = xadj;
								endLocs = new boolean[r.map.length][r.map[0].length];
								endLocs[castle[0]][castle[1]] = true;
								Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
								return r.buildUnit(1,dx,dy);
							}

						}
					}
				}
			}
		}

		if (Pathing.distance(me.x,me.y,castle[1],castle[0])<=2 && deposit){
			deposit = false;
			return r.give(castle[1]-me.x,castle[0]-me.y,me.karbonite,me.fuel);
			
		}
		else if (me.x == toGo[1] && me.y == toGo[0] && !deposit){
			return r.mine();
		}
		if(deposit){

			Integer[] move = nextMove(Cmap);
			return r.move(move[1] - me.x, move[0] -me.y);
		}
		else{
			Integer[] move = nextMove(Rmap);
			return r.move(move[1] - me.x, move[0] -me.y);
		}
	}

}