import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.sql.Time;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;




/**
 * @author JuanMa
 */
public class PrincipalSupervisor extends JFrame {
    public PrincipalSupervisor() {
        initComponents();
        agregarEventos();
        label16.setText(SesionUsuario.usuarioActual);// Añadimos eventos de clic a los labels
        mostrarPanel("card5");
    }

    public void cargarRegistros() {
        // === 1. Fecha del día en la UI ===
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault());
        fecharegistros.setText("Fecha: " + dtf.format(fechaActual));

        // === 2. Consulta SQL: obtener checadas de entrada con diferencia mínima por tipo de registro ===
        String sql =
                "SELECT e.id AS id_empleado, e.nombre, rc.tipo_registro, rc.hora AS hora_checada, " +
                        "  CASE " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_1' THEN t.entrada_1 " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_2' THEN t.entrada_2 " +
                        "    WHEN rc.tipo_registro = 'ENTRADA_3' THEN t.entrada_3 " +
                        "  END AS hora_esperada, " +
                        "  DATEDIFF(MINUTE, " +
                        "    CASE " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_1' THEN t.entrada_1 " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_2' THEN t.entrada_2 " +
                        "      WHEN rc.tipo_registro = 'ENTRADA_3' THEN t.entrada_3 " +
                        "    END, rc.hora) AS minutos_diferencia " +
                        "FROM Empleados e " +
                        "JOIN Turnos t ON e.id_turno = t.id " +
                        "LEFT JOIN Registros_Checada rc ON " +
                        "  rc.id_empleado = e.id AND rc.fecha = ? AND rc.tipo_registro LIKE 'ENTRADA_%' " +
                        "ORDER BY e.id, minutos_diferencia ASC";

        BaseSQL base = null;
        PreparedStatement psCount = null;
        PreparedStatement ps = null;
        ResultSet rsCount = null;
        ResultSet rs = null;

        // === Modelo de tabla para mostrar todos los registros del día (sin columna id) ===
        DefaultTableModel model = new DefaultTableModel(new String[] {
                "Nombre", "Tipo Registro", "Hora Checada", "Hora Esperada", "Min. Dif."
        }, 0);

        int totalEmpleados = 0;
        Map<Integer, Integer> mejoresDiferencias = new HashMap<>();

        try {
            base = new BaseSQL();

            // === 3. Contar el total de empleados registrados ===
            psCount = base.conn.prepareStatement("SELECT COUNT(*) FROM Empleados");
            rsCount = psCount.executeQuery();
            if (rsCount.next()) {
                totalEmpleados = rsCount.getInt(1);
            }

            // === 4. Ejecutar consulta con la fecha actual ===
            ps = base.conn.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(fechaActual));
            rs = ps.executeQuery();

            // === 5. Recorrer cada fila de resultado ===
            while (rs.next()) {
                int id     = rs.getInt("id_empleado");       // para acumulado interno
                String nombre = rs.getString("nombre");
                String tipo   = rs.getString("tipo_registro");
                Time horaCheque = rs.getTime("hora_checada");
                Time horaEsper  = rs.getTime("hora_esperada");
                int dif = rs.getInt("minutos_diferencia");

                if (!rs.wasNull()) {
                    Integer prev = mejoresDiferencias.get(id);
                    if (prev == null || Math.abs(dif) < Math.abs(prev)) {
                        mejoresDiferencias.put(id, dif);
                    }
                }

                // === llenar la fila visible (sin idEmpleado) ===
                model.addRow(new Object[]{ nombre, tipo, horaCheque, horaEsper, dif });
            }

            // === 6. Clasificar asistencias, retardos y faltas ===
            int asist = 0, ret = 0, fal = 0;
            for (Integer minutos : mejoresDiferencias.values()) {
                if (minutos <= 0) {
                    asist++;
                } else if (minutos < 5) {
                    ret++;
                } else {
                    fal++;
                }
            }
            // Los que no marcaron se consideran falta automáticamente
            fal += (totalEmpleados - mejoresDiferencias.size());

            // === 7. Calcular porcentajes ===
            double pctAs = totalEmpleados > 0 ? 100.0 * asist / totalEmpleados : 0.0;
            double pctRet = totalEmpleados > 0 ? 100.0 * ret   / totalEmpleados : 0.0;
            double pctFal = totalEmpleados > 0 ? 100.0 * fal   / totalEmpleados : 0.0;

