import java.util.Comparator;


public class EventList {
	public Event head;
	public int event_count;
	
	public EventList(){		
		event_count = 0;
		head = null;
	}
	
	public void insert(double time, int type, String jobType){
		event_count++;
		Event event = new Event(time, type, jobType);
		if(head == null){
			head = event;
			event.next = null;
		}else if(head.time >= event.time){
			event.next = head;
			head = event;
		}else{
			Event current = head;
			while( current.next != null){
				if(current.next.time < event.time){
					//event.next = head.next
					current = current.next;
				}else{
					break;
				}
			}
			event.next = current.next;
			current.next = event;
		}
		
	}
	
	public Event getEvent(){		 
		if(event_count == 0){
			return null;
		}else{
			event_count--;
			Event event = null;
			event = head;
			head = head.next;
			event.next = null;
			return event;
		}
	}
	
	public void clear(){		
		head = null;
		event_count = 0;
	}
	
	public Event removeEvent(int type){
		if(event_count == 0 || head == null){
			return null;
		}else{
			Event event = null;
			Event prevEvent = null;
			event = head;
			while(event != null){
				if(event.type == type){
					if(prevEvent == null){
						head = event.next;
						event.next = null;
						return event;
					}else{
						prevEvent.next = event.next;
						event.next = null;
						return event;						
					}					
				}
				else{
					prevEvent = event;
					event = event.next;
				}
			}
			return null;
		}
	}
}
