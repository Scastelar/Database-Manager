import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("DB Tool Manager");

        Scene loginScene = createLoginScene(stage);
        stage.setScene(loginScene);
        stage.show();
    }

    // ESCENA DEL LOGIN
    private Scene createLoginScene(Stage stage) {

        Label title = new Label("Iniciar Sesion");

        TextField userField = new TextField();
        userField.setPromptText("Usuario");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");

        Button loginBtn = new Button("Entrar");
        Button goRegisterBtn = new Button("Crear cuenta");

        Label message = new Label();

        loginBtn.setOnAction(e -> {
            try {
                Connection conn = DBConnection.getConnection();

                String sql = "SELECT * FROM usuarios WHERE username=? AND password_hash=?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, userField.getText());
                ps.setString(2, passField.getText());

                var rs = ps.executeQuery();

                if (rs.next()) {
                    message.setText("Login exitoso!");
                } else {
                    message.setText("Credenciales incorrectas");
                }

                conn.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                message.setText("Error de conexion");
            }
        });

        goRegisterBtn.setOnAction(e ->
                stage.setScene(createRegisterScene(stage))
        );

        VBox root = new VBox(10,
                title, userField, passField,
                loginBtn, goRegisterBtn, message
        );
        root.setPadding(new Insets(20));

        return new Scene(root, 300, 300);
    }

    // ESCENA DEL REGISTRO
    private Scene createRegisterScene(Stage stage) {

        Label title = new Label("Crear Cuenta");

        TextField userField = new TextField();
        userField.setPromptText("Nuevo usuario");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Contraseña");

        Button registerBtn = new Button("Registrar");
        Button backLoginBtn = new Button("Volver al login");

        Label message = new Label();

        registerBtn.setOnAction(e -> {
            try {
                Connection conn = DBConnection.getConnection();

                String sql =
                        "INSERT INTO usuarios (username, password_hash) VALUES (?, ?)";

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, userField.getText());
                ps.setString(2, passField.getText());

                ps.executeUpdate();
                conn.close();

                message.setText("Cuenta creada exitosamente");

            } catch (Exception ex) {
                message.setText("Usuario ya existe :(");
            }
        });

        backLoginBtn.setOnAction(e ->
                stage.setScene(createLoginScene(stage))
        );

        VBox root = new VBox(10,
                title, userField, passField,
                registerBtn, backLoginBtn, message
        );
        root.setPadding(new Insets(20));

        return new Scene(root, 300, 300);
    }

    public static void main(String[] args) {
        launch();
    }
}
