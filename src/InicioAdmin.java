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

    private void label1MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void SeguridadYrolesLbl(MouseEvent e) {
        // TODO add your code here
    }

    private void BitacoraActsLBL(MouseEvent e) {
        // TODO add your code here
    }

    private void ReglasLBL(MouseEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panelBase = new JPanel();
        panelMenu = new JPanel();
        label1 = new JLabel();
        separator1 = new JSeparator();
        label2 = new JLabel();
        separator2 = new JSeparator();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        separator3 = new JSeparator();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        panelInicio = new JPanel();
        panelGestionEmp = new JPanel();
        label6 = new JLabel();
        panelSeguridadYrol = new JPanel();
        label7 = new JLabel();
        panelBitácoraAct = new JPanel();
        label8 = new JLabel();
        panelReglas = new JPanel();
        label9 = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panelBase ========
        {
            panelBase.setLayout(new BorderLayout());

            //======== panelMenu ========
            {
                panelMenu.setBackground(new Color(0xf2876b));
                panelMenu.setLayout(new GridBagLayout());
                ((GridBagLayout)panelMenu.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)panelMenu.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)panelMenu.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                ((GridBagLayout)panelMenu.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- label1 ----
                label1.setText("Gesti\u00f3n de empleados");
                label1.setForeground(Color.white);
                label1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label1MouseClicked(e);
                    }
                });
                panelMenu.add(label1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- separator1 ----
                separator1.setBackground(Color.white);
                separator1.setForeground(Color.white);
                panelMenu.add(separator1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label2 ----
                label2.setText("ADMINISTRADOR");
                label2.setForeground(Color.white);
                panelMenu.add(label2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- separator2 ----
                separator2.setForeground(Color.white);
                panelMenu.add(separator2, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label3 ----
                label3.setText("Seguridad y roles");
                label3.setForeground(Color.white);
                label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        SeguridadYrolesLbl(e);
                    }
                });
                panelMenu.add(label3, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label4 ----
                label4.setText("Bit\u00e1cora de actividades");
                label4.setForeground(Color.white);
                label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label4.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        BitacoraActsLBL(e);
                    }
                });
                panelMenu.add(label4, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label5 ----
                label5.setText("Reglas");
                label5.setForeground(Color.white);
                label5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label5.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ReglasLBL(e);
                    }
                });
                panelMenu.add(label5, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- separator3 ----
                separator3.setForeground(Color.white);
                panelMenu.add(separator3, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

                //---- separator4 ----
                separator4.setForeground(Color.white);
                panelMenu.add(separator4, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- separator5 ----
                separator5.setForeground(Color.white);
                panelMenu.add(separator5, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            panelBase.add(panelMenu, BorderLayout.WEST);

            //======== panelInicio ========
            {
                panelInicio.setForeground(new Color(0xf8f0de));
                panelInicio.setBackground(new Color(0xf8f0de));
                panelInicio.setLayout(new CardLayout());

                //======== panelGestionEmp ========
                {
                    panelGestionEmp.setLayout(null);

                    //---- label6 ----
                    label6.setText("text");
                    panelGestionEmp.add(label6);
                    label6.setBounds(new Rectangle(new Point(35, 60), label6.getPreferredSize()));

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelGestionEmp.getComponentCount(); i++) {
                            Rectangle bounds = panelGestionEmp.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelGestionEmp.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelGestionEmp.setMinimumSize(preferredSize);
                        panelGestionEmp.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelGestionEmp, "card1");

                //======== panelSeguridadYrol ========
                {
                    panelSeguridadYrol.setLayout(null);

                    //---- label7 ----
                    label7.setText("text");
                    panelSeguridadYrol.add(label7);
                    label7.setBounds(new Rectangle(new Point(125, 135), label7.getPreferredSize()));

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelSeguridadYrol.getComponentCount(); i++) {
                            Rectangle bounds = panelSeguridadYrol.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelSeguridadYrol.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelSeguridadYrol.setMinimumSize(preferredSize);
                        panelSeguridadYrol.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelSeguridadYrol, "card2");

                //======== panelBitácoraAct ========
                {
                    panelBitácoraAct.setLayout(null);

                    //---- label8 ----
                    label8.setText("text");
                    panelBitácoraAct.add(label8);
                    label8.setBounds(new Rectangle(new Point(80, 240), label8.getPreferredSize()));

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelBitácoraAct.getComponentCount(); i++) {
                            Rectangle bounds = panelBitácoraAct.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelBitácoraAct.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelBitácoraAct.setMinimumSize(preferredSize);
                        panelBitácoraAct.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelBitácoraAct, "card3");

                //======== panelReglas ========
                {
                    panelReglas.setLayout(null);

                    //---- label9 ----
                    label9.setText("text");
                    panelReglas.add(label9);
                    label9.setBounds(new Rectangle(new Point(525, 320), label9.getPreferredSize()));

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelReglas.getComponentCount(); i++) {
                            Rectangle bounds = panelReglas.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelReglas.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelReglas.setMinimumSize(preferredSize);
                        panelReglas.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelReglas, "card4");
            }
            panelBase.add(panelInicio, BorderLayout.CENTER);
        }
        contentPane.add(panelBase);
        panelBase.setBounds(0, -15, 730, 380);

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
    private JPanel panelBase;
    private JPanel panelMenu;
    private JLabel label1;
    private JSeparator separator1;
    private JLabel label2;
    private JSeparator separator2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JSeparator separator3;
    private JSeparator separator4;
    private JSeparator separator5;
    private JPanel panelInicio;
    private JPanel panelGestionEmp;
    private JLabel label6;
    private JPanel panelSeguridadYrol;
    private JLabel label7;
    private JPanel panelBitácoraAct;
    private JLabel label8;
    private JPanel panelReglas;
    private JLabel label9;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
/*public class InicioAdmin extends JFrame {
    public InicioAdmin() {
        initComponents();
    }

    private void label1MouseClicked(MouseEvent e) {
        // TODO add your code here
        CardLayout cl = (CardLayout) panelInicio.getLayout();
        cl.show(panelInicio, "panelGestionEmp");

    }

    private void SeguridadYrolesLbl(MouseEvent e) {
        // TODO add your code here
        CardLayout cl = (CardLayout) panelInicio.getLayout();
        cl.show(panelInicio, "panelSeguridadYrol");

    }

    private void BitacoraActsLBL(MouseEvent e) {
        // TODO add your code here
        CardLayout cl = (CardLayout) panelInicio.getLayout();
        cl.show(panelInicio, "panelBitacoraAct");

    }

    private void ReglasLBL(MouseEvent e) {
        // TODO add your code here
        CardLayout cl = (CardLayout) panelInicio.getLayout();
        cl.show(panelInicio, "panelReglas");

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panelBase = new JPanel();
        panelMenu = new JPanel();
        label1 = new JLabel();
        separator1 = new JSeparator();
        label2 = new JLabel();
        separator2 = new JSeparator();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        separator3 = new JSeparator();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        panelInicio = new JPanel();
        panelGestionEmp = new JPanel();
        panelSeguridadYrol = new JPanel();
        panelBitácoraAct = new JPanel();
        panelReglas = new JPanel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panelBase ========
        {
            panelBase.setLayout(new BorderLayout());

            //======== panelMenu ========
            {
                panelMenu.setBackground(new Color(0xf2876b));
                panelMenu.setLayout(null);

                //---- label1 ----
                label1.setText("Gesti\u00f3n de empleados");
                label1.setForeground(Color.white);
                label1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label1MouseClicked(e);
                    }
                });
                panelMenu.add(label1);
                label1.setBounds(15, 125, 125, label1.getPreferredSize().height);

                //---- separator1 ----
                separator1.setBackground(Color.white);
                separator1.setForeground(Color.white);
                panelMenu.add(separator1);
                separator1.setBounds(0, 115, 145, 3);

                //---- label2 ----
                label2.setText("ADMINISTRADOR");
                label2.setForeground(Color.white);
                panelMenu.add(label2);
                label2.setBounds(20, 80, 105, 16);

                //---- separator2 ----
                separator2.setForeground(Color.white);
                panelMenu.add(separator2);
                separator2.setBounds(0, 151, 145, 3);

                //---- label3 ----
                label3.setText("Seguridad y roles");
                label3.setForeground(Color.white);
                label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        SeguridadYrolesLbl(e);
                    }
                });
                panelMenu.add(label3);
                label3.setBounds(15, 165, 115, 16);

                //---- label4 ----
                label4.setText("Bit\u00e1cora de actividades");
                label4.setForeground(Color.white);
                label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label4.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        BitacoraActsLBL(e);
                    }
                });
                panelMenu.add(label4);
                label4.setBounds(15, 200, 135, 16);

                //---- label5 ----
                label5.setText("Reglas");
                label5.setForeground(Color.white);
                label5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label5.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ReglasLBL(e);
                    }
                });
                panelMenu.add(label5);
                label5.setBounds(15, 235, 55, 16);

                //---- separator3 ----
                separator3.setForeground(Color.white);
                panelMenu.add(separator3);
                separator3.setBounds(0, 256, 145, 3);

                //---- separator4 ----
                separator4.setForeground(Color.white);
                panelMenu.add(separator4);
                separator4.setBounds(0, 190, 145, 3);

                //---- separator5 ----
                separator5.setForeground(Color.white);
                panelMenu.add(separator5);
                separator5.setBounds(0, 225, 145, 3);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panelMenu.getComponentCount(); i++) {
                        Rectangle bounds = panelMenu.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panelMenu.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panelMenu.setMinimumSize(preferredSize);
                    panelMenu.setPreferredSize(preferredSize);
                }
            }
            panelBase.add(panelMenu, BorderLayout.WEST);

            //======== panelInicio ========
            {
                panelInicio.setForeground(new Color(0xf8f0de));
                panelInicio.setBackground(new Color(0xf8f0de));
                panelInicio.setLayout(new CardLayout());

                //======== panelGestionEmp ========
                {
                    panelGestionEmp.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelGestionEmp.getComponentCount(); i++) {
                            Rectangle bounds = panelGestionEmp.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelGestionEmp.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelGestionEmp.setMinimumSize(preferredSize);
                        panelGestionEmp.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelGestionEmp, "card1");

                //======== panelSeguridadYrol ========
                {
                    panelSeguridadYrol.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelSeguridadYrol.getComponentCount(); i++) {
                            Rectangle bounds = panelSeguridadYrol.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelSeguridadYrol.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelSeguridadYrol.setMinimumSize(preferredSize);
                        panelSeguridadYrol.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelSeguridadYrol, "card2");

                //======== panelBitácoraAct ========
                {
                    panelBitácoraAct.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelBitácoraAct.getComponentCount(); i++) {
                            Rectangle bounds = panelBitácoraAct.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelBitácoraAct.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelBitácoraAct.setMinimumSize(preferredSize);
                        panelBitácoraAct.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelBitácoraAct, "card3");

                //======== panelReglas ========
                {
                    panelReglas.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelReglas.getComponentCount(); i++) {
                            Rectangle bounds = panelReglas.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelReglas.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelReglas.setMinimumSize(preferredSize);
                        panelReglas.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelReglas, "card4");
            }
            panelBase.add(panelInicio, BorderLayout.CENTER);
        }
        contentPane.add(panelBase);
        panelBase.setBounds(0, -15, 730, 380);

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
    private JPanel panelBase;
    private JPanel panelMenu;
    private JLabel label1;
    private JSeparator separator1;
    private JLabel label2;
    private JSeparator separator2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JSeparator separator3;
    private JSeparator separator4;
    private JSeparator separator5;
    private JPanel panelInicio;
    private JPanel panelGestionEmp;
    private JPanel panelSeguridadYrol;
    private JPanel panelBitácoraAct;
    private JPanel panelReglas;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}*/