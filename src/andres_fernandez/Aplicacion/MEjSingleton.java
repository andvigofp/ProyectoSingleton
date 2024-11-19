package andres_fernandez.Aplicacion;

import andres_fernandez.Conexiones.DabaseMYSQL;
import andres_fernandez.Conexiones.DatabasePostgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MEjSingleton {
    private static Connection mysqlconn = DabaseMYSQL.getInstance();
    private static Connection postgresConn = DatabasePostgres.getInstance();

    // Método menú de opciones
    public void Menu(Scanner teclado) throws SQLException {
        final String menu = "1. CREAR UNA NUEVA CATEGORÍA (POSTGRESQL)."
                + "\n2. CREAR UN NUEVO PROVEEDOR (POSTGRESQL)."
                + "\n3. ELIMINAR UN PROVEEDOR (POSTGRESQL)."
                + "\n4. CREAR UN NUEVO USUARIO (MYSQL)."
                + "\n5. ELIMINAR UN USUARIO (MYSQL)."
                + "\n6. CREAR NUEVO PRODUCTO (MYSQL + POSTGRESQL)."
                + "\n7. ELIMINAR UN PRODUCTO POR SU NOMBRE (MYSQL + POSTGRESQL)."
                + "\n8. LISTAR LOS PRODUCTOS CON BAJO STOCK (MYSQL)."
                + "\n9. OBTENER EL TOTAL DE PEDIDOS REALIZADOS DISPONIBLES (MYSQL)."
                + "\n10. OBTENER LA CANTIDAD DE PRODUCTOS ALMACENADOS POR CADA ALMACÉN (POSTGRESQL)."
                + "\n11. LISTAR TODOS LOS PRODUCTOS CON SUS RESPECTIVAS CATEGORÍAS Y PROVEEDORES (POSTGRESQL)."
                + "\n12. OBTENER TODOS LOS USUARIOS QUE HAN COMPRADO ALGÚN PRODUCTO DE UNA CATEGORÍA DADA (MYSQL + POSTGRESQL)."
                + "\n13. SALIR (Pulsa 13 para salir).";

        int opcion = -1;

        try {
            do {
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        //Crear una nueva categoría (PostgreSQL)
                        String nombreCategoria = ValiacionesMyqlPost.obtenerNombreCategoriaValido(teclado);

                        //Validar que solo contenga letras
                        if (nombreCategoria != null) {
                            // Verificar si la categoría ya existe

                            if (!existeCategoria(nombreCategoria)) {
                                crearCategoria(nombreCategoria);
                            }else {
                                System.out.println("La categoría ya existe. No se puede insertar.");
                            }
                        } else {
                            System.out.println("No se insertó la categoría debido a la validación fallida.");
                        }
                        break;
                    case 2:
                        //Crear un nuevo proveedor (PostgreSQL)
                        String nombreProveedor = ValiacionesMyqlPost.esNombreValidoProveeedor(teclado);
                        String nif = ValiacionesMyqlPost.esNifValidoProveedor(teclado);
                        int telefono = Integer.parseInt(ValiacionesMyqlPost.esTelefonoValidoProveedor(teclado));
                        String email = ValiacionesMyqlPost.esEmailValidoProveedor(teclado);

                        // Validaciones
                            if (!existeProvedorNifEmail(nif, email)) {
                                crearNuevoProveedor(nombreProveedor, nif, telefono, email);
                            }else {
                                System.out.println("Error: El proveedor con ese NIF, teléfono o email ya existe.");
                            }
                        break;
                    case 3:
                        //Eliminar un nuevo proveedor (PostgreSQL)
                        int id = Integer.parseInt(ValiacionesMyqlPost.esIdProveedor(teclado));

                        elimnarProvedor(id);
                        break;
                    case 4:
                        //Crear un nuevo usuario (MySQL)
                        String nombreUsuario = ValiacionesMyqlPost.esNombreUsuario(teclado);
                        String emailUsuario = ValiacionesMyqlPost.esEmailValidoUsuario(teclado);
                        int anho_nacimiento = Integer.parseInt(ValiacionesMyqlPost.esFechaNacimentoValidaUusario(teclado));


                        if (!existeUsuarioNombreEmialNif(nombreUsuario, emailUsuario, anho_nacimiento)) {
                            crearUsuario(nombreUsuario, emailUsuario, anho_nacimiento);
                        }else {
                            System.out.println("Error: El Usuario con ese nombre, email o año nacimiento ya existe.");
                        }

                        break;
                    case 5:
                        //Eliminar un usuario (MySQL)
                        int idUsuario = Integer.parseInt(ValiacionesMyqlPost.esidUsuarioElimnar(teclado));

                        eliminarUsuario(idUsuario);
                        break;
                    case 6:
                        // Llamar al método para ingresar los datos del producto
                        ArrayList<String> productos = ValiacionesMyqlPost.esNombreCrearProducto(teclado);

                        //Crear nuevo producto (nombre, precio, stock, categoria, proveedor) (MySQL + PostgreSQL)
                        String nombre = productos.get(0);
                        Double precio = Double.valueOf(productos.get(1));
                        int stock = Integer.parseInt(productos.get(2));
                        String categoria = productos.get(3);

                        // Obtener y mostrar los NIFs de los proveedores antes de pedir el NIF del proveedor
                        List<String> nifsProveedores = obtenerNifsProveedores();

                        System.out.println("Lista de NIFs de los proveedores disponibles:");
                        for (String nifProveedor : nifsProveedores) {
                            System.out.println(nifProveedor);
                        }

                        String nifs = ValiacionesMyqlPost.esNifCrearProducto(teclado);

                        crearProducto(nombre, precio, stock, categoria, nifs);
                        break;
                    case 7:
                        //Eliminar un producto por su nombre (MySQL + PostgreSQL)
                        String nombreProductoE = ValiacionesMyqlPost.esNombreProductoEliminar(teclado);

                        eliminarProductoPorNombre(nombreProductoE);
                        break;
                    case 8:
                        //Listar los productos con bajo stock (menos de X unidades disponibles) (MySQL)
                        int stockProducto = Integer.parseInt(ValiacionesMyqlPost.esStock(teclado));
                        listarProductosBajoStock(stockProducto);
                        break;
                    case 9:
                        //Obtener el total de pedidos realizados por cada usuario (MySQL)
                        obtenerTotalPedidosUsuarios();
                        break;
                    case 10:
                        //Obtener la cantidad de productos almacenados por cada almacén (PostgreSQL)
                        obtenerCantidadProductosEnCadaAlmacen();
                        break;
                    case 11:
                        //Listar todos los productos con sus respectivas categorías y proveedores (PostgreSQL)
                        listarTodosProductosConCategoriaYProveedor();
                        break;
                    case 12:
                        //Obtener todos los Usuarios que han comprado algún producto de una categoria dada (MySQL + PostgreSQL).
                        int categoría = Integer.parseInt(ValiacionesMyqlPost.esidCategoria(teclado));
                        obtenerUsuariosCompraronProductosCategoria(categoría);
                        break;
                    case 13:
                        //Listar información de un Paciente por ID
                        System.out.println("Fin del programa");
                        teclado.close();
                        return;

                    default:
                        System.out.println("Opción no válida, por favor elija una opción del menú entre 1-12");
                }
            } while (opcion != 13);
        }catch (Exception e) {
            System.out.println("Se produjo un error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Método para pedir por teclado para atributos de tipo int
    static int pedirInt(Scanner teclado, String mensaje) {
        int valor = -1;
        boolean entradaValida = false;
        while (!entradaValida) {
            try {
                System.out.print(mensaje);
                valor = teclado.nextInt();
                entradaValida = true; // Si la entrada es válida, sale del bucle
            } catch (InputMismatchException e) {
                System.out.println("¡Error! Debes ingresar un número entero.");
                teclado.nextLine(); // Limpiar el buffer de entrada
            }
        }
        return valor;
    }

    //Método para pedir por teclado para atributos de tipo double
    static Double pedirDouble (Scanner teclado, String mensaje) {
        while (true) {
            System.out.println(mensaje);

            try {
                String input = teclado.next(); // Leemos la entrada como un String

                // Reemplazamos la coma por el punto, si existe, para asegurar la compatibilidad con el formato decimal
                input = input.replace(',', '.');

                // Intentamos convertir la entrada a un Double
                Double result = Double.parseDouble(input);

                // Si la conversión es exitosa, retornamos el número
                return result;

            } catch (NumberFormatException e) {
                // En caso de que el número no sea válido
                System.out.println("Error: Debes ingresar un número decimal válido (usa punto para los decimales).");
            }
        }
    }


    //Método para pedir por teclado para atributos de tipo String
    static String pedirStringLine(Scanner teclado, String mensaje) {
        while (true) {
            System.out.println(mensaje);

            try {
                return teclado.nextLine();
            } catch (Exception e) {
                System.out.println("Error. " + e.toString());
            }
        }
    }

    //Método para pedir por teclado para atributos de tipo String
    static String pedirString(Scanner teclado, String mensaje) {
        while (true) {
            System.out.println(mensaje);

            try {
                return teclado.next();
            } catch (Exception e) {
                System.out.println("Error. " + e.toString());
            }
        }
    }

    //Método para mostrar los dni de los proveedores
    public List<String> obtenerNifsProveedores() throws SQLException {
        List<String> nifs = new ArrayList<>();
        String sql = "SELECT (contacto).nif FROM objetos.proveedores";

        try (PreparedStatement stmt = MEjSingleton.postgresConn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                nifs.add(rs.getString("nif"));
            }
        }
        return nifs;
    }



    //Método para comprobar si existe esa categoría
    static boolean existeCategoria(String nombreCategoria) {
        String sql = "SELECT COUNT(*) FROM objetos.categorias WHERE nombre_categoria = ?";

        try (PreparedStatement stm = postgresConn.prepareStatement(sql)){
            stm.setString(1, nombreCategoria);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Devuelve true si hay al menos una coincidencia
            }
        }catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la categoría: " + e.getMessage());
        }
        return false; // Devuelve false si hay un error o si la categoría no existe
    }

    //Método para crear una nueva categoría (PostgreSQL)
    static void crearCategoria(String nombreCategoria)  {
        String sql = "INSERT INTO objetos.categorias (nombre_categoria) VALUES (?)";

        try {
            PreparedStatement statement = postgresConn.prepareStatement(sql);

            //Establecer el parámetro de la consulta
            statement.setString(1, nombreCategoria);

            //Ejecutar la consulta
            int rowInsert = statement.executeUpdate();

            if (rowInsert > 0) {
                System.out.println("Categoría añadida con éxito. " + nombreCategoria);
            }else {
                System.out.println("No se pudo añadir la categoría.");
            }
        }catch (SQLException e) {
            System.out.println("Error al insertar la categpría: " + e.toString());
            e.printStackTrace();
        }
    }



    // Método para verificar si el proveedor ya existe en la base de datos
    static boolean existeProvedorNifEmail(String nif, String email) {
        String sql = "SELECT COUNT(*) FROM objetos.proveedores WHERE (contacto).nif = ? OR (contacto).email = ?";

        try (PreparedStatement stm = postgresConn.prepareStatement(sql)){
            stm.setString(1, nif);
            stm.setString(2, email);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }catch (SQLException e) {
            System.out.println("Error al verificar la existencia del proveedor con ese nif o email: " + e.getMessage());
        }
        return false;
    }

    //Método para crear un nuevo proveedor (PostgreSQL)
    static void crearNuevoProveedor(String nombreProveedor, String nif, int telefono, String email) {
        // SQL para insertar un nuevo proveedor con un campo compuesto para contacto
        String sql = "INSERT INTO objetos.proveedores (nombre_proveedor, contacto) "
                + "VALUES (?, ROW(?, ?, ?, ?)::objetos.contacto_tipo)";

        try (PreparedStatement stmt = postgresConn.prepareStatement(sql)) {
            // Establecer los parámetros en la consulta
            stmt.setString(1, nombreProveedor);
            stmt.setString(2, nombreProveedor);
            stmt.setString(3, nif);
            stmt.setInt(4, telefono);
            stmt.setString(5, email);

            // Ejecutar la inserción
            int rowsAffected = stmt.executeUpdate();

            // Verificar si la inserción fue exitosa
            if (rowsAffected > 0) {
                System.out.println("Proveedor creado correctamente: " + nombreProveedor);
            } else {
                System.out.println("No se pudo crear el proveedor.");
            }
        } catch (SQLException e) {
            System.out.println("Error al crear el proveedor: " + e.getMessage());
            e.printStackTrace();
        }
    }



    //Método para eliminar un nuevo proveedor (PostgreSQL)
    static void elimnarProvedor(int id) {
        // Consultas SQL para eliminar en el orden correcto
        String deleteAlmacenesProductosSql = "DELETE FROM objetos.almacenes_productos " +
                "WHERE id_producto IN (SELECT id_producto FROM objetos.productos WHERE id_proveedor = ?)";

        String deleteProductosSql = "DELETE FROM objetos.productos WHERE id_proveedor = ?";

        String deleteProveedorSql = "DELETE FROM objetos.proveedores WHERE id_proveedor = ?";

        //Comprobar el proveedor con su id
        String getSql = "SELECT * FROM objetos.proveedores WHERE id_proveedor = ?";

        // Mostrar datos del proveedor antes de eliminar
        try (PreparedStatement getStmt = postgresConn.prepareStatement(getSql)) {
            getStmt.setInt(1, id);
            ResultSet rs = getStmt.executeQuery();

            if (rs.next()) {
                System.out.println("Datos del proveedor:");
                System.out.println("ID: " + rs.getInt("id_proveedor"));
                System.out.println("Nombre: " + rs.getString("nombre_proveedor"));
                System.out.println("Contacto: " + rs.getString("contacto"));
            } else {
                System.out.println("No se encontró el proveedor con el ID especificado.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Conexión y manejo de transacción
        try {
            // Desactivar el autocommit para manejar la transacción manualmente
            postgresConn.setAutoCommit(false);

            try (PreparedStatement deleteAlmacenesProductosStmt = postgresConn.prepareStatement(deleteAlmacenesProductosSql);
                 PreparedStatement deleteProductosStmt = postgresConn.prepareStatement(deleteProductosSql);
                 PreparedStatement deleteProveedorStmt = postgresConn.prepareStatement(deleteProveedorSql)) {

                // Paso 1: Eliminar registros de la tabla almacenes_productos
                deleteAlmacenesProductosStmt.setInt(1, id);
                deleteAlmacenesProductosStmt.executeUpdate();

                // Paso 2: Eliminar registros de la tabla productos
                deleteProductosStmt.setInt(1, id);
                deleteProductosStmt.executeUpdate();

                // Paso 3: Eliminar el proveedor de la tabla proveedores
                deleteProveedorStmt.setInt(1, id);
                int rowsAffected = deleteProveedorStmt.executeUpdate();

                // Verificar si el proveedor fue eliminado
                if (rowsAffected > 0) {
                    // Confirmar la transacción
                    postgresConn.commit();
                    System.out.println("Proveedor y registros relacionados eliminados correctamente.");
                } else {
                    System.out.println("No se encontró el proveedor con el ID especificado.");
                    postgresConn.rollback();
                }

            } catch (SQLException e) {
                // Si hay un error, hacer rollback de la transacción
                postgresConn.rollback();
                System.err.println("Error al eliminar el proveedor: " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Método para verificar si el proveedor ya existe en la base de datos
    static boolean existeUsuarioNombreEmialNif(String nombre, String email, int ano_nacimiento) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombre = ? OR email = ? OR ano_nacimiento = ?";

        try (PreparedStatement stm = mysqlconn.prepareStatement(sql)){
            stm.setString(1, nombre);
            stm.setString(2, email);
            stm.setInt(3, ano_nacimiento);

            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }catch (SQLException e) {
            System.out.println("Error al verificar la existencia del usuario con ese nombre, nif o email: " + e.getMessage());
        }
        return false;
    }


    //Método para crear un nuevo usuario (MYSQL)
    static void crearUsuario(String nombre, String email, int anho_nacimiento) {
        // Definir la consulta SQL para insertar un nuevo usuario
        String sqltUsuarioSQL = "INSERT INTO usuarios (nombre, email, ano_nacimiento) VALUES (?, ?, ?)";

        try (PreparedStatement stmMysql = mysqlconn.prepareStatement(sqltUsuarioSQL)){
            stmMysql.setString(1, nombre);
            stmMysql.setString(2, email);
            stmMysql.setInt(3, anho_nacimiento);

            int rowInsert = stmMysql.executeUpdate();

            if (rowInsert > 0) {
                System.out.println("Usuario creado correctamente.");
            }else {
                System.out.println("No se pudo crear el usuario.");
            }
        }catch (SQLException e) {
            System.out.println("Error a insertar al usuario a la base de datos: " + e.toString());
            e.printStackTrace();
        }
    }



    //Método para eliminar un nuevo usuario (MYSQL)
    static void eliminarUsuario(int id) {
        try {
            // Comprobar si el usuario existe y mostrar los datos
            String sqlQuery = "SELECT nombre, email, ano_nacimiento FROM usuarios WHERE id_usuario = ?";

            try (PreparedStatement smtMysql = mysqlconn.prepareStatement(sqlQuery)) {
                // Establecer el valor del parámetro
                smtMysql.setInt(1, id);  // Asegúrate de pasar el parámetro 'id' aquí

                try (ResultSet rs = smtMysql.executeQuery()) {
                    // Devuelve los datos del usuario antes de eliminarlo, si existe
                    while (rs.next()) {
                        // Si el usuario existe, mostrar los datos
                        String nombre = rs.getString("nombre");
                        String email = rs.getString("email");
                        int anoNacimiento = rs.getInt("ano_nacimiento");

                        // Mostrar los datos del usuario
                        System.out.println("Datos del usuario a eliminar:");
                        System.out.println("Nombre: " + nombre);
                        System.out.println("Email: " + email);
                        System.out.println("Año de nacimiento: " + anoNacimiento);
                    }
                }
            }

            // Eliminar los registros de la tabla pedidos_productos que hacen referencia a los pedidos
            String deletePedidoProductosQuery = "DELETE FROM pedidos_productos WHERE id_pedido IN (SELECT id_pedido FROM pedidos WHERE id_usuario = ?)";
            try (PreparedStatement stmt = mysqlconn.prepareStatement(deletePedidoProductosQuery)) {
                stmt.setInt(1, id);
                int rowsAffectedPedidoProductos = stmt.executeUpdate();
                System.out.println(rowsAffectedPedidoProductos + " registros eliminados de pedidos_productos.");
            }

            // Eliminar los pedidos asociados al usuario
            String deletePedidoQuery = "DELETE FROM pedidos WHERE id_usuario = ?";
            try (PreparedStatement stmt = mysqlconn.prepareStatement(deletePedidoQuery)) {
                stmt.setInt(1, id);
                int rowsAffectedPedidos = stmt.executeUpdate();
                System.out.println(rowsAffectedPedidos + " pedidos eliminados.");
            }

            // Eliminar el usuario
            String deleteUsuarioQuery = "DELETE FROM usuarios WHERE id_usuario = ?";
            try (PreparedStatement stmt = mysqlconn.prepareStatement(deleteUsuarioQuery)) {
                stmt.setInt(1, id);
                int rowsAffectedUser = stmt.executeUpdate();
                System.out.println("Usuario eliminado. Filas afectadas: " + rowsAffectedUser);
            }

        } catch (SQLException e) {
            System.out.println("Error al borrar el usuario de la base de datos: " + e.toString());
            e.printStackTrace();
        }
    }

    //Método para insertar un producto en la base de datos en postgres
    static void crearProducto(String nombre, Double precio, int stock, String nombre_categoria, String nif) {

        // Paso 1: Obtener el ID de la categoría en PostgreSQL usando el nombre de la categoría
        String sqlCategoria = "SELECT id_categoria FROM objetos.categorias WHERE nombre_categoria = ?";
        int idCategoria = -1;
        try (PreparedStatement stmtCategoria = postgresConn.prepareStatement(sqlCategoria)) {
            stmtCategoria.setString(1, nombre_categoria);
            ResultSet rsCategoria = stmtCategoria.executeQuery();
            if (rsCategoria.next()) {
                idCategoria = rsCategoria.getInt("id_categoria");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Paso 2: Obtener el ID del proveedor en PostgreSQL usando el NIF del proveedor
        String sqlProveedor = "SELECT id_proveedor FROM objetos.proveedores WHERE (contacto).nif = ?";
        int idProveedor = -1;
        try (PreparedStatement stmtProveedor = postgresConn.prepareStatement(sqlProveedor)) {
            stmtProveedor.setString(1, nif);
            ResultSet rsProveedor = stmtProveedor.executeQuery();
            if (rsProveedor.next()) {
                idProveedor = rsProveedor.getInt("id_proveedor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Paso 3: Insertar el nuevo producto en MySQL
        String sqlInsertMySQL = "INSERT INTO productos (nombre_producto, precio, stock) VALUES (?, ?, ?)";
        int idProductoMySQL = -1;
        try (PreparedStatement stmtMySQL = mysqlconn.prepareStatement(sqlInsertMySQL, Statement.RETURN_GENERATED_KEYS)) {
            stmtMySQL.setString(1, nombre);
            stmtMySQL.setDouble(2, precio);
            stmtMySQL.setInt(3, stock);

            int rowsAffected = stmtMySQL.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmtMySQL.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idProductoMySQL = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Paso 4: Calcular el próximo ID para la tabla de PostgreSQL
        String sqlMaxIdPostgres = "SELECT COALESCE(MAX(id_producto), 0) + 1 AS next_id FROM objetos.productos";
        int nextIdProducto = -1;
        try (Statement stmtPostgres = postgresConn.createStatement()) {
            ResultSet rsMaxId = stmtPostgres.executeQuery(sqlMaxIdPostgres);
            if (rsMaxId.next()) {
                nextIdProducto = rsMaxId.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Paso 5: Insertar el producto en PostgreSQL
        if (idProductoMySQL != -1 && idCategoria != -1 && idProveedor != -1 && nextIdProducto != -1) {
            String sqlInsertPostgres = "INSERT INTO objetos.productos (id_producto, id_proveedor, id_categoria) VALUES (?, ?, ?)";
            try (PreparedStatement stmtPostgres = postgresConn.prepareStatement(sqlInsertPostgres)) {
                stmtPostgres.setInt(1, nextIdProducto);  // ID calculado para PostgreSQL
                stmtPostgres.setInt(2, idProveedor);    // ID del proveedor
                stmtPostgres.setInt(3, idCategoria);    // ID de la categoría

                int rowsAffected = stmtPostgres.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Producto creado en PostgreSQL con ID: " + nextIdProducto);
                } else {
                    System.out.println("No se pudo insertar el producto en PostgreSQL.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Paso 6: Mostrar el producto insertado en ambas bases de datos
        if (idProductoMySQL != -1) {
            // Mostrar el producto en MySQL
            String sqlMostrarMySQL = "SELECT id_producto, nombre_producto, stock FROM productos WHERE id_producto = ?";
            try (PreparedStatement stmtMostrarMySQL = mysqlconn.prepareStatement(sqlMostrarMySQL)) {
                stmtMostrarMySQL.setInt(1, idProductoMySQL);
                ResultSet rsMostrarMySQL = stmtMostrarMySQL.executeQuery();
                if (rsMostrarMySQL.next()) {
                    System.out.println("Producto insertado en MySQL:");
                    System.out.println("ID Producto: " + rsMostrarMySQL.getInt("id_producto"));
                    System.out.println("Nombre Producto: " + rsMostrarMySQL.getString("nombre_producto"));
                    System.out.println("Stock: " + rsMostrarMySQL.getInt("stock"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (nextIdProducto != -1) {
            // Mostrar el producto en PostgreSQL
            String sqlMostrarPostgres = "SELECT id_producto, id_categoria, id_proveedor FROM objetos.productos WHERE id_producto = ?";
            try (PreparedStatement stmtMostrarPostgres = postgresConn.prepareStatement(sqlMostrarPostgres)) {
                stmtMostrarPostgres.setInt(1, nextIdProducto);
                ResultSet rsMostrarPostgres = stmtMostrarPostgres.executeQuery();
                if (rsMostrarPostgres.next()) {
                    System.out.println("Producto insertado en PostgreSQL:");
                    System.out.println("ID Producto: " + rsMostrarPostgres.getInt("id_producto"));
                    System.out.println("ID Categoría: " + rsMostrarPostgres.getInt("id_categoria"));
                    System.out.println("ID Proveedor: " + rsMostrarPostgres.getInt("id_proveedor"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //Método para insertar un producto en la base de datos en postgres
    //pero con count para coger la id y sumarlo + el resultado de la id incremente esa id_producto
    /**static void crearProducto(String nombre, Double precio, int stock, String nombreCategoria, String nif) {
        // Paso 1: Obtener el ID de la categoría en PostgreSQL usando el nombre de la categoría
        String sqlCategoria = "SELECT id_categoria FROM objetos.categorias WHERE nombre_categoria = ?";
        int idCategoria = -1;
        try (PreparedStatement stmtCategoria = postgresConn.prepareStatement(sqlCategoria)) {
            stmtCategoria.setString(1, nombreCategoria);
            ResultSet rsCategoria = stmtCategoria.executeQuery();
            if (rsCategoria.next()) {
                idCategoria = rsCategoria.getInt("id_categoria");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Paso 2: Obtener el ID del proveedor en PostgreSQL usando el NIF del proveedor
        String sqlProveedor = "SELECT id_proveedor FROM objetos.proveedores WHERE (contacto).nif = ?";
        int idProveedor = -1;
        try (PreparedStatement stmtProveedor = postgresConn.prepareStatement(sqlProveedor)) {
            stmtProveedor.setString(1, nif);
            ResultSet rsProveedor = stmtProveedor.executeQuery();
            if (rsProveedor.next()) {
                idProveedor = rsProveedor.getInt("id_proveedor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Paso 3: Insertar el nuevo producto en MySQL
        String sqlInsertMySQL = "INSERT INTO productos (nombre_producto, precio, stock) VALUES (?, ?, ?)";
        int idProductoMySQL = -1;
        try (PreparedStatement stmtMySQL = mysqlconn.prepareStatement(sqlInsertMySQL, Statement.RETURN_GENERATED_KEYS)) {
            stmtMySQL.setString(1, nombre);
            stmtMySQL.setDouble(2, precio);
            stmtMySQL.setInt(3, stock);

            int rowsAffected = stmtMySQL.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmtMySQL.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idProductoMySQL = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Paso 4: Calcular el próximo ID para la tabla de PostgreSQL usando COUNT(*)
        String sqlCountPostgres = "SELECT COUNT(*) + 1 AS next_id FROM objetos.productos";
        int nextIdProducto = -1;
        try (Statement stmtPostgres = postgresConn.createStatement()) {
            ResultSet rsCount = stmtPostgres.executeQuery(sqlCountPostgres);
            if (rsCount.next()) {
                nextIdProducto = rsCount.getInt("next_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Paso 5: Insertar el producto en PostgreSQL
        if (idProductoMySQL != -1 && idCategoria != -1 && idProveedor != -1 && nextIdProducto != -1) {
            String sqlInsertPostgres = "INSERT INTO objetos.productos (id_producto, id_proveedor, id_categoria) VALUES (?, ?, ?)";
            try (PreparedStatement stmtPostgres = postgresConn.prepareStatement(sqlInsertPostgres)) {
                stmtPostgres.setInt(1, nextIdProducto);  // ID calculado para PostgreSQL
                stmtPostgres.setInt(2, idProveedor);    // ID del proveedor
                stmtPostgres.setInt(3, idCategoria);    // ID de la categoría

                int rowsAffected = stmtPostgres.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Producto creado en PostgreSQL con ID: " + nextIdProducto);
                } else {
                    System.out.println("No se pudo insertar el producto en PostgreSQL.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Paso 6: Mostrar el producto insertado en ambas bases de datos
        if (idProductoMySQL != -1) {
            // Mostrar el producto en MySQL
            String sqlMostrarMySQL = "SELECT id_producto, nombre_producto, stock FROM productos WHERE id_producto = ?";
            try (PreparedStatement stmtMostrarMySQL = mysqlconn.prepareStatement(sqlMostrarMySQL)) {
                stmtMostrarMySQL.setInt(1, idProductoMySQL);
                ResultSet rsMostrarMySQL = stmtMostrarMySQL.executeQuery();
                if (rsMostrarMySQL.next()) {
                    System.out.println("Producto insertado en MySQL:");
                    System.out.println("ID Producto: " + rsMostrarMySQL.getInt("id_producto"));
                    System.out.println("Nombre Producto: " + rsMostrarMySQL.getString("nombre_producto"));
                    System.out.println("Stock: " + rsMostrarMySQL.getInt("stock"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (nextIdProducto != -1) {
            // Mostrar el producto en PostgreSQL
            String sqlMostrarPostgres = "SELECT id_producto, id_categoria, id_proveedor FROM objetos.productos WHERE id_producto = ?";
            try (PreparedStatement stmtMostrarPostgres = postgresConn.prepareStatement(sqlMostrarPostgres)) {
                stmtMostrarPostgres.setInt(1, nextIdProducto);
                ResultSet rsMostrarPostgres = stmtMostrarPostgres.executeQuery();
                if (rsMostrarPostgres.next()) {
                    System.out.println("Producto insertado en PostgreSQL:");
                    System.out.println("ID Producto: " + rsMostrarPostgres.getInt("id_producto"));
                    System.out.println("ID Categoría: " + rsMostrarPostgres.getInt("id_categoria"));
                    System.out.println("ID Proveedor: " + rsMostrarPostgres.getInt("id_proveedor"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }**/


    //Método para mostrar los datos del producto antes de eliminarlo por su nombre
    static void mostrarDatosProductoPorNombre(String nombre) {
            String selectProductoMySQL = "SELECT * FROM productos WHERE nombre_producto = ?;";


            try {
                // Mostrar datos en MySQL
                try (PreparedStatement psSelectMySQL = mysqlconn.prepareStatement(selectProductoMySQL)) {
                    psSelectMySQL.setString(1, nombre);  // Buscar por nombre en MySQL
                    ResultSet rsMySQL = psSelectMySQL.executeQuery();
                    while (rsMySQL.next()) {
                        System.out.println("MySQL - Producto: " + rsMySQL.getInt("id_producto")
                                + ", Nombre: " + rsMySQL.getString("nombre_producto")
                                + ", Precio: " + rsMySQL.getBigDecimal("precio")
                                + ", Stock: " + rsMySQL.getInt("stock"));
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error no existe ese producto en la base de datos." + e.toString());
                e.printStackTrace();
            }
        }


    //Método para eliminar un producto por su nombre (MySQL + PostgreSQL)
    static void eliminarProductoPorNombre(String nombre) {
        mostrarDatosProductoPorNombre(nombre);

        //Iniciar Transacción en MYSQL
        try {
            mysqlconn.setAutoCommit(false);

            // Obtener id_producto basándose en el nombre del producto en MySQL
            String selectIdProductoMySQL = "SELECT id_producto FROM productos WHERE nombre_producto = ?;";
            int idProducto = -1;

            try (PreparedStatement psSelectIdProductoMySQL = mysqlconn.prepareStatement(selectIdProductoMySQL)) {
                psSelectIdProductoMySQL.setString(1, nombre);
                try (ResultSet rs = psSelectIdProductoMySQL.executeQuery()) {
                    if (rs.next()) {
                        idProducto = rs.getInt("id_producto");
                    } else {
                        System.out.println("Producto no encontrado en MySQL.");
                        return; // Salir del método si no se encuentra el producto
                    }
                }
            }


            // Eliminar de pedidos_productos en MySQL usando el id_producto
            String deleteFromPedidosProductosMySQL = "DELETE FROM pedidos_productos WHERE id_producto = ?;";
            try (PreparedStatement psDeletePedidosProductosMySQL = mysqlconn.prepareStatement(deleteFromPedidosProductosMySQL)) {
                psDeletePedidosProductosMySQL.setInt(1, idProducto);
                psDeletePedidosProductosMySQL.executeUpdate();
            }

            // Eliminar de productos en MySQL
            String deleteFromProductosMySQL = "DELETE FROM productos WHERE id_producto = ?;";
            try (PreparedStatement psDeleteProductosMySQL = mysqlconn.prepareStatement(deleteFromProductosMySQL)) {
                psDeleteProductosMySQL.setInt(1, idProducto);
                int rowsDeleted = psDeleteProductosMySQL.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Producto eliminado de MySQL.");
                } else {
                    System.out.println("Producto no encontrado en MySQL.");
                }
            }

            // Confirmar transacción en MySQL
            mysqlconn.commit();

            // Iniciar transacción en PostgreSQL
            postgresConn.setAutoCommit(false);

            // Eliminar de almacenes_productos en PostgreSQL usando el id_producto
            String deleteFromAlmacenesProductosPostgres = "DELETE FROM objetos.almacenes_productos WHERE id_producto = ?;";
            try (PreparedStatement psDeleteAlmacenesProductosPostgres = postgresConn.prepareStatement(deleteFromAlmacenesProductosPostgres)) {
                psDeleteAlmacenesProductosPostgres.setInt(1, idProducto);
                psDeleteAlmacenesProductosPostgres.executeUpdate();
            }

            // Eliminar de productos en PostgreSQL
            String deleteFromProductosPostgres = "DELETE FROM objetos.productos WHERE id_producto = ?;";
            try (PreparedStatement psDeleteProductosPostgres = postgresConn.prepareStatement(deleteFromProductosPostgres)) {
                psDeleteProductosPostgres.setInt(1, idProducto);
                psDeleteProductosPostgres.executeUpdate();
            }

            // Confirmar transacción en PostgreSQL
            postgresConn.commit();

            System.out.println("Producto eliminado de PostgreSQL.");

        } catch (SQLException e) {
            System.out.println("Error al eliminar el producto: " + e.getMessage());
            try {
                if (mysqlconn != null) mysqlconn.rollback();
                if (postgresConn != null) postgresConn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error al hacer rollback: " + rollbackEx.getMessage());
            }
        }
    }


    //Método para listar bajo stock en los productos (menos de X unidades disponibles) (MySQL)
    static void listarProductosBajoStock(int stock) {
        //Consulta SQL para productos con bajo stock
        String sqlStock = "SELECT nombre_producto, stock FROM productos WHERE stock < ?"; // Consulta con el parámetro

        try (PreparedStatement pst = mysqlconn.prepareStatement(sqlStock)) {
            pst.setInt(1, stock); // Asignamos el parámetro del stock
            ResultSet rs = pst.executeQuery();

            // Verificamos si hay resultados antes de imprimir
            boolean productosBajoStock = false;
            System.out.println("Productos con bajo stock (menos de " + stock + " unidades):");

            while (rs.next()) {
                String nombreProducto = rs.getString("nombre_producto");
                int cantidadStock = rs.getInt("stock");
                System.out.println("Producto: " + nombreProducto + ", Stock Disponible: " + cantidadStock);
                productosBajoStock = true;
            }

            // Si no se encontraron productos con bajo stock
            if (!productosBajoStock) {
                System.out.println("No hay productos con stock inferior a " + stock + " unidades.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Método para obtener el total de pedidos realizados por cada usuario (MySQL)
    static void obtenerTotalPedidosUsuarios() {
        String sqlTotalPedidos = "SELECT u.nombre, COUNT(p.id_pedido) AS total_pedidos "
                + "FROM usuarios u "
                + "LEFT JOIN pedidos p ON u.id_usuario = p.id_usuario "
                + "GROUP BY u.id_usuario";

        try (Statement statement = mysqlconn.createStatement();
            ResultSet rs = statement.executeQuery(sqlTotalPedidos)){

            System.out.println("Total de pedidos por usuario:");
            while (rs.next()) {
                String nombreUsuario = rs.getString("nombre");
                int totalPedidos = rs.getInt("total_pedidos");
                System.out.println("Usuario: " + nombreUsuario + ", Total de Pedidos: " + totalPedidos);
            }

        } catch (SQLException e) {
            System.out.println("Error al hacer una consulta en la base de datos." + e.toString());
            e.toString();
        }

    }

    //Método para obtener la cantidad de productos almacenados por cada almacén (PostgreSQL)
    static void obtenerCantidadProductosEnCadaAlmacen() {
        String sqlCantidadProductos = "SELECT a.nombre_almacen, SUM(ap.cantidad) AS total_productos "
                + "FROM objetos.almacenes a "
                + "JOIN objetos.almacenes_productos ap ON a.id_almacen = ap.id_almacen "
                + "GROUP BY a.id_almacen";

        try (Statement stmt = postgresConn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlCantidadProductos)) {

            System.out.println("Cantidad de productos en cada almacén:");
            while (rs.next()) {
                String nombreAlmacen = rs.getString("nombre_almacen");
                int totalProductos = rs.getInt("total_productos");
                System.out.println("Almacén: " + nombreAlmacen + ", Total de Productos: " + totalProductos);
            }
        } catch (SQLException e) {
            System.out.println("Error, en hacer la consulta " + e.toString());
            e.printStackTrace();
        }
    }


    //Método para listar todos los productos con sus respectivas categorías y proveedores (PostgreSQL)
    static void listarTodosProductosConCategoriaYProveedor() {
        // Consulta PostgresSQL
        String sqlPostgres  = "SELECT p.id_producto, pr.nombre_proveedor, "
                + "(pr.contacto).nombre_contacto, (pr.contacto).nif, (pr.contacto).telefono, (pr.contacto).email, "
                + "c.nombre_categoria "
                + "FROM objetos.productos p "
                + "JOIN objetos.proveedores pr ON p.id_proveedor = pr.id_proveedor "
                + "JOIN objetos.categorias c ON p.id_categoria = c.id_categoria";


        try (Statement smtPostgres = postgresConn.createStatement();
            ResultSet rsPostgres = smtPostgres.executeQuery(sqlPostgres)){

            // Mientras recorremos los productos de PostgreSQL
            while (rsPostgres.next()) {
                // Obtener información de PostgreSQL
                int idProducto = rsPostgres.getInt("id_producto");
                String nombreProveedor = rsPostgres.getString("nombre_proveedor");
                String nombreContacto = rsPostgres.getString("nombre_contacto");
                String nif = rsPostgres.getString("nif");
                String telefono = rsPostgres.getString("telefono");
                String email = rsPostgres.getString("email");
                String nombreCategoria = rsPostgres.getString("nombre_categoria");

                // Consulta MySQL para obtener precio y stock del producto
                String queryMySQL = "SELECT precio, stock FROM productos WHERE id_producto = ?";
                try (PreparedStatement stmtMySQL = mysqlconn.prepareStatement(queryMySQL)) {
                    stmtMySQL.setInt(1, idProducto);
                    ResultSet rsMySQL = stmtMySQL.executeQuery();

                    if (rsMySQL.next()) {
                        double precio = rsMySQL.getDouble("precio");
                        int stock = rsMySQL.getInt("stock");

                        // Mostrar toda la información concatenada
                        System.out.println("ID Producto: " + idProducto);
                        System.out.println("Proveedor: " + nombreProveedor);
                        System.out.println("Nombre de Contacto: " + nombreContacto);
                        System.out.println("Proveedor NIF: " + nif);
                        System.out.println("Proveedor Teléfono: " + telefono);
                        System.out.println("Proveedor Email: " + email);
                        System.out.println("Categoría: " + nombreCategoria);
                        System.out.println("Precio: " + precio);
                        System.out.println("Stock: " + stock);
                        System.out.println("----------------------------");
                    }
                }
            }

        }catch (SQLException e) {
            System.out.println("Error al hacer la consulta en la base de datos." + e.toString());
            e.printStackTrace();
        }



    }

    //Método obtener todos los usuarios que han comprado algún producto de una categoria dada (MySQL + PostgreSQL)
    static void obtenerUsuariosCompraronProductosCategoria(int idCategoria) {

        try {
            //Paso 1: Obtener los productos de la categoría en PostgreSQL
            String sqlPostgres = "SELECT id_producto FROM objetos.productos WHERE id_categoria = ?";
            PreparedStatement statementPostgres = postgresConn.prepareStatement(sqlPostgres);

            statementPostgres.setInt(1, idCategoria);

            ResultSet rsPostgres = statementPostgres.executeQuery();

            //Crear una lista para almacenar los IDs de los productos obtenidos
            List<Integer> productoIds = new ArrayList<>();

            while (rsPostgres.next()) {
                productoIds.add(rsPostgres.getInt("id_producto"));
            }

            //Verificamos si se encontraron los productos
            if (productoIds.isEmpty()) {
                System.out.println("No se encontraron productos en esta categoría");
                return;
            }

            // Paso 2: Obtener los usuarios que han comprado estos productos desde MySQL
            // Convertimos la lista de IDs en una cadena para hacer la consulta SQL
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < productoIds.size(); i++) {
                sb.append("?");

                if (i < productoIds.size() - 1) {
                    sb.append(",");
                }
            }

            //Consulta SQL para obtener los usuarios que han comprado estos productos
            String sqlMysql = "SELECT DISTINCT u.nombre "
                    + "FROM pedidos_productos pp "
                    + "JOIN pedidos p ON pp.id_pedido = p.id_pedido "
                    + "JOIN usuarios u ON p.id_usuario = u.id_usuario "
                    + "WHERE pp.id_producto IN (" + sb.toString() + ")";

            PreparedStatement statementMysql = mysqlconn.prepareStatement(sqlMysql);

            //Establecer los parámetros para consulta MYSQL
            for (int i = 0; i<productoIds.size(); i++) {
                statementMysql.setInt(i + 1, productoIds.get(i));
            }

            //Ejecutar los resultados
            ResultSet rsMysql = statementMysql.executeQuery();

            //Mostrar los resultados
            System.out.println("Usuarios que han comprado productos de la categoría con ID " + idCategoria + ":");

            while (rsMysql.next()) {
                System.out.println(rsMysql.getString("nombre"));
            }

        }catch (SQLException e) {
            System.out.println("Error al comprobar la categoría, revisa la ID. " + e.toString());
            e.printStackTrace();
        }
    }
}
