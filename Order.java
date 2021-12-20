package Drone_System;

public class Order {
	private int order_number;
	private String my_subscriber_id;
	private String des_subscriber_id;
	private String city_field;
	private String street_field;
	private String apartment_num_field;
	private String zip_field;
	private int my_drone_id;
	private String date;
	
	// test C'tor
	Order(int order_number1, String my_subscriber_id1, String des_subscriber_id1, String date)
	{
		setOrder_number(order_number1);
		setMy_subscriber(my_subscriber_id1);
		setDes_subscriber(des_subscriber_id1);
		setDate(date);
	}
	
	Order()
	{
		setOrder_number(order_number);
		setMy_subscriber(my_subscriber_id);
		setDes_subscriber(des_subscriber_id);
		setDate(date);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public int getOrder_number() {
		return order_number;
	}

	public void setOrder_number(int order_number) {
		this.order_number = order_number;
	}

	public String getMy_subscriber() {
		return my_subscriber_id;
	}

	public void setMy_subscriber(String my_subscriber_id2) {
		this.my_subscriber_id = my_subscriber_id2;
	}

	public String getDes_subscriber() {
		return des_subscriber_id;
	}

	public void setDes_subscriber(String des_subscriber_id2) {
		this.des_subscriber_id = des_subscriber_id2;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
	public int getMy_drone() {
		return my_drone_id;
	}

	public void setMy_drone(int i) {
		my_drone_id = i;
	}

	public String toString ()
	{
	return "The order number is: " + this.order_number + " ,The subscriber id is: " +
	this.my_subscriber_id + " ,The destination id is: " + this.des_subscriber_id +
	" ,The date of the order is: " + this.date;
	}

	public String getCity_field() {
		return city_field;
	}

	public void setCity_field(String city_field) {
		this.city_field = city_field;
	}

	public String getStreet_field() {
		return street_field;
	}

	public void setStreet_field(String street_field) {
		this.street_field = street_field;
	}

	public String getApartment_num_field() {
		return apartment_num_field;
	}

	public void setApartment_num_field(String apartment_num_field) {
		this.apartment_num_field = apartment_num_field;
	}

	public String getZip_field() {
		return zip_field;
	}

	public void setZip_field(String zip_field) {
		this.zip_field = zip_field;
	}

}