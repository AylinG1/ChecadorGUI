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
        String usuario = campoUsuario.getText().trim();
        String password = new String(campoPassword.getPassword());

        if (usuario.isEmpty() || password.isEmpty()) {
            labelMensaje.setText("Por favor, ingresa usuario y contraseña.");
            labelMensaje.setForeground(Color.RED);
            return;
        }

        try {
            // Crear instancia BaseSQL que abre la conexión
            BaseSQL bd = new BaseSQL();

            String sql = "SELECT UsuarioID, HashContraseña, Salt, EsLDAP, Activo FROM Usuarios WHERE Usuario = ?";
            PreparedStatement ps = bd.conn.prepareStatement(sql); // accedes a la conexión directamente
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                labelMensaje.setText("Usuario no encontrado.");
                labelMensaje.setForeground(Color.RED);
                bd.cerrar();
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
                bd.cerrar();
                return;
            }

            if (esLDAP) {
                labelMensaje.setText("Validación LDAP requerida (externa).");
                labelMensaje.setForeground(Color.ORANGE);
                registrarLog(bd.conn, usuarioId, "Intento de login (LDAP)");
                bd.cerrar();
                return;
            }

            String hashIngresado = generarSHA256(password + salt);

            if (hashIngresado.equalsIgnoreCase(hashBD)) {
                registrarLog(bd.conn, usuarioId, "Login exitoso");

                String sqlRol = "SELECT TOP 1 r.NombreRol FROM UsuariosRoles ur JOIN Roles r ON ur.RolID = r.RolID WHERE ur.UsuarioID = ?";
                PreparedStatement psRol = bd.conn.prepareStatement(sqlRol);
                psRol.setInt(1, usuarioId);
                ResultSet rsRol = psRol.executeQuery();

                if (rsRol.next()) {
                    String rol = rsRol.getString("NombreRol").toLowerCase();

                    labelMensaje.setForeground(Color.GREEN);

                    switch (rol) {
                        case "admin":
                            labelMensaje.setText("¡Login exitoso! Bienvenido administrador.");
                            SwingUtilities.invokeLater(() -> {
                                new InicioAdmin().setVisible(true);
                                this.dispose();
                            });
                            break;

                        case "empleado":
                            labelMensaje.setText("¡Login exitoso! Bienvenido empleado.");
                            SesionUsuario.usuarioActual = campoUsuario.getText().trim(); //guarda el usuario
                            SwingUtilities.invokeLater(() -> {
                                new PrincipaloUsuario().setVisible(true);
                                this.dispose();
                            });
                            break;


                        case "supervisor":
                            labelMensaje.setText("¡Login exitoso! Bienvenido supervisor.");
                            SwingUtilities.invokeLater(() -> {
                                new PrincipalSupervisor().setVisible(true);
                                this.dispose();
                            });
                            break;

                        default:
                            labelMensaje.setText("Rol desconocido. Contacte al administrador.");
                            labelMensaje.setForeground(Color.RED);
                            break;
                    }
                } else {
                    labelMensaje.setText("No se encontró ningún rol asignado al usuario.");
                    labelMensaje.setForeground(Color.RED);
                }
            } else {
                labelMensaje.setText("Contraseña incorrecta.");
                labelMensaje.setForeground(Color.RED);
                registrarLog(bd.conn, usuarioId, "Login fallido - contraseña incorrecta");
            }

            bd.cerrar();

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

    private void button1(ActionEvent e) {
        SesionUsuario.usuarioActual = campoUsuario.getText().trim(); //guarda el usuario
        SwingUtilities.invokeLater(() -> {
            new Retardos().setVisible(true);
            this.dispose();
        });
    }



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Juan
        labelInicio = new JLabel();
        labelUsuario = new JLabel();
        campoUsuario = new JTextField();
        labelPassword = new JLabel();
        campoPassword = new JPasswordField();
        botonLogin = new JButton();
        label2 = new JLabel();
        labelMensaje = new JLabel();
        button1 = new JButton();

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
        botonLogin.setBounds(new Rectangle(new Point(460, 285), botonLogin.getPreferredSize()));

        //---- label2 ----
        label2.setIcon(new ImageIcon(getClass().getResource("/image-removebg-preview.png")));
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(60, 75), label2.getPreferredSize()));

        //---- labelMensaje ----
        labelMensaje.setText("text");
        contentPane.add(labelMensaje);
        labelMensaje.setBounds(380, 250, 410, labelMensaje.getPreferredSize().height);

        //---- button1 ----
        button1.setText("text");
        button1.addActionListener(e -> {
			button1(e);
		});
        contentPane.add(button1);
        button1.setBounds(180, 355, 120, button1.getPreferredSize().height);

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
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Juan
    private JLabel labelInicio;
    private JLabel labelUsuario;
    private JTextField campoUsuario;
    private JLabel labelPassword;
    private JPasswordField campoPassword;
    private JButton botonLogin;
    private JLabel label2;
    private JLabel labelMensaje;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}