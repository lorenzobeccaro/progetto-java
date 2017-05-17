public class IntListItem {
	public int info;
	public IntListItem tail;

	IntListItem( int h ) {
		this.info = h;
		this.tail = null;
	}

	IntListItem( int h, IntListItem t ) {
		this.info = h;
		this.tail = t;
	}

}
