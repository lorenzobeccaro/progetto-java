
public class Human implements Runnable {

	private Chromosome chromosome;
	private Chromosome partnerChromosome;
	
	public static final int MAX_CHILDREN = 100;
	public static final int MAX_DATES = 5000;
	public static final int HAPPINESS_THRESHOLD = 3000;
	
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
		try {
			while(this.dateCount < MAX_DATES && this.childCount < MAX_CHILDREN && !Simulator.getPopulation().isShutdown()) {
				if(this.chromosome.getGender() == Gender.FEMALE) {
					Hotel.bar.sit(this);
					wait();
					if(this.partnerChromosome != null)
						generate();
				} else {
					Human partner = Hotel.bar.offerADrink();
					if(Simulator.getMatrix().areCompatible(getType(), partner.getType())) {
						dateWith(partner);
					}
				}
			}
		} catch (InterruptedException e) {
			
		}
		die();
	}
	
	private synchronized void dateWith(Human partner) {
		if(isHappy() && partner.isHappy()) {
			inseminate(partner);
		}
		//System.out.println(this);
		PayOffsMatrix m = Simulator.getMatrix();
		this.happiness += m.getPayOff(getType(), partner.getType());
		partner.happiness += m.getPayOff(partner.getType(), getType());
		this.dateCount++;
		partner.dateCount++;
		partner.awake();
	}

	private void die() {
		Simulator.getPopulation().removeHuman(this);
		//System.out.println(this);
	}
	
	public boolean isHappy() {
		return this.happiness >= HAPPINESS_THRESHOLD;
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
		//System.out.println(child);
		//Simulator.getPopulation().saveState();
		//System.out.println(Simulator.getPopulation());
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
		s += " h="+this.happiness;
		s += " c="+this.childCount;
		s += " d="+this.dateCount;
		return s;
	}
	
	public Human copy() {
		return new Human(this);
	}
	
	private synchronized void awake() {
		notify();
	}

}
