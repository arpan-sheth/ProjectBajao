package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.utility.Utility;

public class OneSong extends BaseServletWithDB{

	private static org.apache.log4j.Logger log = Logger.getLogger(com.servlets.OneSong.class);

	public void service(HttpServletRequest req, HttpServletResponse res)
	throws IOException {

		String song_id = req.getParameter("id");
		song_id = Utility.checkNullString(song_id)?song_id:null;
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();

		if (song_id==null){
			out.println("Something's wrong with the song request :(. Please try again..");
			log.error ("OneSong: Invalid Song Id");
			return;			
		}

		Connection con = connMgr.getConnection("mysql");
		if (con == null) {
			out.println("A lot of our friends are enjoying the service (Yay!). But that overloaded our servers (Yikes!) ");
			out.println("Please try again in a bit...");
			log.error ("OneSong: MySQL Connection more then max available");
			return;
		}
		ResultSet rs = null;
		ResultSetMetaData md = null;
		Statement stmt = null;
		StringBuffer sb = new StringBuffer();
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from music.tags where tag_id = "+ song_id);
			md = rs.getMetaData();
			sb.append( "{\"responseHeader\":{\"params\":{\"q\":\"\"}},\"response\":{\"docs\":[{");
			String title="";
			while (rs.next()) {
				for (int i = 1; i < md.getColumnCount(); i++) {
					if (md.getColumnName(i).equals("title")){
						title = rs.getString(i);
						sb.append( "\"" + "title1" + "\":\""+ title +"\",");
						continue;
					}
					if (md.getColumnName(i).equals("tag_id")){
						sb.append( "\"" + "id" + "\":\""+ rs.getString(i) +"\",");
						continue;
					}
					sb.append( "\"" + md.getColumnName(i) + "\":\""+rs.getString(i) +"\",");
				}
			}
			sb.deleteCharAt(sb.length()-1);//get rid of last comma
			sb.append( "}]}}");
			stmt.close();
			rs.close();
			out.print(sb);
			log.info("OneSong: song_id = " + song_id + " title = " + title);
		}
		catch (SQLException e) {
			e.printStackTrace(out);
		}
		connMgr.freeConnection("mysql", con);
	}
}
