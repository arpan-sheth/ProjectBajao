package com.thread;

import java.io.File;
import java.util.Date;

import com.downloader.GenericHTTPClient;

public class WorkerThread extends Thread{
	private String file_name = "";
	public void run() {
		ThreadManager.incrementCount();
		process();
		ThreadManager.decrementCount();
	}

	public void process() {
		try {
			GenericHTTPClient.getFile("http://projectbajao.homeunix.com/songs_all/"+file_name, "/test/"+file_name);
			//connect to db;
			//get the url
			//download the file;
			//update the db;			
		}catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void setFileName(String file_name) {
		this.file_name = file_name;
	}

}
