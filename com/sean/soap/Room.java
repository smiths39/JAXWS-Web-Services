package com.sean.soap;

public class Room {

	private String room;
	private int userID, day, time;
	
	public Room(int userID, String room, int day, int time) {
		
		this.userID = userID;
		this.room = room;
		this.day = day;
		this.time = time;
	}
	
	// Getter methods
	public int getID() { return userID; }
	public int getDay() { return day; }
	public int getTime() { return time; }
	public String getRoom() { return room; }
}
