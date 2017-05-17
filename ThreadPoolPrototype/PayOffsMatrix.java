package ThreadPoolPrototype;

import java.util.*;

public class PayOffsMatrix {
	
	Map<String,Map<String,Integer>> matrix = new HashMap<String,Map<String,Integer>>();
	
	public boolean areCompatible(String a, String b) {
		return matrix.get(a).containsKey(b);
	}
	
	public List<String> getCompatible(String key) {
		Set<String> s = matrix.get(key).keySet();
		return new LinkedList<String>(s);
	}
	
	public void addFormula(String tipoA, String tipoB, int premioA, int premioB) {
		if(matrix.containsKey(tipoA)) {
			matrix.get(tipoA).put(tipoB, premioA);
		} else {
			Map<String,Integer> newMap = new HashMap<String,Integer>();
			newMap.put(tipoB, premioA);
			matrix.put(tipoA, newMap);
		}
		if(matrix.containsKey(tipoB)) {
			matrix.get(tipoB).put(tipoA, premioB);
		} else {
			Map<String,Integer> newMap = new HashMap<String,Integer>();
			newMap.put(tipoA, premioB);
			matrix.put(tipoB, newMap);
		}
	}
	
	public int getPayOff(String A, String B) {
		return matrix.get(A).get(B);
	}
	
	@Override
	public String toString() {
		String s = "";
		for(String k : matrix.keySet()) {
			for(String j : matrix.get(k).keySet()) {
				s += k+" gains "+this.getPayOff(k, j)+" with "+j+'\n';
			}
		}
		return s;
	}
	
}
