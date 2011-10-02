package com.downloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

public class GenericHTTPClient {
	private static org.apache.log4j.Logger log = Logger.getLogger(com.downloader.GenericHTTPClient.class);

	public static boolean getFile (String url, String storingLocation) {
		boolean result = false;
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		// Create a method instance.
		GetMethod method = null;
		try {
			url= url.replaceAll(" ","%20").replace("[","%5B").replace("]", "%5D").replace("{","%7B").replace("}", "%7D");
			method = new GetMethod(url);
			// Provide custom retry handler is necessary
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					new DefaultHttpMethodRetryHandler(3, false));
			// Execute the method.
			
			int statusCode = client.executeMethod(method);
		
			if (statusCode != HttpStatus.SC_OK) {
				log.error("Method failed: " + method.getStatusLine());
				log.error("GenericHTTPClient: Failure downloading song " + url);
				return result;
			}

			// Read the response body.
			InputStream inputStream = method.getResponseBodyAsStream();

			OutputStream out=new FileOutputStream(new File (storingLocation)); //TODO: Add unique url identifier
			byte buf[]=new byte[1024];
			int len;
			while((len=inputStream.read(buf))>0)
				out.write(buf,0,len);
			out.close();
			inputStream.close();
			result = true;
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			System.err.println("Error occured at url = " + url);
			e.printStackTrace();
			return result;
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			System.err.println("Error occured at url = " + url);
			e.printStackTrace();
			return result;
		}catch (Exception e) {
			System.err.println("Unknown error: " + e.getMessage());
			System.err.println("Error occured at url = " + url);
			e.printStackTrace();
			return result;
		} finally {
			// Release the connection.
			method.releaseConnection();
		}  
		return result;
	}
	
	public static String getRedirectLocationFile (String url) {
		String returnLocation = "error";
		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		// Create a method instance.
		GetMethod method = null;
		try {
			url= url.replaceAll(" ","%20").replace("[","%5B").replace("]", "%5D").replace("{","%7B").replace("}", "%7D");
			method = new GetMethod(url);
			// Provide custom retry handler is necessary
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					new DefaultHttpMethodRetryHandler(3, false));
			// Execute the method.
			method.setFollowRedirects(false);
			int statusCode = client.executeMethod(method);
		
			if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY ) {
				Header locationHeader = method.getResponseHeader("location");
				returnLocation = (locationHeader.getValue());
			}
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			System.err.println("Error occured at url = " + url);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Fatal transport error: " + e.getMessage());
			System.err.println("Error occured at url = " + url);
			e.printStackTrace();
		}catch (Exception e) {
			System.err.println("Unknown error: " + e.getMessage());
			System.err.println("Error occured at url = " + url);
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}  
		return returnLocation;
	}

	public static String getResponse (String url) {
		StringBuffer sb = new StringBuffer();

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		// Create a method instance.
		url= url.replaceAll(" ","%20").replace("[","%5B").replace("]", "%5D").replace("{","%7B").replace("}", "%7D");
		GetMethod method = null;
		try {
			method = new GetMethod(url);
			// Provide custom retry handler is necessary
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
					new DefaultHttpMethodRetryHandler(3, false));
			method.setFollowRedirects(false);
			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY ) {
				Header locationHeader = method.getResponseHeader("location");
				String returnLocation = (locationHeader.getValue());
				if (returnLocation.endsWith(".mp3") || returnLocation.endsWith(".MP3"))
					return returnLocation;
			}
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			// Read the response body.
			InputStream inputStream = method.getResponseBodyAsStream();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			inputStream.close()	;
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			System.err.println("Error occured at url = " + url);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error occured at url = " + url);
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		}  catch (Exception e) {
			System.err.println("Error occured at url = " + url);
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		}finally {
			// Release the connection.
			if (method!=null)
				method.releaseConnection();
		}  
		//	return returnString;
		return sb.toString();
	}
	
	public String getResponseSecureSolr (String url) {
		StringBuffer sb = new StringBuffer();

		// Create an instance of HttpClient.
		HttpClient client = new HttpClient();
		Credentials defaultcreds = new UsernamePasswordCredentials("bajao", "13@jeg@");
		client.getState().setCredentials(AuthScope.ANY, defaultcreds);
		// Create a method instance.
		url= url.replaceAll(" ","%20");
		GetMethod method = new GetMethod(url);
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
				new DefaultHttpMethodRetryHandler(3, false));
		try {
			// Execute the method.
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: " + method.getStatusLine());
			}
			// Read the response body.
			InputStream inputStream = method.getResponseBodyAsStream();
			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			inputStream.close()	;
		} catch (HttpException e) {
			System.err.println("Fatal protocol violation: " + e.getMessage());
			System.err.println("Error occured at url = " + url);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error occured at url = " + url);
			System.err.println("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}  
		//	return returnString;
		return sb.toString();
	}
	
	public static void main(String args[]) {
		for (int i=0; i<80000;i++) {
			getRedirectLocationFile("http://mp3hungama.com/music/download.php?song_id="+i);
		}
	}
}
