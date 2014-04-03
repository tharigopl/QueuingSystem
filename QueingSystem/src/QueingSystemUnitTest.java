
public class QueingSystemUnitTest {
	public static void main(String[] args){
		testEventList();
	}
	
	public static void testEventList(){
		EventList eventList = new EventList();
		
		eventList.insert(GenerateRV.expRV(3), 0, "");
		eventList.insert(GenerateRV.expRV(3), 1, "");
		Event eve = eventList.head;
		
		EventList e = eventList;
	}
}
