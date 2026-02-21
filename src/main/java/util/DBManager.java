
package util;

import Modelos.Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class DBManager {
    
    
    public static Connection conectar(Conexion conexion) throws SQLException {
        try {
            switch(conexion.getTipo().toLowerCase()){
                case "mariadb":
                    Class.forName("org.mariadb.jdbc.Driver");
                    break;
                case "mysql":
                    Class.forName("com.mysql.cj.jdbc.Driver");
                   break;
                case "postgresql":
                    Class.forName("org.postgresql.Driver");
                    break;
            }
            
            return DriverManager.getConnection(conexion.getJDBC(), conexion.getUsuario(), conexion.getPassword());
        } catch (ClassNotFoundException e){
            throw new SQLException("Driver no encontrado: " + e.getMessage());
        }
    }
 
    
    public static boolean probarConexion(Conexion conexion){
        try (Connection conn = conectar(conexion)){
            return conn != null && !conn.isClosed();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    
    
 public static boolean crearBaseDatos(Conexion conexion, String nombreDB){
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
         st.executeUpdate(sql);
         return true;
     }catch (SQLException e){
         e.printStackTrace();
         return false;
     }
 }
     
     //Listar las BDs
     public static java.util.List<String> listarBaseDatos(Conexion conexion){
         java.util.List<String> databases = new java.util.ArrayList<>();
         
         try(Connection conn = conectar(conexion);
                 Statement st = conn.createStatement()){
             String sql;
             if (conexion.getTipo().equalsIgnoreCase("postgresql")){
                 sql = "select datname from pg_database where datistemplate = false";
             } else {
                 sql = "show databases";
             }
             
        java.sql.ResultSet rs = st.executeQuery(sql);
        while(rs.next()){
            databases.add(rs.getString(1));
        }
             
     }catch (SQLException e){
         e.printStackTrace();
     }
         return databases;
 }   
    
    
    
    
    
    
    
    
    
    
    
}
