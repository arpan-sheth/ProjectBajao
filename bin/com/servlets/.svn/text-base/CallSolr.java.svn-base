package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.downloader.GenericHTTPClient;
import com.utility.Utility;

public class CallSolr extends BaseServlet {
	private static org.apache.log4j.Logger log = Logger.getLogger(com.servlets.CallSolr.class);
	public void doPost(HttpServletRequest req, HttpServletResponse res)
	throws IOException {
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		String query = req.getParameter("q");
		query = Utility.checkNullString(query)?query:"empty";
		log.info("CallSolr: Query = " + query);
		int start = Utility.checkForInteger(req.getParameter("start"));
		String response = (new GenericHTTPClient()).getResponseSecureSolr("http://localhost/solr/select?wt=json&indent=on&"+"q="+query+"&start="+start);
		out.println(response);
	}
	public void doGet(HttpServletRequest req, HttpServletResponse res)
	throws IOException {
		doPost (req, res);
	}
}