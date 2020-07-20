package connection;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;

public class ConnectionFactory {
	
	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String DBURL = "jdbc:mysql://localhost:3306/warehouse";
	private static final String USER = "root";
	private static final String PASS = "root";

	private static ConnectionFactory singleInstance = new ConnectionFactory();
	
	private ConnectionFactory() {
		try {
			Class.forName(DRIVER);
		}catch (ClassNotFoundException e) {
			System.out.println("Invalid DRIVER!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Funtia realizeaza conexiunea cu baza de date
	 * @return un obiect de tipul connection, ce contine conexiunea cu baza de date
	 */
	private Connection createConnection() {
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(DBURL, USER, PASS);
		}catch(SQLException e) {
			System.out.println("Invalid username, password or DatabaseURL!!!");
			e.printStackTrace();
		}
	
		return connection;
	}
	
	/**
	 * Functia functioneaza ca unt getter pentru conexiun
	 * @return conexiunea cu baza de date
	 */
	public static Connection getConnection() {
		return singleInstance.createConnection();
	}
	
	/**
	 * Functia realizeaza inchiderea conexiunii cu baza de date
	 * @param connection - conexiunea ce trebuie inchisa
	 */
	public static void close(Connection connection) {
		if( connection != null ) {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
			}
		}
	}
	
	/**
	 * Functia realizeaza inchiderea statementului
	 * @param statement - stamentul ce trebuie inchis
	 */
	public static void close(Statement statement) {
		if( statement != null ) {
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
			}
		}
	}
	
	/**
	 * Functia realizeaza inchiderea setului de rezultate
	 * @param resultSet - setul de rezultate ce trebuie inchis
	 */
	public static void close(ResultSet resultSet) {
		if( resultSet != null ) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
			}
		}
	}
}
