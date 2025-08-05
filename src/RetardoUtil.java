import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class RetardoUtil {

    public static int obtenerMinutosRetardo(String nombreEmpleado) {
        int totalMinutos = 0;

        // Obtener semana y año actual
        LocalDate hoy = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int semanaActual = hoy.get(weekFields.weekOfWeekBasedYear());
        int anioActual = hoy.getYear();

        String sql = "SELECT " +
                "    CASE " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_1%' AND rc.hora > t.entrada_1 THEN DATEDIFF(MINUTE, t.entrada_1, rc.hora) " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_2%' AND rc.hora > t.entrada_2 THEN DATEDIFF(MINUTE, t.entrada_2, rc.hora) " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_3%' AND rc.hora > t.entrada_3 THEN DATEDIFF(MINUTE, t.entrada_3, rc.hora) " +
                "        ELSE 0 " +
                "    END AS minutos_retraso_bruto " +
                "FROM Registros_Checada rc " +
                "JOIN Empleados e ON rc.id_empleado = e.id " +
                "JOIN Turnos t ON e.id_turno = t.id " +
                "WHERE e.nombre = ? " +
                "  AND ( " +
                "        (rc.tipo_registro LIKE 'ENTRADA_1%' AND t.entrada_1 IS NOT NULL AND rc.hora > t.entrada_1) OR " +
                "        (rc.tipo_registro LIKE 'ENTRADA_2%' AND t.entrada_2 IS NOT NULL AND rc.hora > t.entrada_2) OR " +
                "        (rc.tipo_registro LIKE 'ENTRADA_3%' AND t.entrada_3 IS NOT NULL AND rc.hora > t.entrada_3) " +
                "      ) " +
                "  AND DATEPART(WEEK, rc.fecha) = ? " +
                "  AND DATEPART(YEAR, rc.fecha) = ?";

        try {
            BaseSQL base = new BaseSQL();
            PreparedStatement ps = base.conn.prepareStatement(sql);
            ps.setString(1, nombreEmpleado);
            ps.setInt(2, semanaActual);
            ps.setInt(3, anioActual);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                totalMinutos += rs.getInt("minutos_retraso_bruto");
            }

            rs.close();
            ps.close();
            base.cerrar();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalMinutos;
    }
}
