
public class GenetareRV {
	public static double seed = 1111.0;
	public static double k = 16807;
	public static double m = 2147483647;
	
	public static double uniformRV(){
		
		double k = 16807.0;
		double m = 2.147483647e9;
		double randomVariable;
		
		seed = (seed * k) % m;		
		randomVariable = seed / m;
		
		return randomVariable;
	}
	
	public static double expRV(double lambda){
		double exp = 0.0;
		exp = ((double)(-1) / lambda) * Math.log(uniformRV());
		return exp;
	}
}
