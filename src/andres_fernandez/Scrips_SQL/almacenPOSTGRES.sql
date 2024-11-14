-- Crear el esquema si no existe
CREATE SCHEMA IF NOT EXISTS objetos;

-- Crear un tipo compuesto para el atributo contacto
CREATE TYPE objetos.contacto_tipo AS (
    nombre_contacto VARCHAR(100),
    nif VARCHAR(20),
    telefono INT,
    email VARCHAR(100)
);

-- Crear la tabla de proveedores con el atributo contacto usando el tipo compuesto
CREATE TABLE objetos.proveedores (
    id_proveedor SERIAL PRIMARY KEY,
    nombre_proveedor VARCHAR(100) NOT NULL,
    contacto objetos.contacto_tipo NOT NULL
);

-- Crear la tabla de categorías
CREATE TABLE objetos.categorias (
    id_categoria SERIAL PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL
);

-- Crear la tabla de productos con relaciones a proveedores y categorías
CREATE TABLE objetos.productos (
    id_producto SERIAL PRIMARY KEY,
    id_proveedor INT NOT NULL,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_proveedor) REFERENCES objetos.proveedores(id_proveedor) ON UPDATE CASCADE,
    FOREIGN KEY (id_categoria) REFERENCES objetos.categorias(id_categoria) ON UPDATE CASCADE
);

-- Crear la tabla de almacenes
CREATE TABLE objetos.almacenes (
    id_almacen SERIAL PRIMARY KEY,
    nombre_almacen VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(100) NOT NULL
);

-- Crear la tabla de relación almacenes_productos (relación varios a varios)
CREATE TABLE objetos.almacenes_productos (
    id_almacen INT,
    id_producto INT,
    cantidad INT NOT NULL,
    PRIMARY KEY (id_almacen, id_producto),
    FOREIGN KEY (id_almacen) REFERENCES objetos.almacenes(id_almacen) ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES objetos.productos(id_producto) ON UPDATE CASCADE
);

-- Insertar datos en la tabla de proveedores
INSERT INTO objetos.proveedores (nombre_proveedor, contacto) VALUES
('Mayor 210', ('Juan Pérez', '12345678A', '987654321', 'juan@gmail.com')),
('PCcomponenetes', ('María García', '23456789B', '912345678', 'maria@gmail.com')),
('Almazón', ('Carlos López', '34567890C', '934567890', 'carlos@outlook.com')),
('Alcampo', ('Ana Rodríguez', '45678901D', '945678901', 'ana@outlook.com'));

-- Insertar datos en la tabla de categorías
INSERT INTO objetos.categorias (nombre_categoria) VALUES
('Electrónica'),
('Muebles'),
('Ropa'),
('Alimentos');

-- Insertar datos en la tabla de productos (esto asigna los id_producto correctamente)
INSERT INTO objetos.productos (id_proveedor, id_categoria) VALUES
(1, 1),  -- Producto 1 de Proveedor Mayor 210 y categoría Electrónica
(2, 2),  -- Producto 2 de Proveedor PCcomponenetes y categoría Muebles
(3, 3),  -- Producto 3 de Proveedor Almazón y categoría Ropa
(4, 4);  -- Producto 4 de Proveedor Alcampo y categoría Alimentos

-- Insertar datos en la tabla de almacenes
INSERT INTO objetos.almacenes (nombre_almacen, ubicacion) VALUES
('Almacén Central', 'Madrid'),
('Almacén Norte', 'Barcelona'),
('Almacén Sur', 'Sevilla'),
('Almacén Este', 'Valencia');

-- Insertar datos en la tabla de relación almacenes_productos
-- Usamos los id_producto generados automáticamente (1, 2, 3, 4) y los id_almacen (1, 2, 3, 4)
INSERT INTO objetos.almacenes_productos (id_almacen, id_producto, cantidad) VALUES
(1, 1, 100),  -- 100 unidades del Producto 1 en Almacén Central
(2, 2, 50),   -- 50 unidades del Producto 2 en Almacén Norte
(3, 3, 200),  -- 200 unidades del Producto 3 en Almacén Sur
(4, 4, 150);  -- 150 unidades del Producto 4 en Almacén Este