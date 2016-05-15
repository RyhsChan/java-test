package collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IteratorSafety {

	static void iteratorSafety(final Collection<Integer> intList)
			throws InterruptedException {
		new Thread(new Runnable() {
			public void run() {
				Iterator<Integer> iter = intList.iterator();
				while (iter.hasNext()) {
					iter.next();
					iter.remove();
					System.out.println("deleted a item");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

		Iterator<Integer> iter = intList.iterator();
		while (iter.hasNext()) {
			Thread.sleep(200);
			System.out.println(iter.next());
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Collection<Integer> intList = new ConcurrentLinkedQueue<Integer>();
		
		// neither ArrayList nor Collections.synchronizedCollection will
		// guarantee thread safety while iterating the Collection
		intList = new ArrayList<Integer>();
		intList = Collections.synchronizedList(new ArrayList<Integer>());

		intList.add(10);
		intList.add(20);
		intList.add(30);
		intList.add(40);
		intList.add(50);

		iteratorSafety(intList);
	}
}
