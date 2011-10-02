package com.beans;

import java.util.regex.Pattern;

import org.apache.solr.client.solrj.beans.Field;

//tag_id, album, title, artist, year, bitrate, image (boolean), image_link, track 

public class SongTag {

	@Field
	public String album;
	@Field
	public String title1;
	@Field
	public String artist;
	@Field
	public String year;
	@Field
	public String bitrate;
	@Field
	public String image;
	@Field
	public String track_number;
	@Field
	public String length;
	@Field
	public String location;


	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getAlbum() {
		album = Pattern.compile("@.*com|@.*COM|Papuyaar.com").matcher(album).replaceAll("").trim();
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getTitle() {
		title1 = Pattern.compile("@.*com|@.*COM|Papuyaar.com").matcher(title1).replaceAll("").trim();
		return title1;
	}
	public void setTitle(String title) {
		this.title1 = title;
	}
	public String getArtist() {
		return artist.trim();
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getYear() {
		return year.trim();
	}
	public void setYear(String year) {
		this.year = year.trim();
	}
	public String getBitrate() {
		return bitrate.trim();
	}
	public void setBitrate(String bitrate) {
		this.bitrate = bitrate;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTrack_number() {
		return track_number;
	}
	public void setTrack_number(String track_number) {
		this.track_number = track_number;
	}

}
