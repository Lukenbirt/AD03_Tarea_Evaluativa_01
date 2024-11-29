/*
 1.- Crea un programa Java que muestre por pantalla un listado de eventos,
 junto con el número de asistentes, ubicación y dirección. (2 puntos)
 */
package Lucas_Camarero.AD_03_TareaEvaluativa01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Programa1 {

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
		Statement s = null;
		ResultSet rs = null;
		
		try {
			// conexión a la base de datos
			c = DriverManager.getConnection(urlConnection, user, pwd);
			
			// preparación de la consulta
			s = c.createStatement();
			rs = s.executeQuery("SELECT E.NOMBRE_EVENTO, COUNT(AE.DNI), U.NOMBRE, U.DIRECCION "
					+ "FROM EVENTOS E INNER JOIN UBICACIONES U ON E.ID_UBICACION = U.ID_UBICACION INNER JOIN "
					+ "ASISTENTES_EVENTOS AE ON E.ID_EVENTO = AE.ID_EVENTO "
					+ "GROUP BY E.NOMBRE_EVENTO, U.NOMBRE, U.DIRECCION "
					+ "ORDER BY NOMBRE_EVENTO DESC");
			
			// formato de las columnas para el resultado
            String formato = "%-30s | %-10s | %-35s | %-40s%n";
            
            // cabecera
            System.out.printf(formato, "Evento", "Asistentes", "Ubicación", "Dirección");
            System.out.println("----------------------------------------------------------------------------"
            		+ "---------------------------------");
            
			// mostrar los resultados obtenidos
			while(rs.next()) {
				System.out.printf(formato, rs.getString(1), rs.getInt(2), rs.getString(3), rs.getString(4));
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
				if(s != null){s.close();}
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