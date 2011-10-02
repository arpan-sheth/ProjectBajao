package com.crawler;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Stack;

public class DFSCrawlerDataSource {

	private static DFSCrawlerDataSource wcDFSObject  = null;
	// URLs to be searched
	private static Stack<String> stackToSearch;
	// URLs already searched
	private static HashSet<String> hashSearched;
	// URLs which match
	private static HashSet<String> hashMatches;

	private static String mp3NameLocation = "/Users/arpan/Desktop/mp3s_2_desifunda";
	private static BufferedWriter outForMp3 ;
	private static String hasSearchedLocation = "/Users/arpan/Desktop/hasSearchedURLs_2_desifunda";
	private static BufferedWriter outForHasSearched ;

	private static URL input_url_connection = null;

	private DFSCrawlerDataSource(){}

	private static void init() throws Exception {
		// initialize search data structures
		hashSearched = new HashSet<String>();
		hashMatches = new HashSet<String>();
		stackToSearch = new Stack<String>();
		// initialize search data structures
		hashSearched.clear();
		hashMatches.clear();
		stackToSearch.clear();

		outForMp3 = new BufferedWriter(new FileWriter(mp3NameLocation, true));
		outForHasSearched = new BufferedWriter(new FileWriter(hasSearchedLocation, true));
	}

	public synchronized static DFSCrawlerDataSource getInstance(){
		if (wcDFSObject==null){
			try {
				init();
			} catch (Exception e) {
				e.printStackTrace();
			}
			wcDFSObject = new DFSCrawlerDataSource();
		}
		return wcDFSObject;
	}

	public  synchronized String popStackToSearch(){
		String returnString;
		try {
			returnString = stackToSearch.pop();
		}catch (EmptyStackException e){
			returnString = "";
		}
		return returnString;
	}

	public  synchronized void pushStackToSearch(String pushString){
		stackToSearch.push(pushString);
	}

	public  synchronized boolean checkContainsHasSearched(String checkString){
		return hashSearched.contains(checkString);
	}
	
	public  synchronized void addHasSearched(String addString){
		hashSearched.add(addString);
	}

	public  synchronized void addHasMatches(String addString){
		hashMatches.add(addString);
	}

	public  synchronized boolean checkHasMatches(String checkString){
		return hashMatches.contains(checkString);
	}

	public  synchronized void writeInMp3File(String stringToWrite){
		try { 
			outForMp3.newLine();
			outForMp3.write(stringToWrite);
		} catch (IOException e) { 
			System.out.println("Error writing mp3 Url in file");
		} 
	}

	public  synchronized void writeInHasSearchedFile(String stringToWrite){
		try { 
			outForHasSearched.newLine();
			outForHasSearched.write(stringToWrite);
		} catch (IOException e) { 
			System.out.println("Error writing has searched url in file" +e);
		} 
	}

	public synchronized int getStackToSearchSize(){
		return stackToSearch.size();
	}
	
	public  void flushBuffer(){
			try {
				outForMp3.flush();
				outForHasSearched.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	// only the CrawlerBoss should call this.
	public static void destroy(){
		try {
			outForMp3.flush();
			outForMp3.close();
			outForHasSearched.flush();
			outForHasSearched.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void setInput_url_connection(URL input_url_connection) {
		DFSCrawlerDataSource.input_url_connection = input_url_connection;
	}

	public static URL getInput_url_connection() {
		return input_url_connection;
	}
}
