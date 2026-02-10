public class Conexion {
    private int id;
    private String nombre;
    private String host;
    private int puerto;
    private String database;
    private String usuario;
    private String password;

    public Conexion(int id, String nombre, String host, int puerto,
                    String database, String usuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.host = host;
        this.puerto = puerto;
        this.database = database;
        this.usuario = usuario;
        this.password = password;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getHost() { return host; }
    public int getPuerto() { return puerto; }
    public String getDatabase() { return database; }
    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return nombre + " (" + database + ")";
    }
}
