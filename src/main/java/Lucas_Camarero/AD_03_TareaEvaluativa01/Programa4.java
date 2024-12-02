/*
 4.- Crea un programa Java que consulte la cantidad de asistentes a un evento específico utilizando
 la función almacenada obtener_numero_asistentes.
 La función recibe como parámetro el código del evento y devuelve el número de asistentes a dicho evento. (2 puntos)
 */

package Lucas_Camarero.AD_03_TareaEvaluativa01;

import java.sql.*;
import java.util.Scanner;

public class Programa4 {

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
		PreparedStatement st = null;
		ResultSet rs = null;
		CallableStatement cs = null;
		
		try {
			// conexión a la base de datos
			c = DriverManager.getConnection(urlConnection, user, pwd);
			
			// muestra la lista de eventos
			st = c.prepareStatement("SELECT NOMBRE_EVENTO FROM EVENTOS");
			rs = st.executeQuery();
			System.out.println("Lista de eventos:");
			
			int i = 1;			
			while(rs.next()) {
				System.out.println(i++ + ". " + rs.getString(1));			
			}
			
			// el usuario selecciona un evento
			System.out.println("Introduce el ID del evento para consultar la cantidad de asistentes:");
			String respuesta = sc.nextLine();
			int id = Integer.parseInt(respuesta);
			
			// se envia el evento seleccionado a la función almacenada en la base de datos
			cs = c.prepareCall("{? = call obtener_numero_asistentes(?)}");
			cs.setInt(2, id);
			
			// le decimos de qué tipo es el parámetro de salida de la función y la ejecutamos
			cs.registerOutParameter(1, java.sql.Types.INTEGER);
			cs.execute();
			int asistentes = cs.getInt(1);
			System.out.println("El número de asistentes para el evento seleccionado es: " + asistentes);	
						
		// captura de excepciones SQL
		} catch (SQLException e){
			muestraErrorSQL(e);
						
		// captura de cualquier otro tipo de excepción
		} catch (Exception e) {
			e.printStackTrace(System.err);
						
		// se ejecuta siempre
		} finally {
			try {	
				if(cs != null){cs.close();}
				if(rs != null){rs.close();}
				if(st != null){st.close();}
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