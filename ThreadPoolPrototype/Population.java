package ThreadPoolPrototype;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Population extends ThreadPoolExecutor {
  private boolean isPaused;
  private ReentrantLock pauseLock = new ReentrantLock();
  private Condition unpaused = pauseLock.newCondition();
  
  private Map<String,String> observingData = new HashMap<String,String>();
	private LinkedList<SimulationState> states = new LinkedList<SimulationState>();
	
	private volatile List<Human> humans = Collections.synchronizedList(new LinkedList<Human>());

	@Override
	public String toString() {
		return this.getState().toString();
	}

  public Population() {
  	super(5000, 1000000, 100, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
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
		for(Human k : initState.keySet()) {
			LinkedList<Human> list = new LinkedList<Human>();
			for(int j=0;j<initState.get(k);j++) {
				Human newHuman = k.copy();
				list.add(newHuman);
			}
			for(Human h : list) {
				this.addHuman(h);
			}
		}
		
	}
	
	public synchronized SimulationState getState() {
		return new SimulationState(this.humans);
	}

	public void addHuman(Human h) {
		this.execute(h);
		this.humans.add(h);
	}
	
	public void removeHuman(Human h) {
		this.remove(h);
		this.humans.remove(h);
	}

	public boolean isStable() {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<String,String> getObservingData() {
		return observingData;
	}

	public void setObservingData(Map<String,String> observingData) {
		this.observingData = observingData;
	}
}