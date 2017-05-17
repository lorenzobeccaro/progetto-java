public class testcode {
	public static void main(String[] args) {
		// test sulle code
		Coda c = new Coda();
		if (c.is_empty())
			System.out.println("coda inizialmente vuota");
		try {
			c.inserisci(7);
			c.inserisci(9);
			System.out.println(c.estrai()); // stampa 7
			c.estrai();
			c.estrai();
		}
		catch (Coda_Vuota_Exception e) {
			System.out.println("eccezione catturata!");
		}
	}
}