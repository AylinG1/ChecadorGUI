import java.awt.*;
import java.awt.event.*;
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




public class Retardos extends JFrame {


    public Retardos() {
        initComponents();
        //fecha
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaActual.format(formato);
        fechaact.setText("Fecha: " + fechaFormateada);
        //nombre
        String nombreUsuario = SesionUsuario.usuarioActual;
        nombre.setText("Bienvenido, " + nombreUsuario);


    }

    public static void main(String[] args) {

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
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
        scrollPane2.setBounds(45, 220, 300, 160);

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
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
