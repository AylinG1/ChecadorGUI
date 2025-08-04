import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class UtilidadesBitacora { // O puedes integrarla directamente en tu clase PrincipalSupervisor

    public void registrarAccionEnBitacora(String usuario, String accion) {
        BaseSQL base = null;
        PreparedStatement ps = null;
        try {
            base = new BaseSQL();
            String sql = "INSERT INTO Bitacora_Accesos (usuario, accion, fecha, hora) VALUES (?, ?, ?, ?)";
            ps = base.conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, accion);
            ps.setDate(3, new java.sql.Date(new Date().getTime()));
            ps.setTime(4, new java.sql.Time(new Date().getTime()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al registrar la acción en la bitácora: " + ex.getMessage(), "Error de Bitácora", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (ps != null) ps.close(); } catch (SQLException ex) { /* ignored */ }
            try { if (base != null) base.cerrar(); } catch (SQLException ex) { /* ignored */ }
        }
    }
}