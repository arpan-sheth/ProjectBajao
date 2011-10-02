package com.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.downloader.GenericHTTPClient;
import com.utility.Utility;


public class DFSCrawlerParsingThreadWorker extends Thread {

	Pattern p = Pattern.compile(".*funmaza[aA-zZ 0-9]+.*");
	Pattern p_games = Pattern.compile(".*games.funmaza.*");
	//Pattern p = Pattern.compile(".*category/view/[0-9]+/\\?letter.*");
	DFSCrawlerDataSource dfsCDS = DFSCrawlerDataSource.getInstance();

	private void doCrawlDFS () {
		String strURL ="";
		while ((dfsCDS.getStackToSearchSize() > 0)) {
			// get the first element from the to be searched list
			strURL = dfsCDS.popStackToSearch();
			System.out.println("searching " + strURL);
			URL url = Utility.urlValidate(strURL);
			if (url==null){
				break;
			}
			// mark the URL as searched (we want this one way or the other)
			//dfsCDS.addHasMatches(strURL);
			dfsCDS.addHasSearched(strURL);
			dfsCDS.writeInHasSearchedFile(strURL);
			// can only search http: protocol URLs
			if (url.getProtocol().compareTo("http") != 0) 
				break;

			if (strURL.endsWith("MP3") || strURL.endsWith("zip") || strURL.endsWith("gif") || strURL.endsWith("jpg") || strURL.endsWith("png") || strURL.endsWith("mp3") || strURL.endsWith("rm")
					|| strURL.endsWith("RM")|| strURL.endsWith(".3gp") || strURL.endsWith(".3GP") || strURL.endsWith(".EXE") || strURL.endsWith(".exe")|| strURL.endsWith(".mid")|| strURL.endsWith(".MID") || strURL.endsWith(".mmf") 
					|| strURL.endsWith(".MMF") || strURL.contains("mobile") || strURL.contains("MOBILE") || strURL.contains("ringtones") || strURL.contains("ringtone") || strURL.contains("polyphonic")
					|| strURL.contains("video") || strURL.contains(".wmv") || strURL.contains(".WMV")
					|| strURL.contains(".MP4") || strURL.contains(".mp4") || strURL.contains("login")
					|| strURL.contains("image"))
				continue;

			Matcher m = p.matcher(strURL);
			if (m.matches())
				continue;
			Matcher m_games = p_games.matcher(strURL);
			if (m_games.matches())
				continue;
			String content = GenericHTTPClient.getResponse(strURL);
			if (content==null||content.length()==0)
				continue;
			String lowerCaseContent = content.toLowerCase();
			doParsingDFS(lowerCaseContent, content, url);
		}
		dfsCDS.flushBuffer();
	}	

	private void doParsingDFS(String lowerCaseContent, String content, URL url){
		int index = 0;
		if ((lowerCaseContent.endsWith("mp3") || lowerCaseContent.endsWith("MP3") || lowerCaseContent.endsWith("wma") || lowerCaseContent.endsWith("WMA"))) {
			if (!dfsCDS.checkContainsHasSearched(lowerCaseContent) ) {
				dfsCDS.writeInMp3File(lowerCaseContent);
				System.err.println(lowerCaseContent);
			}
			return;
		}
		while ((index = lowerCaseContent.indexOf("<a", index)) != -1){
			if ((index = lowerCaseContent.indexOf("href", index)) == -1) 
				break;
			if ((index = lowerCaseContent.indexOf("=", index)) == -1) 
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
				System.out.println("ERROR: bad URL " + strLink);
				continue;
			}
			// only look at http links
			if (urlLink.getProtocol().compareTo("http") != 0)
				continue;

			Matcher m = p.matcher(strLink);
			if (m.matches())
				continue;
			// check to see if this URL has already been 
			// searched or is going to be searched
			if (!dfsCDS.checkContainsHasSearched(strLink) && ( strLink.contains(DFSCrawlerDataSource.getInput_url_connection().getHost().toString().split("\\.")[1])) ) {
				if ((strLink.endsWith("mp3") || strLink.endsWith("MP3") || strLink.endsWith("wma") || strLink.endsWith("WMA"))) {
					dfsCDS.writeInMp3File(strLink);
					System.err.println(strLink);
					//		dfsCDS.addHasMatches(strLink);
				}else {
					System.out.println("ADDED " + strLink);
					dfsCDS.pushStackToSearch(strLink);
				}
			}
		}
	}

	@Override
	public void run(){
		doCrawlDFS();
	}
}
