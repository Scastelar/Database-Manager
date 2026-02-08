import java.sql.Connection;
import java.sql.PreparedStatement;

public class TestConexion {

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {

            String sql =
                    "INSERT INTO usuarios (username, password_hash) VALUES (?, ?)";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "admin");
            ps.setString(2, "1234"); // luego se encripta

            ps.executeUpdate();
            System.out.println("Usuario creado");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

