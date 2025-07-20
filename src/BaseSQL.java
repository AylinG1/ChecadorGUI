import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseSQL {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://servidor-prueba89.database.windows.net:1433;" +
                "database=BasePrueba;" +
                "user=Administrador@servidor-prueba89;" +
                "password=Contraseña1;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "loginTimeout=30;";

        try (Connection conn = DriverManager.getConnection(url)) {
            System.out.println("Conexion exitosa a Azure SQL Server");
        } catch (SQLException e) {
            System.out.println(" Error de conexion: " + e.getMessage());
        }
    }
}
