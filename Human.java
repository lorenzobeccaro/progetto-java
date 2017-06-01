import java.util.*;

public class Human implements Runnable {

	private Chromosome chromosome;
	private Chromosome partnerChromosome;
	
	 
	
	//public static final int MAX_DATES = 10000;
	//public static final int MAX_CHILDREN = 10000;
	public static final int DATES_RANGE = 100;
	public static final double HAPPINESS_RANGE = 0.2;
	
	
	private int childCount = 0;
	private int dateCount = 0;
	private int points = 0;


	public Human(String type) {
		this.chromosome = new Chromosome(type);
	}
	
	public Human(Human h) {
		this.chromosome = h.chromosome;
	}
	
	public Human(Chromosome c) {
		this.chromosome = c;
	}
	
	public Human(Gender g) {
		this.chromosome = new Chromosome();
		this.chromosome.setGender(g);
	}

	@Override
	public synchronized void run() {
		Simulator.getPopulation().setAlive(this);
		while(!Simulator.getPopulation().isRunning() && !Thread.currentThread().isInterrupted());
		try {
			while(!isSad()) {
				
				if(this.chromosome.getGender() == Gender.FEMALE) {
					Hotel.bar.sit(this);
					wait();
					if(this.partnerChromosome != null && Simulator.getPopulation().isRunning())
						generate();
					else {
						this.dateCount++;
					}
				} else {
					Human partner = Hotel.bar.offerADrink();
					if(Simulator.getMatrix().areCompatible(getType(), partner.getType())) {
						dateWith(partner);
					}
				}
			}
		} catch (InterruptedException e) {
			//System.out.print(" "+this.getType());
			return;
		}
		die();
	}

	private synchronized void dateWith(Human partner) {
		if(isHappy() && partner.isHappy()) {
			inseminate(partner);
		}
		
		//System.out.println(this+" is dating w/ "+partner);
		PayOffsMatrix m = Simulator.getMatrix();
		this.addPoints(m.getPayOff(getType(), partner.getType()));
		partner.addPoints(m.getPayOff(partner.getType(), getType()));
		this.dateCount++;
		partner.dateCount++;
		partner.awake();
	}
	
	private void addPoints(int value) {
		this.points += value;
	}
	
	public double getHappiness() {
		return dateCount == 0 ? 0 : (double)this.points/this.dateCount;
	}

	private void die() {
		if(Simulator.getPopulation().isRunning())
			Simulator.getPopulation().removeHuman(this);
		//System.out.println(this);
		Simulator.getPopulation().removeAlive(this);
	}
	
	public synchronized boolean isHappy() {
		double threshold = Simulator.getPopulation().getThresholdForGender(this);
		return getHappiness() >= threshold+threshold*HAPPINESS_RANGE;
	}
	
	public synchronized boolean isSad() {
		double threshold = Simulator.getPopulation().getThreshold(this);
		double happiness = getHappiness();
		boolean result = false;
		if(happiness == 0) {
			return false;
		} else {
			result = happiness < threshold-threshold*HAPPINESS_RANGE;
			//return happiness <= threshold-threshold*HAPPINESS_RANGE;
		}
		//System.out.println(this+"isSad... h: "+getHappiness()+" t: "+(threshold-threshold*HAPPINESS_RANGE));
		return result;
	}

	private void inseminate(Human partner) {
		partner.getPregnant(this);
		this.childCount++;
	}
	
	private synchronized void getPregnant(Human partner) {
		this.partnerChromosome = partner.getChromosome();
		this.childCount++;
	}
	
	private synchronized void generate() {
		Human child = new Human(new Chromosome(partnerChromosome,chromosome));
		Simulator.getPopulation().addHuman(child);
		this.partnerChromosome = null;
	}

	public String getType() {
		return this.chromosome.getType();
	}
	
	public Gender getGender() {
		return this.chromosome.getGender();
	}
	
	public void setChromosome(Chromosome c) {
		this.chromosome = c;
	}
	
	public Chromosome getChromosome() {
		return this.chromosome;
	}
	
	@Override
	public String toString() {
		return this.chromosome.toString();
	}
	
	public Human copy() {
		return new Human(this);
	}
	
	private synchronized void awake() {
		notify();
	}

}
