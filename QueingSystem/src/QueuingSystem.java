import java.util.Scanner;


public class QueuingSystem {
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
	
	public static double[] rhoArray = {0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
	
	public static void main(String[] args) throws Exception{
		
		QueuingSystem queueingSystem = new QueuingSystem();
		queueingSystem.getInput();
		for(int i = 0 ; i < rhoArray.length; i++){
			queueingSystem.runSimulations(rhoArray[i]);
		}
		//queueingSystem.runSimulations(0.5);
	}	
	
	public void runSimulations(double rhoInput){
		// Calculate lambda from rho * m * mu
		
		double rho = rhoInput;
		lambda1 = rho * m * mu;
		EN = 0.0;
		noOfJobsInTheSystem = 0;
		//eventList = null;
		
		double lambdaAll = lambda1 + lambda2;
		
		eventList = new EventList();
				// Get the jobs arrived
				arrivalOfJobs();
				int departureGeneratedForArrival = 0;
				boolean arrivalIsBlocked = false;
				int noOfArrivalsBlocked = 0;
				int noOfServersBusy = 0;
				int noOfJobsInTheQueue = 0;
				int noOfArrivalJobsInTheSystem = 0;
				int noOfDepartureJobsInTheSystem = 0;
				int noOfUserJobsInTheSystem = 0;
				int noOfAdminJobsInTheSystem = 0;
				int noOfUserArrivalsBlocked = 0;
				int noOfAdminArrivalsBlocked = 0;
				int totalNoOfUserArrivals = 0;
				int totalNoOfAdminArrivals = 0;
				int totalNoOfArrivals = 0;
				noOfJobsInTheSystem = 0;
				noOfJobsDeparted=0;
				int count = 0;
				systemClock = 0.0;
				// Simulate
				while(noOfJobsDeparted < 1000000){					
					double previousClock = systemClock;
										
					Event currentEvent = eventList.getEvent();
					if(currentEvent == null){
						System.out.println("");
					}
					systemClock = currentEvent.time;
					
					switch(currentEvent.type){
						// Arrival Event
						case 0:	
							totalNoOfArrivals++;
							EN += noOfJobsInTheSystem * (systemClock - previousClock);
							String typeOfJob = currentEvent.jobType;
							if(typeOfJob.equalsIgnoreCase("user")){
								totalNoOfUserArrivals++;								
								if(noOfJobsInTheSystem >= l){
									noOfArrivalsBlocked++;
									noOfUserArrivalsBlocked++;
									eventList.insert(systemClock+GenerateRV.expRV(lambda2), 0, "user");
								}/*else if(noOfServersBusy == m && noOfJobsInTheSystem == K - m){
									noOfArrivalsBlocked++;								
								}*/
								else{
									
									noOfJobsInTheSystem++;									
									noOfUserJobsInTheSystem++;									
									
									//if(noOfJobsInTheSystem < K){
										eventList.insert(systemClock+GenerateRV.expRV(lambda2), 0, "user");									
									//}
									if (noOfJobsInTheSystem <= m) {
										eventList.insert(systemClock+GenerateRV.expRV(mu), 1, "departure");	
										noOfDepartureJobsInTheSystem++;
								    }		
								/*	
									if(noOfJobsInTheSystem <= K && noOfJobsInTheSystem > m){
										noOfJobsInTheQueue++;
									}*/
																					
								}
							}else{
								totalNoOfAdminArrivals++;
								if(noOfJobsInTheSystem >= K){
									noOfAdminArrivalsBlocked++;
									noOfArrivalsBlocked++;
									eventList.insert(systemClock+GenerateRV.expRV(lambda1), 0, "admin");									
								}/*else if(noOfServersBusy == m && noOfJobsInTheSystem == K - m){
									noOfArrivalsBlocked++;								
								}*/
								else{
									//totalNoOfArrivals++;
									noOfJobsInTheSystem++;
									noOfAdminJobsInTheSystem++;
									
									//if(noOfJobsInTheSystem < K){
										eventList.insert(systemClock+GenerateRV.expRV(lambda1), 0, "admin");									
									//}
									if (noOfJobsInTheSystem <= m) {
										eventList.insert(systemClock+GenerateRV.expRV(mu), 1, "departure");	
										noOfDepartureJobsInTheSystem++;
								    }												
								}
							}							
														
							break;
						//Departure Event
						case 1:
							EN += noOfJobsInTheSystem * (systemClock - previousClock);							
							// No of jobs departed from the system
							noOfJobsDeparted++;
							// Decrement the capacity of the system
							// if(	!(noOfJobsInTheSystem == 0) )
							noOfArrivalJobsInTheSystem--;
							noOfJobsInTheSystem--;
							//noOfServersBusy--;		
							
							// Create a departure event 
							//if(noOfJobsInTheSystem > 0 && noOfJobsInTheSystem < m){
							if(noOfJobsInTheSystem >= m){
								noOfJobsInTheQueue--;
								eventList.insert(systemClock+GenerateRV.expRV(mu), 1, "departure");								
							}	
							
							break;					
						}
						currentEvent = null;
				}
					
				// output simulation results for N, E[N]
				System.out.println(" RHO Value : "+rho);
				System.out.println( "Current number of jobs in system:  "+noOfJobsInTheSystem);
				System.out.println( "Expected number of jobs (simulation):  "+EN/systemClock);
				System.out.println( "Expected time a job spends in the system "+ EN/noOfJobsDeparted);
				System.out.println("User arrivals blocking probability:  "+ (double)noOfUserArrivalsBlocked / totalNoOfUserArrivals);
				System.out.println("Admin arrivals blocking probability:   "+ (double)noOfAdminArrivalsBlocked / totalNoOfAdminArrivals);
				System.out.println("Total blocking probability of the system:   "+ (double)noOfArrivalsBlocked / totalNoOfArrivals);
				
				
				
				// output derived value for E[N]
				
				lambdaAll = lambda1 + lambda2;
				
				
				double p0 = 0.0;				
				double p1 = 0.0;
				double p2 = 0.0; 
				double p3 = 0.0;
				double p4 = 0.0;
				EN = 0.0;
				double lambdaEff = 0.0;
				double ET = 0.0;
				double userJobsBlockedTheoretical = 0.0;
				double adminJobsBlockedTheoretical = 0.0;
				
				
				if(l == 1){
					p1 = lambdaAll / mu;
					p2 = (lambda1 * (lambdaAll))/(2 * Math.pow(mu, 2));
					p3 = (Math.pow(lambda1, 2)*(lambdaAll))/(2 * 2 * Math.pow(mu, 3));
					p4 = (Math.pow(lambda1, 3)*(lambdaAll))/(2 * 2 * 2 * Math.pow(mu, 4));
					
					p0 = 1 / (1 + p1 + p2 + p3 + p4);
					
					p1 = p1 * p0;
					p2 = p2 * p0;
					p3 = p3 * p0;
					p4 = p4 * p0;
					
					EN = p1 + (2 * p2) + (3 * p3) + (4 * p4);
					
					// lambdaEff = lambda(n) * p(n)
					
					lambdaEff = (lambdaAll * p0) + (lambda1 * p1) + (lambda1 * p2) + (lambda1 * p3); 
													
					ET = EN / lambdaEff;		
					
					// Calculate no of user arrivals blocked
					//userJobsBlockedTheoretical =  (lambda1 * p4) / lambdaEff;
					userJobsBlockedTheoretical = 1 - p0;
							
					// Calculate no of admin arrivals blocked
					adminJobsBlockedTheoretical = p4;
							
				}else if(l == 3){
					p1 = lambdaAll / mu;
					p2 = (Math.pow(lambdaAll,2))/(2 * Math.pow(mu, 2));
					p3 = (Math.pow(lambdaAll,3))/(2 * 2 * Math.pow(mu, 3));
					p4 = (Math.pow(lambdaAll, 3)*(lambda1))/(2 * 2 * 2 * Math.pow(mu, 4));
					
					p0 = 1 / (1 + p1 + p2 + p3 + p4);
					
					p1 = p1 * p0;
					p2 = p2 * p0;
					p3 = p3 * p0;
					p4 = p4 * p0;
					
					EN = p1 + (2 * p2) + (3 * p3) + (4 * p4);
					
					lambdaEff = (lambdaAll * p0) + (lambdaAll * p1) + (lambdaAll * p2) + (lambda1 * p3); 
					
					ET = EN / lambdaEff;
					
					// Calculate no of user arrivals blocked
					
					userJobsBlockedTheoretical =  1 - (p0 + p1 + p2);
					
					
					// Calculate no of admin arrivals blocked
					adminJobsBlockedTheoretical = p4;
				}
				
				System.out.println(" ----------------------------------------------------------------------------------------------------");
				
				System.out.println("Expected number of jobs (theoretical): "+EN);
				System.out.println("Expected time for jobs in the system (theoretical): "+ET);
				System.out.println("Blocking probability of user jobs (theoretical): "+userJobsBlockedTheoretical);
				System.out.println("Blocking probability of admin jobs (theoretical): "+adminJobsBlockedTheoretical);
				System.out.println(" ____________________________________________________________________________________________________");
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
	
	
	public void arrivalOfJobs(){	
		// Block administrative jobs if the current capacity of the system is equal to the total capacity
		if(noOfJobsInTheSystem < K){
			eventList.insert(GenerateRV.expRV(lambda1), 0, "admin");
			//noOfJobsInTheSystem++;
		}
		
		// Block the user jobs if the total no of jobs in the current system is >= the threshold given by the user 
		// and also if the if total no of jobs in the current system is = to the total capacity of the system i.e K
		// TODO: Check if the second part of the below condition is needed or not		
		if(noOfJobsInTheSystem < l && noOfJobsInTheSystem < K){
			eventList.insert(GenerateRV.expRV(lambda2), 0, "user");
			//noOfJobsInTheSystem++;
		}
	}
	
	public void departureOfJobs(double clock){		
		eventList.insert(clock + GenerateRV.expRV(mu), 1, "departure");	
		//noOfJobsInTheSystem++;
	}
}
