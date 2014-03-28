import java.util.Comparator;


public class Event implements Comparable<Event>, Comparator<Event>{
	public double time;
	public int type;
	public Event next;
	
	Event(double time, int type){
		this.time = time;
		this.type = type;
		this.next = null;
	}

	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		
		return 0;
	}

	@Override
	public int compare(Event o1, Event o2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
