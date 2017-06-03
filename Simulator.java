
import java.util.*;



public class Simulator {
	
	private static Population population;
	private static PayOffsMatrix matrix;

	public static void main(String[] args) throws Exception {
		
		long initTime = System.currentTimeMillis();
		
		List<String> listArgs = Arrays.asList(args);
		int a,b,c;
		
		if(!listArgs.contains("-abc")) {
			System.out.println("Arguments missing.");
			return;
		} else {
			int index = listArgs.indexOf("-abc");
			a = Integer.parseInt(args[index+1]); // 15
			b = Integer.parseInt(args[index+2]); // 20
			c = Integer.parseInt(args[index+3]); // 3
		}
		
		Map<Human,Integer> initState = new HashMap<Human,Integer>();
		
		Chromosome.mapTypeToGene(Gender.MALE, "M", true);
		Chromosome.mapTypeToGene(Gender.MALE, "A", false);
		Chromosome.mapTypeToGene(Gender.FEMALE, "P", false);
		Chromosome.mapTypeToGene(Gender.FEMALE, "S", true);
		
		PayOffsMatrix m = new PayOffsMatrix();
		m.addFormula("P", "M", a-b/2-c, a-b/2-c);
		m.addFormula("S", "M", a-b/2, a-b/2);
		m.addFormula("S", "A", a-b, a);
		m.addFormula("P", "A", 0, 0);
		
		setMatrix(m);
		
		System.out.println(m);
				
		Human M = new Human("M");
		Human A = new Human("A");
		Human P = new Human("P");
		Human S = new Human("S");
		
		initState.put(M, 25);
		initState.put(A, 25);
		initState.put(P, 25);
		initState.put(S, 25);
		
		Population pop = new Population();	
		setPopulation(pop);
		
		pop.observeData("P", "S");
		pop.observeData("M", "A");
		pop.setState(initState);
		//System.out.println(pop);
		while(!pop.isStable()) {
			
			
			//System.out.println(Hotel.bar.size());
			//System.out.println("NUMERO POOL: "+pop.threadPools.size());
		}
		pop.stop();
		Thread.sleep(1000);
		System.out.println("DONE");
		System.out.println("---RESULT---" + pop.getResult());
		
		if(listArgs.contains("-g"))
			pop.genealogicalTree();
		System.out.println("Exiting...");
		long millis = System.currentTimeMillis()-initTime;
		long seconds = (millis / 1000) % 60;
		long minutes = (millis / (1000 * 60)) % 60;

		String time = String.format("%02d:%02d", minutes, seconds);
		System.out.println("Simulation time: "+time);
	}

	public static Population getPopulation() {
		return population;
	}

	public static void setPopulation(Population population) {
		Simulator.population = population;
	}

	public static PayOffsMatrix getMatrix() {
		return matrix;
	}

	public static void setMatrix(PayOffsMatrix matrix) {
		Simulator.matrix = matrix;
	}

}
