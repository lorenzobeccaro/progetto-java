import java.util.*;

public class Human implements Runnable {

	private Chromosome chromosome;
	private Chromosome partnerChromosome;
	
	 
	
	public static final int MAX_DATES = 100000;
	public static final int MIN_CHILDREN = 1;
	//public static final int DATES_RANGE = 100;
	//public static final double HAPPINESS_RANGE = 0;
	
	
	private int childrenCount = 0;
	private int dateCount = 0;


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
			while((childrenCount<MIN_CHILDREN) || !isSad()) {
				
				if(this.chromosome.getGender() == Gender.FEMALE) {
					Hotel.bar.sit(this);
					wait(10);
					if(this.partnerChromosome != null && Simulator.getPopulation().isRunning())
						generate();
					else
						Hotel.bar.standUp(this);
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
		this.dateCount++;
		partner.dateCount++;
		partner.awake();
	}
	
	public double getHappiness() {
		return Simulator.getPopulation().getHappiness(this);
	}

	private void die() {
		if(Simulator.getPopulation().isRunning())
			Simulator.getPopulation().removeHuman(this);
		//System.out.println(this);
		Simulator.getPopulation().removeAlive(this);
	}
	
	public synchronized String getRival() {
		List<String> list = Chromosome.getTypesByGender(getGender());
		String rival = "\0";
		for(String t : list) {
			if(!t.equals(getType()))
				rival = t;
		}
		return rival;
	}
	
	public synchronized boolean isHappy() {
		String rival = getRival();
		double rivalHappiness = Simulator.getPopulation().getHappiness(rival);
		double happiness = getHappiness();
		return happiness > rivalHappiness;
	}
	
	public synchronized boolean isSad() {
		String rival = getRival();
		double rivalHappiness = Simulator.getPopulation().getHappiness(rival);
		double happiness = getHappiness();
		if(happiness < rivalHappiness) {
			return Math.random() < Math.min(1, 1 - happiness/rivalHappiness);
		} else
			return false;
	}

	private void inseminate(Human partner) {
		partner.getPregnant(this);
		this.childrenCount++;
	}
	
	private synchronized void getPregnant(Human partner) {
		this.partnerChromosome = partner.getChromosome();
		this.childrenCount++;
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
