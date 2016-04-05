package com.java.test.thread;

public class SimpleThread {
	public static void work(String name, int secs) {
		while (secs-- > 0) {
			System.out.println(name + " is running...");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Runnable simpleThread = new Runnable() {
			private ThreadLocal<Long> threadLocal = new ThreadLocal<Long>();

			public void run() {
				threadLocal.set(Thread.currentThread().getId());
				SimpleThread.work("Thread " + threadLocal.get(), 3);
			}
		};

		Thread threadA = new Thread(simpleThread);
		Thread threadB = new Thread(simpleThread);
		threadA.start();
		threadB.start();

		SimpleThread.work("Thread " + Thread.currentThread().getId(), 3);
	}

}
