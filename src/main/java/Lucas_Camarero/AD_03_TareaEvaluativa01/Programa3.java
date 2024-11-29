/*
 3.- Crea un programa que permita registrar un asistente a un evento. Se pedirá el dni.
 Se mostrará en consola el listado de eventos, cada uno con un número y se le pedirá al usuario que elija el evento.
 Si el dni no se encuentra en la BD se guarda en la tabla de asistentes.
 Si el evento ya alcanzó su capacidad máxima (basado en la ubicación), el programa debe negar la entrada.
 Se debe controlar que el dni tenga formato de 8 números y una letra seguido.
 */
package Lucas_Camarero.AD_03_TareaEvaluativa01;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			// conexión a la base de datos
			c = DriverManager.getConnection(urlConnection, user, pwd);
			
			// se pide el dni al usuario
			System.out.println("Introduce el DNI del asistente:");
			String dni = sc.nextLine();
			
			// se controla el formato del dni
			while (!verificarDni(dni)) {
				System.out.println("Introduce el DNI del asistente:");
				dni = sc.nextLine();
			}
			
			// se busca el dni en la base de datos
			st = c.prepareStatement("SELECT * FROM ASISTENTES WHERE DNI = ?");
			st.setString(1, dni);
			rs = st.executeQuery();
			String nombre = "";
			
			// si el dni existe en la base de datos
			if(rs.next()) {
				nombre = rs.getString(2);
				
			// si el dni no existe en la base de datos creo el asistente
			} else {
				System.out.println("No se encontró un asistente con el DNI proporcionado.");
				System.out.println("Introduce el nombre del asistente:");
				nombre = sc.nextLine();
				st.close();
				st = c.prepareStatement("INSERT INTO ASISTENTES (DNI, NOMBRE) VALUES (?,?)");
				st.setString(1, dni);
				st.setString(2, nombre);
				st.executeUpdate();
			}
				
			System.out.println("Estás realizando la reserva para: " + nombre);
				
			// se muestra en consola el listado de eventos
			st.close();
			st = c.prepareStatement("SELECT E.NOMBRE_EVENTO, U.CAPACIDAD, COUNT(*) FROM eventos E INNER JOIN "
					+ "ASISTENTES_EVENTOS A ON E.ID_EVENTO = A.ID_EVENTO "
					+ "INNER JOIN UBICACIONES U ON E.ID_UBICACION = U.ID_UBICACION "
					+ "GROUP BY E.NOMBRE_EVENTO, U.CAPACIDAD;");
			rs.close();
			rs = st.executeQuery();
			System.out.println("Lista de eventos:");
			
			int i = 1;
			ArrayList<Integer> eventosCompletos = new ArrayList<>();		// guardará los eventos en los que ya no hay capacidad
			
			while(rs.next()) {
				int capacidad = rs.getInt(2) - rs.getInt(3);
				System.out.println(i++ + ". " + rs.getString(1) + " - Espacios disponibles: " + capacidad);
				
				if(capacidad == 0) {
					
					// añade el número de un evento sin capacidad al array
					eventosCompletos.add(i - 1);
				}
				
			}
						
			// se pregunta al usuario qué evento elige
			System.out.println("Elige el número de evento al que quiere asistir:");
			int numeroEvento = sc.nextInt();
			
			// se comprueba si hay capacidad o no en el evento elegido
			boolean b = true;
			for (int z = 0; z < eventosCompletos.size(); z++) {
			    
				// si no la hay el programa se termina
				if (eventosCompletos.get(z) == numeroEvento) {
					b = false;
			    	System.out.println("El evento está completo.");
			    }
			}
			
			// si hay capacidad
			if (b) {
				// se comprueba si el usuario ya está registrado en ese evento
				st.close();
				st = c.prepareStatement("SELECT * FROM ASISTENTES_EVENTOS WHERE DNI = ? AND ID_EVENTO = ?");
				st.setString(1, dni);
				st.setInt(2, numeroEvento);
				rs.close();
				rs = st.executeQuery();
					
				// si ya existe registro
				if(rs.next()) {
					System.out.println(nombre + " ya estaba registrado en ese evento.");
					
				// si no existe se registra al usuario en el evento elegido
				} else {
					st.close();
					st = c.prepareStatement("INSERT INTO ASISTENTES_EVENTOS (DNI, ID_EVENTO) VALUES (?,?)");
					st.setString(1, dni);
					st.setInt(2, numeroEvento);
					st.executeUpdate();
					System.out.println(nombre + " ha sido seleccionado para el evento registrado.");
				}		
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
				if(st != null){st.close();}
				if(sc != null){sc.close();}
				if(c != null){c.close();}
			} catch (Exception ex){	
			}
		}
		
	}
	
	// método para verificar el formato de un DNI
	public static boolean verificarDni(String dni) {
   
		try {
			//Pattern formatoDni = Pattern.compile("[a-z,A-Z,0-9]{1}[0-9]{7}[a-z,A-Z,0-9]{1}");
			Pattern formatoDni = Pattern.compile("\\d{8}[a-zA-Z]");
		    Matcher comparar = formatoDni.matcher(dni);
		               
		    if(comparar.matches()) {
		    	return true;
		    } else {
		    	System.out.println("El dato que has introducido no tiene formato de DNI");
		        return false;
		    }
		               
		} catch (Exception ex) {
			ex.printStackTrace(System.err);   
		}
		            
		return true;
	}
	
	// método para mostrar diferente información de errores SQL
	public static void muestraErrorSQL(SQLException e) {
		System.err.println("SQL ERROR mensaje: " + e.getMessage());
		System.err.println("SQL Estado: " + e.getSQLState());
		System.err.println("SQL código específico: " + e.getErrorCode());
	}

}