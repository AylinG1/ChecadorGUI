import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;

public class InicioAdmin extends JFrame {
    /*
    // JFormDesigner - Variables declaration
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
    private JPanel panelBitacoraAct;
    private JLabel label8;
    private JPanel panelReglas;
    private JLabel label9;
    // JFormDesigner - End of variables declaration
    */
    public InicioAdmin() {
        initComponents(); // generado por JFormDesigner
        agregarEventos();
        mostrarPanel("card1");
    }

    private void agregarEventos() {
        label1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card1");
            }
        });

        label3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card2");
            }
        });

        label4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card3");
            }
        });

        label5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card4");
            }
        });
    }

    private void mostrarPanel(String nombreCard) {
        CardLayout cl = (CardLayout) panelInicio.getLayout();
        cl.show(panelInicio, nombreCard);
    }
    /*
    private void initComponents() {
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
        panelBitacoraAct = new JPanel();
        label8 = new JLabel();
        panelReglas = new JPanel();
        label9 = new JLabel();

        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panelBase ========
        {
            panelBase.setLayout(new BorderLayout());

            //======== panelMenu ========
            {
                panelMenu.setBackground(new Color(0xf2876b));
                panelMenu.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.insets = new Insets(5, 5, 5, 5);

                label2.setText("ADMINISTRADOR");
                label2.setForeground(Color.white);
                panelMenu.add(label2, gbc);

                gbc.gridy = 1;
                panelMenu.add(separator1, gbc);

                gbc.gridy = 2;
                label1.setText("Gestión de empleados");
                label1.setForeground(Color.white);
                label1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panelMenu.add(label1, gbc);

                gbc.gridy = 3;
                panelMenu.add(separator2, gbc);

                gbc.gridy = 4;
                label3.setText("Seguridad y roles");
                label3.setForeground(Color.white);
                label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panelMenu.add(label3, gbc);

                gbc.gridy = 5;
                panelMenu.add(separator4, gbc);

                gbc.gridy = 6;
                label4.setText("Bitácora de actividades");
                label4.setForeground(Color.white);
                label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panelMenu.add(label4, gbc);

                gbc.gridy = 7;
                panelMenu.add(separator5, gbc);

                gbc.gridy = 8;
                label5.setText("Reglas");
                label5.setForeground(Color.white);
                label5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                panelMenu.add(label5, gbc);

                gbc.gridy = 9;
                panelMenu.add(separator3, gbc);
            }
            panelBase.add(panelMenu, BorderLayout.WEST);

            //======== panelInicio (CardLayout) ========
            {
                panelInicio.setBackground(new Color(0xf8f0de));
                panelInicio.setLayout(new CardLayout());

                // Panel 1
                panelGestionEmp.setLayout(null);
                label6.setText("GESTIÓN DE EMPLEADOS");
                panelGestionEmp.add(label6);
                label6.setBounds(50, 50, 200, 30);
                panelInicio.add(panelGestionEmp, "card1");

                // Panel 2
                panelSeguridadYrol.setLayout(null);
                label7.setText("SEGURIDAD Y ROLES");
                panelSeguridadYrol.add(label7);
                label7.setBounds(50, 50, 200, 30);
                panelInicio.add(panelSeguridadYrol, "card2");

                // Panel 3
                panelBitacoraAct.setLayout(null);
                label8.setText("BITÁCORA DE ACTIVIDADES");
                panelBitacoraAct.add(label8);
                label8.setBounds(50, 50, 250, 30);
                panelInicio.add(panelBitacoraAct, "card3");

                // Panel 4
                panelReglas.setLayout(null);
                label9.setText("REGLAS DEL SISTEMA");
                panelReglas.add(label9);
                label9.setBounds(50, 50, 200, 30);
                panelInicio.add(panelReglas, "card4");
            }
            panelBase.add(panelInicio, BorderLayout.CENTER);
        }

        contentPane.add(panelBase);
        panelBase.setBounds(0, 0, 800, 500);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }*/

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

    private void label5MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void label11MouseClicked(MouseEvent e) {
        // TODO add your code here
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Deseas cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose(); // Cierra la ventana actual
            new LoginPrincipal().setVisible(true); // Abre la ventana de login
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panelBase = new JPanel();
        panelMenu = new JPanel();
        label1 = new JLabel();
        separator1 = new JSeparator();
        separator2 = new JSeparator();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        separator3 = new JSeparator();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        label2 = new JLabel();
        label11 = new JLabel();
        panelInicio = new JPanel();
        panelGestionEmp = new JPanel();
        label6 = new JLabel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        panelSeguridadYrol = new JPanel();
        label10 = new JLabel();
        panelBitácoraAct = new JPanel();
        label7 = new JLabel();
        panelReglas = new JPanel();
        label9 = new JLabel();
        label8 = new JLabel();

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
                label1.setBounds(10, 235, 121, label1.getPreferredSize().height);

                //---- separator1 ----
                separator1.setBackground(Color.white);
                separator1.setForeground(Color.white);
                panelMenu.add(separator1);
                separator1.setBounds(10, 75, 125, 20);

                //---- separator2 ----
                separator2.setForeground(Color.white);
                panelMenu.add(separator2);
                separator2.setBounds(10, 260, 125, 20);

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
                label3.setBounds(15, 95, 121, label3.getPreferredSize().height);

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
                label4.setBounds(new Rectangle(new Point(10, 145), label4.getPreferredSize()));

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
                label5.setBounds(15, 190, 121, label5.getPreferredSize().height);

                //---- separator3 ----
                separator3.setForeground(Color.white);
                panelMenu.add(separator3);
                separator3.setBounds(0, 209, 135, 20);

                //---- separator4 ----
                separator4.setForeground(Color.white);
                panelMenu.add(separator4);
                separator4.setBounds(0, 117, 135, 20);

                //---- separator5 ----
                separator5.setForeground(Color.white);
                panelMenu.add(separator5);
                separator5.setBounds(0, 163, 135, 20);

                //---- label2 ----
                label2.setText("ADMINISTRADOR");
                label2.setForeground(Color.white);
                label2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                panelMenu.add(label2);
                label2.setBounds(15, 40, 121, label2.getPreferredSize().height);

                //---- label11 ----
                label11.setText("Cerrar sesi\u00f3n");
                label11.setForeground(Color.white);
                label11.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label5MouseClicked(e);
                        label11MouseClicked(e);
                    }
                });
                panelMenu.add(label11);
                label11.setBounds(15, 340, 125, 16);

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
                    panelGestionEmp.setBackground(new Color(0xf8f0de));
                    panelGestionEmp.setLayout(null);

                    //---- label6 ----
                    label6.setText("Gesti\u00f3n empleados");
                    label6.setForeground(new Color(0xf2876b));
                    panelGestionEmp.add(label6);
                    label6.setBounds(new Rectangle(new Point(35, 60), label6.getPreferredSize()));

                    //======== scrollPane1 ========
                    {

                        //---- table1 ----
                        table1.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                                {null, null},
                            },
                            new String[] {
                                "NOMBRE", "ESTATUS"
                            }
                        ));
                        scrollPane1.setViewportView(table1);
                    }
                    panelGestionEmp.add(scrollPane1);
                    scrollPane1.setBounds(105, 105, 300, 238);

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
                    panelSeguridadYrol.setBackground(new Color(0xf8f0de));
                    panelSeguridadYrol.setLayout(null);

                    //---- label10 ----
                    label10.setText("Panel seguridad y roles");
                    label10.setForeground(new Color(0xf2876b));
                    panelSeguridadYrol.add(label10);
                    label10.setBounds(15, 65, 155, 16);

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
                    panelBitácoraAct.setBackground(new Color(0xf8f0de));
                    panelBitácoraAct.setLayout(null);

                    //---- label7 ----
                    label7.setText("Bit\u00e1cora de actividades");
                    label7.setForeground(new Color(0xf2876b));
                    panelBitácoraAct.add(label7);
                    label7.setBounds(20, 45, 160, 25);

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
                    panelReglas.setBackground(new Color(0xf8f0de));
                    panelReglas.setLayout(null);

                    //---- label9 ----
                    label9.setText("text");
                    panelReglas.add(label9);
                    label9.setBounds(new Rectangle(new Point(525, 320), label9.getPreferredSize()));

                    //---- label8 ----
                    label8.setText("Reglas");
                    label8.setForeground(new Color(0xf2876b));
                    panelReglas.add(label8);
                    label8.setBounds(10, 30, 102, 16);

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
        panelBase.setBounds(-10, -5, 730, 380);

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
    private JSeparator separator2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JSeparator separator3;
    private JSeparator separator4;
    private JSeparator separator5;
    private JLabel label2;
    private JLabel label11;
    private JPanel panelInicio;
    private JPanel panelGestionEmp;
    private JLabel label6;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panelSeguridadYrol;
    private JLabel label10;
    private JPanel panelBitácoraAct;
    private JLabel label7;
    private JPanel panelReglas;
    private JLabel label9;
    private JLabel label8;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}