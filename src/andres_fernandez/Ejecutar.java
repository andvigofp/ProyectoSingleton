package andres_fernandez;

import andres_fernandez.Aplicacion.MEjSingleton;
import andres_fernandez.Conexiones.DabaseMYSQL;
import andres_fernandez.Conexiones.DatabasePostgres;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Ejecutar {
    public static void main(String[] args) {
           try {
               //Conexión a MYSQL
               Connection mysqlconn = DabaseMYSQL.getInstance();
               System.out.println("Conexión a MYSQL exitosa: " + mysqlconn);

               //Conexión a PostGreSQL
               Connection postgresConn = DatabasePostgres.getInstance();
               System.out.println("Conexión a PostgreSQL exitosa: " + postgresConn);


               Scanner teclado = new Scanner(System.in);
               MEjSingleton metodos = new MEjSingleton();

               metodos.Menu(teclado);

               //Cerrar conexiones
               DabaseMYSQL.cerrarConexion();
               DatabasePostgres.cerrarConexion();
           }catch (SQLException | InputMismatchException e) {
               System.out.println("Error de la conexión " + e.getMessage());
           }
    }
}
