
import java.util.*;
import java.util.concurrent.*;

class Population {
	private volatile boolean running = false;
  
  private Map<String,String> observingData = new HashMap<String,String>();
	private volatile List<SimulationState> states = Collections.synchronizedList(new LinkedList<SimulationState>());
	
	private volatile List<Human> humans = Collections.synchronizedList(new LinkedList<Human>());
	public volatile List<Human> alive = Collections.synchronizedList(new LinkedList<Human>());
	
	public volatile List<SubPopulation> threadPools = Collections.synchronizedList(new ArrayList<SubPopulation>());

	private volatile long lastChange;
	
	private SimulationState result = null;
	
	public int MAX_THREADS = 8000;
	public final int MAX_STATES;
	public final int STEPS;
	public int INITIAL_HUMANS = 200;

	private volatile int changes = 0;
	
	public Population() {
		STEPS = 100;
		MAX_STATES = 4000/STEPS;
		
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
			int number = initState.get(k)*INITIAL_HUMANS/100;
			for(int j=0;j<number;j++) {
				Human newHuman = k.copy();
				list.add(newHuman);
			}
			threadPools.add(new SubPopulation(k.getType()));
		}
		Collections.sort(threadPools);
		Collections.shuffle(list);
		//System.out.println("0%"+new String(new char[95]).replace('\0', ' ')+"100%");
		for(Human h : list) {
			addHuman(h);
		}
		running = true;
		update();
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
		
		if(!isRunning())
			return;
		
