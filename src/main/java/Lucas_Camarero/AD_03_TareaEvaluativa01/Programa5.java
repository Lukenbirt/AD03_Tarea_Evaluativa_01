/*
5.-	Ejercicio SGBD embebida y pool de conexiones.  (2 puntos)
Además de los SGBD vistos en esta unidad, existe otro SGBD embebido de Java muy popular llamado HSQLDB.
Crea en Eclipse un proyecto Maven en Java y añade la dependencia necesaria para ello. ¿Cuál es?
Asimismo, otro Pool de conexiones popular es Vibur. Añade la dependencia necesaria en Maven. ¿Cuál es?
Utiliza el DataSource específico de Vibur para reemplazar el DriverManager.getConnection.
Necesitarás investigar y añadir una línea adicional explícitamente en comparación a H2 para que funcione correctamente.
Por último, conéctate a una BD basada en fichero en HSQLDB.
Si llegados a este punto connection.isValid(0) devuelve “true” el ejercicio puede darse por finalizado. 
 */

package Lucas_Camarero.AD_03_TareaEvaluativa01;

import org.vibur.dbcp.ViburDBCPDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class Programa5 {
    public static void main(String[] args) throws SQLException {
        
    	DataSource ds = createDataSource();

        // test de conexión
        try (Connection c = ds.getConnection()) {
            System.out.println("connection.isValid(0) = " + c.isValid(0));
        }
    }
    
    // método para crear el DataSource de Vibur
    private static DataSource createDataSource() {
        ViburDBCPDataSource ds = new ViburDBCPDataSource();
        ds.setJdbcUrl("jdbc:hsqldb:mem");
        
        // usuario administrador de sistema creado por defecto
        ds.setUsername("sa");
        
        // contraseña vacía creada por defecto
        ds.setPassword("");
        
        // inicializa el pool de conexiones
        ds.start();
        return ds;
    }
}
