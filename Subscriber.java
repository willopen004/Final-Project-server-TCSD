package Drone_System;

public class Subscriber {
	private String first_name;
	private String last_name;
	private String id;
	private String phone_number;
	private String password;
	private String city;
	private String street;
	private String apartment_num;
	private String zip;
	private String package_pay;
	private int deliveries_amount;
	private String credit_num;
	private String credit_date;
	private String credit_back_num;
	
	Subscriber()
	{
		
	}
	
	Subscriber(String first_name1, String last_name1, String id1, String phone_number1, String password1)
	{
		setFirst_name(first_name1);
		setLast_name(last_name1);
		setId(id1);
		setPhone_number(phone_number1);
		setPassword(password1);
	}
	
	public void set_personal_details(String split_string[])
	{
		this.setFirst_name(split_string[1]);
		this.setLast_name(split_string[2]);
		this.setId(split_string[3]);
		this.setPhone_number(split_string[4]);
		this.setPassword(split_string[5]);
	}
	
	public void set_address_details(String split_string[])
	{
		this.setCity(split_string[6]);
		this.setStreet(split_string[7]);
		this.setApartment_num(split_string[8]);
		this.setZip(split_string[9]);
	}
	
	public void set_payment_details(String split_string[])
	{
		this.setPackage_pay(split_string[10]);
		switch(split_string[10])
		{

		case("99$ : 50 deliveries"):
		{
			this.setDeliveries_amount(50);
			break;
		}
		case("179$ : 150 deliveries"):
		{
			this.setDeliveries_amount(150);
			break;
		}
		case("monthly 150$ : 100 deliveries"):
		{
			this.setDeliveries_amount(100);
			break;
		}
		}
		this.setCredit_num(split_string[11]);
		this.setCredit_date(split_string[12]);
		this.setCredit_back_num(split_string[13]);
	}

	public Order Create_Order(String[] Array_of_split_String)
	{
		Order My_new_order = new Order();
		int i = 0;
		My_new_order.setMy_subscriber(this.getId());
		My_new_order.setDes_subscriber(Array_of_split_String[i + 2]);
		System.out.println(My_new_order.toString());
		return My_new_order;
	}


	public String getFirst_name() {
		return first_name;
	}


	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}


	public String getLast_name() {
		return last_name;
	}


	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

/*
	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

*/
	public String getPhone_number() {
		return phone_number;
	}


	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String toString ()
		{
		return "The first Name is: " + this.first_name + " ,The last Name is: " +
	this.last_name + " ,The id is: " + this.id + " ,The phone number is: " +
	this.phone_number + " ,The password is: " + this.password + " ,The deliveries amount: " + this.deliveries_amount;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getApartment_num() {
		return apartment_num;
	}

	public void setApartment_num(String apartment_num) {
		this.apartment_num = apartment_num;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPackage_pay() {
		return package_pay;
	}

	public void setPackage_pay(String package_pay) {
		this.package_pay = package_pay;
	}

	public int getDeliveries_amount() {
		return deliveries_amount;
	}

	public void setDeliveries_amount(int deliveries_amount) {
		this.deliveries_amount = deliveries_amount;
	}

	public String getCredit_num() {
		return credit_num;
	}

	public void setCredit_num(String credit_num) {
		this.credit_num = credit_num;
	}

	public String getCredit_date() {
		return credit_date;
	}

	public void setCredit_date(String credit_date) {
		this.credit_date = credit_date;
	}

	public String getCredit_back_num() {
		return credit_back_num;
	}

	public void setCredit_back_num(String credit_back_num) {
		this.credit_back_num = credit_back_num;
	}

}