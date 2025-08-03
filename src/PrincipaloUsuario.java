import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


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
        nombre.setText(SesionUsuario.usuarioActual);
        lblRetardos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card3"); // Muestra el panelRetardos
                cargarRetardosEnPanel(); // Llama la lógica para cargar los datos
            }
        });

    }



    public void cargarRetardosEnPanel() {
        // === Fecha ===
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaActual.format(formato);
        fechaact2.setText("Fecha: " + fechaFormateada);  // Asegúrate que sea el label correcto

        // === Usuario ===
        nombre2.setText(SesionUsuario.usuarioActual);

        String usuario = nombre2.getText();

        String sql = "SELECT " +
                "    rc.fecha, " +
                "    CASE " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_1%' THEN t.entrada_1 " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_2%' THEN t.entrada_2 " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_3%' THEN t.entrada_3 " +
                "        ELSE NULL " +
                "    END AS entrada_valida, " +
                "    rc.hora, " +
                "    CASE " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_1%' AND rc.hora > t.entrada_1 " +
                "            THEN DATEDIFF(MINUTE, t.entrada_1, rc.hora) " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_2%' AND rc.hora > t.entrada_2 " +
                "            THEN DATEDIFF(MINUTE, t.entrada_2, rc.hora) " +
                "        WHEN rc.tipo_registro LIKE 'ENTRADA_3%' AND rc.hora > t.entrada_3 " +
                "            THEN DATEDIFF(MINUTE, t.entrada_3, rc.hora) " +
                "        ELSE 0 " +
                "    END AS minutos_retraso_bruto, " +
                "    rc.tipo_registro " +
                "FROM Registros_Checada rc " +
                "JOIN Empleados e ON rc.id_empleado = e.id " +
                "JOIN Turnos t ON e.id_turno = t.id " +
                "WHERE e.nombre = ? " +
                "  AND ( " +
                "        (rc.tipo_registro LIKE 'ENTRADA_1%' AND t.entrada_1 IS NOT NULL AND rc.hora > t.entrada_1) OR " +
                "        (rc.tipo_registro LIKE 'ENTRADA_2%' AND t.entrada_2 IS NOT NULL AND rc.hora > t.entrada_2) OR " +
                "        (rc.tipo_registro LIKE 'ENTRADA_3%' AND t.entrada_3 IS NOT NULL AND rc.hora > t.entrada_3) " +
                "      ) " +
                "ORDER BY rc.fecha, entrada_valida";

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            base = new BaseSQL();
            ps = base.conn.prepareStatement(sql);
            ps.setString(1, usuario);
            rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Fecha", "Entrada Válida", "Hora Checada", "Minutos Retraso Bruto", "Tipo Registro"}, 0);

            int minutosAcumuladosTotales = 0;

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getDate("fecha");
                fila[1] = rs.getTime("entrada_valida");
                fila[2] = rs.getTime("hora");
                int minutosRetrasoBruto = rs.getInt("minutos_retraso_bruto");
                fila[3] = minutosRetrasoBruto;
                fila[4] = rs.getString("tipo_registro");

                if (minutosRetrasoBruto > 0) {
                    model.addRow(fila);
                    minutosAcumuladosTotales += minutosRetrasoBruto;
                }
            }

            table2.setModel(model); // Usa la tabla en tu panel
            minacum2.setText("Minutos acumulados : " + minutosAcumuladosTotales);

            if (minutosAcumuladosTotales > 15) {
                minacum2.setForeground(Color.RED);
            } else {
                minacum2.setForeground(Color.GREEN);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar retardos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            try { if (base != null) base.cerrar(); } catch (SQLException ex) { ex.printStackTrace(); }
        }

    }


    private void agregarEventos() {
        // Hacemos que los labels se vean como clicables
        label1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRetardos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

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

        lblRetardos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card3"); // Retardos
                cargarRetardosEnPanel();
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

    private void label5MouseClicked(MouseEvent e) {
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
        panel3 = new JPanel();
        panelMenu = new JPanel();
        label1 = new JLabel();
        separator1 = new JSeparator();
        label2 = new JLabel();
        separator2 = new JSeparator();
        label3 = new JLabel();
        lblRetardos = new JLabel();
        separator4 = new JSeparator();
        separator5 = new JSeparator();
        nombre = new JLabel();
        label5 = new JLabel();
        label16 = new JLabel();
        label17 = new JLabel();
        label18 = new JLabel();
        label19 = new JLabel();
        label20 = new JLabel();
        panel1 = new JPanel();
        panelAsistencia = new JPanel();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        label7 = new JLabel();
        fechaact = new JLabel();
        label8 = new JLabel();
        label12 = new JLabel();
        nombre3 = new JLabel();
        panel2 = new JPanel();
        minacum = new JLabel();
        minacumretardo = new JLabel();
        minacumfalta = new JLabel();
        panelHorario = new JPanel();
        scrollPane1 = new JScrollPane();
        tablaHorarios = new JTable();
        label15 = new JLabel();
        labelTurno = new JLabel();
        panelRetardos = new JPanel();
        scrollPane4 = new JScrollPane();
        table2 = new JTable();
        nombre2 = new JLabel();
        fechaact2 = new JLabel();
        label14 = new JLabel();
        label4 = new JLabel();
        label6 = new JLabel();
        label21 = new JLabel();
        panel4 = new JPanel();
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
                panelMenu.setBackground(Color.white);
                panelMenu.setLayout(null);

                //---- label1 ----
                label1.setText("Asistencia");
                label1.setForeground(new Color(0xff6600));
                label1.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                panelMenu.add(label1);
                label1.setBounds(60, 135, 85, label1.getPreferredSize().height);

                //---- separator1 ----
                separator1.setBackground(Color.white);
                separator1.setForeground(new Color(0xff6633));
                panelMenu.add(separator1);
                separator1.setBounds(0, 120, 145, 3);

                //---- label2 ----
                label2.setText("EMPLEADO");
                label2.setForeground(new Color(0xff6633));
                label2.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
                panelMenu.add(label2);
                label2.setBounds(10, 80, 105, 16);

                //---- separator2 ----
                separator2.setForeground(new Color(0xff6600));
                panelMenu.add(separator2);
                separator2.setBounds(0, 160, 145, 3);

                //---- label3 ----
                label3.setText("Horario");
                label3.setForeground(new Color(0xff6600));
                label3.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                panelMenu.add(label3);
                label3.setBounds(60, 175, 65, 16);

                //---- lblRetardos ----
                lblRetardos.setText("Retardos");
                lblRetardos.setForeground(new Color(0xff6600));
                lblRetardos.setFont(new Font("Microsoft JhengHei Light", Font.PLAIN, 13));
                panelMenu.add(lblRetardos);
                lblRetardos.setBounds(60, 215, 70, 16);

                //---- separator4 ----
                separator4.setForeground(new Color(0xff6600));
                panelMenu.add(separator4);
                separator4.setBounds(0, 200, 145, 3);

                //---- separator5 ----
                separator5.setForeground(new Color(0xff6600));
                panelMenu.add(separator5);
                separator5.setBounds(0, 240, 145, 3);

                //---- nombre ----
                nombre.setText("Nombre");
                nombre.setFont(new Font("Franklin Gothic Demi Cond", Font.BOLD, 16));
                nombre.setForeground(new Color(0xff6600));
                panelMenu.add(nombre);
                nombre.setBounds(5, 20, 190, 55);

                //---- label5 ----
                label5.setText("Cerrar sesi\u00f3n");
                label5.setForeground(Color.black);
                label5.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        label5MouseClicked(e);
                    }
                });
                panelMenu.add(label5);
                label5.setBounds(5, 410, 125, 16);

                //---- label16 ----
                label16.setIcon(new ImageIcon(getClass().getResource("/sal.jpg")));
                panelMenu.add(label16);
                label16.setBounds(95, 395, 45, 50);

                //---- label17 ----
                label17.setIcon(new ImageIcon(getClass().getResource("/lii.jpg")));
                panelMenu.add(label17);
                label17.setBounds(new Rectangle(new Point(10, 120), label17.getPreferredSize()));

                //---- label18 ----
                label18.setIcon(new ImageIcon(getClass().getResource("/loc.jpg")));
                panelMenu.add(label18);
                label18.setBounds(new Rectangle(new Point(10, 160), label18.getPreferredSize()));

                //---- label19 ----
                label19.setIcon(new ImageIcon(getClass().getResource("/lk.jpg")));
                panelMenu.add(label19);
                label19.setBounds(new Rectangle(new Point(85, 75), label19.getPreferredSize()));

                //---- label20 ----
                label20.setIcon(new ImageIcon(getClass().getResource("/ly.jpg")));
                panelMenu.add(label20);
                label20.setBounds(new Rectangle(new Point(10, 205), label20.getPreferredSize()));

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
                    panelAsistencia.setBackground(new Color(0xff9966));
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
                    scrollPane2.setBounds(20, 180, 475, 190);

                    //---- label7 ----
                    label7.setText("Asistencias Semanales :");
                    label7.setForeground(Color.black);
                    label7.setFont(new Font("Inter", Font.PLAIN, 14));
                    panelAsistencia.add(label7);
                    label7.setBounds(25, 90, 190, 35);

                    //---- fechaact ----
                    fechaact.setText("dd/mm/aaaa");
                    panelAsistencia.add(fechaact);
                    fechaact.setBounds(390, 10, 170, 30);

                    //---- label8 ----
                    label8.setText("Resumen de checada");
                    label8.setForeground(new Color(0x333333));
                    label8.setFont(new Font("NSimSun", Font.PLAIN, 13));
                    panelAsistencia.add(label8);
                    label8.setBounds(20, 155, 185, 16);

                    //---- label12 ----
                    label12.setText("Bienvenid@");
                    label12.setFont(new Font("Inter Semi Bold", Font.BOLD | Font.ITALIC, 16));
                    label12.setForeground(Color.black);
                    panelAsistencia.add(label12);
                    label12.setBounds(25, 15, 110, 26);

                    //---- nombre3 ----
                    nombre3.setText("Nombre");
                    nombre3.setFont(new Font("Inter Semi Bold", Font.BOLD, 16));
                    nombre3.setForeground(Color.black);
                    panelAsistencia.add(nombre3);
                    nombre3.setBounds(25, 40, 405, 20);

                    //======== panel2 ========
                    {
                        panel2.setForeground(new Color(0xccffcc));
                        panel2.setBackground(new Color(0xffb096));
                        panel2.setLayout(null);

                        //---- minacum ----
                        minacum.setText("text");
                        panel2.add(minacum);
                        minacum.setBounds(20, 10, 325, 16);

                        //---- minacumretardo ----
                        minacumretardo.setText("text");
                        panel2.add(minacumretardo);
                        minacumretardo.setBounds(20, 35, 315, minacumretardo.getPreferredSize().height);

                        //---- minacumfalta ----
                        minacumfalta.setText("text");
                        panel2.add(minacumfalta);
                        minacumfalta.setBounds(20, 60, 320, minacumfalta.getPreferredSize().height);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panel2.getComponentCount(); i++) {
                                Rectangle bounds = panel2.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panel2.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panel2.setMinimumSize(preferredSize);
                            panel2.setPreferredSize(preferredSize);
                        }
                    }
                    panelAsistencia.add(panel2);
                    panel2.setBounds(20, 395, 475, 105);

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
                    panelHorario.setBackground(new Color(0xff9966));
                    panelHorario.setLayout(null);

                    //======== scrollPane1 ========
                    {

                        //---- tablaHorarios ----
                        tablaHorarios.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null, null, null, null, ""},
                                {null, null, null, null, null, null, null},
                                {null, null, null, null, null, null, null},
                                {null, null, null, null, null, null, null},
                                {null, null, null, null, null, null, null},
                                {null, null, null, null, null, null, null},
                                {null, null, null, null, null, null, null},
                            },
                            new String[] {
                                "DOMINGO", "LUNES", "MARTES", "MI\u00c9RCOLES", "JUEVES", "VIERNES", "S\u00c1BADO"
                            }
                        ));
                        scrollPane1.setViewportView(tablaHorarios);
                    }
                    panelHorario.add(scrollPane1);
                    scrollPane1.setBounds(25, 190, 510, 190);

                    //---- label15 ----
                    label15.setText("Horario");
                    label15.setFont(new Font("Inter", Font.BOLD, 18));
                    panelHorario.add(label15);
                    label15.setBounds(15, 75, 190, 40);

                    //---- labelTurno ----
                    labelTurno.setText("text");
                    panelHorario.add(labelTurno);
                    labelTurno.setBounds(30, 135, 385, labelTurno.getPreferredSize().height);

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
                    panelRetardos.setBackground(new Color(0xff9966));
                    panelRetardos.setLayout(null);

                    //======== scrollPane4 ========
                    {

                        //---- table2 ----
                        table2.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null, null},
                                {null, null, null},
                            },
                            new String[] {
                                "D\u00eda", "Hora", "Entrada/Salida"
                            }
                        ));
                        scrollPane4.setViewportView(table2);
                    }
                    panelRetardos.add(scrollPane4);
                    scrollPane4.setBounds(15, 220, 535, 185);

                    //---- nombre2 ----
                    nombre2.setText("Nombre");
                    nombre2.setFont(new Font("Inter Semi Bold", Font.BOLD, 16));
                    nombre2.setForeground(Color.black);
                    panelRetardos.add(nombre2);
                    nombre2.setBounds(30, 45, 405, 20);

                    //---- fechaact2 ----
                    fechaact2.setText("dd/mm/aaaa");
                    fechaact2.setForeground(Color.black);
                    panelRetardos.add(fechaact2);
                    fechaact2.setBounds(395, 25, 165, 16);

                    //---- label14 ----
                    label14.setText("Resumen de checada");
                    label14.setForeground(Color.black);
                    panelRetardos.add(label14);
                    label14.setBounds(45, 190, 180, 16);

                    //---- label4 ----
                    label4.setText("Bienvenid@");
                    label4.setFont(new Font("Inter Semi Bold", Font.BOLD | Font.ITALIC, 16));
                    label4.setForeground(Color.black);
                    panelRetardos.add(label4);
                    label4.setBounds(30, 20, 110, 26);

                    //---- label6 ----
                    label6.setText("Puedes ver aqui tus retardos");
                    label6.setFont(new Font("Inter Semi Bold", Font.BOLD | Font.ITALIC, 16));
                    label6.setForeground(Color.black);
                    panelRetardos.add(label6);
                    label6.setBounds(60, 100, 270, 26);

                    //---- label21 ----
                    label21.setText("___________________");
                    panelRetardos.add(label21);
                    label21.setBounds(375, 30, 161, 16);

                    //======== panel4 ========
                    {
                        panel4.setBackground(new Color(0xccffcc));
                        panel4.setLayout(null);

                        //---- minacum2 ----
                        minacum2.setText("text");
                        minacum2.setForeground(Color.black);
                        panel4.add(minacum2);
                        minacum2.setBounds(15, 15, 190, 16);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panel4.getComponentCount(); i++) {
                                Rectangle bounds = panel4.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panel4.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panel4.setMinimumSize(preferredSize);
                            panel4.setPreferredSize(preferredSize);
                        }
                    }
                    panelRetardos.add(panel4);
                    panel4.setBounds(50, 430, 270, 55);

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
        panel3.setBounds(0, 0, 810, 565);

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
    private JPanel panel3;
    private JPanel panelMenu;
    private JLabel label1;
    private JSeparator separator1;
    private JLabel label2;
    private JSeparator separator2;
    private JLabel label3;
    private JLabel lblRetardos;
    private JSeparator separator4;
    private JSeparator separator5;
    private JLabel nombre;
    private JLabel label5;
    private JLabel label16;
    private JLabel label17;
    private JLabel label18;
    private JLabel label19;
    private JLabel label20;
    private JPanel panel1;
    private JPanel panelAsistencia;
    private JScrollPane scrollPane2;
    private JTable table1;
    private JLabel label7;
    private JLabel fechaact;
    private JLabel label8;
    private JLabel label12;
    private JLabel nombre3;
    private JPanel panel2;
    private JLabel minacum;
    private JLabel minacumretardo;
    private JLabel minacumfalta;
    private JPanel panelHorario;
    private JScrollPane scrollPane1;
    private JTable tablaHorarios;
    private JLabel label15;
    private JLabel labelTurno;
    private JPanel panelRetardos;
    private JScrollPane scrollPane4;
    private JTable table2;
    private JLabel nombre2;
    private JLabel fechaact2;
    private JLabel label14;
    private JLabel label4;
    private JLabel label6;
    private JLabel label21;
    private JPanel panel4;
    private JLabel minacum2;
    private JPanel panel6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}