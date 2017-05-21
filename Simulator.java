
import java.util.*;

public class Simulator {
	
	private static Population population;
	private static PayOffsMatrix matrix;

	public static void main(String[] args) throws Exception {
		
		Map<Human,Integer> initState = new HashMap<Human,Integer>();
		
		int a = 15;
		int b = 20;
		int c = 3;
		
		Chromosome.mapTypeToGene(Gender.MALE, "M", false);
		Chromosome.mapTypeToGene(Gender.MALE, "A", true);
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
		
		initState.put(M, 100);
		initState.put(A, 100);
		initState.put(P, 100);
		initState.put(S, 100);
		
		Population pop = new Population();	
		setPopulation(pop);
		
		pop.observeData("P", "S");
		pop.observeData("M", "A");
		pop.setState(initState);
		//System.out.println(pop);
		while(!pop.isStable()) {
			Thread.sleep(2);
			//System.out.println(pop);
			//pop.saveState();
		}
		pop.shutdownNow();
		while(!pop.isTerminated()) {
			//System.out.println(pop);
			Thread.sleep(100);
		}
		System.out.println("DONE");
		System.out.println("---RESULT---" + pop.getResult());
		

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
