package com.crawler;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.downloader.GenericHTTPClient;

public class wc {
	// URLs to be searched
	Vector<String> vectorToSearch;
	// URLs already searched
	HashSet<String> hashSearched;
	// URLs which match
	HashSet<String> hashMatches;
	Thread searchThread;
	int numberSearched = 0;
	int numberFound = 0;
	Stack<String> stackToSearch;

	public static final int    SEARCH_LIMIT = 999999999;
	public static final String DISALLOW = "Disallow:";
	private URL input_url_connection = null;
	private String format = ".mp3";
	private FileOutputStream fos ;
	private OutputStreamWriter osw;
	private PrintWriter pw;
	BufferedWriter out ;
	Pattern p = Pattern.compile(".*category/view/[0-9]+/\\?letter.*");

	private URL urlValidate(String url){
		URL returnURL = null;
		try {
			returnURL = new URL(url);
		} catch (MalformedURLException e1) {
			setStatus("ERROR: invalid URL " + url);
		}
		return returnURL;
	}

	private void setStatus(String status) {
		System.out.println(status);
	}

	public void init() throws Exception {
		// initialize search data structures
		vectorToSearch = new Vector<String>();
		hashSearched = new HashSet<String>();
		hashMatches = new HashSet<String>();
		searchThread = Thread.currentThread();
		stackToSearch = new Stack<String>();

		fos = new FileOutputStream("/Users/arpan/Desktop/mp3Links");
		osw = new OutputStreamWriter(fos, "UTF8");
		pw =  new PrintWriter(osw);
		
		out = new BufferedWriter(new FileWriter("/Users/arpan/Desktop/mp3stest", true)); 

	}

	public void runBFS(String input_url) {
		//input validation
		input_url_connection = urlValidate(input_url);
		if (input_url_connection==null)
			return;
		String strURL = input_url;
		if (strURL.length() == 0) {
			setStatus("ERROR: must enter a starting URL");
			return;
		}

		// initialize search data structures
		vectorToSearch.removeAllElements();
		hashSearched.clear();
		hashMatches.clear();
		stackToSearch.clear();

		doCrawlBFS(strURL);
	}

	public void runDFS(String input_url) {
		//input validation
		input_url_connection = urlValidate(input_url);
		if (input_url_connection==null)
			return;
		String strURL = input_url;
		if (strURL.length() == 0) {
			setStatus("ERROR: must enter a starting URL");
			return;
		}

		// initialize search data structures
		vectorToSearch.removeAllElements();
		hashSearched.clear();
		hashMatches.clear();
		stackToSearch.clear();

		doCrawlDFS(strURL);
	}

	private void doCrawlBFS(String strURL){
		vectorToSearch.addElement(strURL); //seed URL.
		while ((vectorToSearch.size() > 0) && (Thread.currentThread() == searchThread)) {
			// get the first element from the to be searched list
			strURL = (String) vectorToSearch.elementAt(0);
			setStatus("searching " + strURL);
			URL url = urlValidate(strURL);
			if (url==null){
				break;
			}
			// mark the URL as searched (we want this one way or the other)
			vectorToSearch.removeElementAt(0);
			hashSearched.add(strURL);
			// can only search http: protocol URLs
			if (url.getProtocol().compareTo("http") != 0) 
				break;

			String content = GenericHTTPClient.getResponse(strURL);
			if (content==null||content.length()==0)
				continue;
			if (Thread.currentThread() != searchThread)
				break;
			String lowerCaseContent = content.toLowerCase();
			doParsingBFS(lowerCaseContent, content, url);

			numberSearched++;
			if (numberSearched >= SEARCH_LIMIT)
				break;
		}	
		if (numberSearched >= SEARCH_LIMIT || numberFound >= SEARCH_LIMIT)
			setStatus("reached search limit of " + SEARCH_LIMIT);
		else
			setStatus("done");
		searchThread = null;
	}

	private void doParsingBFS(String lowerCaseContent, String content, URL url){
		int index = 0;
		while ((index = lowerCaseContent.indexOf("<a", index)) != -1){
			if ((index = lowerCaseContent.indexOf("href", index)) == -1) 
				break;
			if ((index = lowerCaseContent.indexOf("=", index)) == -1) 
				break;

			if (Thread.currentThread() != searchThread)
				break;
			index++;
			String remaining = content.substring(index);
			StringTokenizer st = new StringTokenizer(remaining, "\t\n\r\">#");
			String strLink = st.nextToken();

			URL urlLink;
			try {
				urlLink = new URL(url, strLink);
				strLink = urlLink.toString();
			} catch (MalformedURLException e) {
				setStatus("ERROR: bad URL " + strLink);
				continue;
			}
			// only look at http links
			if (urlLink.getProtocol().compareTo("http") != 0)
				break;
			if (Thread.currentThread() != searchThread)
				break;

			// check to see if this URL has already been 
			// searched or is going to be searched
			int i=0; //counter for number of strings in the search vector
			if ((!hashSearched.contains(strLink))	&& (!vectorToSearch.contains(strLink)) && (strLink.contains(input_url_connection.getHost().toString())) ) {
				System.out.println("ADDED " + strLink);
				vectorToSearch.addElement(strLink);
				/*				if (vectorToSearch.size()>(i+10)) {
					System.out.println(vectorToSearch);
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				 */
				i++;
			}
			if ((strLink.endsWith(format)) && hashMatches.contains(strLink) == false) {
				storeInTextFile(strLink);
				hashMatches.add(strLink);
				numberFound++;
				if (numberFound >= SEARCH_LIMIT)
					break;
			}
		}
	}


