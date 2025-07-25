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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Juan
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
        panelInicio = new JPanel();
        panelReportes = new JPanel();
        label10 = new JLabel();
        panelRegistros = new JPanel();
        label7 = new JLabel();
        panelAlertas = new JPanel();
        label9 = new JLabel();
        label8 = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel4 ========
        {
            panel4.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0
            ,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM
            ,new java.awt.Font("D\u0069alog",java.awt.Font.BOLD,12),java.awt.Color.red),
            panel4. getBorder()));panel4. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e
            ){if("\u0062order".equals(e.getPropertyName()))throw new RuntimeException();}});
            panel4.setLayout(new BorderLayout());

            //======== panelMenu ========
            {
                panelMenu.setBackground(new Color(0xf2876b));
                panelMenu.setLayout(null);

                //---- separator1 ----
                separator1.setBackground(Color.white);
                separator1.setForeground(Color.white);
                panelMenu.add(separator1);
                separator1.setBounds(10, 75, 125, 20);

                //---- label3 ----
                label3.setText("Reportes");
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
                label4.setText("Registros");
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
                label5.setText("Alertas");
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
                label2.setText("SUPERVISOR");
                label2.setForeground(Color.white);
                label2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                panelMenu.add(label2);
                label2.setBounds(15, 40, 121, label2.getPreferredSize().height);

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
                    panelReportes.setBackground(new Color(0xf8f0de));
                    panelReportes.setLayout(null);

                    //---- label10 ----
                    label10.setText("Panel reportes");
                    label10.setForeground(new Color(0xf2876b));
                    panelReportes.add(label10);
                    label10.setBounds(15, 65, 155, 16);

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
    // Generated using JFormDesigner Evaluation license - Juan
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
    private JPanel panelInicio;
    private JPanel panelReportes;
    private JLabel label10;
    private JPanel panelRegistros;
    private JLabel label7;
    private JPanel panelAlertas;
    private JLabel label9;
    private JLabel label8;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
