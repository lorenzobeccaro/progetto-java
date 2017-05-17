package ThreadPoolPrototype;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Simulator {
	
	private static Population population;
	private static PayOffsMatrix matrix;

	public static void main(String[] args) throws Exception {
		
		Map<Human,Integer> initState = new HashMap<Human,Integer>();
		
		int a = 15;
		int b = 20;
		int c = 3;
		
		Chromosome.mapTypeToGene(Gender.MALE, "M", true);
		Chromosome.mapTypeToGene(Gender.MALE, "A", false);
		Chromosome.mapTypeToGene(Gender.FEMALE, "P", true);
		Chromosome.mapTypeToGene(Gender.FEMALE, "S", false);
		
		Human.restTime = a;
		
		PayOffsMatrix m = new PayOffsMatrix();
		m.addFormula("P", "M", a-b/2-c, a-b/2-c);
		m.addFormula("S", "M", a-b/2, a-b/2);
		m.addFormula("S", "A", a-b, a);
		//m.addFormula("P", "A", 0, 0);
		
		setMatrix(m);
		
		System.out.println(m);
		
		
				
		Human M = new Human("M");
		Human A = new Human("A");
		Human P = new Human("P");
		Human S = new Human("S");
		
		initState.put(M, 10);
		initState.put(A, 10);
		initState.put(P, 10);
		initState.put(S, 10);
		
		Population pop = new Population();	
		setPopulation(pop);
		
		pop.observeData("P", "S");
		pop.observeData("M", "A");
		pop.setState(initState);
		System.out.println(pop);
		
		Thread.sleep(1000);
		//pop.stopSim();
		System.out.println(pop);
		while(!pop.isStable()) {
			try {
				//System.out.println(pop);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		System.out.println("Finished");
		
		
//		Thread.sleep(1500);
//		
//		pop.stopSim();
		

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
