package com.content;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import com.beans.SongTag;
import com.database.DBConnectionManager;


public class SongTagger {
	protected DBConnectionManager connMgr = DBConnectionManager.getInstance();
	private static org.apache.log4j.Logger log = Logger.getLogger(com.content.SongTagger.class);
	public static final String WEBAPP_FOLDER = "/tomcat/webapps/";
	private Connection con = null;

	/*
	 * This method reads all the files inside the folder and gets the id3tags and 
	 * inserts into tags table
	 */

	public void bulkTagReader(String songFolderPath){
		File dir = new File(songFolderPath);
		if (!dir.isDirectory()) {
			tagReader(dir.getAbsolutePath());
		}else 
			for (File file:dir.listFiles()) {
				bulkTagReader(file.getAbsolutePath());
			}	
	}
	/*
	 * This method reads the file and gets the id3tags and 
	 * inserts into tags table
	 */
	public void tagReader(String songPath){
		if (!songPath.endsWith("mp3")){
			return;
		}

		//Reads the mp3 file and goes to the tagger to find id3 tags
		try{
			SongTag songTag = getID3TagsAudioTaggerOneSong(songPath);
			if (songTag!=null){
				String location = songPath.replace(WEBAPP_FOLDER, "");
				songTag.setLocation(location); 

				//Inserts the tag inside the db for each file (I know its ugly, we have to find a better way to do insertion)
				StringBuffer insertString = new StringBuffer( "Insert into tags (album, title, artist, year, bitrate, image_link, " +
				"track_number, length, location, image) values( \"").
				append(songTag.getAlbum()).append("\",\"").
				append(songTag.getTitle()).append("\",\"").
				append(songTag.getArtist()).append("\",\"").
				append(songTag.getYear()).append("\",\"").
				append(songTag.getBitrate()).append("\",\"").
				append("null").append("\",\"").
				append(songTag.getTrack_number()).append("\",\"").
				append(songTag.getLength()).append("\",\"").
				append(songTag.getLocation()).append("\",\"").
				append(songTag.getImage()).append("\")");
				log.info("SongTagger: Query = " + insertString);
				Statement statement = con.createStatement();
				statement.executeUpdate(insertString.toString());
				statement.close();			
				log.info("SongTagger: Added " + location + " tags into the tags table");
			}
		}catch (Exception ex){
			System.out.println();
			ex.printStackTrace();
			log.error("ERROR: SongTagger:  = " + ex + " Problem was in " + songPath);
			return;
		}
	}

	public SongTag getID3TagsAudioTaggerOneSong(String songPath) {
		SongTag taggerObject = null;
		try {
			taggerObject = new SongTag();
			MP3File f = (MP3File)AudioFileIO.read(new File(songPath));
			Tag tag        = f.getTag();
			MP3AudioHeader mp3AudioHeader = (MP3AudioHeader) f.getAudioHeader();
			taggerObject.setAlbum(tag.getFirst(FieldKey.ALBUM));
			taggerObject.setArtist( tag.getFirst(FieldKey.ARTIST));
			taggerObject.setBitrate( mp3AudioHeader.getBitRate());
			taggerObject.setLength( mp3AudioHeader.getTrackLengthAsString());
			taggerObject.setTitle( tag.getFirst(FieldKey.TITLE));
			taggerObject.setYear(tag.getFirst(FieldKey.YEAR));
			taggerObject.setTrack_number( tag.getFirst(FieldKey.TRACK));

		} catch (Exception e) {
			log.info("ERROR:SongTagger: Error adding song" + songPath+":" +e);
		}
		return taggerObject;
	}

	public  void doStuff(){
		try {
			con = connMgr.getConnection("mysql");
			if (con == null) {
				log.info("ERROR:SongTagger: MySQL Connection more then max available");
				return;
			}
		}catch (Exception ex){
			log.info("ERROR:SongTagger: Error initializing mysql connection" );
		}
		bulkTagReader(WEBAPP_FOLDER+"songs_all/");
	}

	public static void main(String args[]){
		SongTagger obj = new SongTagger();
		obj.doStuff();
		obj.bulkTagReader(WEBAPP_FOLDER+"songs_all/");
	}

}
