package com.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import com.content.SongTagger;

public class InsertSong extends BaseServletWithDB{

	private static org.apache.log4j.Logger log = Logger.getLogger(InsertSong.class);
	
	public void service(HttpServletRequest req, HttpServletResponse res)
	throws IOException {
		SongTagger st = new SongTagger();
		st.doStuff();
	}

}
