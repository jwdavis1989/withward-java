package com.withward.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;

public class JDBC {
	public static Connection getConnection() throws SQLException {
		Connection c = null;
		DriverManager.registerDriver(new Driver());
		
		//String connUrl = System.getenv("DB_URL");
		//String connUser = System.getenv("DB_USERNAME");
		//String connPass = System.getenv("DB_PASSWORD");
		
		String connUrl = "jdbc:postgresql://localhost:5432/withwardServer";
		String connUser = "postgres";
		String connPass = "password";

		
		c = DriverManager.getConnection(connUrl, connUser, connPass);
		return c;
	}
}
