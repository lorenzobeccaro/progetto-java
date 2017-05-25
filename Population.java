
import java.util.*;
import java.util.concurrent.*;

class Population {
	private boolean isStopped;
  
  	private Map<String,String> observingData = new HashMap<String,String>();
	private volatile List<SimulationState> states = Collections.synchronizedList(new LinkedList<SimulationState>());
	
	private volatile List<Human> humans = Collections.synchronizedList(new LinkedList<Human>());
	
	public volatile List<SubPopulation> threadPools = Collections.synchronizedList(new ArrayList<SubPopulation>());

	private int MAX_THREADS = 2000;
	private int MAX_STATES;
	private int STEPS;
	private SimulationState result;
	private volatile int changes = 0;
	
	public Population(int steps) {
		STEPS = steps;
		MAX_STATES = 1000/steps;
		System.out.println("Simulation running with "+steps+" steps.");
		System.out.println("Minimum states: "+MAX_STATES);
	}
	
	@Override
	public String toString() {
		return this.getState().toString();
	}

	public void observeData(String string, String string2) {
		this.getObservingData().put(string, string2);
	}

	public void setState(Map<Human, Integer> initState) {
		LinkedList<Human> list = new LinkedList<Human>();
		for(Human k : initState.keySet()) {
			for(int j=0;j<initState.get(k);j++) {
				Human newHuman = k.copy();
				list.add(newHuman);
			}
			threadPools.add(new SubPopulation(k.getType()));
		}
		Collections.shuffle(list);
		this.isStopped = true;
		for(Human h : list) {
			addHuman(h);
		}
		this.isStopped = false;
	}
	
	public synchronized SimulationState getState() {
		return new SimulationState(this.humans);
	}

	public void addHuman(Human h) {
		humans.add(h);
		change();
		String type = h.getType();
		for(SubPopulation p : threadPools) {
			if(p.getName().equals(type)) {
				try {
					p.execute(h);
				} catch (Exception e) {
					//e.printStackTrace();
				}
				return;
			}
		}
	}
	
	public synchronized void removeHuman(Human h) {
		String type = h.getType();
		for(SubPopulation p : threadPools) {
			if(p.getName() == type) {
				p.remove(h);
			}
		}
		this.humans.remove(h);
		change();
	}
	
	private synchronized void change() {
		this.changes++;
		if(changes % (STEPS/100) == 0) {
			System.out.print("*");
		}
		if(changes>STEPS) {
			update();
			changes = 0;
			System.out.println("0%"+new String(new char[94]).replace('\0', ' ')+"100%");
		}
	}
	
	private void update() {
		SimulationState current = saveState();
		//System.out.println(threadPools);
		for(SubPopulation p : this.threadPools) {
			//current.getFemaleAvgHappiness();
			p.setCorePoolSize((int)Math.round(current.getPercentages().get(p.getName())/100*MAX_THREADS));
		}
	}
	
	public int getAvgHappiness(Gender g) {
		if(states.size() == 0)
			return 10;
			
		SimulationState current = getCurrentState();
		//int avgHapp = (g == Gender.FEMALE ? current.getFemaleAvgHappiness() : current.getMaleAvgHappiness());
		int avgHapp = (current.getFemaleAvgHappiness()+current.getMaleAvgHappiness())/2;
		return avgHapp;
	}
	
	private SimulationState getCurrentState() {
		return this.states.get(0);
	}

	private boolean singleGender() {
		Queue<Human> snap = new LinkedList<Human>(humans);
		Human current = snap.poll();
		boolean result = true;
		while(snap.size()>0) {
			result = result && current.getGender().equals(snap.peek());
			current = snap.poll();
		}
		return result;
	}

	public synchronized boolean isStable() {
		
		if(states.size()<MAX_STATES)
			return false;
		
		Queue<SimulationState> queue = new LinkedList<SimulationState>(this.states);
		//System.out.println(queue);
		SimulationState lastState = null;
		if(states.size()>=1) {
			lastState = states.get(states.size()-1);
			this.result = lastState;
		}
		

		if(singleGender()) {
			return true;
		}
		
		
		while(queue.size()>1) {
			if(!queue.poll().isNear(queue.peek()))
				return false;
		}
		
		return true;
	}
	
	public synchronized SimulationState saveState() {
		SimulationState state = getState();
		this.states.add(0,state);
		while(states.size()>MAX_STATES) {
			this.states.remove(this.states.get(states.size()-1));
		}
		System.out.println();
		System.out.println(state);
		return state;
	}

	public SimulationState getResult() {
		return result;
	}
	
	public Map<String,String> getObservingData() {
		return observingData;
	}

	public void setObservingData(Map<String,String> observingData) {
		this.observingData = observingData;
	}
	
	public class SubPopulation extends ThreadPoolExecutor {
		private String name;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public SubPopulation(String name) {
		  	super(500, 10000, 1, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		  	setName(name);
		}
	}

	public synchronized void stop() {
		isStopped = true;
		for(SubPopulation p : threadPools) {
			p.shutdownNow();
		}
	}

	public boolean isStopped() {
		return isStopped;
	}
	
	public void genealogicalTree() {
		Human root = getRandomHuman();;
		//printBinaryTree(root.getChromosome(),0);
		TreePrinter.print(root.getChromosome());
	}

	private Human getRandomHuman() {
		return humans.get((int)(Math.random()*humans.size())-1);
	}
	
	public static void printBinaryTree(Chromosome root, int level){
	    if(root==null)
	         return;
	    printBinaryTree(root.getRight(), level+1);
	    if(level!=0){
	        for(int i=0;i<level-1;i++)
	            System.out.print("    |\t");
	            System.out.println("    |---"+root.toString());
	    }
	    else
	        System.out.println(root.toString());
	    printBinaryTree(root.getLeft(), level+1);
	} 
}