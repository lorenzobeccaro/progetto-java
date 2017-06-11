
import java.util.*;




public class Simulator {
	
	private static Population population;
	private static PayOffsMatrix matrix;
	public static boolean verbose = false;

	public static void main(String[] args) throws Exception {
		
		long initTime = System.currentTimeMillis();
		
		List<String> listArgs = Arrays.asList(args);
		int a,b,c;
		
		if(listArgs.contains("-h") || listArgs.contains("--help")) {
			String help = "Uso: Simulator [-opzioni]\n";
			help += "dove le opzioni sono:\n";
			help += "\t-abc\t\ta seguire i valori interi di a, b e c\n";
			help += "\t\t\tse omesso i valori saranno quelli di Dawkins\n";
			help += "\t-m\t\tstampa i payoff della tabella MAPS, la condizione di stabilità e termina\n";
			help += "\t-g\t\tstampa alla fine della simulazione un albero genealogico\n";
			help += "\t-h | --help\tstampa questo messaggio\n";
			help += "\t-p\t\timposta la popolazione iniziale con le percentuali in input\n";
			help += "\t\t\trispettivamente M, A, P e S.\n";
			help += "\t--max-threads\timposta il numero massimo di thread attivi nello stesso momento";
			help += "\t--humans\timposta il numero iniziale di individui per la simulazione";
			System.out.println(help);
			return;
		}
		
		if(listArgs.contains("--verbose")) {
			verbose = true;
		}
		
		if(!listArgs.contains("-abc")) {
			a = 15;
			b = 20;
			c = 3;
		} else {
			int index = listArgs.indexOf("-abc");
			try {
				a = Integer.parseInt(args[index+1]); // 15
				b = Integer.parseInt(args[index+2]); // 20
				c = Integer.parseInt(args[index+3]); // 3
				if(a-b/2-c <= 0 || !((c < a) && (a < b)) || c==0)
					throw new Exception();
					
			} catch (Exception e) {
				System.out.println("Invalid input for -abc.");
				return;
			} 
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
				
		Human M = new Human("M");
		Human A = new Human("A");
		Human P = new Human("P");
		Human S = new Human("S");
		
		int nm,na,np,ns;;
		nm = na = np = ns = 25;
		
		if(listArgs.contains("-p")) {
			int index = listArgs.indexOf("-p");
			try {
			nm = Integer.parseInt(args[index+1]);
			na = Integer.parseInt(args[index+2]);
			np = Integer.parseInt(args[index+3]);
			ns = Integer.parseInt(args[index+4]);
			if(nm+na+np+ns != 100) {
				System.out.println("Invalid input with -p");
				return;
			}
			} catch (Exception e) {
				System.out.println("Expecting 4 integers after -p.\nRespectively percentages for M, A, P, S.");
				return;
			} 
		}
		
		
		
		
		
		if(listArgs.contains("-m")) {
			System.out.println(m);
			getStableValues(m);
			return;
		}
		int multiplier = 1;
		initState.put(M, nm*multiplier);
		initState.put(A, na*multiplier);
		initState.put(P, np*multiplier);
		initState.put(S, ns*multiplier);
		
		Population pop = new Population();	
		if(listArgs.contains("-s")) {
			System.out.println("Simulation running with "+pop.STEPS+" steps.");
			System.out.println("Minimum states: "+pop.MAX_STATES);
			return;
		}
		setPopulation(pop);
		
		if(listArgs.contains("--max-threads")) {
			try {
				int i = listArgs.indexOf("--max-threads");
				pop.MAX_THREADS = Integer.parseInt(args[i+1]);
			} catch(Exception e) {
				
			}
		}
		if(listArgs.contains("--humans")) {
			try {
				int i = listArgs.indexOf("--humans");
				pop.INITIAL_HUMANS = Integer.parseInt(args[i+1]);
			} catch(Exception e) {

			}
		}
		
		pop.observeData("P", "S");
		pop.observeData("M", "A");
		pop.setState(initState);
		//System.out.println(pop);
		pop.waitResult();
		Thread.sleep(1000);
		System.out.println("DONE");
		System.out.println("---RESULT---" + pop.getResult());
		
		if(listArgs.contains("-g")) {
			boolean pretty = false;
			int generations = 4;
			try {
				int i = listArgs.indexOf("-g");
				generations = Integer.parseInt(args[i+1]);
				pretty = Boolean.parseBoolean(args[i+2]);
			} catch(Exception e) {

			}
			pop.genealogicalTree(pretty,generations);
		}
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
	
	public static void getStableValues(PayOffsMatrix m) {
		double M = (m.getPayOff("S","A") - m.getPayOff("P","A"));
		double A = (m.getPayOff("P","M") - m.getPayOff("S","M"));
		double P = (m.getPayOff("A","S") - m.getPayOff("M","S"));
		double S = (m.getPayOff("M","P") - m.getPayOff("A","P"));
		System.out.println("M/M+A: "+(M/(M+A)));
		System.out.println("P/P+S: "+(P/(P+S)));
	}

}
