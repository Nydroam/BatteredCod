package bc19;
import java.util.LinkedList;
public class Resource{

	int x;
	int y;
	int worker;
	int priority;
	boolean isKarb;
	boolean replaced;
	public Resource(int x, int y, int p){
		this.x = x;
		this.y = y;
		this.priority = p;
		this.worker = -1;
	}
}