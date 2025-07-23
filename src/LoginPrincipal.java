import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import javax.swing.SwingUtilities;

/*
 * Created by JFormDesigner on Wed Jul 23 11:52:47 GMT-06:00 2025
 */



/**
 * @author aylin
 */
public class LoginPrincipal extends JFrame {
    public LoginPrincipal() {
        initComponents();
    }
    private String generarSHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02X", 0xFF & b));
        }
        return hexString.toString();
    }
    private void registrarLog(Connection conn, int usuarioId, String accion) throws SQLException {
        String sql = "INSERT INTO LogsActividad (UsuarioID, Accion) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, usuarioId);
        ps.setString(2, accion);
        ps.executeUpdate();
    }

    private void mostrarRoles(Connection conn, int usuarioId) throws SQLException {
        String sql = "SELECT r.NombreRol FROM UsuariosRoles ur JOIN Roles r ON ur.RolID = r.RolID WHERE ur.UsuarioID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, usuarioId);
        ResultSet rs = ps.executeQuery();

        StringBuilder roles = new StringBuilder("Roles: ");
        while (rs.next()) {
            roles.append(rs.getString("NombreRol")).append(" ");
        }

        JOptionPane.showMessageDialog(this, roles.toString().trim(), "Roles del Usuario", JOptionPane.INFORMATION_MESSAGE);
    }


    private void botonLogin(ActionEvent e) {
        // TODO add your code here
        String usuario = campoUsuario.getText().trim();
        String password = new String(campoPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            labelMensaje.setText("Por favor, ingresa usuario y contraseña.");
            labelMensaje.setForeground(Color.RED);
            return;
        }

        try (Connection conn = BaseSQL.obtenerConexion()) {
            String sql = "SELECT UsuarioID, HashContraseña, Salt, EsLDAP, Activo FROM Usuarios WHERE Usuario = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                labelMensaje.setText("Usuario no encontrado.");
                labelMensaje.setForeground(Color.RED);
                return;
            }

            int usuarioId = rs.getInt("UsuarioID");
            boolean activo = rs.getBoolean("Activo");
            boolean esLDAP = rs.getBoolean("EsLDAP");
            String salt = rs.getString("Salt").trim();
            String hashBD = rs.getString("HashContraseña").trim();

            if (!activo) {
                labelMensaje.setText("Usuario desactivado.");
                labelMensaje.setForeground(Color.RED);
                return;
            }

            if (esLDAP) {
                labelMensaje.setText("Validación LDAP requerida (externa).");
                labelMensaje.setForeground(Color.ORANGE);
                registrarLog(conn, usuarioId, "Intento de login (LDAP)");
                return;
            }

            String hashIngresado = generarSHA256(password + salt);

            if (hashIngresado.equalsIgnoreCase(hashBD)) {
                registrarLog(conn, usuarioId, "Login exitoso");

                // Verificar si el usuario es admin
                String sqlRol = "SELECT r.NombreRol FROM UsuariosRoles ur JOIN Roles r ON ur.RolID = r.RolID WHERE ur.UsuarioID = ?";
                PreparedStatement psRol = conn.prepareStatement(sqlRol);
                psRol.setInt(1, usuarioId);
                ResultSet rsRol = psRol.executeQuery();

                boolean esAdmin = false;
                boolean esUsuario = false;
                while (rsRol.next()) {
                    String rol = rsRol.getString("NombreRol");
                    if ("admin".equalsIgnoreCase(rol)) {
                        esAdmin = true;
                        break;
                    }
                    else if ("empleado".equalsIgnoreCase(rol)) {
                        esUsuario = true;
                        break;
                    }
                }

                if (esAdmin) {
                    labelMensaje.setText("¡Login exitoso! Bienvenido administrador.");
                    labelMensaje.setForeground(Color.GREEN);

                    // Abrir ventana InicioAdmin
                    SwingUtilities.invokeLater(() -> {
                        InicioAdmin adminWindow = new InicioAdmin();
                        adminWindow.setVisible(true);
                        this.dispose();
                    });
                }
                else if (esUsuario) {
                    labelMensaje.setText("¡Login exitoso! Bienvenido administrador.");
                    labelMensaje.setForeground(Color.GREEN);

                    // Abrir ventana InicioAdmin
                    SwingUtilities.invokeLater(() -> {
                        PrincipaloUsuario usuarioWindow = new PrincipaloUsuario();
                        usuarioWindow.setVisible(true);
                        this.dispose();
                    });
                }
                else {
                    labelMensaje.setText("Acceso denegado: no eres administrador.");
                    labelMensaje.setForeground(Color.RED);
                }
            } else {
                labelMensaje.setText("Contraseña incorrecta.");
                labelMensaje.setForeground(Color.RED);
                registrarLog(conn, usuarioId, "Login fallido - contraseña incorrecta");
            }

        } catch (SQLException ex) {
            labelMensaje.setText("Error SQL: " + ex.getMessage());
            labelMensaje.setForeground(Color.RED);
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            labelMensaje.setText("Error de seguridad.");
            labelMensaje.setForeground(Color.RED);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Estilo visual opcional
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LoginPrincipal login = new LoginPrincipal();
            login.setVisible(true);
        });
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        labelInicio = new JLabel();
        labelUsuario = new JLabel();
        campoUsuario = new JTextField();
        labelPassword = new JLabel();
        campoPassword = new JPasswordField();
        botonLogin = new JButton();
        label2 = new JLabel();
        labelMensaje = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- labelInicio ----
        labelInicio.setText("Inicio de sesi\u00f3n");
        labelInicio.setFont(new Font("Segoe UI Black", Font.ITALIC, 22));
        contentPane.add(labelInicio);
        labelInicio.setBounds(new Rectangle(new Point(385, 100), labelInicio.getPreferredSize()));

        //---- labelUsuario ----
        labelUsuario.setText("Usuario");
        contentPane.add(labelUsuario);
        labelUsuario.setBounds(new Rectangle(new Point(380, 155), labelUsuario.getPreferredSize()));
        contentPane.add(campoUsuario);
        campoUsuario.setBounds(460, 155, 130, campoUsuario.getPreferredSize().height);

        //---- labelPassword ----
        labelPassword.setText("Contrase\u00f1a:");
        contentPane.add(labelPassword);
        labelPassword.setBounds(new Rectangle(new Point(375, 200), labelPassword.getPreferredSize()));
        contentPane.add(campoPassword);
        campoPassword.setBounds(460, 205, 125, campoPassword.getPreferredSize().height);

        //---- botonLogin ----
        botonLogin.setText("Iniciar sesi\u00f3n");
        botonLogin.addActionListener(e -> botonLogin(e));
        contentPane.add(botonLogin);
        botonLogin.setBounds(new Rectangle(new Point(450, 260), botonLogin.getPreferredSize()));

        //---- label2 ----
        label2.setIcon(new ImageIcon(getClass().getResource("/image-removebg-preview.png")));
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(60, 75), label2.getPreferredSize()));

        //---- labelMensaje ----
        labelMensaje.setText("text");
        contentPane.add(labelMensaje);
        labelMensaje.setBounds(new Rectangle(new Point(375, 235), labelMensaje.getPreferredSize()));

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel labelInicio;
    private JLabel labelUsuario;
    private JTextField campoUsuario;
    private JLabel labelPassword;
    private JPasswordField campoPassword;
    private JButton botonLogin;
    private JLabel label2;
    private JLabel labelMensaje;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}