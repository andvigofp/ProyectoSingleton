package andres_fernandez.Conexiones;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DabaseMYSQL {
    private static Connection conn;
    private final String usuario = "root";
    private final String clave = "abc123.";
    private final String url = "jdbc:mysql://localhost:3306/productosMYSQL";

    // El constructor del singleton siempre debe ser privado para evitar llamadas de construcción directas con el operador `new`.
    public DabaseMYSQL() {
        try {
            // Establecer la conexión solo si aún no está creada
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, usuario, clave);
            }
        } catch (SQLException e) {
            System.out.println("Error al abrir la conexión: " + e.toString());
        }
    }


    //El método estático que controla el acceso a la instancia
    //Singleton.
    public static Connection getInstance() {
        if(DabaseMYSQL.conn == null) {
            new DabaseMYSQL();
        }
        return DabaseMYSQL.conn;
    }

    public static void cerrarConexion() {
        try {
            if(conn !=null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexión cerrada con éxito.");
            }
        }catch (SQLException e) {
            System.out.println("Error al cerrar la conexión " + e.toString());
        }
    }
}
