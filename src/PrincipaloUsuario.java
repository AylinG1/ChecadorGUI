import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
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
        nombre = new JLabel();
        panel1 = new JPanel();
        panelAsistencia = new JPanel();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        label7 = new JLabel();
        fechaact = new JLabel();
        button1 = new JButton();
        label8 = new JLabel();
        label9 = new JLabel();
        label10 = new JLabel();
        label11 = new JLabel();
        minacum = new JLabel();
        panelHorario = new JPanel();
        scrollPane1 = new JScrollPane();
        table3 = new JTable();
        panelRetardos = new JPanel();
        scrollPane3 = new JScrollPane();
        textArea2 = new JTextArea();
        label12 = new JLabel();
        scrollPane4 = new JScrollPane();
        table2 = new JTable();
        label13 = new JLabel();
        nombre2 = new JLabel();
        fechaact2 = new JLabel();
        button2 = new JButton();
        label14 = new JLabel();
        label15 = new JLabel();
        label16 = new JLabel();
        label17 = new JLabel();
        minacum2 = new JLabel();
        panel6 = new JPanel();

        //======== this ========
        setBackground(new Color(0x2385c7));
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel3 ========
        {
            panel3.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
            . EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax
            . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,
            12 ), java. awt. Color. red) ,panel3. getBorder( )) ); panel3. addPropertyChangeListener (new java. beans
            . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .
            getPropertyName () )) throw new RuntimeException( ); }} );
            panel3.setLayout(new BorderLayout());

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
                label6.setBounds(new Rectangle(new Point(25, 235), label6.getPreferredSize()));

                //---- nombre ----
                nombre.setText("Nombre");
                nombre.setFont(new Font("Inter Semi Bold", Font.BOLD, 16));
                panelMenu.add(nombre);
                nombre.setBounds(30, 20, 80, 55);

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

                    //======== scrollPane2 ========
                    {

                        //---- table1 ----
                        table1.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null},
                                {null, null, null},
                            },
                            new String[] {
                                null, null, null
                            }
                        ));
                        scrollPane2.setViewportView(table1);
                    }
                    panelAsistencia.add(scrollPane2);
                    scrollPane2.setBounds(15, 210, 385, 160);

                    //---- label7 ----
                    label7.setText("Asistencias ");
                    panelAsistencia.add(label7);
                    label7.setBounds(25, 65, 173, 16);

                    //---- fechaact ----
                    fechaact.setText("dd/mm/aaaa");
                    panelAsistencia.add(fechaact);
                    fechaact.setBounds(315, 15, 165, 16);

                    //---- button1 ----
                    button1.setText("Hacer un comentario");
                    panelAsistencia.add(button1);
                    button1.setBounds(320, 390, 155, 26);

                    //---- label8 ----
                    label8.setText("Resumen de checada");
                    panelAsistencia.add(label8);
                    label8.setBounds(25, 150, 126, 16);

                    //---- label9 ----
                    label9.setText("D\u00eda");
                    panelAsistencia.add(label9);
                    label9.setBounds(25, 180, 19, 16);

                    //---- label10 ----
                    label10.setText("Hora");
                    panelAsistencia.add(label10);
                    label10.setBounds(160, 180, 27, 16);

                    //---- label11 ----
                    label11.setText("Entrada/Salida");
                    panelAsistencia.add(label11);
                    label11.setBounds(230, 180, 83, 16);

                    //---- minacum ----
                    minacum.setText("text");
                    panelAsistencia.add(minacum);
                    minacum.setBounds(255, 375, 120, 16);

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

                    //======== scrollPane1 ========
                    {
                        scrollPane1.setViewportView(table3);
                    }
                    panelHorario.add(scrollPane1);
                    scrollPane1.setBounds(40, 190, 390, 208);

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

                    //======== scrollPane3 ========
                    {

                        //---- textArea2 ----
                        textArea2.setBackground(Color.lightGray);
                        textArea2.setForeground(Color.pink);
                        scrollPane3.setViewportView(textArea2);
                    }
                    panelRetardos.add(scrollPane3);
                    scrollPane3.setBounds(220, 80, 210, 90);

                    //---- label12 ----
                    label12.setText("Motivo del retardo");
                    panelRetardos.add(label12);
                    label12.setBounds(270, 45, 150, 16);

                    //======== scrollPane4 ========
                    {

                        //---- table2 ----
                        table2.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null},
                                {null, null, null},
                            },
                            new String[] {
                                null, null, null
                            }
                        ));
                        scrollPane4.setViewportView(table2);
                    }
                    panelRetardos.add(scrollPane4);
                    scrollPane4.setBounds(20, 245, 315, 125);

                    //---- label13 ----
                    label13.setText("Mostrar retardos por semana:");
                    panelRetardos.add(label13);
                    label13.setBounds(15, 70, 173, 16);

                    //---- nombre2 ----
                    nombre2.setText("Nombre");
                    nombre2.setFont(new Font("Inter Semi Bold", Font.BOLD, 16));
                    panelRetardos.add(nombre2);
                    nombre2.setBounds(10, 10, 405, 20);

                    //---- fechaact2 ----
                    fechaact2.setText("dd/mm/aaaa");
                    panelRetardos.add(fechaact2);
                    fechaact2.setBounds(290, 5, 165, 16);

                    //---- button2 ----
                    button2.setText("Hacer un comentario");
                    panelRetardos.add(button2);
                    button2.setBounds(305, 405, 155, 26);

                    //---- label14 ----
                    label14.setText("Resumen de checada");
                    panelRetardos.add(label14);
                    label14.setBounds(20, 195, 126, 16);

                    //---- label15 ----
                    label15.setText("D\u00eda");
                    panelRetardos.add(label15);
                    label15.setBounds(30, 225, 19, 16);

                    //---- label16 ----
                    label16.setText("Hora");
                    panelRetardos.add(label16);
                    label16.setBounds(135, 225, 27, 16);

                    //---- label17 ----
                    label17.setText("Entrada/Salida");
                    panelRetardos.add(label17);
                    label17.setBounds(235, 225, 83, 16);

                    //---- minacum2 ----
                    minacum2.setText("text");
                    panelRetardos.add(minacum2);
                    minacum2.setBounds(225, 380, 120, 16);

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
        panel3.setBounds(0, 0, 645, 450);

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
    private JLabel nombre;
    private JPanel panel1;
    private JPanel panelAsistencia;
    private JScrollPane scrollPane2;
    private JTable table1;
    private JLabel label7;
    private JLabel fechaact;
    private JButton button1;
    private JLabel label8;
    private JLabel label9;
    private JLabel label10;
    private JLabel label11;
    private JLabel minacum;
    private JPanel panelHorario;
    private JScrollPane scrollPane1;
    private JTable table3;
    private JPanel panelRetardos;
    private JScrollPane scrollPane3;
    private JTextArea textArea2;
    private JLabel label12;
    private JScrollPane scrollPane4;
    private JTable table2;
    private JLabel label13;
    private JLabel nombre2;
    private JLabel fechaact2;
    private JButton button2;
    private JLabel label14;
    private JLabel label15;
    private JLabel label16;
    private JLabel label17;
    private JLabel minacum2;
    private JPanel panel6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}