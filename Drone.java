package Drone_System;

import java.util.Timer;

public class Drone {
	private int serial_num;
	private boolean available;
	private boolean loaded;
	private int battery;
	private int air_time;
	private Order my_order;
	static public TimerDemo my_timer;
	
	public Drone(int serial_num, boolean available, boolean loaded, int battery, int air_time) 
	{
		this.setSerial_num(serial_num);
		this.setAvailable(available);
		this.setLoaded(loaded);
		this.setBattery(battery);
		this.setAir_time(air_time);
		
	}
	
	
	
	@SuppressWarnings("static-access")
	static public void drone_timer ()
	{
		my_timer = new TimerDemo();
		my_timer.timer = new Timer();
		my_timer.timer.schedule(my_timer,0, 1000);
		return;
	}

	public int getSerial_num() {
		return serial_num;
	}


	public void setSerial_num(int serial_num) {
		this.serial_num = serial_num;
	}


	public boolean isAvailable() {
		return available;
	}


	public void setAvailable(boolean available) {
		this.available = available;
	}


	public boolean isLoaded() {
		return loaded;
	}


	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}


	public int getBattery() {
		return battery;
	}


	public void setBattery(int battery) {
		this.battery = battery;
	}


	public int getAir_time() {
		return air_time;
	}


	public void setAir_time(int air_time) {
		this.air_time = air_time;
	}


	public Order getMy_order() {
		return my_order;
	}


	public void setMy_order(Order my_order) {
		this.my_order = my_order;
	}

}
