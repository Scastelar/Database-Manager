package Modelos;

public class Conexion {
    private int id;
    private String nombre;
    private String tipo;
    private String host;
    private int puerto;
    private String database;
    private String usuario;
    private String password;

    public Conexion(int id, String nombre,String tipo, String host, int puerto,
                    String database, String usuario, String password) {
        this.id = id;
        this.nombre = nombre;
        this.host = host;
        this.puerto = puerto;
        this.database = database;
        this.usuario = usuario;
        this.password = password;
        this.tipo = tipo;
    }
    
      public Conexion(String nombre,String tipo, String host, int puerto,
                    String database, String usuario, String password) {
        this.id = 0;
        this.nombre = nombre;
        this.host = host;
        this.puerto = puerto;
        this.database = database;
        this.usuario = usuario;
        this.password = password;
        this.tipo = tipo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
    

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public String getHost() { return host; }
    public int getPuerto() { return puerto; }
    public String getDatabase() { return database; }
    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }

    @Override
    public String toString() {
        return nombre + " (" + database + ")";
    }
    
    //obtener la URL JDBC
   public String getJDBC(){
    String dbPart = (database != null && !database.isEmpty()) ? "/" + database : "";
    
    return "jdbc:mariadb://" + host + ":" + puerto + dbPart + 
            "?useSSL=false" + 
            "&allowPublicKeyRetrieval=true" +
            "&useLocalSessionState=true" + 
            "&rewriteBatchedStatements=true";
}
}
