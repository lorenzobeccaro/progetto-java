package ThreadPoolPrototype;

public class Human implements Runnable {

	private Chromosome chromosome;
	private Chromosome partnerChromosome;
	
	public static final int MAX_CHILDREN = 3;
	public static final int LIFE_SPAN = 10;
	public static final int TIME_SPEED = 10;
	
	public static int restTime = 0;
	
	private int childCount = 0;

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
		try {
			for(int i=0; i<LIFE_SPAN && this.childCount < MAX_CHILDREN; i++) {
				if(this.chromosome.getGender() == Gender.FEMALE) {
					Hotel.bar.sit(this);
					wait();
					generate();
				} else {
					Human partner = Hotel.bar.offerADrink();
					if(Simulator.getMatrix().areCompatible(getType(), partner.getType()))
						inseminate(partner);
				}
			}
		} catch (InterruptedException e) {
			
		}
		die();
	}
	
	private void die() {
		Simulator.getPopulation().removeHuman(this);
	}

	private void inseminate(Human female) throws InterruptedException {
		female.getPregnant(this.chromosome);
		this.childCount++;
		Thread.sleep((restTime - Simulator.getMatrix().getPayOff(getType(), female.getType()))*TIME_SPEED);
	}
	
	private synchronized void getPregnant(Chromosome partner) throws InterruptedException {
		this.partnerChromosome = partner;
		this.childCount++;
		Thread.sleep((restTime - Simulator.getMatrix().getPayOff(getType(), partner.getType()))*TIME_SPEED);
		notify();
	}
	
	private synchronized void generate() {
		Human child = new Human(new Chromosome(partnerChromosome,chromosome));
		Simulator.getPopulation().addHuman(child);
		System.out.println(Simulator.getPopulation());
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
	
	@Override
	public String toString() {
		String s = "";
		s += this.getType();
		s += "("+this.chromosome+")";
		return s;
	}
	
	public Human copy() {
		return new Human(this);
	}
	
	public synchronized void awake() {
		notify();
	}

}
