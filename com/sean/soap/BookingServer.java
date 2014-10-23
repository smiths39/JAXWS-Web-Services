package com.sean.soap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import javax.jws.WebMethod;
import javax.jws.WebService;

// The name of the service endpoint interface
@WebService(endpointInterface = "com.sean.soap.BookingInterface")
public class BookingServer implements BookingInterface {

	// A data structure used to store users in a booking queue
	public Queue<Room> bookingQueue = new LinkedList<Room>();
	
	// A list of users and notifications generated within application
	public ArrayList<String> notifications = new ArrayList<String>();
	public ArrayList<Integer> users = new ArrayList<Integer>();

	public AvailableRoom [] availableRoom = new AvailableRoom[1000];
	public String [] availableRoomList = new String[1000];
	public String [] capacityRoomList = new String[1000];
		
	// Read in rooms and capacities specified in Room_Capacity text file
	public void initialiseRooms() {
		
		String bookingRecord = null, room = null, capacity=null;
		int counter = 0;
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("Room_Capacity.txt"));
			
			while ((bookingRecord = bufferedReader.readLine()) != null) {
				
				room = bookingRecord.substring(0, bookingRecord.lastIndexOf(" ", bookingRecord.length()));
				capacity = bookingRecord.substring(4, bookingRecord.length());
	
				// Store rooms and capacities read in from file
				availableRoom[counter] = new AvailableRoom(room);
				availableRoomList[counter] = room;
				capacityRoomList[counter] = capacity;
				counter++;
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Read in list of rooms specified in Room_Capacity text file and check with stored rooms in application
	public boolean checkRoomExists(String room) {
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("Room_Capacity.txt"));	
			Scanner scanner = new Scanner(bufferedReader);
	
			while (scanner.hasNext()) {
				
				int trimRoom = Integer.parseInt(room.replaceAll("[^0-9]", ""));
				int trimLine = Integer.parseInt(scanner.next().replaceAll("[^0-9]", ""));				

				if (trimRoom == trimLine) {
					return true;
				}
			}
			bufferedReader.close();
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	// Compare room with all rooms currently stored
	private int compareExistingRooms(String room) {
	
		for (int index = 0; index < availableRoom.length; index++) {
			
			if (availableRoom[index].equals(room) == false) {
				
				return index;
			}
		}
		
		return -1;
	}
	
	// Retrieve integer representing day of the week
	@Override
	@WebMethod
	public int getDayValue(String day) {
		
		if (day.equals("Monday")) {
			return 0;
		} else if (day.equals("Tuesday")) {
			return 1;
		} else if (day.equals("Wednesday")) {
			return 2;
		} else if (day.equals("Thursday")) {
			return 3;
		} else if (day.equals("Friday")) {
			return 4;
		} else if (day.equals("Saturday")) {
			return 5;
		} else if (day.equals("Sunday")) {
			return 6;
		} 
		
		return -1;
	}

	// Retrieve integer representing time of the day
	@Override
	@WebMethod
	public int getTimeValue(String time) {
		
		int timeValue = 0;
		
		if (time.length() == 3) {
			timeValue = Integer.parseInt(time.substring(0, 1));
		} else {
			timeValue = Integer.parseInt(time.substring(0, 2));
		}
		
		if (timeValue == 9) {
			return 0;
		} else if (timeValue == 10) {
			return 1;
		} else if (timeValue == 11) {
			return 2;
		} else if (timeValue == 12) {
			return 3;
		} else if (timeValue == 1) {
			return 4;
		} else if (timeValue == 2) {
			return 5;
		} else if (timeValue == 3) {
			return 6;
		} else if (timeValue == 4) {
			return 7;
		} else if (timeValue == 5) {
			return 8;
		} else if (timeValue == 6) {
			return 9;
		} else if (timeValue == 7) {
			return 10;
		} else if (timeValue == 8) {
			return 11;
		} 
		
		return -1;
	}

		
	// Returns a 3D array of a room's timetable
	@Override
	@WebMethod
	public int[][][] bookingTimeTable(String room) {
		
		for (int index = 0; index < availableRoom.length; index++) {
			
			if (availableRoom[index].roomName.equals(room) == false) {
	
				return availableRoom[index].timetableSlot;
			} 
		}
		
		return availableRoom[availableRoom.length - 1].timetableSlot;
	}

	// Generating a new user to the application
	@Override
	@WebMethod
	public String addNewUser() {
	
		int lastItem;
		
		// If no users have yet to be created, set user name as '1'
		// Else set user name as a value incremented from previous generated user
		if (users.isEmpty()) { 
			lastItem = 1;
		} else {
			lastItem = users.size() + 1;
		}

		users.add(lastItem);
		
		return "User successfully added";
	}
	
	// Cancel a booking of a room obtained by a user
	@Override
	@WebMethod
	public String cancelBooking(String room, int dayOfWeek, int timeOfDay) {
		
		int roomNumber = compareExistingRooms(room);
		
		if (checkRoomExists(room) == false) {
			return "Room does not exist";
		}
		
		// Check if room is currently booked
		if (availableRoom[roomNumber].roomIsAvailable(room, dayOfWeek, timeOfDay)) {
			
			return "Room is not currently booked";
		} else {
			
			// Cancel booking
			availableRoom[roomNumber].cancelRoomBooking(room, dayOfWeek, timeOfDay);

			for (Room detail : bookingQueue) {
				
				// Locate supplied details in booking queue to discover new owner of room
				if (detail.getRoom().equals(room) && detail.getDay() == dayOfWeek && detail.getTime() == timeOfDay) {

					// Automatically book room for previously queued user
					availableRoom[roomNumber].bookRoom(room, dayOfWeek, timeOfDay, detail.getID());
					
					// Store notification message
					notifications.add("Room " + room + " has been cancelled. User " + detail.getID() + " now owns the room.");
					
					// Remove user is now in possession of booked room
					bookingQueue.remove();
					
					return "User " + detail.getID() + " now owns the room";
				}
			}
			
			return "Booking has been cancelled"; 
		}
	}
	
	// Add all stored notifications to returned list
	@Override
	@WebMethod
	public ArrayList<String> getNotifications() {
		
		ArrayList<String> notificationsList = new ArrayList<String>();
		
		for (int index = 0; index < notifications.size(); index++) {
			notificationsList.add(notifications.get(index));
		}

		return notificationsList;
	}
	
	@Override
	@WebMethod
	public String addUserBookingQueue(String room, int dayOfWeek, int timeOfDay, int user) {
	
		// Create new room object of with booking details
		Room bookingDetails = new Room(user, room, dayOfWeek, timeOfDay);
		int roomNumber = compareExistingRooms(room);
		
		for (Room detail : bookingQueue) {
			
			// Check if user is already stored in queue
			if (detail.getID() == user && detail.getRoom().equals(room) && detail.getDay() == dayOfWeek && detail.getTime() == timeOfDay) {
				return "User " + user + " already in waiting queue";
			}
		}
		
		// Add user who is requesting a currently booked room to the queue
		if (!availableRoom[roomNumber].userOfBookedRoom(room, dayOfWeek, timeOfDay, user)) {
			
			bookingQueue.add(bookingDetails);		
			return "User " + user + " added to waiting queue";
		}
		
		return "User " + user + " currently owns the room";
	}
	
	// Book room for a specified user
	@Override
	@WebMethod
	public String bookAvailableRoom(String room, int dayOfWeek, int timeOfDay, int user) {
		
		int roomNumber = compareExistingRooms(room);
		
		if (checkRoomExists(room) == false) {
			return "Room does not exist";
		}
		
		// If room is not currently booked, book room for user
		if (availableRoom[roomNumber].roomIsAvailable(room, dayOfWeek, timeOfDay)) {
			
			availableRoom[roomNumber].bookRoom(room, dayOfWeek, timeOfDay, user);
			return "Room has been booked";
		} else {
			
			return "Room is already booked"; 
		}
	}
	

	// Ensure room is available for booking
	@Override
	@WebMethod
	public String checkRoomAvailability(String room, int dayOfWeek, int timeOfDay) {
		
		int roomNumber = compareExistingRooms(room);
		
		if (checkRoomExists(room) == false) {
			return "Room does not exist";
		}
		
		if (availableRoom[roomNumber].roomIsAvailable(room, dayOfWeek, timeOfDay)) {
			return "Room can be booked";
		} else {
			return "Room cannot be booked";
		}
	}
	
	// Initialise all rooms to be used within applcation
	@Override
	@WebMethod
	public void createAllRooms() {
		initialiseRooms();
	}
	
	
	// Retrieve all rooms and capacities read in from Room_Capacity text file
	@Override
	@WebMethod
	public ArrayList<String> getAllRoomsAndCapacities() {
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (int index = 0; index < 100; index++) {
			
			// If not more rooms are stored, finish
			if (availableRoomList[index] == null) {
				break;
			}
			list.add(availableRoomList[index]);
			list.add(capacityRoomList[index]);
		}	
		
		return list;
	}
	
	// Retrieve all rooms read in from Room_Capacity text file
	@Override
	@WebMethod
	public ArrayList<String> getAllRooms() {
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (int index = 0; index < 100; index++) {

			// If not more rooms are stored, finish
			if (availableRoomList[index] == null) {
				break;
			}
			list.add(availableRoomList[index]);
		}	
		
		return list;
	}

	// Retrieve all users currently generated within application
	@Override
	@WebMethod
	public ArrayList<Integer> getAllUsers() {
		
		ArrayList<Integer> list = new ArrayList<Integer>();
		
		for (int index = 0; index < users.size(); index++) {
			
			list.add(users.get(index));
		}
		
		return list;
	}
}