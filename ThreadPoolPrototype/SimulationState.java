package ThreadPoolPrototype;

import java.text.DecimalFormat;
import java.util.*;

public class SimulationState {
	private Map<String,Integer> types;


	public SimulationState(List<Human> list) {
		Map<String,Integer> types = new TreeMap<String,Integer>();
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
		this.types = types;
		//System.out.println(types);
	}

	@Override
	public String toString() {
		String s = "";
		Map<String,Integer> state = this.types;
		int humanNumber = this.getPopulationNumber();
		DecimalFormat df = new DecimalFormat("#.##");
		for(String k : state.keySet()) {
			s += k+": "+df.format(state.get(k)*100f/humanNumber)+"% "+state.get(k)+" ";
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
			double value = ((double)this.types.get(k)/(this.types.get(v)+this.types.get(k)));
			data.put(key, value);
		}
		return data;
	}

	private int getPopulationNumber() {
		int n = 0;
		for(String s : types.keySet()) {
			n += types.get(s);
		}
		return n;
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) {
			return true;
		}
		if(!(other instanceof SimulationState)) {
			return false;
		}
		SimulationState otherState = (SimulationState) other;
		if(this.types.equals(otherState.types)) {
			return true;
		} else {
			Map<String,Double> data,otherData;
			data = getObservingData();
			otherData = otherState.getObservingData();
			for(String k : data.keySet()) {
				if(otherData.containsKey(k)) {
					if(!almostEqual(data.get(k),otherData.get(k), 0.01)) {
						return false;
					}
				} else {
					return false;
				}
			}
			return true;
		}
	}

	private boolean almostEqual(double a, double b, double eps){
		return Math.abs(a-b)<eps;
	}

}