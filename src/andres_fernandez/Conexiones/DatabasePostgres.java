package andres_fernandez.Conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabasePostgres {

    private static Connection conn;
    private static final String usuario = "postgres";
    private static final String clave = "abc123.";
    private static final String url = "jdbc:postgresql://localhost:5432/almacenPOSTGRES";

    // El constructor del Singleton debe ser privado para evitar creación de nuevas instancias directamente.
    public DatabasePostgres() {
        try {
            // Establecer la conexión solo si aún no está creada
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, usuario, clave);
            }
        } catch (SQLException e) {
            System.out.println("Error al abrir la conexión: " + e.toString());
        }
    }

    // El método estático que controla el acceso a la instancia Singleton
    public static Connection getInstance() {
       if (DatabasePostgres.conn == null) {
           new DatabasePostgres();
       }
       return DatabasePostgres.conn;
    }

    // Método para cerrar la conexión cuando ya no se necesite
    public static void cerrarConexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexión cerrada con éxito.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.toString());
        }
    }
}

