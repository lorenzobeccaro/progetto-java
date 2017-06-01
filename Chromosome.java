

import java.util.*;


public class Chromosome implements TreePrinter.PrintableNode {
	private static Map<String,Gender> type2gender = new HashMap<String,Gender>();
	private static Map<String,Character> maleType2gene = new HashMap<String,Character>();;
	private static Map<String,Character> femaleType2gene = new HashMap<String,Character>();;
	private Gene maleGene;
	private Gene femaleGene;
	private Gender gender;
	
	private Chromosome father;
	private Chromosome mother;
	
	public Chromosome(Gene mGene, Gene fGene) {
		this.maleGene = mGene;
		this.femaleGene = fGene;
	}
	
	public Chromosome(String type) {
		this.gender = type2gender.get(type);
		if(this.gender == Gender.MALE) {
			this.maleGene = new Gene(maleType2gene,type);
		} else {
			this.femaleGene = new Gene(femaleType2gene,type);
		}
	}
	
	public Chromosome(Chromosome father, Chromosome mother) {
		
		this.father = father;
		this.mother = mother;
		
		if(father.femaleGene == null || mother.maleGene == null) {
			this.maleGene = father.maleGene;
			this.femaleGene = mother.femaleGene;
		} else {
			this.maleGene = new Gene(father.maleGene,mother.maleGene);
			this.femaleGene = new Gene(father.femaleGene,mother.femaleGene);
		}
		
		if(Math.random()>.5) {
			this.gender = Gender.MALE;
		} else {
			this.gender = Gender.FEMALE;
		}
		
	}
	
	public Chromosome() {
		this.maleGene = new Gene();
		this.femaleGene = new Gene();
	}

	public Chromosome getLeft() {
		return father;
	}
	
	public Chromosome getRight() {
		return mother;
	}
	
	public String getText() {
		return this.toString();
	}
	
	public static List<String> getTypesByGender(Gender g) {
		List<String> list = new ArrayList<String>();
		for(String t : type2gender.keySet()) {
			if(type2gender.get(t)==g)
				list.add(t);
		}
			
		return list;
	}
	
	public static void mapTypeToGene(Gender gender,String type, boolean dominant) {
		if(type.length()>1)/* TODO Lancia eccezione */;
		Chromosome.type2gender.put(type,gender);
		Map<String,Character> genes;
		if(gender == Gender.MALE) {
			genes = Chromosome.maleType2gene;
		} else if(gender == Gender.FEMALE) {
			genes = Chromosome.femaleType2gene;
		} else {
			genes = new HashMap<String,Character>();
		}
		char gene;
		if(dominant) {
			gene = Character.toUpperCase(type.charAt(0));
		} else {
			gene = Character.toLowerCase(type.charAt(0));
		}
		genes.put(type, gene);
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public Gender getGender() {
		return this.gender;
	}
	
	public String getType() {
		char type;
		if(this.gender == Gender.MALE) {
			type = this.maleGene.getType();
		} else if(this.gender == Gender.FEMALE) {
			type = this.femaleGene.getType();
		} else {
			type = '!';
		}
		return ""+Character.toUpperCase(type);
	}
	
	@Override
	public String toString() {
		String s = ""+this.getType()+"";
		s += "(["+this.maleGene+"]";
		s += "["+this.femaleGene+"])";
		return s;
	}

	private class Gene {
		private char x1;
		private char x2;
		
		public Gene(Gene g1, Gene g2) {
			if(Math.random()>.5) {
				this.x1 = g1.x1;
			} else {
				this.x1 = g1.x2;
			}
			if(Math.random()>.5) {
				this.x2 = g2.x1;
			} else {
				this.x2 = g2.x2;
			}
		}

		public Gene(Map<String,Character> geneMap, String type) {
			this.x1 = geneMap.get(type);
			this.x2 = geneMap.get(type);
		}

		public Gene() {
			this.x1 = '\0';
			this.x2 = '\0';
		}

		public char getType() {
			char c;
			if(isDominant(this.x1)) {
				c = this.x1;
			} else if(isDominant(this.x2)) {
				c = this.x2;
			} else {
				c = this.x1;
			}
			return c;
		}

		public boolean isDominant(char ch) {
			return Character.isUpperCase(ch);
		}
		
		@Override
		public String toString() {
			return ""+this.x1+this.x2;
		}

	}

	public static List<String> getTypes() {
		return new LinkedList<String>(type2gender.keySet());
	}

	public static Gender getGenderByType(String type) {
		return type2gender.get(type);
	}


}


