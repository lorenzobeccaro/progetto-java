
import java.text.DecimalFormat;
import java.util.*;

public class SimulationState {
	private Map<String,Integer> population;
	private Map<String,Double> percentages;
	
	private final double ERROR = 0.05;

	public SimulationState(List<Human> list) {
		Map<String,Integer> types = new TreeMap<String,Integer>();
		Map<String,Double> perc = new TreeMap<String,Double>();
		List<Human> snapshot = new LinkedList<Human>(list);
		
		for(Human h : snapshot) {
			if(h==null) continue;
			if(types.containsKey(h.getType())) {
				types.put(h.getType(), types.get(h.getType())+1);
			} else {
				types.put(h.getType(), 1);
			}
			
		}
		
		this.population = types;
		for(String k : types.keySet()) {
			perc.put(k, (types.get(k).doubleValue()/getPopulationNumber()));
		}
		this.percentages = perc;
	}

	public Map<String, Integer> getPopulation() {
		return population;
	}

	public void setPopulation(Map<String, Integer> population) {
		this.population = population;
	}

	public Map<String, Double> getPercentages() {
		return percentages;
	}

	public void setPercentages(Map<String, Double> percentages) {
		this.percentages = percentages;
	}
	
	public double getHappiness(String type) {
		PayOffsMatrix m = Simulator.getMatrix();
		double happiness = 0;
		double weight = 0;
		for(String t : this.getPercentages().keySet()) {
			if(!Chromosome.getGenderByType(t).equals(Chromosome.getGenderByType(type))) {
				happiness += this.getPercentages().get(t)*m.getPayOff(type, t);
				weight += this.getPercentages().get(t);
			}
		}
		return happiness/weight;
	}

	@Override
	public String toString() {
		String s = "";
		Map<String,Integer> state = this.population;
		Map<String,Double> perc = this.percentages;
		int humanNumber = this.getPopulationNumber();
		DecimalFormat df = new DecimalFormat("#.##");
		
		final int MAX_COLS = 80;
		final int MAX_LINES = 50;
		int num_types = perc.keySet().size();
		
		for(int i=0;i<MAX_LINES;i++) {
			
			for(String k : perc.keySet()) {
				String spaces = new String(new char[MAX_COLS/num_types]).replace('\0', ' ');
				s += spaces;
				if(perc.get(k)*100>(MAX_LINES-i)*100/MAX_LINES)
					s += "|";
				else {
					s += " ";
				}
				
			}
			s += "\n";
		}

		int prev = 0;
		for(String k : perc.keySet()) {
			String info = k+": "+df.format(perc.get(k)*100)+"% "+state.get(k)+" ";
			int current_spaces = info.length()/2;
			String spaces = new String(new char[(MAX_COLS/num_types)-(current_spaces+prev)]).replace('\0', ' ');
			s += spaces+info;
			prev = current_spaces;
		}
		prev = 0;
		s += "\n";
		for(Population.SubPopulation t : Simulator.getPopulation().threadPools) {
			String info = "Threads "+t.getName()+": "+t.getActiveCount();
			int current_spaces = info.length()/2;
			String spaces = new String(new char[(MAX_COLS/num_types)-(current_spaces+prev)]).replace('\0', ' ');
			s += spaces+info;
			prev = current_spaces;
		}
		s += "\npopulation: "+humanNumber;
		s += "\nThreads: "+Simulator.getPopulation().getTotalThreads()+"\n";
		s += "Active Humans: "+Simulator.getPopulation().alive.size()+"\n";
		Map<String,Double> data = this.getObservingData();
		for(String k : data.keySet()) {
			s += k;
			s += ": "+df.format(data.get(k));
			s += " ";
		}
		s += "  Avg happiness: ";
		for(String k : perc.keySet()) {
			s += k;
			s += ": "+df.format(Simulator.getPopulation().getHappiness(k));
			s += " ";
		}
		s += " Male: "+df.format(Simulator.getPopulation().getThresholdForGender(Gender.MALE,this));
		s += " Female: "+df.format(Simulator.getPopulation().getThresholdForGender(Gender.FEMALE,this));
		return s;
	}

	private Map<String,Double> getObservingData() {
		Map<String,Double> data = new HashMap<String,Double>();
		for(String k : Simulator.getPopulation().getObservingData().keySet()) {
			String v = Simulator.getPopulation().getObservingData().get(k);
			String key = k+"/"+v+"+"+k;
			if(population.containsKey(k) && population.containsKey(v)) {
				double value = ((double)this.population.get(k)/(this.population.get(v)+this.population.get(k)));
				data.put(key, value);
			}
			
			
		}
		return data;
	}

	private int getPopulationNumber() {
		int n = 0;
		for(String s : population.keySet()) {
			n += population.get(s);
		}
		return n;
	}

	public boolean isNear(SimulationState otherState) {
		Map<String,Double> data,otherData;
		data = this.getPercentages();
		otherData = otherState.getPercentages();
		for(String k : data.keySet()) {
			if(otherData.containsKey(k)) {
				double perc1, perc2;
				perc1 = data.get(k);
				perc2 = otherData.get(k);
				if(!almostEqual(perc1,perc2, ERROR)) {
					return false;
				}
			}
		}
		return true;

	}

	private boolean almostEqual(double a, double b, double eps){
		return Math.abs(a-b)<eps;
	}

}