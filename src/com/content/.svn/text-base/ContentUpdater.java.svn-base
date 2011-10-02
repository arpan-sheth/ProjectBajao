package com.content;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.database.DBConnectionManager;
import com.downloader.GenericHTTPClient;
import com.utility.Utility;

public class ContentUpdater {
	protected DBConnectionManager connMgr = DBConnectionManager.getInstance();
	private static org.apache.log4j.Logger log = Logger.getLogger(com.content.ContentUpdater.class);

	public void doStuff() {
		try {
			FileReader fr = new FileReader("/mp3s");
			BufferedReader br = new BufferedReader(fr);
			String line = "";
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine())!=null) {
				sb.append("(\"").append(line).append("\", 0, \"").append(new Date()).append("\" ),");
			}
			sb.deleteCharAt(sb.length()-1);
			insertMp3(sb.toString());
		}catch (Exception ex) {
			ex.printStackTrace();
		}finally {
			connMgr.release();			
		}
	}

	public void insertMp3(String line) {
		Connection con = connMgr.getConnection("mysql");
		if (con == null) {
			log.error ("ContentUpdater: MySQL Connection more then max available");
			return;
		}
		Statement stmt = null;
		StringBuffer sql = new StringBuffer();
		try {
			sql.append("insert into music.mp3_links (link, status, added_at) values ").append(line);
			stmt = con.createStatement();
			stmt.executeUpdate(sql.toString());
			stmt.close();
			log.info("ContentUpdater: MP3s added ");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		connMgr.freeConnection("mysql", con);
	}

	public void httpInserter(String line) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("(\"").append(line).append("\", 0, \"").append(new Date()).append("\" ),");
			sb.deleteCharAt(sb.length()-1);
			insertMp3(sb.toString());
			log.info("ContentUpdater: inserting " + line + "links_table");
		}catch (Exception ex) {
			log.info("ContentUpdater: inserting " + line + "failed");
			ex.printStackTrace();
		}finally {
			connMgr.release();			
		}
	}
	
	public static void main(String args[]) {
		new ContentUpdater().doStuff();
	}

	public void mp3HungamaUpdater() {
		for (int i=0; i<80000;i++) {
			String mp3_link = GenericHTTPClient.getRedirectLocationFile("http://mp3hungama.com/music/download.php?song_id="+i);
			if (Utility.checkNullString(mp3_link)) {
				if (mp3_link.endsWith("mp3"))
					httpInserter(mp3_link);
			}
		}
	}
}