package com.utility;

import java.net.MalformedURLException;
import java.net.URL;

public class Utility {

	public static boolean checkNullString(String string){
		if (string==null || string.length()==0 || string.equals("")|| string=="")
			return false;
		else
			return true;
	}

	public static int checkForInteger(String integer){
		try {
			int return_int = Integer.parseInt(integer);
			return return_int;
		}catch (Exception ex){
			return 0;
		}
	}


	public static URL urlValidate(String url){
		URL returnURL = null;
		try {
			returnURL = new URL(url);
		} catch (MalformedURLException e1) {
			System.out.println("ERROR: invalid URL " + url);
		}
		return returnURL;
	}
}
