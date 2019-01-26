package bc19;
import java.util.LinkedList;
public class ProphetBot extends Bot{
	LinkedList<Integer[]> targets;
	Integer[] toGo;
	boolean running;
	boolean[][] endLocs;
	Integer[] castle;
	Robot castleBot;
	int castleId;
	int[][] Rmap;
	int strat;
	boolean attack;
	public ProphetBot(MyRobot r){
		super(r);
		targets = new LinkedList<Integer[]>();
		attack = false;
	}
	public void calcMap(){
		endLocs = new boolean[r.map.length][r.map[0].length];
		/*for( Integer[] c : targets){
			endLocs[c[0]][c[1]] = true;
		}*/
		endLocs[toGo[0]][toGo[1]] = true;
		Rmap = Pathing.rangeBFS(r,endLocs,4,blockers,new Task());
		//Pathing.printMap(Rmap,r);
	}
	public void extractSignal(int signal){
		strat = (int)Math.floor(signal/10000);
		if(strat == 0){
			return;
		}
		else if (strat == 1 || strat == 2){
			Integer[] targ = new Integer[2];
			int sig = signal % 10000;
			int ycor = sig % 100;
			int xcor = (int)Math.floor((sig - ycor)/100);
			toGo[0] = ycor;
			toGo[1] = xcor;
			targ[1] = xcor;
			targ[0] = ycor;
			boolean have = false;
			for(int i = 0; i < targets.size(); i++){
				if(targets.get(i)[1] == xcor && targets.get(i)[0] == ycor){
					have = true;
					break;
				}
			}
			if(!have){
				targets.add(targ);
			}
			if (strat == 2){
				calcMap();
				attack = true;
			}
		}
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
				if (c.unit == 0 && c.team == me.team){
					castleId = c.id;
					castle[1] = c.x;
					castle[0] = c.y;
					int symmetry = Logistics.symmetry(r.map,r);
					for(Integer[] coor:Logistics.findOpposite(r,castle[1],castle[0],symmetry))
						targets.add(coor);
					break;
				}
			}
			//calcMap();
		}
		castleBot = r.getRobot(castleId);
		if (castleBot != null && r.isRadioing(castleBot) && strat != 2){
			int sig = castleBot.signal;
			extractSignal(sig);
			if(strat == 2){
				//r.log("ATTTCK");
			}
		}
		/*for(int i = 0; i < targets.size(); i++){
			Integer[] c = targets.get(i);
			int dist = Pathing.distance(c[1],c[0],me.x,me.y);
			if (dist <= 8){
				boolean found = false;
				for(Robot g: visible){
					if (g.unit == 0){
						found = true;
						break;
					}
				}
				if (!found){
					targets.remove(i);
					calcMap();
					i--;
				}
			}
		}*/
		boolean enemySeen = false;
		boolean minRange = false;
		LinkedList<Integer[]> enemies = new LinkedList<Integer[]>();
		boolean preacher = false;
		for(Robot other : visible){
			if(other.team != me.team){
				if(other.unit == 5)
					preacher = true;
				enemySeen = true;
				int dist = Pathing.distance(other.x,other.y,me.x,me.y);
				Integer[] enemyCoord = new Integer[2];
				enemyCoord[1] = other.x;
				enemyCoord[0] = other.y;
				enemies.add(enemyCoord);
				//r.log("Enemy At: " + other.x + "," + other.y);
				if(dist <= 16) {
					minRange = true;
					//r.log("Start Running");
				}
			}
		}
		if(minRange || (enemySeen && Pathing.distance(castle[1],castle[0],me.x,me.y) <= 2)){ //retret in opposite direction of enemy units
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

		if( !(me.x == toGo[1] && me.y == toGo[0]) && strat == 2 || r.fuelMap[me.y][me.x] || r.karboniteMap[me.y][me.x]){//forward march
		
			Integer[] move = nextMove(Rmap);
			Action a = r.move(move[1] - me.x, move[0] -me.y);
			if(!a.equals(null))
				return a; 
		}
		return null;
	}
}