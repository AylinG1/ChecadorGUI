import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/*
 * Created by JFormDesigner on Wed Jul 23 10:49:29 GMT-06:00 2025
 */



/**
 * @author aylin
 */
public class PrincipaloUsuario extends JFrame {
    public PrincipaloUsuario() {
        initComponents();
        agregarEventos();         // añadimos eventos de clic a los labels
        mostrarPanel("card1");
    }

    private void agregarEventos() {
        // Hacemos que los labels se vean como clicables
        label1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Agregamos eventos a cada label para cambiar de panel
        label1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card1"); // Asistencia
            }
        });

        label3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card2"); // Horario
            }
        });

        label4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card3"); // Retardos
            }
        });
    }

    private void mostrarPanel(String nombreCard) {
        CardLayout cl = (CardLayout) panel1.getLayout();
        cl.show(panel1, nombreCard);
    }
    private void button1(ActionEvent e) {
        Retardos retardos = new Retardos();
        this.dispose();
        retardos.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Juan
        panel3 = new JPanel();
        panel4 = new JPanel();
        panelMenu = new JPanel();
        label1 = new JLabel();
        separator1 = new JSeparator();
        label2 = new JLabel();
        separator2 = new JSeparator();
        label3 = new JLabel();
        label4 = new JLabel();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        label6 = new JLabel();
        panel1 = new JPanel();
        panelAsistencia = new JPanel();
        panelHorario = new JPanel();
        panelRetardos = new JPanel();
        panel6 = new JPanel();

        //======== this ========
        setBackground(new Color(0x2385c7));
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel3 ========
        {
            panel3.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing
            . border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e" , javax. swing .border . TitledBorder
            . CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069al\u006fg", java .
            awt . Font. BOLD ,12 ) ,java . awt. Color .red ) ,panel3. getBorder () ) )
            ; panel3. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e
            ) { if( "\u0062or\u0064er" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } )
            ;
            panel3.setLayout(new BorderLayout());

            //======== panel4 ========
            {
                panel4.setMaximumSize(new Dimension(1002773, 1002773));
                panel4.setMinimumSize(new Dimension(1000, 1000));
                panel4.setLayout(null);
            }
            panel3.add(panel4, BorderLayout.NORTH);

            //======== panelMenu ========
            {
                panelMenu.setBackground(new Color(0xf2876b));
                panelMenu.setLayout(null);

                //---- label1 ----
                label1.setText("Asistencia");
                label1.setForeground(Color.white);
                panelMenu.add(label1);
                label1.setBounds(15, 125, 125, label1.getPreferredSize().height);

                //---- separator1 ----
                separator1.setBackground(Color.white);
                separator1.setForeground(Color.white);
                panelMenu.add(separator1);
                separator1.setBounds(0, 115, 145, 3);

                //---- label2 ----
                label2.setText("USUARIO");
                label2.setForeground(Color.white);
                label2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                panelMenu.add(label2);
                label2.setBounds(25, 90, 105, 16);

                //---- separator2 ----
                separator2.setForeground(Color.white);
                panelMenu.add(separator2);
                separator2.setBounds(0, 151, 145, 3);

                //---- label3 ----
                label3.setText("Horario");
                label3.setForeground(Color.white);
                panelMenu.add(label3);
                label3.setBounds(15, 165, 115, 16);

                //---- label4 ----
                label4.setText("Retardos");
                label4.setForeground(Color.white);
                panelMenu.add(label4);
                label4.setBounds(15, 200, 135, 16);

                //---- separator4 ----
                separator4.setForeground(Color.white);
                panelMenu.add(separator4);
                separator4.setBounds(0, 190, 145, 3);

                //---- separator5 ----
                separator5.setForeground(Color.white);
                panelMenu.add(separator5);
                separator5.setBounds(0, 225, 145, 3);

                //---- label6 ----
                label6.setIcon(new ImageIcon(getClass().getResource("/Captura de pantalla 2025-07-24 135647.png")));
                panelMenu.add(label6);
                label6.setBounds(new Rectangle(new Point(20, 0), label6.getPreferredSize()));

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
            panel3.add(panelMenu, BorderLayout.WEST);

            //======== panel1 ========
            {
                panel1.setLayout(new CardLayout());

                //======== panelAsistencia ========
                {
                    panelAsistencia.setBackground(new Color(0xf8f0de));
                    panelAsistencia.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelAsistencia.getComponentCount(); i++) {
                            Rectangle bounds = panelAsistencia.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelAsistencia.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelAsistencia.setMinimumSize(preferredSize);
                        panelAsistencia.setPreferredSize(preferredSize);
                    }
                }
                panel1.add(panelAsistencia, "card1");

                //======== panelHorario ========
                {
                    panelHorario.setBackground(new Color(0xf8f0de));
                    panelHorario.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelHorario.getComponentCount(); i++) {
                            Rectangle bounds = panelHorario.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelHorario.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelHorario.setMinimumSize(preferredSize);
                        panelHorario.setPreferredSize(preferredSize);
                    }
                }
                panel1.add(panelHorario, "card2");

                //======== panelRetardos ========
                {
                    panelRetardos.setForeground(new Color(0xf8f0de));
                    panelRetardos.setBackground(new Color(0xf8f0de));
                    panelRetardos.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panelRetardos.getComponentCount(); i++) {
                            Rectangle bounds = panelRetardos.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panelRetardos.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panelRetardos.setMinimumSize(preferredSize);
                        panelRetardos.setPreferredSize(preferredSize);
                    }
                }
                panel1.add(panelRetardos, "card3");

                //======== panel6 ========
                {
                    panel6.setBackground(new Color(0xf8f0de));
                    panel6.setLayout(null);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panel6.getComponentCount(); i++) {
                            Rectangle bounds = panel6.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panel6.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panel6.setMinimumSize(preferredSize);
                        panel6.setPreferredSize(preferredSize);
                    }
                }
                panel1.add(panel6, "card4");
            }
            panel3.add(panel1, BorderLayout.CENTER);
        }
        contentPane.add(panel3);
        panel3.setBounds(0, 0, 620, 450);

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
    // Generated using JFormDesigner Evaluation license - Juan
    private JPanel panel3;
    private JPanel panel4;
    private JPanel panelMenu;
    private JLabel label1;
    private JSeparator separator1;
    private JLabel label2;
    private JSeparator separator2;
    private JLabel label3;
    private JLabel label4;
    private JSeparator separator4;
    private JSeparator separator5;
    private JLabel label6;
    private JPanel panel1;
    private JPanel panelAsistencia;
    private JPanel panelHorario;
    private JPanel panelRetardos;
    private JPanel panel6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}