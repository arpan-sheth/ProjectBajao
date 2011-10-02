package com.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInit extends HttpServlet {

	public void init() {
		try {
			String prefix =  getServletContext().getRealPath("/");
			String file = getInitParameter("log4j.properties");
			// if the log4j-init-file is not set, then no point in trying
			if(file != null) {
				PropertyConfigurator.configure(prefix+file);
				System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				System.out.println("Logger Initialized at [" + prefix+file +"]" );
				System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws IOException {

	}
}