import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/*
 * Created by JFormDesigner on Wed Jul 23 20:49:17 CST 2025
 */



/**
 * @author JuanMa
 */
public class PrincipalSupervisor extends JFrame {
    public PrincipalSupervisor() {
        initComponents();
        agregarEventos();        // Añadimos eventos de clic a los labels
        mostrarPanel("reportes"); // Mostramos panel por defecto al iniciar
    }


    private void agregarEventos() {
        // Opcional: cambiar cursor a "mano" al pasar sobre los labels
        label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Agregamos eventos a cada label para cambiar de panel
        label3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("reportes");
            }
        });

        label4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("registros");
            }
        });

        label5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("alertas");
            }
        });
    }

    private void mostrarPanel(String nombreCard) {
        CardLayout cl = (CardLayout) panelInicio.getLayout();
        cl.show(panelInicio, nombreCard);
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

    private void label6MouseClicked(MouseEvent e) {
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
        // Generated using JFormDesigner Evaluation license - liz
        panel4 = new JPanel();
        panelMenu = new JPanel();
        separator1 = new JSeparator();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        separator3 = new JSeparator();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        label2 = new JLabel();
        label6 = new JLabel();
        label1 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();
        label13 = new JLabel();
        panelInicio = new JPanel();
        panelReportes = new JPanel();
        label10 = new JLabel();
        panel1 = new JPanel();
        panelRegistros = new JPanel();
        label7 = new JLabel();
        scrollBar1 = new JScrollBar();
        panelAlertas = new JPanel();
        label9 = new JLabel();
        label8 = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel4 ========
        {
            panel4.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder
            ( 0, 0 ,0 , 0) ,  "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border
            .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,12 ) ,java . awt
            . Color .red ) ,panel4. getBorder () ) ); panel4. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void
            propertyChange (java . beans. PropertyChangeEvent e) { if( "\u0062ord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( )
            ;} } );
            panel4.setLayout(new BorderLayout());

            //======== panelMenu ========
            {
                panelMenu.setBackground(Color.white);
                panelMenu.setLayout(null);

                //---- separator1 ----
                separator1.setBackground(Color.white);
                separator1.setForeground(new Color(0xff6633));
                panelMenu.add(separator1);
                separator1.setBounds(0, 70, 135, 25);

                //---- label3 ----
                label3.setText("Reportes");
                label3.setForeground(new Color(0xff6633));
                label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label3.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                label3.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        SeguridadYrolesLbl(e);
                    }
                });
                panelMenu.add(label3);
                label3.setBounds(55, 90, 60, label3.getPreferredSize().height);

                //---- label4 ----
                label4.setText("Registros");
                label4.setForeground(new Color(0xff6633));
                label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label4.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                label4.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        BitacoraActsLBL(e);
                    }
                });
                panelMenu.add(label4);
                label4.setBounds(new Rectangle(new Point(55, 135), label4.getPreferredSize()));

                //---- label5 ----
                label5.setText("Alertas");
                label5.setForeground(new Color(0xff6633));
                label5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                label5.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                label5.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ReglasLBL(e);
                    }
                });
                panelMenu.add(label5);
                label5.setBounds(55, 185, 60, label5.getPreferredSize().height);

                //---- separator3 ----
                separator3.setForeground(new Color(0xff6633));
                panelMenu.add(separator3);
                separator3.setBounds(0, 215, 135, 20);

                //---- separator4 ----
                separator4.setForeground(new Color(0xff6633));
                panelMenu.add(separator4);
                separator4.setBounds(0, 120, 135, 13);

                //---- separator5 ----
                separator5.setForeground(new Color(0xff6633));
                panelMenu.add(separator5);
                separator5.setBounds(0, 170, 135, 13);

                //---- label2 ----
                label2.setText("SUPERVISOR");
                label2.setForeground(new Color(0xff6633));
                label2.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
                panelMenu.add(label2);
                label2.setBounds(15, 40, 121, label2.getPreferredSize().height);

                //---- label6 ----
                label6.setText("Cerrar sesi\u00f3n");
                label6.setForeground(Color.black);
                label6.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label5MouseClicked(e);
                        label6MouseClicked(e);
                    }
                });
                panelMenu.add(label6);
                label6.setBounds(0, 305, 125, 16);

                //---- label1 ----
                label1.setIcon(new ImageIcon(getClass().getResource("/lolo.jpg")));
                panelMenu.add(label1);
                label1.setBounds(new Rectangle(new Point(5, 75), label1.getPreferredSize()));

                //---- label11 ----
                label11.setIcon(new ImageIcon(getClass().getResource("/ki.jpg")));
                panelMenu.add(label11);
                label11.setBounds(new Rectangle(new Point(10, 125), label11.getPreferredSize()));

                //---- label12 ----
                label12.setIcon(new ImageIcon(getClass().getResource("/fd.jpg")));
                panelMenu.add(label12);
                label12.setBounds(new Rectangle(new Point(10, 175), label12.getPreferredSize()));

                //---- label13 ----
                label13.setIcon(new ImageIcon(getClass().getResource("/sal.jpg")));
                panelMenu.add(label13);
                label13.setBounds(new Rectangle(new Point(90, 295), label13.getPreferredSize()));

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
            panel4.add(panelMenu, BorderLayout.WEST);

            //======== panelInicio ========
            {
                panelInicio.setForeground(new Color(0xf8f0de));
                panelInicio.setBackground(new Color(0xf8f0de));
                panelInicio.setLayout(new CardLayout());

                //======== panelReportes ========
                {
                    panelReportes.setBackground(new Color(0xff9966));
                    panelReportes.setLayout(null);

                    //---- label10 ----
                    label10.setText("Panel reportes");
                    label10.setForeground(new Color(0xcccccc));
                    panelReportes.add(label10);
                    label10.setBounds(20, 35, 155, 16);

                    //======== panel1 ========
                    {
                        panel1.setBackground(new Color(0xffcc99));
                        panel1.setLayout(null);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panel1.getComponentCount(); i++) {
                                Rectangle bounds = panel1.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panel1.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panel1.setMinimumSize(preferredSize);
                            panel1.setPreferredSize(preferredSize);
                        }
                    }
                    panelReportes.add(panel1);
                    panel1.setBounds(25, 70, 110, 65);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelReportes.getComponentCount(); i++) {
                            Rectangle bounds = panelReportes.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelReportes.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelReportes.setMinimumSize(preferredSize);
                        panelReportes.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelReportes, "card2");

                //======== panelRegistros ========
                {
                    panelRegistros.setBackground(new Color(0xf8f0de));
                    panelRegistros.setLayout(null);

                    //---- label7 ----
                    label7.setText("Registros");
                    label7.setForeground(new Color(0xf2876b));
                    panelRegistros.add(label7);
                    label7.setBounds(20, 45, 160, 25);
                    panelRegistros.add(scrollBar1);
                    scrollBar1.setBounds(440, 0, 15, 345);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelRegistros.getComponentCount(); i++) {
                            Rectangle bounds = panelRegistros.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelRegistros.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelRegistros.setMinimumSize(preferredSize);
                        panelRegistros.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelRegistros, "card3");

                //======== panelAlertas ========
                {
                    panelAlertas.setBackground(new Color(0xf8f0de));
                    panelAlertas.setLayout(null);

                    //---- label9 ----
                    label9.setText("text");
                    panelAlertas.add(label9);
                    label9.setBounds(new Rectangle(new Point(525, 320), label9.getPreferredSize()));

                    //---- label8 ----
                    label8.setText("Alertas");
                    label8.setForeground(new Color(0xf2876b));
                    panelAlertas.add(label8);
                    label8.setBounds(10, 30, 102, 16);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelAlertas.getComponentCount(); i++) {
                            Rectangle bounds = panelAlertas.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelAlertas.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelAlertas.setMinimumSize(preferredSize);
                        panelAlertas.setPreferredSize(preferredSize);
                    }
                }
                panelInicio.add(panelAlertas, "card4");
            }
            panel4.add(panelInicio, BorderLayout.CENTER);
        }
        contentPane.add(panel4);
        panel4.setBounds(5, 0, 590, 345);

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
    // Generated using JFormDesigner Evaluation license - liz
    private JPanel panel4;
    private JPanel panelMenu;
    private JSeparator separator1;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JSeparator separator3;
    private JSeparator separator4;
    private JSeparator separator5;
    private JLabel label2;
    private JLabel label6;
    private JLabel label1;
    private JLabel label11;
    private JLabel label12;
    private JLabel label13;
    private JPanel panelInicio;
    private JPanel panelReportes;
    private JLabel label10;
    private JPanel panel1;
    private JPanel panelRegistros;
    private JLabel label7;
    private JScrollBar scrollBar1;
    private JPanel panelAlertas;
    private JLabel label9;
    private JLabel label8;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
