package util;

import Modelos.Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBManager {

    public static Connection conectar(Conexion conexion) throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String jdbcUrl = conexion.getJDBC();
            System.out.println("Conectando a MariaDB: " + jdbcUrl);
            System.out.println("Usuario: " + conexion.getUsuario());

            Properties props = new Properties();
            props.setProperty("user", conexion.getUsuario());
            props.setProperty("password", conexion.getPassword());
            props.setProperty("disabledAuthenticationPlugins", "gssapi,auth_gssapi_client");

            Connection conn = DriverManager.getConnection(jdbcUrl, props);
            System.out.println("Conexion exitosa a MariaDB!");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Driver no encontrado: " + e.getMessage());
            throw new SQLException("Driver no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error SQL: " + e.getMessage());
            throw e;
        }
    }

    public static boolean probarConexion(Conexion conexion) {
        try (Connection conn = conectar(conexion)) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error al probar conexión: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean crearBaseDatos(Conexion conexion, String nombreDB) {
        Conexion tempConexion = new Conexion(
                conexion.getNombre(),
                conexion.getTipo(),
                conexion.getHost(),
                conexion.getPuerto(),
                "", //Espacio abierto para que se inicialice sin una BD
                conexion.getUsuario(),
                conexion.getPassword()
        );

        try (Connection conn = conectar(tempConexion);
                Statement st = conn.createStatement()){
            String sql = "create database " + nombreDB;
            System.out.println("Ejecutando: " + sql);
            st.executeUpdate(sql);
            return true;
        } catch (SQLException e){
            System.err.println("Error al crear base de datos: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //Listar las BDs
    public static java.util.List<String> listarBaseDatos(Conexion conexion) {
        java.util.List<String> databases = new java.util.ArrayList<>();

          try (Connection conn = conectar(conexion);
             Statement st = conn.createStatement()) {
            
            String sql = "show databases";
            System.out.println("Listando bases de datos con: " + sql);
            
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                databases.add(rs.getString(1));
            }
             
        } catch (SQLException e){
            System.err.println("Error al listar bases de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return databases;
    }   
    
    //obtener informacion de la bd usando las system tables
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
}
