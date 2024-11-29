/*
 3.- Crea un programa que permita registrar un asistente a un evento. Se pedirá el dni.
 Se mostrará en consola el listado de eventos, cada uno con un número y se le pedirá al usuario que elija el evento.
 Si el dni no se encuentra en la BD se guarda en la tabla de asistentes.
 Si el evento ya alcanzó su capacidad máxima (basado en la ubicación), el programa debe negar la entrada.
 Se debe controlar que el dni tenga formato de 8 números y una letra seguido.
 */
package Lucas_Camarero.AD_03_TareaEvaluativa01;

import java.sql.*;
import java.util.Scanner;

public class Programa3 {

	public static void main(String[] args) {
		// parámetros para la conexión con la base de datos
		String basedatos = "dbeventos";
		String host = "localhost";
		String port = "3306";
		// parAdic son parámetros adicionales aunque no son necesarios
		String parAdic = "?useUnicode=true&useJDBCCompliantTimezoneShift= true&useLegacyDatetimeCode= false&serverTimezone=UTC";
		// url de conexión
		String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos + parAdic;
		String user = "Luken";		// usuario
		String pwd = "Luken1470#";	// contraseña
		Connection c = null;
		Scanner sc = new Scanner(System.in);
		
		try {
			// conexión a la base de datos
			c = DriverManager.getConnection(urlConnection, user, pwd);	
			
		// captura de excepciones SQL
		} catch (SQLException e){
			muestraErrorSQL(e);
			
		// captura de cualquier otro tipo de excepción
		} catch (Exception e) {
			e.printStackTrace(System.err);
			
		// se ejecuta siempre
		} finally {
			try {
				// liberación de recursos
				if(sc != null){sc.close();}
				if(c != null){c.close();}
			} catch (Exception ex){	
			}
		}
		
	}
	
	// método para mostrar diferente información de errores SQL
	public static void muestraErrorSQL(SQLException e) {
		System.err.println("SQL ERROR mensaje: " + e.getMessage());
		System.err.println("SQL Estado: " + e.getSQLState());
		System.err.println("SQL código específico: " + e.getErrorCode());
	}

}