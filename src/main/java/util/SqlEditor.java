package util;

import Modelos.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class SqlEditor {

    public static ResultadoSql ejecutarConsulta(Conexion conexion, String sql) {
        ResultadoSql resultado = new ResultadoSql();
        long startTime = System.currentTimeMillis();

        try (Connection conn = DBManager.conectar(conexion)) {
            Statement st = conn.createStatement();

            String sqlTrimmed = sql.trim().toLowerCase();

            if (sqlTrimmed.startsWith("select")
                    || sqlTrimmed.startsWith("show")
                    || sqlTrimmed.startsWith("describe")
                    || sqlTrimmed.startsWith("desc")) {

                //Consulta que devuelve resultados
                ResultSet rs = st.executeQuery(sql);
                resultado.tableModel = construirTablaDesdeResultSet(rs);
                resultado.filas = resultado.tableModel.getRowCount();
                resultado.esExitoso = true;
            } else {
                int filasAfectadas = st.executeUpdate(sql);
                resultado.filas = filasAfectadas;
                resultado.mensaje = "Consulta ejecutada. Filas Afectadas: " + filasAfectadas;
                resultado.esExitoso = true;
            }
        } catch (SQLException e) {
            resultado.esExitoso = false;
            resultado.mensaje = "Error SQL: " + e.getMessage();
            e.printStackTrace();
        }

        return resultado;
    }

    //Construit defaultTableModel desde result set
    private static DefaultTableModel construirTablaDesdeResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        //obtener nombres de las columnas
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }

        //obtener datos
        List<Object[]> data = new ArrayList<>();
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            data.add(row);
        }

        //convertir lista a array
        Object[][] dataArray = data.toArray(new Object[0][]);
        return new DefaultTableModel(dataArray, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; //tabla no editable
            }
        };
    }

    //listar todas las tablas de la bd actual
    public static List<String> listarTablas(Conexion conexion) {
        List<String> tablas = new ArrayList<>();

        try (Connection conn = DBManager.conectar(conexion);
                Statement st = conn.createStatement()) {

            String sql = "show tables";
            System.out.println("Listando tablas con: "+sql);

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                tablas.add(rs.getString(1));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar tablas: " + e.getMessage());
            e.printStackTrace();
        }

        return tablas;
    }

    //obtener estructura de una tabla
    public static List<ColumnaInfo> obtenerEstructuraTabla(Conexion conexion, String nombreTabla) {
        List<ColumnaInfo> columnas = new ArrayList<>();

        try (Connection conn = DBManager.conectar(conexion); Statement stmt = conn.createStatement()) {

            // show columns consulta directamente las system tables de mariadb
            String sql = "SHOW COLUMNS FROM `" + nombreTabla + "`";
            System.out.println("Obteniendo estructura con: " + sql);
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                ColumnaInfo col = new ColumnaInfo();
                col.nombre = rs.getString("Field");
                col.tipo = rs.getString("Type");
                col.nulo = rs.getString("Null");
                col.clave = rs.getString("Key");
                col.defecto = rs.getString("Default");
                columnas.add(col);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener estructura: " + e.getMessage());
        }

        return columnas;
    }
    
     public static DefaultTableModel obtenerStatusTabla(Conexion conexion, String nombreTabla) {
        try (Connection conn = DBManager.conectar(conexion);
             Statement stmt = conn.createStatement()) {
            
            String sql = "SHOW TABLE STATUS LIKE '" + nombreTabla + "'";
            ResultSet rs = stmt.executeQuery(sql);
            
            return construirTablaDesdeResultSet(rs);
            
        } catch (SQLException e) {
            System.err.println("Error al obtener status de tabla: " + e.getMessage());
            return new DefaultTableModel();
        }
    }
     
     //listar indices
       public static DefaultTableModel obtenerIndicesTabla(Conexion conexion, String nombreTabla) {
        try (Connection conn = DBManager.conectar(conexion);
             Statement stmt = conn.createStatement()) {
            
            String sql = "SHOW INDEX FROM `" + nombreTabla + "`";
            ResultSet rs = stmt.executeQuery(sql);
            
            return construirTablaDesdeResultSet(rs);
            
        } catch (SQLException e) {
            System.err.println("Error al obtener índices: " + e.getMessage());
            return new DefaultTableModel();
        }
    }
       
    public static String obtenerCreateTable(Conexion conexion, String nombreTabla) {
        try (Connection conn = DBManager.conectar(conexion);
             Statement stmt = conn.createStatement()) {
            
            String sql = "SHOW CREATE TABLE `" + nombreTabla + "`";
            ResultSet rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getString(2);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al obtener CREATE TABLE: " + e.getMessage());
        }
        
        return "";
    }
}
