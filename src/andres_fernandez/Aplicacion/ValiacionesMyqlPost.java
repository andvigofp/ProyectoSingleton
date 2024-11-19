package andres_fernandez.Aplicacion;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class ValiacionesMyqlPost {
    
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
    static Double pedirDouble(Scanner teclado, String mensaje) {
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

    //Método de validar nombre de categoría que solo pueda poner letras
    static String obtenerNombreCategoriaValido(Scanner teclado) {
        do {
            String nombreCategoria = pedirStringLine(teclado, "Ingrese el nombre de la categoría a insertar: ");

            // Validar que solo contenga letras
            if (nombreCategoria.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                // Convertir a minúsculas (o mayúsculas si lo prefieres)
                return nombreCategoria;
            } else {
                System.out.println("Error: El nombre de la categoría solo debe contener letras.");
            }
        } while (true); //Repite hasta que el nombre sea válido
    }


    // Método para validar el nombre del proveedor
    static String esNombreValidoProveeedor(Scanner teclado) {
        do {
            String nombreProveedor = pedirStringLine(teclado, "Ingrese el nombre del proveedor a insertar: ");

            // Validar que solo contenga letras y números
            if (nombreProveedor.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+")) {
                return nombreProveedor;
            } else {
                System.out.println("Error: El nombre del proveedor solo debe contener letras, números y espacios.");
            }
        } while (true); //Repite hasta que el nombre sea válido
    }

    // Método para validar el NIF (8 números y una letra)
    static String esNifValidoProveedor(Scanner teclado) {
        do {
            String nif = pedirStringLine(teclado, "Ingresa el nif del proveedor a insertar: ");

            // Validar que solo contenga 8 números y una letra
            if (nif.matches("\\d{8}[a-zA-Z]")) {
                return nif;
            } else {
                System.out.println("Error: El nif del proveedor solo debe contener 8 números y letra.");
            }
        } while (true); //Repite hasta que el nif sea válido
    }

    // Método para validar el teléfono (solo números)
    static String esTelefonoValidoProveedor(Scanner teclado) {
        do {
            String telefono = pedirStringLine(teclado, "Ingrese el número teléfono del proveedor a insertar: ");

            if (telefono.matches("\\d{9}")) {
                return telefono;
            } else {
                System.out.println("Error: El teléfono del proveedor solo debe contener un máximo de 9 números.");
            }
        } while (true); // Repite hasta que el teléfono sea válido

    }

    // Método para validar el email con un formato válido
    static String esEmailValidoProveedor(Scanner teclado) {
        do {
            String email = pedirStringLine(teclado, "Ingrese el email del proveedor  a insertar: ");
            if (email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
                return email;
            } else {
                System.out.println("Error: El email del proveedor solo debe contener letras, números y carácter especial @.");
            }
        } while (true); // Repite hasta que el email sea válido
    }

    // Método para validar el nombre con un formato válido
    static String esNombreUsuario(Scanner teclado) {
        do {
            String nombreUsuario = pedirStringLine(teclado, "Introduce el nombre del usuario a insertar");

            if (nombreUsuario.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+")) {
                return nombreUsuario;
            }else {
                System.out.println("Error: El nombre del usuario solo debe contener letras, números y espacios.");
            }
        }while (true);
    }

    // Método para validar el id con un formato válido
    static String esIdProveedor(Scanner teclado) {
        do {
            String id = pedirStringLine(teclado, "Ingrese la ID del proveedor a eliminar: ");

            if (id.matches("\\d+")) {
                return id;
            }else {
                System.out.println("Error. El id del proveedor solo puede contener números.");
            }
        }while (true); // Repite hasta que el email sea válido
    }

    // Método para validar el email con un formato válido
    static String esEmailValidoUsuario(Scanner teclado) {
        do {
            String email = pedirStringLine(teclado, "Ingrese el email del usuario a insertar: ");
            if (email.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
                return email;
            } else {
                System.out.println("Error: El email del usuario solo debe contener letras, números y carácter especial @.");
            }
        } while (true); // Repite hasta que el email sea válido
    }


    // Método para validar el año nacimiento con un formato válido
    static String esFechaNacimentoValidaUusario(Scanner teclado) {
        do {
            String ano_nacimento = pedirStringLine(teclado, "Introduce el año de nacimiento del usuario a insertar");

            if (ano_nacimento.matches("\\d+")) {
                return ano_nacimento;
            }else {
                System.out.println("Error: El año de nacimiento del usuario solo puede tener un máximo de cuatro números.");
            }
        }while (true); // Repite hasta que el email sea válido
    }

    // Método para validar el id usuario con un formato válido
    static String esidUsuarioElimnar(Scanner teclado) {
        do {
            String id = pedirStringLine(teclado, "Introduce la ID del usuario a eliminar: ");

            if (id.matches("\\d+")) {
                return id;
            }else {
                System.out.println("Error. El id del proveedor solo puede contener números.");
            }
        }while (true); // Repite hasta que el id sea válido
    }

        // Método para validar y capturar los datos del producto
        static ArrayList<String> esNombreCrearProducto(Scanner teclado) {
            ArrayList<String> productoData = new ArrayList<>();

            while (true) {
                // Validación solo del nombre
                String nombre = pedirStringLine(teclado, "Ingrese el nombre del producto a insertar: ");
                while (!nombre.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+")) {
                    System.out.println("Error. El nombre solo puede contener letras, números y espacios.");
                    nombre = pedirStringLine(teclado, "Ingrese el nombre del producto a insertar: ");
                }

                // Validación de los otros campos
                String precio = pedirStringLine(teclado, "Ingrese el precio del producto a insertar: ");
                while (!precio.matches("\\d+(\\.\\d{1,2})?")) {
                    System.out.println("Error. El precio debe ser un número positivo, con hasta dos decimales.");
                    precio = pedirStringLine(teclado, "Ingrese el precio del producto a insertar: ");
                }

                String stock = pedirStringLine(teclado, "Ingrese el stock del producto a insertar: ");
                while (!stock.matches("\\d+")) {
                    System.out.println("Error. El stock debe ser un número entero.");
                    stock = pedirStringLine(teclado, "Ingrese el stock del producto a insertar: ");
                }

                String categoria = pedirStringLine(teclado, "Ingrese el nombre de la categoría del producto a insertar: ");
                while (!categoria.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+")) {
                    System.out.println("Error. La categoría solo puede contener letras, números y espacios.");
                    categoria = pedirStringLine(teclado, "Ingrese el nombre de la categoría del producto a insertar: ");
                }

                // Si todos los campos son válidos, los agregamos al ArrayList
                productoData.add(nombre);
                productoData.add(precio);
                productoData.add(stock);
                productoData.add(categoria);

                return productoData;  // Devolvemos los datos cuando son válidos
            }
        }

    //Método para validar el nombre producto con un formato válido
    static String esNombreProductoEliminar(Scanner teclado) {
        do {
            String nombreProducto = pedirStringLine(teclado, "Introduce el nombre del producto a eliminar: ");

            if (nombreProducto.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s]+")) {
                return nombreProducto;
            }else {
                System.out.println("Error: El nombre del producto solo debe contener letras, números y espacios.");
            }
        }while (true);
    }

    // Método para validar el stock con un formato válido
    static String esStock(Scanner teclado) {
        do {
            String stock = pedirStringLine(teclado, "Introduce stock para listar (menos de X unidades disponibles): ");

            if (stock.matches("\\d+")) {
                return stock;
            }else {
                System.out.println("Error. El stock de los productos solo puede contener números.");
            }
        }while (true);
    }

    // Método para validar el id de la categoría con un formato válido
    static String esidCategoria(Scanner teclado) {
        do {
            String id = pedirStringLine(teclado, "Ingrese el ID de la categoría: ");

            if (id.matches("\\d+")) {
                return id;
            }else {
                System.out.println("Error. El id de la categoría solo puede contener números.");
            }
        }while (true); // Repite hasta que el id sea válido
    }

    // Método para validar el nif de la categoría con un formato válido
    static String esNifCrearProducto(Scanner teclado) {
        do {
            String nif = pedirStringLine(teclado, "Ingrese el dni del proveedor del producto a insertar: ");

            if (nif.matches("\\d{8}[a-zA-Z]")) {
                return nif;
            }else {
                System.out.println("Error: El nif del proveedor solo debe contener 8 números y letra.");
            }
            }while (true); // Repite hasta que el nif sea válido
        }
    }

