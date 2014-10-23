package com.sean.soap;

import java.util.ArrayList;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
public interface BookingInterface {

	// All methods called on client side
	@WebMethod public void createAllRooms();
	
	@WebMethod public int getDayValue(String day);
	@WebMethod public int getTimeValue(String time);
	@WebMethod public int[][][] bookingTimeTable(String room);
	
	@WebMethod public ArrayList<String> getAllRooms();
	@WebMethod public ArrayList<Integer> getAllUsers();
	@WebMethod public ArrayList<String> getNotifications();
	@WebMethod public ArrayList<String> getAllRoomsAndCapacities();	
	
	@WebMethod public String addNewUser();
	@WebMethod public String cancelBooking(String room, int dayOfWeek, int timeOfDay);
	@WebMethod public String checkRoomAvailability(String room, int dayOfWeek, int timeOfDay);
	@WebMethod public String bookAvailableRoom(String room, int dayOfWeek, int timeOfDay, int user);
	@WebMethod public String addUserBookingQueue(String room, int dayOfWeek, int timeOfDay, int user);
}
