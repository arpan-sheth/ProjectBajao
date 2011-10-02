package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.utility.Utility;

public class PlaySong extends BaseServletWithDB{

	private static org.apache.log4j.Logger log = Logger.getLogger(com.servlets.PlaySong.class);
	
	public void service(HttpServletRequest req, HttpServletResponse res)
	throws IOException {

		String song_id = req.getParameter("id");
		song_id = Utility.checkNullString(song_id)?song_id:null;
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		if (song_id==null){
			out.println("Something's wrong with the song request :(. Please try again..");
			log.error ("PlaySong: Invalid Song Id");
			return;			
		}

		Connection con = connMgr.getConnection("mysql");
		if (con == null) {
			out.println("A lot of our friends are enjoying the service (Yay!). But that overloaded our servers (Yikes!) ");
			out.println("Please try again in a bit...");
			log.error ("PlaySong: MySQL Connection more then max available");
			return;
		}
		ResultSet rs = null;
		Statement stmt = null;
		StringBuffer sb = new StringBuffer();
		String title = "" ;
		String location ="";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select title,location from music.tags where tag_id = "+ song_id);
			sb.append( "{\"location\":");
			
			if (rs.next()) {
				title = rs.getString(1);
				location = rs.getString(2);
				sb.append( "\"" + location +"\"");
			}
			sb.append( "}");
			stmt.close();
			rs.close();
			out.print(sb);
			log.info("PlaySong: Playing song_id = " + song_id + " title = " + title);
		}
		catch (SQLException e) {
			e.printStackTrace(out);
		}
		connMgr.freeConnection("mysql", con);
	}
}