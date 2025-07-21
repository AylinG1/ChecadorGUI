import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseSQL {

    /**
     * Método estático que devuelve una conexión activa a SQL Server Azure.
     */
    public static Connection obtenerConexion() throws SQLException {
        String url = "jdbc:sqlserver://servidor-prueba89.database.windows.net:1433;" +
                "database=BasePrueba;" +
                "user=Administrador@servidor-prueba89;" +
                "password=Contraseña1;" +
                "encrypt=true;trustServerCertificate=false;loginTimeout=30;";
        return DriverManager.getConnection(url);
    }
}