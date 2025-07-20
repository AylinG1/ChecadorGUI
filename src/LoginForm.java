import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

public class LoginForm {
    private JPanel panel1;
    private JTextField inicio;
    private JLabel labelUsuario;
    private JTextField campoUsuario;
    private JLabel labelPassword;
    private JPasswordField campoPassword;
    private JButton botonLogin;
    private JLabel labelMensaje;

    public LoginForm() {
        panel1 = new JPanel();
        panel1.setLayout(null); // Layout absoluto

        // Label inicio
        labelUsuario = new JLabel("Inicio de Sesión");
        labelUsuario.setBounds(600, 10, 140, 40);
        panel1.add(labelUsuario);

        // Label Usuario
        labelUsuario = new JLabel("Usuario:");
        labelUsuario.setBounds(500, 250, 80, 25);
        panel1.add(labelUsuario);

        // Campo Usuario
        campoUsuario = new JTextField();
        campoUsuario.setBounds(580, 250, 200, 25);
        panel1.add(campoUsuario);

        // Label Password
        labelPassword = new JLabel("Contraseña:");
        labelPassword.setBounds(500, 290, 80, 25);
        panel1.add(labelPassword);

        // Campo Password
        campoPassword = new JPasswordField();
        campoPassword.setBounds(580, 290, 200, 25);
        panel1.add(campoPassword);

        // Botón Login
        botonLogin = new JButton("Iniciar sesión");
        botonLogin.setBounds(580, 330, 200, 30);
        panel1.add(botonLogin);

        // Label Mensaje (para mostrar errores)
        labelMensaje = new JLabel("");
        labelMensaje.setBounds(500, 370, 300, 25);
        labelMensaje.setForeground(java.awt.Color.RED);
        panel1.add(labelMensaje);

        // Acción del botón
        botonLogin.addActionListener(new ActionListener() {
            BaseSQL objeto = new BaseSQL();
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = campoUsuario.getText();
                String password = new String(campoPassword.getPassword());

                if (usuario.isEmpty() || password.isEmpty()) {
                    labelMensaje.setText("Por favor, ingresa usuario y contraseña.");
                } else {
                    // Aquí pondrías la lógica real de autenticación
                    if (usuario.equals("admin") && password.equals("1234")) {
                        labelMensaje.setForeground(java.awt.Color.GREEN);
                        labelMensaje.setText("¡Login exitoso!");
                    } else {
                        labelMensaje.setForeground(java.awt.Color.RED);
                        labelMensaje.setText("Usuario o contraseña incorrectos.");
                    }
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel1;
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        LoginForm loginForm = new LoginForm();

        frame.setContentPane(loginForm.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 800);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);



    }
}
