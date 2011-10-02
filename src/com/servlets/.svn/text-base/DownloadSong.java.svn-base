package com.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.content.SongDownloader;

public class DownloadSong extends BaseServletWithDB{

	private static org.apache.log4j.Logger log = Logger.getLogger(com.servlets.DownloadSong.class);

	public void service(HttpServletRequest req, HttpServletResponse res)
	throws IOException {
		SongDownloader sd = new SongDownloader();
		sd.doStuff();
	}

}