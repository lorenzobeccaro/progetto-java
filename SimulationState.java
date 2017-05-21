
import java.text.DecimalFormat;
import java.util.*;

public class SimulationState {
	private Map<String,Integer> population;
	private Map<String,Double> percentages;
	
	private final double ERROR = 0.0001;

	public SimulationState() {
		
	}

	public SimulationState(List<Human> list) {
		Map<String,Integer> types = new TreeMap<String,Integer>();
		Map<String,Double> perc = new TreeMap<String,Double>();
		List<Human> snapshot = new LinkedList<Human>(list);
		//System.out.println(snapshot);
		for(Human h : snapshot) {
			//System.out.println(h);
			if(h==null) continue;
			if(types.containsKey(h.getType())) {
				types.put(h.getType(), types.get(h.getType())+1);
			} else {
				types.put(h.getType(), 1);
			}
		}
		this.population = types;
		for(String k : types.keySet()) {
			perc.put(k, (types.get(k)*100d/getPopulationNumber()));
		}
		this.percentages = perc;
		//System.out.println(types);
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

	@Override
	public String toString() {
		String s = "";
		Map<String,Integer> state = this.population;
		Map<String,Double> perc = this.percentages;
		int humanNumber = this.getPopulationNumber();
		DecimalFormat df = new DecimalFormat("#.##");
		for(String k : perc.keySet()) {
			s += k+": "+df.format(perc.get(k))+"% "+state.get(k)+" ";
		}
		s += "population: "+humanNumber;
		s += '\n';
		Map<String,Double> data = this.getObservingData();
		for(String k : data.keySet()) {
			s += k;
			s += ": "+df.format(data.get(k));
			s += " ";
		}

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
		Map<String,Integer> data,otherData;
		data = this.getPopulation();
		otherData = otherState.getPopulation();
		for(String k : data.keySet()) {
			if(otherData.containsKey(k)) {
				double perc1, perc2;
				perc1 = data.get(k)/this.getPopulationNumber();
				perc2 = otherData.get(k)/otherState.getPopulationNumber();
				if(!almostEqual(perc1,perc2, ERROR)) {
					//System.out.println(""+data.get(k)+" "+otherData.get(k));
					return false;
				}
			} else {
				//System.out.println(""+data.get(k)+" "+otherData.get(k));
				return false;
			}
		}
		return true;

	}

	private boolean almostEqual(double a, double b, double eps){
		//System.out.println(Math.abs(a-b)+"<"+eps);
		return Math.abs(a-b)<eps;
	}

}