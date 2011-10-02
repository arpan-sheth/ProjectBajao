package com.content;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.database.DBConnectionManager;
import com.downloader.GenericHTTPClient;

public class SongDownloaderThread implements Runnable {

	protected DBConnectionManager connMgr = DBConnectionManager.getInstance();
	private static org.apache.log4j.Logger log = Logger.getLogger(com.content.SongDownloaderThread.class);
	Connection con = connMgr.getConnection("mysql");
	String id = "";
	String link = null;
	String file_name = null;

	public  boolean doStuff(){
		boolean result = false;
		try {
			Statement stmt_update = null;
			try {
				stmt_update = con.createStatement();
				log.info("Thread " + Thread.currentThread().getId() + " is downloading song = " + link+"");
				if (GenericHTTPClient.getFile(link, "/tomcat/webapps/songs_all/"+file_name))
					result =true;
			}catch (Exception ex){
				log.error(ex);
				log.error("SongDownloaderThread: Error downloading song = "+ link + " id = " + id);	
				stmt_update.executeUpdate("update mp3_links set status = 1 where id = "  + id);
			}
			if (result){
				stmt_update.executeUpdate("update mp3_links set status = 2, file_name = '"+ file_name + "' where id = "  + id);
				log.info("SongDownloaderThread: File Name = " + file_name +" Downloaded song = "+ link + " id = " + id);
			}
		}catch (SQLException e) {
			log.error("SongDownloaderThread: "+e);
		}
		return result;
	}
	
	public void run() {
		System.out.println("New thread generated: " + Thread.currentThread().getId());

		if (con == null) {
			log.error ("SongDownloaderThread: Error creating MySQL Connection");
			return;
		}
		while (SongDownloaderThreadManager.vector.size()>0){
			downloadObject obj = SongDownloaderThreadManager.selectAndRemove();
			id = obj.id;
			link = obj.download_link;
			file_name = Thread.currentThread().getId()+"_"+obj.store_link;
			doStuff();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}