		lastChange = System.currentTimeMillis();
		this.changes++;
		if((changes % ((double)STEPS/100)) == 0) {
			if(Simulator.verbose)
				System.out.print("*");
		}
		if(changes>STEPS) {
			update();
			changes = 0;
			if(Simulator.verbose)
				System.out.println("0%"+new String(new char[95]).replace('\0', ' ')+"100%");
		}
		if(isStable())
			notify();
	}
	
	public synchronized void checkStatus() {
		if(System.currentTimeMillis()-lastChange > 100) {
			change();
			if(isStable())
				notify();
		}
	}
	
	private void update() {
		if(!isRunning())
			return;
		SimulationState current = saveState();
		//System.out.println(threadPools);
		for(SubPopulation p : this.threadPools) {
			//current.getFemaleAvgHappiness();
			if(current.getPercentages().containsKey(p.getName()))
					p.setCorePoolSize((int)Math.round(current.getPercentages().get(p.getName())*MAX_THREADS));
		}
	}
	
	public synchronized void waitResult() {
		try {
			wait();
		} catch(Exception e) {
			return;
		}
		this.stop();
	}
	
	public synchronized double getThresholdForGender(Human h) {
		if(states.size() == 0)
			return 0;
			
		SimulationState current = getCurrentState();
		return getThresholdByType(h.getType(),current, true);
	}
	
	public synchronized double getThresholdForGender(Gender g) {
		if(states.size() == 0)
			return 0;
			
		String type = Chromosome.getTypesByGender(g).get(0);
		SimulationState current = getCurrentState();
		return getThresholdByType(type, current, true);
	}
	
	public synchronized double getThresholdForGender(Human h, SimulationState s) {
		return getThresholdByType(h.getType(),s,true); 
	}
	
	public synchronized double getThresholdForGender(Gender g, SimulationState s) {
		String type = Chromosome.getTypesByGender(g).get(0);
		return getThresholdByType(type,s,true); 
	}
	
	public synchronized double getThreshold(Human h) {
		if(states.size() == 0)
			return 0;
			
		SimulationState current = getCurrentState();
		return getThresholdByType(h.getType(),current, false);
	}
	
	private synchronized double getThresholdByType(String type, SimulationState s, boolean forGender) {
		if(states.size() == 0)
			return 0;
			
		SimulationState current = s;
		List<String> types;
		if(forGender) {
			Gender g = Chromosome.getGenderByType(type);
			types = Chromosome.getTypesByGender(g);
		} else {
			types = new LinkedList<String>();
			types.add(type);
		}
		
		double happiness = 0;
		double weight = 0;
		for(String t : types) {
			if(!current.getPercentages().containsKey(t))
				continue;
			happiness += current.getHappiness(t)*current.getPercentages().get(t);
			weight += current.getPercentages().get(t);
		}
		happiness = happiness/weight;
		return happiness;
		
		
	}
	
	public SimulationState getCurrentState() {
		if(states.isEmpty())
			return null;
		return this.states.get(0);
	}

	private boolean singleGender() {
		Queue<Human> snap = new LinkedList<Human>(humans);
		Human current = snap.poll();
		boolean result = true;
		while(snap.size()>0) {
			result = result && current.getGender().equals(snap.peek().getGender());
			current = snap.poll();
		}
		return result;
	}

	public synchronized boolean isStable() {
		
		if(this.humans.size()==0 || singleGender())
			return true;
		
		
		SimulationState s = getState();
		List<Double> maleHappiness = new ArrayList<Double>();
		List<Double> femaleHappiness = new ArrayList<Double>();
		for(String t : s.getPopulation().keySet()) {
			if(Chromosome.getGenderByType(t) == Gender.MALE)
				maleHappiness.add(s.getHappiness(t));
			else
				femaleHappiness.add(s.getHappiness(t));
		}
		
		double error = 0.05;
		if(almostEqual(maleHappiness, error) && almostEqual(femaleHappiness, error)) {
			this.result = s;
			return true;
		} else {
			return false;
		}
	}
	
	private boolean almostEqual(List<Double> list, double eps){
		boolean result = true;
		if(list.size()<2)
			return true;
		for(int i=0; i<list.size()-1; i++) {
			result = result && Math.abs(list.get(i)-list.get(i+1))<eps;
		}
		return result;
	}
	
	public synchronized SimulationState saveState() {
		SimulationState state = getState();
		this.states.add(0,state);
		while(states.size()>MAX_STATES) {
			this.states.remove(this.states.get(states.size()-1));
		}
		if(Simulator.verbose) {
			System.out.println();
			System.out.println(state);
		}
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
	
	public class SubPopulation extends ThreadPoolExecutor implements Comparable<SubPopulation> {
		private String name;
		
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public SubPopulation(String name) {
		  	super(0, 10000, 1, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		  	setName(name);
		}

		@Override
		public int compareTo(SubPopulation s) {
			return this.getName().compareTo(s.getName());
		}
	}

	public synchronized void stop() {
		running = false;
		for(SubPopulation p : threadPools) {
			p.shutdownNow();
			p.getQueue().clear();
		}
	}

	public boolean isRunning() {
		return running;
	}
	
	public void genealogicalTree(boolean pretty,int generations) {
		Human root = getRandomHuman();
		if(!pretty)
			printBinaryTree(root.getChromosome(),0,generations);
		else
			if(root != null)
				TreePrinter.print(root.getChromosome(),generations);
	}

	private Human getRandomHuman() {
		if(humans.isEmpty())
			return null;
		return humans.get((int)(Math.random()*humans.size())-1);
	}

	public static void printBinaryTree(Chromosome root, int level,int maxlevel){
	    if(root==null || level>maxlevel)
	         return;
	    printBinaryTree(root.getRight(), level+1,maxlevel);
	    if(level!=0){
	        for(int i=0;i<level-1;i++)
	            System.out.print("    |\t");
	        System.out.println("    |---"+root.toString());
	    }
	    else
	        System.out.println(root.toString());
	    printBinaryTree(root.getLeft(), level+1,maxlevel);
	}

	public int getTotalThreads() {
		int n = 0;
		for(SubPopulation s : threadPools) {
			n += s.getActiveCount();
		}
		return n;
	}

	public void setAlive(Human human) {
		alive.add(human);		
	}
	
	public void removeAlive(Human human) {
		alive.remove(human);
	}
	
	public double getHappiness(String type) {
		if(states.size() == 0)
			return 0;
			
		SimulationState current = getState();
		return current.getHappiness(type);
	}

	public double getHappiness(Human human) {
		return getHappiness(human.getType());
	}
}
