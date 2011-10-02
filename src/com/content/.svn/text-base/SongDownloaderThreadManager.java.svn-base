package com.content;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.database.DBConnectionManager;

public class SongDownloaderThreadManager {

	protected DBConnectionManager connMgr = DBConnectionManager.getInstance();
	private static org.apache.log4j.Logger log = Logger.getLogger(com.content.SongDownloaderThreadManager.class);
	public static Vector<downloadObject> vector  = new Vector <downloadObject>();
	private static final int NUMBER_OF_THREADS =1;
	public  void doStuff(){
		try {
			Connection con = connMgr.getConnection("mysql");
			if (con == null) {
				log.error ("SongDownloaderThreadManager: Error getting MySQL Connection");
				return;
			}
			ResultSet rs = null;
			Statement stmt = null;
			try {
				stmt = con.createStatement();
				rs = stmt.executeQuery("select link, id from music.mp3_links where status = 0 ");
				while (rs.next()) {
					downloadObject obj  = new downloadObject();
					obj.store_link = new Date().getTime()+".mp3";
					obj.download_link = rs.getString("link");
					obj.id = rs.getString("id");
					vector.add(obj);
				}
				int i =0;
				while (i<NUMBER_OF_THREADS){
					SongDownloaderThread sdthread = new SongDownloaderThread();
					new Thread(sdthread).start();
					i++;
				}
			}
			catch (SQLException e) {
				log.error("SongDownloaderThreadManager: "+e);
				connMgr.freeConnection("mysql", con);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static synchronized downloadObject selectAndRemove(){
		downloadObject obj = vector.firstElement();
		if (obj!=null) 
			vector.remove(0);
		return obj;
	}
	public static void main(String args[]){
		new SongDownloaderThreadManager().doStuff();
	}
}
