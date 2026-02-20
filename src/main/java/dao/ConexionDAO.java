package dao;

import Modelos.Conexion;
import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.DBConnection;
import util.Session;

public class ConexionDAO {

    public Conexion obtenerConexionPorID(int id) {
        String sql = "select * from conexiones where id=? AND usuario_id=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, Session.usuarioActual.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Conexion(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getString("host"),
                        rs.getInt("puerto"),
                        rs.getString("db_name"),
                        rs.getString("db_user"),
                        rs.getString("db_password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public boolean guardarConexion(Conexion conexion) {
        String sql = "INSERT INTO conexiones (usuario_id, nombre, tipo"
                + ", host, puerto, database_name, username, password) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Session.usuarioActual.getId());
            ps.setString(2, conexion.getNombre());
            ps.setString(3, conexion.getTipo());
            ps.setString(4, conexion.getHost());
            ps.setInt(5, conexion.getPuerto());
            ps.setString(6, conexion.getDatabase());
            ps.setString(7, conexion.getUsuario());
            ps.setString(8, conexion.getPassword());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public java.util.List<Conexion> obtenerConexionesUsuario() {
        java.util.List<Conexion> conexiones = new ArrayList<>();
        String sql = "select * from conexiones where usuario_id = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, Session.usuarioActual.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Conexion conexion = new Conexion(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("tipo"),
                        rs.getString("host"),
                        rs.getInt("puerto"),
                        rs.getString("db_name"),
                        rs.getString("db_user"),
                        rs.getString("db_password")
                );
                conexiones.add(conexion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conexiones;
    }

    public boolean actualizarConexion(Conexion conexion) {
        String sql = "update conexiones set nombre=?, tipo=?, host=?, puerto=?, "
                + "db_name=?, db_user=?, db_password=? where id=? and usuario=id?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, conexion.getNombre());
            ps.setString(2, conexion.getTipo());
            ps.setString(3, conexion.getHost());
            ps.setInt(4, conexion.getPuerto());
            ps.setString(5, conexion.getDatabase());
            ps.setString(6, conexion.getUsuario());
            ps.setString(7, conexion.getPassword());
            ps.setInt(8, conexion.getId());
            ps.setInt(9, Session.usuarioActual.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarConexion(int id) {
        String sql = "DELETE FROM conexiones WHERE id=? and usuario_id=?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.setInt(2, Session.usuarioActual.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
