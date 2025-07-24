import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InicioAdmin extends JFrame {

    private JPanel contentPane;
    private JPanel sidebar;
    private JPanel panelInicio;

    private JLabel labelGestionEmp;
    private JLabel labelSeguridadYrol;
    private JLabel labelBitacoraAct;
    private JLabel labelReglas;

    private JPanel panelGestionEmp;
    private JPanel panelSeguridadYrol;
    private JPanel panelBitacoraAct;
    private JPanel panelReglas;

    public InicioAdmin() {
        initComponents();
        agregarEventos();
        mostrarPanel("panelGestionEmp");
    }

    private void initComponents() {
        // Configuración de la ventana
        setTitle("Sistema con CardLayout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // ----- Barra lateral (menú) -----
        sidebar = new JPanel();
        sidebar.setBackground(new Color(50, 50, 50));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new GridLayout(4, 1));

        labelGestionEmp = crearMenuLabel("Gestión Empleados");
        labelSeguridadYrol = crearMenuLabel("Seguridad y Rol");
        labelBitacoraAct = crearMenuLabel("Bitácora Actividad");
        labelReglas = crearMenuLabel("Reglas");

        sidebar.add(labelGestionEmp);
        sidebar.add(labelSeguridadYrol);
        sidebar.add(labelBitacoraAct);
        sidebar.add(labelReglas);

        contentPane.add(sidebar, BorderLayout.WEST);

        // ----- Panel central con CardLayout -----
        panelInicio = new JPanel(new CardLayout());

        panelGestionEmp = crearPanelColor(Color.LIGHT_GRAY, "Panel Gestión Empleados");
        panelSeguridadYrol = crearPanelColor(Color.CYAN, "Panel Seguridad y Rol");
        panelBitacoraAct = crearPanelColor(Color.ORANGE, "Panel Bitácora");
        panelReglas = crearPanelColor(Color.PINK, "Panel Reglas");

        panelInicio.add(panelGestionEmp, "panelGestionEmp");
        panelInicio.add(panelSeguridadYrol, "panelSeguridadYrol");
        panelInicio.add(panelBitacoraAct, "panelBitacoraAct");
        panelInicio.add(panelReglas, "panelReglas");

        contentPane.add(panelInicio, BorderLayout.CENTER);
    }

    private JLabel crearMenuLabel(String texto) {
        JLabel label = new JLabel(texto, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    private JPanel crearPanelColor(Color color, String titulo) {
        JPanel panel = new JPanel();
        panel.setBackground(color);
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(titulo, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, BorderLayout.CENTER);

        return panel;
    }

    private void agregarEventos() {
        labelGestionEmp.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("panelGestionEmp");
            }
        });

        labelSeguridadYrol.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("panelSeguridadYrol");
            }
        });

        labelBitacoraAct.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("panelBitacoraAct");
            }
        });

        labelReglas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("panelReglas");
            }
        });
    }

    private void mostrarPanel(String nombrePanel) {
        CardLayout cl = (CardLayout) panelInicio.getLayout();
        cl.show(panelInicio, nombrePanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InicioAdmin().setVisible(true);
        });
    }
}
