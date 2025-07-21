
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import javax.swing.SwingUtilities;
import javax.swing.JFrame; // Importación ya presente, pero la incluyo para claridad

/**
 * Clase principal del formulario de Login.
 */
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

        /**
         * Acción que se ejecuta cuando se pulsa el botón de login
         */
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

                    // Verifica si el usuario es LDAP (externo)
                    if (esLDAP) {
                        labelMensaje.setForeground(Color.ORANGE);
                        labelMensaje.setText("Validación LDAP requerida (externa).");
                        registrarLog(conn, usuarioId, "Intento de login (LDAP)");
                        // --- DEBUG: Usuario LDAP ---
                        System.out.println("DEBUG: Usuario '" + usuario + "' es LDAP.");
                        // ---------------------------
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
                        mostrarRoles(conn, usuarioId); // Llama a este método para mostrar roles y abrir la nueva ventana
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
     * Genera un hash SHA-256 en formato hexadecimal.
     * Se usa para validar la contraseña ingresada contra la almacenada en la base de datos.
     * @param input La cadena a hashear (contraseña + salt).
     * @return El hash SHA-256 en formato hexadecimal.
     * @throws NoSuchAlgorithmException Si el algoritmo SHA-256 no está disponible.
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

    /**
     * Registra en la base de datos el log de la actividad del usuario.
     * Ejemplos: "Login exitoso", "Login fallido", etc.
     * @param conn La conexión a la base de datos.
     * @param usuarioId El ID del usuario.
     * @param accion La descripción de la acción.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private void registrarLog(Connection conn, int usuarioId, String accion) throws SQLException {
        String sql = "INSERT INTO LogsActividad (UsuarioID, Accion) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, usuarioId);
        ps.setString(2, accion);
        ps.executeUpdate();
    }

    /**
     * Consulta y muestra los roles del usuario logueado en un mensaje emergente.
     * Y abre la ventana de administración si el login es exitoso.
     * @param conn La conexión a la base de datos.
     * @param usuarioId El ID del usuario.
     * @throws SQLException Si ocurre un error de SQL.
     */
    private void mostrarRoles(Connection conn, int usuarioId) throws SQLException {
        String sql = "SELECT r.NombreRol FROM UsuariosRoles ur JOIN Roles r ON ur.RolID = r.RolID WHERE ur.UsuarioID = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, usuarioId);
        ResultSet rs = ps.executeQuery();

        StringBuilder roles = new StringBuilder("Roles: ");
        boolean isAdmin = false; // <-- CORREGIDO: Declaración de isAdmin fuera del bucle
        while (rs.next()) {
            String rol = rs.getString("NombreRol");
            roles.append(rol).append(" ");
            if ("Admin".equalsIgnoreCase(rol)) { // Compara si el rol es "Admin" (ignora mayúsculas/minúsculas)
                isAdmin = true;
            }
        }

        JOptionPane.showMessageDialog(null, roles.toString().trim(), "Roles del Usuario", JOptionPane.INFORMATION_MESSAGE);

        // Lógica para abrir la ventana PrincipalAdmin
        if (isAdmin) { // Solo si el usuario tiene el rol de Admin
            // Asegúrate de que PrincipalAdmin esté en el mismo paquete o importado correctamente
            // Si PrincipalAdmin también está en el paquete LoginForm, no necesitas importarlo explícitamente aquí
            // Si está en otro paquete, por ejemplo 'com.tuempresa.admin', necesitarías 'import com.tuempresa.admin.PrincipalAdmin;'

            // Ejecutar la creación y visualización de la nueva ventana en el Event Dispatch Thread (EDT)
            SwingUtilities.invokeLater(() -> {
                PrincipalAdmin adminPanel = new PrincipalAdmin(); // Crea una instancia de tu clase PrincipalAdmin

                JFrame adminFrame = new JFrame("Panel de Administración"); // Crea un nuevo JFrame para la ventana
                adminFrame.setContentPane(adminPanel.getPanel()); // Asigna el JPanel de PrincipalAdmin al JFrame
                adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Comportamiento al cerrar la nueva ventana
                adminFrame.setSize(1000, 700); // Tamaño de la nueva ventana
                adminFrame.setLocationRelativeTo(null); // Centra la nueva ventana en la pantalla
                adminFrame.setVisible(true); // Hace visible la nueva ventana

                // Cierra la ventana de login actual
                // Esto busca el JFrame que contiene el panel1 (el panel de login) y lo cierra
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(panel1);
                if (currentFrame != null) {
                    currentFrame.dispose();
                }
            });
        } else { // <-- AÑADIDO: Bloque else para usuarios que no son Admin
            // Si el usuario no es admin, puedes mostrar otro mensaje o abrir otra ventana
            JOptionPane.showMessageDialog(null, "Acceso no autorizado para esta función de administración.", "Acceso Denegado", JOptionPane.WARNING_MESSAGE);
            // Opcional: Si hay otra ventana para usuarios no admin, abrirla aquí
            // Por ejemplo: new VentanaEmpleado().setVisible(true);
            // Y cerrar la ventana de login: ((JFrame) SwingUtilities.getWindowAncestor(panel1)).dispose();
        }
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
            JFrame frame = new JFrame("Login Empresa"); // Título más descriptivo
            LoginForm loginForm = new LoginForm();

            frame.setContentPane(loginForm.getPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1300, 800); // Tamaño fijo para la ventana
            frame.setLocationRelativeTo(null); // Centra la ventana en la pantalla
            frame.setVisible(true); // Hace visible la ventana
        });
    }
}