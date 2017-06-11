import java.util.*;

public class Human implements Runnable {

	private Chromosome chromosome;
	private Chromosome partnerChromosome;
	
	 
	private static int MIN_CHILDREN = 2;
	private static int MAX_DATES = 5;
	
	private int dateCount = 0;
	private int childrenCount = 0;

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
		
		try {
			while(!isSad()) {
				if(this.chromosome.getGender() == Gender.FEMALE) {
					Hotel.bar.sit(this);
					wait(1);
					if(this.partnerChromosome != null && Simulator.getPopulation().isRunning())
						generate();
					else {
						Hotel.bar.standUp(this);
						dateCount++;
					}
				} else {
					Human partner = Hotel.bar.offerADrink();
					if(Simulator.getMatrix().areCompatible(getType(), partner.getType())) {
						dateWith(partner);
					}
				}
				Simulator.getPopulation().checkStatus();
			}
		} catch (InterruptedException e) {
			return;
		}
		die();
	}

	private synchronized void dateWith(Human partner) {
		if(isHappy() && partner.isHappy()) {
			inseminate(partner);
		}
		partner.awake();
		this.dateCount++;
		partner.dateCount++;
	}
	
	public double getHappiness() {
		return Simulator.getPopulation().getHappiness(this);
	}

	private void die() {
		if(Simulator.getPopulation().isRunning())
			Simulator.getPopulation().removeHuman(this);
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
		if(happiness > rivalHappiness) {
			return true;
		} else {
			return false;
		}
	}
	
	public synchronized boolean isSad() {		
		String rival = getRival();
		double rivalHappiness = Simulator.getPopulation().getHappiness(rival);
		double happiness = getHappiness();
		if(happiness <= rivalHappiness) {
			if(getGender() == Gender.MALE)
				return Math.random() < (dateCount*3/MAX_DATES + childrenCount/MIN_CHILDREN)/4;
			else 
				return Math.random() < (dateCount/MAX_DATES + childrenCount*3/MIN_CHILDREN)/4;
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
