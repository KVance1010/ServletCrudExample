package com.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/StudentInsert")
public class StudentInsert extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	// constructor
	public StudentInsert() { super(); }

	// creates a method that can be called to created a connection
	private static Connection getConnection() {
		
		// have to create a connection to the database
		Connection con = null;

		// Step 1 : load Driver in memory
		try {
			// to call Class loader to load class in memory at dymanic time. This loads the driver into memory for use.
			Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL = com.mysql.cj.jdbc.Driver // Oracle = oracle.jdbc.driver.OracleDriver

			// This tells where the database is located
			String url = "jdbc:mysql://localhost:3306/cred"; // if the host is not local use jdbc:mysql://URL:portNumber/databaseName
			String username = "root";
			String password = "OZuzz&FA^qkfHKJkU9u=8v4=eS+Jt/8%";

			// Step 2: Database information //DriverManager is used to control jdbc drivers
			con = DriverManager.getConnection(url, username, password);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
// update values in a table
		try {
			Connection con = getConnection();
			PrintWriter out = response.getWriter();

			// read values
			int rollno = Integer.parseInt(request.getParameter("txtRollno"));
			String name = request.getParameter("txtname");
			Date dob = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("txtdob"));
			java.sql.Date sdob = new java.sql.Date(dob.getTime());
			PreparedStatement ps = con.prepareStatement("insert into studentDetails(rollno, studname, dob) values(?,?,?)");
			ps.setInt(1, rollno);
			ps.setString(2, name);
			ps.setDate(3, sdob);
			int results = ps.executeUpdate();

			if (results > 0) {
				out.println("Record inserted<br><br>");
			} else {
				out.println("Record not updated<br><br>");
			}

			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
// show data from in a table
		try {
			// calls the method of get connection and returns the current connection
			Connection con = getConnection();
			// Statement allows you read data from the database
			Statement stmt = con.createStatement();
			// ResultSet allows you to save the information from the database // this will
			// get all records
			ResultSet rs = stmt.executeQuery("select * from studentdetails");

			PrintWriter out = response.getWriter();

			// printing the information to a table on the url // setting the table amount of
			// columns
			out.println("<table border=2>");
			while (rs.next())
				// tr = table rule td = table data // this is how you fill in the table
				out.println("<tr><td>" + rs.getInt("rollno") + "</td><td> " + rs.getString("studname") + "</td><td> "
						+ rs.getDate("dob") + "</td></tr> ");
			out.println("</table><br><br>");

			// Use PreparedStatment when filtering the information
			PreparedStatement ps = con.prepareStatement("Select * from studentDetails where rollno=? or studname=?");
			ps.setInt(1, 2); // (how many parameter, what value )
			ps.setString(2, "nikita");
			ResultSet rs2 = ps.executeQuery();

			out.println("<table border=2>");
			while (rs2.next())
				// tr = table rule td = table data // this is how you fill in the table
				out.println("<tr><td>" + rs2.getInt("rollno") + "</td><td> " + rs2.getString("studname") + "</td><td> "
						+ rs2.getDate("dob") + "</td></tr> ");
			out.println("</table><br><br>");
			// always need to close the connection.
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
