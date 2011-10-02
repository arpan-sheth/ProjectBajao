package com.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.content.ContentUpdater;

public class UpdateDB extends BaseServletWithDB{

	private static org.apache.log4j.Logger log = Logger.getLogger(com.servlets.UpdateDB.class);
	
	public void service(HttpServletRequest req, HttpServletResponse res)
	throws IOException {
		ContentUpdater cu = new ContentUpdater();
		String site = req.getParameter("site");
		if (site!=null) {
			if (site.equals("mp3Hungama")) {
				cu.mp3HungamaUpdater();
			}
			if (site.equals("fmw11")) {
				cu.doStuff();
			}
		}
	}

}
