package com.java.test.thread;

import java.util.concurrent.atomic.AtomicLong;

class Counter {
	int waiters = 0;
	Integer signal = 0;
	long count = 0;
	AtomicLong atomiCount = new AtomicLong();

	public long get() {
		return count;
	}

	public long getAtomic() {
		return this.atomiCount.get();
	}

	public void addAtomic(long value) {
		this.atomiCount.addAndGet(value);
	}

	public synchronized void add(long value) {
		count += value;
	}

	public void doWait(int waiters) {
		synchronized (signal) {
			this.waiters = waiters;
			try {
				System.out.println("thread " + Thread.currentThread().getId()
						+ " waiting...");
				signal.wait();
				System.out.println("thread " + Thread.currentThread().getId()
						+ " done...");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void doNotify() {
		System.out.println("thread " + Thread.currentThread().getId()
				+ " notify...");
		synchronized (signal) {
			if (--waiters <= 0) {
				signal.notify();
			}
		}
	}
}

class CounterThread extends Thread {
	private Counter counter = null;

	public CounterThread(Counter counter) {
		this.counter = counter;
	}

	public void run() {
		for (int i = 0; i < 100; i++) {
			counter.add(i);
			counter.addAtomic(i);
		}
		this.counter.doNotify();
	}
}

public class Synchronized {
	public static void main(String[] args) {
		Counter counter = new Counter();
		// Counter counterB = new Counter();
		Thread threadA = new CounterThread(counter);
		Thread threadB = new CounterThread(counter);

		threadA.start();
		threadB.start();
		// try {
		// threadA.join();
		// threadB.join();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		counter.doWait(2);
		System.out.println(counter.get());
		System.out.println(counter.getAtomic());

	}
}
