import java.sql.*;

public class BaseSQL {

    private Connection conn;

    // Constructor: crea y guarda una conexión activa
    public ConexionBD() throws SQLException {
        String url = "jdbc:sqlserver://servidor-prueba89.database.windows.net:1433;" +
                "database=BasePrueba;" +
                "user=Administrador@servidor-prueba89;" +
                "password=Contraseña1;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "loginTimeout=30;";
        conn = DriverManager.getConnection(url);
    }

    // Método tipo C#: ejecutar una consulta SELECT y devolver el ResultSet
    public ResultSet ejecutarConsulta(String sql) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeQuery();
    }

    // (Opcional) Ejecutar INSERT, UPDATE o DELETE
    public int ejecutarActualizacion(String sql) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(sql);
        return stmt.executeUpdate();
    }

    // Cerrar conexión
    public void cerrar() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

}
