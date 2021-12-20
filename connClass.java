package Drone_System;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
public class connClass {
public static Connection conn;
public static Connection getConn() throws ClassNotFoundException
	{
		try{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/drone post","root","");
		System.out.println("Connected to data base");
		return conn;
		}catch(SQLException e){
		System.out.println(e);
		return null;
		}
	}

static int date_diff(Subscriber subscriber) throws SQLException, ClassNotFoundException
{
	ResultSet rs;
	Statement stmt;
	String query;
	int days_from_registrtion = 0;
	try {
		conn = (Connection) getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		query = "SELECT datediff  "+
	     "(CURRENT_TIMESTAMP (),join_date_field) FROM subscriber where id_field = " + subscriber.getId();
		stmt = (Statement) conn.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next())
		{
			days_from_registrtion = rs.getInt(1);
		}
		System.out.println(days_from_registrtion + " days are passed from registrtion");
		conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return days_from_registrtion;
}

static int fetch_deliveries(Subscriber my_subscriber) throws SQLException, ClassNotFoundException
{
	ResultSet rs;
	Statement stmt;
	String  query = "";
	int deliveries = 0;

	try {
		conn = (Connection) getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		query ="SELECT * from subscriber where id_field ="  + my_subscriber.getId();
		stmt = (Statement) conn.createStatement();
		rs = stmt.executeQuery(query);
		
		while(rs.next())
		{
			deliveries = rs.getInt("deliveries_amount_field");
		}
		conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return deliveries;
}


static void deliveries_amount_update_DS_to_DB(Subscriber my_subscriber) throws SQLException, ClassNotFoundException
{
	try {
		conn = (Connection) getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		PreparedStatement preparedStmt = 
		(PreparedStatement) conn.prepareStatement("update subscriber set deliveries_amount_field = ? where id_field = ?");
		preparedStmt.setInt(1,my_subscriber.getDeliveries_amount());
		preparedStmt.setString(2, my_subscriber.getId());
		preparedStmt.executeUpdate();
		preparedStmt.close();
			
		conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
}

static void payment_details_DS_to_DB(Subscriber my_Subscriber) throws SQLException, ClassNotFoundException
{
	try {
		conn = (Connection) connClass.getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	String update_package = "update subscriber set package_pay_field = ? where id_field = ?";
	String update_deliveries = "update subscriber set deliveries_amount_field = ? where id_field = ?";
	String update_credit_num = "update subscriber set credit_num_field = ? where id_field = ?";
	String update_date = "update subscriber set credit_date_field = ? where id_field = ?";
	String update_back_num = "update subscriber set credit_back_num_field = ? where id_field = ?";
	try {
		PreparedStatement update_pack = 
		(PreparedStatement) conn.prepareStatement(update_package);
		PreparedStatement update_del = 
		(PreparedStatement) conn.prepareStatement(update_deliveries);
		PreparedStatement update_credit = 
		(PreparedStatement) conn.prepareStatement(update_credit_num);
		PreparedStatement update_credit_date = 
		(PreparedStatement) conn.prepareStatement(update_date);
		PreparedStatement update_credit_back = 
		(PreparedStatement) conn.prepareStatement(update_back_num);

		update_pack.setString(1, my_Subscriber.getPackage_pay());
		update_pack.setString(2,my_Subscriber.getId());
		update_pack.executeUpdate();
		
		update_del.setInt(1, my_Subscriber.getDeliveries_amount());
		update_del.setString(2,my_Subscriber.getId());
		update_del.executeUpdate();
		
		update_credit.setString(1, my_Subscriber.getCredit_num());
		update_credit.setString(2,my_Subscriber.getId());
		update_credit.executeUpdate();
		
		update_credit_date.setString(1, my_Subscriber.getCredit_date());
		update_credit_date.setString(2,my_Subscriber.getId());
		update_credit_date.executeUpdate();
		
		update_credit_back.setString(1, my_Subscriber.getCredit_back_num());
		update_credit_back.setString(2,my_Subscriber.getId());
		update_credit_back.executeUpdate();
		conn.close();
        System.out.println("subscriber payment details updated ");
	} catch (SQLException e) {
		e.printStackTrace();
	}
}



static void subscribers_to_database(Subscriber my_subscriber) throws SQLException, ClassNotFoundException
{
	//Connection conn = null;
	try {
		conn = (Connection) connClass.getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement("insert into subscriber" +
	"(first_name_field, last_name_field, id_field, phone_number_field, password_field, city_field, street_field, "
	+ "apartment_num_field, zip_field, package_pay_field,deliveries_amount_field, credit_num_field, credit_date_field, "
	+ "credit_back_num_field )values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		preparedStmt.setString(1, my_subscriber.getFirst_name());
		preparedStmt.setString(2, my_subscriber.getLast_name());
		preparedStmt.setString(3, my_subscriber.getId());
		preparedStmt.setString(4, my_subscriber.getPhone_number());
		preparedStmt.setString(5, my_subscriber.getPassword());
		preparedStmt.setString(6, my_subscriber.getCity());
		preparedStmt.setString(7, my_subscriber.getStreet());
		preparedStmt.setString(8, my_subscriber.getApartment_num());
		preparedStmt.setString(9, my_subscriber.getZip());
		preparedStmt.setString(10, my_subscriber.getPackage_pay());
		preparedStmt.setInt(11, my_subscriber.getDeliveries_amount());
		preparedStmt.setString(12, my_subscriber.getCredit_num());
		preparedStmt.setString(13, my_subscriber.getCredit_date());
		preparedStmt.setString(14, my_subscriber.getCredit_back_num());
		preparedStmt.executeUpdate();
		preparedStmt.close();
		conn.close();
        System.out.println("Updated");
	} catch (SQLException e) {
		e.printStackTrace();
	}
}



static Subscriber database_to_object(String[] split_string) throws SQLException, ClassNotFoundException
{
	//Connection conn = null;
	ResultSet rs;
	Statement stmt;
	String query;
	Subscriber my_subscriber = new Subscriber();

	try {
		conn = (Connection) getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		
		query ="SELECT * from subscriber where id_field = "  + split_string[1];
		stmt = (Statement) conn.createStatement();
		rs = stmt.executeQuery(query);
		
		while(rs.next())
		{
			my_subscriber.setFirst_name(rs.getString("first_name_field"));
			my_subscriber.setLast_name(rs.getString("last_name_field"));
			my_subscriber.setId(rs.getString("id_field"));
			my_subscriber.setPhone_number(rs.getString("phone_number_field"));
			my_subscriber.setPassword(rs.getString("password_field"));
			my_subscriber.setCity(rs.getString("city_field"));
			my_subscriber.setStreet(rs.getString("street_field"));
			my_subscriber.setApartment_num(rs.getString("apartment_num_field"));
			my_subscriber.setZip(rs.getString("zip_field"));
			my_subscriber.setPackage_pay(rs.getString("package_pay_field"));
			my_subscriber.setDeliveries_amount(rs.getInt("deliveries_amount_field"));
			my_subscriber.setCredit_num(rs.getString("credit_num_field"));
			my_subscriber.setCredit_date(rs.getString("credit_date_field"));
			my_subscriber.setCredit_back_num(rs.getString("credit_back_num_field"));
			System.out.println(my_subscriber.toString());
		}
		conn.close();
        System.out.println("subscriber created");
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return my_subscriber;
}



static boolean exist_sub_by_id(String sub_id) throws SQLException, ClassNotFoundException
{
	ResultSet rs;
	Statement stmt;
	String query;
	boolean sub_exist = false;

	try {
		conn = (Connection) getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		
		query ="SELECT * from subscriber where id_field = "  + sub_id;
		stmt = (Statement) conn.createStatement();
		rs = stmt.executeQuery(query);
		
		
		while(rs.next())
		{
			if(rs.getString("id_field").equals(sub_id))
			{
				sub_exist = true;
				break;
			}
		}
		conn.close();
        System.out.println("subscriber found");
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return sub_exist;
}

static ArrayList<String> show_orders(String[] split_string, String status)
{
	ArrayList<String> my_orders = new ArrayList<String>();
	Order my_order = new Order();
	String OrderToString = "";
	String query = "";
	ResultSet rs;
	Statement stmt;
	
	try {
		conn = (Connection) getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		if (status.equals("sent"))
		{
			query = "SELECT * from orders where sender_id = "  + split_string[1];
		}
		else if (status.equals("recived"))
		{
			query = "SELECT * from orders where des_id = "  + split_string[1];
		}

		stmt = (Statement) conn.createStatement();
		rs = stmt.executeQuery(query);
		
			while(rs.next())
			{			
		        my_order.setOrder_number(rs.getInt("order_number"));
		        my_order.setMy_subscriber(rs.getString("sender_id"));
		        my_order.setDes_subscriber(rs.getString("des_id"));
		        my_order.setDate(rs.getString("date"));	        
		        OrderToString = String.valueOf(my_order);
		        my_orders.add(OrderToString);
			}
        System.out.println("order here");
        conn.close();
	    }catch (SQLException e) {
			e.printStackTrace();
		}
	return my_orders;
}

static void orders_to_database(Order My_Order) throws SQLException, ClassNotFoundException
{
	try {
		conn = (Connection) getConn();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
	try {
		PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement("insert into orders" +
	"(sender_id, des_id, city_field, street_field, apartment_num_field, zip_field)values(?,?,?,?,?,?)");
		preparedStmt.setString(1, My_Order.getMy_subscriber());
		preparedStmt.setString(2, My_Order.getDes_subscriber());
		preparedStmt.setString(3, My_Order.getCity_field());
		preparedStmt.setString(4, My_Order.getStreet_field());
		preparedStmt.setString(5, My_Order.getApartment_num_field());
		preparedStmt.setString(6, My_Order.getZip_field());


		preparedStmt.executeUpdate();
		preparedStmt.close();
		conn.close();
        System.out.println("Order Updated");
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
}