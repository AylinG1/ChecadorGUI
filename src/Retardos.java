import java.awt.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.*;
/*
 * Created by JFormDesigner on Wed Jul 23 16:28:00 GMT-06:00 2025
 */



/**
 * @author aylin
 */
public class Retardos extends JFrame {
    public Retardos() {
        initComponents();
        //fecha
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaActual.format(formato);
        fechaact.setText("Fecha: " + fechaFormateada);
        //nombre
        nombre.setText(SesionUsuario.usuarioActual);

        //llenar tabla
        cargarRetardos();
    }

    public void cargarRetardos() {
        String usuario = nombre.getText();
        String sql =
                "SELECT rc.fecha, t.entrada_1 AS entrada_valida, rc.hora, " +
                        "DATEDIFF(MINUTE, t.entrada_1, rc.hora) AS minutos_retraso, rc.tipo_registro " +
                        "FROM Registros_Checada rc " +
                        "JOIN Empleados e ON rc.id_empleado = e.id " +
                        "JOIN Turnos t ON e.id_turno = t.id " +
                        "WHERE e.nombre = ? AND t.entrada_1 IS NOT NULL AND rc.hora > t.entrada_1 " +
                        "UNION ALL " +
                        "SELECT rc.fecha, t.entrada_2 AS entrada_valida, rc.hora, " +
                        "DATEDIFF(MINUTE, t.entrada_2, rc.hora) AS minutos_retraso, rc.tipo_registro " +
                        "FROM Registros_Checada rc " +
                        "JOIN Empleados e ON rc.id_empleado = e.id " +
                        "JOIN Turnos t ON e.id_turno = t.id " +
                        "WHERE e.nombre = ? AND t.entrada_2 IS NOT NULL AND rc.hora > t.entrada_2 " +
                        "UNION ALL " +
                        "SELECT rc.fecha, t.entrada_3 AS entrada_valida, rc.hora, " +
                        "DATEDIFF(MINUTE, t.entrada_3, rc.hora) AS minutos_retraso, rc.tipo_registro " +
                        "FROM Registros_Checada rc " +
                        "JOIN Empleados e ON rc.id_empleado = e.id " +
                        "JOIN Turnos t ON e.id_turno = t.id " +
                        "WHERE e.nombre = ? AND t.entrada_3 IS NOT NULL AND rc.hora > t.entrada_3 " +
                        "ORDER BY entrada_valida";

        BaseSQL base = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            base = new BaseSQL();
            ps = base.conn.prepareStatement(sql);

            ps.setString(1, usuario);
            ps.setString(2, usuario);
            ps.setString(3, usuario);

            rs = ps.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[] {"Fecha", "Entrada Valida", "Hora", "Minutos Retraso", "Tipo Registro"}, 0);

            int minutosAcumulados = 0;

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getDate("fecha");
                fila[1] = rs.getTime("entrada_valida");
                fila[2] = rs.getTime("hora");
                int minutos = rs.getInt("minutos_retraso");
                fila[3] = minutos;
                fila[4] = rs.getString("tipo_registro");

                minutosAcumulados += minutos;

                model.addRow(fila);
            }

            table1.setModel(model);

            minacum.setText("Minutos acumulados: " + minutosAcumulados);

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



    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Retardos ventana = new Retardos();
            ventana.setVisible(true);
        });
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Juan
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        label1 = new JLabel();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        label2 = new JLabel();
        nombre = new JLabel();
        fechaact = new JLabel();
        button1 = new JButton();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        minacum = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== scrollPane1 ========
        {

            //---- textArea1 ----
            textArea1.setBackground(Color.lightGray);
            textArea1.setForeground(Color.pink);
            scrollPane1.setViewportView(textArea1);
        }
        contentPane.add(scrollPane1);
        scrollPane1.setBounds(440, 200, 290, 115);

        //---- label1 ----
        label1.setText("Motivo del retardo");
        contentPane.add(label1);
        label1.setBounds(440, 170, 150, label1.getPreferredSize().height);

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
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(20, 220, 385, 160);

        //---- label2 ----
        label2.setText("Mostrar retardos por semana:");
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(45, 75), label2.getPreferredSize()));

        //---- nombre ----
        nombre.setText("Nombre");
        nombre.setFont(new Font("Inter Semi Bold", Font.BOLD, 16));
        contentPane.add(nombre);
        nombre.setBounds(30, 10, 405, nombre.getPreferredSize().height);

        //---- fechaact ----
        fechaact.setText("dd/mm/aaaa");
        contentPane.add(fechaact);
        fechaact.setBounds(575, 10, 165, fechaact.getPreferredSize().height);

        //---- button1 ----
        button1.setText("Hacer un comentario");
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(440, 340), button1.getPreferredSize()));

        //---- label5 ----
        label5.setText("Resumen de checada");
        contentPane.add(label5);
        label5.setBounds(new Rectangle(new Point(45, 160), label5.getPreferredSize()));

        //---- label6 ----
        label6.setText("D\u00eda");
        contentPane.add(label6);
        label6.setBounds(new Rectangle(new Point(45, 190), label6.getPreferredSize()));

        //---- label7 ----
        label7.setText("Hora");
        contentPane.add(label7);
        label7.setBounds(new Rectangle(new Point(180, 190), label7.getPreferredSize()));

        //---- label8 ----
        label8.setText("Entrada/Salida");
        contentPane.add(label8);
        label8.setBounds(new Rectangle(new Point(250, 190), label8.getPreferredSize()));

        //---- minacum ----
        minacum.setText("text");
        contentPane.add(minacum);
        minacum.setBounds(275, 400, 120, minacum.getPreferredSize().height);

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
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JLabel label1;
    private JScrollPane scrollPane2;
    private JTable table1;
    private JLabel label2;
    private JLabel nombre;
    private JLabel fechaact;
    private JButton button1;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel minacum;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}