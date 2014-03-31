
import java.util.Scanner;


public class MM1kSystem {

	public static enum eventType {ARR, DEP};
	// Total no of jobs in the system i.e the capacity of the system = K
	public static Integer K;
	// TODO: Check and remove if we need this variable or not
	// public static Integer totalCapacityOfTheSystem = 0;
		
	public Integer noOfJobsInTheSystem = 0;
	public static EventList eventList = new EventList();
	
	// Administrative jobs rate = lambda1 - Poisson Process
	public double lambda1 = 0;
	// User jobs rate = lambda2 - Poisson Process		
	public double lambda2 = 0;
	// Processing time for the jobs at the rate of mu
	public double mu = 0;
	// Threshold limit for user jobs = l
	public Integer l = 0;
	// Total no of processors - servers = m
	public Integer m = 0;
	private double systemClock = 0.0;
	
	public double EN = 0.0;
	public Integer noOfJobsDeparted = 0;
	
	public double[] rho = {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MM1kSystem queueingSystem = new MM1kSystem();
			
		queueingSystem.runSimulations();
	}
	
	public void runSimulations(){
		// Calculate lambda from rho * m * mu
				double lambda = 0.4;
				mu = 1.0;
				K = 6;
				
				// Get the jobs arrived
				eventList.insert(GenerateRV.expRV(lambda), 0);
				
				int count = 0;
				// Simulate
				while(noOfJobsDeparted < 10000){					
					Event currentEvent = eventList.getEvent();
					double previousClock = systemClock;
					
					if(currentEvent == null){
						System.out.println("");
					}
					systemClock = currentEvent.time;
					
					switch(currentEvent.type){
						// Arrival Event
						case 0: 
							EN += noOfJobsInTheSystem * (systemClock - previousClock);
							noOfJobsInTheSystem++;
							
							if(noOfJobsInTheSystem < K){
								eventList.insert(systemClock+GenerateRV.expRV(lambda), 0);
							}
							
							if (noOfJobsInTheSystem == 1) {
								eventList.insert(systemClock+GenerateRV.expRV(mu), 1);
						    }
							
							
							break;
						//Departure Event
						case 1:
							EN += noOfJobsInTheSystem * (systemClock - previousClock);							
							// No of jobs departed from the system
							noOfJobsDeparted++;
							// Decrement the capacity of the system
							noOfJobsInTheSystem--;
							
							// Create a departure event 
							if(noOfJobsInTheSystem > 0){
								eventList.insert(systemClock+GenerateRV.expRV(mu), 1);
							}							
							
							if(noOfJobsInTheSystem == K-1){
								eventList.insert(systemClock+GenerateRV.expRV(lambda), 0);
							}
							
							break;					
					}
					currentEvent = null;
				}
					
				// output simulation results for N, E[N]
				System.out.println( "Current number of jobs in system: "+noOfJobsInTheSystem);
				System.out.println( "Expected number of jobs (simulation): "+EN/systemClock);
				
				// output derived value for E[N]
				double rho = lambda/mu; 
				System.out.println("Expected number of jobs (analysis): "+rho/(1-rho));
	}
	
	public void getInput(){		
		Scanner scanner = new Scanner(System.in);		
		System.out.println("Enter total no of jobs in the system  [K]");
		K = Integer.parseInt(scanner.next().toString());
		
		System.out.println("Enter Threshold limit for user jobs  [l]");
		l = Integer.parseInt(scanner.next().toString());
		
		System.out.println("Enter  User jobs rate Poisson Process [lambda2]");
		lambda2 = Integer.parseInt(scanner.next().toString());
		
		System.out.println("Enter Processing time for the jobs at the rate of [mu]");
		mu = Integer.parseInt(scanner.next().toString());
		
		System.out.println("Enter total no of processeors in the system  [m]");
		m = Integer.parseInt(scanner.next().toString());
		
	}

}
