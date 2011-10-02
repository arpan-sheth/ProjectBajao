package com.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.content.SongDownloaderThreadManager;

public class SoundDownloaderThreadManagerServlet extends BaseServlet {
private static org.apache.log4j.Logger log = Logger.getLogger(com.servlets.SoundDownloaderThreadManagerServlet.class);
	
	public void service(HttpServletRequest req, HttpServletResponse res)
	throws IOException {
		SongDownloaderThreadManager mgr = new SongDownloaderThreadManager();
		mgr.doStuff();
	}
}
