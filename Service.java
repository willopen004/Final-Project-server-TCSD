package Drone_System;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Service {
	
	// creates the drones
	static void create_drones (LinkedList<Drone> available_drones)
	{ 
		Drone drone1 = new Drone(1, true, false, 100, 180); 
		Drone drone2 = new Drone(2, true, false, 100, 180);
		Drone drone3 = new Drone(3, true, false, 100, 180);
		Drone drone4 = new Drone(4, true, false, 100, 180);
		Drone drone5 = new Drone(5, true, false, 100, 180);
		available_drones.addFirst(drone5);
		available_drones.addFirst(drone4);
		available_drones.addFirst(drone3);
		available_drones.addFirst(drone2);
		available_drones.addFirst(drone1);		
	}	
	
	@SuppressWarnings("static-access")
	static void check_drone_timer (int my_drone_index, LinkedList<Drone> busy_drones, LinkedList<Drone> lost_drones, Drone my_drone) throws IOException
	{
		if(my_drone.my_timer.is_over)
		{	
			Drone drone = my_drone;
			String string_drone_lost = "your drone is lost!" + "-" + my_drone.getMy_order().getMy_subscriber() +
					"-" + my_drone.getMy_order().getDes_subscriber();
			Service.Send_to_client(string_drone_lost, my_drone.getMy_order().getMy_subscriber(), System_Control.clients);
			Service.Send_to_client(string_drone_lost, my_drone.getMy_order().getDes_subscriber(), System_Control.clients);
			busy_drones.remove(my_drone_index);
			lost_drones.add(drone);
		}
	}
	
	static Subscriber find_subscriber (ArrayList<Subscriber> subscribers, String Sub_id)
	{
		Subscriber my_subscriber = null;
		for(Subscriber subscriber : subscribers)
		{
			if (subscriber.getId().equals(Sub_id))
			{
				my_subscriber = subscriber;
			}
		}			
		return my_subscriber;
	}
		
	public static Subscriber Create_Subscriber(String[] Array_of_split_String, ArrayList<Subscriber> subscribers)
	{
		Subscriber Subscriber = new Subscriber();
		Subscriber.set_personal_details(Array_of_split_String);
		Subscriber.set_address_details(Array_of_split_String);
		Subscriber.set_payment_details(Array_of_split_String);
		subscribers.add(Subscriber);
		return Subscriber;
	}
	static public void Send_to_client (String message, String subscriber_index, HashMap<String, Socket> clients) throws IOException
	{
		Socket Des_client = System_Control.clients.get(subscriber_index);
		final PrintStream my_to_Server_stream = new PrintStream(Des_client.getOutputStream());
		my_to_Server_stream.println(message);
	}
	
	static public String Message_from_client (String Client_id, HashMap<String, Socket> clients) throws IOException
	{
		Socket Des_client = clients.get(Client_id);
		DataInputStream my_from_Server_stream = new DataInputStream(Des_client.getInputStream());
		String my_string_from_server = my_from_Server_stream.readLine();
		return my_string_from_server;
	}
	
	static public Subscriber Find_sub_by_id(ArrayList<Subscriber> subscribers, String Sub_id)
	{
		Subscriber my_subscriber = null;
		for(Subscriber subscriber : subscribers)
		{
			if(subscriber.getId().equals(Sub_id))
			{
				System.out.println(subscriber.toString());
				my_subscriber = subscriber;
			}
		}
		return my_subscriber;
	}
	
	static public Drone Find_drone_by_id(LinkedList<Drone> busy_drones, String Drone_id)
	{
		Drone my_drone = null;
		for(Drone drone : busy_drones)
		{
			if(drone.getSerial_num() == Integer.parseInt(Drone_id))
			{
				System.out.println(drone.toString());
				my_drone = drone;
			}
		}
		return my_drone;
	}
	
	static public Boolean exist_sub_by_id(ArrayList<Subscriber> subscribers, String Sub_id)
	{
		for(Subscriber subscriber : subscribers)
		{
			if(subscriber.getId().equals(Sub_id))
			{
				System.out.println(subscriber.toString());
				return true;
			}
		}
		return false;
	}
	
	public void Show_All_Subscribers(ArrayList<Subscriber> subscribers)
	{
		for(Subscriber subscriber : subscribers)
		{
			System.out.println(subscriber.toString());
		}
	}
	
	public static String[] Split_String(String my_String)
	{
		String StringToSplit = my_String;
		String[] Array_of_split_String = StringToSplit.split("-");
		return Array_of_split_String;
	}
}
