/*
 2.- Crea un programa Java que permita cambiar la capacidad máxima de una ubicación.
 El programa recibirá el nombre de la ubicación, mostrará la capacidad actual y luego permitirá al usuario 
 actualizar la capacidad máxima. Si la ubicación no existe, se informará al usuario. (2 puntos)
 */
package Lucas_Camarero.AD_03_TareaEvaluativa01;

import java.sql.*;
import java.util.Scanner;

public class Programa2 {

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
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			// conexión a la base de datos
			c = DriverManager.getConnection(urlConnection, user, pwd);
			
			// el usuario introduce el nombre de la ubicación
			System.out.print("Introduce el nombre de la ubicación: ");
			String ubicacionUsuario = sc.nextLine();
			
			// se comprueba si existe la ubicación
			ps = c.prepareStatement("SELECT * FROM UBICACIONES WHERE NOMBRE = ?");
			ps.setString(1, ubicacionUsuario);
			rs = ps.executeQuery();
			
			// si existe la ubicación
            if (rs.next()) {
				// se informa de la capacidad de la ubicación
				System.out.println("La capacidad actual de la ubicación '" + rs.getString(2) + "' es: " + rs.getInt(4));
				
				// guardo el id de la ubicación (MySql me lo pide para hacer el Update posterior)
				int id = rs.getInt(1);
				
				// el usuario introduce la nueva capacidad
				System.out.print("Introduce la nueva capacidad máxima: ");
				String respuesta = sc.nextLine();
				int nuevaCapacidad = Integer.parseInt(respuesta);
				
				// actualización de la capacidad
				ps.close();
				ps = c.prepareStatement("UPDATE UBICACIONES SET CAPACIDAD = ? WHERE ID_UBICACION = ?");
				ps.setInt(1, nuevaCapacidad);
				ps.setInt(2, id);
				int num = ps.executeUpdate();
				
				// se informa si ha sido posible o no la actualización
				if(num == 1) {
					System.out.println("Capacidad actualizada correctamente.");			
				} else {
					System.out.println("No se ha podido actualizar la capacidad.");
				}
				
			// si la ubicación no existe se informa al usuario
            } else {
            	System.out.println("Esta ubicación no existe en la base de datos.");
            }
			
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
				if(rs != null){rs.close();}
				if(ps != null){ps.close();}
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