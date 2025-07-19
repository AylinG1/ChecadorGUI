import javax.swing.*;

public class LoginForm {
    private JPanel panel1;
    private JButton continuarButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Login");
        frame.setContentPane(new LoginForm().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrar ventana
        frame.setVisible(true);
    }
}

/*public static void main(String[] args) {
    JFrame frame = new JFrame("Login");
    frame.setContentPane(new LoginForm().panel1);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setLocationRelativeTo(null); // Centrar ventana
    frame.setVisible(true);
}*/
