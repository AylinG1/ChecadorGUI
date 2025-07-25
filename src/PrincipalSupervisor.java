import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author JuanMa
 */
public class PrincipalSupervisor extends JFrame {
    public PrincipalSupervisor() {
        initComponents();
        agregarEventos();
        mostrarPanel("card1"); // Mostramos panel por defecto
    }

    private void agregarEventos() {
        label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        label3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card1"); // Reportes
            }
        });

        label4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card2"); // Registros
            }
        });

        label5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card3"); // Alertas
            }
        });
    }

    private void mostrarPanel(String nombreCard) {
        CardLayout cl = (CardLayout) panel1.getLayout();
        cl.show(panel1, nombreCard);
    }

    private void initComponents() {
        panel3 = new JPanel();
        panel4 = new JPanel();
        panelMenu = new JPanel();
        label3 = new JLabel();
        separator1 = new JSeparator();
        label2 = new JLabel();
        separator2 = new JSeparator();
        label4 = new JLabel();
        label5 = new JLabel();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        label6 = new JLabel();
        panel1 = new JPanel();
        panelReportes = new JPanel();
        panelRegistros = new JPanel();
        panelAlertas = new JPanel();

        setBackground(new Color(0x2385c7));
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel3 ========
        panel3.setLayout(new BorderLayout());

        //======== panel4 ========
        panel4.setMaximumSize(new Dimension(1002773, 1002773));
        panel4.setMinimumSize(new Dimension(1000, 1000));
        panel4.setLayout(null);
        panel3.add(panel4, BorderLayout.NORTH);

        //======== panelMenu ========
        panelMenu.setBackground(new Color(0xf2876b));
        panelMenu.setLayout(null);

        label3.setText("Reportes");
        label3.setForeground(Color.white);
        panelMenu.add(label3);
        label3.setBounds(15, 125, 125, label3.getPreferredSize().height);

        separator1.setBackground(Color.white);
        separator1.setForeground(Color.white);
        panelMenu.add(separator1);
        separator1.setBounds(0, 115, 145, 3);

        label2.setText("SUPERVISOR");
        label2.setForeground(Color.white);
        label2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panelMenu.add(label2);
        label2.setBounds(25, 90, 105, 16);

        separator2.setForeground(Color.white);
        panelMenu.add(separator2);
        separator2.setBounds(0, 151, 145, 3);

        label4.setText("Registros");
        label4.setForeground(Color.white);
        panelMenu.add(label4);
        label4.setBounds(15, 165, 115, 16);

        label5.setText("Alertas");
        label5.setForeground(Color.white);
        panelMenu.add(label5);
        label5.setBounds(15, 200, 135, 16);

        separator4.setForeground(Color.white);
        panelMenu.add(separator4);
        separator4.setBounds(0, 190, 145, 3);

        separator5.setForeground(Color.white);
        panelMenu.add(separator5);
        separator5.setBounds(0, 225, 145, 3);

        label6.setIcon(new ImageIcon(getClass().getResource("/Captura de pantalla 2025-07-24 135647.png"))); // cambia por tu ruta
        panelMenu.add(label6);
        label6.setBounds(new Rectangle(new Point(20, 0), label6.getPreferredSize()));

        panel3.add(panelMenu, BorderLayout.WEST);

        //======== panel1 (CardLayout) ========
        panel1.setLayout(new CardLayout());

        //======== panelReportes ========
        panelReportes.setBackground(new Color(0xf8f0de));
        panelReportes.setLayout(null);
        panel1.add(panelReportes, "card1");

        //======== panelRegistros ========
        panelRegistros.setBackground(new Color(0xf8f0de));
        panelRegistros.setLayout(null);
        panel1.add(panelRegistros, "card2");

        //======== panelAlertas ========
        panelAlertas.setBackground(new Color(0xf8f0de));
        panelAlertas.setLayout(null);
        panel1.add(panelAlertas, "card3");

        panel3.add(panel1, BorderLayout.CENTER);
        contentPane.add(panel3);
        panel3.setBounds(0, 0, 620, 450);

        {
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < contentPane.getComponentCount(); i++) {
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
    }

    // Variables
    private JPanel panel3;
    private JPanel panel4;
    private JPanel panelMenu;
    private JLabel label3;
    private JSeparator separator1;
    private JLabel label2;
    private JSeparator separator2;
    private JLabel label4;
    private JLabel label5;
    private JSeparator separator4;
    private JSeparator separator5;
    private JLabel label6;
    private JPanel panel1;
    private JPanel panelReportes;
    private JPanel panelRegistros;
    private JPanel panelAlertas;
}
