
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;


public class MMckSystem {

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
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		MMckSystem queueingSystem = new MMckSystem();
			
		queueingSystem.runSimulations();
	}
	
	public void runSimulations() throws Exception{
		// Calculate lambda from rho * m * mu
				/*double lambda = 20;
				mu = 5.0;
				K = 10;
				m = 3;*/
				double lambda = 5;
				mu = 5.0;
				K = 3;
				m = 2;
							
				// Get the jobs arrived
				eventList.insert(GenerateRV.expRV(lambda), 0);
								
				boolean arrivalIsBlocked = false;
				int noOfArrivalsBlocked = 0;
				int noOfServersBusy = 0;
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
							if(noOfJobsInTheSystem == K || arrivalIsBlocked){
								noOfArrivalsBlocked++;
							}
							else{
								noOfJobsInTheSystem++;
							
								if(noOfJobsInTheSystem < K){
									eventList.insert(systemClock+GenerateRV.expRV(lambda), 0);
								}
								
								if (noOfJobsInTheSystem < m) {
									eventList.insert(systemClock+GenerateRV.expRV(mu), 1);									
							    }
							}
														
							break;
						//Departure Event
						case 1:
							EN += noOfJobsInTheSystem * (systemClock - previousClock);							
							// No of jobs departed from the system
							noOfJobsDeparted++;
							// Decrement the capacity of the system
							if(	!(noOfJobsInTheSystem == 0) )
								noOfJobsInTheSystem--;
							/*if(noOfJobsInTheSystem < 0){
								eventList.insert(systemClock+GenerateRV.expRV(lambda), 0);
								noOfJobsInTheSystem++;
							}*/
														
							// Create a departure event 
							if(noOfJobsInTheSystem > 0 ){
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
				
				/*lambda = 20;
				mu = 5;
				m = 3;
				K = 10;*/
				double rho = lambda/mu; 
				ArrayList<Double> pValues = new ArrayList<Double>();
				
				double p0 = 0.0;			
				double tempToCalP0 = 0.0;
				double temp1ToCalP0 = 0.0;
				double calcEM1 = 0.0;
				double calcEM2 = 0.0;
				
							
				if(rho/m != 1){
					for(int n = 0 ; n <= m-1; n++ ){
						tempToCalP0 += ((double) Math.pow(rho, n)) / (double)factorial(n);
					}
					
					temp1ToCalP0 = (Math.pow(rho, m) * (1-Math.pow((double)(rho/m), K-m+1))) / ((double)factorial(m) * (1-(rho/m)));
					
					p0 = 1/(temp1ToCalP0 + tempToCalP0);
					 
					
				}/*else{
					for(int n = 0 ; n <= m-1; n++ ){
						tempToCalP0 += ((double) Math.pow(rho, n)) / (double)factorial(n);
					}
					temp1ToCalP0 = Math.pow(rho, m) / (double)factorial(m) * (K-m+1);
					p0 = 1/(temp1ToCalP0 + tempToCalP0);
				}*/
				double EM = 0.0;
				
				double result = 0.0;
				for(int i = 0 ; i <= K; i++){
					if(i == 0)
						pValues.add(p0);
					else if(i<m){
						result = (Math.pow(rho, i) * p0)/(factorial(i));
						pValues.add(result);
					}else{
						result = (Math.pow(rho, i) * p0)/(factorial(m) * Math.pow(m, i-m));
						pValues.add(result);
					}
				}
				EN = 0.0;		
				for(int i = 0 ; i < pValues.size(); i++){
					EN += (i * pValues.get(i));
				}							
				System.out.println("Expected number of jobs (analysis): "+EN);
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
	
	public static int factorial(int n) {
	       int result = 1;
	       for (int i = 1; i <= n; i++) {
	           result = result * i;
	       }
	       return result;
	   }

}
