package Drone_System;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Calendar;



public class System_Control {
	static ArrayList<Subscriber> subscribers = new ArrayList<Subscriber>();
	static LinkedList<Drone> available_drones = new LinkedList<Drone>();
	static LinkedList<Drone> busy_drones = new LinkedList<Drone>();
	static LinkedList<Drone> lost_drones = new LinkedList<Drone>();
	static HashMap<String, Socket> clients = new HashMap<String, Socket>();	
	static int drone_index = 0;


	System_Control() throws IOException, InterruptedException
	{
		Service.create_drones(available_drones);
	}
	void Server_managment() throws IOException, InterruptedException
	{
		ServerSocket myServer = new ServerSocket(7000);
		System.out.println("Server is waiting...");
		while(true)
		{
			final Socket client = myServer.accept();		
			new Thread(new Runnable()
					{
				@SuppressWarnings("static-access")
				public void run ()
				{
					String address = "";
					try
					{
						Order my_order_to_upgrade = null;
						String[] split_string;
						String my_subscriber_id = null;
						String my_order_num = null;			
						String Sender_id = null;
						address = client.getInetAddress() + " " + client.getPort();
						System.out.println("Client connected..." + address);
						final DataInputStream my_from_Server_stream = new DataInputStream(client.getInputStream());
						final PrintStream my_to_Server_stream = new PrintStream(client.getOutputStream());
						my_to_Server_stream.println("Welcome to our server");
						String my_string_from_server = "";
						my_string_from_server = my_from_Server_stream.readLine();
						System.out.println("Client " + address + "said: " + my_string_from_server);
						do
						{
							split_string = Service.Split_String(my_string_from_server);				
							switch(split_string[0])
							{
							
							case ("new_subscriber"):
							{	
								connClass.subscribers_to_database(Service.Create_Subscriber(split_string, subscribers));
								my_to_Server_stream.println("new subscriber created!");
								System.out.println("new subscriber created!");
								break;
							}
							
							// looking for the client in the DS and the DB
							case ("existing_subscriber"):
							{
								boolean found_DS = Service.exist_sub_by_id(subscribers, split_string[1]);
								boolean found_DB = connClass.exist_sub_by_id(split_string[1]);
								
								// if the client is in the DS
								if(found_DS && found_DB)
								{
									Subscriber my_subscriber = Service.Find_sub_by_id(subscribers, split_string[1]);
									String password = split_string[2];										
									while(!password.equals(my_subscriber.getPassword()))
									{
										String message = "wrong password!";
										my_to_Server_stream.println(message); 
										password = my_from_Server_stream.readLine();
										split_string = Service.Split_String(password);	
										password = split_string[2];
									}
									found_DS = true;
									found_DB = true;
									my_subscriber_id = my_subscriber.getId();
									clients.put(my_subscriber.getId(), client);
									int deliveries_amount = connClass.fetch_deliveries(my_subscriber);
									if (deliveries_amount <= 0)
									{
										String String_to_send = "subscriber found but you have no more deliveries left!" + "-" + split_string[1]+ "-" + my_subscriber.getFirst_name() + 
												"-" + my_subscriber.getLast_name();
										my_to_Server_stream.println(String_to_send);
										break;
									}
									else
									{
										String String_to_send = "subscriber found" + "-" + split_string[1]+ "-" + my_subscriber.getFirst_name() + 
												"-" + my_subscriber.getLast_name() + "-" + deliveries_amount;
										my_to_Server_stream.println(String_to_send); 
										clients.put(my_subscriber.getId(), client);
										System.out.println("sending to client: subscriber found");	
										break;
									}
								}
								
								// if the client is in the DB
								if(found_DB && !found_DS)
								{
									Subscriber subscriber = connClass.database_to_object(split_string);
									Calendar calendar = Calendar.getInstance();	
									clients.put(subscriber.getId(), client);
									String password = split_string[2];
									while(!password.equals(subscriber.getPassword()))
									{						
										String message = "wrong password!";
										my_to_Server_stream.println(message); 
										password = my_from_Server_stream.readLine();
										split_string = Service.Split_String(password);	
										password = split_string[2];
									}
									found_DB = true;	
									found_DS = true;
									subscribers.add(subscriber);
									my_subscriber_id = subscriber.getId();
									int deliveries_amount = connClass.fetch_deliveries(subscriber);
									if (deliveries_amount <= 0)
									{
										String String_to_send = "subscriber found but you have no more deliveries left!" + "-" + split_string[1]+ "-" + subscriber.getFirst_name() + 
												"-" + subscriber.getLast_name();
										my_to_Server_stream.println(String_to_send);
										break;
									}
									if(connClass.date_diff(subscriber) > (calendar.get(calendar.getMaximum(Calendar.MONTH))))
									{
										String String_to_send = "subscriber found but you have no more deliveries left!" + "-" + split_string[1]+ "-" + subscriber.getFirst_name() + 
												"-" + subscriber.getLast_name();
										my_to_Server_stream.println(String_to_send);
										break;
									}
									else
									{
										String String_to_send = "subscriber found" + "-" + split_string[1] + "-" + subscriber.getFirst_name() + 
												"-" + subscriber.getLast_name()+ "-" + deliveries_amount;
										my_to_Server_stream.println(String_to_send); 
										System.out.println("sending to client: subscriber found");		
									}
								}
								
								// if the client does not exist
								else if(!found_DS && !found_DB)
								{
									my_to_Server_stream.println("subscriber not found!" + "-" + split_string[1]);
								}
								break;
							}
							
							// if the client makes a new order 
							case ("New_Order"):
							{
								Subscriber subscriber = Service.Find_sub_by_id(subscribers, my_subscriber_id);
								Subscriber des_subscriber = Service.Find_sub_by_id(subscribers, split_string[2]);
								
								// if destination subscriber does not exist
								if (des_subscriber == null)
								{
									String string_to_send = "you can't send that package" + "-" + " to " + "-" +
									split_string[2] + "-" + " because there is no such client!";
									Service.Send_to_client(string_to_send, subscriber.getId(), clients);
									break;
								}
								String string_to_send = null;
								Drone my_drone = null;
								System.out.println(des_subscriber.toString());
								System.out.println("Client said: " + split_string[0]); 
								System.out.println(split_string);
								int deliveries_amount = connClass.fetch_deliveries(subscriber);
								
								// if the sender does not have any more deliveries 
								if(deliveries_amount <= 0)
								{	
									System.out.println("im here!!!");
									string_to_send = "subscriber found but you have no more deliveries left!" + "-" +
											subscriber.getId() + "-" + subscriber.getFirst_name() + "-" + subscriber.getLast_name();
									my_to_Server_stream.println(string_to_send);
									break;
								}
								
								// if all is good
								else
								{
									Order My_Order = subscriber.Create_Order(split_string);
									my_order_to_upgrade = My_Order;
									subscriber.setDeliveries_amount(subscriber.getDeliveries_amount() - 1);
									connClass.deliveries_amount_update_DS_to_DB(subscriber);					
									my_drone = System_Control.available_drones.getFirst();
									System_Control.available_drones.pop();
									My_Order.setMy_drone(my_drone.getSerial_num());
									my_drone.setMy_order(My_Order);
									my_drone.setLoaded(true);
									my_drone.setAvailable(false);
									System_Control.busy_drones.addLast(my_drone);
									string_to_send = "Drone arrived" + "-" + my_drone.getSerial_num() + "-" + des_subscriber.getId() +
											"-" + des_subscriber.getCity() + "-" + des_subscriber.getStreet() + "-" + des_subscriber.getApartment_num()
											+ "-" + des_subscriber.getZip();
									System.out.println(string_to_send);								
									my_to_Server_stream.println(string_to_send);
									my_drone.my_timer.cnt = 10800;
									my_drone.drone_timer();	
									System.out.println("sending to client: Drone arrived " + my_drone.getSerial_num());
									System.out.println("returned from subscriber!");
									break;
								}
							}
							
							// if the sender need to make a new payment for deliveries package
							case ("new payment"):
							{
								Subscriber my_subscriber = Service.Find_sub_by_id(subscribers, my_subscriber_id);
								my_subscriber.setPackage_pay(split_string[1]);
								switch(split_string[1])
								{

								case("99$ : 50 deliveries"):
								{
									my_subscriber.setDeliveries_amount(50);
									break;
								}
								case("179$ : 150 deliveries"):
								{
									my_subscriber.setDeliveries_amount(150);
									break;
								}
								case("monthly 150$ : 100 deliveries"):
								{
								my_subscriber.setDeliveries_amount(100);
								break;
								}
								}
								my_subscriber.setCredit_num(split_string[2]);
								my_subscriber.setCredit_date(split_string[3]);
								my_subscriber.setCredit_back_num(split_string[4]);
								connClass.payment_details_DS_to_DB(my_subscriber);
								String message_to_des = "your payment details has been updated!";
								String String_to_send = message_to_des + "-" + my_subscriber.getId() + "-" + my_subscriber.getFirst_name() + 
										"-" + my_subscriber.getLast_name() + "-" + my_subscriber.getDeliveries_amount();
								System.out.println("you are sending: " + String_to_send);
								Service.Send_to_client(String_to_send, my_subscriber.getId(), clients);
								break;
							}
							
							// finds the destination of the drone sent by the sender client
							case ("drone_destination_id"):
							{
								Service.check_drone_timer(drone_index, System_Control.busy_drones,System_Control.lost_drones, System_Control.busy_drones.getLast());
								Subscriber my_des_subscriber = Service.Find_sub_by_id(subscribers, split_string[1]);
								if (my_des_subscriber == null)
								{
									String string_to_send = "you can't send that package" + "-" + " to " + "-" +
									split_string[2] + "-" + " because there is no such client!";
									Service.Send_to_client(string_to_send, split_string[3], clients);
									break;
								}
								my_order_to_upgrade.setCity_field(split_string[4]);
								my_order_to_upgrade.setStreet_field(split_string[5]);
								my_order_to_upgrade.setApartment_num_field(split_string[6]);
								my_order_to_upgrade.setZip_field(split_string[7]);
								connClass.orders_to_database(my_order_to_upgrade);
								System.out.println("destination subscriber found!");
								for(int i = 0; i < busy_drones.size(); i++)										
								{
									if (busy_drones.get(i).getSerial_num() == Integer.parseInt(split_string[2]))
									{
										System.out.println("drone found!");
										drone_index = i;
										Sender_id = split_string[3];
										System.out.println("sender id: " + Sender_id + ":" + "splitstring: " + split_string[3]);
										System.out.println("reciver id: " + my_des_subscriber.getId() + ":" + "splitstring: " + split_string[1]);
										String message_to_des = "You got a package" + "-" + Sender_id;
										Service.Send_to_client(message_to_des, my_des_subscriber.getId(), clients);
									}
								}
								break;
							}
							
							// in case the client has recived the package from the sender
							case ("package recived!"):
							{
								Drone my_drone = busy_drones.get(drone_index);
								my_drone.my_timer.cancel();
								my_drone.my_timer.cnt = 10;
								busy_drones.remove(drone_index);
								my_drone.setMy_order(null); 
								my_drone.setLoaded(false); 
								my_drone.setAvailable(true);
								available_drones.add(my_drone);
								String string_to_text = "package delivered" + "-" + split_string[2];
								Service.Send_to_client(string_to_text, split_string[1], clients);
								System.out.println("sending to client: package delivered");
								break;
							}
							
							// get all the orders sent by the client from the DB
							case ("all orders sent"):
							{
								{
									String status = "sent";
									System.out.println("sending orders");
									Service.Send_to_client("order sent", split_string[1], clients);
									ArrayList<String> my_orders_sent = connClass.show_orders(split_string, status);
									for(String order : my_orders_sent)
									{
										Service.Send_to_client(order.toString(), split_string[1],clients);						
									}
									Service.Send_to_client("thats all the orders sent", split_string[1], clients);					
								}
								break;
							}
							
							// get all the orders recived by the client from the DB
							case ("all orders recived"):
							{
								{
									String status = "recived";
									System.out.println("sending orders");
									Service.Send_to_client("order recived", split_string[1], clients);
									ArrayList<String> my_orders_recived = connClass.show_orders(split_string, status);
									for(String order : my_orders_recived)
									{
										Service.Send_to_client(order.toString(), split_string[1],clients);						
									}
									Service.Send_to_client("thats all the orders recived", split_string[1], clients);					
								}
								break;
							}	
							
							// send a message from one client to another
							case ("message to client"):
							{
								boolean found = false;
								if (!found)
								{
									String Client_name = null;
									Client_name = Service.Find_sub_by_id(subscribers, split_string[2]).getFirst_name();
									System.out.println("the client name is:" + Client_name);
									found = true;
									String des_id = split_string[1];
									String source_id = split_string[2];
									String message = split_string[3];				
									String String_to_send = "message from user" + "-" + des_id + "-" + source_id +
											"-" + message + "-" + Client_name;
									Service.Send_to_client(String_to_send, des_id, clients);	
									break;
								}									
								
								if (!found)
								{
									String string = "Client for chat not found!";
									my_to_Server_stream.println(string); 
									break;								
								}
								break;
							}	
							
							case ("quit"):
							{
								String string = "goodbye!";
								my_to_Server_stream.println(string); 
								System.out.println("run done!");
								client.close();
								return;
							}
							 }
							
							// waiting for message from client//
							my_string_from_server = my_from_Server_stream.readLine();	
							System.out.println("Client " + my_subscriber_id + " said: " + my_string_from_server);
						
						} while (my_string_from_server != "quit");						
					}
					catch(Exception e)
					{
						System.err.println(e);
					}
					finally {
						try
						{
							client.close();
							System.out.println("Client " + address + " disconnected");
						}
						catch(Exception e)
						{
							System.err.println(e);
						}
					}
				}
					}).start();			
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System_Control System = new System_Control();
		System.Server_managment();
	}

	}

		
	