import java.util.concurrent.TimeoutException;

public class Human implements Runnable {

	private Chromosome chromosome;
	private Chromosome partnerChromosome;
	
	 
	
	public static final int MAX_DATES = 100;
	
	private int childCount = 0;
	private int dateCount = 0;
	private int happiness = 0;

	public Human(String type) {
		this.chromosome = new Chromosome(type);
	}
	
	public Human(Human h) {
		this.chromosome = h.chromosome;
	}
	
	public Human(Chromosome c) {
		this.chromosome = c;
	}
	
	@Override
	public synchronized void run() {
		while(Simulator.getPopulation().isStopped());
		try {
			while(this.dateCount < MAX_DATES) {
				
				if(Thread.currentThread().isInterrupted())
					return;
				
				if(this.chromosome.getGender() == Gender.FEMALE) {
					Hotel.bar.sit(this);
					wait();
					if(this.partnerChromosome != null && !Simulator.getPopulation().isStopped())
						generate();
				} else {
					Human partner = Hotel.bar.offerADrink();
					if(Simulator.getMatrix().areCompatible(getType(), partner.getType())) {
						dateWith(partner);
					}
				}
			}
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
		die();
	}
	
	private synchronized void dateWith(Human partner) {
		if(isHappy() && partner.isHappy()) {
			if(!(this.getType() == "A" && partner.getType() == "P"))
				inseminate(partner);
		}
		
		if(partner.getType().equals("S")) {
			//System.out.println(partner);
		}
		
		//System.out.println(this+" is dating w/ "+partner);
		PayOffsMatrix m = Simulator.getMatrix();
		this.changeHappiness(m.getPayOff(getType(), partner.getType()));
		partner.changeHappiness(m.getPayOff(partner.getType(), getType()));
		this.dateCount++;
		partner.dateCount++;
		partner.awake();
	}
	
	private void changeHappiness(int value) {
		this.happiness += value;
	}
	
	public int getHappiness() {
		return this.happiness;
	}

	private void die() {
		if(!Simulator.getPopulation().isStopped())
			Simulator.getPopulation().removeHuman(this);
		//System.out.println(this);
	}
	
	public boolean isHappy() {
		return this.happiness >= Simulator.getPopulation().getAvgHappiness(getGender());
	}

	private void inseminate(Human partner) {
		partner.getPregnant(this.chromosome);
		this.childCount++;
	}
	
	private synchronized void getPregnant(Chromosome partner) {
		this.partnerChromosome = partner;
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
