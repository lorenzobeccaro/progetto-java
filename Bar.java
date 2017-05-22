

import java.util.*;


public class Bar {

		private Queue<Human> seats = new LinkedList<Human>();

		public synchronized boolean isEmpty() {
			return seats.isEmpty();
		}

		public synchronized void sit(Human client) {
			
			if (isEmpty()) {
				seats.add(client);
				notifyAll();
			} else {
				seats.add(client);
			}
		}
		
		public int size() {
			return seats.size();
		}

		public synchronized Human offerADrink() throws InterruptedException {
			while (isEmpty()) {
				wait();
			}
			Human woman = seats.poll();
			return woman;
		}	

	}