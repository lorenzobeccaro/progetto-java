
public abstract class IntList {
	
	public IntListItem head;
	
	abstract void inserisci( int i );
	
	abstract int estrai() throws Coda_Vuota_Exception;
	
	public boolean is_empty() {
		return ( this.head == null );
	}

	public int len() {
		return lenRec(this.head);
	}

	private int lenRec( IntListItem a ) {
		if(a.tail == null) {
			return 1;
		} else {
			return 1 + lenRec(a.tail);
		}
	}

	public String toString() {
		return "[ " + toStringRec(this.head);
	}

	private String toStringRec( IntListItem a ) {
		if(a.tail == null) {
			return a.info + " ]";
		} else {
			return a.info + ", " + toStringRec(a.tail);
		}
	}
	
}
