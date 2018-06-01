package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class SqliteDB {
	private static Connection connection = null;

	public static void connect() {
		try {
			String url = "jdbc:sqlite:effectiveness.db";
			connection = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static Connection getConnection() {
		return connection;
	}
}