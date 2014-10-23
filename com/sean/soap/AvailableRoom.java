package com.sean.soap;

public class AvailableRoom {

	public String roomName;
	
	/*
	 *	Each available room object contains an array representing the following:
 	 *		- 1000	=> Room numbers that can be stored, ranging between 0 - 1000
	 *		- 7 	=> Days of the week
	 *	    - 12	=> Hours of the day, ranging between 9am - 8pm
	 *		- 100   => Users that can be stored, with ID's ranging between 0 - 100
	 */
	public int [][][][] timeSlot = new int[1000][7][12][100];
	
	// A separate 3D array is used for timetable purposes
	public int [][][] timetableSlot = new int[1000][7][12];
	
	// All rooms are made available upon initialisation
	public AvailableRoom(String newRoomName) {
		
		this.roomName = newRoomName;
		
		int room = extractRoomNameIntegerValue(roomName);
		
		for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
		
			for (int hourIndex = 0; hourIndex < 12; hourIndex++) {
				
				for (int userIndex = 0; userIndex < 100; userIndex++) {
								
					this.timeSlot[room][dayIndex][hourIndex][userIndex] = 0;
				}
				
				this.timetableSlot[room][dayIndex][hourIndex] = 0;
			}
		}
	}
	
	// Associate the booking of a room with a user and initialise value to 1 to indicate room is now booked
	public boolean userOfBookedRoom(String room, int dayOfWeek, int timeOfDay, int user) {
		
		int roomNumber = extractRoomNameIntegerValue(room);
		
		if (timeSlot[roomNumber][dayOfWeek][timeOfDay][user] == 1) {
			return true;
		}
		
		return false;
	}
	
		
	// A value of 1 indicates that a room is not available for booking at that specific day and time
	public boolean roomIsAvailable(String newRoomName, int dayOfWeek, int timeOfDay) {

		int room = extractRoomNameIntegerValue(newRoomName);
		
		for (int index = 0; index < 100; index++) {
		
			if (timeSlot[room][dayOfWeek][timeOfDay][index] == 1) {
				return false;
			}
		} 
			
		return true;
	}
	
	// A value of 1 is set to indicate that the room has been booked at that specific day and time.
	public void bookRoom(String newRoomName, int dayOfWeek, int timeOfDay, int user) {
	
		int room = extractRoomNameIntegerValue(newRoomName);
		this.timeSlot[room][dayOfWeek][timeOfDay][user] = 1;
		this.timetableSlot[room][dayOfWeek][timeOfDay] = 1;
	}
	
	// Cancel the booking of a room upon the request of a user
	public void cancelRoomBooking(String newRoomName, int dayOfWeek, int timeOfDay) {
		
		int room = extractRoomNameIntegerValue(newRoomName);

		for (int index = 0; index < 100; index++) {
		
			this.timeSlot[room][dayOfWeek][timeOfDay][index] = 0;
			this.timetableSlot[room][dayOfWeek][timeOfDay] = 0;
		}
	}
	
	// Strips all characters from string name and returns name in integer format.
	private int extractRoomNameIntegerValue(String newRoomName) {
	
		return Integer.parseInt(newRoomName.replaceAll("[^0-9]", ""));
	}
}
