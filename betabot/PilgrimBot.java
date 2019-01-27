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
			symmetry = Logistics.symmetry(r.map,r);
			for(Robot c : visible){
				if (c.unit <= 1  && r.isRadioing(c) && Pathing.distance(c.x,c.y,me.x,me.y) <= 2){
					if(c.unit == 0)
						enemyCastles=(Logistics.findOpposite(r,c.x,c.y,symmetry));
					int sig = c.signal;
					castle[1] = c.x;
					castle[0] = c.y;
					extractSignal(sig);
					break;
				}
			}

			if(enemyCastles != null){
			for(int y = 0; y < blockers.length; y++){
				for(int x = 0; x < blockers[0].length; x++){
					for(Integer[] c : enemyCastles){
						if(Pathing.distance(c[1],c[0],x,y) <= 100)
							blockers[y][x] = true;
					}
				}
			}
		}

			endLocs = new boolean[r.map.length][r.map[0].length];
			endLocs[toGo[0]][toGo[1]] = true;
			Rmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
			endLocs = new boolean[r.map.length][r.map[0].length];
			endLocs[castle[0]][castle[1]] = true;
			Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
		}


		if(enemyCastles != null){
			for(int y = 0; y < blockers.length; y++){
				for(int x = 0; x < blockers[0].length; x++){
					for(Integer[] c : enemyCastles){
						if(Pathing.distance(c[1],c[0],x,y) <= 100)
							blockers[y][x] = true;
					}
				}
			}
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
				if (other.team != me.team && other.unit != 1 && other.unit != 2){
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
	boolean dRange = false;
				int castleDist = 9999;
				for(Robot other : visible){
					if(other.team == me.team && (other.unit == 0 || other.unit == 1)){
						int newCastleDist = Pathing.distance(other.x,other.y,me.x,me.y);
						if (newCastleDist <=36 && newCastleDist < castleDist) {
							castleDist = newCastleDist;
							dRange = true;
							castle[0] = other.y;
							castle[1] = other.x;
							endLocs = new boolean[r.map.length][r.map[0].length];
							endLocs[castle[0]][castle[1]] = true;
							Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
						}
					}
				}
				if(!dRange && r.karbonite >= 50 && r.fuel >=300){//make church if no church/castle in range
					int range = 36; //range to check for distance to resources
					int steps = (int)Math.floor(Math.sqrt(range));
					LinkedList<Integer[]> churchTile = new LinkedList<Integer[]>();
					int maxResources = 0;
					int minDist = 9999;
					Integer[] toBuild = new Integer[2];
					for(int dy = -1; dy <= 1; dy++){ //record all adjacent tiles to check for distance to resources
						for(int dx = -1; dx <= 1; dx++){
							int xadj = me.x + dx;
							int yadj = me.y + dy;
							if(Pathing.checkBounds(r,xadj,yadj,blockers) && !r.getFuelMap()[yadj][xadj] && !r.getKarboniteMap()[yadj][xadj]){
								Integer[] tile = new Integer[2];
								tile[1] = dx;
								tile[0] = dy;
								churchTile.add(tile);
							}
						}
					}
					for(Integer[] tile : churchTile) { //check each adjacent tile for distance to resources
						int numResources = 0;
						int newDist = 0;
						for(int y = 0 - steps; y <= steps; y++){
							for(int x = 0 - steps; x <= steps; x++){
								int dist = Pathing.distance(0,0,x,y);
								int xCor = tile[1] + x + me.x;
								int yCor = tile[0] + y + me.y;
								if(dist <= range && Pathing.checkBounds(r,xCor,yCor,blockers) && (r.getFuelMap()[yCor][xCor] || r.getKarboniteMap()[yCor][xCor])){
									numResources += 1;
									newDist += Pathing.distance(me.x+tile[1],me.y+tile[0],xCor,yCor);
								}
							}
						}
						if (numResources > maxResources) {
							maxResources = numResources;
							minDist = newDist;
							castle[1] = me.x+tile[1];
							castle[0] = me.y+tile[0];
							toBuild[1] = tile[1];
							toBuild[0] = tile[0];
						}
						if (numResources == maxResources) {
							if (newDist < minDist) {
								minDist = newDist;
								castle[1] = me.x+tile[1];
								castle[0] = me.y+tile[0];
								toBuild[1] = tile[1];
								toBuild[0] = tile[0];
							}
						}
						
					}
					endLocs = new boolean[r.map.length][r.map[0].length];
					endLocs[castle[0]][castle[1]] = true;
					Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
					
					r.signal(1,2);
					return r.buildUnit(1,toBuild[1],toBuild[0]);
				}
				deposit = true;
				}
		}

		if (Pathing.distance(me.x,me.y,castle[1],castle[0])<=2 && deposit){
			deposit = false;
			return r.give(castle[1]-me.x,castle[0]-me.y,me.karbonite,me.fuel);
			
		}
		else if (me.x == toGo[1] && me.y == toGo[0] && !deposit){
			if (me.karbonite == 0 && me.fuel == 0) {
				boolean dRange = false;
				int castleDist = 9999;
				for(Robot other : visible){
					if(other.team == me.team && (other.unit == 0 || other.unit == 1)){
						int newCastleDist = Pathing.distance(other.x,other.y,me.x,me.y);
						if (newCastleDist <=36 && newCastleDist < castleDist) {
							castleDist = newCastleDist;
							dRange = true;
							castle[0] = other.y;
							castle[1] = other.x;
							endLocs = new boolean[r.map.length][r.map[0].length];
							endLocs[castle[0]][castle[1]] = true;
							Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
						}
					}
				}
				if(!dRange && r.karbonite >= 50 && r.fuel >=300){//make church if no church/castle in range
					int range = 36; //range to check for distance to resources
					int steps = (int)Math.floor(Math.sqrt(range));
					LinkedList<Integer[]> churchTile = new LinkedList<Integer[]>();
					int maxResources = 0;
					int minDist = 9999;
					Integer[] toBuild = new Integer[2];
					for(int dy = -1; dy <= 1; dy++){ //record all adjacent tiles to check for distance to resources
						for(int dx = -1; dx <= 1; dx++){
							int xadj = me.x + dx;
							int yadj = me.y + dy;
							if(Pathing.checkBounds(r,xadj,yadj,blockers) && !r.getFuelMap()[yadj][xadj] && !r.getKarboniteMap()[yadj][xadj]){
								Integer[] tile = new Integer[2];
								tile[1] = dx;
								tile[0] = dy;
								churchTile.add(tile);
							}
						}
					}
					for(Integer[] tile : churchTile) { //check each adjacent tile for distance to resources
						int numResources = 0;
						int newDist = 0;
						for(int y = 0 - steps; y <= steps; y++){
							for(int x = 0 - steps; x <= steps; x++){
								int dist = Pathing.distance(0,0,x,y);
								int xCor = tile[1] + x + me.x;
								int yCor = tile[0] + y + me.y;
								if(dist <= range && Pathing.checkBounds(r,xCor,yCor,blockers) && (r.getFuelMap()[yCor][xCor] || r.getKarboniteMap()[yCor][xCor])){
									numResources += 1;
									newDist += Pathing.distance(me.x+tile[1],me.y+tile[0],xCor,yCor);
								}
							}
						}
						if (numResources > maxResources) {
							maxResources = numResources;
							minDist = newDist;
							castle[1] = me.x+tile[1];
							castle[0] = me.y+tile[0];
							toBuild[1] = tile[1];
							toBuild[0] = tile[0];
						}
						if (numResources == maxResources) {
							if (newDist < minDist) {
								minDist = newDist;
								castle[1] = me.x+tile[1];
								castle[0] = me.y+tile[0];
								toBuild[1] = tile[1];
								toBuild[0] = tile[0];
							}
						}
						
					}
					endLocs = new boolean[r.map.length][r.map[0].length];
					endLocs[castle[0]][castle[1]] = true;
					Cmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
					
					r.signal(1,2);
					return r.buildUnit(1,toBuild[1],toBuild[0]);
				}
			}
			return r.mine();
		}
		if(deposit){

			Integer[] move = nextMove(Cmap);
			return r.move(move[1] - me.x, move[0] -me.y);
		}
		else{
			Integer[] move = nextMove(Rmap);
			if (Rmap[move[0]][move[1]] == Rmap[me.y][me.x]) {
				return null;
			}
			return r.move(move[1] - me.x, move[0] -me.y);
		}
	}

}