            labelAsistenciasTotales.setText(asist + "/" + totalEmpleados);
            labelAsistenciasPorcentaje.setText(String.format("%.1f%%", pctAs));
            labelRetardosTotales.setText(ret + "/" + totalEmpleados);
            labelRetardosPorcentaje.setText(String.format("%.1f%%", pctRet));
            labelFaltasTotales.setText(fal + "/" + totalEmpleados);
            labelFaltasPorcentaje.setText(String.format("%.1f%%", pctFal));

            // === 8. Asignar el modelo a la JTable visible ===
            tablaRegistros.setModel(model);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar registros: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception _ex) {}
            try { if (ps != null) ps.close(); } catch (Exception _ex) {}
            try { if (rsCount != null) rsCount.close(); } catch (Exception _ex) {}
            try { if (psCount != null) psCount.close(); } catch (Exception _ex) {}
            try { if (base != null) base.cerrar(); } catch (Exception _ex) {}
        }
    }



    public void cargarAlertasDeRetardos() {
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaActual.format(formato);
        fechaact.setText("Fecha: " + fechaFormateada);
        String sql = "SELECT e.nombre, r.minutos_acumulados " +
                "FROM Retardos r " +
                "JOIN Empleados e ON r.id_empleado = e.id " +
                "WHERE r.minutos_acumulados > 15";

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            base = new BaseSQL();
            ps = base.conn.prepareStatement(sql);
            rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Nombre", "Minutos Acumulados"}, 0);

            while (rs.next()) {
                Object[] fila = new Object[2];
                fila[0] = rs.getString("nombre");
                fila[1] = rs.getInt("minutos_acumulados");
                model.addRow(fila);
            }

            tablaAlertas.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar alertas de retardos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try { if (rs != null) rs.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            try { if (ps != null) ps.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            try { if (base != null) base.cerrar(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
    }


    private void agregarEventos() {
        // Opcional: cambiar cursor a "mano" al pasar sobre los labels
        label3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Agregamos eventos a cada label para cambiar de panel
        label3.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card2"); // panelReportes
            }
        });

        label4.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card3"); // panelRegistros
                cargarRegistros();
            }
        });

        label5.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mostrarPanel("card4"); // panelAlertas
                cargarAlertasDeRetardos();
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

    private void reporteMouseClicked(MouseEvent e) {
        // TODO add your code here
        mostrarPanel("card2");
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
        label6 = new JLabel();
        label1 = new JLabel();
        label11 = new JLabel();
        label12 = new JLabel();
        label13 = new JLabel();
        label16 = new JLabel();
        panelInicio = new JPanel();
        panelReportes = new JPanel();
        label10 = new JLabel();
        label15 = new JLabel();
        comboBox1 = new JComboBox();
        textField1 = new JTextField();
        button1 = new JButton();
        label17 = new JLabel();
        label18 = new JLabel();
        label19 = new JLabel();
        label20 = new JLabel();
        label21 = new JLabel();
        panelRegistros = new JPanel();
        label7 = new JLabel();
        panel2 = new JPanel();
        label14 = new JLabel();
        labelAsistenciasTotales = new JLabel();
        labelAsistenciasPorcentaje = new JLabel();
        panel3 = new JPanel();
        labelRetardosTotales = new JLabel();
        label26 = new JLabel();
        labelRetardosPorcentaje = new JLabel();
        panel5 = new JPanel();
        labelFaltasTotales = new JLabel();
        label27 = new JLabel();
        labelFaltasPorcentaje = new JLabel();
        panel7 = new JPanel();
        scrollPane1 = new JScrollPane();
        tablaRegistros = new JTable();
        label9 = new JLabel();
        fecharegistros = new JLabel();
        label30 = new JLabel();
        panelAlertas = new JPanel();
        label8 = new JLabel();
        scrollPane2 = new JScrollPane();
        tablaAlertas = new JTable();
        reporte = new JButton();
        fechaact = new JLabel();
        panel1 = new JPanel();

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
                label4.setBounds(55, 135, 70, label4.getPreferredSize().height);

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

                //---- label16 ----
                label16.setText("SUPERVISOR");
                label16.setForeground(new Color(0xff6633));
                label16.setFont(new Font("Segoe UI Black", Font.BOLD, 14));
                panelMenu.add(label16);
                label16.setBounds(10, 10, 120, 20);

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
                    panelReportes.add(label15);
                    label15.setBounds(new Rectangle(new Point(115, 175), label15.getPreferredSize()));
                    panelReportes.add(comboBox1);
                    comboBox1.setBounds(new Rectangle(new Point(40, 110), comboBox1.getPreferredSize()));
                    panelReportes.add(textField1);
                    textField1.setBounds(55, 220, 120, 50);

                    //---- button1 ----
                    button1.setText("Enviar");
                    panelReportes.add(button1);
                    button1.setBounds(new Rectangle(new Point(295, 255), button1.getPreferredSize()));

                    //---- label17 ----
                    label17.setText("Escribe el reporte aqu\u00ed");
                    panelReportes.add(label17);
                    label17.setBounds(new Rectangle(new Point(35, 195), label17.getPreferredSize()));

                    //---- label18 ----
                    label18.setText("empleado");
                    panelReportes.add(label18);
                    label18.setBounds(new Rectangle(new Point(155, 110), label18.getPreferredSize()));

                    //---- label19 ----
                    label19.setText("minutos de retardo");
                    panelReportes.add(label19);
                    label19.setBounds(new Rectangle(new Point(260, 110), label19.getPreferredSize()));

                    //---- label20 ----
                    label20.setText("No. de semana");
                    panelReportes.add(label20);
                    label20.setBounds(new Rectangle(new Point(155, 135), label20.getPreferredSize()));

                    //---- label21 ----
                    label21.setText("d\u00edas");
                    panelReportes.add(label21);
                    label21.setBounds(new Rectangle(new Point(265, 140), label21.getPreferredSize()));

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
                    panelRegistros.setBackground(new Color(0xff9966));
                    panelRegistros.setLayout(null);

                    //---- label7 ----
                    label7.setText("Registros del D\u00eda");
                    label7.setForeground(Color.black);
                    label7.setFont(new Font("Inter", Font.PLAIN, 16));
                    panelRegistros.add(label7);
                    label7.setBounds(15, 165, 160, 25);

                    //======== panel2 ========
                    {
                        panel2.setBackground(new Color(0xccffcc));
                        panel2.setLayout(null);

                        //---- label14 ----
                        label14.setText("Asistencias");
                        label14.setFont(new Font("Inter", Font.PLAIN, 16));
                        panel2.add(label14);
                        label14.setBounds(10, 5, 105, label14.getPreferredSize().height);

                        //---- labelAsistenciasTotales ----
                        labelAsistenciasTotales.setText("text");
                        panel2.add(labelAsistenciasTotales);
                        labelAsistenciasTotales.setBounds(10, 30, 105, 17);

                        //---- labelAsistenciasPorcentaje ----
                        labelAsistenciasPorcentaje.setText("text");
                        panel2.add(labelAsistenciasPorcentaje);
                        labelAsistenciasPorcentaje.setBounds(10, 50, 100, 17);

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
                    panelRegistros.add(panel2);
                    panel2.setBounds(30, 80, 130, 75);

                    //======== panel3 ========
                    {
                        panel3.setBackground(new Color(0xccffcc));
                        panel3.setLayout(null);

                        //---- labelRetardosTotales ----
                        labelRetardosTotales.setText("text");
                        panel3.add(labelRetardosTotales);
                        labelRetardosTotales.setBounds(15, 30, 95, labelRetardosTotales.getPreferredSize().height);

                        //---- label26 ----
                        label26.setText("Retardos");
                        label26.setFont(new Font("Inter", Font.PLAIN, 16));
                        panel3.add(label26);
                        label26.setBounds(10, 5, 105, 20);

                        //---- labelRetardosPorcentaje ----
                        labelRetardosPorcentaje.setText("text");
                        panel3.add(labelRetardosPorcentaje);
                        labelRetardosPorcentaje.setBounds(15, 50, 100, 16);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panel3.getComponentCount(); i++) {
                                Rectangle bounds = panel3.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panel3.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panel3.setMinimumSize(preferredSize);
                            panel3.setPreferredSize(preferredSize);
                        }
                    }
                    panelRegistros.add(panel3);
                    panel3.setBounds(245, 80, 130, 75);

                    //======== panel5 ========
                    {
                        panel5.setBackground(new Color(0xccffcc));
                        panel5.setLayout(null);

                        //---- labelFaltasTotales ----
                        labelFaltasTotales.setText("text");
                        panel5.add(labelFaltasTotales);
                        labelFaltasTotales.setBounds(10, 30, 105, labelFaltasTotales.getPreferredSize().height);

                        //---- label27 ----
                        label27.setText("Faltas");
                        label27.setFont(new Font("Inter", Font.PLAIN, 16));
                        panel5.add(label27);
                        label27.setBounds(10, 5, 105, 20);

                        //---- labelFaltasPorcentaje ----
                        labelFaltasPorcentaje.setText("text");
                        panel5.add(labelFaltasPorcentaje);
                        labelFaltasPorcentaje.setBounds(10, 50, 105, 16);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panel5.getComponentCount(); i++) {
                                Rectangle bounds = panel5.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panel5.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panel5.setMinimumSize(preferredSize);
                            panel5.setPreferredSize(preferredSize);
                        }
                    }
                    panelRegistros.add(panel5);
                    panel5.setBounds(450, 80, 130, 75);

                    //======== panel7 ========
                    {
                        panel7.setBackground(new Color(0xccffcc));
                        panel7.setLayout(null);

                        //======== scrollPane1 ========
                        {
                            scrollPane1.setViewportView(tablaRegistros);
                        }
                        panel7.add(scrollPane1);
                        scrollPane1.setBounds(10, 10, 565, 255);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < panel7.getComponentCount(); i++) {
                                Rectangle bounds = panel7.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = panel7.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            panel7.setMinimumSize(preferredSize);
                            panel7.setPreferredSize(preferredSize);
                        }
                    }
                    panelRegistros.add(panel7);
                    panel7.setBounds(15, 195, 590, 275);

                    //---- label9 ----
                    label9.setText("Resumen estad\u00edstico del d\u00eda");
                    label9.setForeground(Color.black);
                    label9.setFont(new Font("Inter", Font.PLAIN, 16));
                    panelRegistros.add(label9);
                    label9.setBounds(15, 45, 275, 25);

                    //---- fecharegistros ----
                    fecharegistros.setText("text");
                    panelRegistros.add(fecharegistros);
                    fecharegistros.setBounds(485, 10, 115, fecharegistros.getPreferredSize().height);

                    //---- label30 ----
                    label30.setText("REGISTROS");
                    label30.setForeground(Color.black);
                    label30.setFont(new Font("Inter", Font.PLAIN, 20));
                    panelRegistros.add(label30);
                    label30.setBounds(15, 10, 275, 25);

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
                    panelAlertas.setBackground(new Color(0xff9966));
                    panelAlertas.setLayout(null);

                    //---- label8 ----
                    label8.setText("Alertas");
                    label8.setForeground(Color.black);
                    label8.setFont(new Font("Inter", Font.PLAIN, 16));
                    panelAlertas.add(label8);
                    label8.setBounds(30, 75, 102, 16);

                    //======== scrollPane2 ========
                    {

                        //---- tablaAlertas ----
                        tablaAlertas.setModel(new DefaultTableModel(
                            new Object[][] {
                                {null, null},
                                {null, null},
                                {null, null},
                            },
                            new String[] {
                                "Nombre", "Minutos de retardo"
                            }
                        ));
                        scrollPane2.setViewportView(tablaAlertas);
                    }
                    panelAlertas.add(scrollPane2);
                    scrollPane2.setBounds(55, 115, 435, 265);

                    //---- reporte ----
                    reporte.setText("Generar un reporte");
                    reporte.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            reporteMouseClicked(e);
                        }
                    });
                    panelAlertas.add(reporte);
                    reporte.setBounds(415, 400, 170, reporte.getPreferredSize().height);

                    //---- fechaact ----
                    fechaact.setText("text");
                    fechaact.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
                    panelAlertas.add(fechaact);
                    fechaact.setBounds(445, 25, 145, 21);

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

                //======== panel1 ========
                {
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
                panelInicio.add(panel1, "card5");
            }
            panel4.add(panelInicio, BorderLayout.CENTER);
        }
        contentPane.add(panel4);
        panel4.setBounds(5, 0, 765, 530);

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
    private JLabel label6;
    private JLabel label1;
    private JLabel label11;
    private JLabel label12;
    private JLabel label13;
    private JLabel label16;
    private JPanel panelInicio;
    private JPanel panelReportes;
    private JLabel label10;
    private JLabel label15;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JButton button1;
    private JLabel label17;
    private JLabel label18;
    private JLabel label19;
    private JLabel label20;
    private JLabel label21;
    private JPanel panelRegistros;
    private JLabel label7;
    private JPanel panel2;
    private JLabel label14;
    private JLabel labelAsistenciasTotales;
    private JLabel labelAsistenciasPorcentaje;
    private JPanel panel3;
    private JLabel labelRetardosTotales;
    private JLabel label26;
    private JLabel labelRetardosPorcentaje;
    private JPanel panel5;
    private JLabel labelFaltasTotales;
    private JLabel label27;
    private JLabel labelFaltasPorcentaje;
    private JPanel panel7;
    private JScrollPane scrollPane1;
    private JTable tablaRegistros;
    private JLabel label9;
    private JLabel fecharegistros;
    private JLabel label30;
    private JPanel panelAlertas;
    private JLabel label8;
    private JScrollPane scrollPane2;
    private JTable tablaAlertas;
    private JButton reporte;
    private JLabel fechaact;
    private JPanel panel1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