	private void doCrawlDFS (String strURL) {
		stackToSearch.push(strURL);
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=1");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=A");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=B");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=C");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=D");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=E");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=F");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=G");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=H");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=I");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=J");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=K");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=L");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=M");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=N");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=O");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=P");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=Q");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=R");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=S");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=T");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=U");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=V");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=W");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=X");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=Y");
		stackToSearch.push("http://www.apunkabollywood.net/browser/category/?letter=Z");

		while ((stackToSearch.size() > 0) && (Thread.currentThread() == searchThread)) {
			// get the first element from the to be searched list
			strURL = (String) stackToSearch.pop();
		//	setStatus("searching " + strURL);
			URL url = urlValidate(strURL);
			if (url==null){
				break;
			}
			// mark the URL as searched (we want this one way or the other)
			hashSearched.add(strURL);
			// can only search http: protocol URLs
			if (url.getProtocol().compareTo("http") != 0) 
				break;

			if (strURL.endsWith("MP3"))
				continue;
	
			String content = GenericHTTPClient.getResponse(strURL);
			if (content==null||content.length()==0)
				continue;
			if (Thread.currentThread() != searchThread)
				break;
			String lowerCaseContent = content.toLowerCase();
			doParsingDFS(lowerCaseContent, content, url);
		}
	}
	
	private void doParsingDFS(String lowerCaseContent, String content, URL url){
		int index = 0;
		while ((index = lowerCaseContent.indexOf("<a", index)) != -1){
			if ((index = lowerCaseContent.indexOf("href", index)) == -1) 
				break;
			if ((index = lowerCaseContent.indexOf("=", index)) == -1) 
				break;

			if (Thread.currentThread() != searchThread)
				break;
			index++;
			String remaining = content.substring(index);
			StringTokenizer st = new StringTokenizer(remaining, "\t\n\r\">#");
			String strLink = st.nextToken();
			strLink = strLink.replace("'","");

			URL urlLink;
			try {
				if (!strLink.contains("http"))
					urlLink = new URL(url, strLink);
				else 
					urlLink = new URL(strLink);
				strLink = urlLink.toString();
			} catch (MalformedURLException e) {
				setStatus("ERROR: bad URL " + strLink);
				continue;
			}
			// only look at http links
			if (urlLink.getProtocol().compareTo("http") != 0)
				continue;
			if (Thread.currentThread() != searchThread)
				break;

			Matcher m = p.matcher(strLink);
			if (m.matches())
				continue;
			// check to see if this URL has already been 
			// searched or is going to be searched
			if ((!hashSearched.contains(strLink))	 && (strLink.contains(input_url_connection.getHost().toString().split("\\.")[1])) ) {
				if ((strLink.endsWith(format) || strLink.endsWith("MP3")) && (!hashMatches.contains(strLink))) {
					write(strLink);
					System.out.println(numberFound + " : ["+strLink+"]");
					hashMatches.add(strLink);
					numberFound++;
					if (numberFound >= SEARCH_LIMIT)
						break;
				}else {
					if (!strLink.endsWith(format) && !strLink.endsWith("wma") && !strLink.endsWith("WMA")&& (!strLink.endsWith("MP3"))){
					System.out.println("ADDED " + strLink);
					stackToSearch.push(strLink);
					}
				}
			}
		}
	}


	public void write(String mp3Link){
		try { 
			out.newLine();
			out.write(mp3Link);
			if (numberFound%10==0){
				out.flush();
			}
			System.out.println("Writing file "+ mp3Link);
			 
		} catch (IOException e) { 

		} 
	}

	public void storeInTextFile(String mp3Link){
		System.out.println("Writing file "+ mp3Link);
		pw.println(mp3Link);
	}

	public void destroy(){
		pw.close();
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		wc obj = new wc();
		try{
			obj.init();
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("iniitialization failed");
			return;
		}
		obj.format=".mp3"; 
		//	obj.run("http://www.apunkabollywood.net/");
		obj.runDFS("http://www.apunkabollywood.net/");
		obj.destroy(); 
	}
}