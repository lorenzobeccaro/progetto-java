
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Population extends ThreadPoolExecutor {
  private boolean isPaused;
  private ReentrantLock pauseLock = new ReentrantLock();
  private Condition unpaused = pauseLock.newCondition();
  
  private Map<String,String> observingData = new HashMap<String,String>();
	private volatile List<SimulationState> states = Collections.synchronizedList(new LinkedList<SimulationState>());
	
	private volatile List<Human> humans = Collections.synchronizedList(new LinkedList<Human>());

	private static final int MAX_STATES = 10;
	private static final int STEPS = 50;
	private SimulationState result;
	private volatile int changes = 0;
	
	@Override
	public String toString() {
		return this.getState().toString();
	}

  public Population() {
  	super(2000, 200000, 1, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
  }

  @Override
  protected void beforeExecute(Thread t, Runnable r) {
    super.beforeExecute(t, r);
    pauseLock.lock();
    try {
      while (isPaused) unpaused.await();
    } catch (InterruptedException ie) {
      t.interrupt();
    } finally {
      pauseLock.unlock();
    }
  }

  public void pause() {
    pauseLock.lock();
    try {
      isPaused = true;
    } finally {
      pauseLock.unlock();
    }
    System.out.println("PAUSED");
  }

  public void resume() {
    pauseLock.lock();
    try {
      isPaused = false;
      unpaused.signalAll();
    } finally {
      pauseLock.unlock();
    }
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
		}
		Collections.shuffle(list);
		for(Human h : list) {
			addHuman(h);
		}
	}
	
	public synchronized SimulationState getState() {
		return new SimulationState(this.humans);
	}

	public synchronized void addHuman(Human h) {
		try {
			this.execute(h);
			this.humans.add(h);
			this.changes++;
			System.out.print(".");
		} catch (Exception e) {
			// human rejected
			//System.out.println(h+" rejected");
		}
		if(changes>STEPS) {
			saveState();
			changes = 0;
		}
	}
	
	public void removeHuman(Human h) {
		this.remove(h);
		this.humans.remove(h);
	}

	public synchronized boolean isStable() {
		Queue<SimulationState> queue = new LinkedList<SimulationState>(this.states);
		//System.out.println(queue);
		SimulationState lastState = null;
		if(!states.isEmpty()) {
			lastState = states.get(states.size()-1);
		} else {
			return false;
		}
		while(queue.size()>1) {
			if(!queue.poll().isNear(queue.peek()))
				return false;
		}
		this.result = lastState;
		return true;
	}
	
	public synchronized void saveState() {
		SimulationState state = getState();
		this.states.add(state);
		while(states.size()>MAX_STATES) {
			this.states.remove(0);
		}
		System.out.println();
		System.out.println(state);
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
}