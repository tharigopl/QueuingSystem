
public class HW1No3_4_5 {

	public static double seed = 1111;
	public static double k = 16807;
	public static double m = 2147483647;
	public static int lambda = 2;
	public static double[] arrVar = new double[10000];
	public static double[] arrR = new double[10000];
	public static double[] arrExpVar = new double[10000];
	public static double[] arrExpR = new double[10000];
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double s0 = seed;
		double rn;
		double newR = 0.0;
		double newSeed = 0.0;
		double[] interval = new double[10];
		
		for(int i = 0 ; i < 10000 ; i++){
			newSeed = calculateSeed(s0);
			//arrSn[i] = newSeed;			
			newR = calculateR(s0);
			arrR[i] = newR;
			arrVar[i] = newR * newR;
			s0 = newSeed;
			interval[(int) (newR*10)]++;
		}
		
		System.out.println(" ************* Uniformly distributed pseudorandom numbers using the multiplicative congruential method ****************");
		double mean = calculateMean(arrR);
		System.out.println(" The calculated mean "+mean);
		double variance = calculateVariance(arrVar) - (mean*mean);
		System.out.println(" The calculated variance "+ variance);
		
		for (int i=0; i < 10; i++){
			System.out.println("[" + (double) i/10 + "," + (double) (i+1)/10 + "]: "+interval[i]);			
		}
		
		
		// Exponential Random Variable
		double exp = 0.0;
		
		for(int i = 0 ; i < 10000 ; i++){
			newSeed = calculateSeed(s0);
			newR = calculateR(s0);	
			exp = ((double)(-1) / lambda) * Math.log(newR);			
			arrExpR[i] = exp;
			arrExpVar[i] = exp * exp;
			s0 = newSeed;
		}
		System.out.println();
		System.out.println(" ************* Exponentialy distributed pseudorandom numbers using the multiplicative congruential method ****************");
		double meanExp = calculateMean(arrExpR);
		System.out.println(" The calculated mean "+meanExp);
		double varianceExp = calculateVariance(arrExpVar) - (meanExp*meanExp);
		System.out.println(" The calculated variance "+ varianceExp);
		
		System.out.println("Theoritical Mean = " + (double)1/lambda);
		System.out.println("Theoritical variance = " + (double)1/(lambda*lambda));
		
		
		// Binary Communication Channel Simulation
		
		double bin = 0.0;
		int zero_Rec = 0;
		int zero_Trans = 0;
		int zero_Trans_Zero_Rec = 0;
		int zero_Trans_One_Rec = 0;
		int one_Trans = 0;
		int one_Rec = 0;
		int one_Trans_One_Rec = 0;
		int one_Trans_Zero_Rec = 0;
		int errorProb = 0;
		
		for(int i = 0 ; i < 10000 ; i++){
			newSeed = calculateSeed(s0);
			newR = calculateR(s0);	
			s0 = newSeed;		
			if(newR < 0.45){
				zero_Trans++;
				newSeed = calculateSeed(s0);
				newR = calculateR(s0);
				s0 = newSeed;
				
				if(newR < 0.85){
					zero_Rec++;
					zero_Trans_Zero_Rec++;
				}else{
					one_Rec++;
					zero_Trans_One_Rec++;
					errorProb++;
				}
			}else{
				one_Trans++;
				newSeed = calculateSeed(s0);
				newR = calculateR(s0);
				s0 = newSeed;
				if(newR < 0.95){
					one_Rec++;
					one_Trans_One_Rec++;
				}else{
					one_Trans_Zero_Rec++;
					zero_Rec++;
					errorProb++;
				}
			}			
			s0 = newSeed;
		}
		System.out.println();
		System.out.println(" *********************** Binary Channel Exp ******************************");
		System.out.println(" Prob( 0 Received ) = "+ (double)zero_Rec / 10000);
		System.out.println(" Prob( 1 Transmitted | 1 Received ) = "+ 
				(double)(one_Trans_One_Rec / (double)(one_Trans_One_Rec + zero_Trans_One_Rec)));
		System.out.println(" Probability of error = "+ (double)errorProb/10000);
		
	}
	
	public static double calculateMean(double[] input){
		double sum = 0.0;
		for(int i = 0 ; i < input.length ; i++){
			sum += input[i]; 
		}
		
		return sum/input.length;
	}
	
	public static double calculateVariance(double[] input){
		double sum = 0.0;
		for(int i = 0 ; i < input.length ; i++){
			sum += input[i];
		}
		
		
		return sum/input.length;
	}
	
	
	public static double calculateSeed(double seed){
		double calculatedSeed = 0.0;
		calculatedSeed = (seed * k) % m;
		//System.out.println("Calculated Seed "+calculatedSeed);
		return calculatedSeed;
	}
	
	public static double calculateR(double seed){
		return seed/m;		
	}
	
}
