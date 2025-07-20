import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;


public class LoginForm {
    private JPanel panel1;
    private JTextField inicio; // Este campo no parece usarse en el código, podrías revisarlo
    private JLabel labelUsuario;
    private JTextField campoUsuario;
    private JLabel labelPassword;
    private JPasswordField campoPassword;
    private JButton botonLogin;
    private JLabel labelMensaje;

    public LoginForm() {
        panel1 = new JPanel();
        panel1.setLayout(null); // Layout absoluto (posición manual)

        // Etiqueta principal de la ventana
        labelUsuario = new JLabel("Inicio de Sesión");
        labelUsuario.setBounds(600, 10, 140, 40);
        panel1.add(labelUsuario);

        // Etiqueta de usuario
        labelUsuario = new JLabel("Usuario:");
        labelUsuario.setBounds(500, 250, 80, 25);
        panel1.add(labelUsuario);

        // Campo para ingresar el usuario
        campoUsuario = new JTextField();
        campoUsuario.setBounds(580, 250, 200, 25);
        panel1.add(campoUsuario);

        // Etiqueta de contraseña
        labelPassword = new JLabel("Contraseña:");
        labelPassword.setBounds(500, 290, 80, 25);
        panel1.add(labelPassword);

        // Campo para ingresar la contraseña
        campoPassword = new JPasswordField();
        campoPassword.setBounds(580, 290, 200, 25);
        panel1.add(campoPassword);

        // Botón para iniciar sesión
        botonLogin = new JButton("Iniciar sesión");
        botonLogin.setBounds(580, 330, 200, 30);
        panel1.add(botonLogin);

        // Etiqueta para mensajes de estado
        labelMensaje = new JLabel("");
        labelMensaje.setBounds(500, 370, 400, 25);
        labelMensaje.setForeground(Color.RED);
        panel1.add(labelMensaje);



        botonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtiene el texto del campo de usuario y elimina espacios en blanco al inicio/final
                String usuario = campoUsuario.getText().trim();
                String password = new String(campoPassword.getPassword()); // No se aplica trim a la contraseña por seguridad

                // --- DEBUG: Imprime el usuario y la contraseña ingresados ---
                System.out.println("DEBUG: Usuario ingresado (trim): '" + usuario + "'");
                System.out.println("DEBUG: Contraseña ingresada: '" + password + "'");
                // -------------------------------------------------------------

                // Verificación rápida si los campos están vacíos
                if (usuario.isEmpty() || password.isEmpty()) {
                    labelMensaje.setText("Por favor, ingresa usuario y contraseña.");
                    return;
                }

                try (Connection conn = BaseSQL.obtenerConexion()) { // Asegúrate que BaseSQL.getConnection() es el método correcto

                    // Consulta para obtener la información del usuario
                    String sql = "SELECT UsuarioID, HashContraseña, Salt, EsLDAP, Activo FROM Usuarios WHERE Usuario = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, usuario); // El PreparedStatement maneja la limpieza de la cadena para la consulta
                    ResultSet rs = ps.executeQuery();

                    if (!rs.next()) {
                        labelMensaje.setText("Usuario no encontrado.");
                        // --- DEBUG: Usuario no encontrado en la BD ---
                        System.out.println("DEBUG: Usuario '" + usuario + "' NO encontrado en la base de datos.");
                        // ----------------------------------------------
                        return;
                    }

                    int usuarioId = rs.getInt("UsuarioID");
                    boolean activo = rs.getBoolean("Activo");
                    boolean esLDAP = rs.getBoolean("EsLDAP");
                    // Obtiene el salt y el hash de la base de datos, eliminando espacios en blanco
                    String salt = rs.getString("Salt").trim();
                    String hashBD = rs.getString("HashContraseña").trim();

                    // --- DEBUG: Imprime datos obtenidos de la BD ---
                    System.out.println("DEBUG: UsuarioID de BD: " + usuarioId);
                    System.out.println("DEBUG: Activo de BD: " + activo);
                    System.out.println("DEBUG: EsLDAP de BD: " + esLDAP);
                    System.out.println("DEBUG: Salt de BD (trim): '" + salt + "'");
                    System.out.println("DEBUG: Hash de BD (trim): '" + hashBD + "'");
                    // ------------------------------------------------

                    // Verifica si el usuario está activo
                    if (!activo) {
                        labelMensaje.setText("Usuario desactivado.");
                        // --- DEBUG: Usuario desactivado ---
                        System.out.println("DEBUG: Usuario '" + usuario + "' está DESACTIVADO.");
                        // ----------------------------------
                        return;
                    }


                    // Generamos el hash con la contraseña ingresada + salt
                    String hashIngresado = generarSHA256(password + salt);

                    // --- DEBUG: Imprime el hash generado ---
                    System.out.println("DEBUG: Hash generado (password + salt): '" + hashIngresado + "'");
                    // ----------------------------------------

                    // Comparación del hash ingresado con el guardado (ignorando mayúsculas/minúsculas en el hash)
                    if (hashIngresado.equalsIgnoreCase(hashBD)) {
                        labelMensaje.setForeground(Color.GREEN);
                        labelMensaje.setText("¡Login exitoso!");
                        registrarLog(conn, usuarioId, "Login exitoso");
                        mostrarRoles(conn, usuarioId);
                        // --- DEBUG: Login exitoso ---
                        System.out.println("DEBUG: Login exitoso para el usuario '" + usuario + "'.");
                        // ----------------------------
                    } else {
                        labelMensaje.setForeground(Color.RED);
                        labelMensaje.setText("Contraseña incorrecta.");
                        registrarLog(conn, usuarioId, "Login fallido - contraseña incorrecta");
                        // --- DEBUG: Contraseña incorrecta ---
                        System.out.println("DEBUG: Contraseña incorrecta para el usuario '" + usuario + "'.");
                        System.out.println("DEBUG: Hash esperado (BD): '" + hashBD + "'");
                        System.out.println("DEBUG: Hash recibido (generado): '" + hashIngresado + "'");
                        // ------------------------------------
                    }

                } catch (SQLException ex) {
                    labelMensaje.setForeground(Color.RED);
                    labelMensaje.setText("Error SQL: " + ex.getMessage());
                    // Puedes imprimir la traza de la excepción para depuración avanzada
                    ex.printStackTrace();
                    // --- DEBUG: Error SQL ---
                    System.err.println("DEBUG: Error SQL capturado: " + ex.getMessage());
                    // ------------------------
                } catch (NoSuchAlgorithmException ex) {
                    labelMensaje.setForeground(Color.RED);
                    labelMensaje.setText("Error de seguridad: Algoritmo de hash no encontrado.");
                    ex.printStackTrace();
                    // --- DEBUG: Error de algoritmo de hash ---
                    System.err.println("DEBUG: Error de algoritmo de hash: " + ex.getMessage());
                    // -----------------------------------------
                }
            }
        });
    }

    /**
     Genera un hash SHA-256 en formato hexadecimal.
     Se usa para validar la contraseña ingresada contra la almacenada en la base de datos.
     @param input La cadena a hashear (contraseña + salt).
     @return El hash SHA-256 en formato hexadecimal.
     @throws NoSuchAlgorithmException Si el algoritmo SHA-256 no está disponible.
     */
    private String generarSHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02X", 0xFF & b)); // Asegura formato de dos dígitos
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

        JOptionPane.showMessageDialog(null, roles.toString().trim(), "Roles del Usuario", JOptionPane.INFORMATION_MESSAGE);
        // Puedes añadir aquí la lógica para abrir la ventana principal de la aplicación
        // Por ejemplo: new VentanaPrincipal().setVisible(true);
        // Y cerrar la ventana de login: ((JFrame) SwingUtilities.getWindowAncestor(panel1)).dispose();
    }

    /**
     * Método para obtener el panel principal del LoginForm.
     * @return El JPanel que contiene la interfaz de login.
     */
    public JPanel getPanel() {
        return panel1;
    }

    /**
     * Método principal para ejecutar la aplicación de login.
     * Este es el punto de entrada de la aplicación Swing.
     * Se encarga de crear el JFrame y mostrar el LoginForm.
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        // Ejecutar la UI en el Event Dispatch Thread (EDT) para seguridad de hilos en Swing
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login de Farmacia"); // Título más descriptivo
            LoginForm loginForm = new LoginForm();

            frame.setContentPane(loginForm.getPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 800); // Tamaño fijo para la ventana
            frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
            frame.setVisible(true); // Hace visible la ventana
        });
    }
}
