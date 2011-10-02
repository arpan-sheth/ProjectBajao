package com.content;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.database.DBConnectionManager;
import com.downloader.GenericHTTPClient;

public class SongDownloader {

	protected DBConnectionManager connMgr = DBConnectionManager.getInstance();
	private static org.apache.log4j.Logger log = Logger.getLogger(com.content.SongDownloader.class);

	public  void doStuff(){
		try {
			Connection con = connMgr.getConnection("mysql");
			if (con == null) {
				log.error ("SongDownloader: MySQL Connection more then max available");
				return;
			}
			ResultSet rs = null;
			Statement stmt = null;
			Statement stmt_update = null;
			try {
				stmt = con.createStatement();
				stmt_update = con.createStatement();
				rs = stmt.executeQuery("select * from music.mp3_links where status = 0 ");
				String title = "";
				String id="";
				while (rs.next()) {
					String file_name = new Date().getTime()+".mp3";
					boolean result = false;
					try{
						id = rs.getString("id");
						title = rs.getString("link");
						
						if (GenericHTTPClient.getFile(title, "/tomcat/webapps/songs_all/"+file_name))
							result =true;
					}catch (Exception ex){
						log.error(ex);
						log.error("SongDownloader: Error downloading song = "+ title + " id = " + id);	
						stmt_update.executeUpdate("update mp3_links set status = 1 where id = "  + id);
					}
					if (result){
						stmt_update.executeUpdate("update mp3_links set status = 2, file_name = '"+ file_name + "' where id = "  + id);
						log.info("SongDownloader: Downloaded song = "+ title + " id = " + id);
					}
				}
			}
			catch (SQLException e) {
				log.error("SongDownloader: "+e);
			}
			connMgr.freeConnection("mysql", con);
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String args[]){
		new SongDownloader().doStuff();
	}

}
