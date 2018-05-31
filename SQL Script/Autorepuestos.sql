

CREATE TABLE Factura (
	factura_ID INT PRIMARY KEY auto_increment,
	fecha DATETIME NOT NULL,
	total DECIMAL(8,2) NOT NULL,
	cliente_ID INT NOT NULL,
	empleado_ID INT NOT NULL,
    CONSTRAINT FK_ClienteFactura FOREIGN KEY  (cliente_ID) REFERENCES Cliente(cliente_ID),
	CONSTRAINT FK_EmpleadoFactura FOREIGN KEY  (empleado_ID) REFERENCES Empleado(empleado_ID)
);


CREATE TABLE Venta (
	venta_ID INT PRIMARY KEY auto_increment,
	cantidad INT NOT NULL,
	factura_ID INT NOT NULL,
    componente_ID INT NOT NULL,
    CONSTRAINT FK_ComponenteVenta FOREIGN KEY  (componente_ID) REFERENCES Componente(componente_ID),
	CONSTRAINT FK_FacturaVenta FOREIGN KEY  (factura_ID) REFERENCES Factura(factura_ID)
);


CREATE TABLE CategoriaComponente (
	categoriaComponente_ID INT PRIMARY KEY auto_increment,
	nombre VARCHAR ( 40 ) NOT NULL,
	descripcion VARCHAR ( 300 ) NOT NULL
);

CREATE TABLE Usuario (
	usuario_ID INT PRIMARY KEY auto_increment,
	usuario VARCHAR ( 30 ) NOT NULL,
	clave VARCHAR ( 30 ) NOT NULL,
    estado VARCHAR ( 10) NOT NULL,
	empleado_ID INT NOT NULL, 
	
	CONSTRAINT FK_EmpleadoUsuario FOREIGN KEY (empleado_ID)
    REFERENCES Empleado(empleado_ID)
);


CREATE TABLE Empleado (
	empleado_ID INT PRIMARY KEY auto_increment,
	cedula VARCHAR ( 18 ) NOT NULL,
	nombres VARCHAR ( 40 ) NOT NULL,
	apellidos VARCHAR ( 40 ) NOT NULL,
	sexo VARCHAR(10) NOT NULL
);

CREATE TABLE Cliente (
	cliente_ID INT PRIMARY KEY auto_increment,
	cedula VARCHAR ( 18 ) NOT NULL,
	nombres VARCHAR ( 30 ) NOT NULL,
	apellidos VARCHAR ( 30 ) NOT NULL,
	sexo VARCHAR(10) NOT NULL
);

CREATE TABLE Fabricante (
	fabricante_ID INT PRIMARY KEY auto_increment,
	nombre VARCHAR ( 30 ) NOT NULL
);




CREATE TABLE TipoComponente (
	tipoComponente_ID INT PRIMARY KEY auto_increment,
	nombre VARCHAR ( 50 ) NOT NULL,
	descripcion VARCHAR ( 300 ) NOT NULL
);
CREATE TABLE Componente (
	componente_ID INT PRIMARY KEY auto_increment,
	descripcion VARCHAR ( 100 ) NOT NULL,
	precioUnitario  DECIMAL(8,2) NOT NULL,
	stock INT NOT NULL,
	categoriaComponente_ID INT NOT NULL,
	tipoComponente_ID INT NOT NULL,
	fabricante_ID INT NOT NULL,
	CONSTRAINT FK_CategoriaComponenteComponente FOREIGN KEY  (categoriaComponente_ID) REFERENCES CategoriaComponente(categoriaComponente_ID),
	CONSTRAINT FK_TipoComponenteComponente FOREIGN KEY  (tipoComponente_ID) REFERENCES TipoComponente(tipoComponente_ID),
    CONSTRAINT FK_FabricanteComponente FOREIGN KEY  (fabricante_ID) REFERENCES Fabricante(fabricante_ID)
);

SELECT c.componente_ID, c.descripcion, c.precioUnitario, c.stock, t.nombre, m.nombre, s.nombre
FROM Componente c INNER JOIN TipoComponente t ON c.tipoComponente_ID = t.tipoComponente_ID INNER JOIN
Fabricante m ON c.fabricante_ID = m.fabricante_ID INNER JOIN CategoriaComponente s ON c.categoriaComponente_ID =
s.categoriaComponente_ID






