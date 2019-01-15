package bc19;
import java.util.LinkedList;
public class Task{
	
	boolean markFuel;
	boolean markKarb;

	LinkedList<Resource> karbList;
	LinkedList<Resource> fuelList;

	public Task(){
		markFuel = true;
		markKarb = true;
		karbList = new LinkedList<Resource>();
		fuelList = new LinkedList<Resource>();
	}


}