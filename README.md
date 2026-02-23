
## Herramientas
![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![MariaDB](https://img.shields.io/badge/MariaDB-3.4.1-blue?style=for-the-badge&logo=mariadb)
![Maven](https://img.shields.io/badge/Maven-4.0.0-red?style=for-the-badge&logo=apache-maven)
![Swing](https://img.shields.io/badge/Swing-GUI-green?style=for-the-badge)

## Tabla de Contenidos

- [Descripción General](#-descripción-general)
- [Características Principales](#-características-principales)
- [Arquitectura del Proyecto](#-arquitectura-del-proyecto)
- [Requisitos del Sistema](#-requisitos-del-sistema)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Documentación Técnica](#-documentación-técnica)
  - [Modelos de Datos](#modelos-de-datos)
  - [Capa de Acceso a Datos (DAO)](#capa-de-acceso-a-datos-dao)
  - [Utilidades](#utilidades)
  - [Interfaz de Usuario](#interfaz-de-usuario)
- [Guía de Uso](#-guía-de-uso)
- [Base de Datos](#-base-de-datos)
- [Seguridad](#-seguridad)
- [Troubleshooting](#-troubleshooting)

---

## Descripción General

**Database Manager** es una herramienta administrativa completa para bases de datos MariaDB, desarrollada como parte del proyecto TBD2.  Esta herramienta permite la administración de los principales objetos de la base de datos,
gestionando conexiones y ejecutando comandos SQL, interactuando directamente con las tablas del sistema (system tables) del SGBD.

### Objetivo del Proyecto

Proporcionar una interfaz gráfica de usuario (GUI) robusta y amigable que permita:
- Gestión completa de múltiples conexiones de bases de datos
- Administración de bases de datos
- Ejecución de consultas SQL con visualización de resultados
- Exploración de estructuras de bases de datos
- Sistema de autenticación de usuarios
- Gestión segura de credenciales de conexión

---

## Características Principales

### Sistema de Autenticación
- **Login/Registro de usuarios**: Sistema completo de gestión de cuentas
- **Sesiones persistentes**: Manejo de sesiones de usuario activas
- **Validación de credenciales**: Autenticación segura contra base de datos

### Gestor de Conexiones
- **Múltiples conexiones**: Soporte para gestionar conexiones a diferentes servidores MariaDB
- **Prueba de conexión**: Verificación de conectividad antes de guardar
- **CRUD completo**: Crear, leer, actualizar y eliminar conexiones
- **Almacenamiento seguro**: Credenciales almacenadas por usuario

### Administración de Bases de Datos
- **Listar bases de datos**: Visualización de todas las bases de datos disponibles
- **Filtrado de bases del sistema**: Protección automática de bases de datos del sistema (information_schema, mysql, performance_schema, sys)
- **Información detallada**: Consulta de metadata y estructura

### Editor SQL
- **Ejecución de consultas**: Soporte para SELECT, INSERT, UPDATE, DELETE, etc.
- **Visualización de resultados**: Tabla dinámica con resultados de consultas
- **Comandos DDL**: Soporte para SHOW, DESCRIBE, CREATE, ALTER, DROP
- **Feedback en tiempo real**: Mensajes de éxito/error y contador de filas afectadas

### Explorador de Estructura
- **Listado de tablas**: Visualización de todas las tablas en una base de datos
- **Estructura de columnas**: Información detallada de cada columna (nombre, tipo, nulabilidad, claves, valores por defecto)
- **Metadata del sistema**: Acceso directo a las tablas del sistema de MariaDB

---

## Arquitectura del Proyecto

El proyecto sigue un patrón de arquitectura **MVC (Model-View-Controller)** con una capa adicional de **DAO (Data Access Object)** para la separación de responsabilidades:

```
┌─────────────────────────────────────────────────────────────┐
│                        INTERFAZ DE USUARIO (View)            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │  LoginFrame  │  │RegisterFrame │  │DashboardFrame│      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ConnectionDialog│ │CreateDatabase│ │ SqlEditor GUI│      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE LÓGICA (Controller)              │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ConexionDAO   │  │  DBManager   │  │  SqlEditor   │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     CAPA DE DATOS (Model)                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Usuario    │  │   Conexion   │  │ResultadoSql  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     UTILIDADES (Utilities)                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ DBConnection │  │   Session    │  │ ColumnaInfo  │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                     BASE DE DATOS MariaDB                    │
│              tool_manager (Sistema) + Bases Gestionadas      │
└─────────────────────────────────────────────────────────────┘
```

---

##  Requisitos del Sistema

### Software Requerido

| Componente | Versión Mínima | Recomendada | Notas |
|------------|----------------|-------------|-------|
| **Java JDK** | 17 | 17+ | OpenJDK o Oracle JDK |
| **Maven** | 3.6+ | 3.8+ | Para gestión de dependencias |
| **MariaDB** | 10.5+ | 10.11+ | Servidor de base de datos |
| **IDE** | - | NetBeans/IntelliJ/Eclipse | Opcional pero recomendado |

### Dependencias Maven

```xml
<!-- MariaDB JDBC Driver -->
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>3.4.1</version>
</dependency>
```

---

## 🚀 Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Scastelar/Database-Manager.git
cd Database-Manager
```

### 2. Configurar la Base de Datos del Sistema

El proyecto requiere una base de datos llamada `tool_manager` para almacenar usuarios y conexiones.

```sql
-- Base de datos principal del sistema
create database tool_manager;
use tool_manager;

-- Tabla de usuarios 
create table usuarios (
	id int auto_increment primary key,
	username varchar(50) unique not null,
	password_hash varchar(100) not null

);

-- Tabla de conexiones 
create table conexiones(
	id int auto_increment primary key,
	usuario_id int,
	nombre varchar(50),
	tipo varchar(20),
	host varchar(50), 
	puerto int,
	db_name varchar(50),
	db_user varchar(50),
	db_password varchar(50),
	foreign key (usuario_id) references usuarios(id)
);
```

### 3. Configurar Credenciales de Conexión

Editar el archivo `src/main/java/util/DBConnection.java`:

```java
private static final String URL = "jdbc:mariadb://localhost:3306/tool_manager";
private static final String USER = "root"; // Tu usuario de MariaDB
private static final String PASSWORD = "tu_password"; // Tu contraseña
```

### 4. Compilar el Proyecto

```bash
mvn clean compile
```

### 5. Ejecutar la Aplicación

```bash
mvn exec:java -Dexec.mainClass="util.Main"
```

O desde tu IDE, ejecutar la clase `Main.java`

---

##  Estructura del Proyecto

```
Database-Manager/
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── Modelos/              # Modelos de datos
│       │   │   ├── Conexion.java     # Modelo de conexión DB
│       │   │   └── Usuario.java      # Modelo de usuario
│       │   │
│       │   ├── dao/                  # Data Access Objects
│       │   │   └── ConexionDAO.java  # DAO para conexiones
│       │   │
│       │   ├── ui/                   # Interfaz de usuario (Swing)
│       │   │   ├── LoginFrame.java   # Ventana de login
│       │   │   ├── RegisterFrame.java # Ventana de registro
│       │   │   ├── DashboardFrame.java # Panel principal
│       │   │   ├── ConnectionDialog.java # Diálogo de conexión
│       │   │   └── CreateDatabaseDialog.java # Diálogo crear DB
│       │   │
│       │   ├── util/                 # Utilidades
│       │   │   ├── Main.java         # Punto de entrada
│       │   │   ├── DBConnection.java # Conexión sistema
│       │   │   ├── DBManager.java    # Gestor de bases de datos
│       │   │   ├── SqlEditor.java    # Editor SQL
│       │   │   ├── Session.java      # Gestión de sesión
│       │   │   ├── ResultadoSql.java # Resultado de consultas
│       │   │   └── ColumnaInfo.java  # Info de columnas
│       │   │
│       │   └── icons/                # Iconos de la aplicación
│       │
│       └── resources/                # Recursos adicionales
│
├── target/                           # Archivos compilados
├── pom.xml                           # Configuración Maven
├── nbactions.xml                     # Configuración NetBeans
└── README.md                         # Este archivo
```

---

## Documentación Técnica

### Modelos de Datos

#### 1. **Usuario** (`Modelos/Usuario.java`)

Representa un usuario del sistema con capacidad de login y gestión de conexiones.

```java
public class Usuario {
    private int id;              // ID único del usuario
    private String username;     // Nombre de usuario único
    private String password;     // Contraseña (no se usa directamente)
    
    // Constructor
    public Usuario(int id, String username)
    
    // Getters y Setters
}
```

**Campos:**
- `id`: Identificador único autogenerado
- `username`: Nombre de usuario único (validado en BD)
- `password`: Campo para gestión de contraseña

**Uso:**
```java
Usuario usuario = new Usuario(1, "admin");
int userId = usuario.getId();
String username = usuario.getUsername();
```

---

#### 2. **Conexion** (`Modelos/Conexion.java`)

Modelo que encapsula los parámetros de conexión a una base de datos MariaDB.

```java
public class Conexion {
    private int id;              // ID de la conexión
    private String nombre;       // Nombre descriptivo
    private String tipo;         // Tipo de BD (MariaDB)
    private String host;         // Servidor (localhost, IP, dominio)
    private int puerto;          // Puerto (default: 3306)
    private String database;     // Nombre de la base de datos
    private String usuario;      // Usuario de la BD
    private String password;     // Contraseña de la BD
    
    // Constructores
    public Conexion(int id, String nombre, String tipo, String host, 
                    int puerto, String database, String usuario, String password)
    public Conexion(String nombre, String tipo, String host, int puerto,
                    String database, String usuario, String password) // Sin ID
    
    // Método clave: Genera URL JDBC
    public String getJDBC()
}
```

**Método Especial: `getJDBC()`**

Genera la URL JDBC completa con parámetros de conexión optimizados:

```java
public String getJDBC() {
    String dbPart = (database != null && !database.isEmpty()) ? "/" + database : "";
    
    return "jdbc:mariadb://" + host + ":" + puerto + dbPart + 
            "?useSSL=false" + 
            "&allowPublicKeyRetrieval=true" +
            "&useLocalSessionState=true" + 
            "&rewriteBatchedStatements=true";
}
```

**Parámetros JDBC:**
- `useSSL=false`: Desactiva SSL (para desarrollo local)
- `allowPublicKeyRetrieval=true`: Permite autenticación con clave pública
- `useLocalSessionState=true`: Optimiza gestión de sesiones
- `rewriteBatchedStatements=true`: Mejora rendimiento de inserts por lotes

**Ejemplo de Uso:**
```java
Conexion conn = new Conexion("Mi Servidor", "MariaDB", "localhost", 
                             3306, "mi_database", "root", "password");
String jdbcUrl = conn.getJDBC();
// Resultado: jdbc:mariadb://localhost:3306/mi_database?useSSL=false&...
```

---

### Capa de Acceso a Datos (DAO)

#### **ConexionDAO** (`dao/ConexionDAO.java`)

Maneja todas las operaciones CRUD para las conexiones de bases de datos en la tabla `conexiones`.

**Métodos Principales:**

1. **`obtenerConexionPorID(int id)`**
   - Recupera una conexión específica del usuario actual
   - Validación de pertenencia al usuario (seguridad)
   ```java
   String sql = "SELECT * FROM conexiones WHERE id=? AND usuario_id=?";
   ```

2. **`guardarConexion(Conexion conexion)`**
   - Guarda una nueva conexión en la base de datos
   - Asocia automáticamente con el usuario de la sesión
   ```java
   String sql = "INSERT INTO conexiones (usuario_id, nombre, tipo, host, puerto, 
                 db_name, db_user, db_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
   ```

3. **`obtenerConexionesUsuario()`**
   - Lista todas las conexiones del usuario actual
   - Retorna `List<Conexion>`
   ```java
   String sql = "SELECT * FROM conexiones WHERE usuario_id = ?";
   ```

4. **`actualizarConexion(Conexion conexion)`**
   - Actualiza una conexión existente
   - Valida pertenencia al usuario
   ```java
   String sql = "UPDATE conexiones SET nombre=?, tipo=?, host=?, puerto=?, 
                 db_name=?, db_user=?, db_password=? WHERE id=? AND usuario_id=?";
   ```

**Características de Seguridad:**
- Todas las consultas usan `PreparedStatement` (prevención de SQL Injection)
- Validación de `usuario_id` en todas las operaciones
- Uso de la sesión activa (`Session.usuarioActual.getId()`)

**Ejemplo de Uso:**
```java
ConexionDAO dao = new ConexionDAO();

// Obtener todas las conexiones del usuario
List<Conexion> conexiones = dao.obtenerConexionesUsuario();

// Guardar una nueva conexión
Conexion nuevaConexion = new Conexion("Producción", "MariaDB", 
                                      "192.168.1.100", 3306, 
                                      "prod_db", "admin", "pass123");
boolean guardado = dao.guardarConexion(nuevaConexion);
```

---

### Utilidades

#### 1. **DBConnection** (`util/DBConnection.java`)

Gestiona la conexión a la base de datos del sistema (`tool_manager`).

```java
public class DBConnection {
    private static final String URL = "jdbc:mariadb://localhost:3306/tool_manager";
    private static final String USER = "root";
    private static final String PASSWORD = "sofia123";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        props.setProperty("disabledAuthenticationPlugins", "gssapi,auth_gssapi_client");
        
        return DriverManager.getConnection(URL, props);
    }
}
```

**Características:**
- Conexión singleton al sistema
- Desactiva plugins de autenticación GSSAPI (evita conflictos)
- Usado para operaciones de login, registro y gestión de conexiones

---

#### 2. **DBManager** (`util/DBManager.java`)

Clase utilitaria central para operaciones con bases de datos MariaDB remotas/gestionadas.

**Métodos Principales:**

1. **`conectar(Conexion conexion)`**
   - Establece conexión usando un objeto Conexion
   - Carga el driver JDBC dinámicamente
   - Configura propiedades de conexión
   ```java
   public static Connection conectar(Conexion conexion) throws SQLException {
       Class.forName("org.mariadb.jdbc.Driver");
       String jdbcUrl = conexion.getJDBC();
       Properties props = new Properties();
       props.setProperty("user", conexion.getUsuario());
       props.setProperty("password", conexion.getPassword());
       props.setProperty("disabledAuthenticationPlugins", "gssapi,auth_gssapi_client");
       return DriverManager.getConnection(jdbcUrl, props);
   }
   ```

2. **`probarConexion(Conexion conexion)`**
   - Prueba si una conexión es válida
   - Retorna `true` si la conexión se establece correctamente
   ```java
   public static boolean probarConexion(Conexion conexion) {
       try (Connection conn = conectar(conexion)) {
           return conn != null && !conn.isClosed();
       } catch (SQLException e) {
           return false;
       }
   }
   ```

3. **`crearBaseDatos(Conexion conexion, String nombreDB)`**
   - Crea una nueva base de datos en el servidor
   - Se conecta sin especificar base de datos (conexión al servidor)
   - Ejecuta `CREATE DATABASE`
   ```java
   public static boolean crearBaseDatos(Conexion conexion, String nombreDB) {
       Conexion tempConexion = new Conexion(
           conexion.getNombre(), conexion.getTipo(), conexion.getHost(),
           conexion.getPuerto(), "", // Sin base de datos específica
           conexion.getUsuario(), conexion.getPassword()
       );
       
       try (Connection conn = conectar(tempConexion);
            Statement st = conn.createStatement()) {
           String sql = "CREATE DATABASE " + nombreDB;
           st.executeUpdate(sql);
           return true;
       } catch (SQLException e) {
           return false;
       }
   }
   ```

4. **`listarBaseDatos(Conexion conexion)`**
   - Lista todas las bases de datos en el servidor
   - Ejecuta `SHOW DATABASES`
   - Retorna `List<String>`
   ```java
   public static List<String> listarBaseDatos(Conexion conexion) {
       List<String> databases = new ArrayList<>();
       try (Connection conn = conectar(conexion);
            Statement st = conn.createStatement()) {
           String sql = "SHOW DATABASES";
           ResultSet rs = st.executeQuery(sql);
           while (rs.next()) {
               databases.add(rs.getString(1));
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return databases;
   }
   ```

5. **`obtenerInfoBaseDatos(Conexion conexion, String nombreDB)`**
   - Obtiene información de creación de una base de datos
   - Ejecuta `SHOW CREATE DATABASE`
   - Retorna el DDL de la base de datos
   ```java
   public static String obtenerInfoBaseDatos(Conexion conexion, String nombreDB) {
       StringBuilder info = new StringBuilder();
       try (Connection conn = conectar(conexion);
            Statement st = conn.createStatement()) {
           String sql = "SHOW CREATE DATABASE `" + nombreDB + "`";
           ResultSet rs = st.executeQuery(sql);
           if (rs.next()) {
               info.append(rs.getString(2));
           }
       } catch (SQLException e) {
           System.err.println("Error al obtener info de BD: " + e.getMessage());
       }
       return info.toString();
   }
   ```

---

#### 3. **SqlEditor** (`util/SqlEditor.java`)

Motor de ejecución de consultas SQL con soporte para DQL, DML y DDL.

**Métodos Principales:**

1. **`ejecutarConsulta(Conexion conexion, String sql)`**
   - Ejecuta cualquier consulta SQL
   - Detecta automáticamente el tipo de consulta (SELECT vs UPDATE/INSERT/DELETE)
   - Retorna objeto `ResultadoSql`
   
   ```java
   public static ResultadoSql ejecutarConsulta(Conexion conexion, String sql) {
       ResultadoSql resultado = new ResultadoSql();
       
       try (Connection conn = DBManager.conectar(conexion)) {
           Statement st = conn.createStatement();
           String sqlTrimmed = sql.trim().toLowerCase();
           
           // Consultas que devuelven resultados
           if (sqlTrimmed.startsWith("select") || 
               sqlTrimmed.startsWith("show") ||
               sqlTrimmed.startsWith("describe") || 
               sqlTrimmed.startsWith("desc")) {
               ResultSet rs = st.executeQuery(sql);
               resultado.tableModel = construirTablaDesdeResultSet(rs);
               resultado.filas = resultado.tableModel.getRowCount();
               resultado.esExitoso = true;
           } 
           // Consultas de modificación (INSERT, UPDATE, DELETE, CREATE, etc.)
           else {
               int filasAfectadas = st.executeUpdate(sql);
               resultado.filas = filasAfectadas;
               resultado.mensaje = "Consulta ejecutada. Filas Afectadas: " + filasAfectadas;
               resultado.esExitoso = true;
           }
       } catch (SQLException e) {
           resultado.esExitoso = false;
           resultado.mensaje = "Error SQL: " + e.getMessage();
       }
       
       return resultado;
   }
   ```

2. **`construirTablaDesdeResultSet(ResultSet rs)`** (privado)
   - Convierte un `ResultSet` en `DefaultTableModel` para Swing
   - Extrae nombres de columnas y datos
   - Crea tabla no editable
   
   ```java
   private static DefaultTableModel construirTablaDesdeResultSet(ResultSet rs) throws SQLException {
       ResultSetMetaData metaData = rs.getMetaData();
       int columnCount = metaData.getColumnCount();
       
       // Nombres de columnas
       String[] columnNames = new String[columnCount];
       for (int i = 1; i <= columnCount; i++) {
           columnNames[i - 1] = metaData.getColumnName(i);
       }
       
       // Datos
       List<Object[]> data = new ArrayList<>();
       while (rs.next()) {
           Object[] row = new Object[columnCount];
           for (int i = 1; i <= columnCount; i++) {
               row[i - 1] = rs.getObject(i);
           }
           data.add(row);
       }
       
       Object[][] dataArray = data.toArray(new Object[0][]);
       return new DefaultTableModel(dataArray, columnNames) {
           @Override
           public boolean isCellEditable(int row, int column) {
               return false; // Tabla no editable
           }
       };
   }
   ```

3. **`listarTablas(Conexion conexion)`**
   - Lista todas las tablas de la base de datos actual
   - Ejecuta `SHOW TABLES`
   - Retorna `List<String>`
   
   ```java
   public static List<String> listarTablas(Conexion conexion) {
       List<String> tablas = new ArrayList<>();
       try (Connection conn = DBManager.conectar(conexion);
            Statement st = conn.createStatement()) {
           String sql = "SHOW TABLES";
           ResultSet rs = st.executeQuery(sql);
           while (rs.next()) {
               tablas.add(rs.getString(1));
           }
       } catch (SQLException e) {
           System.err.println("Error al listar tablas: " + e.getMessage());
       }
       return tablas;
   }
   ```

4. **`obtenerEstructuraTabla(Conexion conexion, String nombreTabla)`**
   - Obtiene la estructura de una tabla específica
   - Retorna `List<ColumnaInfo>` con detalles de cada columna
   - Ejecuta `DESCRIBE nombreTabla`

**Tipo de Consultas Soportadas:**
- **DQL (Data Query Language)**: SELECT, SHOW, DESCRIBE
- **DML (Data Manipulation Language)**: INSERT, UPDATE, DELETE
- **DDL (Data Definition Language)**: CREATE, ALTER, DROP

**Ejemplo de Uso:**
```java
Conexion conn = // ... conexión configurada
String sql = "SELECT * FROM usuarios WHERE activo = 1";

ResultadoSql resultado = SqlEditor.ejecutarConsulta(conn, sql);

if (resultado.esExitoso) {
    DefaultTableModel model = resultado.tableModel;
    int filas = resultado.filas;
    // Mostrar en JTable
} else {
    System.err.println(resultado.mensaje);
}
```

---

#### 4. **Session** (`util/Session.java`)

Gestión de sesión global de la aplicación.

```java
public class Session {
    public static Usuario usuarioActual;      // Usuario logueado
    public static Conexion conexionActiva;    // Conexión seleccionada actualmente
}
```

**Uso:**
- Almacena el usuario autenticado tras login exitoso
- Mantiene la conexión activa seleccionada en el dashboard
- Usado por DAO para filtrar conexiones por usuario

```java
// Después del login
Session.usuarioActual = new Usuario(id, username);

// En DAO
ps.setInt(1, Session.usuarioActual.getId());
```

---

#### 5. **ResultadoSql** (`util/ResultadoSql.java`)

Encapsula el resultado de una ejecución SQL.

```java
public class ResultadoSql {
    public boolean esExitoso;               // ¿Consulta exitosa?
    public DefaultTableModel tableModel;    // Datos de SELECT
    public int filas;                       // Filas afectadas/devueltas
    public String mensaje;                  // Mensaje de error/éxito
}
```

---

#### 6. **ColumnaInfo** (`util/ColumnaInfo.java`)

Información detallada de una columna de tabla.

```java
public class ColumnaInfo {
    public String nombre;       // Nombre de la columna
    public String tipo;         // Tipo de dato (VARCHAR, INT, etc.)
    public String nulo;         // YES/NO (permite nulos)
    public String clave;        // PRI, UNI, MUL (tipo de clave)
    public String defecto;      // Valor por defecto
    
    @Override
    public String toString() {
        return nombre + " (" + tipo + ")";
    }
}
```

---

#### 1. **Main** (`util/Main.java`)

Punto de entrada de la aplicación.

```java
public class Main {
    public static void main(String[] args) {
        new LoginFrame(); // Inicia la ventana de login
    }
}
```

---

#### 2. **LoginFrame** (`ui/LoginFrame.java`)

Ventana de autenticación de usuarios.

**Características:**
- Campos de usuario y contraseña
- Validación contra tabla `usuarios`
- Botón para crear cuenta (navega a RegisterFrame)
- Manejo de sesión tras login exitoso

**Flujo de Login:**
```
Usuario ingresa credenciales
    ↓
Click en "Entrar"
    ↓
Query: SELECT * FROM usuarios WHERE username=? AND password_hash=?
    ↓
Si existe → Session.usuarioActual = usuario → DashboardFrame
Si no existe → Mensaje "Credenciales incorrectas"
```

**Código Clave:**
```java
loginBtn.addActionListener(e -> {
    try {
        Connection conn = getConnection();
        String sql = "SELECT * FROM usuarios WHERE username=? AND password_hash=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, userField.getText());
        ps.setString(2, new String(passField.getPassword()));
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            Session.usuarioActual = new Usuario(rs.getInt("id"), rs.getString("username"));
            new DashboardFrame(userField.getText());
            dispose();
        } else {
            message.setText("Credenciales incorrectas");
        }
    } catch (Exception ex) {
        message.setText("Error conexión");
    }
});
```

---

#### 3. **RegisterFrame** (`ui/RegisterFrame.java`)

Ventana de registro de nuevos usuarios.

**Características:**
- Campos: usuario, contraseña, confirmar contraseña
- Validación de campos vacíos
- Validación de coincidencia de contraseñas
- Inserción en tabla `usuarios`
- Botón "Volver al Login"

**Validaciones:**
```java
// Campos vacíos
if (username.isEmpty() || password.isEmpty()) {
    message.setText("Campos vacíos");
    return;
}

// Contraseñas coinciden
if (!password.equals(confirm)) {
    message.setText("Las contraseñas no coinciden");
    return;
}
```

**Código de Registro:**
```java
String sql = "INSERT INTO usuarios (username, password_hash) VALUES (?, ?)";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setString(1, username);
ps.setString(2, password); // NOTA: En producción debería hashearse
ps.executeUpdate();
```

---

#### 4. **DashboardFrame** (`ui/DashboardFrame.java`)

Panel principal de la aplicación. Utiliza un **CardLayout** para alternar entre diferentes vistas.

**Estructura:**
```
┌────────────────────────────────────────────────┐
│  Dashboard - username                          │
├──────────┬─────────────────────────────────────┤
│  [icon]  │                                     │
│  Conn    │                                     │
│          │      PANEL PRINCIPAL                │
│  [icon]  │      (CardLayout)                   │
│  SQL     │                                     │
│          │                                     │
│  [icon]  │                                     │
│  Logout  │                                     │
└──────────┴─────────────────────────────────────┘
```

**Paneles Disponibles:**
1. **CONNECTIONS**: Gestor de conexiones
2. **SQL**: Editor SQL

**Sidebar con Iconos:**
```java
JButton connectionsBtn = createIconButton("src\\main\\java\\icons\\gestion-de-bases-de-datos.png", 
                                         "Gestor de Conexiones");
JButton sqlBtn = createIconButton("src\\main\\java\\icons\\servidor-sql.png", 
                                  "Editor SQL");
JButton logoutBtn = createIconButton("src\\main\\java\\icons\\cerrar-sesion.png", 
                                     "Cerrar sesión");
```

**Protección de Bases de Datos del Sistema:**
```java
private static final List<String> SYSTEM_DATABASES = Arrays.asList(
    "information_schema",
    "mysql",
    "performance_schema",
    "sys"
);

private boolean esBaseDatosSistema(String nombreBD) {
    return SYSTEM_DATABASES.contains(nombreBD.toLowerCase());
}
```

---

#### 5. **ConnectionDialog** (`ui/ConnectionDialog.java`)

Diálogo modal para crear/editar conexiones de bases de datos.

**Campos del Formulario:**
- **Nombre**: Identificador descriptivo de la conexión
- **Tipo**: ComboBox con "MariaDB" (extensible)
- **Host**: Servidor (localhost, IP, dominio)
- **Puerto**: Puerto MariaDB (default: 3306)
- **Base de Datos**: Nombre de la BD (opcional para conexión a servidor)
- **Usuario**: Usuario de MariaDB
- **Contraseña**: Password (campo oculto)

**Botones:**
- **Probar Conexión**: Valida conectividad antes de guardar
- **Guardar**: Almacena la conexión en BD
- **Cancelar**: Cierra sin guardar

**Validación de Conexión:**
```java
testBtn.addActionListener(e -> {
    Conexion temp = crearConexionDesdeFormulario();
    boolean exitoso = DBManager.probarConexion(temp);
    
    if (exitoso) {
        JOptionPane.showMessageDialog(this, "Conexión exitosa", 
                                     "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Error de conexión", 
                                     "Error", JOptionPane.ERROR_MESSAGE);
    }
});
```

**Guardar Conexión:**
```java
saveBtn.addActionListener(e -> {
    Conexion conexion = crearConexionDesdeFormulario();
    ConexionDAO dao = new ConexionDAO();
    
    boolean guardado;
    if (conexionEditada == null) {
        // Nueva conexión
        guardado = dao.guardarConexion(conexion);
    } else {
        // Actualizar existente
        conexion.setId(conexionEditada.getId());
        guardado = dao.actualizarConexion(conexion);
    }
    
    if (guardado) {
        this.guardado = true;
        dispose();
    }
});
```

---

#### 6. **CreateDatabaseDialog** (`ui/CreateDatabaseDialog.java`)

Diálogo para crear nuevas bases de datos en un servidor.

**Características:**
- Muestra información del servidor (host, puerto, tipo, usuario)
- Campo de texto para nombre de nueva BD
- Validación de nombre (solo alfanuméricos y guiones bajos)
- Ejecución asíncrona con indicador de carga

**Validación de Nombre:**
```java
if (!nombreDB.matches("[a-zA-Z0-9_]+")) {
    JOptionPane.showMessageDialog(this, 
        "El nombre solo puede contener letras, números y guiones bajos",
        "Error", JOptionPane.ERROR_MESSAGE);
    return;
}
```

**Creación Asíncrona:**
```java
Thread createThread = new Thread(() -> {
    try {
        SwingUtilities.invokeLater(() -> 
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        );
        
        boolean exito = DBManager.crearBaseDatos(conexion, nombreDB);
        
        SwingUtilities.invokeLater(() -> {
            setCursor(Cursor.getDefaultCursor());
            
            if (exito) {
                creado = true;
                JOptionPane.showMessageDialog(CreateDatabaseDialog.this,
                    "Base de datos '" + nombreDB + "' creada exitosamente",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(CreateDatabaseDialog.this,
                    "Error al crear la base de datos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    } catch (Exception ex) {
        ex.printStackTrace();
    }
});
createThread.start();
```

---

## 📖 Guía de Uso

### 1. Primer Inicio - Crear una Cuenta

1. Ejecutar la aplicación
2. En la ventana de Login, clic en **"Crear cuenta"**
3. Ingresar:
   - Nombre de usuario (único)
   - Contraseña
   - Confirmar contraseña
4. Clic en **"Registrarse"**
5. Mensaje de confirmación: "Cuenta creada correctamente"
6. Clic en **"Volver al Login"**

### 2. Iniciar Sesión

1. Ingresar usuario y contraseña
2. Clic en **"Entrar"**
3. Si las credenciales son correctas → Dashboard

### 3. Crear una Conexión a Base de Datos

1. En el Dashboard, la vista por defecto es **"Gestor de Conexiones"**
2. Clic en botón **"Nueva Conexión"** (o similar en la interfaz)
3. En el diálogo:
   - **Nombre**: `Mi Servidor Local`
   - **Tipo**: `MariaDB`
   - **Host**: `localhost` (o IP del servidor)
   - **Puerto**: `3306`
   - **Base de Datos**: (opcional - dejar vacío para conexión general)
   - **Usuario**: `root` (o tu usuario)
   - **Contraseña**: `tu_password`
4. Clic en **"Probar Conexión"** para verificar
5. Si es exitoso, clic en **"Guardar"**

### 4. Crear una Nueva Base de Datos

1. Seleccionar una conexión guardada
2. Clic en botón/opción **"Crear Base de Datos"**
3. Ingresar nombre (ej: `mi_proyecto_db`)
4. Clic en **"Crear"**
5. Esperar confirmación

### 5. Ejecutar Consultas SQL

1. En el Dashboard, clic en icono **"Editor SQL"** (sidebar)
2. Seleccionar una conexión activa
3. Escribir consulta SQL, por ejemplo:
   ```sql
   SELECT * FROM usuarios;
   ```
4. Clic en **"Ejecutar"**
5. Resultados se muestran en tabla

**Ejemplos de Consultas:**

```sql
-- Crear tabla
CREATE TABLE clientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos
INSERT INTO clientes (nombre, email) VALUES 
    ('Juan Pérez', 'juan@example.com'),
    ('María García', 'maria@example.com');

-- Consultar
SELECT * FROM clientes WHERE nombre LIKE '%Juan%';

-- Actualizar
UPDATE clientes SET email = 'nuevo@example.com' WHERE id = 1;

-- Eliminar
DELETE FROM clientes WHERE id = 2;

-- Ver estructura
DESCRIBE clientes;

-- Listar tablas
SHOW TABLES;

-- Ver bases de datos
SHOW DATABASES;
```

### 6. Explorar Estructura de Tablas

1. En el Editor SQL o panel de conexiones
2. Seleccionar una base de datos
3. Ver listado de tablas disponibles
4. Seleccionar una tabla para ver su estructura (columnas, tipos, claves)

### 7. Cerrar Sesión

1. Clic en icono **"Cerrar sesión"** en sidebar
2. Regresa a la ventana de Login

---

##  Base de Datos

### Esquema de la Base de Datos del Sistema (`tool_manager`)

```sql
-- Base de datos principal del sistema
create database tool_manager;
use tool_manager;

-- Tabla de usuarios registrados
create table usuarios (
	id int auto_increment primary key,
	username varchar(50) unique not null,
	password_hash varchar(100) not null

);

-- Tabla de conexiones del usuario activo
create table conexiones(
	id int auto_increment primary key,
	usuario_id int,
	nombre varchar(50),
	tipo varchar(20),
	host varchar(50), 
	puerto int,
	db_name varchar(50),
	db_user varchar(50),
	db_password varchar(50),
	foreign key (usuario_id) references usuarios(id)
);
```

### Relaciones

```
usuarios (1) ----< (N) conexiones
```

Un usuario puede tener múltiples conexiones, pero cada conexión pertenece a un único usuario.

### Políticas de Eliminación

- **CASCADE**: Si se elimina un usuario, se eliminan automáticamente todas sus conexiones asociadas.

---

##  Seguridad

### Consideraciones Actuales

⚠️ **Advertencias de Seguridad:**

1. **Contraseñas en Texto Plano**
   - Actualmente las contraseñas de usuarios se almacenan sin hashear
   - **Recomendación**: Implementar BCrypt, Argon2 o PBKDF2
   
   ```java
   // Ejemplo con BCrypt
   String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
   boolean matches = BCrypt.checkpw(plainPassword, hashedPassword);
   ```

2. **Credenciales de Conexiones**
   - Las contraseñas de bases de datos se almacenan en texto plano en la tabla `conexiones`
   - **Recomendación**: Cifrado AES con clave maestra

3. **SQL Injection**
   - **Protegido**: Se usan `PreparedStatement` en todas las consultas del sistema
   - **Riesgo**: El editor SQL permite consultas directas (por diseño)

4. **Conexión sin SSL**
   - Por defecto `useSSL=false` en las conexiones
   - **Recomendación**: Habilitar SSL en producción

### Buenas Prácticas Implementadas

 **Ventajas de Seguridad:**

1. **Prepared Statements**: Todas las consultas de autenticación y gestión usan `PreparedStatement`
2. **Validación de Pertenencia**: Las consultas DAO validan que las conexiones pertenezcan al usuario actual
3. **Gestión de Sesión**: Uso de `Session` para mantener contexto de usuario
4. **Validación de Input**: Validación de nombres de bases de datos (alfanuméricos + guiones bajos)

### Recomendaciones para Producción

```java
// 1. Hashear contraseñas
import org.mindrot.jbcrypt.BCrypt;
String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));

// 2. Habilitar SSL
String jdbcUrl = "jdbc:mariadb://host:port/db?useSSL=true&trustServerCertificate=false";

// 3. Variables de entorno para credenciales
String dbPassword = System.getenv("DB_PASSWORD");

// 4. Logging de accesos
Logger logger = Logger.getLogger(LoginFrame.class.getName());
logger.info("Usuario " + username + " inició sesión");
```

---

## Troubleshooting

### Problemas Comunes y Soluciones

#### 1. **Error: "Driver no encontrado"**

**Causa:** El driver JDBC de MariaDB no está en el classpath.

**Solución:**
```bash
mvn clean install
mvn dependency:resolve
```

O verificar en `pom.xml`:
```xml
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>3.4.1</version>
</dependency>
```

---

#### 2. **Error: "Access denied for user"**

**Causa:** Credenciales incorrectas en `DBConnection.java`.

**Solución:**
- Verificar usuario y contraseña de MariaDB
- Ejecutar en MySQL/MariaDB:
```sql
CREATE USER 'tu_usuario'@'localhost' IDENTIFIED BY 'tu_password';
GRANT ALL PRIVILEGES ON *.* TO 'tu_usuario'@'localhost';
FLUSH PRIVILEGES;
```

---

#### 3. **Error: "Unknown database 'tool_manager'"**

**Causa:** La base de datos del sistema no existe.

**Solución:**
```sql
CREATE DATABASE tool_manager;
-- Luego ejecutar los scripts de creación de tablas
```

---

#### 4. **Error de conexión GSSAPI**

**Causa:** Conflicto con plugins de autenticación GSSAPI.

**Solución:**
Ya está implementado en el código:
```java
props.setProperty("disabledAuthenticationPlugins", "gssapi,auth_gssapi_client");
```

---

#### 5. **Iconos no se muestran**

**Causa:** Rutas de iconos con barras invertidas (Windows) en código.

**Solución temporal:**
- Verificar que exista la carpeta `src/main/java/icons/`
- Colocar los archivos PNG de iconos

**Solución permanente:**
```java
// Usar rutas relativas al classpath
String iconPath = "/icons/gestion-de-bases-de-datos.png";
InputStream stream = getClass().getResourceAsStream(iconPath);
```

---

#### 6. **Error: "Table 'conexiones' doesn't exist"**

**Causa:** Tablas del sistema no creadas.

**Solución:**
Ejecutar script SQL completo de creación de tablas (ver sección [Base de Datos](#-base-de-datos)).

---

#### 7. **NullPointerException en Session**

**Causa:** `Session.usuarioActual` es null (no hay usuario logueado).

**Solución:**
Verificar que el flujo de login establece correctamente:
```java
Session.usuarioActual = new Usuario(id, username);
```

---

**Última actualización:** Febrero 2026  
**Versión:** 1.0-SNAPSHOT
