package com.thread;

import java.util.Date;
import java.util.Random;

public class ThreadManager {

	final static int MAX_THREADS = 3;
	private static int active_count=0;
	Random random = new Random();

	public long initializeThread(String file_name) {
		WorkerThread d_thread = new WorkerThread();
		d_thread.setFileName(file_name);
		d_thread.start();
		return d_thread.getId();
	}

	public void killThread(long thread_id) {

	}
	public void doStuff() throws InterruptedException {
		//initialized the max number of threads
		int i=1;
		while (true ) {
			if (active_count<MAX_THREADS) {
				System.out.println("Thread generated with id = " + initializeThread("songs16."+i+".mp3"));
				i++;
			}
			Thread.currentThread().sleep(random.nextInt(3000));
		}
	}

	public static synchronized void decrementCount() {
		active_count--;
		System.out.println("Active count after decrementing = "+ active_count);
	}

	public static synchronized void incrementCount() {
		active_count++;
		System.out.println("Active count after incrementing = "+ active_count);
	}

	public static void main(String args[]) throws InterruptedException {
		new ThreadManager().doStuff();
	}

}
