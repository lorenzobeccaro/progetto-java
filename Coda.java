// STRUTTURA DATI FIFO
public class Coda extends IntList {
	
	Coda() {
		super.head = null;
	}

	Coda( int i ) {
		super.head = new IntListItem(i);
	}
	
	public void inserisci( int i ) {
		IntListItem a = new IntListItem(i);
		this.inserisci(a);
	}

	public void inserisci( IntListItem a ) {
		this.inserisciRec(this.head, a);
	}

	private void inserisciRec( IntListItem a, IntListItem b ) {
		if( this.is_empty() ) {
			this.head = b;
		} else if( a.tail == null ) {
			a.tail = b;
		} else {
			this.inserisciRec(a.tail, b);
		}
	}

	public int estrai() throws Coda_Vuota_Exception {
		if ( this.is_empty() ) {
			throw new Coda_Vuota_Exception();
		}
		int r = this.head.info;
		this.head = this.head.tail;
		return r;
	}


}
