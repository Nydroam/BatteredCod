package bc19;
import java.util.LinkedList;
public class Task{
	
	boolean markFuel;
	boolean markKarb;

	LinkedList<Resource> karbList;
	LinkedList<Resource> fuelList;

	LinkedList<Integer[]> lattice;

	public Task(){
		markFuel = true;
		markKarb = true;
		karbList = new LinkedList<Resource>();
		fuelList = new LinkedList<Resource>();
		lattice = new LinkedList<Integer[]>();
	}


}