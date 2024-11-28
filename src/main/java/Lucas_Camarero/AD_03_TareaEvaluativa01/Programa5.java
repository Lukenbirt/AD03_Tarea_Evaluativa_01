package Lucas_Camarero.AD_03_TareaEvaluativa01;

import java.sql.*;

public class Programa5 {

	public static void main(String[] args) throws SQLException {
		try(Connection c = DriverManager.getConnection("jdbc:mysql://localhost/dbeventos", "Luken", "Luken1470#")) {
        	System.out.println( "connection.isValid(0) = " + c.isValid(0));
        }
	}

}
